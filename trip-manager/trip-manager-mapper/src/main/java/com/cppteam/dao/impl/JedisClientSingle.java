package com.cppteam.dao.impl;

import com.cppteam.dao.JedisClient;
import org.springframework.beans.factory.annotation.Autowired;


import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Map;
import java.util.Set;

/**
 * jedis单机版实现
 */
public class JedisClientSingle implements JedisClient {
	
	@Autowired
	private JedisPool jedisPool; 
	
	@Override
	public String get(String key) {
		Jedis jedis = jedisPool.getResource();
		String string = jedis.get(key);
		jedis.close();
		return string;
	}

	@Override
	public String set(String key, String value) {
		Jedis jedis = jedisPool.getResource();
		String string = jedis.set(key, value);
		jedis.close();
		return string;
	}

	@Override
	public Boolean exists(String key) {
		Jedis jedis = jedisPool.getResource();
		Boolean exists = jedis.exists(key);
		jedis.close();
		return exists;
	}

	@Override
	public Set<String> keys(String pattern) {
		Jedis jedis = jedisPool.getResource();
		Set<String> keys = jedis.keys(pattern);
		jedis.close();
		return keys;
	}

	@Override
	public String hget(String hkey, String key) {
		Jedis jedis = jedisPool.getResource();
		String string = jedis.hget(hkey, key);
		jedis.close();
		return string;
	}

	@Override
	public Long hset(String hkey, String key, String value) {
		Jedis jedis = jedisPool.getResource();
		Long result = jedis.hset(hkey, key, value);
		jedis.close();
		return result;
	}

	@Override
	public Long incr(String key) {
		Jedis jedis = jedisPool.getResource();
		Long result = jedis.incr(key);
		jedis.close();
		return result;
	}

	/**
	 * 减1
	 *
	 * @param key
	 * @return
	 */
	@Override
	public Long decr(String key) {
		Jedis jedis = jedisPool.getResource();
		Long result = jedis.decr(key);
		jedis.close();
		return result;
	}

	@Override
	public Long expire(String key, int second) {
		Jedis jedis = jedisPool.getResource();
		Long result = jedis.expire(key, second);
		jedis.close();
		return result;
	}

	@Override
	public Long ttl(String key) {
		Jedis jedis = jedisPool.getResource();
		Long result = jedis.ttl(key);
		jedis.close();
		return result;
	}

	@Override
	public Long del(String key) {
		Jedis jedis = jedisPool.getResource();
		Long result = jedis.del(key);
		jedis.close();
		return result;
	}

	@Override
	public Long hdel(String hkey, String key) {
		Jedis jedis = jedisPool.getResource();
		Long result = jedis.hdel(hkey, key);
		jedis.close();
		return result;
	}

	@Override
	public Set<String> hkeys(String hkey) {
		Jedis jedis = jedisPool.getResource();
        Set<String> hkeys = jedis.hkeys(hkey);
        jedis.close();
		return hkeys;
	}

	@Override
	public Boolean hexists(String hkey, String field) {
		Jedis jedis = jedisPool.getResource();
		Boolean hexists = jedis.hexists(hkey, field);
		jedis.close();
		return hexists;
	}

	@Override
	public Long zadd(String key, Double score, String member) {
		Jedis jedis = jedisPool.getResource();
		Long zadd = jedis.zadd(key, score, member);
		jedis.close();
		return zadd;
	}

	@Override
	public Long zadd(String key, Map<String, Double> scoreMembers) {
		Jedis jedis = jedisPool.getResource();
		Long zadd = jedis.zadd(key, scoreMembers);
		jedis.close();
		return zadd;
	}

	@Override
	public Set<String> zrange(String key, Long start, Long end) {
		Jedis jedis = jedisPool.getResource();
		Set<String> zrange = jedis.zrange(key, start, end);

		jedis.close();
		return zrange;
	}

	@Override
	public Set<String> zrevrange(String key, Long start, Long end) {
		Jedis jedis = jedisPool.getResource();
		Set<String> zrevrange = jedis.zrevrange(key, start, end);
		jedis.close();
		return zrevrange;
	}

	@Override
	public Long zremrangeByRank(String key, Long start, Long end) {
		Jedis jedis = jedisPool.getResource();
		Long byRank = jedis.zremrangeByRank(key, start, end);
		jedis.close();
		return byRank;
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
		Jedis jedis = jedisPool.getResource();
		Long zrem = jedis.zrem(key, members);
		jedis.close();
		return zrem;
	}


}
