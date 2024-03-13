package com.xust.sims.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.text.SimpleDateFormat;
import java.util.Date;


@Data
public class NoticeBoard {
    private Integer id;
    @NotEmpty(message = "title can not be empty")
    private String title;
    @NotNull(message = "published status can not be empty")
    private Boolean published;
    @NotEmpty(message = "content can not be empty")
    private String content;
    @NotEmpty(message = "notice type can not be empty")
    private String typeName;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createTime;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date updateTime;
}
