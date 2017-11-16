package com.cppteam.xcx.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cppteam.common.util.HttpClientUtil;
import com.cppteam.common.util.IDGenerator;
import com.cppteam.common.util.JWTUtil;
import com.cppteam.common.util.TripResult;
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

	@Autowired
	private UserMapper userMapper;
	/**
	 * 根据code获取用户登录token
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

			Map<String, String> token = new HashMap<String, String>();
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
				token.put("token", JWTUtil.generateToken(userId));
			} else {
				token.put("token", JWTUtil.generateToken(users.get(0).getId()));
			}

			return TripResult.ok("ok", token);
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
