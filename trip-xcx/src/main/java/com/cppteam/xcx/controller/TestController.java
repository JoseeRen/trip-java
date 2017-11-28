package com.cppteam.xcx.controller;

import com.cppteam.common.util.TripResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

/**
 * 测试Controller
 * Created by happykuan on 2017/10/26.
 */
@RestController
public class TestController {

    @RequestMapping(value = "/test")
    public TripResult test() {
        return TripResult.ok("测试成功", new ArrayList());
    }
}
