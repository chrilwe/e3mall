package com.taotao.content.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.ContextLoader;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import com.taotao.content.mapper.ContentMapper;
import com.taotao.content.model.Content;
import com.taotao.content.model.ContentPageBean;
import com.taotao.content.queue.RoutRequestToQueue;
import com.taotao.content.request.ContentRequest;
import com.taotao.content.request.impl.ReadContentRequestImpl;
import com.taotao.content.request.impl.UpdateContentRequestImpl;
import com.taotao.content.service.ContentRequestService;
import com.taotao.content.service.ContentService;
import com.taotao.content.utils.JsonUtils;
/**
 * 广告内容更新或者读取服务(解决了读写并发的问题)
 * @author Administrator
 *
 */
@Service
public class ContentRequestServiceImpl implements ContentRequestService {
	
	@Autowired
	private ContentMapper contentMapper;
	/**
	 * 更新广告内容
	 */
	@Override
	public String updateContentRequest(Content content) {
		//封装请求
		ContentRequest request = new UpdateContentRequestImpl(content);
		
		//将请求路由到队列中
		RoutRequestToQueue rout = new RoutRequestToQueue(request);
		rout.routing();
		
		return "SUCCESS";
	}
	
	/**
	 * 读取广告内容
	 */
	@Override
	public Content readContentRequest(long contentId) {
		//封装请求
		ContentRequest request = new ReadContentRequestImpl(contentId);
		
		//将请求路由到队列中
		RoutRequestToQueue rout = new RoutRequestToQueue(request);
		rout.routing();
		
		//读取数据
		long waitTime = 0l;
		long startTime = System.currentTimeMillis();
		while(true) {
			//超时，不再从redis中获取，直接从数据库中获取
			if(waitTime > 200) {
				System.out.println("日志-----------》从redis查询contentId="+contentId+"超过200ms,不再从redis中查找");
				break;
			}
			
			//从redis中查询数据
			JedisPool jedisPool = ContextLoader.getCurrentWebApplicationContext().getBean(JedisPool.class);
			Jedis jedis = jedisPool.getResource();
			String value = jedis.get("content_" + contentId);
			jedis.close();
			if(value != null) {
				System.out.println("日志-------------》查询到contentId="+contentId+"的数据为："+value);
				return JsonUtils.jsonToPojo(value, Content.class);
			}
			
			waitTime = System.currentTimeMillis() - startTime;
		}
		
		//从数据库中获取
		ContentService contentService = ContextLoader.getCurrentWebApplicationContext().getBean(ContentService.class);
		Content content = contentService.findContentById(contentId);
		System.out.println("日志--------------》从数据库中查询contentId="+contentId+"的数据为："+content);
		
		return content;
	}
	
	/**
	 * 根据分类id查询，批量接口
	 */
	@Override
	public List<Content> readContentsRequest(long categoryId) {
		List<Content> contents = new ArrayList<Content>();
		//调用广告分类服务，查询该分类下的所有contentId
		List<Long> contentIds = contentMapper.findContentIdByCid(categoryId);
		System.out.println("日志---------------------》查询到contentCategory="+categoryId+"的contentIds="+contentIds.toString());
		
		if(contentIds != null && contentIds.size() > 0) {
			//根据id查询广告信息
			for (Long contentId : contentIds) {
				Content content = readContentRequest(contentId);
				contents.add(content);
			}
		}
		return contents;
	}

}
