package com.cppteam.app.service.impl;

import com.cppteam.app.service.UserService;
import com.cppteam.common.util.JWTUtil;
import com.cppteam.common.util.SerializeUtil;
import com.cppteam.common.util.TripResult;
import com.cppteam.dao.JedisClient;
import com.cppteam.mapper.UserMapper;
import com.cppteam.pojo.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Created by happykuan on 2017/11/6.
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private JedisClient jedisClient;
    @Value("${APP_USER_INFO_KEY}")
    private String APP_USER_INFO_KEY;
    @Value("${DEFAULT_NULL}")
    private String DEFAULT_NULL;
    @Value("${REDIS_EXPIRE_TIME}")
    private Integer REDIS_EXPIRE_TIME;

    @Override
    public TripResult getUserInfo(String token) {
        String userId = JWTUtil.validToken(token);
        // 尝试从缓存中读取
        try {
            String res = jedisClient.get(APP_USER_INFO_KEY + userId);
            if (StringUtils.isNotBlank(res)) {
                return TripResult.ok("获取用户信息成功", SerializeUtil.unSerialize(res));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        User user = userMapper.selectByPrimaryKey(userId);
        // 清除敏感信息
        user.setOpenid(DEFAULT_NULL);
        user.setUnionid(DEFAULT_NULL);

        // 存入缓存
        try {
            jedisClient.set(APP_USER_INFO_KEY + userId, SerializeUtil.serialize(user));
            jedisClient.expire(APP_USER_INFO_KEY + userId, REDIS_EXPIRE_TIME);
        } catch (Exception e) {
            e.printStackTrace();
        }


        return TripResult.ok("获取用户信息成功", user);


    }
}
