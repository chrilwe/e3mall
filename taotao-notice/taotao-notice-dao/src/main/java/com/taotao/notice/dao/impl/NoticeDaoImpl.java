package com.taotao.notice.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import com.taotao.notice.dao.NoticeDao;
import com.taotao.notice.dao.utils.JsonUtils;
import com.taotao.notice.pojo.Notice;
/**
 * 公告消息dao持久化到redis
 * @author Administrator
 *
 */
@Repository
public class NoticeDaoImpl implements NoticeDao {
	
	@Autowired
	private JedisPool jedisPool;

	@Override
	public void addNotice(Notice notice) {
		Jedis jedis = jedisPool.getResource();
		jedis.set("notice", notice.getMessage());
	}

	@Override
	public void updateNotice(Notice notice) {
		Jedis jedis = jedisPool.getResource();
		jedis.set("notice", notice.getMessage());
	}

	@Override
	public Notice findNotice() {
		Jedis jedis = jedisPool.getResource();
		return JsonUtils.jsonToPojo(jedis.get("notice"), Notice.class);
	}

}
