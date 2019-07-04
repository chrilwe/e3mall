package com.taotao.cart.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taotao.cart.dao.CartDao;
import com.taotao.cart.model.Cart;
import com.taotao.cart.service.CartService;
/**
 * 购物车信息服务系统
 * @author Administrator
 *
 */
@Service
public class CartServiceImpl implements CartService {
	
	@Autowired
	private CartDao cartDao;
	
	/**
	 * 将购物车添加到redis中
	 */
	@Override
	public void addCart2Redis(Cart cart, long userId) {
		cartDao.addCart2Redis(cart, userId);
	}
	
	/**
	 * 根据用户id查找该用户的购物车
	 */
	@Override
	public List<Cart> findCartsByUserId(long userId) {
		
		return cartDao.findCartFromRedis(userId);
	}
	
	/**
	 * 清空购物车
	 */
	@Override
	public void deleteAllCartByUserId(long userId) {
		cartDao.deleteAllCartByuserId(userId);
	}
	
	/**
	 * 删除购物车中的宝贝
	 */
	@Override
	public void deleteCartByUserIdAndItemId(long userId, long itemId) {
		cartDao.deleteCartFromRedis(userId, itemId);
	}
	
	/**
	 * 批量删除购物车中的宝贝
	 */
	@Override
	public void deleteCartsByUserIdAndItemIds(String itemIds, long userId) {
		String[] itemIdsArray = itemIds.split(",");
		for (String idStr : itemIdsArray) {
			cartDao.deleteCartFromRedis(userId, Long.parseLong(idStr));
		}
	}

}
