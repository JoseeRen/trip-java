package com.cppteam.app.controller;

import com.cppteam.common.util.TripResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

/**
 * Created by happykuan on 2017/11/6.
 */
@RestController
@RequestMapping(value = "/app/error")
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
        Enumeration<String> attributeNames = request.getAttributeNames();
        if(attributeNames.hasMoreElements()) {
            String attributeName = attributeNames.nextElement();
            Object attribute = request.getAttribute(attributeName);
            System.out.println("request.attribute['" + attributeName + "'] = " + attribute);

        }
        message = StringUtils.isBlank(message) ? "请求参数有误" : message;
        return TripResult.build(400, message);
    }

    @RequestMapping(value = "/403")
    public TripResult forbidden() {
        return TripResult.build(403, "禁止访问");
    }
}
