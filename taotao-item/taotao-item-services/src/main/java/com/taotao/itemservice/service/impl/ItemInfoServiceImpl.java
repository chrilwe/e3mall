package com.taotao.itemservice.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.taotao.itemservice.mapper.ItemCategoryMapper;
import com.taotao.itemservice.mapper.ItemDescriptionMapper;
import com.taotao.itemservice.mapper.ItemInfoMapper;
import com.taotao.itemservice.mapper.ItemTypeMapper;
import com.taotao.itemservice.model.Item;
import com.taotao.itemservice.model.ItemCategory;
import com.taotao.itemservice.model.ItemDescription;
import com.taotao.itemservice.model.ItemPageBean;
import com.taotao.itemservice.service.ItemInfoService;
import com.taotao.itemservice.service.activemq.SendChangeDataProducter;
/**
 * 商品服务service
 * @author Administrator
 *
 */
@Service
public class ItemInfoServiceImpl implements ItemInfoService {
	
	@Autowired
	private ItemInfoMapper itemInfoMapper;
	@Autowired
	private ItemDescriptionMapper itemDescriptionMapper;
	@Autowired
	private ItemTypeMapper itemTypeMapper;
	@Autowired
	private ItemCategoryMapper itemCategoryMapper;
	
	@Value("${ItemInstock}")
	private Integer ItemInstock;
	/**
	 * 根据商品id查询商品信息
	 */
	@Override
	public Item getItemInfoById(long itemId) {
		
		Item item = itemInfoMapper.getItemInfoById(itemId);
		
		return item;
	}
	
	/**
	 * 添加商品
	 */
	@Override
	@Transactional
	public void addItem(Item item, String queueType) {
		
		itemInfoMapper.addItem(item);
		
		String dataType = "itemInfo";
		String eventType = "add";
		
		SendChangeDataProducter producter = new SendChangeDataProducter();
		producter.sendMessage(dataType, eventType, item.getId(), queueType);
	}
	
	/**
	 * 删除商品
	 */
	@Override
	@Transactional
	public void deleteItem(long itemId, String queueType) {
		
		itemInfoMapper.deleteItem(itemId);
		
		String dataType = "itemInfo";
		String eventType = "delete";
		
		SendChangeDataProducter producter = new SendChangeDataProducter();
		producter.sendMessage(dataType, eventType, itemId, queueType);
	}
	
	/**
	 * 修改商品
	 */
	@Override
	@Transactional
	public void updateItem(Item item, String queueType) {
		
		itemInfoMapper.updateItem(item);
		
		String dataType = "itemInfo";
		String eventType = "update";
		
		SendChangeDataProducter producter = new SendChangeDataProducter();
		producter.sendMessage(dataType, eventType, item.getId(), queueType);
	}
	
	/**
	 * 分页查询
	 */
	@Override
	public ItemPageBean findItems(int page, int pageSize) {
		List<Item> items = itemInfoMapper.findItemList(page, pageSize);
		int total = itemInfoMapper.count();
		ItemPageBean itemPageBean = new ItemPageBean();
		itemPageBean.setRows(items);
		itemPageBean.setTotal(total);
		return itemPageBean;
	}
	
	/**
	 * 批量删除商品服务
	 */
	@Override
	@Transactional
	public void deleteItems(String ids, String queueType) {
		//解析ids
		String[] idArray = ids.split(",");
		for (String idStr : idArray) {
			itemInfoMapper.deleteItem(Long.parseLong(idStr));
			
			String dataType = "itemInfo";
			String eventType = "delete";
			
			SendChangeDataProducter producter = new SendChangeDataProducter();
			producter.sendMessage(dataType, eventType, Long.parseLong(idStr), queueType);
		}
	}
	
	/**
	 * 批量更新商品状态信息服务
	 */
	@Override
	@Transactional
	public void updateItemsStatus(String ids, String queueType) {
		String[] idArray = ids.split(",");
		
		for (String idStr : idArray) {
			itemInfoMapper.updateItemStatusById(Long.parseLong(idStr), ItemInstock);
			
			/*String dataType = "itemInfo";
			String eventType = "update";
			
			SendChangeDataProducter producter = new SendChangeDataProducter();
			producter.sendMessage(dataType, eventType, Long.parseLong(idStr), queueType);*/
		}
	}
	
	/**
	 * 批量查询接口
	 */
	@Override
	public List<Item> getItemsByIds(String ids) {
		List<Item> list = new ArrayList<Item>();
		String[] split = ids.split(",");
		for (String id : split) {
			Item item = getItemInfoById(Long.parseLong(id));
			list.add(item);
		}
		return list;
	}
	
	/**
	 * 更新商品的聚合信息
	 */
	@Override
	@Transactional
	public void updateItemInfoAndDesc(Item item, ItemDescription itemDescription) {
		itemInfoMapper.updateItem(item);
		itemDescriptionMapper.updateItemDescription(itemDescription);
	}
	
	/**
	 * 根据分类的父类id查询子类
	 */
	@Override
	public List<ItemCategory> findCidByParentId(long parentId) {
		
		return itemCategoryMapper.findItemCategoryByParentId(parentId);
	}
	
	/**
	 * 根据商品分类id查询商品类型别名(作为搜索索引的type)
	 */
	@Override
	public String findItemTypeCid(long cid) {
		
		return itemTypeMapper.findItemTypeByCid(cid);
	}

}
