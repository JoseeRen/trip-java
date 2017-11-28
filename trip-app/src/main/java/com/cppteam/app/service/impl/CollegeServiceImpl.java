package com.cppteam.app.service.impl;

import com.cppteam.app.service.CollegeService;
import com.cppteam.common.util.TripResult;
import com.cppteam.dao.JedisClient;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Created by happykuan on 2017/11/5.
 */
@Service
public class CollegeServiceImpl implements CollegeService {

    @Autowired
    private JedisClient jedisClient;

    @Value("${COLLEGE_LIST_KEY}")
    private String COLLEGE_LIST_KEY;

    @Override
    public TripResult listColleges() {
        String colleges = jedisClient.get(COLLEGE_LIST_KEY);
        if (StringUtils.isBlank(colleges)) {
            return TripResult.build(404, "empty set");
        }
        return TripResult.ok("获取大学列表成功", colleges);
    }
}
