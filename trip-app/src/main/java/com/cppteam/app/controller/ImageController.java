package com.cppteam.app.controller;

import com.cppteam.app.service.ImageService;
import com.cppteam.common.util.TripResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * 图片控制器
 * Created by happykuan on 2017/10/30.
 * @author happykuan
 */
@RestController
@RequestMapping(value = "/app/image")
public class ImageController {

    @Autowired
    private ImageService imageService;

    /**
     *
     * @param image
     * @param width     生成缩略图的指定宽度
     * @return
     */
    @RequestMapping(value = "/upload")
    public TripResult uploadImage(@RequestParam(value = "image") MultipartFile image, Integer width) {
        return imageService.uploadImage(image, width);
    }
}
