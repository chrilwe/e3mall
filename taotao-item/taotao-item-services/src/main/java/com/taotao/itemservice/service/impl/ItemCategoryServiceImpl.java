package com.taotao.itemservice.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.jms.Destination;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.taotao.itemservice.mapper.ItemCategoryMapper;
import com.taotao.itemservice.model.ItemCategory;
import com.taotao.itemservice.service.ItemCategoryService;
import com.taotao.itemservice.service.activemq.SendChangeDataProducter;
/**
 * 商品分类服务
 * @author Administrator
 *
 */
@Service
public class ItemCategoryServiceImpl implements ItemCategoryService {
	
	@Autowired
	private ItemCategoryMapper itemCategoryMapper;
	
	/**
	 * 添加商品分类
	 */
	@Override
	@Transactional
	public void addItemCategory(ItemCategory itemCategory ,String queueType) {
		itemCategoryMapper.addItemCategory(itemCategory);
		
		String dataType = "itemCategory";
		String eventType = "add";
		
		//发送添加消息
		SendChangeDataProducter producter = new SendChangeDataProducter();
		producter.sendMessage(dataType, eventType, itemCategory.getId(), queueType);
	}
	
	/**
	 * 删除商品分类
	 */
	@Override
	@Transactional
	public void deleteItemCategory(long id ,String queueType) {
		itemCategoryMapper.deleteItemCategory(id);
		
		String dataType = "itemCategory";
		String eventType = "add";
		
		//发送删除消息
		SendChangeDataProducter producter = new SendChangeDataProducter();
		producter.sendMessage(dataType, eventType, id, queueType);
	}
	
	/**
	 * 更新商品分类
	 */
	@Override
	@Transactional
	public void updateItemCategory(ItemCategory itemCategory ,String queueType) {
		itemCategoryMapper.updateItemCategory(itemCategory);
		
		String dataType = "itemCategory";
		String eventType = "add";
		
		//发送修改消息
		SendChangeDataProducter producter = new SendChangeDataProducter();
		producter.sendMessage(dataType, eventType, itemCategory.getId(), queueType);
	}
	
	/**
	 * id查询商品分类
	 */
	@Override
	public ItemCategory findItemCategoryById(long id) {
		ItemCategory itemCategory = itemCategoryMapper.findItemCategoryById(id);
		return itemCategory;
	}
	
	/**
	 * 根据父类查询子类
	 */
	@Override
	public List<ItemCategory> findItemCategoryByParentId(long parentId) {
		
		return itemCategoryMapper.findItemCategoryByParentId(parentId);
	}
	
	/**
	 * 批量查找
	 */
	@Override
	public List<ItemCategory> findItemCategorys(String ids) {
		List<ItemCategory> list = new ArrayList<ItemCategory>();
		String[] split = ids.split(",");
		for (String id : split) {
			ItemCategory itemCategory = findItemCategoryById(Long.parseLong(id));
			list.add(itemCategory);
		}
		return list;
	}

}
