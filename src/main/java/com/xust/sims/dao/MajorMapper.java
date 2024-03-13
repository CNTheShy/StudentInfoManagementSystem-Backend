package com.xust.sims.dao;

import com.xust.sims.entity.Major;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface MajorMapper {
    /**
     * 根据id来查找专业信息
     * @param id id
     * @return academy's info
     */
    Major findMajorBaseInfoById(Integer id);

    /**
     * 获取所有专业信息
     * @return 获取所有专业信息
     */
    List<Major> findAllMajors();

    /**
     * 查询专业细节信息
     * @return
     */
    List<Major> findMajorsDetails(Integer academyId);

    /**
     * 根据学院ID来获取专业信息
     * @param academyId 学院ID
     * @return 该学院下的所有专业信息
     */
    List<Major> findMajorsByAcademyId(Integer academyId);

    Integer findMajorIdByName(String majorName);

    int addMajorInfo(Integer academyId, String majorName);

    /**
     * 根据专业ID来获取专业信息
     * @param majorId 专业ID
     * @return 该专业下的学生数量
     */
    Integer getStudentNumberByMajorId(Integer majorId);
}
