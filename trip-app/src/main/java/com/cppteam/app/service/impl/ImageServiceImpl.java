package com.cppteam.app.service.impl;

import com.cppteam.app.service.ImageService;
import com.cppteam.common.util.ImageUtils;
import com.cppteam.common.util.TripResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 图片服务接口实现
 * Created by happykuan on 2017/10/30.
 * @author happykuan
 */
@Service
public class ImageServiceImpl implements ImageService {

    @Value("${IMG_SERVER_BASE_URL}")
    private String IMG_SERVER_BASE_URL;
    @Value("${UPLOAD_THUMB_DEFAULT_WIDTH}")
    private Integer UPLOAD_THUMB_DEFAULT_WIDTH;
    /**
     * 单图上传
     * @param image
     * @return
     */
    @Override
    public TripResult uploadImage(MultipartFile image, Integer targetWidth) {
        // 检验图片
        if (image == null) {
            return TripResult.build(400, "图片为空");
        }
        String originalFilename = image.getOriginalFilename();
        if (!ImageUtils.isLegalFilename(originalFilename)) {
            return TripResult.build(400, "图片格式不合法");
        }
        // 缩略图宽度，默认宽度为200px
        targetWidth = targetWidth == null || targetWidth == 0 ? UPLOAD_THUMB_DEFAULT_WIDTH : targetWidth;
        String originImg = null;
        String thumbImg = null;
        try {
            // 获取图片字节数组
            byte[] imageBytes = image.getBytes();
            // 上传原图
            originImg = ImageUtils.saveImage(imageBytes, originalFilename);
            // 上传缩略图
            String formatName = ImageUtils.getImgExtName(originalFilename);
            thumbImg = ImageUtils.saveImage( ImageUtils.thumbnailImage(imageBytes, formatName, targetWidth), originalFilename );

        } catch (IOException e) {
            e.printStackTrace();
        }
        if (StringUtils.isBlank(originImg)) {
            return TripResult.build(405, "图片上传失败");
        }

        // 封装结果
        Map<String, String> result = new HashMap<String, String>(2);
        result.put("baseUrl", IMG_SERVER_BASE_URL);
        result.put("imgUri", originImg);
        // 如果生成了缩略图，返回缩略图地址
        if (thumbImg != null) {
            result.put("imgThumbUri", thumbImg);
        }

        return TripResult.ok("图片上传成功", result);
    }
}
