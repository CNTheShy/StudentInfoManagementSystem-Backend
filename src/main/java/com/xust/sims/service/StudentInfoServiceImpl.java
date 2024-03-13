package com.xust.sims.service;

import com.alibaba.excel.EasyExcel;
import com.xust.sims.dao.StudentMapper;
import com.xust.sims.dao.MajorMapper;
import com.xust.sims.dto.StudentExcelData;
import com.xust.sims.dto.StudentInfoQuery;
import com.xust.sims.dto.StudentInsert;
import com.xust.sims.entity.Class;
import com.xust.sims.entity.Registry;
import com.xust.sims.entity.Student;
import com.xust.sims.exceldatalistener.StudentDataListener;
import com.xust.sims.web.exception.SchoolInfoNotFoundException;
import com.xust.sims.web.exception.StudentInfoInsertException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@Service
@Slf4j
public class StudentInfoServiceImpl implements StudentInfoService {
    @Autowired
    private StudentMapper studentMapper;
    @Autowired
    private MajorMapper majorMapper;
    @Autowired
    private JmsMessagingTemplate messagingTemplate;
    @Autowired
    private SchoolInfoService schoolInfoService;
    @Autowired
    private UserService userService;
    @Getter
    private final List<StudentExcelData> errorList = new ArrayList<>();

    private final Map<Integer, Integer> classSizeMap = new ConcurrentHashMap<>();

    @Override
    public List<Student> getStudentByQueryInfo(StudentInfoQuery query) {
        return studentMapper.findStudentByCondition(query);
    }

    @Override
    public List<Student> getStudentByIds(int[] ids) {
        if (ids.length > 0) {
            List<Integer> idsCollection = new ArrayList<>();
            for (int id : ids) {
                idsCollection.add(id);
            }
            return studentMapper.findStudentByIds(idsCollection);
        }
        return new ArrayList<>();
    }

    @Override
    public List<Student> getStudentByName(String[] name) {
        return null;
    }

    @Override
    public Student getStudentInfoDetailsById(String id) {
        return studentMapper.findStudentDetailsById(id);
    }

    @Override
    public Student getStudentInfoDetailsByName(String name) {
        return null;
    }

    @Override
    public String getStudentIdByName(String name) {
        return null;
    }

    @Override
    public boolean querySelectFlag(String studentId) {
        return studentMapper.getSelectFlagByStudentId(studentId);
    }

    @Override
    public void addOneStudentInfo(Student student) {
        messagingTemplate.convertAndSend("com.xust.student.welcome", student);
    }

    @Override
    public void addBatchStudentInfo(List<Student> students) {
        for (Student student : students) {
            messagingTemplate.convertAndSend("com.xust.student.welcome", student);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void addOneStudentInsertInfo(StudentInsert studentInsert) {
        //生成相应的学号
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");
        String year = dateFormat.format(studentInsert.getCreateTime());
        int count = majorMapper.getStudentNumberByMajorId(studentInsert.getMajorId()) + 1;
        //学生id的前两位代表年份
        String id = year.substring(year.length() - 2);
        //2位数代表学院
        if (studentInsert.getAcademyId() <= 9) {
            id += "0" + studentInsert.getAcademyId();
        } else {
            id += studentInsert.getAcademyId();
        }
        //3位数代表专业
        if (studentInsert.getMajorId() <= 9) {
            id += "00" + studentInsert.getMajorId();
        } else if (studentInsert.getMajorId() <= 99) {
            id += "0" + studentInsert.getMajorId();
        } else {
            id += studentInsert.getMajorId();
        }
        //每年每专业最多999名新生，超出上限就会让id长度变多
        if (count <= 9) {
            id += "00" + count;
        } else if (count <= 99) {
            id += "0" + count;
        } else {
            id += count;
        }
        studentInsert.setId(id);
        studentMapper.insertOneStudentInfo(studentInsert);
        String idCard = studentInsert.getIdCard();
        Registry registry = new Registry(studentInsert.getId(),
                idCard.substring(idCard.length() - 6), studentInsert.getName(), 2);
        userService.registryUser(registry);
        messagingTemplate.convertAndSend("com.xust.student.welcome",
                new Student(studentInsert.getId(), studentInsert.getName(), studentInsert.getIdCard(), studentInsert.getEmail()));
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void processStudentExcelData(List<StudentExcelData> dataList) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");
        boolean existError = false;
        for (StudentExcelData excelData : dataList) {
            try {
                //获取相关学院、专业、班级信息(id)
                Integer academyId = schoolInfoService.getAcademyIdByName(excelData.getAcademyName());
                Integer majorId = schoolInfoService.getMajorIdByName(excelData.getMajorName());
                Integer classId = schoolInfoService.getClassIdByName(excelData.getMajorName(), excelData.getClassName());
                if (academyId == null || majorId == null || classId == null) {
                    throw new SchoolInfoNotFoundException();
                } else {
                    excelData.setAcademyId(academyId);
                    excelData.setMajorId(majorId);
                    excelData.setClassId(classId);
                }
                //生成相应的学工号
                String year = dateFormat.format(excelData.getCreateTime());
                String className = excelData.getClassName();
                String id = year.substring(year.length() - 2) +
                        academyId +
                        majorId +
                        className.substring(className.length() - 2);
                int count = getClassPersonAndIncrement(classId);
                if (count <= 9) {
                    id += "0" + count;
                } else {
                    id += count;
                }
                excelData.setId(id);
            } catch (SchoolInfoNotFoundException e) {
                errorList.add(excelData);
                existError = true;
            }
            //最后更新相关的班级人数
            if (!existError) {
                schoolInfoService.updateClassSize(classSizeMap);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void saveExcelData(List<StudentExcelData> list) throws Exception {
        if (errorList.size() > 0) {
            list.removeAll(errorList);
            addErrorMessage(errorList);
        }
        try {
            processStudentExcelData(list);
            studentMapper.insertBatchStudentInfos(list);
            //初始相关密码
            List<Registry> registries = new ArrayList<>();
            List<Student> students = new ArrayList<>();
            for (StudentExcelData excelData : list) {
                String idCard = excelData.getIdCard();
                String password = idCard.substring(idCard.length() - 6);
                Registry registry = new Registry(excelData.getId(), password, excelData.getName(), 2);
                registries.add(registry);
                students.add(new Student(excelData.getId(), excelData.getName(), excelData.getIdCard(), excelData.getEmail()));
            }
            //添加注册信息
            userService.registryUser(registries);
            //发送欢迎邮件
            addBatchStudentInfo(students);
        } catch (Exception e) {
            log.info("Collections of student information with error information:{}", errorList);
            log.info("Student information Add exception collection:{}", list);
            log.info("The abnormal information is:{}", e.getMessage());
            addBusyMessage(list);
            errorList.addAll(list);
            throw new StudentInfoInsertException();
        }
    }

    @Override
    public List<StudentExcelData> saveStudentExcelData(MultipartFile[] files) throws Exception {
        errorList.clear();
        List<StudentExcelData> res = new ArrayList<>();
        for (MultipartFile file : files) {
            EasyExcel.read(file.getInputStream(), StudentExcelData.class, new StudentDataListener(this))
                    .sheet().doRead();
            res.addAll(errorList);
            errorList.clear();
        }
        return res;
    }

    @Override
    public void updateStudentInfo(Student student) {
        student.setUpdateTime(new Date());
        studentMapper.updateStudentInfo(student);
    }

    private void addErrorMessage(List<StudentExcelData> errorList) {
        for (StudentExcelData excelData : errorList) {
            if (StringUtils.isEmpty(excelData.getDescription())) {
                excelData.setDescription("Please check whether the relevant college, major, class information is correct!!");
            }
        }
    }

    private void addBusyMessage(List<StudentExcelData> dataList) {
        for (StudentExcelData excelData : dataList) {
            excelData.setDescription("Add exception, please try again later!!");
            excelData.setId(null);
        }
    }

    private int getClassPersonAndIncrement(Integer classId) {
        if (classSizeMap.containsKey(classId)) {
            classSizeMap.put(classId, classSizeMap.get(classId) + 1);
        } else {
            classSizeMap.put(classId, schoolInfoService.getClassSize(classId) + 1);
        }
        return classSizeMap.get(classId);
    }

}
