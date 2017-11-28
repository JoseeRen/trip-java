package com.cppteam.app.service;

import com.cppteam.common.util.TripResult;

/**
 * Created by happykuan on 2017/11/5.
 */
public interface CollegeService {

    /**
     * 获取大学json数据
     * @return
     */
    public TripResult listColleges();
}
