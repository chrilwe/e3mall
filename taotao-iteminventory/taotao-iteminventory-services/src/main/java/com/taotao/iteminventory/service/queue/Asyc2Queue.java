package com.taotao.iteminventory.service.queue;

import java.util.concurrent.ArrayBlockingQueue;

import com.taotao.iteminventory.service.request.Request;

/**
 * 将请求路由到内存队列中
 * @author Administrator
 *
 */
public class Asyc2Queue {
	
	private long itemId;
	
	public Asyc2Queue(long itemId) {
		this.itemId = itemId;
	}
	
	public void process(Request request) {
		//获取路由到的内存队列
		ArrayBlockingQueue<Request> queue = getQueue(itemId);
		//将请求放入内存队列中
		try {
			queue.put(request);
		} catch (InterruptedException e) {
			System.out.println("日志============》请求放入内存队列异常:" + e);
			e.printStackTrace();
		}
	}
	
	private ArrayBlockingQueue<Request> getQueue(long itemId) {
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
