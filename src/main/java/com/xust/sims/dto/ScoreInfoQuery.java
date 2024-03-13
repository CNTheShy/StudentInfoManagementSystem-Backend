package com.xust.sims.dto;

import com.xust.sims.entity.Course;
import com.xust.sims.entity.Student;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
public class ScoreInfoQuery {
    private String id;
    private String courseId;
    private String studentId;
    private String grade;

    private String startTime;

    private String endTime;
    @NotNull
    private Integer pageNum;
    @NotNull
    private Integer pageSize;
}
