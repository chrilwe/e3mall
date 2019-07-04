package com.taotao.dataAsyc.activemq;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

import com.taotao.dataAsyc.utils.JsonUtils;
import com.taotao.itemservice.model.Item;
import com.taotao.itemservice.model.ItemBrand;
import com.taotao.itemservice.model.ItemCategory;
import com.taotao.itemservice.model.ItemDescription;
import com.taotao.itemservice.model.ItemProperty;
import com.taotao.itemservice.model.ItemSpecification;
import com.taotao.itemservice.service.ItemBrandService;
import com.taotao.itemservice.service.ItemCategoryService;
import com.taotao.itemservice.service.ItemDescriptionService;
import com.taotao.itemservice.service.ItemInfoService;
import com.taotao.itemservice.service.ItemPropertyService;
import com.taotao.itemservice.service.ItemSpecificationService;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
/**
 * 监听商品服务数据变更消息
 * @author Administrator
 *
 */
public class DataChangeMessageReciver implements MessageListener {
	
	@Autowired
	private JedisPool jedisPool;
	
	@Autowired
	private JmsTemplate jmsTemplate;
	
	@Autowired
	private Destination dataAggrDestination;
	
	@Autowired
	private ItemBrandService itemBrandService;
	
	@Autowired
	private ItemCategoryService itemCategoryService;
	
	@Autowired
	private ItemPropertyService itemPropertyService;
	
	@Autowired
	private ItemDescriptionService itemDescriptionService;
	
	@Autowired
	private ItemInfoService itemInfoService;
	
	@Autowired
	private ItemSpecificationService itemSpecificationService;
	
	//去重数据结构,线程安全
	private Set<String> removeSameDataSet = Collections.synchronizedSet(new HashSet<String>());
	
	private String dimEventType = "";
	private String dimDataType = "";
	
	public DataChangeMessageReciver() {
		//初始化线程
		new Thread(new RemoveSameDataThread()).start();
	}
	
	@Override
	public void onMessage(Message message) {
		try {
			
			//获取消息
			TextMessage textMessage = (TextMessage)message;
			String text = textMessage.getText();
			
			//解析消息
			String[] data = text.split(",");
			String dataType = data[0];
			String eventType = data[1];
			long id = Long.parseLong(data[2]);
			
			if(dataType.equals("itemBrand")) {
				//品牌原子数据变更
				processItemBrandDataChange(eventType, id);
			} else if(dataType.equals("itemCategory")) {
				//商品分类数据变更
				processItemCategoeyDataChange(eventType, id);
			} else if(dataType.equals("itemDescription")) {
				//商品介绍变更
				processItemDescriptionDataChange(eventType, id);
			} else if (dataType.equals("itemInfo")) {
				//商品基本信息变更
				processItemInfoDataChange(eventType, id);
			} else if (dataType.equals("itemProperty")) {
				//商品属性信息变更
				processItemPropertyDataChange(eventType, id);
			} else if (dataType.equals("itemSpecification")) {
				//商品规格信息变更
				processItemSpecificationDataChange(eventType, id);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 品牌数据类型变更消息
	 */
	private void processItemBrandDataChange(String eventType, long id) {
		
		Jedis jedis = jedisPool.getResource();
		
		//调用商品服务查询
		ItemBrand itemBrand = itemBrandService.findItemBrandById(id);
		System.out.println("=====日志=====调用品牌数据服务接口,获取品牌内容:" + itemBrand );
		
		if(eventType.equals("add") || eventType.equals("update")) {
			//添加消息
			//更新缓存主集群
			jedis.set("itemBrand_" + itemBrand.getItemId(), JsonUtils.objectToJson(itemBrand));
			System.out.println("=====日志=====更新品牌数据到redis缓存主集群中");
			
			//把属于该维度的组成部分的id，发送到消息队列中
			String dimDataType = "ItemBrand";
			String dimEventType = "update";
			
			//将发送的消息放到set集合去重
			removeSameDataSet.add(dimDataType + "," + dimEventType + "," + itemBrand.getItemId());
			SendMessager sendMessager = new SendMessager(dimDataType, dimEventType, 
					itemBrand.getItemId(), jmsTemplate, dataAggrDestination);
			sendMessager.sendMessage();
			System.out.println("=====日志======发送更新消息到数据聚合服务" + ",dimDataType:" + dimDataType + ",dimEventType:" + dimEventType +",itemId:" + itemBrand.getItemId());
			
		} else {
			//删除消息
			//将缓存中的数据删除
			jedis.del("itemBrand_" + itemBrand.getItemId());
			System.out.println("=====日志=====从redis缓存主集群中删除数据");
			
			//把属于该维度的组成部分的id，发送到消息队列中
			String dimDataType = "ItemBrand";
			String dimEventType = "delete";
			
			//将发送的消息放到set集合去重
			removeSameDataSet.add(dimDataType + "," + dimEventType + "," + itemBrand.getItemId());
			SendMessager sendMessager = new SendMessager(dimDataType, dimEventType, 
					itemBrand.getItemId(), jmsTemplate, dataAggrDestination);
			sendMessager.sendMessage();
			System.out.println("=====日志======发送删除消息到数据聚合服务" + ",dimDataType:" + dimDataType + ",dimEventType:" + dimEventType +",itemId:" + itemBrand.getItemId());
		}
	}
	
	/**
	 * 商品分类数据的变更消息
	 */
	private void processItemCategoeyDataChange(String eventType, long id) {
		Jedis jedis = jedisPool.getResource();
		
		//调用商品基本信息接口，查询商品分类id
		Item item = itemInfoService.getItemInfoById(id);
		//调用商品分类服务接口，根据商品分类id查询数据
		ItemCategory itemCategory = itemCategoryService.findItemCategoryById(item.getCid());
		
		//更新或者删除的消息
		if(eventType.equals("update")) {
			//更新缓存主集群的数据
			jedis.set("itemCategory_" + id, JsonUtils.objectToJson(itemCategory));
			
			//发送变更消息到数据聚合服务
			String dimDataType = "ItemCategory";
			String dimEventType = "update";
			
			//将发送的消息放到set集合去重
			removeSameDataSet.add(dimDataType + "," + dimEventType + "," + id);
			SendMessager sendMessager = new SendMessager(dimDataType, dimEventType, 
					id, jmsTemplate, dataAggrDestination);
			sendMessager.sendMessage();
		} else {
			//删除消息
			//删除缓存主集群的数据
			jedis.del("itemCategory_" + id);
			
			//发送删除消息到数据聚合服务
			String dimDataType = "ItemCategory";
			String dimEventType = "delete";
			
			//将发送的消息放到set集合去重
			removeSameDataSet.add(dimDataType + "," + dimEventType + "," + id);
			SendMessager sendMessager = new SendMessager(dimDataType, dimEventType, 
					id, jmsTemplate, dataAggrDestination);
			sendMessager.sendMessage();
		}
	}
	
	/**
	 * 商品介绍数据变更消息
	 */
	private void processItemDescriptionDataChange(String eventType, long id) {
		Jedis jedis = jedisPool.getResource();
		//调用商品介绍服务的接口，根据id查询
		ItemDescription itemDescription = itemDescriptionService.findItemDescriptionById(id);
		
		//添加或者更新消息
		if(eventType.equals("add") || eventType.equals("update")) {
			//更新缓存主集群的数据
			jedis.set("itemDescription_" + itemDescription.getItemId(), JsonUtils.objectToJson(itemDescription));
			
			//发送变更消息到数据聚合服务
			String dimDataType = "ItemDescription";
			String dimEventType = "update";
			
			//将发送的消息放到set集合去重
			removeSameDataSet.add(dimDataType + "," + dimEventType + "," + itemDescription.getItemId());
			SendMessager sendMessager = new SendMessager(dimDataType, dimEventType, 
					id, jmsTemplate, dataAggrDestination);
			sendMessager.sendMessage();
		} else {
			//删除消息
			//删除缓存主集群的数据
			jedis.del("itemDescription_" + itemDescription.getItemId());
			
			//发送消息到数据聚合服务
			String dimDataType = "ItemDescription";
			String dimEventType = "delete";
			
			//将发送的消息放到set集合去重
			removeSameDataSet.add(dimDataType + "," + dimEventType + "," + itemDescription.getItemId());
			SendMessager sendMessager = new SendMessager(dimDataType, dimEventType, 
					id, jmsTemplate, dataAggrDestination);
			sendMessager.sendMessage();
		}
		
	}
	
	/**
	 * 商品基本数据变更消息
	 */
	private void processItemInfoDataChange(String eventType, long id) {
		Jedis jedis = jedisPool.getResource();
		//调用商品介绍服务的接口，根据id查询
		Item item = itemInfoService.getItemInfoById(id);
		
		//添加或者更新消息
		if(eventType.equals("add") || eventType.equals("update")) {
			//更新缓存主集群的数据
			jedis.set("itemInfo_" + id, JsonUtils.objectToJson(item));
			
			//发送变更消息到数据聚合服务
			String dimDataType = "ItemInfo";
			String dimEventType = "update";
			
			
			//将发送的消息放到set集合去重
			removeSameDataSet.add(dimDataType + "," + dimEventType + "," + id);
			SendMessager sendMessager = new SendMessager(dimDataType, dimEventType, 
					id, jmsTemplate, dataAggrDestination);
			sendMessager.sendMessage();
		} else {
			//删除消息
			//删除缓存主集群的数据
			jedis.del("itemInfo_" + id);
			
			//发送消息到数据聚合服务
			String dimDataType = "ItemInfo";
			String dimEventType = "delete";
			
			//将发送的消息放到set集合去重
			removeSameDataSet.add(dimDataType + "," + dimEventType + "," + id);
			SendMessager sendMessager = new SendMessager(dimDataType, dimEventType, 
					id, jmsTemplate, dataAggrDestination);
			sendMessager.sendMessage();
		}
	}
	
	/**
	 * 商品属性变更消息
	 */
	private void processItemPropertyDataChange(String eventType, long id) {
		Jedis jedis = jedisPool.getResource();
		//调用商品介绍服务的接口，根据id查询
		ItemProperty itemProperty = itemPropertyService.findItemPropertyById(id);
		
		//添加或者更新消息
		if(eventType.equals("add") || eventType.equals("update")) {
			//更新缓存主集群的数据
			jedis.set("itemProperty_" + itemProperty.getItemId(), JsonUtils.objectToJson(itemProperty));
			
			//发送变更消息到数据聚合服务
			String dimDataType = "ItemInfo";
			String dimEventType = "update";
			
			//将发送的消息放到set集合去重
			removeSameDataSet.add(dimDataType + "," + dimEventType + "," + itemProperty.getItemId());
			SendMessager sendMessager = new SendMessager(dimDataType, dimEventType, 
					itemProperty.getItemId(), jmsTemplate, dataAggrDestination);
			sendMessager.sendMessage();
		} else {
			//删除消息
			//删除缓存主集群的数据
			jedis.del("itemProperty_" + itemProperty.getItemId());
			
			//发送消息到数据聚合服务
			String dimDataType = "ItemInfo";
			String dimEventType = "delete";
			
			//将发送的消息放到set集合去重
			removeSameDataSet.add(dimDataType + "," + dimEventType + "," + itemProperty.getItemId());
			SendMessager sendMessager = new SendMessager(dimDataType, dimEventType, 
					itemProperty.getItemId(), jmsTemplate, dataAggrDestination);
			sendMessager.sendMessage();
		}
	}
	
	/**
	 * 商品规格数据变更消息
	 */
	private void processItemSpecificationDataChange(String eventType, long id) {
		Jedis jedis = jedisPool.getResource();
		//调用商品介绍服务的接口，根据id查询
		ItemSpecification itemSpecification = itemSpecificationService.findItemSpecificationById(id);
		
		//添加或者更新消息
		if(eventType.equals("add") || eventType.equals("update")) {
			//更新缓存主集群的数据
			jedis.set("itemSpecification_" + itemSpecification.getItemId(), JsonUtils.objectToJson(itemSpecification));
			
			//发送变更消息到数据聚合服务
			String dimDataType = "ItemInfo";
			String dimEventType = "update";
			
			//将发送的消息放到set集合去重
			removeSameDataSet.add(dimDataType + "," + dimEventType + "," + itemSpecification.getItemId());
			SendMessager sendMessager = new SendMessager(dimDataType, dimEventType, 
					itemSpecification.getItemId(), jmsTemplate, dataAggrDestination);
			sendMessager.sendMessage();
		} else {
			//删除消息
			//删除缓存主集群的数据
			jedis.del("itemSpecification_" + itemSpecification.getItemId());
			
			//发送消息到数据聚合服务
			String dimDataType = "ItemInfo";
			String dimEventType = "delete";
			
			//将发送的消息放到set集合去重
			removeSameDataSet.add(dimDataType + "," + dimEventType + "," + itemSpecification.getItemId());
			
		}
	}
	
	/**
	 * 去重后的消息，每隔5分钟发送一次
	 */
	private class RemoveSameDataThread implements Runnable {

		@Override
		public void run() {
			while(true) {
				if(removeSameDataSet.size() > 0) {
					for(String message : removeSameDataSet) {
						String[] messageArr = message.split(",");
						long itemId = Long.parseLong(messageArr[2]);
						
						SendMessager sendMessager = new SendMessager(dimDataType, dimEventType, 
								itemId, jmsTemplate, dataAggrDestination);
						sendMessager.sendMessage();
					}
				}
				
				try {
					//等待5分钟
					Thread.sleep(50000);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
		}
		
	}
	
}

