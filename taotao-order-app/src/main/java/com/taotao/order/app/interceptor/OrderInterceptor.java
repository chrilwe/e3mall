package com.taotao.order.app.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.taotao.order.app.utils.CookieUtils;
import com.taotao.user.model.User;
import com.taotao.user.service.UserService;
/**
 * 订单登录拦截器
 * @author Administrator
 *
 */
public class OrderInterceptor implements HandlerInterceptor {
	
	@Value("${COOKIENAME}")
	private String COOKIENAME;
	@Value("${RETURNURL}")
	private String RETURNURL;
	
	@Autowired
	private UserService userService;

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		//获取token
		String token = CookieUtils.getCookieValue(request, COOKIENAME);
		if(token == null || token.equals("")) {
			System.out.println("日志=============》获取到的"+COOKIENAME+"为空，重定向到登录首页");
			response.sendRedirect(RETURNURL + request.getRequestURI());
			return false;
		}
		
		//调用会员系统服务，获取会员信息
		User user = userService.findUserByToken(token);
		if(user == null) {
			System.out.println("日志=============》该用户登录过期，重定向到登录首页");
			response.sendRedirect(RETURNURL + request.getRequestURI());
			return false;
		}
		
		//将user信息放到request域中
		request.setAttribute("user", user);
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// TODO Auto-generated method stub

	}

}
