package com.taotao.item.price.service.queue;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;

import org.springframework.beans.factory.annotation.Value;

import com.taotao.item.price.service.request.Request;

/**
 * 存放封装请求的内存队列
 * @author Administrator
 *
 */
public class RequestQueue {
	
	private List<ArrayBlockingQueue<Request>> queues;
	
	public RequestQueue() {
		queues = new ArrayList<ArrayBlockingQueue<Request>>();
	}
	
	/**
	 * 添加内存队列
	 */
	public void add(ArrayBlockingQueue<Request> queue) {
		queues.add(queue);
	}
	
	/**
	 * 获得内存队列
	 */
	public ArrayBlockingQueue<Request> getQueue(int index) {
		
		return queues.get(index);
	}
	
	/**
	 * 获取队列的大小
	 */
	public int size() {
		return queues.size();
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
