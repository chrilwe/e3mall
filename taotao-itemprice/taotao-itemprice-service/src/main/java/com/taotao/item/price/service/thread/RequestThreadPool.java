package com.taotao.item.price.service.thread;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Value;

import com.taotao.item.price.service.queue.RequestQueue;
import com.taotao.item.price.service.request.Request;

/**
 * 单例线程池，处理商品价格的更新读取的后台线程
 * @author Administrator
 *
 */
public class RequestThreadPool {
	
	public RequestThreadPool() {
		ExecutorService executorService = Executors.newFixedThreadPool(10);
		RequestQueue queue = RequestQueue.getInstance();
		for(int i = 0; i < 10; i++) {
			//初始化10个内存队列放到list中
			ArrayBlockingQueue<Request> arrayBlockingQueue = new ArrayBlockingQueue<Request>(1000);
			queue.add(arrayBlockingQueue);
			
			//初始化10个线程，并将初始化的内存队列传入后台线程中执行
			executorService.execute(new RequestThread(arrayBlockingQueue));
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
	 * 初始化
	 */
	public static void init() {
		getInstance();
	}
}
