package com.taotao.cart.app.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.cart.app.utils.CookieUtils;
import com.taotao.cart.app.utils.JsonUtils;
import com.taotao.cart.app.utils.TaotaoUtils;
import com.taotao.cart.model.Cart;
import com.taotao.cart.service.CartService;
import com.taotao.itemservice.model.Item;
import com.taotao.itemservice.service.ItemInfoService;
import com.taotao.user.model.User;
/**
 * 购物车客户端服务
 * @author Administrator
 *
 */
@Controller
public class CartAppController {
	
	@Autowired
	private CartService cartService;
	@Autowired
	private ItemInfoService itemInfoService;
	
	@Value("${CARTNAME}")
	private String CARTNAME;
	@Value("${COOKIEMAXAGE}")
	private Integer COOKIEMAXAGE;
	
	/**
	 * 查找购物车清单接口
	 * @return
	 */
	@RequestMapping("/cart")
	public String getCart(HttpServletRequest request, Model model) {
		User user = (User) request.getAttribute("user");
		List<Cart> carts = cartService.findCartsByUserId(user.getId());
		
		model.addAttribute("cartList", carts);
		return "cart";
	}
	
	/**
	 * 添加到购物车接口
	 * @Param itemId 【商品id】
	 */
	@RequestMapping("/cart/add")
	public String addCart(Long itemId, HttpServletRequest request, 
			HttpServletResponse response, Integer num) {
		User user = (User) request.getAttribute("user");
		//封装cart
		Cart cart = createCart(itemId, num);
		
		//将封装的购物车放到redis中
		cartService.addCart2Redis(cart, user.getId());
		System.out.println("日志============》将cart："+cart+"放入redis缓存中");
		
		return "cartSuccess";
	}
	
	/**
	 * 封装购物车cart类
	 */
	private Cart createCart(long itemId, Integer num) {
		//调用商品服务，获取商品数据
		Item item = itemInfoService.getItemInfoById(itemId);
		System.out.println("日志==============》调用商品服务获取商品数据:"+item);
		
		//封装cart
		Cart cart = new Cart();
		cart.setId(itemId);
		//取第一张图片
		String images = item.getImage();
		String[] imagesArray = images.split(",");
		cart.setImage(imagesArray[0]);
		cart.setNum(num);
		cart.setPrice(item.getPrice());
		cart.setTitle(item.getTitle());
		
		return cart;
	}
	
	/**
	 * 清空购物车
	 */
	@RequestMapping("/cart/clear-all")
	@ResponseBody
	public TaotaoUtils delteAllCart(HttpServletRequest request, HttpServletResponse response) {
		User user = (User) request.getAttribute("user");
		cartService.deleteAllCartByUserId(user.getId());
		System.out.println("日志==============》清空用户"+user.getUsername()+"购物车数据");
		return TaotaoUtils.ok();
	}
	
	/**
	 * 批量删除购物车中的宝贝
	 * @return
	 */
	@RequestMapping("/cart/deletebatch")
	@ResponseBody
	public TaotaoUtils deleteCarts(String itemIds, HttpServletRequest request,
			HttpServletResponse response) {
		User user = (User) request.getAttribute("user");
		cartService.deleteCartsByUserIdAndItemIds(itemIds, user.getId());
		System.out.println("日志=============》批量删除商品:"+itemIds);
		return TaotaoUtils.ok();
	}
	
	/**
	 * 删除购物车的宝贝
	 */
	@RequestMapping("/cart/delete/{cartId}")
	public String deleteCart(@PathVariable("cartId")long cartId, HttpServletRequest request) {
		try {
			User user = (User) request.getAttribute("user");
			System.out.println("日志=============》删除购物车id:"+cartId);
			cartService.deleteCartByUserIdAndItemId(user.getId(), cartId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "redirect:http://cart.e3mall.cn/cart";
	}
}
