package com.taotao.cache.hystrix;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandProperties;
import com.netflix.hystrix.HystrixThreadPoolProperties;
import com.taotao.cache.model.Item;
import com.taotao.cache.utils.HttpClientUtils;
import com.taotao.cache.utils.JsonUtils;

/**
 * 商品服务限流
 * @author Administrator
 *withCoreSize：设置你的线程池的大小
 *withMaxQueueSize：设置的是你的等待队列，缓冲队列的大小
 *withQueueSizeRejectionThreshold：如果withMaxQueueSize<withQueueSizeRejectionThreshold，
 *那么取的是withMaxQueueSize，反之，取得是withQueueSizeRejectionThreshold
 */
public class GetItemInfoCommand extends HystrixCommand<Item> {

	private long itemId;
	
	@Value("${getItemInfoUrl}")
	private String getItemInfoUrl;

	public GetItemInfoCommand(long itemId) {
		super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("ProductService"))
				.andThreadPoolPropertiesDefaults(HystrixThreadPoolProperties.Setter()
						.withCoreSize(10)
						.withMaximumSize(30) 
						.withAllowMaximumSizeToDivergeFromCoreSize(true) 
						.withKeepAliveTimeMinutes(1) 
						.withMaxQueueSize(50)
						.withQueueSizeRejectionThreshold(100))
				.andCommandPropertiesDefaults(HystrixCommandProperties.Setter()
						.withExecutionTimeoutInMilliseconds(50000))
						
				);
		this.itemId = itemId;
	}

	@Override
	protected Item run() throws Exception {
		String url = getItemInfoUrl + itemId;
		Map<String, String> map = new HashMap<String,String>();
		String response = HttpClientUtils.sendPostRequest(url, map);
		
		Item itemInfo = JsonUtils.jsonToPojo(response, Item.class);
		//解决缓存穿透的问题，如果从mysql数据库没有查到数据，直接返回空的数据
		if(itemInfo == null) {
			itemInfo.setId(itemId);
		}
		return itemInfo;
	}
	
	/**
	 * 降级机制
	 */
	@Override
	protected Item getFallback() {
		//返回拼接的值
		Item itemInfo = new Item();
		itemInfo.setId(itemId);
		return itemInfo;
	}

}
