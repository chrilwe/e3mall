package com.taotao.item.price.service.queue;

import java.util.concurrent.ArrayBlockingQueue;

import com.taotao.item.price.service.request.Request;

/**
 * 将分装的请求路由到对应的内存队列中
 * @author Administrator
 *
 */
public class AsycRequest2Queue {
	
	
	public void process(Request request, long itemId) {
		try {
			//对商品id进行hash，路由到对应的内存队列
			ArrayBlockingQueue<Request> queue = routQueue(itemId);
			
			//将封装的请求放入内存队列中
			queue.put(request);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("异常=================》路由商品到内存队列发生异常:" + e);
		}
	}
	
	/**
	 * 获取路由到的内存队列
	 * @return
	 */
	private ArrayBlockingQueue<Request> routQueue(long itemId) {
		RequestQueue queue = RequestQueue.getInstance();
		//计算hash值，取模
		String key = String.valueOf(itemId);
		int h;
		int hash = (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
						
		// 对hash值取模，将hash值路由到指定的内存队列中，比如内存队列大小8
		// 用内存队列的数量对hash值取模之后，结果一定是在0~7之间
		// 所以任何一个商品id都会被固定路由到同样的一个内存队列中去的
		int index = (queue.size() - 1) & hash;
		System.out.println("日志=============》将商品id取模运算后是" + index + "内存队列大小"+queue.size());
		return queue.getQueue(index);
	}
}
