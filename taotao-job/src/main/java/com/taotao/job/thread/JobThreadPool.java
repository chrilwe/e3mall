package com.taotao.job.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 处理调度线程的线程池
 * @author Administrator
 *
 */
public class JobThreadPool {
	
	private JobThreadPool() {
		System.out.println("==============线程池初始化==============");
		//初始化线程池
		ExecutorService executorService = Executors.newFixedThreadPool(10);
		for(int i = 0; i < 10; i++) {
			OrderCancelJobThread thread = new OrderCancelJobThread();
			executorService.submit(thread);
		}
	}
	
	/**
	 * 静态内部类
	 */
	private static class Singleton {
		private static JobThreadPool pool = null;
		static {
			pool = new JobThreadPool();
		}
		
		private static JobThreadPool getInstance() {
			
			return pool;
		}
	}
	
	/**
	 * 获取单例实例
	 */
	public static JobThreadPool getInstance() {
		
		return Singleton.getInstance();
	}
	
	/**
	 * 初始化类
	 */
	public static void init() {
		getInstance();
	}
}
