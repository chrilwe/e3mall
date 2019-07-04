package com.taotao.cache.hystrix;

import org.springframework.beans.factory.annotation.Autowired;

import redis.clients.jedis.JedisCluster;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandProperties;
import com.taotao.cache.model.Content;
import com.taotao.cache.utils.JsonUtils;

/**
 * 对redsi访问进行资源隔离，以及redis发生雪崩故障时进行熔断，降级
 * @author Administrator
 *
 */
public class SetContentInfo2RedisCommand extends HystrixCommand<Boolean> {
	
	@Autowired
	private JedisCluster jedisCluster;
	
	private Content content;

	public SetContentInfo2RedisCommand(Content content) {
		super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("SetContentInfoCommand"))
				.andCommandPropertiesDefaults(HystrixCommandProperties.Setter()
						.withCircuitBreakerErrorThresholdPercentage(70)
						.withCircuitBreakerSleepWindowInMilliseconds(60 * 1000)
						.withCircuitBreakerRequestVolumeThreshold(1000)
						.withExecutionTimeoutInMilliseconds(5000)));
		this.content = content;
	}

	@Override
	protected Boolean run() throws Exception {
		/**
		 * 将广告内容更新到redis中
		 */
		jedisCluster.set("contentId_" + content.getId(), JsonUtils.objectToJson(content));
		return true;
	}
	
	/**
	 * redis发生雪崩时，进行降级
	 */
	@Override
	protected Boolean getFallback() {
		
		return false;
	}

}
