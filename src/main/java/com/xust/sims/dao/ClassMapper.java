package com.xust.sims.dao;

import com.xust.sims.dto.NewEnroll;
import com.xust.sims.entity.Class;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ClassMapper {
    /**
     * 根据id来查找班级信息
     * @param id id
     * @return class's info
     */
    Class findClassBaseInfoById(Integer id);

    /**
     * 添加新class
     * @param newClass
     */
    void addClass(Class newClass);

    /**
     * 查询指定课程有多少class
     * @param courseId 课程id
     */
    int getCountClass(Integer courseId);

    /**
     * 删除class
     * @param classId 班级id
     * @param courseId 课程id
     */
    void deleteClassById(String classId, Integer courseId);

    /**
     * 根据课程ID来获取班级信息
     * @param courseId 课程ID
     * @return 在该课程下，所有班级信息
     */
    List<Class> findClassByCourseId(Integer courseId);

    /**
     * 根据专业ID来获取班级信息
     * @param majorId 专业ID
     * @return 在该专业下，所有班级信息
     */
    List<Class> findClassByMajorId(Integer majorId);

    void enrollToClassById(String studentId, Integer courseId, Integer classId);

    Integer findClassIdByMajorNameAndClassName(@Param("majorName") String majorName,
                                               @Param("className") String className);

    Integer findClassSizeByClassId(Integer classId);

    Integer findNotFullClassByCourseId(Integer classId);

    int updateClassSize(@Param("classId") Integer classId,
                        @Param("classSize") Integer classSize);

    int reduceOneClassSizeByClassId(Integer classId);

    int updateClassSizePlusOne(Integer classId);
}
