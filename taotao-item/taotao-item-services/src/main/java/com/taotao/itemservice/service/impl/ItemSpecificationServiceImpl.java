package com.taotao.itemservice.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.taotao.itemservice.mapper.ItemSpecificationMapper;
import com.taotao.itemservice.model.ItemSpecification;
import com.taotao.itemservice.service.ItemSpecificationService;
import com.taotao.itemservice.service.activemq.SendChangeDataProducter;
/**
 * 商品规格服务
 * @author Administrator
 *
 */
@Service
public class ItemSpecificationServiceImpl implements ItemSpecificationService {
	
	@Autowired
	private ItemSpecificationMapper itemSpecificationMapper;
	
	/**
	 * 添加商品规格
	 */
	@Override
	@Transactional
	public void addItemSpecification(ItemSpecification itemSpecification, String queueType) {
		itemSpecificationMapper.addItemSpecification(itemSpecification);
		
		String dataType = "itemSpecification";
		String eventType = "add";
		
		SendChangeDataProducter producter = new SendChangeDataProducter();
		producter.sendMessage(dataType, eventType, itemSpecification.getItemId(), queueType);
	}
	
	/**
	 * 删除商品规格
	 */
	@Override
	@Transactional
	public void deleteItemSpecification(long id, String queueType) {
		itemSpecificationMapper.deleteItemSpecification(id);
		
		String dataType = "itemSpecification";
		String eventType = "delete";
		
		SendChangeDataProducter producter = new SendChangeDataProducter();
		producter.sendMessage(dataType, eventType, id, queueType);
	
	}
	
	/**
	 * 更新商品规格
	 */
	@Override
	@Transactional
	public void updateItemSpecification(ItemSpecification itemSpecification, String queueType) {
		itemSpecificationMapper.updateItemSpecification(itemSpecification);
		
		String dataType = "itemSpecification";
		String eventType = "update";
		
		SendChangeDataProducter producter = new SendChangeDataProducter();
		producter.sendMessage(dataType, eventType, itemSpecification.getItemId(), queueType);
	}
	
	/**
	 * 根据商品规格id查询商品规格
	 */
	@Override
	public ItemSpecification findItemSpecificationById(long id) {
		ItemSpecification itemSpecification = itemSpecificationMapper.findItemSpecificationById(id);
		return itemSpecification;
	}

}
