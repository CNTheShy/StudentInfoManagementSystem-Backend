package com.xust.sims.dto;

import lombok.Data;

import java.util.List;


@Data
public class SelectCourse {
    private String name;
    private List<Integer> status;
    private Integer academyId;
    private Integer currPage;
    private Integer pageSize;
}
