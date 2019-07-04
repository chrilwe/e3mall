package com.taotao.count.test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;




import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;

public class TestKafka {
	public static void main(String[] args) {
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
	
	private static class KafkaMessageProcessor implements Runnable {
		
		private KafkaStream stream;
		
		public KafkaMessageProcessor(KafkaStream stream) {
			this.stream = stream;
		}

		@Override
		public void run() {
			ConsumerIterator<byte[], byte[]> iterator = stream.iterator();
			while(iterator.hasNext()) {
				String message = new String(iterator.next().message());
		
				System.out.println("message="+message);
			}
		}
		
	}
}
