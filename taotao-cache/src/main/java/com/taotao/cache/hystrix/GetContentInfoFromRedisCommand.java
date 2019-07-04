package com.taotao.cache.hystrix;

import org.springframework.beans.factory.annotation.Autowired;

import redis.clients.jedis.JedisCluster;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandProperties;

/**
 * 对redis资源进行资源隔离，redis缓存雪崩时进行熔断和降级
 * @author Administrator
 *
 */
public class GetContentInfoFromRedisCommand extends HystrixCommand<String>{
	
	@Autowired
	private JedisCluster jedisCluster;
	
	private long contentId;

	public GetContentInfoFromRedisCommand(long contentId) {
		super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("GetContentInfoCommand"))
				.andCommandPropertiesDefaults(HystrixCommandProperties.Setter()
						.withCircuitBreakerErrorThresholdPercentage(70)
						.withCircuitBreakerRequestVolumeThreshold(1000)
						.withCircuitBreakerSleepWindowInMilliseconds(60 * 1000)
						.withExecutionTimeoutInMilliseconds(5000)));
		this.contentId = contentId;
	}

	@Override
	protected String run() throws Exception {
		/**
		 * 从redis缓存中获取广告信息
		 */
		String contentInfoStr = jedisCluster.get("contentId_" + contentId);
		return contentInfoStr;
	}
	
	/**
	 * 发生异常，熔断进行降级
	 */
	@Override
	protected String getFallback() {
		
		return null;
	}

}
