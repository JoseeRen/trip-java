package com.cppteam.dao.impl;

import com.cppteam.dao.JedisClient;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.JedisCluster;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

/**
 * Jedis客户端单机版实现
 * @author happykuan
 */
public class JedisClientCluster implements JedisClient {

	@Autowired
	private JedisCluster jedisCluster;
	
	@Override
	public String get(String key) {
		return jedisCluster.get(key);
	}

	@Override
	public String set(String key, String value) {
		return jedisCluster.set(key, value);
	}

	@Override
	public Boolean exists(String key) {
		return jedisCluster.exists(key);
	}

	@Override
	public Set<String> keys(String pattern) {
		return null;
	}

	@Override
	public String hget(String hkey, String key) {
		return jedisCluster.hget(hkey, key);
	}

	@Override
	public Long hset(String hkey, String key, String value) {
		return jedisCluster.hset(hkey, key, value);
	}

	@Override
	public Long incr(String key) {
		return jedisCluster.incr(key);
	}

	/**
	 * 减1
	 *
	 * @param key
	 * @return
	 */
	@Override
	public Long decr(String key) {
		return jedisCluster.decr(key);
	}

	@Override
	public Long expire(String key, int second) {
		return jedisCluster.expire(key, second);
	}

	@Override
	public Long ttl(String key) {
		return jedisCluster.ttl(key);
	}

	@Override
	public Long del(String key) {
		return jedisCluster.del(key);
	}

	@Override
	public Long hdel(String hkey, String key) {
		return jedisCluster.hdel(hkey, key);
	}

	@Override
	public Set<String> hkeys(String hkey) {
		return jedisCluster.hkeys(hkey);
	}

	@Override
	public Boolean hexists(String hkey, String field) {
		Boolean hexists = jedisCluster.hexists(hkey, field);
		try {
			jedisCluster.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return hexists;
	}

	/**
	 * 向有序列表中添加一个成员
	 *
	 * @param key
	 * @param score
	 * @param member
	 * @return
	 */
	@Override
	public Long zadd(String key, Double score, String member) {
		return jedisCluster.zadd(key, score, member);
	}

	/**
	 * 向有序列表中加入成员
	 *
	 * @param key
	 * @param scoreMembers
	 * @return
	 */
	@Override
	public Long zadd(String key, Map<String, Double> scoreMembers) {
		return jedisCluster.zadd(key, scoreMembers);
	}

	/**
	 * 指定范围在有序列表中获取一组成员，score升序
	 *
	 * @param key
	 * @param start
	 * @param end
	 * @return
	 */
	@Override
	public Set<String> zrange(String key, Long start, Long end) {
		return jedisCluster.zrange(key, start, end);
	}

	/**
	 * 指定范围在有序列表中获取一组成员，score降序
	 *
	 * @param key
	 * @param start
	 * @param end
	 * @return
	 */
	@Override
	public Set<String> zrevrange(String key, Long start, Long end) {
		return jedisCluster.zrevrange(key, start, end);
	}

	/**
	 * 移除有序列表中指定范围内的成员
	 *
	 * @param key
	 * @param start
	 * @param end
	 * @return
	 */
	@Override
	public Long zremrangeByRank(String key, Long start, Long end) {
		return jedisCluster.zremrangeByRank(key, start, end);
	}

	/**
	 * 移除有序列表中的指定元素
	 *
	 * @param key
	 * @param members
	 * @return
	 */
	@Override
	public Long zrem(String key, String... members) {
		return jedisCluster.zrem(key, members);
	}

}
