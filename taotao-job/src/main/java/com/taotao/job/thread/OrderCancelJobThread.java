package com.taotao.job.thread;

import java.util.Date;
import java.util.List;

import org.springframework.web.context.ContextLoader;

import com.taotao.job.zookeeper.ZkDistributeLock;
import com.taotao.order.model.Order;
import com.taotao.order.model.OrderRecord;
import com.taotao.order.service.OrderService;

/**
 * 处理超时订单后台线程
 * @author Administrator
 *
 */
public class OrderCancelJobThread implements Runnable {

	@Override
	public void run() {
		while(true) {
			//调用订单服务，查询未支付的订单
			OrderService orderService = ContextLoader.getCurrentWebApplicationContext()
					.getBean(OrderService.class);
			
			int count = orderService.countNoPayOrder("未支付");
			
			//分页查询
			int lastIndex = (count % 1000 == 0)?count/1000:count/1000+1;
			for(int i = 1; i <= lastIndex; i++) {
				List<Order> orders = orderService.findNoPayOrderByPageAndSize("未支付", (i -1)*1000, 1000);
				System.out.println("=======orders="+orders);
				//判断当前的未支付订单是否过期
				for (Order order : orders) {
					long createTime = order.getCreateTime().getTime();//创建订单的日期
					long currentTime = System.currentTimeMillis();//当前时间
					long expireTime = 30 * 60 * 1000;//过期时间
					if(currentTime - createTime >= expireTime) {
						//更新订单状态(多线程下，需要保证每条数据不会并发更新,处理过一次即可)
						//获得锁
						ZkDistributeLock zkLock = ZkDistributeLock.getInstance();
						String path = "/order-job-" + order.getMerchantOrderNo();
						Boolean lock = zkLock.requireLock(path);
						if(lock) {
							//更新订单 
							orderService.updateOrderStatusByOrderId(order.getMerchantOrderNo(), new Date(), "取消订单");
							//释放锁
							zkLock.releaseLock(path);
						}
						
					}
				}
			}
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
