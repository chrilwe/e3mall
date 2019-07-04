package com.taotao.administration.controller;

import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.administration.utils.TaotaoUtils;
import com.taotao.user.model.Function;
import com.taotao.user.model.User;
import com.taotao.user.model.UserPageBean;
import com.taotao.user.service.FunctionService;


/**
 * 用户权限管理
 * @author Administrator
 *
 */
@Controller
public class FunctionAppController {
	
	@Autowired
	private FunctionService functionService;
	
	/**
	 * 根据当前用户权限展示菜单栏选项
	 * @return
	 */
	@RequestMapping("/manager/menu")
	@ResponseBody
	public List<Function> showMenu(HttpServletRequest request) {
		//从request域中获取user信息
		User user = (User) request.getSession().getAttribute("usr");
		
		//调用权限信息服务，获取菜单栏功能信息
		System.out.println("user="+user);
		List<Function> functions = functionService.findFunctionByUserId(String.valueOf(user.getId()));
		return functions;
	}
	
	/**
	 * 权限分页查询
	 * @param page 当前页
	 * @param rows 页面大小
	 * @return
	 */
	@RequestMapping("/manager/getFunctionList")
	@ResponseBody
	public UserPageBean getFunctionList(int page, int rows) {
		//调用会员服务的权限系统，查询权限
		UserPageBean userPageBean = functionService.queryFunctions((page - 1) * rows, rows);
		return userPageBean;
	}
	
	/**
	 * 查询所有是菜单的权限
	 * @return
	 */
	@RequestMapping("/manager/parentId/list")
	@ResponseBody
	public List<Function> findIsMenuFunction() {
		
		return functionService.findFunctionsWhereIsMenu(1);
	}
	
	/**
	 * 添加权限信息
	 * @param function
	 * @return
	 */
	@RequestMapping("/manager/function/add")
	@ResponseBody
	public TaotaoUtils addFunction(Function function) {
		function.setId(UUID.randomUUID().toString());
		// 调用权限添加服务
		try {
			functionService.addFunction(function);
		} catch (Exception e) {
			e.printStackTrace();
			return TaotaoUtils.build(500, "权限添加失败");
		}
		return TaotaoUtils.ok();
	}
	
	/**
	 * 批量删除权限
	 * @param ids
	 * @return
	 */
	@RequestMapping("/manager/function/delete")
	@ResponseBody
	public TaotaoUtils deleteFunctions(String ids) {
		functionService.deleteFunctions(ids);
		return TaotaoUtils.ok();
	}
	
}
