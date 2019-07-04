package com.taotao.cart.app.interceptor;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.taotao.cart.app.utils.CookieUtils;
import com.taotao.cart.app.utils.JsonUtils;
import com.taotao.cart.model.Cart;
import com.taotao.cart.service.CartService;
import com.taotao.user.model.User;
import com.taotao.user.service.UserService;
/**
 * 登录拦截器
 * @author Administrator
 *
 */
public class LoginInterceptor implements HandlerInterceptor {
	
	@Autowired
	private UserService userService;
	@Autowired
	private CartService cartService;
	
	@Value("${COOKIENAME}")
	private String COOKIENAME;
	@Value("${CARTNAME}")
	private String CARTNAME;
	@Value("${RETURNURL}")
	private String RETURNURL;

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		//从cookie中获取到token
		String token = CookieUtils.getCookieValue(request, COOKIENAME);
		if(token == null) {
			System.out.println("日志============》TOKEN没有获取到，用户没有登录，拒绝访问购物车服务");
			//重定向到登录页面
			response.sendRedirect(RETURNURL);
			return false;
		}
		
		//调用会员信通服务，根据token查询user信息
		User user = userService.findUserByToken(token);
		if(user == null) {
			System.out.println("日志============》用户登录过期");
			//重定向到登录页面
			response.sendRedirect(RETURNURL);
			return false;
		}
		
		System.out.println("日志============》用户"+user.getUsername()+"登录");
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
