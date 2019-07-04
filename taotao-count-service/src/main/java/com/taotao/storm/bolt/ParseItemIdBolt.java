package com.taotao.storm.bolt;

import java.util.Map;

import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;

import com.alibaba.fastjson.JSONObject;

/**
 * 解析spout发射的数据,并且发送到下一个blot
 * @author Administrator
 *
 */
public class ParseItemIdBolt extends BaseRichBolt {
	
	private OutputCollector collector;
	
	/**
	 * 初始化blot，获得OutputCollector
	 */
	@Override
	public void prepare(Map stormConf, TopologyContext context,
			OutputCollector collector) {
		this.collector = collector;
	}

	@Override
	public void execute(Tuple input) {
		String message = input.getStringByField("message");
		System.out.println("日志-------------》从spout获取message="+message);
		JSONObject jsonObject = JSONObject.parseObject(message);
		JSONObject ArgsObject = jsonObject.getJSONObject("uri_args");
		Long itemId = ArgsObject.getLongValue("itemId");
		System.out.println("日志-------------》productId="+itemId);
		if(itemId != null) {
			collector.emit(new Values(itemId));
		}
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("parse"));
	}
	
}
