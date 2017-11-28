package com.cppteam.xcx.service.impl;

import com.cppteam.common.util.*;
import com.cppteam.dao.JedisClient;
import com.cppteam.mapper.FollowerMapper;
import com.cppteam.mapper.JourneyMapper;
import com.cppteam.mapper.SponsorMapper;
import com.cppteam.mapper.UserMapper;
import com.cppteam.pojo.*;
import com.cppteam.pojo.Follower;
import com.cppteam.xcx.mapper.DaysMapper;
import com.cppteam.xcx.mapper.FollowersMapper;
import com.cppteam.xcx.mapper.JourneysMapper;
import com.cppteam.xcx.pojo.*;
import com.cppteam.xcx.pojo.Day;
import com.cppteam.xcx.service.JourneyService;
import com.cppteam.xcx.service.UserService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by happykuan on 2017/10/31.
 * @author happykuan
 */
@Service
public class JourneyServiceImpl implements JourneyService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private JourneyMapper journeyMapper;
    @Autowired
    private SponsorMapper sponsorMapper;
    @Autowired
    private FollowerMapper followerMapper;
    @Autowired
    private FollowersMapper followersMapper;
    @Autowired
    private JourneysMapper journeysMapper;
    @Autowired
    private DaysMapper daysMapper;
    @Autowired
    private JedisClient jedisClient;

    @Autowired
    private UserService userService;


    @Value("${AVATAR_THUMB_DEFAULT_WIDTH}")
    private Integer AVATAR_THUMB_DEFAULT_WIDTH;
    @Value("${USER_TRIP_LIST_KEY}")
    private String USER_TRIP_LIST_KEY;
    @Value("${JOURNEY_CONTENT_BY_TRIPID}")
    private String JOURNEY_CONTENT_BY_TRIPID;
    @Value("${TRIP_FOLLOWERS}")
    private String TRIP_FOLLOWERS;

    @Value("${FOUND_JOURNEY_LIST_KEY}")
    private String FOUND_JOURNEY_LIST_KEY;
    @Value("${REDIS_EXPIRE_TIME}")
    private Integer REDIS_EXPIRE_TIME;
    @Value("${APP_USER_INFO_KEY}")
    private String APP_USER_INFO_KEY;
    @Value("${DEFAULT_NULL}")
    private String DEFAULT_NULL;

    /**
     * 类线程锁
     */
    private static final Class CLASS_LOCK = JourneyServiceImpl.class;

    /**
     * 发起一个行程。该行为将会使用户从journey表中拉取一篇游记创建一个属于用户的行程，成功后将会将创建信息存入sponsor表中<br>
     * sponsor表的主键则为该行程ID<br>
     * 用户发起行程时，数据库中对应的用户信息将会被更新至用户当前状态<br>
     *
     * @param token     用户唯一标识
     * @param journeyId 被添加为行程的游记ID
     * @return          tripId - 行程ID
     */
    @Override
    public TripResult createJourney(String token, String journeyId, User user) {
        // 校验信息
        if (StringUtils.isBlank(journeyId)) {
            return TripResult.build(400, "缺少参数：journeyId");
        }

        Journey journey = journeyMapper.selectByPrimaryKey(journeyId);
        if (journey == null) {
            return TripResult.build(404, "游记不存在");
        }
        // 验证是否已经发起了该行程
        String userId = JWTUtil.validToken(token);

        // 添加行程
        Sponsor sponsor = new Sponsor();
        String tripId = IDGenerator.createId();
        sponsor.setId(tripId);
        sponsor.setJourneyId(journeyId);
        sponsor.setUserId(userId);

        try {
            // 更新用户信息
            updateUserInfo(user, userId);

            // 插入创建者信息
            sponsorMapper.insertSelective(sponsor);

            Map<String, String> result = new HashMap<String, String>(1);
            result.put("tripId", tripId);

            return TripResult.ok("创建行程成功", result);
        } catch (Exception e) {
            e.printStackTrace();
            return TripResult.build(500, e.getMessage());
        }

    }

    /**
     * 跟随一个行程。该行为会使用户加入他人所发起的一个行程中
     * @param token      用户唯一标识
     * @param tripId     跟随的行程id，即sponsor表的主键
     * @return           添加状态 - 成功 or 失败
     */
    @Override
    public TripResult followJourney(String token, String tripId, User user) {
        if (StringUtils.isBlank(tripId)) {
            return TripResult.build(400, "请求参数有误");
        }

        Sponsor sponsor = sponsorMapper.selectByPrimaryKey(tripId);
        if (sponsor == null) {
            return TripResult.build(404, "行程不存在");
        }

        // 跟随者的id和发起者的id是否一样
        String user_id = sponsor.getUserId();
        String followerId = JWTUtil.validToken(token);
        if (user_id.equalsIgnoreCase(followerId)) {
            return TripResult.build(404, "不能跟随自己创建的行程");
        }

        // 是否已经跟随过
        FollowerExample followerExample = new FollowerExample();
        FollowerExample.Criteria followerExampleCriteria = followerExample.createCriteria();
        followerExampleCriteria.andSponsorIdEqualTo(tripId);
        followerExampleCriteria.andUserIdEqualTo(followerId);
        List<Follower> followers = followerMapper.selectByExample(followerExample);
        if (!followers.isEmpty()) {
            return TripResult.build(404, "已经跟随过该行程了");
        }

        // 插入信息
        Follower follower = new Follower();
        follower.setId(IDGenerator.createId());
        follower.setSponsorId(tripId);
        follower.setUserId(followerId);

        user.setId(followerId);
        try {

            // 插入跟随者信息
            followerMapper.insert(follower);

            // 更新用户信息
            updateUserInfo(user, followerId);

            // 更新行程对应跟随者缓存
            try {
                jedisClient.hdel(TRIP_FOLLOWERS, tripId);
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
     * 根据条件筛选游记列表
     *
     * @param collegeId 高校ID
     * @param type      行程类型
     * @param dayNum    行程天数
     * @return          符合条件的游记列表
     */
    @Override
    public TripResult findJourney(Integer collegeId, Integer type, Integer dayNum, Integer page, Integer count) {

        // 默认分页参数
        page = (page == null || page <= 0) ? 1 : page;
        count = (count == null || count <= 0) ? 10 : count;

        // 从缓存中获取缓存内容的key
        String cacheKey = collegeId + "_" + type + "_" + dayNum + "-" + page + "_" + count;


        // 先尝试在缓存中读取
        try {
            String obj = jedisClient.hget(FOUND_JOURNEY_LIST_KEY, cacheKey);
            if (StringUtils.isNotBlank(obj)) {
                Object result = SerializeUtil.unSerialize(obj);
                return TripResult.ok("获取成功", result);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 设置查询条件
        JourneyExample journeyExample = new JourneyExample();
        JourneyExample.Criteria criteria = journeyExample.createCriteria();
        List<Integer> args = new ArrayList<Integer>();

        // 1 -> 审核已通过且不对游记创建者隐藏; 2-> 审核已通过但对游记创建者隐藏
        args.add(1);
        args.add(2);
        criteria.andStatusIn(args);
        criteria.andDayNumEqualTo(dayNum);
        criteria.andTypeEqualTo(type + "");
        criteria.andCollegeCidEqualTo(collegeId);

        // 设置分页
        PageHelper.startPage(page, count);
        Page<Journey> pageInfo = (Page<Journey>) journeyMapper.selectByExample(journeyExample);

        // 取分页结果
        List<Journey> journeys = pageInfo.getResult();
        if (journeys.isEmpty()) {
            return TripResult.build(404, "查询为空");
        }

        // 删除冗余字段
        for(Journey journey: journeys) {
            journey.setStatus(null);
            journey.setCreatorId(null);
            journey.setCollegeCid(null);
            journey.setType(null);
            journey.setDayNum(null);
            journey.setCreateTime(null);
        }

        long total = pageInfo.getTotal();

        // 封装结果并返回
        Map<String, Object> result = new HashMap<String, Object>(3);
        result.put("page", page);
        result.put("total", total);
        result.put("list", journeys);

        // 查询结果加入缓存中
        try {
            synchronized (CLASS_LOCK) {
                Boolean exists = jedisClient.hexists(FOUND_JOURNEY_LIST_KEY, cacheKey);
                if (!exists) {
                    jedisClient.hset(FOUND_JOURNEY_LIST_KEY, cacheKey, SerializeUtil.serialize(result));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return TripResult.ok("获取成功", result);
    }

    /**
     * 列出用户加入的行程，按加入时间倒序排序
     * @param token         用户唯一标识
     * @return              用户添加的行程包括同行者列表
     */
    @Override
    public TripResult listTrip(String token, Integer page, Integer count) {

        String userId = JWTUtil.validToken(token);
        if (StringUtils.isBlank(userId)) {
            return TripResult.build(400, "请求参数有误");
        }

        PageHelper.startPage(page, count);

        // 数据库分页查询获取该用户的行程列表
        Page<JoinedJourney> pageInfo = (Page<JoinedJourney>) journeysMapper.getJourneysByUserId(userId);

        // 取分页结果
        long total = pageInfo.getTotal();
        int pageNum = pageInfo.getPageNum();
        List<JoinedJourney> journeys = pageInfo.getResult();
        if (journeys.isEmpty()) {
            return TripResult.build(404, "该用户没有加入任何行程");
        }

        // 封装结果
        Map<String, Object> result = new HashMap<>(3);
        result.put("page", pageNum);
        result.put("total", total);
        result.put("list", journeys);

        return TripResult.ok("获取行程列表成功", result);
    }

    /**
     * 根据行程id获取行程详情
     * @param token     用户唯一标识
     * @param tripId    行程ID
     * @return          tripId, days, followers
     */
    @Override
    public TripResult showTrip(String token, String tripId) {

        // 从缓存中通过行程id获取游记的详情
        List<Day> days = null;
        try {
            String obj = jedisClient.get(JOURNEY_CONTENT_BY_TRIPID + tripId);
            if (StringUtils.isNotBlank(obj)) {
                days = (List<Day>) SerializeUtil.unSerialize(obj);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 若缓存中没有该行程对应的游记详情，则根据行程id在数据库中获取游记的详情
        // 假如数据库中也没有该游记详情，则为缓存击穿现象，这将会在后续完善
        if (days == null || days.isEmpty()) {
            days = daysMapper.getDayByTripId(tripId);
            if (days.isEmpty()) {
                return TripResult.build(404, "游记不存在");
            }
            // 格式化行程详情返回内容
            for (Day day: days) {
                List<Site> sites = day.getSites();
                for (Site site: sites) {
                    if (StringUtils.isBlank(site.getImg())) {
                        site.setImg(DEFAULT_NULL);
                    }
                    if (StringUtils.isBlank(site.getImgThumb())) {
                        site.setImgThumb(DEFAULT_NULL);
                    }
                }
            }

            // 将游记详情存入缓存中
            try {
                synchronized (CLASS_LOCK) {
                    Boolean exists = jedisClient.exists(JOURNEY_CONTENT_BY_TRIPID + tripId);
                    if (!exists) {
                        jedisClient.set(JOURNEY_CONTENT_BY_TRIPID + tripId, SerializeUtil.serialize(days));
                        // 设置缓存过期时间为30分钟
                        jedisClient.expire(JOURNEY_CONTENT_BY_TRIPID + tripId, REDIS_EXPIRE_TIME);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // 从缓存中通过tripId获取该行程的跟随者
        List<com.cppteam.xcx.pojo.Follower> followers = null;
        try {
            String obj = jedisClient.get(TRIP_FOLLOWERS + tripId);
            if (StringUtils.isNotBlank(obj)) {
                followers = (List<com.cppteam.xcx.pojo.Follower>) SerializeUtil.unSerialize(obj);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 通过行程id在数据库中获取跟随用户列表
        if (followers == null || followers.isEmpty()) {
            followers = followersMapper.getFollowersByTripId(tripId);

            // 格式化跟随者返回内容，清除冗余数据
            for(com.cppteam.xcx.pojo.Follower follower: followers) {
                follower.setId(null);
                if (StringUtils.isBlank(follower.getAvatar())) {
                    follower.setAvatar(DEFAULT_NULL);
                }
                if (StringUtils.isBlank(follower.getAvaterThumb())) {
                    follower.setAvaterThumb(DEFAULT_NULL);
                }
            }

            // 将跟随者列表存入缓缓存中
            try {
                synchronized (CLASS_LOCK) {
                    Boolean exists = jedisClient.exists(TRIP_FOLLOWERS + tripId);
                    if (!exists) {
                        jedisClient.set(TRIP_FOLLOWERS + tripId, SerializeUtil.serialize(followers));
                        jedisClient.expire(TRIP_FOLLOWERS + tripId, REDIS_EXPIRE_TIME);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        // 封装结果集
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("days", days);
        result.put("tripId", tripId);
        result.put("followers", followers);

        return TripResult.ok("获取游记详情成功", result);
    }


    /**
     * 更新用户信息, 在用户创建、加入行程时调用
     * @param user      创建、跟随行程中表单提交的用户信息
     * @param userId    将被更新用户信息的用户ID
     */
    private void updateUserInfo(User user, String userId) {

        if (user == null) {
            return ;
        }

        // 获取表单用户名信息
        String nickname = StringUtils.isBlank(user.getNickname()) ? null : user.getNickname();

        // 获取表单头像信息url, 并将头像保存在本地服务器。avatar是一张网络图片
        String originalAvatar = user.getAvatar();

        if (StringUtils.isBlank(nickname) && StringUtils.isBlank(originalAvatar)) {
            return ;
        }

        // 新上传头像fileId默认为null, 假如没有上传图片或上传图片失败，则不会更新数据库中的值
        String avatar = null;
        String avatarThumb = null;
        if (StringUtils.isNotBlank(originalAvatar)) {
            // 上传头像
            avatar = ImageUtils.saveImage(originalAvatar);
            // 上传缩略图头像
            avatarThumb = ImageUtils.saveImage(ImageUtils.thumbnailImage(originalAvatar, AVATAR_THUMB_DEFAULT_WIDTH), null);

        }
        // 从数据库中获取用户原有信息
        user = userMapper.selectByPrimaryKey(userId);

        // 若原资料中有头像信息且上传头像不为空，将其数据库中保存的头像从服务器删除
        String oldAvatar = user.getAvatar();
        String oldAvatarThumb = user.getAvaterThumb();
        if (!"default".equals(oldAvatar) && StringUtils.isNotBlank(avatar)) {
            ImageUtils.deleteImage(oldAvatar);
        }
        if (!"default".equals(oldAvatarThumb) && StringUtils.isNotBlank(avatarThumb)) {
            ImageUtils.deleteImage(oldAvatarThumb);
        }

        // 更新数据
        user.setId(userId);
        user.setAvatar(avatar);
        user.setAvaterThumb(avatarThumb);
        user.setNickname(nickname);

        userMapper.updateByPrimaryKeySelective(user);

        // 同步用户信息缓存
        // 同步app端用户信息缓存
        try {
            jedisClient.hdel(APP_USER_INFO_KEY, userId);

            // 更新游记详情中的跟随者缓存
            userService.refreshFollowersCache(userId);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
