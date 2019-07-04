package com.taotao.content.request.impl;

import org.springframework.web.context.ContextLoader;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import com.taotao.content.model.Content;
import com.taotao.content.request.ContentRequest;
import com.taotao.content.service.ContentService;
import com.taotao.content.utils.JsonUtils;
/**
 * 封装广告内容读请求
 * @author Administrator
 *
 */
public class ReadContentRequestImpl implements ContentRequest {
	
	private long contentId;
	
	public ReadContentRequestImpl(long contentId) {
		this.contentId = contentId;
	}

	@Override
	public void process() {
		System.out.println("----------------------读请求后台线程--------------------");
		// 先从redis中读取数据，如果没有读到，从源头数据服务获取数据,并且更新到redis缓存当中
		JedisPool jedisPool = ContextLoader.getCurrentWebApplicationContext().getBean(JedisPool.class);
		ContentService contentService = ContextLoader.getCurrentWebApplicationContext().getBean(ContentService.class);
		Jedis jedis = jedisPool.getResource();
		String value = jedis.get("content_" + contentId);
		System.out.println("日志-------------》从缓存中查询广告内容contentId=" + contentId +":value");
		
		if(value == null) {
			Content content = contentService.findContentById(contentId);
			System.out.println("日志--------------》缓存中没有查到，调用广告服务查询contentId=" + contentId + "的数据为：" + content);
			if(content != null) {
				jedis.set("content_" + contentId, JsonUtils.objectToJson(content));
				System.out.println("日志------------》更新缓存中的数据");
			}
		}
		jedis.close();
		System.out.println("----------------------读请求后台线程--------------------");
	}

	@Override
	public long getContentId() {
		
		return contentId;
	}

}
