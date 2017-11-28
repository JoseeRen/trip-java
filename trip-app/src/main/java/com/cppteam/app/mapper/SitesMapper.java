package com.cppteam.app.mapper;

import com.cppteam.app.pojo.SiteForm;
import com.cppteam.pojo.Site;
import org.apache.ibatis.annotations.Param;

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

    /**
     * dayId获取地点详情
     * @param dayId
     * @return
     */
    List<SiteForm> selectByDayId(@Param("dayId") String dayId);
}
