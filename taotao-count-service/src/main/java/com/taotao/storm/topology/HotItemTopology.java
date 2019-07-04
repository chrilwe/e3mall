package com.taotao.storm.topology;

import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.StormSubmitter;
import org.apache.storm.topology.TopologyBuilder;
import org.apache.storm.tuple.Fields;
import org.apache.storm.utils.Utils;


import clojure.main;

import com.taotao.storm.bolt.CountItemIdBlot;
import com.taotao.storm.bolt.ParseItemIdBolt;
import com.taotao.storm.spout.GetItemIdFromQueueSpout;

/**
 * 热点商品访问统计拓扑
 * @author Administrator
 *
 */
public class HotItemTopology {
	
	public static void main(String[] args) {
		//初始化spring
		/*ApplicationContext context = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-comsumer.xml");*/
		
		TopologyBuilder builder = new TopologyBuilder();
		/**
		 * 参数：设置的名称，对应的spout或者bolt，excutor有多少个
		 */
		builder.setSpout("GetItemIdFromQueueSpout", new GetItemIdFromQueueSpout(), 2);
		builder.setBolt("ParseItemIdBolt", new ParseItemIdBolt(), 2)
							.setNumTasks(1)
							.shuffleGrouping("GetItemIdFromQueueSpout");
		builder.setBolt("CountItemIdBlot", new CountItemIdBlot(), 2)
							.setNumTasks(1)
							//这里的field对应的是上一级blot中的declareOutPutFields
							.fieldsGrouping("ParseItemIdBolt", new Fields("parse"));
		
		Config config = new Config();
		config.setDebug(true);
		
		if(args != null && args.length > 1) {
			config.setNumWorkers(3);  
			try {
				StormSubmitter.submitTopology(args[0], config, builder.createTopology());
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			//在本地eclipse运行
			LocalCluster cluster = new LocalCluster();
			cluster.submitTopology("Topology", config, builder.createTopology());  
			Utils.sleep(6000000); 
			cluster.shutdown();
		}
	}
}
