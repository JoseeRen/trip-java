package com.cppteam.xcx.controller;

import com.cppteam.common.util.TripResult;
import com.cppteam.xcx.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 小程序登录控制器
 * Created by happykuan on 2017/10/31.
 */
@RestController
@RequestMapping(value = "/xcx/login")
public class LoginController {

    @Autowired
    private LoginService loginService;

    @RequestMapping("/get_token/{code}")
    public TripResult getToken(@PathVariable String code) {
        return loginService.getToken(code);
    }
    /**
     *
     * @param encryptedData 明文,加密数据
     * @param iv 加密算法的初始向量
     * @param code 用户允许登录后，回调内容会带上 code（有效期五分钟），
     *             开发者需要将 code 发送到开发者服务器后台，
     *             使用code 换取 session_key api，将 code 换成 openid 和 session_key
     * @return
     */
    @RequestMapping("/get_token1")
    public TripResult loginByXcx(String encryptedData, String iv, String code){
        return loginService.getToken1(encryptedData,iv,code);
    }
}
