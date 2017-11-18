package com.cppteam.xcx.service;

import com.cppteam.common.util.TripResult;

/**
 * 微信小程序登录服务接口
 * @author happykuan
 *
 */
public interface LoginService {
	public TripResult getToken(String code);
	public TripResult checkLoginStatus(String token);

    TripResult getToken1(String encryptedData, String iv, String code);
}
