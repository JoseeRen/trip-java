package com.cppteam.service;

import com.cppteam.common.util.TripResult;

/**
 * Created by happykuan on 2017/11/5.
 */
public interface CollegeService {

    /**
     * 大学列表缓存到redis中
     * @return
     */
    public TripResult cacheCollege();

}
