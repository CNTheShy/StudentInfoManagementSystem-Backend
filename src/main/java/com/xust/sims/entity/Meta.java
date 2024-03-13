package com.xust.sims.entity;

import lombok.Data;


@Data
public class Meta {
    private Integer id;
    private Boolean keepAlive = Boolean.FALSE;
    private Boolean requireAuth = Boolean.FALSE;
}
