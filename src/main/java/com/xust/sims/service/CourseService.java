package com.xust.sims.service;

import com.alibaba.fastjson.JSON;
import com.xust.sims.dto.ScheduleConfig;
import com.xust.sims.dto.SelectCourse;
import com.xust.sims.dto.StudentTimetable;
import com.xust.sims.entity.Course;
import com.xust.sims.entity.Class;

import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;


public interface CourseService {
    /**
     * 增设课程
     * @param course
     */
    void addCourse(Course course);

    /**
     * 增设班级
     * @param newClass
     */
    void addClass(Class newClass);

    /**
     * 在指定的课程增设班级
     * @param courseId 课程id
     */
    void openNewClass(Integer courseId);

    /**
     * 根据课程id查询班级
     * @param courseId
     */
    List<Class> getClassesByCourseId(Integer courseId);

    /**
     * 根据查询课程条件，来 search 课程
     * @param selectCourse
     * @return
     */
    List<Course> findCourse(SelectCourse selectCourse);

    Course getCourseByName(String courseName);
    Integer getCourseIdByName(String courseName);

    /**
     * 获取缓存中的课程信息（分页查询）
     * @return
     */
    JSON findCommonCourseInCache(int pageSize, int currPage);

    /**
     * 根据学生id、班级id查询个人所属的课程信息
     * @param id
     * @return
     */
    List<StudentTimetable> getCoursesByStudentId(String id);

    /**
     * 获取抢课地址签名值（前提，选课已经开启）
     * @return
     */
    JSON getSelectCourseUrlSign();

    /**
     * 对比签名值，如果一致则调用抢课逻辑，否则返回签名值比对失败
     * @param studentId
     * @param cid
     * @param sign
     * @return
     */
    CompletableFuture<JSON> studentSelectCourseByCid(String studentId, int cid, String sign);

    /**
     * @param studentId
     * @param cid
     * @return
     */
    String[] findPredictionData(String studentId, int cid);

    /**
     * @param cid
     * @return
     */
    List<Course> getPreRequest(int courseId);

    /**
     * 通过学生id，获取已经选择的课程（选课系统开放时）
     * @param studentId
     * @return
     */
    List<Course> getSelectedCourse(String studentId);

    /**
     * 根据学生 id、课程 cid 退选课程
     * @param studentId
     * @param cid
     */
    void cancelSelectedCourse(String studentId, int cid);

    /**
     * 教学计划配置
     * @param config
     */
    void courseConfig(ScheduleConfig config);

    /**
     * 通过课程id删除课程信息
     * @param id
     */
    void deleteCourseById(Integer id);

    /**
     * 通过班级id和课程id删除课程信息
     * @param classId
     * @param courseId
     */
    void deleteClassById(String classId, Integer courseId);

    /**
     * 开设选课系统，根据学院开启，并预热课程信息
     * @param startTime
     * @param endTime
     * @param academyIds
     * @return 是否开启成功
     */
    boolean openCourseSystem(Date startTime, Date endTime, int[] academyIds);

    /**
     * 关闭选课系统
     * @param courseList
     */
    void closeCourseSystem(List<Course> courseList);
}
