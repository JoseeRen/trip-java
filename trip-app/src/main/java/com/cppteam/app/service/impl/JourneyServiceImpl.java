package com.cppteam.app.service.impl;

import com.cppteam.app.mapper.DaysMapper;
import com.cppteam.app.mapper.JourneysMapper;
import com.cppteam.app.mapper.SitesMapper;
import com.cppteam.app.pojo.DayForm;
import com.cppteam.app.pojo.JourneyForm;
import com.cppteam.app.pojo.SiteForm;
import com.cppteam.app.service.JourneyService;
import com.cppteam.common.util.IDGenerator;
import com.cppteam.common.util.JWTUtil;
import com.cppteam.common.util.SerializeUtil;
import com.cppteam.common.util.TripResult;
import com.cppteam.dao.JedisClient;
import com.cppteam.mapper.JourneyMapper;
import com.cppteam.pojo.*;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 游记服务实现类
 * Created by happykuan on 2017/10/28.
 * @author happykuan、lin
 */

@Service
public class JourneyServiceImpl implements JourneyService {

    @Autowired
    private JourneyMapper journeyMapper;
    @Autowired
    private JourneysMapper journeysMapper;
    @Autowired
    private DaysMapper daysMapper;

    @Autowired
    private SitesMapper sitesMapper;

    @Autowired
    private JedisClient jedisClient;

    @Value("${APP_JOURNEY_LIST_KEY}")
    private String APP_JOURNEY_LIST_KEY;
    @Value("${XCX_FOUND_JOURNEY_LIST_KEY}")
    private String XCX_FOUND_JOURNEY_LIST_KEY;
    @Value("${DEFAULT_NULL}")
    private String DEFAULT_NULL;

    @Override
    public TripResult listTrips(String token, Integer page, Integer count) {

        String userId = JWTUtil.validToken(token);
        page = page == null || page <= 0 ? 1 : page;
        count = count == null || count <= 0 ? 10 : count;

        // 从缓存中读取数据
        try {
            String s = jedisClient.hget(APP_JOURNEY_LIST_KEY + userId, page + "_" + count);
            if (StringUtils.isNotBlank(s)) {
                Object data = SerializeUtil.unSerialize(s);
                return TripResult.ok("获取成功", data);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        JourneyExample journeyExample = new JourneyExample();
        JourneyExample.Criteria criteria = journeyExample.createCriteria();

        criteria.andCreatorIdEqualTo(userId);

        List<Integer> args = new ArrayList<Integer>();
        // 未审核
        args.add(0);
        // 审核通过
        args.add(1);
        criteria.andStatusIn(args);

        // 按创建时间降序排序
        journeyExample.setOrderByClause("create_time desc");

        // 设置分页信息
        // 默认第一页，显示10条
        PageHelper.startPage(page, count);

        List<Journey> journeys = journeyMapper.selectByExample(journeyExample);
        if (journeys.isEmpty()) {
            return TripResult.build(404, "游记列表为空");
        }

        // 取分页结果
        Page<Journey> listJourney = (Page<Journey>) journeys;
        List<Journey> result = listJourney.getResult();
        long total = listJourney.getTotal();
        int pageNum = listJourney.getPageNum();

        // 封装结果集
        Map<String, Object> data = new HashMap<String, Object>();

        data.put("page", pageNum);
        data.put("total", total);
        data.put("list", result);


        // 放入缓存中
        // hkey : `app_journey_list_key : userId `
        // key : `page_count`
        // value : 序列化的data
        try {
            jedisClient.hset(APP_JOURNEY_LIST_KEY + userId, page + "_" + count, SerializeUtil.serialize(data));
        } catch (Exception e) {
            e.printStackTrace();
        }


        return TripResult.ok("获取成功", data);
    }

    /**
     * 删除游记
     * @param token
     * @param journeyId
     * @return
     */
    @Override
    public TripResult removeTrip(String token, String journeyId) {
        String userId = JWTUtil.validToken(token);
        Journey journey = journeyMapper.selectByPrimaryKey(journeyId);
        if (journey == null) {
            return TripResult.build(404, "游记不存在");
        }
        String creatorId = journey.getCreatorId();
        if(!creatorId.equalsIgnoreCase(userId)) {
            return TripResult.build(403, "非本人创建，不能删除");
        }

        Integer status = journey.getStatus();


        // 执行删除
        switch (status) {
            case 0:
                journey.setStatus(3);
                break;
            case 1:
                journey.setStatus(2);
                break;
            default:
                return TripResult.build(404, "游记不存在");
        }
        try {
            journeyMapper.updateByPrimaryKey(journey);

            try {
                // 清除redis缓存
                // app用户的游记列表缓存
                Set<String> hkeys = jedisClient.hkeys(APP_JOURNEY_LIST_KEY + userId);
                for (String key: hkeys) {
                    jedisClient.hdel(APP_JOURNEY_LIST_KEY + userId, key);
                }

                // 更新小程序查询游记列表缓存
                updateXcxJourneyListCache(journey);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return TripResult.ok();
        } catch (Exception e) {
            e.printStackTrace();
            return TripResult.build(500, e.getMessage());
        }
    }


    /**
     * 添加或更新一篇游记，无journeyId->创建；有则更新
     * @param token
     * @param journeyForm
     * @return
     */
    @Override
    public TripResult addTrip(String token, JourneyForm journeyForm) {
        String creatorId = JWTUtil.validToken(token);

        String journeyId = journeyForm.getId();

        // 被更新的游记的状态，默认为0未审核
        Integer status = 0;

        // 更新游记
        if (StringUtils.isNotBlank(journeyId)) {
            // 删除原来的游记
            Journey journey = journeyMapper.selectByPrimaryKey(journeyId);
            status = journey.getStatus();
            if (journey != null && journey.getCreatorId().equals(creatorId)) {
                journeyMapper.deleteByPrimaryKey(journeyId);
            } else {
                return TripResult.build(405, "update failed");
            }
        } else {
            journeyId = IDGenerator.createId();
        }

        // 添加游记
        // 处理journeyForm
        journeyForm.setId(journeyId);

        // 空配图转default
        String img = journeyForm.getImg();
        String imgThumb = journeyForm.getImgThumb();

        img = StringUtils.isBlank(img) ? DEFAULT_NULL : img;
        imgThumb = StringUtils.isBlank(imgThumb) ? DEFAULT_NULL : imgThumb;

        journeyForm.setImg(img);
        journeyForm.setImgThumb(imgThumb);

        Journey journey = new Journey();
        BeanUtils.copyProperties(journeyForm, journey, "days");
        journey.setCreatorId(creatorId);
        // 插入 journey
        try {
            journeyMapper.insertSelective(journey);
        } catch (Exception e) {
            e.printStackTrace();
            return TripResult.build(500, e.getMessage());
        }

        // 处理DayForm
        List<DayForm> days = journeyForm.getDays();
        if (days == null || days.isEmpty()) {
            return TripResult.ok();
        }

        // dayList 用于批量insert
        List<Day> dayList = new ArrayList<Day>();
        // siteList 用于批量insert
        List<Site> siteList = new ArrayList<Site>();
        for (DayForm dayForm: days) {
            Day day = new Day();
            BeanUtils.copyProperties(dayForm, day, "sites");
            String dayId = IDGenerator.createId();
            day.setId(dayId);
            day.setJourneyId(journeyId);

            // 加入到dayList中
            dayList.add(day);

            // 处理sites
            List<SiteForm> sites = dayForm.getSites();
            if (sites == null || sites.isEmpty()) {
                continue;
            }

            for (SiteForm siteForm: sites) {
                Site site = new Site();
                String siteId = IDGenerator.createId();
                BeanUtils.copyProperties(siteForm, site);
                site.setId(siteId);
                site.setDayId(dayId);

                // 空配图转default
                String img1 = site.getImg();
                String imgThumb1 = site.getImgThumb();

                img1 = StringUtils.isBlank(img1) ? DEFAULT_NULL : img1;
                imgThumb1 = StringUtils.isBlank(imgThumb1) ? DEFAULT_NULL : imgThumb1;

                site.setImg(img1);
                site.setImgThumb(imgThumb1);
                // 加入到siteList中
                siteList.add(site);
            }
        }

        // 批量插入 days 和 sites
        try {
            daysMapper.batchInsert(dayList);
            if (!siteList.isEmpty()) {
                sitesMapper.batchInsert(siteList);
            }

            // 更新status
            Journey record = new Journey();
            record.setId(journeyId);
            record.setStatus(status);
            journeyMapper.updateByPrimaryKeySelective(record);

            try {
                // 清除redis缓存
                // app用户的游记列表缓存
                Set<String> hkeys = jedisClient.hkeys(APP_JOURNEY_LIST_KEY + creatorId);
                for (String key: hkeys) {
                    jedisClient.hdel(APP_JOURNEY_LIST_KEY + creatorId, key);
                }

                // 更新小程序查询游记列表缓存
                updateXcxJourneyListCache(journey);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return TripResult.ok();
        } catch (Exception e) {
            e.printStackTrace();
            return TripResult.build(500, e.getMessage());
        }
    }

    /**
     * 获取用户的游记详情
     * @param token
     * @param journeyId
     * @return
     */
    @Override
    public TripResult showJourney(String token, String journeyId) {
        String userId = JWTUtil.validToken(token);
        JourneyForm journeyForm = journeysMapper.select(userId, journeyId);

        if (journeyForm == null) {
            return TripResult.build(404, "not found");
        }

        return TripResult.ok("ok", journeyForm);
    }


    /**
     * 更新小程序查询游记列表缓存
     * @param journey collegeId, dayNum, type
     */
    private void updateXcxJourneyListCache(Journey journey) {
        Set<String> hkeys;// 更新小程序查找游记列表的缓存
        Integer collegeCid = journey.getCollegeCid();
        Integer dayNum = journey.getDayNum();
        String type = journey.getType();
        String keyPrefix = collegeCid + "_" + type + "_" + dayNum;
        hkeys = jedisClient.hkeys(XCX_FOUND_JOURNEY_LIST_KEY);
        // key的定义：collegeId + "_" + type + "_" + dayNum + "-" + page + "_" + count
        for (String key: hkeys) {
            // 如果key的collegeId、type、dayNum与该篇删除的游记相同，则刷新该key下的缓存
            String k = key;
            String prefix = k.substring(0, key.indexOf("-"));
            if (keyPrefix.equals(prefix)) {
                jedisClient.hdel(XCX_FOUND_JOURNEY_LIST_KEY, key);
            }
        }
    }
}
