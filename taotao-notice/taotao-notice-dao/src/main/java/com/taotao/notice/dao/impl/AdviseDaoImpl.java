package com.taotao.notice.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import com.taotao.notice.dao.AdviseDao;
import com.taotao.notice.dao.utils.JsonUtils;
import com.taotao.notice.pojo.Advise;
/**
 * 用户建议反馈持久层
 * @author Administrator
 *
 */
@Repository
public class AdviseDaoImpl implements AdviseDao {
	
	@Autowired
	private JedisPool jedisPool;

	@Override
	public void updateAdvise(Advise advise) {
		Jedis jedis = jedisPool.getResource();
		jedis.hset("advise", advise.getUserId()+"", JsonUtils.objectToJson(advise));
	}

	@Override
	public Advise findAdvise(long userId) {
		Jedis jedis = jedisPool.getResource();
		return JsonUtils.jsonToPojo(jedis.hget("advise", userId+""), Advise.class);
	}

}
