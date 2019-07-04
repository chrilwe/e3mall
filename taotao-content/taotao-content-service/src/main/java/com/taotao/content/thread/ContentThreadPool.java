package com.taotao.content.thread;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.taotao.content.queue.RequestQueues;
import com.taotao.content.request.ContentRequest;

public class ContentThreadPool {
	
	private ContentThreadPool() {
		ExecutorService executorService = Executors.newFixedThreadPool(10);//初始化大小为10的线程池
		RequestQueues queues = RequestQueues.getInstance();
		for(int i = 0; i < 10; i++) {
			//实例化10个内存队列,并且加入到内存队列集合中
			ArrayBlockingQueue<ContentRequest> queue = new ArrayBlockingQueue<ContentRequest>(1000);
			queues.addQueue(queue);
			
			//实例化10个后台线程
			ContentThread workThread = new ContentThread(queue);
			
			executorService.submit(workThread);
		}
	}
	
	/**
	 * 静态内部类
	 */
	private static class Singleton {
		private static ContentThreadPool pool = null;
		
		static {
			pool = new ContentThreadPool();
		}
		
		private static ContentThreadPool getInstance() {
			
			return pool;
		}
	}
	
	/**
	 * 获取单例
	 */
	public static ContentThreadPool getInstance() {
		
		return Singleton.getInstance();
	}
	
	/**
	 * 线程池初始化
	 */
	public static void init() {
		getInstance();
	}
}
