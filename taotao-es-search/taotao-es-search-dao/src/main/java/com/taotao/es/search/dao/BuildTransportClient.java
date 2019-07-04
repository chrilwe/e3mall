package com.taotao.es.search.dao;

import java.net.InetAddress;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

public class BuildTransportClient {
	
	public static TransportClient build() throws Exception{
		Settings settings = Settings.builder().
				put("cluster.name", "elasticsearch").
				build();
		TransportClient client = new PreBuiltTransportClient(settings)
		.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("localhost"), 9300));
		
		return client;
	}
}
