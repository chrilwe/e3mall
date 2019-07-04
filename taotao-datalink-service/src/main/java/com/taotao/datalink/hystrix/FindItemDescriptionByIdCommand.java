package com.taotao.datalink.hystrix;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandProperties;
import com.netflix.hystrix.HystrixThreadPoolProperties;
import com.netflix.hystrix.HystrixCommand.Setter;
import com.taotao.itemservice.model.ItemDescription;
import com.taotao.itemservice.service.ItemDescriptionService;

public class FindItemDescriptionByIdCommand extends HystrixCommand<ItemDescription> {
	
	private long itemId;
	
	private ItemDescriptionService itemDescriptionService;

	@Override
	protected ItemDescription getFallback() {
		System.out.println("日志========》限流降级");
		ItemDescription itemDescription = new ItemDescription();
		itemDescription.setItemId(-1l);
		itemDescription.setCreated(new Date());
		itemDescription.setItemDesc("降级数据");
		return itemDescription;
	}

	public FindItemDescriptionByIdCommand(long itemId, ItemDescriptionService itemDescriptionService) {
		super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("ItemDescriptionService"))
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
		this.itemDescriptionService = itemDescriptionService;
	}

	@Override
	protected ItemDescription run() throws Exception {
		ItemDescription itemDescription = itemDescriptionService.findItemDescriptionById(itemId);
		if(itemDescription == null) {
			ItemDescription result = new ItemDescription();
			System.out.println("日志=======》缓存穿透");
			result.setItemId(itemId);
			return result;
		}
		return itemDescription;
	}

}
