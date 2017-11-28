package com.cppteam.xcx.mapper;

import com.cppteam.xcx.pojo.Day;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by happykuan on 2017/11/1.
 */
public interface DaysMapper {

    /**
     * 通过游记id获取游记详情
     * @param journeyId
     * @return
     */
    List<Day> getDaysByJourney(@Param(value = "journeyId") String journeyId);

    /**
     * 通过行程id获取游记详情
     * @param tripId
     * @return
     */
    List<Day> getDayByTripId(@Param(value = "tripId") String tripId);
}
