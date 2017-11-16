package com.cppteam.dao;

import java.util.Set;

/**
 * JedisClient简单封装接口
 * Created by happykuan on 2017/11/3.
 * @author happykuan
 */
public interface JedisClient {

    /**
     * key获取value
     * @param key
     * @return
     */
    String get(String key);

    /**
     * 存入键值对
     * @param key
     * @param value
     * @return
     */
    String set(String key, String value);

    /**
     * 取出hash键值对
     * @param hkey
     * @param key
     * @return
     */
    String hget(String hkey, String key);

    /**
     * 存放hash键值对
     * @param hkey
     * @param key
     * @param value
     * @return
     */
    Long hset(String hkey, String key, String value);

    /**
     * 加1
     * @param key
     * @return
     */
    Long incr(String key);

    /**
     * 设置过期时间
     * @param key
     * @param second
     * @return
     */
    Long expire(String key, int second);


    Long ttl(String key);

    /**
     * 删除一条记录
     * @param key
     * @return
     */
    Long del(String key);

    /**
     * 删除一条hash记录
     * @param hkey
     * @param key
     * @return
     */
    Long hdel(String hkey, String key);

    /**
     * 获得hash表中的所有字段
     * @param hkey
     * @return
     */
    Set<String> hkeys(String hkey);

}

