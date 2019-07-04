package com.taotao.point.app.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.taotao.point.app.constant.PointContant;
import com.taotao.point.app.utils.CookieUtils;
import com.taotao.user.model.User;
import com.taotao.user.service.UserService;
/**
 *  用户登录拦截器
 * @author Administrator
 *
 */
public class UserLoginInterceptor implements HandlerInterceptor {
	
	@Autowired
	private UserService userService;
	
	@Value("${LOGIN_URL}")
	private String LOGIN_URL;

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		String token = CookieUtils.getCookieValue(request, PointContant.COOKIE_NAME);
		
		if(token == null || token.equals("")) {
			response.sendRedirect(LOGIN_URL);
			return false;
		}
		
		User u = userService.findUserByToken(token);
		if(u == null) {
			response.sendRedirect(LOGIN_URL);
			return false;
		}
		
		request.setAttribute("u", u);
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
