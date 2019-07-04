package com.taotao.dataaggr.activemq;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.springframework.beans.factory.annotation.Autowired;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import com.taotao.dataaggr.model.ItemBrandAggr;
import com.taotao.dataaggr.model.ItemCategoryAggr;
import com.taotao.dataaggr.model.ItemDescriptionAggr;
import com.taotao.dataaggr.model.ItemInfoAggr;
import com.taotao.dataaggr.utils.JsonUtils;
import com.taotao.itemservice.model.Item;
import com.taotao.itemservice.model.ItemBrand;
import com.taotao.itemservice.model.ItemCategory;
import com.taotao.itemservice.model.ItemDescription;
import com.taotao.itemservice.model.ItemProperty;
import com.taotao.itemservice.model.ItemSpecification;

/**
 * 批量处理队列
 * @author Administrator
 *
 */
public class ChangeBetchQueueReciver implements MessageListener {

	@Autowired
	private JedisPool jedisPool;

	@Override
	public void onMessage(Message message) {
		try {
			/**
			 * 解析数据
			 * @Param dataType 维度类型
			 * @Param eventType 是更新数据还是删除数据
			 * @Param dimId 聚合数据的id
			 */
			TextMessage textMessage = (TextMessage) message;
			String text = textMessage.getText();
			String[] data = text.split(",");
			
			String dataType = data[0];
			String eventType = data[1];
			long dimId = Long.parseLong(data[2]);
			
			/**
			 * 更新每一个维度的数据
			 */
			if(dataType.equals("ItemBrand")) {
				dimItemBrandDataChange(eventType, dimId);
			} else if(dataType.equals("ItemCategory")) {
				dimItemCategoryDataChange(eventType, dimId);
			} else if(dataType.equals("ItemDescription")) {
				dimItemDescriptionDataChange(eventType, dimId);
			} else if(dataType.equals("ItemInfo")) {
				dimItemInfoDataChange(eventType, dimId);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 商品品牌维度数据变更
	 */
	private void dimItemBrandDataChange(String eventType, long itemId) {
		Jedis jedis = jedisPool.getResource();
		//品牌数据更新
		if(eventType.equals("update")) {
			//从缓存主集群中获取组成品牌维度的数据
			String itemBrandStr = jedis.get("itemBrand_" + itemId);
			System.out.println("======日志======从redis缓存主集群中查询品牌的原子数据:" + itemBrandStr);
			
			//将组成品牌维度的原子数据组合在一起放到缓存主集群中
			ItemBrandAggr itemBrandAggr = new ItemBrandAggr();
			itemBrandAggr.setItemId(itemId);
			itemBrandAggr.setItemBrand(JsonUtils.jsonToPojo(itemBrandStr, ItemBrand.class));
			if(itemBrandStr != null && !itemBrandStr.equals("")) {
				jedis.set("dim_item_brand_" + itemId, JsonUtils.objectToJson(itemBrandAggr));
				System.out.println("======日志=====将聚合的品牌数据更新到缓存主集群中");
			}
		} else {
			//删除缓存主集群的聚合数据
			jedis.del("dim_item_brand_" + itemId);
			System.out.println("======日志======删除缓存主集群中品牌维度的聚合数据");
		}
	}
	
	/**
	 * 商品分类的数据变更
	 */
	private void dimItemCategoryDataChange(String eventType, long itemId) {
		Jedis jedis = jedisPool.getResource();
		//分类数据更新
		if(eventType.equals("update")) {
			//从缓存主集群中获取组成分类维度的数据
			String itemCategoryStr = jedis.get("itemCategory_" + itemId);
			
			//将组成分类维度的原子数据组合在一起放到缓存主集群中
			ItemCategoryAggr itemCategoryAggr = new ItemCategoryAggr();
			itemCategoryAggr.setItemCategory(JsonUtils.jsonToPojo(itemCategoryStr, ItemCategory.class));
			itemCategoryAggr.setItemId(itemId);
			
			if(itemCategoryStr != null && !itemCategoryStr.equals("")) {
				jedis.set("dim_item_category_" + itemId, JsonUtils.objectToJson(itemCategoryAggr));
			}
		} else {
			//删除缓存主集群的聚合数据
			jedis.del("dim_item_category_" + itemId);
		}
	}
	
	/**
	 * 商品描述维度的数据变更
	 */
	private void dimItemDescriptionDataChange(String eventType, long itemId) {
		Jedis jedis = jedisPool.getResource();
		//介绍数据更新
		if(eventType.equals("update")) {
			//从缓存主集群中获取组成分类维度的数据
			String itemDescriptionStr = jedis.get("itemDescription_" + itemId);
			
			//将组成分类维度的原子数据组合在一起放到缓存主集群中
			ItemDescriptionAggr itemDescriptionAggr = new ItemDescriptionAggr();
			itemDescriptionAggr.setItemDescription(JsonUtils.jsonToPojo(itemDescriptionStr, ItemDescription.class));
			itemDescriptionAggr.setItemId(itemId);
			
			if(itemDescriptionStr != null && !itemDescriptionStr.equals("")) {
				jedis.set("dim_item_description_" + itemId, JsonUtils.objectToJson(itemDescriptionAggr));
			}
		} else {
			//删除缓存主集群的聚合数据
			jedis.del("dim_item_description_" + itemId);
		}
	}
	
	/**
	 * 商品基本数据类型变更
	 */
	private void dimItemInfoDataChange(String eventType, long itemId) {
		Jedis jedis = jedisPool.getResource();
		
		//基本数据更新
		if(eventType.equals("update")) {
			//从缓存主集群中获取组成基本维度的数据
			String itemInfoStr = jedis.get("itemInfo_" + itemId);
			String itemPropertyStr = jedis.get("itemProperty_" + itemId);
			String itemSpecificationStr = jedis.get("itemSpecification_" + itemId);
			
			//将组成基本维度的原子数据组合在一起放到缓存主集群中
			ItemInfoAggr itemInfoAggr = new ItemInfoAggr();
			itemInfoAggr.setItem(JsonUtils.jsonToPojo(itemInfoStr, Item.class));
			itemInfoAggr.setItemProperty(JsonUtils.jsonToPojo(itemPropertyStr, ItemProperty.class));
			itemInfoAggr.setItemSpecification(JsonUtils.jsonToPojo(itemSpecificationStr, ItemSpecification.class));
			itemInfoAggr.setItemId(itemId);
			
			jedis.set("dim_item_info_" + itemId, JsonUtils.objectToJson(itemInfoAggr));
		} else {
			//删除缓存主集群的聚合数据
			jedis.del("dim_item_description_" + itemId);
		}
	}

}
