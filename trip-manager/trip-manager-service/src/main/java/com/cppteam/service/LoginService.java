package com.cppteam.service;

import com.cppteam.pojo.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 *
 * @author happykuan
 * @date 2017/11/18
 */
public interface LoginService {

    /**
     * 管理员登录
     * @param username
     * @param password
     * @param request
     * @param response
     * @return tokenMap
     */
    Map<String, String> login(String username, String password, HttpServletRequest request, HttpServletResponse response);

    /**
     * 检测管理员登录状态
     * @param request
     * @param response
     * @return
     */
    boolean checkLogin(HttpServletRequest request, HttpServletResponse response);
}
