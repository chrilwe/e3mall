package com.taotao.content.queue;

import java.util.concurrent.ArrayBlockingQueue;

import com.taotao.content.request.ContentRequest;

/**
 * 将请求路由到内存队列
 * @author Administrator
 *
 */
public class RoutRequestToQueue {
	
	private ContentRequest request;
	
	public RoutRequestToQueue(ContentRequest request) {
		this.request = request;
	}
	
	public void routing() {
		long contentId = request.getContentId();
		System.out.println("日志---------------》需要路由的contentId="+contentId);
		ArrayBlockingQueue<ContentRequest> queue = getQueue(contentId);
		
		//将请求放到队列中
		try {
			queue.put(request);
			System.out.println("日志-------------》将请求放到队列中");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 计算hash值，并且获取路由到的内存队列
	 */
	private ArrayBlockingQueue<ContentRequest> getQueue(long contentId) {
		RequestQueues requestQueues = RequestQueues.getInstance();
		//计算hash值，取模
		String key = String.valueOf(contentId);
		int h;
		int hash = (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
								
		// 对hash值取模，将hash值路由到指定的内存队列中，比如内存队列大小8
		// 用内存队列的数量对hash值取模之后，结果一定是在0~7之间
		// 所以任何一个商品id都会被固定路由到同样的一个内存队列中去的
		int index = (requestQueues.size() - 1) & hash;
		System.out.println("日志----------------》需要路由的index=" + index);
		
		return requestQueues.get(index);
	}
}
