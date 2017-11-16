package com.cppteam.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 路由控制器
 * Created by happykuan on 2017/11/8.
 * @author happykuan
 */
@Controller
@RequestMapping(value = "/manager")
public class RouterController {

    /**
     * 显示游记列表页
     * @return
     */
    @RequestMapping(value = "/journey/list")
    public String viewJourneysList() {
        return "journeysList";
    }

    @RequestMapping(value = "/journey/detail")
    public String viewJourneyDetail(Model model, String journeyId) {
        model.addAttribute("journeyId", journeyId);
        return "journeyDetail";
    }
}
