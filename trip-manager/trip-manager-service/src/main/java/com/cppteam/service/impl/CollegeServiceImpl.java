package com.cppteam.service.impl;

import com.cppteam.common.util.JsonUtils;
import com.cppteam.common.util.TripResult;
import com.cppteam.dao.JedisClient;
import com.cppteam.mapper.CollegeMapper;
import com.cppteam.pojo.College;
import com.cppteam.pojo.CollegeExample;
import com.cppteam.service.CollegeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by happykuan on 2017/11/5.
 */
@Service
public class CollegeServiceImpl implements CollegeService {

    @Autowired
    private CollegeMapper collegeMapper;
    @Autowired
    private JedisClient jedisClient;

    @Value("${COLLEGE_LIST_KEY}")
    private String COLLEGE_LIST_KEY;

    @Override
    public TripResult cacheCollege() {

        CollegeExample collegeExample = new CollegeExample();
        List<College> colleges = collegeMapper.selectByExampleWithBLOBs(collegeExample);

        if (colleges.isEmpty()) {
            return TripResult.build(404, "empty set");
        }

        String json = JsonUtils.objectToJson(colleges);

        // 将json字符串放入redis中
        jedisClient.set(COLLEGE_LIST_KEY, json);

        return TripResult.ok("ok", json);
    }
}
