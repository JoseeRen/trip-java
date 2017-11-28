package com.cppteam.app.interceptor;

import com.cppteam.app.service.LoginService;
import com.cppteam.common.util.TripResult;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * 登录拦截器
 * Created by happykuan on 2017/10/28.
 */
public class LoginInterceptor implements HandlerInterceptor {

    @Autowired
    private LoginService loginService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        response.setHeader("Content-Type", "application/json");
        response.setCharacterEncoding("utf-8");
        request.setCharacterEncoding("utf-8");

        // 放行公开接口
//        String uri = request.getRequestURI();
//        if (uri.contains("/login/get_token") || uri.contains("/login/token")) {
//            return true;
//        }

        // 检查操作权限
//        TripResult result = loginService.checkLoginStatus(request);
//        if (result.getStatus() == 200) {
//            return true;
//        }
//
//        PrintWriter out = response.getWriter();
//        out.print(new Gson().toJson(result));
//        return false;

        // 开发阶段全部放行
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
