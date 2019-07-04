package com.taotao.point.account.mapper;

import java.util.Date;

import org.apache.ibatis.annotations.Param;

import com.taotao.point.account.model.PointAccount;

public interface PointAccountMapper {
	/**
	 * 添加记录
	 * @param pointAccount
	 */
	public void addPointAccount(PointAccount pointAccount);
	
	/**
	 * 删除记录
	 */
	public void deletePointAccountById(String id);
	
	/**
	 * 更新记录
	 */
	public void updatePointAccount(PointAccount pointAccount);
	
	/**
	 * 根据userNo更新积分余额
	 */
	public void updatePointAccountBalanceEditTimeByUserNo(@Param("banlance")Integer balance,
			@Param("editTime")Date editTime,@Param("userNo")String userNo);
	
	/**
	 * 根据id查询
	 */
	public PointAccount findPointAccountById(String id);
	
	/**
	 * 根据userNo查询记录
	 */
	public PointAccount findPointAccountByUserNo(String userNo);
}
