package com.cppteam.app.mapper;

import com.cppteam.app.pojo.JourneyForm;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Created by happykuan on 2017/11/27.
 */
public interface JourneysMapper {
    JourneyForm select(@Param("creatorId") String creatorId, @Param("journeyId") String journeyId);
}
