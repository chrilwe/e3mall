package com.taotao.iteminventory.service.thread;

import java.util.concurrent.ArrayBlockingQueue;

import com.taotao.iteminventory.service.request.Request;

/**
 * 后台线程，处理读和更新请求
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
				request.process();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
}
