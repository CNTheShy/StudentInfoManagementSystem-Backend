package com.xust.sims.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.List;


@Data
public class Academy {
    private Integer id;
    private String name;

    @JSONField(name = "children")
    private List<Major> majors;

    private List<Teacher> teachers;
}
