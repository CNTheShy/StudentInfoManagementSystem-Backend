package com.xust.sims.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import javax.validation.constraints.*;
import java.util.Date;

@Data
public class ScoreInsert {
    private Long id;
    @NotEmpty(message = "student can not be empty!")
    private String studentId;
    @NotNull(message = "course can not be empty!")
    private String courseId;
    @NotNull(message = "grade can not be empty!")
    private String grade;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "exam time can not be empty!")
    private Date examTime;
}
