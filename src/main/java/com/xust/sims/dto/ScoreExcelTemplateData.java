package com.xust.sims.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.xust.sims.entity.Student;
import com.xust.sims.entity.Course;
import lombok.Data;
import java.util.Date;

@Data
public class ScoreExcelTemplateData {
    @ExcelProperty(value = "studentId")
    private String studentId;
    @ExcelProperty(value = "courseId")
    private String courseId;
    @ExcelProperty(value = "grade")
    private String grade;
    @ExcelProperty(value = "exam time")
    @DateTimeFormat("yyyy-MM-dd")
    @ColumnWidth(15)
    private Date examTime;
    @ExcelProperty(value = "update time")
    @DateTimeFormat("yyyy-MM-dd")
    @ColumnWidth(15)
    private Date updateTime;
}
