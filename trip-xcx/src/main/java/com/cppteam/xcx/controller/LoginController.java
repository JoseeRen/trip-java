package com.cppteam.xcx.controller;

import com.cppteam.common.util.TripResult;
import com.cppteam.xcx.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 小程序登录控制器
 * Created by happykuan on 2017/10/31.
 */
@RestController
@RequestMapping(value = "/xcx/login")
public class LoginController {

    @Autowired
    private LoginService loginService;

    @RequestMapping("/get_token/{code}")
    public TripResult getToken(@PathVariable String code) {
        return loginService.getToken(code);
    }
}
