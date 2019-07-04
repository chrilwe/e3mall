package com.taotao.user.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.taotao.user.model.Function;

public interface FunctionMapper {
	public void add(Function function);
	public void delete(String functionId);
	public void update(Function function);
	public Function findById(@Param("functionId")String functionId, @Param("isMenu")Integer isMeun);
	public Function findByUri(String uri);
	public List<Function> queryFunctions(@Param("page")int page, 
			@Param("size")int size);//分页查询权限
	public int count();
	public List<Function> findFunctionsWhereIsMenu(int isMenu);//查询是菜单的权限
	public List<Function> findFunctions();
}
