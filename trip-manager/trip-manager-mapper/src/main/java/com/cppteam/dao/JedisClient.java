package com.cppteam.dao;

import java.util.Map;
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
     * 某个key是否存在
     * @param key
     * @return
     */
    Boolean exists(String key);

    /**
     *
     * @param pattern
     * @return
     */
    Set<String> keys(String pattern);

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
     * 减1
     * @param key
     * @return
     */
    Long decr(String key);

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

    /**
     * 查看hash表key中，给定域field是否存在
     * @param hkey
     * @param field
     * @return
     */
    Boolean hexists(String hkey, String field);

    /**
     * 向有序列表中添加一个成员
     * @param key
     * @param score
     * @param member
     * @return
     */
    Long zadd(String key, Double score, String member);

    /**
     * 向有序列表中加入成员
     * @param key
     * @param scoreMembers
     * @return
     */
    Long zadd(String key, Map<String, Double> scoreMembers);

    /**
     * 指定范围在有序列表中获取一组成员，score升序
     * @param key
     * @param start
     * @param end
     * @return
     */
    Set<String> zrange(String key, Long start, Long end);

    /**
     * 指定范围在有序列表中获取一组成员，score降序
     * @param key
     * @param start
     * @param end
     * @return
     */
    Set<String> zrevrange(String key, Long start, Long end);

    /**
     * 移除有序列表中指定范围内的成员
     * @param key
     * @param start
     * @param end
     * @return
     */
    Long zremrangeByRank(String key,Long start, Long end);

    /**
     * 移除有序列表中的指定元素
     * @param key
     * @param members
     * @return
     */
    Long zrem(String key, String... members);

}

