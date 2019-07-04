package com.taotao.itemservice.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.taotao.itemservice.mapper.ItemDescriptionMapper;
import com.taotao.itemservice.model.ItemDescription;
import com.taotao.itemservice.service.ItemDescriptionService;
import com.taotao.itemservice.service.activemq.SendChangeDataProducter;
/**
 * 商品介绍服务
 * @author Administrator
 *
 */
@Service
public class ItemDescriptionServiceImpl implements ItemDescriptionService {
	
	@Autowired
	private ItemDescriptionMapper itemDescriptionMapper;
	
	/**
	 * 添加商品介绍
	 */
	@Override
	@Transactional
	public void addItemDescription(ItemDescription itemDescription ,String queueType) {
		itemDescriptionMapper.addItemDescription(itemDescription);
		
		String dataType = "itemDescription";
		String eventType = "add";
		
		SendChangeDataProducter producter = new SendChangeDataProducter();
		producter.sendMessage(dataType, eventType, itemDescription.getItemId(), queueType);
	}
	
	/**
	 * 删除商品介绍
	 */
	@Override
	@Transactional
	public void deleteItemDescription(long id ,String queueType) {
		itemDescriptionMapper.deleteItemDescription(id);
		
		String dataType = "itemDescription";
		String eventType = "delete";
		
		SendChangeDataProducter producter = new SendChangeDataProducter();
		producter.sendMessage(dataType, eventType, id, queueType);
	}
	
	/**
	 * 更新商品介绍
	 */
	@Override
	@Transactional
	public void updateItemDescription(ItemDescription itemDescription ,String queueType) {
		itemDescriptionMapper.updateItemDescription(itemDescription);
		
		String dataType = "itemDescription";
		String eventType = "update";
		
		SendChangeDataProducter producter = new SendChangeDataProducter();
		producter.sendMessage(dataType, eventType, itemDescription.getItemId(), queueType);
	}
	
	/**
	 * id查询商品介绍
	 */
	@Override
	public ItemDescription findItemDescriptionById(long id) {
		ItemDescription itemDescription = itemDescriptionMapper.findItemDescriptionById(id);
		return itemDescription;
	}

}
