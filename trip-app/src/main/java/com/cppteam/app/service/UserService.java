package com.cppteam.app.service;

import com.cppteam.common.util.TripResult;

/**
 * Created by happykuan on 2017/11/6.
 */
public interface UserService {

    /**
     * 获取用户信息
     * @param token
     * @return
     */
    public TripResult getUserInfo(String token);

}