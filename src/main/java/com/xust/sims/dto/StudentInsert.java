package com.xust.sims.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.*;
import java.util.Date;


@Data
public class StudentInsert {
    private String id;
    @NotEmpty(message = "name can not be empty")
    private String name;
    @NotEmpty(message = "gender can not be empty")
    private String sex;
    @NotNull(message = "age can not be empty")
    @Min(value = 0, message = "age can not be smaller than 0")
    private Integer age;
    @NotEmpty(message = "e-mail can not be empty")
    @Email(message = "e-mail format error")
    private String email;
    @NotNull(message = "academy can not be empty")
    private Integer academyId;
    @NotNull(message = "major can not be empty")
    private Integer majorId;
    @NotEmpty(message = "id number can not be empty")
    private String idCard;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "enrollment can not be empty")
    private Date createTime;
}
