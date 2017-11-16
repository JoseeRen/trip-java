package com.cppteam.xcx.controller;

import com.cppteam.common.util.TripResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by happykuan on 2017/11/6.
 */
@RestController
@RequestMapping(value = "/xcx/error")
public class ErrorController {

    /**
     * 404
     * @return
     */
    @RequestMapping(value = "/404")
    public TripResult notFound(HttpServletRequest request) {
        return TripResult.build(404, "请求资源未找到");
    }

    @RequestMapping(value = "/400")
    public TripResult invalidParams(HttpServletRequest request) {
        String message = (String) request.getAttribute("javax.servlet.error.message");
        return TripResult.build(400, message);
    }

    @RequestMapping(value = "/403")
    public TripResult forbidden() {
        return TripResult.build(403, "禁止访问");
    }

    @RequestMapping(value = "/500")
    public TripResult serverErr() {
        return TripResult.build(500, "未知错误");
    }
}
