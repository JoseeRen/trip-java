package com.cppteam.xcx.service.impl;

import com.cppteam.common.util.*;
import com.cppteam.dao.JedisClient;
import com.cppteam.mapper.UserMapper;
import com.cppteam.pojo.User;
import com.cppteam.pojo.UserExample;
import com.cppteam.xcx.service.UserService;
import com.google.gson.Gson;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @version V1.0
 * @author: lin_shen
 * @date: 2017/11/21
 * @Description: TODO
 */

public class UserServiceImpl implements UserService {

    @Value("${SESSION_KEY}")
    private String SESSION_KEY;
    @Value("${AVATAR_THUMB_DEFAULT_WIDTH}")
    private Integer AVATAR_THUMB_DEFAULT_WIDTH;
    @Value("${DEFAULT_NULL}")
    private String DEFAULT_NULL;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private JedisClient jedisClient;

    @Override
    public TripResult refreshInfo(String token, String encryptedData, String iv) {
        if(StringUtils.isBlank(encryptedData)||StringUtils.isBlank(iv)){
            return TripResult.build(400,"缺少参数");
        }
        //从缓存中获取session_key
        String session_key=jedisClient.hget( SESSION_KEY, token);
        if(StringUtils.isBlank(session_key)){
            return TripResult.build(400,"找不到session_key,刷新数据失败");
        }
        //////////////// 对encryptedData加密数据进行AES解密 ////////////////
        String result = null;
        try {
            result = AesCbcUtil.decrypt(encryptedData, session_key, iv, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
            return TripResult.build(400, "参数非法,解密失败");
        }
        if (StringUtils.isBlank(result)) {
            return TripResult.build(400, "密钥有误,解密失败");
        }
        //解密成功,获得信息
        Gson gson = new Gson();
        Map<String, Object> userInfoJSON = new HashMap<>();
        userInfoJSON = gson.fromJson(result, userInfoJSON.getClass());

        User newUserInfo = new User();
        newUserInfo.setOpenid((String) userInfoJSON.get("openId"));
        newUserInfo.setNickname((String) userInfoJSON.get("nickName"));
        newUserInfo.setSex((Double)userInfoJSON.get("gender") == 0 ? "0" : "1");
        newUserInfo.setProvince((String) userInfoJSON.get("province"));
        newUserInfo.setCity((String) userInfoJSON.get("city"));
        newUserInfo.setCountry((String) userInfoJSON.get("country"));
        newUserInfo.setUnionid((userInfoJSON.get("unionId")==null) ? "default" : (String) userInfoJSON.get("unionId"));
        //获取新头像
        String url = (String) userInfoJSON.get("avatarUrl");
        String avatar = ImageUtils.saveImage(url);
        String avatarThumb = ImageUtils.saveImage(ImageUtils.thumbnailImage(url, AVATAR_THUMB_DEFAULT_WIDTH), null);
        newUserInfo.setAvatar(avatar);
        newUserInfo.setAvaterThumb(avatarThumb);

        //通过openId,unionId判断是否存在user,符合其中之一即可
        UserExample userExample = new UserExample();
        userExample.or().andOpenidEqualTo(newUserInfo.getOpenid());
        userExample.or().andUnionidEqualTo(newUserInfo.getUnionid());

        List<User> useres = userMapper.selectByExample(userExample);
        if (useres.isEmpty()) {
            return TripResult.build(400,"数据库不存在此用户");
        } else {
            //老用户,更新信息到数据库
            // 删除旧头像
            User oldUserInfo = useres.get(0);
            String oldAvatar = oldUserInfo.getAvatar();
            String oldAvatarThumb = oldUserInfo.getAvaterThumb();
            if (!DEFAULT_NULL.equals(oldAvatar)) {
                ImageUtils.deleteImage(oldAvatar);
            }
            if (!DEFAULT_NULL.equals(oldAvatarThumb)) {
                ImageUtils.deleteImage(oldAvatarThumb);
            }
            newUserInfo.setId(useres.get(0).getId());
            userMapper.updateByPrimaryKeySelective(newUserInfo);
        }
        return TripResult.ok();
    }
}
