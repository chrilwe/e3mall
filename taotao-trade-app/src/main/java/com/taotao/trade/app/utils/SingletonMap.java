package com.taotao.trade.app.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * 单例map
 * @author Administrator
 *
 */
public class SingletonMap {
	
	private Map<String, String> paramMap;
	
	public SingletonMap() {
		paramMap = new HashMap<String, String>();
	}
	
	/**
	 * 获取map
	 */
	public Map<String, String> getMap() {
		
		return paramMap;
	}
	
	/**
	 * 静态内部类
	 */
	private static class Singleton {
		private static SingletonMap map = null;
		
		static {
			map = new SingletonMap();
		}
		
		private static SingletonMap getInstance() {
			
			return map;
		}
	}
	
	/**
	 * 获取单例
	 */
	public static SingletonMap getInstance() {
		
		return Singleton.getInstance();
	}
}
