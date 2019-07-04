package com.taotao.storm.spout;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ArrayBlockingQueue;

import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;

import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichSpout;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;
import org.apache.storm.utils.Utils;

import com.taotao.storm.singleton.SingletonQueue;

/**
 * 从内存队列中取出数据，并发送数据到blot
 * @author Administrator
 *
 */
public class GetItemIdFromQueueSpout extends BaseRichSpout {
	
	private SpoutOutputCollector collector;
	
	private ArrayBlockingQueue<String> queue = new ArrayBlockingQueue<String>(1000);
	
	/**
	 * 初始化spout，获得SpoutOutputCollector
	 */
	@Override
	public void open(Map conf, TopologyContext context,
			SpoutOutputCollector collector) {
		this.collector = collector;
		startKafaComsumer();
	}

	@Override
	public void nextTuple() {
		try {
			if(queue.size() > 0) {
				//当有请求访问商品详情页时，将从kafka消费到的消息发送给blot解析
				String message = queue.take();
				collector.emit(new Values(message));
			} else {
				Utils.sleep(500);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("message"));
	}
	
	/**
	 * 开启kafka
	 */
	private void startKafaComsumer() {
		
		Properties props = new Properties();
		props.put("zookeeper.connect", "192.168.43.51:2181,192.168.43.104:2181,192.168.43.126:2181");
		props.put("group.id", "taotao-itemCount-group");
        props.put("zookeeper.session.timeout.ms", "60000");
        props.put("zookeeper.sync.time.ms", "2000");
        props.put("auto.commit.interval.ms", "1000");
        
        ConsumerConfig config = new ConsumerConfig(props);
        
        ConsumerConnector javaConsumerConnector = Consumer.createJavaConsumerConnector(config);
        String topic = "access-log";
        Map<String, Integer> topicCountMap = new HashMap<String, Integer>();
        topicCountMap.put(topic, 1);
        Map<String, List<KafkaStream<byte[], byte[]>>> messageStreamsMap = javaConsumerConnector.createMessageStreams(topicCountMap);
        List<KafkaStream<byte[], byte[]>> streams = messageStreamsMap.get(topic);
        for (KafkaStream<byte[], byte[]> stream : streams) {
			new Thread(new KafkaMessageProcessor(stream)).start();
		}
	}
	
	/**
	 * 解析消息线程
	 */
	private class KafkaMessageProcessor implements Runnable {
		
		private KafkaStream stream;
		
		public KafkaMessageProcessor(KafkaStream stream) {
			this.stream = stream;
		}

		@Override
		public void run() {
			ConsumerIterator<byte[], byte[]> iterator = stream.iterator();
			while(iterator.hasNext()) {
				String message = new String(iterator.next().message());
				
				//将消费到的消息放入queue中
				try {
					System.out.println("=======================message==============="+message);
					/**
					 * 将消息放到队列中,供storm的spout消费，将数据发射到tuple中,将数据交给blot进行解析计算
					 */
					queue.put(message);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
	}
}
