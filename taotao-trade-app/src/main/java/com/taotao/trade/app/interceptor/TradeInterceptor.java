package com.taotao.trade.app.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.taotao.trade.app.utils.CookieUtils;
import com.taotao.user.model.User;
import com.taotao.user.service.UserService;
/**
 * 登录拦截器
 * @author Administrator
 *
 */
public class TradeInterceptor implements HandlerInterceptor {
	
	@Value("${REDIRECT_URL}")
	private String REDIRECT_URL;

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		String token = CookieUtils.getCookieValue(request, "TAOTAO_TOKEN_");
		if(token == null) {
			response.sendRedirect(REDIRECT_URL);
			return false;
		}
		//调用会员系统服务，查询会员信息
		UserService userService = ContextLoader.getCurrentWebApplicationContext().getBean(UserService.class);
		User user = userService.findUserByToken(token);
		
		if(user == null) {
			response.sendRedirect(REDIRECT_URL);
			return false;
		}
		
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
