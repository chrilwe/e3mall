package com.taotao.cache.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.cache.hystrix.GetContentInfoCommand;
import com.taotao.cache.hystrix.GetItemInfoCommand;
import com.taotao.cache.model.Content;
import com.taotao.cache.model.Item;
import com.taotao.cache.queue.ItemInfoCacheQueue;
import com.taotao.cache.service.CacheService;
import com.taotao.cache.utils.HttpClientUtils;
import com.taotao.cache.utils.JsonUtils;

/**
 * 缓存服务
 * @author Administrator
 *
 */
@Controller
public class CacheController {
	
	@Autowired
	private CacheService cacheService;
	@Value("${getItemInfoUrl}")
	private String getItemInfoUrl;
	
	/**
	 * 获取商品信息接口
	 * @param itemId
	 * @return
	 */
	@RequestMapping("/getItemInfo")
	@ResponseBody
	public Item getItemInfo(long itemId) {
		/**
		 * 请求通过nginx分发层，将请求打到nginx应用层，从nginx应用层的本地缓存获取
		 * 缓存信息，如果没有，发送请求到该接口
		 */
		//从redis缓存获取商品详情数据
		System.out.println("========日志======" + "从Redis缓存获取数据");
		Item itemInfo = cacheService.getItemInfoFromRedis(itemId);
		
		//Redis缓存中不存在数据，从ehcache中获取数据
		if(itemInfo == null) {
			System.out.println("========日志======" + "从ehcache缓存获取数据");
			itemInfo = cacheService.getItemInfoFromEhcache(itemId);
		}
		
		//从本地缓存中没有获取到数据，调用商品服务，从数据库中查询
		if(itemInfo == null) {
			System.out.println("========日志======" + "从源头服务获取数据");
			//利用hystrix对源头商品服务进行限流,防止并发量过大打死mysql
			GetItemInfoCommand command = new GetItemInfoCommand(itemId);
			itemInfo = command.execute();
			
			//重建缓存,将结果放到内存队列中，异步更新到redis缓存和本地缓存当中
			ItemInfoCacheQueue queue = ItemInfoCacheQueue.getInstance();
			queue.put(itemInfo);
		}
		
		return itemInfo;
	}
	
	/**
	 * 获取广告信息接口
	 */
	@RequestMapping("/getContentInfo")
	@ResponseBody
	public Content getContentInfoById(long contentId) {
		
		//首先尝试从redis缓存中获取数据
		Content content = cacheService.getContentInfoFromRedis(contentId);
		
		//如果从redis缓存中没有获取到数据，尝试从本地缓存中获取数据
		if(content == null) {
			content = cacheService.getContentInfoFromEhcache(contentId);
		}
		
		//如果从本地缓存中没有获取到数据，从源头服务获取数据
		if(content == null) {
			GetContentInfoCommand command = new GetContentInfoCommand(contentId);
			content = command.execute();
			
			//重建缓存
			
		}
		
		return content;
	}
}
