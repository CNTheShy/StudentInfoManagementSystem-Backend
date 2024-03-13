package com.xust.sims.web.controller;

import com.xust.sims.entity.User;
import com.xust.sims.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.security.Principal;


public class BaseController {
    @Autowired
    private UserService userService;

    protected User getUserInfo(Principal principal) {
        return userService.getUserInfoByUsername(principal.getName());
    }
}
