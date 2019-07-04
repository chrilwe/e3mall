package com.taotao.itemservice.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.taotao.itemservice.mapper.ItemPropertyMapper;
import com.taotao.itemservice.model.ItemProperty;
import com.taotao.itemservice.service.ItemPropertyService;
import com.taotao.itemservice.service.activemq.SendChangeDataProducter;
/**
 * 商品属性服务
 * @author Administrator
 *
 */
@Service
public class ItemPropertyServiceImpl implements ItemPropertyService {
	
	@Autowired
	private ItemPropertyMapper itemPropertyMapper;
	
	/**
	 * 添加商品属性
	 */
	@Override
	@Transactional
	public void addItemProperty(ItemProperty itemProperty, String queueType) {
		
		itemPropertyMapper.addItemProperty(itemProperty);
		
		String dataType = "itemProperty";
		String eventType = "add";
		
		SendChangeDataProducter producter = new SendChangeDataProducter();
		producter.sendMessage(dataType, eventType, itemProperty.getItemId(), queueType);
	}
	
	/**
	 * 删除商品属性
	 */
	@Override
	@Transactional
	public void deleteItemProperty(long id, String queueType) {
		itemPropertyMapper.deleteItemProperty(id);
		
		String dataType = "itemProperty";
		String eventType = "delete";
		
		SendChangeDataProducter producter = new SendChangeDataProducter();
		producter.sendMessage(dataType, eventType, id, queueType);
	}
	
	/**
	 * 更新商品属性
	 */
	@Override
	@Transactional
	public void updateItemProperty(ItemProperty itemProperty, String queueType) {
		itemPropertyMapper.updateItemProperty(itemProperty);
		
		String dataType = "itemProperty";
		String eventType = "update";
		
		SendChangeDataProducter producter = new SendChangeDataProducter();
		producter.sendMessage(dataType, eventType, itemProperty.getItemId(), queueType);
	}
	
	/**
	 * 根据id查询商品属性
	 */
	@Override
	public ItemProperty findItemPropertyById(long id) {
		ItemProperty itemProperty = itemPropertyMapper.findItemPropertyById(id);
		return itemProperty;
	}
	
	/**
	 * 根据商品id查询
	 */
	@Override
	public ItemProperty findItemPropertyByItemId(long itemId) {
		
		return itemPropertyMapper.findItemPropertyByItemId(itemId);
	}

}
