package com.cppteam.controller;

import com.cppteam.common.util.TripResult;
import com.cppteam.service.JourneyService;
import com.cppteam.utils.TableResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by happykuan on 2017/11/7.
 */
@Controller
@RequestMapping(value = "/api/manager/journey")
public class JourneyController {

    @Autowired
    private JourneyService journeyService;

    @RequestMapping(value = "/list")
    @ResponseBody
    public TableResult listJourneys(Integer page, Integer limit) {
        return journeyService.listJourney(page, limit);
    }

    @RequestMapping(value = "/check")
    @ResponseBody
    public TripResult checkJourney(String journeyId) {
        boolean b = journeyService.checkJourney(journeyId);
        if (b) {
            return TripResult.ok();
        } else {
            return TripResult.build(405, "");
        }
    }

    @RequestMapping(value = "/view")
    @ResponseBody
    public TripResult viewJourney(String journeyId) {
        return journeyService.viewJourney(journeyId);
    }

}
