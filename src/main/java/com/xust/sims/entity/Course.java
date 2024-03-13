package com.xust.sims.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;


@Data
public class Course {
    private Integer id;
    @NotEmpty(message = "course name can not be empty")
    private String name;
    /**
     * 课程性质，1 表示必修，2 表示选修，3 表示公选
     */
    @NotNull(message = "course type can not be empty")
    private Integer status;
    @NotNull(message = "credit can not be empty")
    @Min(value = 0, message = "credit can not be smaller than 0")
    private Double credit;
    @NotNull(message = "period can not be empty")
    @Min(value = 0, message = "period can not be smaller than 0")
    private Integer period;
    private Integer total;
    private Integer academyId;
    private String teacherId;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createTime;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date updateTime;
    private Integer leftNum;

    private String classId;

    private Teacher teacher;
    private Academy academy;
}
