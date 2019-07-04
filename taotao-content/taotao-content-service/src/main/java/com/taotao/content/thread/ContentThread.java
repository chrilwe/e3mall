package com.taotao.content.thread;

import java.util.concurrent.ArrayBlockingQueue;

import com.taotao.content.request.ContentRequest;

/**
 * 执行读取或者更新请求的后台线程
 * @author Administrator
 *
 */
public class ContentThread implements Runnable {
	
	private ArrayBlockingQueue<ContentRequest> queue;
	
	public ContentThread(ArrayBlockingQueue<ContentRequest> queue) {
		this.queue = queue;
	}

	@Override
	public void run() {
		try {
			while(true) {
				ContentRequest request = queue.take();
				request.process();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
