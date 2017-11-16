package com.cppteam.xcx.service;

import com.cppteam.common.util.TripResult;

/**
 * 小程序游记地点、事件详情服务
 * Created by happykuan on 2017/10/31.
 */
public interface SiteService {
    /**
     * 查看某个地点、事件的详情
     * @param siteId
     * @return
     */
    public TripResult showSite(String siteId);
}
