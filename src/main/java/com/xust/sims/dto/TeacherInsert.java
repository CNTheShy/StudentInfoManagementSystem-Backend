package com.xust.sims.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.*;
import java.util.Date;


@Data
public class TeacherInsert {
    private String id;
    @NotEmpty(message = "Name cannot be empty!")
    private String name;
    @NotEmpty(message = "Gender cannot be empty!")
    private String sex;
    @NotEmpty(message = "id number cannot be empty!")
    private String idCard;
    @NotEmpty(message = "E-mail cannot be empty!")
    @Email(message = "E-mail format error!")
    private String email;
    @NotNull(message = "Academy information cannot be empty!")
    private Integer academyId;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "Enrollment time can not be empty")
    private Date createTime;
}
