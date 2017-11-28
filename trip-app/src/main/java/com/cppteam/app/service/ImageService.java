package com.cppteam.app.service;

import com.cppteam.common.util.TripResult;
import org.springframework.web.multipart.MultipartFile;

/**
 * 图片服务接口
 * Created by happykuan on 2017/10/30.
 * @author happykuan
 */
public interface ImageService {


    /**
     * 上传单张图片
     * @param image         图片
     * @param targetWidth   可选，生成缩略图的宽度
     * @return
     */
    public TripResult uploadImage(MultipartFile image, Integer targetWidth);
}
