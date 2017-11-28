package com.cppteam.xcx.controller;

import com.cppteam.common.util.TripResult;
import com.cppteam.xcx.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @version V1.0
 * @author: lin_shen
 * @date: 2017/11/21
 * @Description: TODO
 */
@RestController
@RequestMapping(value = "/xcx/user")
public class UserController {
    @Autowired
    private UserService userService;

    @RequestMapping(value = "/refreshInfo")
    public TripResult getUserInfo(@RequestHeader(value = "Authorization") String token, String encryptedData, String iv) {
        return userService.refreshInfo(token,encryptedData,iv);
    }
}
