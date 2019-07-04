package com.taotao.datalink.hystrix;

import org.springframework.beans.factory.annotation.Autowired;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandProperties;
import com.netflix.hystrix.HystrixThreadPoolProperties;
import com.netflix.hystrix.HystrixCommand.Setter;
import com.taotao.itemservice.model.Item;
import com.taotao.itemservice.service.ItemInfoService;

public class GetItemInfoByIdCommand extends HystrixCommand<Item> {
	
	private long itemId;
	
	private ItemInfoService itemInfoService;

	public GetItemInfoByIdCommand(long itemId, ItemInfoService itemInfoService) {
		super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("ItemInfoService"))
				.andThreadPoolPropertiesDefaults(HystrixThreadPoolProperties.Setter()
						.withCoreSize(10)
						.withMaximumSize(30) 
						.withAllowMaximumSizeToDivergeFromCoreSize(true) 
						.withKeepAliveTimeMinutes(50) 
						.withMaxQueueSize(50)
						.withQueueSizeRejectionThreshold(100))
				.andCommandPropertiesDefaults(HystrixCommandProperties.Setter()
						.withExecutionTimeoutInMilliseconds(600000))
						
				);
		this.itemId = itemId;
		this.itemInfoService = itemInfoService;
	}


	@Override
	protected Item run() throws Exception {
		System.out.println("开始itemId_" + itemId);
		Item item = null;
		try {
			item = itemInfoService.getItemInfoById(itemId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("日志===============》item_"+item);
		/**
		 * 缓存穿透解决
		 */
		if(item == null) {
			item.setId(itemId);
			System.out.println("日志========》数据库中没有找到商品基本信息");
		}
		return item;
	}
	
	/**
	 * 限流降级
	 */
	@Override
	protected Item getFallback() {
		Item item = new Item();
		item.setId(536563l);
		return item;
	}


}
