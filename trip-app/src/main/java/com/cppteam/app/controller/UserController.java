package com.cppteam.app.controller;

import com.cppteam.app.service.UserService;
import com.cppteam.common.util.TripResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by happykuan on 2017/11/6.
 */
@RestController
@RequestMapping(value = "/app/user")
public class UserController {


    @Autowired
    private UserService userService;

    @RequestMapping(value = "/info")
    public TripResult getUserInfo(@RequestHeader(value = "Authorization") String token) {
        return userService.getUserInfo(token);
    }
}
