package com.taotao.datalink.hystrix;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandProperties;
import com.netflix.hystrix.HystrixThreadPoolProperties;
import com.netflix.hystrix.HystrixCommand.Setter;
import com.taotao.itemservice.model.ItemBrand;
import com.taotao.itemservice.service.ItemBrandService;

public class FindItemBrandByItemIdCommand extends HystrixCommand<ItemBrand> {
	
	private long itemId;
	
	private ItemBrandService itemBrandService;

	public FindItemBrandByItemIdCommand(long itemId, ItemBrandService itemBrandService) {
		super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("ItemBrandService"))
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
		this.itemBrandService = itemBrandService;
	}
	
	/**
	 * 限流降级
	 */
	@Override
	protected ItemBrand getFallback() {
		System.out.println("日志=======》商品标题限流降级");
		ItemBrand itemBrand = new ItemBrand();
		itemBrand.setId(-1l);
		itemBrand.setItemId(-1l);
		return itemBrand;
	}

	@Override
	protected ItemBrand run() throws Exception {
		ItemBrand itemBrand = itemBrandService.findItemBrandByItemId(itemId);
		if(itemBrand == null) {
			ItemBrand result = new ItemBrand();
			result.setBrand("标题");
			result.setItemId(itemId);
			return result;
		}
		return itemBrand;
	}

}
