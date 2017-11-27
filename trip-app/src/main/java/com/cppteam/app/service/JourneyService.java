package com.cppteam.app.service;

import com.cppteam.app.pojo.JourneyForm;
import com.cppteam.common.util.TripResult;

import javax.servlet.http.HttpServletRequest;

/**
 * 游记服务接口
 * Created by happykuan on 2017/10/28.
 * @author happykuan
 */
public interface JourneyService {

    /**
     * 列出用户提交的游记
     * @param token
     * @param page      页码
     * @param count     显示条数
     * @return
     */
    public TripResult listTrips(String token, Integer page, Integer count);

    /**
     * 移除一个游记
     * @param token
     * @param journeyId
     * @return
     */
    public TripResult removeTrip(String token, String journeyId);

    /**
     * 添加一个游记
     * @param token
     * @param journeyForm
     * @return
     */
    public TripResult addTrip(String token, JourneyForm journeyForm);

    /**
     * 获取用户添加的游记详情
     * @param token
     * @param journeyId
     * @return
     */
    public TripResult showJourney(String token, String journeyId);
}
