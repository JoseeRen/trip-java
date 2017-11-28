package com.cppteam.xcx.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 自定义pojo，用于查询用户已经加入的行程返回结果
 * Created by happykuan on 2017/11/1.
 * @author happykuan
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class JoinedJourney implements Serializable{
    private static final long serialVersionUID = 7906219203401854837L;
    private String id;
    private String nickname;
    private String avatar;
    private String avaterThumb;
    private String name;
    private Integer dayNum;
    private String img;
    private String imgThumb;
    private Date joinTime;
    private List<Follower> followers;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getAvaterThumb() {
        return avaterThumb;
    }

    public void setAvaterThumb(String avaterThumb) {
        this.avaterThumb = avaterThumb;
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

    public Date getJoinTime() {
        return joinTime;
    }

    public void setJoinTime(Date joinTime) {
        this.joinTime = joinTime;
    }

    public List<Follower> getFollowers() {
        return followers;
    }

    public void setFollowers(List<Follower> followers) {
        this.followers = followers;
    }
}
