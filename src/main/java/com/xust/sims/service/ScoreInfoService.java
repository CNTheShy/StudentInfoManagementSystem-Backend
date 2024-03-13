package com.xust.sims.service;

import com.xust.sims.dto.*;
import com.xust.sims.entity.Score;
import com.xust.sims.dto.ScoreExcelData;
import com.xust.sims.dto.ScoreInfoQuery;
import com.xust.sims.dto.ScoreInsert;
import com.xust.sims.entity.Student;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
public interface ScoreInfoService {
    List<Score> getAllScoreInfo();

    Score getScoreInfoById(Long id);

    Score getScoreInfoByStudentAndCourse(String studentId, Integer courseId);

    void addOneScoreInsertInfo(ScoreInsert scoreInsert);

    void addBatchScoreInsertInfo(List<Score> scores);

    void processScoreExcelData(List<ScoreExcelData> dataList);

    void saveExcelData(List<ScoreExcelData> list) throws Exception;

    void updateScoreInsertInfo(Score score);

    void deleteScoreInfo(Long id);

    List<Score> getScoreByQueryInfo(ScoreInfoQuery query);

    List<Score> getScoreByIds(int[] ids);

    List<ScoreExcelData> saveScoreExcelData(MultipartFile[] files) throws Exception;
}
