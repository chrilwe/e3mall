package com.taotao.item.price.service.thread;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;

import com.taotao.item.price.service.request.Request;

/**
 * 后台线程，处理商品价格读或者写请求
 * @author Administrator
 *
 */
public class RequestThread implements Runnable {
	
	private ArrayBlockingQueue<Request> queue;
	
	public RequestThread(ArrayBlockingQueue<Request> queue) {
		this.queue = queue;
	}

	@Override
	public void run() {
		try {
			while(true) {
				Request request = queue.take();
				System.out.println("日志============》后台线程执行,request_"+request);
				request.process();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
