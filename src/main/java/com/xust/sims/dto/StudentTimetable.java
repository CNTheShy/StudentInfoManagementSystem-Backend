package com.xust.sims.dto;

import lombok.Data;


@Data
public class StudentTimetable {
    private String id;
    private String name;
    private Integer status;
    private Double credit;
    private Integer period;
    private String teacher;
}
