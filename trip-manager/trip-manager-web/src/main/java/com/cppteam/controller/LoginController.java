package com.cppteam.controller;

import com.cppteam.common.util.TripResult;
import com.cppteam.pojo.User;
import com.cppteam.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Created by happykuan on 2017/11/18.
 */
@Controller
@RequestMapping("/api/manager")
public class LoginController {

    @Autowired
    private LoginService loginService;

    /**
     * 管理员登录action
     * @param username
     * @param password
     * @param response
     * @return
     */
    @RequestMapping("/login")
    @ResponseBody
    public TripResult loginAction(String username, String password,
                                  HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {

        Map<String, String> tokenMap = loginService.login(username, password, request, response);

        if (tokenMap != null) {
            return TripResult.ok("ok", tokenMap);
        } else {
            return TripResult.build(403, "登录失败，请重试！");
        }


    }

}
