package com.taotao.cache.listener;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.taotao.cache.ZkSession.ZkSession;
import com.taotao.cache.model.Content;
import com.taotao.cache.model.Item;
import com.taotao.cache.service.CacheService;
import com.taotao.cache.utils.HttpClientUtils;
import com.taotao.cache.utils.JsonUtils;

/**
 * activemq监听是否有缓存信息变更
 * @author Administrator
 *
 */
public class UpdateMQListener implements MessageListener {
	
	@Autowired
	private CacheService cacheService;
	@Value("${getItemInfoUrl}")
	private String getItemInfoUrl;

	@Override
	public void onMessage(Message message) {
		try {
			TextMessage textMessage = (TextMessage)message;
			String typeAndItemId = textMessage.getText();
			
			//判断更新数据的类型
			String[] strArr = typeAndItemId.split(",");
			String type = strArr[0];
			long itemId = Long.parseLong(strArr[1]);
			
			//等待数据库的信息更新完成
			Thread.sleep(200);
			
			//商品信息的更新
			if(type.equals("itemInfo")) {
				updateItemInfo(itemId);
			}
			
			//广告内容的更新
			if(type.equals("contentInfo")) {
				
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 主动更新商品信息
	 */
	private void updateItemInfo(long itemId) {
		//调用商品服务，查询商品信息
		String url = getItemInfoUrl + itemId;
		Map<String, String> map = new HashMap<String, String>();
		map.put("", "");
		String response = HttpClientUtils.sendPostRequest(url, map);
		Item itemInfo = JsonUtils.jsonToPojo(response, Item.class);
		
		//更新本地内存数据
		cacheService.setItemInfo2Ehcache(itemInfo);
		
		//更新redis缓存中 的数据
		//获得分布式锁，解决多个服务实例并发更新下，导致数据更新不是最新版本的问题
		ZkSession zkSession = ZkSession.getInstance();
		zkSession.acquireZkLock(itemId);
		
		//从Redis缓存尝试获取到该商品信息的旧版本数据
		//如果当前数据是最新的，就更新Redis中的缓存，否则不做任何处理
		Item item = cacheService.getItemInfoFromRedis(itemId);
		if(item != null) {
			Date updatedTime = item.getUpdated();
			if(updatedTime.before(itemInfo.getUpdated())) {
				cacheService.setItemInfo2Redis(itemInfo);
			}
		} else {
			cacheService.setItemInfo2Redis(itemInfo);
		}
		
		//释放锁
		zkSession.realeaseZkLock(item.getId());
	}
	
	/**
	 * 主动更新广告内容
	 */
	private void updateContentInfo(long contentId) {
		
		//调用广告服务接口
		String contentUrl = "http://127.0.0.1:8084/getContentInfo";
		Map<String, String> map = new HashMap<String, String>();
		map.put("contentId", String.valueOf(contentId));
		String contentStr = HttpClientUtils.sendPostRequest(contentUrl, map);
		
		//更新本地缓存中的缓存数据
		cacheService.setContentInfo2Ehcache(JsonUtils.jsonToPojo(contentStr, Content.class));
		
		//更新redis缓存中 的数据
		//获得分布式锁，解决多个服务实例并发更新下，导致数据更新不是最新版本的问题
		ZkSession zkSession = ZkSession.getInstance();
		String path = "/contentId-lock-" + contentId;
		zkSession.acquireZkSessionLock(path);
		
		//从redis中尝试取出旧版本的数据
		Content content = cacheService.getContentInfoFromRedis(contentId);
		if(content != null) {
			if(content.getUpdated().
					before(JsonUtils.jsonToPojo(contentStr, Content.class).getUpdated())) {
				cacheService.setContentInfo2Redis(JsonUtils.jsonToPojo(contentStr, Content.class));
			}
		} else {
			cacheService.setContentInfo2Redis(JsonUtils.jsonToPojo(contentStr, Content.class));
		}
		
		//释放分布式锁
		zkSession.realeaseLock(path);
	}

}
