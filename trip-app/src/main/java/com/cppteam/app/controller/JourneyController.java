package com.cppteam.app.controller;

import com.cppteam.app.pojo.JourneyForm;
import com.cppteam.app.service.JourneyService;
import com.cppteam.common.util.TripResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Date;

/**
 * 游记Controller
 * Created by happykuan on 2017/10/28.
 * @author
 */
@RestController
@RequestMapping(value = "/app/journey")
public class JourneyController {

    @Autowired
    private JourneyService journeyService;
    /**
     * 列出用户添加的游记
     * @param token
     * @return
     */
    @RequestMapping(value = "/list")
    public TripResult listTrips(@RequestHeader("Authorization") String token, Integer page, Integer count) {
        return journeyService.listTrips(token, page, count);
    }

    /**
     *  APP
     *  获得给定创建时间后的游记列表
     * @param token
     * @param createTime 创建时间
     * @param count 返回条数
     * @return
     */
    @RequestMapping("/showCreatedByTime")
    public TripResult showCreatedByTime(@RequestHeader("Authorization") String token,String createTime,
                                        @RequestParam(value="count",required = false,defaultValue = "10") Integer count){
        if(createTime==null){
            return listTrips(token,1,count);
        }
        return journeyService.showCreatedByTime(token,new Date(Long.parseLong(createTime)),count);
    }

    /**
     * 添加一篇游记
     * @param journeyForm
     * @param token
     * @return
     */
    @RequestMapping(value = "/add")
    public TripResult addTrip(@RequestBody @Valid JourneyForm journeyForm, @RequestHeader("Authorization") String token) {

        return journeyService.addTrip(token, journeyForm);
    }

    /**
     * 删除一篇游记
     * @param journeyId
     * @param token
     * @return
     */
    @RequestMapping(value = "/delete/{journeyId}")
    public TripResult removeTrip(@PathVariable String journeyId, @RequestHeader("Authorization") String token) {
        return journeyService.removeTrip(token, journeyId);
    }

    /**
     * 获取游记详情
     * @param journeyId
     * @param token
     * @return
     */
    @RequestMapping(value = "/view/{journeyId}")
    public TripResult showJourney(@PathVariable String journeyId, @RequestHeader("Authorization") String token) {
        return journeyService.showJourney(token, journeyId);
    }

}
