package com.cppteam.app.mapper;

import com.cppteam.app.pojo.DayForm;
import com.cppteam.pojo.Day;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * Created by happykuan on 2017/10/30.
 */
public interface DaysMapper {
    int batchInsert(List<Day> days);
    List<DayForm> select(@Param(value = "journeyId") String journeyId);
}
