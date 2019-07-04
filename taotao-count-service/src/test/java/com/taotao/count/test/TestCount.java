package com.taotao.count.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
/**
 * 测试排序算法
 * @author Administrator
 *
 */
public class TestCount {
	public static void main(String[] args) {
		List<Map.Entry<Long, Integer>> list = new ArrayList<Map.Entry<Long, Integer>>();
		/**
		 * 创造测试数据
		 */
		Map<Long, Integer> map = new HashMap<Long, Integer>();
		for(int i = 0; i < 20; i ++) {
			map.put(Long.parseLong(String.valueOf(i)), new Random().nextInt(1000));
		}
		
		String data1 = "";
		for(Map.Entry<Long, Integer> entry : map.entrySet()) {
			Integer value = entry.getValue();
			data1 += value + ",";
		}
		System.out.println("排序前数据:"+data1);
		
		/**
		 * 排序算法
		 */
		for(Map.Entry<Long, Integer> entry : map.entrySet()) {
			if(list.size() == 0) {
				list.add(entry);
			} else {
				//map中的数据比较大
				boolean flag = false;
				for(int i = 0; i < list.size(); i++) {
					if(list.get(i).getValue() < entry.getValue()) {
						Entry<Long, Integer> lastOne = list.get(list.size() - 1);
						for(int j = list.size() - 1; j > i; j--) {
							list.set(j, list.get(j - 1));
						}
						list.add(lastOne);
						list.set(i, entry);
						flag = true;
						break;
					}
					
				}
				
				if(!flag) {
					list.add(entry);
				}
			}
		}
		
		String data2 = "";
		for(Map.Entry<Long, Integer> entry : list) {
			data2 += entry.getValue() + ",";
		}
		System.out.println("排序后的数据:"+data2);
	}
}
