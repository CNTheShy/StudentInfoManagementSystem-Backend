package com.xust.sims.service;

import com.xust.sims.entity.Academy;
import com.xust.sims.entity.Teacher;
import com.xust.sims.dto.TeacherInsert;
import com.xust.sims.dto.TeacherInfoQuery;

import java.util.List;


public interface TeacherService {
    /**
     * 根据学院ID获取教师信息
     * @param academyId
     * @return
     */
    List<Teacher> getTeacherInfoByAcademyId(Integer academyId);

    /**
     * 获取学院-教师对应信息
     * @return
     */
    List<Academy> getAcademyWithTeacherInfo();
    /**
     * 根据查询条件来获取老师信息
     * @param query query condition
     * @return student's info
     */
    List<Teacher> getTeacherByQueryInfo(TeacherInfoQuery query);
    /**
     * 根据老师id获取详细信息
     * @param id
     * @return
     */
    Teacher getTeacherInfoDetailsById(String id);
    /**
     * 添加老师信息(单条)，发送一封欢迎邮件
     * @param teacher
     */
    void addOneTeacherInfo(Teacher teacher);
    /**
     * 根据单条的插入数据信息，进行录入
     * @param teacherInsert
     */
    void addOneTeacherInsertInfo(TeacherInsert teacherInsert);
    /**
     * 更新学生信息
     * @param teacher
     */
    void updateTeacherInfo(Teacher teacher);
}
