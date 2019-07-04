package com.taotao.itemservice.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.taotao.itemservice.model.Item;

/**
 * 商品服务mapper
 * @author Administrator
 *
 */
public interface ItemInfoMapper {
	
	public void addItem(Item item);//插入商品信息
	
	public void deleteItem(long itemId);//删除商品信息
	
	public void updateItem(Item item);//更新商品信息
	
	public Item getItemInfoById(long itemId);//根据商品id查询商品信息
	
	public List<Item> findItemList(@Param("page")int page, @Param("pageSize")int pageSize);//分页
	
	public int count();
	
	public void updateItemStatusById(@Param("id")long id, @Param("status")int status);//更新商品状态
}
