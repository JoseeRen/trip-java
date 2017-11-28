package com.cppteam.app.service.impl;

import com.cppteam.app.service.LoginService;
import com.cppteam.common.util.ImageUtils;
import com.cppteam.common.util.*;
import com.cppteam.dao.JedisClient;
import com.cppteam.mapper.UserMapper;
import com.cppteam.mapper.UserWxMapper;
import com.cppteam.pojo.User;
import com.cppteam.pojo.UserWx;
import com.cppteam.pojo.UserWxExample;
import com.google.gson.Gson;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 登录服务接口实现
 * Created by happykuan on 2017/10/27.
 */

@Service
public class LoginServiceImpl implements LoginService {

    @Value("${APPID}")
    private String APPID;
    @Value("${SECRET}")
    private String SECRET;
    @Value("${ACCESS_TOKEN_BASE_URI}")
    private String ACCESS_TOKEN_BASE_URI;
    @Value("${USERINFO_BASE_URL}")
    private String USERINFO_BASE_URL;
    @Value("${REFRESH_TOKEN_BASE_URL}")
    private String REFRESH_TOKEN_BASE_URL;
    @Value("${AVATAR_THUMB_DEFAULT_WIDTH}")
    private Integer AVATAR_THUMB_DEFAULT_WIDTH;
    @Value("${APP_USER_INFO_KEY}")
    private String APP_USER_INFO_KEY;
    @Value("${DEFAULT_NULL}")
    private String DEFAULT_NULL;

    @Autowired
    private UserWxMapper userWxMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private JedisClient jedisClient;

    /**
     * 通过code登录, 使用code登录时, 用户信息会被更新
     * @param code code
     * @return
     */
    @Override
    public TripResult login(String code) {

        if (StringUtils.isBlank(code)) {
            return TripResult.build(400, "缺少参数：code");
        }

        // 获取access_token
        Map<String, String> params = new HashMap<String, String>(4);
        params.put("appid", APPID);
        params.put("secret", SECRET);
        params.put("code", code);
        params.put("grant_type", "authorization_code");
        String resJson = HttpClientUtil.doGet(ACCESS_TOKEN_BASE_URI, params);

        Map resObj = new HashMap<String, Object>(1);
        resObj = new Gson().fromJson(resJson, resObj.getClass());

        // 返回的数据不含openid, 获取access_token失败
        if (!resObj.containsKey("openid")) {
            return TripResult.build(401, resObj.get("errmsg").toString());
        }
        String openid = (String) resObj.get("openid");
        String accessToken = (String) resObj.get("access_token");
        Integer expiresIn = ((Double) resObj.get("expires_in")).intValue();
        String refreshToken = (String) resObj.get("refresh_token");
        String scope = (String) resObj.get("scope");

        // 获取用户信息
        User user = this.getUserInfo(openid, accessToken);
        if (user == null) {
            // 获取用户信息失败
            return TripResult.build(403, "未知错误");
        }

        // 数据库查找用户
        try {
            UserWxExample userWxExample = new UserWxExample();
            UserWxExample.Criteria criteria = userWxExample.createCriteria();
            criteria.andOpenidEqualTo(openid);
            List<UserWx> wxList = userWxMapper.selectByExample(userWxExample);

            String userId = null;
            // 第一次用该微信账号使用该app
            if (wxList.isEmpty()) {

                // 插入用户信息
                // 上传头像
                String url = user.getAvatar();
                String avatar = ImageUtils.saveImage(url);
                String avatarThumb = ImageUtils.saveImage(ImageUtils.thumbnailImage(url, AVATAR_THUMB_DEFAULT_WIDTH), null);

                // 生成用户id插入
                userId = IDGenerator.createUserId();
                user.setId(userId);
                user.setAvatar(avatar);
                user.setAvaterThumb(avatarThumb);
                userMapper.insertSelective(user);

                // 插入用户登录信息
                String userWxId = IDGenerator.createUserWxId();
                UserWx userWx = new UserWx();
                userWx.setId(userWxId);
                userWx.setUserId(userId);
                userWx.setAccessToken(accessToken);
                userWx.setExpiresIn(expiresIn);
                userWx.setRefreshToken(refreshToken);
                userWx.setScope(scope);
                userWxMapper.insertSelective(userWx);

            } else {
                // 微信用户曾经在该app登录过
                UserWx userWx = wxList.get(0);
                // 更新用户登录信息
                userWx.setAccessToken(accessToken);
                userWx.setRefreshToken(refreshToken);
                userWxMapper.updateByPrimaryKeySelective(userWx);

                // 上传新头像
                String url = user.getAvatar();
                String avatar = ImageUtils.saveImage(url);
                String avatarThumb = ImageUtils.saveImage(ImageUtils.thumbnailImage(url, AVATAR_THUMB_DEFAULT_WIDTH), null);

                // 删除旧头像
                User oldUserInfo = userMapper.selectByPrimaryKey(userWx.getUserId());
                String oldAvatar = oldUserInfo.getAvatar();
                String oldAvatarThumb = oldUserInfo.getAvaterThumb();
                if (!DEFAULT_NULL.equals(oldAvatar)) {
                    ImageUtils.deleteImage(oldAvatar);
                }
                if (!DEFAULT_NULL.equals(oldAvatarThumb)) {
                    ImageUtils.deleteImage(oldAvatarThumb);
                }

                // 更新用户资料信息
                user.setId(oldUserInfo.getId());
                user.setAvatar(avatar);
                user.setAvaterThumb(avatarThumb);
                userMapper.updateByPrimaryKeySelective(user);

                // 同步redis缓存
                try {
                    jedisClient.hdel(APP_USER_INFO_KEY, oldUserInfo.getId());
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            // 生成登录token并返回
            String token = JWTUtil.generateToken(userId);
            Map<String, String> result = new HashMap<String, String>(1);
            result.put("token", token);
            return TripResult.ok("登录成功", result);

        } catch (Exception e) {
            e.printStackTrace();
            return TripResult.build(500, e.getMessage());
        }

    }

    /**
     * 通过token登录
     * @param request
     * @return
     */
    @Override
    public TripResult login(HttpServletRequest request) {

        // 验证token合法性
        String token = request.getHeader("Authorization");
        if (StringUtils.isBlank(token)) {
            return TripResult.build(400, "请求头缺少参数：Authorization");
        }

        String userId = JWTUtil.validToken(token);
        if (StringUtils.isBlank(userId)) {
            return TripResult.build(400, "token不合法");
        }

        // 数据库中是否有该用户
        UserWxExample userWxExample = new UserWxExample();
        UserWxExample.Criteria criteria = userWxExample.createCriteria();
        criteria.andUserIdEqualTo(userId);
        List<UserWx> userWxes = userWxMapper.selectByExample(userWxExample);
        if (userWxes.isEmpty()) {
            return TripResult.build(403, "用户不存在");
        }
        UserWx userWx = userWxes.get(0);

        // 刷新access_token
        String refreshToken = userWx.getRefreshToken();
        String openid = userWx.getOpenid();
        String grantType = "refresh_token";

        Map<String, String> params = new HashMap<String, String>(3);
        params.put("appid", APPID);
        params.put("grant_type", grantType);
        params.put("refresh_token", refreshToken);
        String resJson = HttpClientUtil.doGet(REFRESH_TOKEN_BASE_URL, params);

        // 服务器网络异常情况
        if(StringUtils.isBlank(resJson)) {
            return TripResult.build(500, "服务器异常，请稍后重试");
        }
        Map resObj = new HashMap<String, Object>(1);
        resObj = new Gson().fromJson(resJson, resObj.getClass());

        // 刷新access_token失败
        if (!resObj.containsKey("access_token")) {
            return TripResult.build(403, "登录凭证过期，请重新使用code登录");
        }
        // 成功
        String accessToken = (String) resObj.get("access_token");
        try {
            // 更新登录信息
            userWx.setAccessToken(accessToken);
            userWxMapper.updateByPrimaryKeySelective(userWx);

            return TripResult.ok("登录成功", null);
        } catch (Exception e) {
            return TripResult.build(500, e.getMessage());
        }

    }

    /**
     * 检查用于登录权限，为登录拦截器使用
     * @param request
     * @return
     */
    @Override
    public TripResult checkLoginStatus(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (StringUtils.isBlank(token)) {
            return TripResult.build(400, "请求头缺少参数：Authorization");
        }

        String userId = JWTUtil.validToken(token);
        if (StringUtils.isBlank(userId)) {
            return TripResult.build(400, "token不合法");
        }
        return TripResult.ok();
    }


    /**
     * 通过access_token和openid在微信服务器获取用户信息
     * @param openid
     * @param accessToken
     * @return User对象实例，不含id
     */
    private User getUserInfo(String openid, String accessToken) {
        try {
            Map<String, String> params = new HashMap<String, String>(2);
            params.put("openid", openid);
            params.put("access_token", accessToken);
            String resJson = HttpClientUtil.doGet(USERINFO_BASE_URL, params);

            if (StringUtils.isBlank(resJson)) {
                return null;
            }

            Map resObj = new HashMap<String, Object>(1);
            resObj = new Gson().fromJson(resJson, resObj.getClass());

            if (!resObj.containsKey("openid")) {
                return null;
            }

            String openId = (String) resObj.get("openid");
            String nickname = (String) resObj.get("nickname");
            String headimgurl = (String) resObj.get("headimgurl");
            String sex = (String) resObj.get("sex");
            String province = (String) resObj.get("province");
            String country = (String) resObj.get("country");
            String unionid = resObj.containsKey("unionid") ? (String) resObj.get("unionid") : null;

            User user = new User();
            user.setOpenid(openId);
            user.setAvatar(headimgurl);
            user.setNickname(nickname);
            user.setSex(sex);
            user.setProvince(province);
            user.setCountry(country);
            user.setUnionid(unionid);
            return user;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }
}
