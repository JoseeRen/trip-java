package com.cppteam.app.service;

import com.cppteam.common.util.TripResult;

import javax.servlet.http.HttpServletRequest;


/**
 * App 登录服务接口
 * Created by happykuan on 2017/10/27.
 */
public interface LoginService {
    /**
     * 使用code登录
     * @param code
     * @return
     */
    public TripResult login(String code);

    /**
     * 使用token登录
     * @param request
     * @return
     */
    public TripResult login(HttpServletRequest request);

    /**
     * 验证登录状态
     * @param request
     * @return
     */
    public TripResult checkLoginStatus(HttpServletRequest request);
}
