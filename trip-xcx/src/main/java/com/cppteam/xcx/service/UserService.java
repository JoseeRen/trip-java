package com.cppteam.xcx.service;

import com.cppteam.common.util.TripResult;

/**
 * @version V1.0
 * @author: lin_shen
 * @date: 2017/11/21
 * @Description: TODO
 */

public interface UserService {
    TripResult refreshInfo(String token, String encryptedData, String iv);
}
