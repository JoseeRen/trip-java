package com.cppteam.xcx.service;

import com.cppteam.common.util.TripResult;

/**
 * 微信小程序登录服务接口
 * @author happykuan
 *
 */
public interface LoginService {
	TripResult getToken(String code);
	TripResult checkLoginStatus(String token);
}
