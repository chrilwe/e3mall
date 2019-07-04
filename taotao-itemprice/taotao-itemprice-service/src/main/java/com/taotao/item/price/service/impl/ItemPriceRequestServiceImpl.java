package com.taotao.item.price.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.taotao.item.price.model.ItemPrice;
import com.taotao.item.price.service.ItemPriceService;
import com.taotao.item.price.service.ItemPriceRequestService;
import com.taotao.item.price.service.queue.AsycRequest2Queue;
import com.taotao.item.price.service.request.Request;
import com.taotao.item.price.service.request.impl.ItemPriceReadRequestImpl;
import com.taotao.item.price.service.request.impl.ItemPriceUpdateRequestImpl;
import com.taotao.item.price.service.utils.JsonUtils;
/**
 * 商品价格对外服务(解决了从redis读写并发的问题)
 * @author Administrator
 *
 */
@Service
public class ItemPriceRequestServiceImpl implements ItemPriceRequestService {
	
	@Autowired
	private ItemPriceService itemPriceService;
	@Value("${WAITTIME}")
	private Long WAITTIME;
	
	/**
	 * 更新商品价格
	 */
	@Override
	public void updateItemPrice(long itemId, long price) {
		//将更新商品价格请求进行封装
		Request request = new ItemPriceUpdateRequestImpl(itemId, price);
		
		// 将封装的请求路由到对应的内存队列中,让后台线程更新价格
		AsycRequest2Queue asycRequest2Queue = new AsycRequest2Queue();
		asycRequest2Queue.process(request,itemId);
	}
	
	/**
	 * 查询商品价格
	 */
	@Override
	public ItemPrice findItemPriceByItemId(long itemId) {
		//封装读取商品价格请求
		Request request = new ItemPriceReadRequestImpl(itemId);
		
		//将封装的请求路由到相应的内存队列中
		AsycRequest2Queue asycRequest2Queue = new AsycRequest2Queue();
		asycRequest2Queue.process(request,itemId);
		/**
		 * 读取商品价格数据
		 */
		//从redis中读取数据,如果超过规定的时间没有获取到数据，直接从数据库中查找
		long startTime = System.currentTimeMillis();//开始时间
		long waitTime = 0l;//等待时间
		while(true) {
			if(waitTime > WAITTIME) {
				System.out.println("日志=============》从redis中获取商品价格时间超过" + WAITTIME + "ms，不在从redis中查找，从数据库查找");
				break;
			}
			String itemPriceStr = itemPriceService.findItemPriceFromRedis(itemId);
			if(itemPriceStr == null) {
				waitTime = System.currentTimeMillis() - startTime;
				try {
					System.out.println("日志===============》从redis中没有获取到商品价格数据，休眠20ms");
					Thread.sleep(20);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			} else {
				ItemPrice itemPrice = JsonUtils.jsonToPojo(itemPriceStr, ItemPrice.class);
				System.out.println("日志==============》从redis中读取到商品价格" + itemPrice.getPrice() + "花费时间长度" + waitTime + "毫秒");
				return itemPrice;
			}	
		}
		
		//从数据库中读取数据
		ItemPrice itemPrice = itemPriceService.findItemPriceByItemId(itemId);
		System.out.println("日志=================》从数据库中获取商品价格数据");
		return itemPrice;
	}

}
