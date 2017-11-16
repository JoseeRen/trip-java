package com.cppteam.xcx.service;

import com.cppteam.common.util.TripResult;

/**
 * 小程序游记路线服务接口
 * Created by happykuan on 2017/10/31.
 */
public interface DayService {

    /**
     * 查看路线详情
     * @param journeyId
     * @return
     */
    public TripResult showDay(String journeyId);
}
