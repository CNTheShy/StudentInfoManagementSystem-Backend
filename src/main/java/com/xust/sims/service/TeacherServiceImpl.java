package com.xust.sims.service;

import com.xust.sims.dao.TeacherMapper;
import com.xust.sims.dto.TeacherInsert;
import com.xust.sims.dto.TeacherInfoQuery;
import com.xust.sims.entity.*;
import com.xust.sims.entity.Class;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


@Service
public class TeacherServiceImpl implements TeacherService {
    @Autowired
    private TeacherMapper teacherMapper;
    @Autowired
    private JmsMessagingTemplate messagingTemplate;
    @Autowired
    private SchoolInfoService schoolInfoService;
    @Autowired
    private UserService userService;

    @Override
    public List<Teacher> getTeacherInfoByAcademyId(Integer academyId) {
        return teacherMapper.getTeacherInfoByAcademyId(academyId);
    }

    @Override
    public List<Academy> getAcademyWithTeacherInfo() {
        return teacherMapper.getAcademyWithTeacherInfo();
    }

    @Override
    public List<Teacher> getTeacherByQueryInfo(TeacherInfoQuery query) {
        return teacherMapper.findTeacherByCondition(query);
    }

    @Override
    public Teacher getTeacherInfoDetailsById(String id) {
        return teacherMapper.getTeacherInfoById(id);
    }

    @Override
    public void addOneTeacherInfo(Teacher teacher) {
        messagingTemplate.convertAndSend("com.xust.teacher.welcome", teacher);
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void addOneTeacherInsertInfo(TeacherInsert teacherInsert) {
        //生成相应的学号
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");
        String year = dateFormat.format(teacherInsert.getCreateTime());
        String id = year.substring(year.length() - 2) + teacherInsert.getAcademyId();
        List<Teacher> teachers = getTeacherInfoByAcademyId(teacherInsert.getAcademyId());
        int count = teachers.size();
        if (count <= 9) {
            id += "0" + count;
        } else {
            id += count;
        }
        teacherInsert.setId(id);
        teacherMapper.insertOneTeacherInfo(teacherInsert);
        String idCard = teacherInsert.getIdCard();
        Registry registry = new Registry(teacherInsert.getId(),
                idCard.substring(idCard.length() - 6), teacherInsert.getName(), 3);
        userService.registryUser(registry);
        Teacher payload = new Teacher(teacherInsert.getId(), teacherInsert.getName(), teacherInsert.getIdCard(), teacherInsert.getEmail());
        messagingTemplate.convertAndSend("com.xust.teacher.welcome", payload.toString());
    }

    @Override
    public void updateTeacherInfo(Teacher teacher) {
        teacher.setUpdateTime(new Date());
        teacherMapper.updateTeacherInfo(teacher);
    }
}
