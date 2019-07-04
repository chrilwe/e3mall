package com.taotao.cache.thread;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.taotao.cache.ZkSession.ZkSession;
import com.taotao.cache.model.Item;
import com.taotao.cache.service.CacheService;
import com.taotao.cache.utils.HttpClientUtils;
import com.taotao.cache.utils.JsonUtils;

/**
 * 缓存预热线程
 * @author Administrator
 *
 */
public class RewarmCacheThread implements Runnable{
	
	@Autowired
	private CacheService cacheService;

	@Override
	public void run() {
		
		//从zookeeper节点中取出taskidlist
		ZkSession zkSession = ZkSession.getInstance();
		String taskIdListPath = "/taskId-list";
		String taskIdList = zkSession.getDataNode(taskIdListPath);
		String[] taskIds = taskIdList.split(",");
		
		for (String taskId : taskIds) {
			//获取taskid锁，如果没有获得锁，说明其他线程已经在预热了，忽略操作
			String taskIdLockPath = "/taskid-lock-" + taskId;
			boolean result = zkSession.acquireZkSessionLock(taskIdLockPath);
			if(!result) {
				continue;
			}
			
			//获得商品是否更新状态锁
			String taskIdStatusLockPath = "/taskid-status-lock-" + taskId;
			zkSession.acquireZkSessionLock(taskIdStatusLockPath);
			
			//从zookeeper节点中获取状态信息
			String taskStatusPath = "/taskid-status-" + taskId;
			String status = zkSession.getDataNode(taskStatusPath);
			if(status.equals("")) {
				//没有预热
				//从zookeeper节点中取出商品id list
				//调用商品服务，查询商品信息
				//将商品信息更新到本地缓存和Redis缓存中
				String itemIdList = zkSession.getDataNode("/itemInfo-lock-" + taskId);
				List<String> itemIds = JsonUtils.jsonToList(itemIdList, String.class);
				for (String itemId : itemIds) {
					Map<String, String> map = new HashMap<String, String>();
					String response = HttpClientUtils.sendPostRequest("http://127.0.0.1:8080/getItemInfo/" + itemId, map);
					
					cacheService.setItemInfo2Ehcache(JsonUtils.jsonToPojo(response, Item.class));
					cacheService.setItemInfo2Redis(JsonUtils.jsonToPojo(response, Item.class));
				}
				
				//将节点更新状态改为success
				zkSession.createNode(taskStatusPath);
				zkSession.setDataNode(taskStatusPath, "success");
			}
			
			//释放锁
			zkSession.realeaseLock(taskIdStatusLockPath);
			zkSession.realeaseLock(taskIdLockPath);
		}
	}

}
