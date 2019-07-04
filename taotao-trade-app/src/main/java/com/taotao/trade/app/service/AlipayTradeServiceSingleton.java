package com.taotao.trade.app.service;

import com.alipay.demo.trade.service.AlipayTradeService;
import com.alipay.demo.trade.service.impl.AlipayTradeServiceImpl;

/**
 * 创建单例对象
 * @author Administrator
 *
 */
public class AlipayTradeServiceSingleton {
	
	private AlipayTradeService tradeService;
	
	public AlipayTradeServiceSingleton() {
		tradeService = new AlipayTradeServiceImpl.ClientBuilder().build();
	}
	
	/**
	 * 获取AlipayTradeService实例
	 */
	public AlipayTradeService getAlipayTradeService() {
		return tradeService;
	}
	
	/**
	 * 内部静态类
	 */
	private static class Singleton {
		private static AlipayTradeServiceSingleton tradeServiceSingleton = null;
		
		static {
			tradeServiceSingleton = new AlipayTradeServiceSingleton();
		}
		
		private static AlipayTradeServiceSingleton getInstance() {
			
			return tradeServiceSingleton;
		}
	}
	
	/**
	 * 获取单例
	 */
	public static AlipayTradeServiceSingleton getInstance() {
		
		return Singleton.getInstance();
	}
}
