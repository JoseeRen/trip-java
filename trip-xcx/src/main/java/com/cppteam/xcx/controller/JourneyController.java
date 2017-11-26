package com.cppteam.xcx.controller;

import com.cppteam.common.util.TripResult;
import com.cppteam.pojo.User;
import com.cppteam.xcx.service.JourneyService;
import com.cppteam.xcx.service.SiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by happykuan on 2017/11/1.
 */
@RestController
@RequestMapping(value = "/xcx/journey")
public class JourneyController {

    @Autowired
    private JourneyService journeyService;
    @Autowired
    private SiteService siteService;

    /**
     * 列出用户加入的行程
     * @param token
     * @return
     */
    @RequestMapping(value = "/list")
    public TripResult listTrip(@RequestHeader(value = "Authorization") String token, Integer page, Integer count) {

        // 为page和count设置默认值
        page = (page == null || page <= 0) ? 1 : page;
        count = (count == null || count <= 0) ? 5 : count;
        return journeyService.listTrip(token, page, count);
    }

    /**
     * 发起一个行程
     * @param journeyId 被添加为行程的游记id
     * @param token
     * @param user 当前用户
     * @return
     */
    @RequestMapping("/create")
    public TripResult createJourney(@RequestHeader(value = "Authorization")String token, @RequestParam("journeyId") String journeyId, User user){
        System.out.println(user.getNickname());
        System.out.println(user.getAvatar());
        return journeyService.createJourney(token,journeyId,user);
    }
    /**
     * 跟随一个行程
     * @param token
     * @param tripId        已被发起的行程id, 即sponsor表的Id!
     * @param user 当前用户
     * @return
     */
    @RequestMapping("/follow")
    public TripResult followJourney(@RequestHeader(value = "Authorization") String token,@RequestParam(value = "tripId") String tripId,  User user){
        return journeyService.followJourney(token, tripId, user);
    }

    /**
     * 查找适合的行程
     * @param type      行程类型
     * @param dayNum    行程天数
     * @return
     */
    @RequestMapping("/find")
    public TripResult findJourney(@RequestHeader(value = "Authorization") String token,
                                  @RequestParam("collegeId") Integer collegeId,
                                  @RequestParam("type") Integer type,
                                  @RequestParam("dayNum") Integer dayNum,
                                  Integer page,
                                  Integer count){
        return journeyService.findJourney(collegeId, type, dayNum, page, count);
    }

    /**
     * 查看行程详情
     * @param token
     * @param tripId
     * @return
     */
    @RequestMapping("/show")
    public TripResult showJourney(@RequestHeader(value = "Authorization") String token,@RequestParam("tripId") String tripId){
        return journeyService.showTrip(token, tripId);
    }
    /**
     * 获取地点详细信息
     * @param siteId
     * @return
     */
    @RequestMapping("/show_site")
    public TripResult showSite(@RequestHeader(value = "Authorization") String token,@RequestParam("siteId") String siteId){
        return siteService.showSite(siteId);
    }
}
