package com.taotao.cache.hystrix;

import org.springframework.beans.factory.annotation.Autowired;

import redis.clients.jedis.JedisCluster;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandProperties;
import com.netflix.hystrix.HystrixCommand.Setter;

/**
 * 对Redis资源访问进行资源隔离,熔断，降级fall-back
 * 熔断：每秒的qps为10 在10s内，时间窗口的qps线程达到或者超过了1000 那么如果此时访问异常
 * 超过的百分比大于70%的话，就认为Redis缓存崩溃了，就会打开短路器，进行熔断降级，当过了1分钟后
 * 尝试重新访问Redis资源,短路器就会进入half-open状态
 *
 */
public class GetItemInfoFromRedisCommand extends HystrixCommand<String>{
	
	@Autowired
	private JedisCluster jedisCluster;
	
	private String key;

	public GetItemInfoFromRedisCommand(String key) {
		super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("RedisGroup"))
				.andCommandPropertiesDefaults(HystrixCommandProperties.Setter()
						//最少10s内的访问次数达到多少
						.withCircuitBreakerRequestVolumeThreshold(1000)
						//访问次数异常达到多少，就会打开短路器熔断
						.withCircuitBreakerErrorThresholdPercentage(70)
						//熔断后多长时间尝试着恢复
						.withCircuitBreakerSleepWindowInMilliseconds(60 * 1000)
						//超时时间
						.withExecutionTimeoutInMilliseconds(5000)));
		this.key = key;
	}

	@Override
	protected String getFallback() {
		//熔断，异常降级策略
		return null;
	}

	@Override
	protected String run() throws Exception {
		String value = jedisCluster.get(key);
		return value;
	}

}
