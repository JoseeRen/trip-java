package com.cppteam.service.impl;

import com.cppteam.common.util.TripResult;
import com.cppteam.mapper.DayMapper;
import com.cppteam.mapper.JourneyMapper;
import com.cppteam.mapper.SiteMapper;
import com.cppteam.pojo.*;
import com.cppteam.service.JourneyService;
import com.cppteam.utils.JourneyUtil;
import com.cppteam.utils.TableResult;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by happykuan on 2017/11/7.
 */
@Service
public class JourneyServiceImpl implements JourneyService {

    @Autowired
    private JourneyMapper journeyMapper;
    @Autowired
    private DayMapper dayMapper;
    @Autowired
    private SiteMapper siteMapper;

    /**
     * 分页获取游记列表
     * @param page
     * @param limit
     * @return
     */
    @Override
    public TableResult listJourney(Integer page, Integer limit) {

        // 设置分页
        PageHelper.startPage(page, limit);
        JourneyExample journeyExample = new JourneyExample();
        JourneyExample.Criteria criteria = journeyExample.createCriteria();

        // 筛选未被删除的记录
        criteria.andStatusNotEqualTo(3);
        List<Journey> journeys = journeyMapper.selectByExample(journeyExample);

        // 取分页结果
        Page<Journey> pageInfo = (Page<Journey>) journeys;
        List<Journey> list = pageInfo.getResult();
        long total = pageInfo.getTotal();

        TableResult result = new TableResult();
        result.setData(list);
        result.setCount(total);
        result.setCode(0);
        result.setMsg("");
        return result;
    }

    /**
     * 审核游记
     * @param journeyId
     * @return
     */
    @Override
    public Boolean checkJourney(String journeyId) {

        if (StringUtils.isBlank(journeyId)) {
            return false;
        }
        Journey journey = journeyMapper.selectByPrimaryKey(journeyId);
        if (journey == null) {
            return false;
        }

        Integer status = journey.getStatus();
        switch (status) {
            case 0:
                journey.setStatus(1);
                break;
            case 1:
                journey.setStatus(0);
                break;
            case 2:
                journey.setStatus(3);
                break;
            default:
                return false;
        }
        try{
            journeyMapper.updateByPrimaryKey(journey);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    @Override
    public TripResult viewJourney(String journeyId) {
        // 找出游记
        if (StringUtils.isBlank(journeyId)) {
            return TripResult.build(404, "");
        }

        Journey journey = journeyMapper.selectByPrimaryKey(journeyId);

        // 找出游记中的日程
        DayExample dayExample = new DayExample();
        DayExample.Criteria dayCriteria = dayExample.createCriteria();
        dayCriteria.andJourneyIdEqualTo(journeyId);
        // 升序查找
        dayExample.setOrderByClause("count asc");
        List<Day> dayList = dayMapper.selectByExample(dayExample);
        if (dayList.isEmpty()) {
            return TripResult.ok("", journey);
        }

        // 找出日程中的地点
        List<String> dayIds = new ArrayList<String>();
        for(Day day: dayList) {
            dayIds.add(day.getId());
        }
        SiteExample siteExample = new SiteExample();
        SiteExample.Criteria siteCriteria = siteExample.createCriteria();
        siteCriteria.andDayIdIn(dayIds);
        siteExample.setOrderByClause("count asc");
        List<Site> siteList = siteMapper.selectByExampleWithBLOBs(siteExample);
        if (siteList.isEmpty()) {
            JourneyUtil trip = new JourneyUtil(journey);
            for (Day d: dayList) {
                JourneyUtil day = new JourneyUtil(d);
                // 降序存入
                trip.add(day);
            }
            return TripResult.ok("", trip);
        }

        JourneyUtil trip = new JourneyUtil(journey);
        for (Day d: dayList) {
            JourneyUtil day = new JourneyUtil(d);
            for (Site s: siteList) {
                if (d.getId().equals(s.getDayId())) {
                    JourneyUtil site = new JourneyUtil(s);
                    day.add(site);
                }
            }
            trip.add(day);
        }

        return TripResult.ok("", trip);
    }

}
