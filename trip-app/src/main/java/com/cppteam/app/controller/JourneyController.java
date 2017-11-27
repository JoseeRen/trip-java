package com.cppteam.app.controller;

import com.cppteam.app.pojo.JourneyForm;
import com.cppteam.app.service.JourneyService;
import com.cppteam.common.util.TripResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

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
