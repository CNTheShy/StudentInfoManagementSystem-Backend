package com.xust.sims.dao;

import com.xust.sims.dto.TeacherInsert;
import com.xust.sims.dto.TeacherInfoQuery;
import com.xust.sims.entity.Academy;
import com.xust.sims.entity.Student;
import com.xust.sims.entity.Teacher;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface TeacherMapper {
    /**
     * 根据学院信息获取教师信息
     * @param academyId
     * @return
     */
    List<Teacher> getTeacherInfoByAcademyId(Integer academyId);

    /**
     * 通过教师id获取教师信息
     * @param teacherId
     * @return
     */
    Teacher getTeacherInfoById(String teacherId);

    /**
     * 单条插入老师信息
     * @param teacherInsert 单条插入老师信息
     * @return effect rows
     */
    int insertOneTeacherInfo(TeacherInsert teacherInsert);

    /**
     * 获取学院、教师关系
     * @return
     */
    List<Academy> getAcademyWithTeacherInfo();

    /**
     * 根据查询条件来查询老师基础信息（除去课程、考试信息）
     * @param query 查询条件
     * @return 老师信息
     */
    List<Teacher> findTeacherByCondition(TeacherInfoQuery query);

    /**
     * 更新老师信息
     * @param teacher
     */
    void updateTeacherInfo(Teacher teacher);

    /**
     * 根据id来删除老师信息
     * @param id
     */
    void deleteTeacherInfo(String id);
}
