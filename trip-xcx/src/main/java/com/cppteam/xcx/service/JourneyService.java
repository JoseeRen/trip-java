package com.cppteam.xcx.service;

import com.cppteam.common.util.TripResult;
import com.cppteam.pojo.User;

/**
 * 游记服务接口
 * Created by happykuan on 2017/10/31.
 */
public interface JourneyService {

    /**
     * 发起一个行程
     * @param token
     * @param journeyId 被添加为行程的游记id
     * @return
     */
    public TripResult createJourney(String token, String journeyId, User user);
    
    /**
     * 加入一个行程
     * @param token
     * @param tripId
     * @param user
     * @return
     */
    public TripResult followJourney(String token, String tripId, User user);

    /**
     * 查找适合的行程
     *
     * @param collegeId
     * @param type      行程类型
     * @param dayNum    行程天数
     * @return
     */
    public TripResult findJourney(Integer collegeId, Integer type, Integer dayNum, Integer page, Integer count);

    /**
     * 列出用户加入的行程
     * @param token
     * @return
     */
    public TripResult listJourney(String token, Integer page, Integer count);


    /**
     * 查看行程详情
     * @param token
     * @param tripId  行程id, sponsor表的主键
     * @return
     */
    public TripResult showTrip(String token, String tripId);

}
