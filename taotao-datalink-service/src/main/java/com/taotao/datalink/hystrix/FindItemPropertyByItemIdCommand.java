package com.taotao.datalink.hystrix;

import org.springframework.beans.factory.annotation.Autowired;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandProperties;
import com.netflix.hystrix.HystrixThreadPoolProperties;
import com.netflix.hystrix.HystrixCommand.Setter;
import com.taotao.itemservice.model.ItemProperty;
import com.taotao.itemservice.service.ItemPropertyService;
/**
 * *withCoreSize：设置你的线程池的大小
 *withMaxQueueSize：设置的是你的等待队列，缓冲队列的大小
 *withQueueSizeRejectionThreshold：如果withMaxQueueSize<withQueueSizeRejectionThreshold，
 *那么取的是withMaxQueueSize，反之，取得是withQueueSizeRejectionThreshold
 * @author Administrator
 *
 */
public class FindItemPropertyByItemIdCommand extends HystrixCommand<ItemProperty> {
	
	private ItemPropertyService itemPropertyService;
	
	private long itemId;

	public FindItemPropertyByItemIdCommand(long itemId, ItemPropertyService itemPropertyService) {
		super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("ItemPropertyService"))
				.andThreadPoolPropertiesDefaults(HystrixThreadPoolProperties.Setter()
						.withCoreSize(10)
						.withMaximumSize(30) 
						.withAllowMaximumSizeToDivergeFromCoreSize(true) 
						.withKeepAliveTimeMinutes(5) 
						.withMaxQueueSize(50)
						.withQueueSizeRejectionThreshold(100))
				.andCommandPropertiesDefaults(HystrixCommandProperties.Setter()
						.withExecutionTimeoutInMilliseconds(500000))
						
				);
		this.itemId = itemId;
		this.itemPropertyService = itemPropertyService;
	}

	@Override
	protected ItemProperty run() throws Exception {
		ItemProperty itemProperty = null;
		
		try {
			itemProperty = itemPropertyService.findItemPropertyByItemId(itemId);
			
			//解决缓存穿透的问题
			if(itemProperty == null) {
				ItemProperty result = new ItemProperty();
				result.setItemId(itemId);
				result.setName("属性");
				result.setValue("value");
				return result;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return itemProperty;
	}
	
	/**
	 * 降级处理逻辑
	 */
	/* (non-Javadoc)
	 * @see com.netflix.hystrix.HystrixCommand#getFallback()
	 */
	@Override
	protected ItemProperty getFallback() {
		System.out.println("日志=======》商品属性做降级处理");
		ItemProperty itemProperty = new ItemProperty();
		itemProperty.setName("降级处理的商品属性");
		itemProperty.setId(-1l);
		itemProperty.setValue("商品属性");
		return itemProperty;
	}

}
