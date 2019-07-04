package com.taotao.cache.hystrix;

import org.springframework.beans.factory.annotation.Autowired;

import redis.clients.jedis.JedisCluster;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandProperties;
import com.taotao.cache.model.Item;
import com.taotao.cache.utils.JsonUtils;
/**
 * 对Redis资源访问进行资源隔离,熔断，降级fall-back
 * 熔断：每秒的qps为10 在10s内，时间窗口的qps线程达到或者超过了1000 那么如果此时访问异常
 * 超过的百分比大于70%的话，就认为Redis缓存崩溃了，就会打开短路器，进行熔断降级，当过了1分钟后
 * 尝试重新访问Redis资源,短路器就会进入half-open状态
 * @author Administrator
 *
 */
public class SetItemInfo2RedisCommand extends HystrixCommand<Boolean>{
	
	@Autowired
	private JedisCluster jedisCluster;
	
	private Item item;

	public SetItemInfo2RedisCommand(Item item) {
		super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("RedisGroup"))
				.andCommandPropertiesDefaults(HystrixCommandProperties.Setter()
						.withCircuitBreakerRequestVolumeThreshold(1000)
						.withCircuitBreakerErrorThresholdPercentage(70)
						.withCircuitBreakerSleepWindowInMilliseconds(60 * 1000)
						.withExecutionTimeoutInMilliseconds(5000)));
		this.item = item;
	}

	@Override
	protected Boolean getFallback() {
		
		return null;
	}

	@Override
	protected Boolean run() throws Exception {
		jedisCluster.set("itemId_" + item.getId(), JsonUtils.objectToJson(item));
		return true;
	}

}
