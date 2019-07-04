package com.taotao.administration.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.administration.utils.CookieUtils;
import com.taotao.administration.utils.TaotaoUtils;
import com.taotao.user.model.User;
import com.taotao.user.model.UserPageBean;
import com.taotao.user.service.UserService;
/**
 * 用户管理
 * @author Administrator
 *
 */
@Controller
public class UserAppController {
	
	@Autowired
	private UserService userService;
	
	@Value("${COOKIE_NAME}")
	private String COOKIE_NAME;
	
	/**
	 * 用户分页查询
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping("/manager/getUserList")
	@ResponseBody
	public UserPageBean getUserList(int page, int rows) {
		
		return userService.queryUsers((page - 1) * rows, rows);
	}
	
	/**
	 * 添加管理员
	 * @return
	 */
	@RequestMapping("/manager/user/add")
	@ResponseBody
	public TaotaoUtils addUser(User user) {
		try {
			userService.addUser(user);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return TaotaoUtils.ok();
	}
	
	/**
	 * 批量删除用户
	 * @param ids
	 * @return
	 */
	@RequestMapping("/manager/user/delete")
	@ResponseBody
	public TaotaoUtils deleteUsers(String ids) {
		userService.deleteUsers(ids);
		return TaotaoUtils.ok();
	}
	
	/**
	 * 注销
	 */
	@RequestMapping("/manager/logout")
	@ResponseBody
	public TaotaoUtils logout(HttpServletRequest request, HttpServletResponse response) {
		request.getSession().removeAttribute("usr");
		String token = CookieUtils.getCookieValue(request, COOKIE_NAME);
		userService.deleteUserByToken(token);
		CookieUtils.deleteCookie(request, response, COOKIE_NAME);
		
		return TaotaoUtils.ok();
	}
	
	/**
	 * 修改用户密码
	 */
	@RequestMapping("/manager/password/update")
	@ResponseBody
	public TaotaoUtils changePassword(String oldPassword, String newPassword1, 
			String newPassword2, HttpServletRequest request) {
		//查询旧密码是否正确
		oldPassword = DigestUtils.md5DigestAsHex(oldPassword.getBytes());
		User user = (User) request.getSession().getAttribute("usr");
		User u = userService.findUserById(user.getId());
		if(!oldPassword.equals(u.getPassword())) {
			return TaotaoUtils.build(500, "输入的旧密码有误");
		}
		
		//判断两次输入密码是否一致
		if(!newPassword1.equals(newPassword2)){
			return TaotaoUtils.build(500, "输入的密码不一致");
		}
		
		//更新密码
		userService.updatePassword(DigestUtils.md5DigestAsHex(newPassword1.getBytes()), user.getId());
		
		return TaotaoUtils.ok();
	}
}
