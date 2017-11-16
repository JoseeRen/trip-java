package com.cppteam.app.controller;

import com.cppteam.app.service.LoginService;
import com.cppteam.common.util.TripResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 登录控制器
 * Created by happykuan on 2017/10/27.
 */
@RestController
@RequestMapping(value = "/app/login")
public class LoginController {

    @Autowired
    private LoginService loginService;

    /**
     * 通过code登录
     * @param code 微信登录code
     * @return
     */
    @RequestMapping(value = "/get_token/{code}")
    public TripResult loginbyCode(@PathVariable String code) {
        return loginService.login(code);
    }

    /**
     * 通过token登录
     * @param request request请求头带有Authorization字段
     * @return
     */
    @RequestMapping(value = "/token")
    public TripResult loginByToken(HttpServletRequest request) {
        return loginService.login(request);
    }
}
