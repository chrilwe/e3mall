package com.taotao.user.service;

import java.util.List;

import com.taotao.user.model.Function;
import com.taotao.user.model.Role;
import com.taotao.user.model.User;
import com.taotao.user.model.UserPageBean;

/**
 * 淘淘商城会员系统
 * @author Administrator
 *
 */
public interface UserService {
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
	
	//根据token查询会员
	public User findUserByToken(String token);
	
	//根据token删除会员信息
	public void deleteUserByToken(String token);
	
	//用户分页查询
	public UserPageBean queryUsers(int page, int rows);
	
	//批量删除用户
	public void deleteUsers(String ids);
	
	//删除用户
	public void deleteUser(String userId);
	
	public void updatePassword(String password, long userId);
}
