package com.cppteam.app.pojo;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.List;

/**
 * 行程提交表单
 * Created by happykuan on 2017/10/28.
 * @author happykuan
 */
public class JourneyForm implements Serializable {
    private static final long serialVersionUID = 296214656292558277L;

    private String id;

    /**
     * 游记名称，不能为空
     */
    @NotEmpty(message = "缺少参数：name")
    private String name;

    /**
     * 游记天数，至少为1
     */
    @Min(value = 1, message = "dayNum无效")
    private Integer dayNum;

    /**
     * 游记类型，0或1
     */
    @Pattern(regexp = "^[0-1]{1}$", message = "游记类型有误")
    private String type;

    /**
     * 图片，可以为空，否则必须满足格式
     */
    @Pattern(regexp = "^$|^group[1-9]{1}[0-9]*(\\/|\\\\)M([0-9]{2}(\\/|\\\\)){3}\\S+", message = "图片uri格式有误")
    private String img;

    @Pattern(regexp = "^$|^group[1-9]{1}[0-9]*(\\/|\\\\)M([0-9]{2}(\\/|\\\\)){3}\\S+", message = "图片uri格式有误")
    private String imgThumb;

    /**
     * 高校id，>=30 && <=3244
     */
    @DecimalMin(value = "30", message = "collegeCid无效")
    @DecimalMax(value = "3244", message = "collegeCid无效")
    private Integer collegeCid;

    @Valid
    private List<DayForm> days;

    public JourneyForm() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getDayNum() {
        return dayNum;
    }

    public void setDayNum(Integer dayNum) {
        this.dayNum = dayNum;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getCollegeCid() {
        return collegeCid;
    }

    public void setCollegeCid(Integer collegeCid) {
        this.collegeCid = collegeCid;
    }

    public List<DayForm> getDays() {
        return days;
    }

    public void setDays(List<DayForm> days) {
        this.days = days;
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
