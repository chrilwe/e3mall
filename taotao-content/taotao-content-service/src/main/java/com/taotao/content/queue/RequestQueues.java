package com.taotao.content.queue;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;

import com.taotao.content.request.ContentRequest;

/**
 * 封装内存队列的集合
 * @author Administrator
 *
 */
public class RequestQueues {
	
	private List<ArrayBlockingQueue<ContentRequest>> list;
	
	public RequestQueues() {
		list = new ArrayList<ArrayBlockingQueue<ContentRequest>>();
	}
	
	/**
	 * 添加内存队列
	 */
	public void addQueue(ArrayBlockingQueue<ContentRequest> queue) {
		list.add(queue);
	}
	
	/**
	 *根据index获取到对应的内存队列
	 */
	public ArrayBlockingQueue<ContentRequest> get(int index) {
		
		return list.get(index);
	}
	
	/**
	 * 获取大小
	 */
	public int size() {
		
		return list.size();
	}
	
	/**
	 * 静态内部类
	 */
	private static class Singleton {
		private static RequestQueues queues = null;
		
		static {
			queues = new RequestQueues();
		}
		
		private static RequestQueues getInstance() {
			
			return queues;
		}
	}
	
	/**
	 * 获取单例
	 */
	public static RequestQueues getInstance() {
		
		return Singleton.getInstance();
	}
}
