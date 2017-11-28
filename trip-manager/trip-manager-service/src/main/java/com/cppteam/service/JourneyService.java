package com.cppteam.service;

import com.cppteam.common.util.TripResult;
import com.cppteam.utils.TableResult;

/**
 * Created by happykuan on 2017/11/7.
 */
public interface JourneyService {

    /**
     * 获取游记列表
     * @param page
     * @param limit
     * @return
     */
    public TableResult listJourney(Integer page, Integer limit);

    /**
     * 审核游记
     * @param journeyId
     * @return
     */
    public Boolean checkJourney(String journeyId);

    /**
     * 查看游记详细信息
     * @param journeyId
     * @return
     */
    public TripResult viewJourney(String journeyId);

}
