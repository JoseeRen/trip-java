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
    @Value("${APP_JOURNEY_TOTAL}")
    private String APP_JOURNEY_TOTAL;
    @Value("${DEFAULT_NULL}")
    private String DEFAULT_NULL;

    @Override
    public TripResult listTrips(String token, Integer page, Integer count) {

        String userId = JWTUtil.validToken(token);
        page = page == null || page <= 0 ? 1 : page;
        count = count == null || count <= 0 ? 10 : count;

        // 从缓存中读取数据
        try {
            Set<String> set = jedisClient.zrevrange(APP_JOURNEY_LIST_KEY + userId, (long) ((page - 1) * count), (long) count);
            if (!set.isEmpty()) {
                // 从缓存中分页获取记录，并反序列化为对象装入List中
                List<Object> result = new LinkedList<>();
                for (String obj: set) {
                    result.add(SerializeUtil.unSerialize(obj));
                }
                Collections.reverse(result);

                // 从缓存中获取总记录数
                long total = Long.parseLong(jedisClient.get(APP_JOURNEY_TOTAL));

                // 返回缓存中的数据
                Map<String, Object> data = new HashMap<>(3);
                data.put("list", result);
                data.put("total", total);
                data.put("page", page);
                return TripResult.ok("ok", data);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 从数据库中查询数据
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
            return TripResult.build(404, "没有更多数据了");
        }

        // 取分页结果
        Page<Journey> listJourney = (Page<Journey>) journeys;
        List<Journey> result = listJourney.getResult();
        long total = listJourney.getTotal();
        int pageNum = listJourney.getPageNum();

        // 放入缓存中
        // key : `app_journey_list_key : userId `
        // member : 序列化的data
        try {
            Map<String, Double> scoreMembers = new HashMap<>();
            for (Journey journey: result) {
                // 使用时间戳作为score批量存放到redis中
                scoreMembers.put(SerializeUtil.serialize(journey), (double) journey.getCreateTime().getTime());
            }
            jedisClient.zadd(APP_JOURNEY_LIST_KEY + userId, scoreMembers);

            // 数据库记录总数缓存
            jedisClient.set(APP_JOURNEY_TOTAL + userId, total + "");
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 封装结果集
        Map<String, Object> data = new HashMap<>(3);

        data.put("page", pageNum);
        data.put("total", total);
        data.put("list", result);

        return TripResult.ok("ok", data);
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
                // 从缓存中清除该记录
                journey.setStatus(status);
                jedisClient.zrem(APP_JOURNEY_LIST_KEY + userId, SerializeUtil.serialize(journey));

                // 缓存记录数-1
                jedisClient.decr(APP_JOURNEY_TOTAL + userId);

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
        List<Day> dayList = new ArrayList<>();
        // siteList 用于批量insert
        List<Site> siteList = new ArrayList<>();
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

            // 更新status为原来的状态
            Journey record = new Journey();
            record.setId(journeyId);
            record.setStatus(status);
            journeyMapper.updateByPrimaryKeySelective(record);

            try {
                // 清除redis缓存
                // app用户的游记列表缓存, 清除所有
                jedisClient.zremrangeByRank(APP_JOURNEY_LIST_KEY + creatorId, 0L, -1L);

                // 更新小程序查询游记列表缓存, 将同校同类型同天数的缓存清除
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

        Integer collegeCid = journey.getCollegeCid();
        Integer dayNum = journey.getDayNum();
        String type = journey.getType();

        String keyPrefix = collegeCid + "_" + type + "_" + dayNum;
        Set<String> hkeys = jedisClient.hkeys(XCX_FOUND_JOURNEY_LIST_KEY);

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
