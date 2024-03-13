package com.xust.sims.entity;

import lombok.Data;

import java.util.Date;
import java.util.List;


@Data
public class Admin {
    private String id;
    private String name;
    private String avatar;
    private String sex;
    private String phoneNum;
    private String comment;
    private Date createTime;
    private Date updateTime;

    private List<NoticeBoard> boards;
}
