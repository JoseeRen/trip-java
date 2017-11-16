package com.cppteam.app.controller;

import com.cppteam.common.util.HttpClientUtil;
import com.cppteam.common.util.JsonUtils;
import com.cppteam.common.util.TripResult;
import com.google.gson.Gson;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 测试Controller
 * Created by happykuan on 2017/10/26.
 */
@RestController
public class TestController {

    @RequestMapping(value = "/test")
    public TripResult test() {
        return TripResult.ok("测试成功", new ArrayList());
    }

    @RequestMapping(value="/test/code/{code}")
    public TripResult testWechat(@PathVariable  String code) {
        String appid = "wx6c79bfcb3ad13461";
        String secret = "7a836a46ba53952387c9325591a3b523";
        String grantType = "authorization_code";
        Map<String, String > params = new HashMap<String, String>(4);
        params.put("appid", appid);
        params.put("secret", secret);
        params.put("code", code);
        params.put("grant_type", grantType);
        String result = HttpClientUtil.doGet("https://api.weixin.qq.com/sns/oauth2/access_token", params);
        Gson gson = new Gson();
        Map<String, Object> map = new HashMap<String, Object>(5);
        map = gson.fromJson(result, map.getClass());
        return TripResult.ok("ok", map);

    }

    @RequestMapping(value="/test2")
    public TripResult test2(@RequestHeader(value = "Authorization") String token) {
        return TripResult.ok("ok", token);
    }
}
