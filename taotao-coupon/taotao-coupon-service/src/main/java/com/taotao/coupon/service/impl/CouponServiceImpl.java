package com.taotao.coupon.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.taotao.coupon.mapper.CouponMapper;
import com.taotao.coupon.model.Coupon;
import com.taotao.coupon.service.CouponService;
/**
 * 红包系统服务
 * @author Administrator
 *
 */
@Service
public class CouponServiceImpl implements CouponService {
	
	@Autowired
	private CouponMapper couponMapper;
	
	/**
	 * 创建红包
	 */
	@Override
	@Transactional
	public void addCoupon(Coupon coupon) {
		
	}
	
	/**
	 * 删除红包
	 */
	@Override
	@Transactional
	public void deleteCouponById(int id) {
		// TODO Auto-generated method stub

	}
	
	/**
	 * 更新红包
	 */
	@Override
	@Transactional
	public void updateCoupon(Coupon coupon) {
		// TODO Auto-generated method stub

	}
	
	/**
	 * 查找该用户的有效的红包
	 * @param userId 用户id
	 * @param status 红包状态
	 */
	@Override
	public List<Coupon> findCouponsByUserId(long userId, String status) {
		// TODO Auto-generated method stub
		return null;
	}

}
