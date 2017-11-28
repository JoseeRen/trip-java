package com.cppteam.controller;

import com.cppteam.common.util.TripResult;
import com.cppteam.service.CollegeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by happykuan on 2017/11/5.
 */
@RestController
@RequestMapping(value = "/manager/college")
public class CollegeController {

    @Autowired
    private CollegeService collegeService;

    @RequestMapping(value = "/cache_all")
    public TripResult cacheCollege() {
        return collegeService.cacheCollege();
    }
}
