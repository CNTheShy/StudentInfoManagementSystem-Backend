package com.xust.sims.dto;

import lombok.Data;


@Data
public class LoginUser {
    private String username;
    private String password;
    private Boolean rememberMe;
    private String imageCode;
}
