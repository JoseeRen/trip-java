package com.cppteam.xcx.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

/**
 * Created by happykuan on 2017/11/1.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Follower implements Serializable{
    private static final long serialVersionUID = 4770831164132096552L;
    private String id;
    private String nickname;
    private String avatar;
    private String avaterThumb;

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
}
