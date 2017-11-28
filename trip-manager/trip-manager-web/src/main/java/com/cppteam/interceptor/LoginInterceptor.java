package com.cppteam.interceptor;

import com.cppteam.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 管理员登录拦截器
 * @author happykuan
 * @date 2017/11/18
 */
public class LoginInterceptor implements HandlerInterceptor {

    @Autowired
    private LoginService loginService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String requestURI = request.getRequestURI();
        // 如果是登录页面或者是登录action 放行
        if (requestURI.contains("/manager/login") || requestURI.contains("/api/manager/login")) {
            return true;
        }

        boolean isLogin = loginService.checkLogin(request, response);
        if (isLogin) {
            return true;
        }
        response.sendRedirect("/manager/login");
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
