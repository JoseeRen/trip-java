package com.cppteam.xcx.mapper;

import com.cppteam.pojo.User;
import com.cppteam.xcx.pojo.JoinedJourney;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by happykuan on 2017/11/1.
 * @author happykuan
 */
public interface JourneysMapper {

    List<JoinedJourney> getJourneysByUserId(@Param(value = "userId") String userId);


}
