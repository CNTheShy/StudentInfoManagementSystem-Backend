package com.xust.sims.entity;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;


@Data
public class Class {
    private String id;
    private String name;
    private Integer count;

    private Integer courseId;

    public Class(String id, String name, Integer courseId) {
        this.id = id;
        this.name = name;
        this.courseId = courseId;
    }
}
