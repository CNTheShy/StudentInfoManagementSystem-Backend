package com.xust.sims.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class NewEnroll {
    @NotNull(message = "student id can not be empty!")
    private String studentId; // Changed from int to String
    @NotNull(message = "course id can not be empty!")
    private int courseId;

}
