package com.taotao.datalink.hystrix;

import org.springframework.beans.factory.annotation.Autowired;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandProperties;
import com.netflix.hystrix.HystrixThreadPoolProperties;
import com.netflix.hystrix.HystrixCommand.Setter;
import com.taotao.itemservice.model.ItemCategory;
import com.taotao.itemservice.service.ItemCategoryService;

/**
 * 服务限流
 * @author Administrator
 *withCoreSize：设置你的线程池的大小
 *withMaxQueueSize：设置的是你的等待队列，缓冲队列的大小
 *withQueueSizeRejectionThreshold：如果withMaxQueueSize<withQueueSizeRejectionThreshold，
 *那么取的是withMaxQueueSize，反之，取得是withQueueSizeRejectionThreshold
 */
public class FindItemCategoryByIdCommand extends HystrixCommand<ItemCategory> {
	
	private long itemId;
	
	private ItemCategoryService itemCategoryService;

	public FindItemCategoryByIdCommand(long itemId, ItemCategoryService itemCategoryService) {
		super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("ItemCategoryService"))
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
		this.itemCategoryService = itemCategoryService;
	}
	
	/**
	 * 限流降级
	 */
	@Override
	protected ItemCategory getFallback() {
		ItemCategory itemCategory = new ItemCategory();
		itemCategory.setId(-1l);
		return itemCategory;
	}

	@Override
	protected ItemCategory run() throws Exception {
		ItemCategory itemCategory = itemCategoryService.findItemCategoryById(itemId);
		//缓存穿透问题解决
		if(itemCategory == null) {
			ItemCategory result = new ItemCategory();
			result.setName("分类名称");
			result.setId(itemId);
			
			return result;
		}
		return itemCategory;
	}
	
}
