package com.cppteam.xcx.mapper;

import com.cppteam.xcx.pojo.Follower;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by happykuan on 2017/11/1.
 */
public interface FollowersMapper {

    /**
     * 通过行程的id获取该行程的跟随者信息
     * @param tripId
     * @return
     */
    List<Follower> getFollowersByTripId(@Param(value = "tripId") String tripId);
}
