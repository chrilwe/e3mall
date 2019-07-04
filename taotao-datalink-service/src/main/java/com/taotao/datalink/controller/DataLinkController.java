package com.taotao.datalink.controller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.datalink.cache.rebuild.queue.RebuildItemBrandAggrCacheQueue;
import com.taotao.datalink.cache.rebuild.queue.RebuildItemCatAggrCacheQueue;
import com.taotao.datalink.cache.rebuild.queue.RebuildItemDescriptionAggrCacheQueue;
import com.taotao.datalink.cache.rebuild.queue.RebuildItemInfoAggrCacheQueue;
import com.taotao.datalink.hystrix.FindItemBrandByItemIdCommand;
import com.taotao.datalink.hystrix.FindItemCategoryByIdCommand;
import com.taotao.datalink.hystrix.FindItemDescriptionByIdCommand;
import com.taotao.datalink.hystrix.FindItemPropertyByItemIdCommand;
import com.taotao.datalink.hystrix.FindItemSpecificationByIdCommand;
import com.taotao.datalink.hystrix.GetItemInfoByIdCommand;
import com.taotao.datalink.model.ItemBrandAggr;
import com.taotao.datalink.model.ItemCategoryAggr;
import com.taotao.datalink.model.ItemDescriptionAggr;
import com.taotao.datalink.model.ItemInfoAggr;
import com.taotao.datalink.service.DatalinkService;
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

/**
 * 数据直连服务
 * @author Administrator
 *
 */
@Controller
public class DataLinkController {
	
	@Autowired
	private DatalinkService datalinkService;
	@Autowired
	private ItemInfoService itemInfoService;
	@Autowired
	private ItemSpecificationService itemSpecificationService;
	@Autowired
	private ItemPropertyService itemPropertyService;
	@Autowired
	private ItemBrandService itemBrandService;
	@Autowired
	private ItemCategoryService itemCategoryService;
	@Autowired
	private ItemDescriptionService itemDescriptionService;
	/**
	 * 查询商品基本信息维度的聚合数据
	 * @param itemId
	 * @return
	 */
	@RequestMapping("/item/getItemInfo")
	@ResponseBody
	public ItemInfoAggr getItemInfos(long itemId) {
		//从本地内存中查询商品基本信息维度的聚合数据
		ItemInfoAggr itemInfoAggr = datalinkService.getItemInfoAggrFromEhcache(itemId);
		System.out.println("日志=================》从数据直连本地缓存中获取商品聚合数据:" + itemInfoAggr);
		
		//本地缓存中没有取到数据,从redis缓存主集群中获取数据
		if(itemInfoAggr == null) {
			itemInfoAggr = datalinkService.getItemInfoAggrFromRedis(itemId);
			System.out.println("日志===============》从数据直连服务的缓存从集群中获取商品聚合数据:" + itemInfoAggr);
		}
		
		//redis缓存主集群没有取到数据，调用商品基本信息维度服务获取各个组成原子数据
		if(itemInfoAggr == null) {
			//资源隔离，限流，缓存穿透的降级处理
			GetItemInfoByIdCommand itemInfoCommand = new GetItemInfoByIdCommand(itemId, itemInfoService);
			Item item = itemInfoCommand.execute();
			System.out.println("日志================》数据直连服务调用商品基本数据服务获取数据:" + item);
			
			FindItemPropertyByItemIdCommand itemPropertyCommand = new FindItemPropertyByItemIdCommand(itemId, itemPropertyService);
			ItemProperty itemProperty = itemPropertyCommand.execute();
			System.out.println("日志================》数据直连服务调用商品属性数据服务获取数据:" + itemProperty);
			
			FindItemSpecificationByIdCommand itemSpecificationCommand = new FindItemSpecificationByIdCommand(itemId, itemSpecificationService);
			ItemSpecification itemSpecification = itemSpecificationCommand.execute();
			System.out.println("日志================》数据直连服务调用商品规格数据服务获取数据:" + itemSpecification);
			
			//聚合数据
			ItemInfoAggr itemAggr = new ItemInfoAggr();
			itemAggr.setCid(item.getCid());
			itemAggr.setImage(item.getImage());
			itemAggr.setItemId(itemId);
			itemAggr.setParamData(itemSpecification.getParamData());
			itemAggr.setPropertyName(itemProperty.getName());
			itemAggr.setPropertyValue(itemProperty.getValue());
			itemAggr.setSellPoint(item.getSellPoint());
			itemAggr.setStatus(item.getStatus());
			itemAggr.setTitle(item.getTitle());
			itemAggr.setUpdated(new Date());
			
			//缓存重建,将维度聚合数据更新到redis缓存主集群中,主集群通过主从复制，同步到从集群
			RebuildItemInfoAggrCacheQueue queue = RebuildItemInfoAggrCacheQueue.getInstance();
			queue.put(itemAggr);
			
			return itemAggr;
		}
		return itemInfoAggr;
	}
	
	/**
	 * 查询商品分类维度的聚合数据
	 * @param itemId
	 * @return
	 */
	@RequestMapping("/item/getItemCategory")
	@ResponseBody
	public ItemCategoryAggr getItemCategoryAggr(long itemId) {
		//从本地缓存中获取数据
		ItemCategoryAggr itemCategoryAggr = datalinkService.getItemCategoryAggrFromEhcache(itemId);
		
		//没有从本地缓存中获得数据，从redis缓存主集群中获取数据
		if(itemCategoryAggr == null) {
			itemCategoryAggr = datalinkService.getItemCategoryAggrFromRedis(itemId);
		}
		
		//没有从redis缓存主集群中获得数据，将调用商品分类维度服务的接口，获取这个维度的所有原子数据
		if(itemCategoryAggr == null) {
			//资源隔离，限流，缓存穿透的降级处理
			FindItemCategoryByIdCommand itemCategoryCommand = new FindItemCategoryByIdCommand(itemId, itemCategoryService);
			ItemCategory itemCategory = itemCategoryCommand.execute();
			
			ItemCategoryAggr result = new ItemCategoryAggr();
			result.setItemId(itemId);
			result.setName(itemCategory.getName());
			
			//缓存重建(异步执行，提高性能)
			RebuildItemCatAggrCacheQueue queue = RebuildItemCatAggrCacheQueue.getInstance();
			queue.put(result);
			
			return result;
		}
		
		return itemCategoryAggr;
	}
	
	/**
	 * 查询商品品牌维度数据
	 * @param itemId
	 * @return
	 */
	@RequestMapping("/item/getItemBrand")
	@ResponseBody
	public ItemBrandAggr getItemBrandAggr(long itemId) {
		//从本地缓存中查询
		ItemBrandAggr itemBrandAggr = datalinkService.getItemBrandAggrFromEhcache(itemId);
		
		// 没有从本地缓存获得，从redis主集群中查找
		if(itemBrandAggr == null) {
			itemBrandAggr = datalinkService.getItemBrandAggrFromRedis(itemId);
		}
		
		//没有从redis主集群获得数据，调用品牌服务维度接口查询
		if(itemBrandAggr == null) {
			//资源隔离，限流，缓存穿透的降级处理
			FindItemBrandByItemIdCommand itemBrandCommand = new FindItemBrandByItemIdCommand(itemId, itemBrandService);
			ItemBrand itemBrand = itemBrandCommand.execute();
			System.out.println("日志==============》查询到商品标题的原子数据："+itemBrand);
			
			ItemBrandAggr result = new ItemBrandAggr();
			result.setItemId(itemId);
			result.setBrand(itemBrand.getBrand());
			result.setEditTime(new Date());
			result.setBrandId(itemId);
			
			//缓存重建
			RebuildItemBrandAggrCacheQueue queue = RebuildItemBrandAggrCacheQueue.getInstance();
			queue.put(result);
			
			return result;
		}
		
		return itemBrandAggr;
	}
	
	/**
	 * 获取商品介绍维度的数据
	 * @param itemId
	 * @return
	 */
	@RequestMapping("/item/getItemDescription")
	@ResponseBody
	public ItemDescriptionAggr getItemDescriptionAggr(long itemId) {
		//从本地缓存中查找
		ItemDescriptionAggr itemDescriptionAggr = datalinkService.getItemDescriptionAggrFromEhcache(itemId);
		
		//没有从本地缓存中找到，从redis缓存主集群中找
		if(itemDescriptionAggr == null) {
			itemDescriptionAggr = datalinkService.getItemDescriptionAggrFromRedis(itemId);
		}
		
		//没有从redis缓存主集群找到，调用商品介绍服务
		if(itemDescriptionAggr == null) {
			//资源隔离，限流，缓存穿透的降级处理
			FindItemDescriptionByIdCommand itemDescriptionCommand = new FindItemDescriptionByIdCommand(itemId, itemDescriptionService);
			ItemDescription itemDescription = itemDescriptionCommand.execute();
			System.out.println("日志==============》调用商品分类服务获取原子数据:"+ itemDescription);
			
			ItemDescriptionAggr result = new ItemDescriptionAggr();
			result.setItemId(itemId);
			result.setItemDesc(itemDescription.getItemDesc());
			result.setUpdated(new Date());
			
			//缓存重建
			RebuildItemDescriptionAggrCacheQueue queue = RebuildItemDescriptionAggrCacheQueue.getInstance();
			queue.put(result);
			
			return result;
		}
		
		return itemDescriptionAggr;
	}

}
