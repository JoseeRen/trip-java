package com.cppteam.common.util;

import java.util.UUID;

/**
 * id生成器
 * Created by happykuan on 2017/10/27.
 */
public class IDGenerator {

    /**
     * 获取用户id
     * @return
     */
    public static String createUserId() {
        return UUID.randomUUID().toString();
    }

    /**
     * 获取用户登录信息id
     * @return
     */
    public static String createUserWxId(){
        return IDGenerator.createUserId();
    }

    /**
     * 随便生成一个id
     * @return
     */
    public static String createId() {
        return IDGenerator.createUserId();
    }
}
