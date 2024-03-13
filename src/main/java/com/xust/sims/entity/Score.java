package com.xust.sims.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.Date;


@Data
@NoArgsConstructor
public class Score implements Serializable {
    @NotEmpty(message = "score id can not be empty")
    private Long id;
    private String grade;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date examTime;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date updateTime;

    private Course course;
    private Student student;

    private String studentId;

    private Integer courseId;


    public Score(Long id, String grade){
        this.id = id;
        this.grade = grade;
    }
}
