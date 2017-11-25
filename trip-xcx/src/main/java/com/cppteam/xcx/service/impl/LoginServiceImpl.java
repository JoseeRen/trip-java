package com.cppteam.xcx.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cppteam.common.util.*;
import com.cppteam.dao.JedisClient;
import com.cppteam.mapper.UserMapper;
import com.cppteam.pojo.User;
import com.cppteam.pojo.UserExample;
import com.cppteam.xcx.service.LoginService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;

/**
 * 微信小程序登录接口实现
 * @author happykuan
 *
 */
@Service
public class LoginServiceImpl implements LoginService {

	@Value("${APP_ID}")
	private String APP_ID;
	@Value("${APP_SECRET}")
	private String APP_SECRET;
	@Value("${GRANT_TYPE}")
	private String GRANT_TYPE;
	@Value("${SESSION_KEY_URL}")
	private String SESSION_KEY_URL;

	@Value("${DEFAULT_NULL}")
	private String DEFAULT_NULL;
	@Value("${AVATAR_THUMB_DEFAULT_WIDTH}")
	private Integer AVATAR_THUMB_DEFAULT_WIDTH;

	@Value("${SESSION_KEY}")
	private String SESSION_KEY;

	@Autowired
	private UserMapper userMapper;
	@Autowired
	private JedisClient jedisClient;


    /**
     * 通过code获取用户登录凭证token
     * @param code      小程序发起登录请求获得的code
     * @return
     */
	@Override
	public TripResult getToken(String code) {
		// code 换取session_key
		Map<String, String> params = new HashMap<String, String>();
		params.put("appid", APP_ID);
		params.put("secret", APP_SECRET);
		params.put("js_code", code);
		params.put("grant_type", GRANT_TYPE);
		String resJson = HttpClientUtil.doPost(SESSION_KEY_URL, params);
		
		// 将json字符串转成map
		Gson gson = new Gson();
		Map<String, Object> map = new HashMap<String, Object>();
		map = gson.fromJson(resJson, map.getClass());
		
		// 成功获取openID, 返回token
		if (map.containsKey("openid")) {
			String openid = (String) map.get("openid");
			UserExample userExample = new UserExample();
			UserExample.Criteria criteria = userExample.createCriteria();
			criteria.andOpenidEqualTo(openid);
			List<User> users = userMapper.selectByExample(userExample);


			Map<String, Object> resultMap = new HashMap<String, Object>(2);
			String token = "";
			// 是否需要前端传递用户信息进行解密
			boolean needFillInfo = false;
			// 新使用用户
			if (users.isEmpty()) {
				String userId = IDGenerator.createUserId();
				User user = new User();
				user.setId(userId);
				user.setOpenid(openid);
				user.setNickname("default");
				user.setAvatar("default");
				user.setAvaterThumb("default");
				userMapper.insertSelective(user);
				token = JWTUtil.generateToken(userId);
				needFillInfo = true;
			} else {
				token = JWTUtil.generateToken(users.get(0).getId());
			}
			resultMap.put("token", token);
			resultMap.put("need_fill", needFillInfo);


			// 获取会话密钥（session_key）存入redis中供后续使用
			String sessionKey = (String) map.get("session_key");

			// 如果redis中已经有该用户的旧会话密钥，删除之
            if (StringUtils.isNotBlank(jedisClient.hget(SESSION_KEY, token))) {
                jedisClient.hdel(SESSION_KEY, token);
            }
            jedisClient.hset(SESSION_KEY, token, sessionKey);

			return TripResult.ok("ok", resultMap);
		}
		return TripResult.build(405, (String) map.get("errmsg"));
	}


	/**
	 * 根据token检测用户是否合法
	 */
	@Override
	public TripResult checkLoginStatus(String token) {
		String openid = JWTUtil.validToken(token);
		if (StringUtils.isBlank(openid)) {
			return TripResult.build(400, "token无效");
		}
		return TripResult.ok("ok", openid);
	}

}
