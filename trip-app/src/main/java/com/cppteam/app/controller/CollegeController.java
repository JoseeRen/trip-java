package com.cppteam.app.controller;

import com.cppteam.app.service.CollegeService;
import com.cppteam.common.util.TripResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by happykuan on 2017/11/5.
 */
@RestController
@RequestMapping(value = "/app/college")
public class CollegeController {

    @Autowired
    private CollegeService collegeService;

    @RequestMapping(value = "/list_all")
    public TripResult getColleges() {
        return collegeService.listColleges();
    }
}
