package com.taotao.user.app.controller;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import com.alibaba.dubbo.config.support.Parameter;
import com.taotao.user.app.utils.CookieUtils;
import com.taotao.user.app.utils.JsonUtils;
import com.taotao.user.app.utils.TaotaoUtils;
import com.taotao.user.app.utils.UserAndRole;
import com.taotao.user.model.Role;
import com.taotao.user.model.User;
import com.taotao.user.service.RoleService;
import com.taotao.user.service.UserService;

/**
 * 会员app对外服务
 * @author Administrator
 *
 */
@Controller
public class UserController {
	
	@Autowired
	private UserService userService;
	@Autowired
	private RoleService roleService;
	
	@Autowired
	private JedisPool jedisPool;
	
	@Value("${expireSeconds}")
	private Integer expireSeconds;
	
	@Value("${TOKEN}")
	private String TOKEN;
	
	/**
	 * 会员登录页面
	 * @param returnUrl
	 * @return
	 */
	@RequestMapping("/user/loginPage")
	public String showLoginPage(@RequestParam(defaultValue="",name="returnUrl")String returnUrl,
			Model model) {
		model.addAttribute("returnUrl", returnUrl);
		return "login";
	}
	
	/**
	 * 会员登录接口
	 * @param username
	 * @return
	 */
	@RequestMapping("/user/login")
	@ResponseBody
	public TaotaoUtils login(String username,String password, HttpServletRequest request,
			HttpServletResponse response) {
		User user = userService.findUserByUserName(username);
		/**
		 * 登录验证
		 */
		boolean result = checkLogin(username, password, user);
		if(!result) {
			return TaotaoUtils.build(500, "用户账号或者密码错误");
		}
		
		/**
		 * 生成token为key，将user写入redis当中
		 */
		String token = saveUserToRedis(user);
		System.out.println("日志===========》生成token="+token+
				"，将user="+user+"保存到redis中,用户登录过期时间"+expireSeconds);
		
		/**
		 * 将token写入cookie中
		 */
		CookieUtils.setCookie(request, response, TOKEN, token);
		System.out.println("日志============》将token=" + token + "放到cookie中");
		
		return TaotaoUtils.ok();
	}
	
	/**
	 * 登录验证
	 */
	private boolean checkLogin(String username, String password, User user) {
		
		if(user == null) {
			System.out.println("日志===========》不存在该账号");
			return false;
		}
		
		if(!DigestUtils.md5DigestAsHex(password.getBytes()).equals(user.getPassword())) {
			System.out.println("日志============》密码错误");
			return false;
		}
		
		return true;
	}
	
	/**
	 * 在redis中缓存会员信息
	 */
	private String saveUserToRedis(User user) {
		String userStr = JsonUtils.objectToJson(user);
		Jedis jedis = jedisPool.getResource();
		
		String token = UUID.randomUUID().toString();
		jedis.set(token, userStr);
		jedis.expire(token, expireSeconds);
		
		return token;
	}
	
	/**
	 * 注册页面
	 */
	@RequestMapping("/user/registerPage")
	public String showRegisterPage() {
		
		return "register";
	}
	
	/**
	 * 检查注册账号是否被占用
	 */
	@RequestMapping("/user/check/{username}/1")
	@ResponseBody
	public TaotaoUtils checkUserName(@PathVariable("username")String username) {
		User user = userService.findUserByUserName(username);
		if(user != null) {
			System.out.println("日志=========》当前账号已经被注册");
			return TaotaoUtils.ok(false);
		}
		
		System.out.println("日志==========》当前账号可用");
		return TaotaoUtils.ok(true);
	}
	
	/**
	 * 检查手机号是否已经被注册
	 */
	@RequestMapping("/user/check/{phone}/2")
	@ResponseBody
	public TaotaoUtils checkPhone(@PathVariable("phone")String phone) {
		User user = userService.findUserByPone(phone);
		if(user != null) {
			System.out.println("日志========》当前手机号已经被注册");
			return TaotaoUtils.ok(false);
		}
		
		System.out.println("日志==========》当前手机号可用");
		return TaotaoUtils.ok(true);
	}
	
	/**
	 * 用户注册
	 */
	@RequestMapping("/user/register")
	@ResponseBody
	public TaotaoUtils registerUser(User user) {
		//对密码进行MD5加密
		String password = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
		user.setPassword(password);
		
		user.setCreated(new Date());
		user.setUpdated(new Date());
		
		//将注册用户信息插入数据库中
		userService.addUser(user);
		
		return TaotaoUtils.ok();
	}
	
	/**
	 * 跨域根据token获取user信息
	 * @param token
	 * @return
	 */
	@RequestMapping("/user/token/{token}")
	@ResponseBody
	public String getUserByToken(@PathVariable("token")String token, String callback) {
		User user = userService.findUserByToken(token);
		if(callback != null && !callback.equals("")) {
			if(user == null) {
				return JsonUtils.objectToJson(TaotaoUtils.build(500, "登录过期"));
			} else {
				return callback + "(" + JsonUtils.objectToJson(TaotaoUtils.ok(user)) + ");";
			}
		} 
		
		return JsonUtils.objectToJson(user);
	}
	
	/**
	 * 用户注销
	 * @return
	 */
	@RequestMapping("/user/logout")
	@ResponseBody
	public TaotaoUtils logout(HttpServletRequest request, HttpServletResponse response, String callback) {
		try {
			//根据token删除redis中的用户信息
			String token = CookieUtils.getCookieValue(request, TOKEN);
			userService.deleteUserByToken(token);
			System.out.println("日志=============》token:"+token + "注销redis中的会员信息");
			
			//删除cookie中的token
			CookieUtils.deleteCookie(request, response, TOKEN);
			System.out.println("日志=============》注销cookie中的token:"+token);
		} catch (Exception e) {
			System.out.println("异常=============》用户注销异常:"+e);
			return TaotaoUtils.build(500, "用户注销异常");
		}
		return TaotaoUtils.ok();
	}
	
	/**
	 * 后台跨域获取用户名称和角色
	 * @param token
	 * @param callback
	 * @return
	 */
	@RequestMapping("/user-role/token/{token}")
	@ResponseBody
	public String getRoleAndUser(@PathVariable("token")String token, String callback) {
		if(token != null && !token.equals("")) {
			User user = userService.findUserByToken(token);
			if(callback != null && !callback.equals("")) {
				if(user != null) {
					List<Role> roles = roleService.findRoleByUserId(String.valueOf(user.getId()));
					String rolename = "";
					for (int i = 0; i < roles.size(); i++) {
						if(i == roles.size() - 1) {
							rolename += roles.get(i).getName();
						} else {
							rolename += roles.get(i).getName() + ",";
						}
					}
					UserAndRole userAndRole = new UserAndRole();
					userAndRole.setRolename(rolename);
					userAndRole.setUsername(user.getUsername());
					
					return callback + "(" + JsonUtils.objectToJson(TaotaoUtils.ok(userAndRole)) + ");";
				}
			}
		}
		
		return null;
	}
}
