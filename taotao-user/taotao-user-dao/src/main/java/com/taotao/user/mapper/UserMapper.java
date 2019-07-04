package com.taotao.user.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.taotao.user.model.User;

public interface UserMapper {
	//添加会员记录
	public void addUser(User user);
	
	//删除会员记录
	public void deleteUser(long userId);
	
	//更新会员记录
	public void updateUser(User user);
	
	//根据id查询会员记录
	public User findUserById(long userId);
	
	//根据会员账号查询记录
	public User findUserByUserName(String username);
	
	//根据会员手机号查询记录
	public User findUserByPone(String phone);
	
	//用户分页查询
	public List<User> queryUsers(@Param("page")int page, @Param("size")int size);
	
	public int count();
	
	public void updatePassword(@Param("userId")long userId, @Param("password")String password);
}
