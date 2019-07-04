package com.taotao.iteminventory.service.thread;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.taotao.iteminventory.service.queue.RequestQueue;
import com.taotao.iteminventory.service.request.Request;

/**
 * 初始化spring容器时，实例化一个单例的线程池
 * @author Administrator
 *
 */
public class RequestThreadPool {
	
	public RequestThreadPool() {
		ExecutorService executorService = Executors.newFixedThreadPool(10);
		RequestQueue requestQueue = RequestQueue.getInstance();
		
		for(int i = 0; i < 10; i++) {
			ArrayBlockingQueue<Request> queue = new ArrayBlockingQueue<Request>(1000);
			requestQueue.add(queue);
			
			executorService.submit(new RequestThread(queue));
		}
	}
	
	/**
	 * 静态内部类
	 */
	private static class Singleton {
		private static RequestThreadPool pool = null;
		
		static {
			pool = new RequestThreadPool();
		}
		
		private static RequestThreadPool getInstance() {
			
			return pool;
		}
	}
	
	/**
	 * 获取单例
	 */
	public static RequestThreadPool getInstance() {
		
		return Singleton.getInstance();
	}
	
	/**
	 * 初始化方法
	 */
	public static void init() {
		getInstance();
	}
}
