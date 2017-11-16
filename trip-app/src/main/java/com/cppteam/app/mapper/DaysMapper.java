package com.cppteam.app.mapper;

import com.cppteam.pojo.Day;

import java.util.List;

/**
 * Created by happykuan on 2017/10/30.
 */
public interface DaysMapper {
    int batchInsert(List<Day> days);
}
