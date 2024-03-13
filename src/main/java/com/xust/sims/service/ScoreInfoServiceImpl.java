package com.xust.sims.service;

import com.xust.sims.dao.CourseMapper;
import com.xust.sims.entity.Course;
import com.xust.sims.exceldatalistener.StudentDataListener;
import com.xust.sims.service.CourseService;
import com.xust.sims.dao.ScoreMapper;
import com.xust.sims.dto.ScoreExcelData;
import com.xust.sims.dto.ScoreInfoQuery;
import com.xust.sims.dto.ScoreInsert;
import com.xust.sims.dto.StudentExcelData;
import com.xust.sims.entity.Score;
import com.alibaba.excel.EasyExcel;
import com.xust.sims.dao.StudentMapper;
import com.xust.sims.entity.Student;
import com.xust.sims.service.StudentInfoService;
import com.xust.sims.exceldatalistener.ScoreDataListener;
import com.xust.sims.web.exception.CourseNotFoundException;
import com.xust.sims.web.exception.ScoreInfoInsertException;
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
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class ScoreInfoServiceImpl implements ScoreInfoService {
    @Autowired
    private ScoreMapper scoreMapper;
    @Autowired
    private StudentInfoService studentInfoService;
    @Autowired
    private CourseService courseService;
    @Autowired
    private StudentMapper studentMapper;
    @Autowired
    private CourseMapper courseMapper;
    @Autowired
    private JmsMessagingTemplate messagingTemplate;

    // 生成错误信息
    @Getter
    private final List<ScoreExcelData> errorList = new ArrayList<>();

    public List<Score> getScoreByQueryInfo(ScoreInfoQuery query) {
        if (StringUtils.isEmpty(query.getEndTime())){
            query.setEndTime(null);
        }
        if (StringUtils.isEmpty(query.getStartTime())){
            query.setStartTime(null);
        }
        List<Score> scoreByCondition = scoreMapper.findScoreByCondition(query);
        for (Score score : scoreByCondition) {

            Course courseBaseInfoById = courseMapper.findCourseById(score.getCourseId());
            score.setCourse(courseBaseInfoById);

            Student studentDetailsById = studentMapper.findStudentDetailsById(score.getStudentId());
            score.setStudent(studentDetailsById);
        }

        return scoreByCondition;
    }

    // 获取所有的成绩信息
    @Override
    public List<Score> getAllScoreInfo() {
        return scoreMapper.findAllScoreInfoDetails();
    }

    // 通过成绩的id来查找具体的成绩
    @Override
    public Score getScoreInfoById(Long id) {
        return scoreMapper.findScoreInfoById(id);
    }

    // 通过学生的id和课程的id来查找成绩信息
    @Override
    public Score getScoreInfoByStudentAndCourse(String studentId, Integer courseId) {
        return scoreMapper.findScoreInfoByStudentAndCourse(studentId, courseId);
    }

    @Override
    public List<Score> getScoreByIds(int[] ids) {
        if (ids.length > 0) {
            List<Integer> idsCollection = new ArrayList<>();
            for (int id : ids) {
                idsCollection.add(id);
            }
            return scoreMapper.findAllScoreByIds(idsCollection);
        }
        return new ArrayList<>();
    }

    // 添加单一的一个成绩信息
    @Override
    public void addOneScoreInsertInfo(ScoreInsert scoreInsert) {
        // 为了先处理插入成绩后，生成id的问题
        // 先对考试日期进行数据类型的转换
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");
        String year = dateFormat.format(scoreInsert.getExamTime());
        String studentId = scoreInsert.getStudentId();
        // 这里设置学生id只获取后面4位
        String studentIdFour = studentId.substring(studentId.length() - 4);
        String courseId = scoreInsert.getCourseId().toString();
        String combined = studentIdFour + courseId + year;
        // 这里进行数据类型转换
        Long scoreId = (long) combined.hashCode();
        // 设置id到scoreInsert
        scoreInsert.setId(scoreId);
        // 将成绩添加到我们的数据库当中
        scoreMapper.insertOneScoreInfo(scoreInsert);
        String grade = scoreInsert.getGrade();

        messagingTemplate.convertAndSend("com.xust.score.welcome",
                new Score(scoreInsert.getId(), scoreInsert.getGrade()));
    }

    // 添加多条成绩信息
    @Override
    public void addBatchScoreInsertInfo(List<Score> scores) {
        for (Score score : scores) {
            messagingTemplate.convertAndSend("com.xust.score.welcome", score);
        }
    }

    // 处理成绩的Excel数据
    @Override
    public void processScoreExcelData(List<ScoreExcelData> dataList) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");
        boolean existError = false;

        for (ScoreExcelData excelData : dataList) {
            try {
                // 获取课程信息的逻辑，可以根据课程名称获取课程ID
                Integer courseId = Integer.parseInt(excelData.getCourseId());
                String studentId = excelData.getStudentId();
                if (courseId == null || studentId == null) {
                    throw new CourseNotFoundException();
                } else {
                    excelData.setCourseId(String.valueOf(courseId));
                    excelData.setStudentId(studentId);
                }

                // 处理考试时间等数据，校验成绩的有效性等操作...

                // 生成成绩ID的逻辑
                String year = dateFormat.format(excelData.getExamTime());
                String studentIdFour = studentId.substring(studentId.length() - 4);
                String combined = studentIdFour + courseId + year;
                // 使用 UUID 生成唯一的ID
                Long scoreId = (long) combined.hashCode();
                excelData.setId(scoreId);

                // 处理其他数据校验、逻辑等...

            } catch (CourseNotFoundException e) {
                // 处理异常逻辑，记录错误数据等...
                errorList.add(excelData);
                existError = true;
            }
            // 可能的其他逻辑...

            if (!existError) {
                // 如果没有错误，可以继续处理其他逻辑，比如存储到数据库等...

            }
        }
    }

    // 保存Excel数据
    @Override
    public void saveExcelData(List<ScoreExcelData> list) throws Exception {
        if (errorList.size() > 0) {
            list.removeAll(errorList);
            addErrorMessage(errorList);
        }
        try {
            processScoreExcelData(list);
            scoreMapper.insertBatchScoreInfos(list);

            // 做相关处理（例如生成密码、注册用户等），此处略去

            // 发送欢迎消息
            // addBatchScoreInsertInfo(scores);
        } catch (Exception e) {
            log.info("Collections of score information with error information: {}", errorList);
            log.info("Score information Add exception collection: {}", list);
            log.info("The abnormal information is: {}", e.getMessage());
            addBusyMessage(list);
            errorList.addAll(list);
            throw new ScoreInfoInsertException();
        }
    }

    @Override
    public List<ScoreExcelData> saveScoreExcelData(MultipartFile[] files) throws Exception {
        errorList.clear();
        List<ScoreExcelData> res = new ArrayList<>();
        for (MultipartFile file : files) {
            EasyExcel.read(file.getInputStream(), ScoreExcelData.class, new ScoreDataListener(this))
                    .sheet().doRead();
            res.addAll(errorList);
            errorList.clear();
        }
        return res;
    }

    // 更新成绩信息
    @Override
    public void updateScoreInsertInfo(Score score) {
        score.setUpdateTime(new Date());
        scoreMapper.updateScoreInfo(score);
    }

    // 删除成绩信息
    @Override
    public void deleteScoreInfo(Long id) {
        scoreMapper.deleteScoreInfo(id);
    }

    private void addErrorMessage(List<ScoreExcelData> errorList) {
        for (ScoreExcelData excelData : errorList) {
            if (StringUtils.isEmpty(excelData.getDescription())) {
                excelData.setDescription("Please check whether the relevant college, major, class information is correct!!");
            }
        }
    }

    private void addBusyMessage(List<ScoreExcelData> dataList) {
        for (ScoreExcelData excelData : dataList) {
            excelData.setDescription("Add exception, please try again later!!");
            excelData.setId(null);
        }
    }


}