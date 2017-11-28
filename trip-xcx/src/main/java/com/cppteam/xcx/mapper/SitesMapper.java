package com.cppteam.xcx.mapper;

import com.cppteam.pojo.Site;

import java.util.List;

/**
 * Created by happykuan on 2017/11/1.
 */
public interface SitesMapper {

    /**
     * 通过日程id获取行程地点事件列表
     * @param dayId
     * @return
     */
    List<Site> getSitesByDay(String dayId);
}
