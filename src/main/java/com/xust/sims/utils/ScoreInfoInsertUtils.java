package com.xust.sims.utils;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import com.xust.sims.dto.ScoreExcelTemplateData;
import com.xust.sims.dto.ScoreExcelData;
import com.xust.sims.entity.Score;
import org.apache.poi.ss.usermodel.IndexedColors;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
public class ScoreInfoInsertUtils {
    private static final List<ScoreExcelData> EXCEL_DATA = new ArrayList<>();

    public static void getTemplateFile(HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        //防止中文乱码
        String filename = URLEncoder.encode("Grade insert template", "utf-8");
        response.setHeader("Content-disposition", "attachment;filename=" + filename + ".xlsx");
        EasyExcel.write(response.getOutputStream(), ScoreExcelTemplateData.class)
                .registerWriteHandler(getHorizontalCellStyleStrategy())
                .sheet().doWrite(EXCEL_DATA);
    }

    public static void getErrorFile(HttpServletResponse response, List<ScoreExcelData> excelData, String currFilename) throws IOException {
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        //防止中文乱码
        String filename = URLEncoder.encode(currFilename, "utf-8");
        response.setHeader("Content-disposition", "attachment;filename=" + filename + ".xlsx");
        EasyExcel.write(response.getOutputStream(), ScoreExcelData.class)
                .registerWriteHandler(getHorizontalCellStyleStrategy())
                .sheet().doWrite(excelData);
    }

    private static HorizontalCellStyleStrategy getHorizontalCellStyleStrategy() {
        WriteCellStyle headStyle = new WriteCellStyle();
        headStyle.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
        return new HorizontalCellStyleStrategy(headStyle, new WriteCellStyle());
    }

    public static void getScoreInfoFile(HttpServletResponse response, List<Score> scores, String currFilename) throws IOException {
        List<ScoreExcelData> excelData = convertScoreInfo(scores);
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        //防止中文乱码
        String filename = URLEncoder.encode(currFilename, "utf-8");
        response.setHeader("Content-disposition", "attachment;filename=" + filename + ".xlsx");
        EasyExcel.write(response.getOutputStream(), ScoreExcelData.class)
                .registerWriteHandler(getHorizontalCellStyleStrategy())
                .sheet().doWrite(excelData);
    }

    private static List<ScoreExcelData> convertScoreInfo(List<Score> scores) {
        List<ScoreExcelData> excelData = new ArrayList<>();
        for (Score score : scores) {
            excelData.add(new ScoreExcelData(score.getId(), score.getCourse().toString(), score.getStudent().toString(), score.getGrade(),
                    score.getExamTime(), score.getUpdateTime()));
        }
        return excelData;
    }
}
