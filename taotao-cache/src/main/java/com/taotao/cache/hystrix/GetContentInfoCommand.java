package com.taotao.cache.hystrix;

import java.util.HashMap;
import java.util.Map;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandProperties;
import com.netflix.hystrix.HystrixThreadPoolProperties;
import com.netflix.hystrix.HystrixCommand.Setter;
import com.taotao.cache.model.Content;
import com.taotao.cache.utils.HttpClientUtils;
import com.taotao.cache.utils.JsonUtils;

/**
 * 从广告服务获取源头数据，对访问服务进行访问流量的限制以及资源的隔离
 * @author Administrator
 *
 */
public class GetContentInfoCommand extends HystrixCommand<Content> {
	
	private long contentId;

	public GetContentInfoCommand(long contentId) {
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
		this.contentId = contentId;
	}

	@Override
	protected Content run() throws Exception {
		/**
		 * 从源头服务获取数据
		 */
		String contentInfoUrl = "http://127.0.0.1:8084/getContentInfo";
		Map<String, String> map = new HashMap<String, String>();
		String response = HttpClientUtils.sendPostRequest(contentInfoUrl, map);
		Content content = JsonUtils.jsonToPojo(response, Content.class);
		
		/**
		 *  解决缓存穿透问题,设置一个空json串
		 */
		if(content == null) {
			content.setId(contentId);
		}
		
		return content;
	}
	
	/**
	 * 超过限制流量，进行降级
	 */
	@Override
	protected Content getFallback() {
		Content content = new Content();
		content.setId(contentId);
		return content;
	}

}
