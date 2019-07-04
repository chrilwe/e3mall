package com.taotao.datalink.hystrix;

import java.util.Date;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandProperties;
import com.netflix.hystrix.HystrixThreadPoolProperties;
import com.netflix.hystrix.HystrixCommand.Setter;
import com.taotao.itemservice.model.ItemSpecification;
import com.taotao.itemservice.service.ItemSpecificationService;

/**
 * 服务限流
 * @author Administrator
 *withCoreSize：设置你的线程池的大小
 *withMaxQueueSize：设置的是你的等待队列，缓冲队列的大小
 *withQueueSizeRejectionThreshold：如果withMaxQueueSize<withQueueSizeRejectionThreshold，
 *那么取的是withMaxQueueSize，反之，取得是withQueueSizeRejectionThreshold
 */
public class FindItemSpecificationByIdCommand extends HystrixCommand<ItemSpecification> {
	
	private long itemId;
	
	private ItemSpecificationService itemSpecificationService;

	public FindItemSpecificationByIdCommand(long itemId, ItemSpecificationService itemSpecificationService) {
		super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("ItemSpecificationService"))
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
		this.itemSpecificationService = itemSpecificationService;
	}

	@Override
	protected ItemSpecification getFallback() {
		System.out.println("日志=======》对商品规格限流，降级策略");
		ItemSpecification itemSpecification = new ItemSpecification();
		itemSpecification.setId(-1l);
		itemSpecification.setUpdated(new Date());
		itemSpecification.setCreated(new Date());
		itemSpecification.setItemId(-1l);
		itemSpecification.setParamData("降级数据");
		return itemSpecification;
	}
	
	/**
	 * 限流降级策略
	 */
	@Override
	protected ItemSpecification run() throws Exception {
		ItemSpecification itemSpecification = null;
		try {
			itemSpecification = itemSpecificationService.findItemSpecificationById(itemId);
			//解决缓存穿透的问题
			if(itemSpecification == null) {
				ItemSpecification result = new ItemSpecification();
				result.setItemId(itemId);
				result.setParamData("规格");
				return result;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return itemSpecification;
		
	}

}
