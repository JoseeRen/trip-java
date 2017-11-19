package com.cppteam.service.impl;

import com.cppteam.common.util.CookieUtils;
import com.cppteam.common.util.JWTUtil;
import com.cppteam.common.util.SerializeUtil;
import com.cppteam.dao.JedisClient;
import com.cppteam.pojo.User;
import com.cppteam.service.LoginService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.util.SerializationUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author happykuan
 * @date 2017/11/18
 */
@Service
public class LoginServiceImpl implements LoginService {

    @Value("${USERNAME}")
    private String USERNAME;
    @Value("${PASSWORD}")
    private String PASSWORD;
    @Value("${MANAGER_TOKEN_KEY}")
    private String MANAGER_TOKEN_KEY;

    @Autowired
    private JedisClient jedisClient;

    /**
     * 管理员登录
     * @param username
     * @param password
     * @param request
     * @param response
     * @return
     */
    @Override
    public Map<String, String> login(String username, String password, HttpServletRequest request, HttpServletResponse response) {

        String u = DigestUtils.md5DigestAsHex(username.getBytes());
        String p = DigestUtils.md5DigestAsHex(password.getBytes());

        if (USERNAME.equalsIgnoreCase(u) && PASSWORD.equalsIgnoreCase(p)) {
            // 验证成功
            User user = new User();
            user.setNickname(username);

            // 生成token
            String token = JWTUtil.generateToken(username);

            // 用户信息存入redis作为session
            jedisClient.set(MANAGER_TOKEN_KEY, SerializeUtil.serialize(user));
            // 有效期为30分钟
            jedisClient.expire(MANAGER_TOKEN_KEY, 1800);

            // 将token写入浏览器cookie中
            CookieUtils.setCookie(request, response, MANAGER_TOKEN_KEY, token);
            Map<String, String> tokenMap = new HashMap<String, String>();
            tokenMap.put("manager_token", token);
            // 返回非空user对象，表示登录成功
            return tokenMap;
        }

        return null;
    }

    /**
     * 检测管理员登录状态
     * @param request
     * @param response
     * @return
     */
    @Override
    public boolean checkLogin(HttpServletRequest request, HttpServletResponse response) {

        String token = CookieUtils.getCookieValue(request, MANAGER_TOKEN_KEY);
        if (StringUtils.isBlank(token)) {
            return false;
        }

        String objStr = jedisClient.get(MANAGER_TOKEN_KEY);
        if (StringUtils.isBlank(objStr)) {
            return false;
        }

        // 管理员已经登录
        // 更新session生命周期
        jedisClient.expire(MANAGER_TOKEN_KEY, 1800);
        return true;
    }
}
