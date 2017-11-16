package com.cppteam.app.mapper;

import com.cppteam.pojo.Site;

import java.util.List;

/**
 * 自定义sitemapper
 * Created by happykuan on 2017/10/30.
 * @author happykuan
 */
public interface SitesMapper {
    /**
     * 批量insert site
     * @param sites site list
     * @return 受影响条数
     */
    int batchInsert(List<Site> sites);
}
