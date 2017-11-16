package com.cppteam.xcx.service.impl;

import com.cppteam.common.util.TripResult;
import com.cppteam.xcx.mapper.DaysMapper;
import com.cppteam.xcx.pojo.Day;
import com.cppteam.xcx.service.DayService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by happykuan on 2017/10/31.
 */
@Service
public class DayServiceImpl implements DayService {

    @Autowired
    private DaysMapper daysMapper;
    @Override
    public TripResult showDay(String journeyId) {
        if (StringUtils.isBlank(journeyId)) {
            return TripResult.build(400, "参数有误");
        }
        List<Day> days = daysMapper.getDaysByJourney(journeyId);
        if (days.isEmpty()) {
            return TripResult.build(404, "empty set");
        }
        return TripResult.ok("ok", days);
    }
}
