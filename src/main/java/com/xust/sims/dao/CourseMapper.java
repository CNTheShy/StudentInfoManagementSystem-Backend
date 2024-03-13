package com.xust.sims.dao;

import com.xust.sims.dto.NewEnroll;
import com.xust.sims.dto.ScheduleConfig;
import com.xust.sims.dto.SelectCourse;
import com.xust.sims.dto.StudentTimetable;
import com.xust.sims.entity.Course;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;


@Repository
public interface CourseMapper {
    /**
     * 增设课程
     * @param course
     */
    void addCourse(Course course);

    /**
     * 查询课程信息（通过条件）
     * @param selectCourse
     * @return
     */
    List<Course> findCourse(SelectCourse selectCourse);

    /**
     * 查询课程信息（通过学生id）
     * @param studentId 学生id
     * @return
     */
    List<StudentTimetable> findCourseByStudentId(String studentId);

    /**
     * 查询课程信息（通过名字）
     * @param courseName
     * @return
     */
    Course findCourseByName(String courseName);
    Integer findCourseIdByName(String courseName);

    /**
     * 查询课程信息（通过id）
     * @param courseId
     * @return
     */
    Course findCourseById(Integer courseId);

    List<Integer> findPreRequestById(Integer courseId);

    List<Integer> findCourseTakenById(String studentId);

    List<Integer> findPastScoreById(String studentId);

    /**
     * 查询教学计划中的课程信息，通过班级id
     * @param classId
     * @return
     */
    List<Course> findScheduleCourses(Integer classId);

    /**
     * 查找公选课程信息
     * @param studentId
     * @return
     */
    List<Course> findCommonCourses(String studentId);

    /**
     * 获取所有公选性质的课程
     * @return
     */
    List<Course> getCommonCourse();

    /**
     * 删除课程信息
     * @param id
     * @return
     */
    int deleteCourseInfoById(Integer id);

    /**
     * 增加教学计划
     * @param config
     * @return
     */
    int addCourseConfig(ScheduleConfig config);

    List<NewEnroll> getNewEnrolls();

    /**
     * 选课系统开放（全部学院）
     * @return
     */
    int openAllCourseSystem();

    /**
     * 根据学院id开放选课系统
     * @param academyIds
     * @return
     */
    int openCourseSystemByAcademyIds(int[] academyIds);

    /**
     * 关闭所有选课系统
     * @return
     */
    int closeAllCourseSystem();

    /**
     * 增加公选课和学生之间的联系
     * @param courseId
     * @param studentId
     */
    void addStudentCommonCourse(@Param("cid") Object courseId, @Param("studentIds") Set<Object> studentId);
		Course findCourseBaseInfoById(Integer id);
}
