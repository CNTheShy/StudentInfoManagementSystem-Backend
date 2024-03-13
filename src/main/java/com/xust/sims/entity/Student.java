package com.xust.sims.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;


@Data
@NoArgsConstructor
public class Student implements Serializable {
    private static final long serialVersionUID = -6195864794816991937L;
    @NotEmpty(message = "user name can not be empty")
    private String id;
    @NotEmpty(message = "name can not be empty")
    private String name;
    private String nation;
    private String sex;
    private Integer age;
    private String politicsStatus;
    private String idCard;
    private String phoneNum;
    @Email(message = "e-mail format error")
    private String email;
    private String avatar;
    private String description;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createTime;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date updateTime;

    private Class classes;
    private Academy academy;
    private Major major;

    public Student(String id, String name, String idCard, String email) {
        this.id = id;
        this.name = name;
        this.idCard = idCard;
        this.email = email;
    }
}
