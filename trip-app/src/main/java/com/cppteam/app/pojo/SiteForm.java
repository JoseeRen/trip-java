package com.cppteam.app.pojo;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.DecimalMax;
import java.io.Serializable;

/**
 * Created by happykuan on 2017/10/28.
 * @author happykuan
 */
public class SiteForm implements Serializable {

    private static final long serialVersionUID = -1344228520353749547L;
    /**
     * 地点、事件名称
     */
    @NotEmpty
    private String name;

    /**
     * 游玩时长
     */
    @DecimalMax(value = "24", message = "time不合法")
    private Float time;

    /**
     * 图片
     */
    private String img;
    private String imgThumb;

    /**
     * 第n个景点
     */
    private Integer count;
    /**
     * 攻略
     */
    private String tips;

    public SiteForm() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Float getTime() {
        return time;
    }

    public void setTime(Float time) {
        this.time = time;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getTips() {
        return tips;
    }

    public void setTips(String tips) {
        this.tips = tips;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getImgThumb() {
        return imgThumb;
    }

    public void setImgThumb(String imgThumb) {
        this.imgThumb = imgThumb;
    }
}
