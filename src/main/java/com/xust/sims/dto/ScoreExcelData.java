package com.xust.sims.dto;
import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.xust.sims.entity.Course;
import com.xust.sims.entity.Student;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.*;
import java.util.Date;
@Data
@NoArgsConstructor
public class ScoreExcelData {
    @ExcelProperty(value = "score id")
    private Long id;

    @NotEmpty(message = "can not be empty")
    @ExcelProperty(value = "courseId")
    private String courseId;

    @NotEmpty(message = "can not be empty")
    @ExcelProperty(value = "studentId")
    private String studentId;

    @NotEmpty(message = "can not be empty")
    @ExcelProperty(value = "grade")
    private String grade;

    @ExcelProperty(value = "exam time")
    @ColumnWidth(15)
    @DateTimeFormat("yyyy-MM-dd")
    @NotNull(message = "can not be empty")
    private Date examTime;

    @ExcelProperty(value = "update time")
    @ColumnWidth(15)
    @DateTimeFormat("yyyy-MM-dd")
    @NotNull(message = "can not be empty")
    private Date updateTime;

    @ExcelProperty(value = "description")
    @ColumnWidth(25)
    private String description;


    public ScoreExcelData(Long id, String course, String student, String grade,
                          Date examTime, Date updateTime) {
        this.id = id;
        this.courseId = course;
        this.studentId = student;
        this.grade = grade;
        this.examTime = examTime;
        this.updateTime = updateTime;
    }


}
