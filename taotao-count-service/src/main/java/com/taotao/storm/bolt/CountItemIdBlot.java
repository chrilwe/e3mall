package com.taotao.storm.bolt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.trident.util.LRUMap;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;
import org.apache.storm.utils.Utils;

import com.taotao.storm.http.HttpClientUtils;

/**
 * 统计访问次数blot
 * @author Administrator
 *
 */
public class CountItemIdBlot extends BaseRichBolt {
	
	private OutputCollector collector;
	private LRUMap<Long, Integer> lruMap = new LRUMap<Long, Integer>(1000);
	
	/**
	 * 初始化bolt,获得OutputCollector
	 */
	@Override
	public void prepare(Map stormConf, TopologyContext context,
			OutputCollector collector) {
		this.collector = collector;
	}

	@Override
	public void execute(Tuple input) {
		Long itemId = input.getLongByField("parse");
		
		/**
		 * 统计每一个ItemId访问次数,并且放入LRUMap中
		 */
		Integer requestCount = lruMap.get(itemId);
		if(requestCount == null) {
			requestCount = 0;
		}
		
		requestCount++;
		lruMap.put(itemId, requestCount);
		System.out.println("日志--------------.>bolt2统计数据:"+lruMap.toString());
		
		collector.emit(new Values(itemId,requestCount));
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("itemId","count"));
	}
	
	/**
	 * 开启一个后台线程，统计热点数据
	 */
	private class GetHotItemIdThread implements Runnable {
		
		/**
		 * 从大到小排序LRUMap中的访问次数
		 */
		private List<Map.Entry<Long, Integer>> descList = new ArrayList<Map.Entry<Long, Integer>>();
		/**
		 * 存放当前热点商品id
		 */
		private List<Long> currentHotItem = new ArrayList<Long>();
		/**
		 * 标记之前到现在之间的热点数据,每次计算时不会清空list
		 */
		private List<Long> aliveHotItem = new ArrayList<Long>();

		@Override
		public void run() {
			while(true) {
				//倒序排序
				List<Entry<Long, Integer>> descResult = desc(lruMap, descList, currentHotItem);
				
				/**
				 * 统计后95%数据平均值的100倍,自动识别热点数据并且降级
				 */
				int index = (int) Math.floor(descResult.size() * 0.95);
				int count = 0;
				for(int i = index; i < descResult.size() - index; i++) {
					count += descResult.get(i).getValue();
				}
				
				int avg = ( count / index ) * 100;
				
				for(int i = 0; i < descList.size(); i++) {
					if(avg < descList.get(i).getValue()) {
						long itemId = descList.get(i).getKey();
						System.out.println("日志-------------》商品itemId="+itemId+"是当前热点数据，开始自动降级策略");
						//如果大于95%的100倍，判定为热点数据，发送http请求到nginx分发层，nginx采用
						//负载均衡策略分发，将该商品的数据发送到nginx的各个应用层
						String sendGetRequest = HttpClientUtils.sendGetRequest("http//:192.168.43.126/hot?itemId="+itemId);
						
						//调用数据直连服务和一站式服务，将相关数据发送到nginx
						Map<String, String> map = new HashMap<String, String>();
						String itemInfoAggr = HttpClientUtils.sendPostRequest("http://localhost:8085/item/getItemInfo", map);
						String itemCategoryAggr = HttpClientUtils.sendPostRequest("http://localhost:8085/item/getItemCategory", map);
						String itemBrandAggr = HttpClientUtils.sendPostRequest("http://localhost:8085/item/getItemBrand", map);
						String itemDescriptionAggr = HttpClientUtils.sendPostRequest("http://localhost:8085/item/getItemDescription", map);
						HttpClientUtils.sendGetRequest("http://192.168.43.104/setHostCache?itemInfoAggr="+itemInfoAggr+"itemCategoryAggr="+itemCategoryAggr+"itemBrandAggr="+itemBrandAggr+"itemDescriptionAggr="+itemDescriptionAggr);
						HttpClientUtils.sendGetRequest("http://192.168.43.51/setHostCache?itemInfoAggr="+itemInfoAggr+"itemCategoryAggr="+itemCategoryAggr+"itemBrandAggr="+itemBrandAggr+"itemDescriptionAggr="+itemDescriptionAggr);
					}
				}
				
				/**
				 * 自动识别热点数据降温，并且将降级恢复
				 */
				for(Long itemId : aliveHotItem) {
					if(!currentHotItem.contains(itemId)) {
						System.out.println("日志===================》热点数据itemId="+itemId+"失去热度，nginx分发策略从负载均衡改为路由请求");
						HttpClientUtils.sendGetRequest("http//:192.168.43.126/lost_hot?itemId="+itemId);
					}
				}
				
				/**
				 * 线程等待一段时间后继续计算
				 */
				Utils.sleep(500);
			}
		}
		
	}
	
	/**
	 * 将LRUMap中的数据进行排序，由大到小
	 */
	private List<Map.Entry<Long, Integer>> desc(LRUMap<Long, Integer> lruMap, 
			List<Map.Entry<Long, Integer>> descList, List<Long> currentHotItem) {
		//首先清空之前的排序数据
		descList.clear();
		currentHotItem.clear();
		/**
		 * 倒序排序
		 */
		for(Map.Entry<Long, Integer> mapEntry : lruMap.entrySet()) {
			if(descList.size() <= 0) {
				descList.add(mapEntry);
			} else {
				boolean flag = false;
				for(int i = 0; i < descList.size(); i++) {
					if(mapEntry.getValue() > descList.get(i).getValue()) {
						Entry<Long, Integer> lastOne = descList.get(descList.size() - 1);
						for(int j = descList.size() - 1; j >= i; j--) {
							descList.set(j, descList.get(j - 1));
						}
						descList.add(lastOne);
						descList.set(i, mapEntry);
						flag = true;
						break;
					}
				}
				
				if(!flag) {
					descList.add(mapEntry);
				}
			}
		}
		
		return descList;
	}
	
}
