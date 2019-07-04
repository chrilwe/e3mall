package com.taotao.user.service;

import java.util.List;

import com.taotao.user.model.Function;
import com.taotao.user.model.UserPageBean;

public interface FunctionService {
	//根据用户id查询用户权限
	public List<Function> findFunctionByUserId(String userId);
	
	//根据用户id和权限的uri查询权限
	public Function findFunctionByUserIdAndURI(String userId, String uri);
		
	//权限分页查询
	public UserPageBean queryFunctions(int page, int size);
		
	//查询是菜单的权限
	public List<Function> findFunctionsWhereIsMenu(int isMenu);
		
	//添加权限信息
	public void addFunction(Function function);
	
	//批量删除权限接口
	public void deleteFunctions(String ids);
	
	//删除权限
	public void deleteFunction(String functionId);
	
	//查询所有权限
	public List<Function> findFunctions();
}
