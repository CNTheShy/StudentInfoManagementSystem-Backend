package com.xust.sims.dao;

import com.xust.sims.dto.ScoreInfoQuery;
import com.xust.sims.dto.ScoreExcelData;
import com.xust.sims.dto.ScoreInsert;
import com.xust.sims.entity.Score;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ScoreMapper {

    List<Score> findAllScoreInfoDetails();

    Score findScoreInfoById(Long id);

		Score findScoreInfoByStudentAndCourse(String studentId, Integer courseId);

    /**
     * 单条插入学生信息
     * @param scoreInsert 单条插入学生信息
     * @return effect rows
     */
    int insertOneScoreInfo(ScoreInsert scoreInsert);

    /**
     * 批量插入分数信息
     * @param scoreExcelData 学生信息Excel对应的实体类
     * @return effect rows
     */
    int insertBatchScoreInfos(@Param("scoreExcelData") List<ScoreExcelData> scoreExcelData);

    /**
     * 更新学生信息
     * @param score
     */
    void updateScoreInfo(Score score);
    void deleteScoreInfo(Long id);
    List<Score> findScoreByCondition(ScoreInfoQuery query);
    List<Score> findAllScoreByIds(@Param("ids") List<Integer> ids);
}
