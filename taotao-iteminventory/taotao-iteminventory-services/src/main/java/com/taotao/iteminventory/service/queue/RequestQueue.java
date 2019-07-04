package com.taotao.iteminventory.service.queue;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;

import com.taotao.iteminventory.service.request.Request;

/**
 * 单例内存队列
 * @author Administrator
 *
 */
public class RequestQueue {
	
	private List<ArrayBlockingQueue<Request>> queues;
	
	public RequestQueue() {
		queues = new ArrayList<ArrayBlockingQueue<Request>>();
	}
	
	/**
	 * 将内存队列加入queues中
	 */
	public void add(ArrayBlockingQueue<Request> queue) {
		queues.add(queue);
	}
	
	/**
	 * 获取list的大小
	 */
	public int size() {
		return queues.size();
	}
	
	/**
	 * 获取对应的内存队列
	 */
	public ArrayBlockingQueue<Request> getQueue(int index) {
		
		return queues.get(index);
	}
	
	/**
	 * 静态内部类
	 */
	private static class Singleton {
		private static RequestQueue requestQueue = null;
		
		static {
			requestQueue = new RequestQueue();
		}
		
		private static RequestQueue getInstance() {
			
			return requestQueue;
		}
	}
	
	/**
	 * 获取单例
	 */
	public static RequestQueue getInstance() {
		
		return Singleton.getInstance();
	}
}
