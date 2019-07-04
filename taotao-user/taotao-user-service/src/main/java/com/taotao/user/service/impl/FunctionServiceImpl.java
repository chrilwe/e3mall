package com.taotao.user.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.taotao.user.mapper.FunctionMapper;
import com.taotao.user.mapper.RoleFunctionMapper;
import com.taotao.user.mapper.UserRoleMapper;
import com.taotao.user.model.Function;
import com.taotao.user.model.UserPageBean;
import com.taotao.user.service.FunctionService;
/**
 * 权限服务
 * @author Administrator
 *
 */
@Service
public class FunctionServiceImpl implements FunctionService {
	
	@Autowired
	private FunctionMapper functionMapper;
	@Autowired
	private UserRoleMapper userRoleMapper;
	@Autowired
	private RoleFunctionMapper roleFunctionMapper;

	/**
	 * 根据用户id查询用户权限
	 */
	@Override
	public List<Function> findFunctionByUserId(String userId) {
		Set<String> set = new HashSet<String>();
		List<Function> functions = new ArrayList<Function>();
		//根据用户id查询用户的角色id
		List<String> roleIds = userRoleMapper.findRoleIdsByUserId(userId);
		System.out.println("roleIds="+roleIds);
		
		//根据用户角色id，查询用户权限id(需要去重)
		for (String roleId : roleIds) {
			List<String> functionIds = roleFunctionMapper.findFunctionIdsByRoleId(roleId);
			System.out.println("functionIds="+functionIds);
			for (String functionId : functionIds) {
				set.add(functionId);
			}
		}
		
		//遍历去重后的set,根据functionId查询菜单
		for(String functionId : set) {
			Function function = functionMapper.findById(functionId, 1);//0：表示不是菜单选项，1：表示是菜单选项
			functions.add(function);
		}
		return functions;
	}
	
	/**
	 * 根据用户id和权限的uri查询权限
	 */
	@Override
	public Function findFunctionByUserIdAndURI(String userId, String uri) {
		Set<String> set = new HashSet<String>();
		//根据用户id查询用户的角色id
		List<String> roleIds = userRoleMapper.findRoleIdsByUserId(userId);
		System.out.println("roleIds="+roleIds);
		
		//根据用户角色id，查询用户权限id(需要去重)
		for (String roleId : roleIds) {
			List<String> functionIds = roleFunctionMapper.findFunctionIdsByRoleId(roleId);
			System.out.println("functionIds="+functionIds);
			for (String functionId : functionIds) {
				set.add(functionId);
			}
		}
		
		//遍历去重后的set,根据functionId查询菜单
		for(String functionId : set) {
			Function function = functionMapper.findByUri(uri);
			if(function != null) {
				return function;
			}
		}
		return null;
	}
	
	/**
	 * 是菜单的权限分页查询
	 */
	@Override
	public UserPageBean queryFunctions(int page, int size) {
		List<Function> functions = functionMapper.queryFunctions(page, size);
		int total = functionMapper.count();
		
		UserPageBean userPageBean = new UserPageBean();
		userPageBean.setRows(functions);
		userPageBean.setTotal(total);
		return userPageBean;
	}
	
	/**
	 * 查询是菜单的权限
	 */
	@Override
	public List<Function> findFunctionsWhereIsMenu(int isMenu) {
		
		return functionMapper.findFunctionsWhereIsMenu(isMenu);
	}
	
	/**
	 * 添加权限
	 */
	@Override
	@Transactional
	public void addFunction(Function function) {
		functionMapper.add(function);
	}
	
	/**
	 * 批量删除权限接口
	 */
	@Override
	public void deleteFunctions(String ids) {
		//删除权限
		//删除权限关联角色
		//删除角色关联的用户
		String[] idsArray = ids.split(",");
		for (String functionId : idsArray) {
			deleteFunction(functionId);
		}
	}
	
	/**
	 * 删除权限
	 */
	@Override
	@Transactional
	public void deleteFunction(String functionId) {
		functionMapper.delete(functionId);
		roleFunctionMapper.deleteByFunctionId(functionId);
		List<String> roleIds = roleFunctionMapper.findRoleIdsByFunctionId(functionId);
		for (String roleId : roleIds) {
			userRoleMapper.deleteByRoleId(roleId);
		}
	}
	
	/**
	 * 查询所有权限
	 */
	@Override
	public List<Function> findFunctions() {
		
		return functionMapper.findFunctions();
	}

}
