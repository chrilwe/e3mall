package com.taotao.itemservice.service.impl;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.taotao.itemservice.mapper.ItemBrandMapper;
import com.taotao.itemservice.model.ItemBrand;
import com.taotao.itemservice.service.ItemBrandService;
import com.taotao.itemservice.service.activemq.SendChangeDataProducter;
/**
 * 商品品牌服务
 * @author Administrator
 *
 */
@Service
public class ItemBrandServiceImpl implements ItemBrandService {
	
	@Autowired
	private ItemBrandMapper itemBrandMapper;
	
	/**
	 * 商品品牌添加
	 */
	@Override
	@Transactional
	public void addItemBrand(ItemBrand itemBrand ,String queueType) {
		itemBrandMapper.addItemBrand(itemBrand);
		System.out.println("=====日志=====商品品牌数据添加到数据库" + itemBrand);
		
		//发送品牌添加消息到等待队列
		String dataType = "itemBrand";
		String eventType = "add";
		SendChangeDataProducter producter = new SendChangeDataProducter();
		producter.sendMessage(dataType, eventType, itemBrand.getId(), queueType);
		System.out.println("====日志====发送品牌添加消息到等待消息队列" + "dataType:" + dataType + ",eventType:" + eventType + ",itemBrandId" + itemBrand.getId());
	}
	
	/**
	 * 删除商品品牌
	 */
	@Override
	@Transactional
	public void deleteItemBrand(long id ,String queueType) {
		itemBrandMapper.deleteItemBrand(id);
		
		//删除品牌，将删除消息发送到等待消息队列
		String dataType = "itemBrand";
		String eventType = "delete";
		SendChangeDataProducter producter = new SendChangeDataProducter();
		producter.sendMessage(dataType, eventType, id, queueType);
	}
	
	/**
	 * 更新商品品牌
	 */
	@Override
	@Transactional
	public void updateItemBrand(ItemBrand itemBrand ,String queueType) {
		itemBrandMapper.updateItemBrand(itemBrand);
		
		//更新品牌，将更新消息发送到等待消息队列
		String dataType = "itemBrand";
		String eventType = "update";
		SendChangeDataProducter producter = new SendChangeDataProducter();
		producter.sendMessage(dataType, eventType, itemBrand.getId(), queueType);
	}
	
	/**
	 * 根据id查询商品品牌
	 */
	@Override
	public ItemBrand findItemBrandById(long id) {
		ItemBrand itemBrand = itemBrandMapper.findItemBrandById(id);
		return itemBrand;
	}
	
	/**
	 * 商品id查询
	 */
	@Override
	public ItemBrand findItemBrandByItemId(long itemId) {
		
		return itemBrandMapper.findItemBrandByItemId(itemId);
	}
	
}
