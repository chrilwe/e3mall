package com.taotao.user.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import com.taotao.user.mapper.UserMapper;
import com.taotao.user.model.User;
import com.taotao.user.model.UserPageBean;
import com.taotao.user.service.UserService;
import com.taotao.user.service.utils.JsonUtils;

/**
 * 淘淘商城会员系统服务
 * @author Administrator
 *
 */
@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserMapper userMapper;
	@Autowired
	private JedisPool jedisPool;
	
	/**
	 * 添加会员记录
	 */
	@Override
	@Transactional
	public void addUser(User user) {
		userMapper.addUser(user);
	}
	
	/**
	 * 删除会员记录
	 */
	@Override
	@Transactional
	public void deleteUser(long userId) {
		userMapper.deleteUser(userId);
	}
	
	/**
	 * 更新会员记录
	 */
	@Override
	public void updateUser(User user) {
		userMapper.updateUser(user);
	}
	
	/**
	 * 根据会员id查询
	 */
	@Override
	public User findUserById(long userId) {
		
		return userMapper.findUserById(userId);
	}
	
	/**
	 * 根据会员账号查询
	 */
	@Override
	public User findUserByUserName(String username) {
		
		return userMapper.findUserByUserName(username);
	}
	
	/**
	 * 根据会员手机号查询
	 */
	@Override
	public User findUserByPone(String phone) {
		
		return userMapper.findUserByPone(phone);
	}
	
	/**
	 * 根据token查找会员
	 */
	@Override
	public User findUserByToken(String token) {
		Jedis jedis = jedisPool.getResource();
		String userStr = jedis.get(token);
		if(userStr == null) {
			return null;
		}
		return JsonUtils.jsonToPojo(userStr, User.class);
	}
	
	/**
	 * 根据token删除会员
	 */
	@Override
	public void deleteUserByToken(String token) {
		Jedis jedis = jedisPool.getResource();
		jedis.del(token);
	}
	
	/**
	 * 用户分页查询
	 */
	@Override
	public UserPageBean queryUsers(int page, int rows) {
		List<User> users = userMapper.queryUsers(page, rows);
		int total = userMapper.count();
		
		UserPageBean userPageBean = new UserPageBean();
		userPageBean.setTotal(total);
		userPageBean.setRows(users);
		return userPageBean;
	}

	@Override
	public void deleteUsers(String ids) {
		String[] idsArr = ids.split(",");
		for (String userId : idsArr) {
			deleteUser(userId);
		}
	}

	@Override
	@Transactional
	public void deleteUser(String userId) {
		userMapper.deleteUser(Long.parseLong(userId));
	}

	@Override
	@Transactional
	public void updatePassword(String password, long userId) {
		userMapper.updatePassword(userId, password);
	}
	
}
