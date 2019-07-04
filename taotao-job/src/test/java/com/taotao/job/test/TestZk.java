package com.taotao.job.test;

import com.taotao.job.zookeeper.ZkDistributeLock;

public class TestZk {
	public static void main(String[] args) {
		ZkDistributeLock.getInstance();
	}
}
