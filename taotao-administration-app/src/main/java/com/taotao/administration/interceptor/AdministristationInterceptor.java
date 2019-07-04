package com.taotao.administration.interceptor;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.taotao.administration.utils.CookieUtils;
import com.taotao.user.model.Function;
import com.taotao.user.model.User;
import com.taotao.user.service.FunctionService;
import com.taotao.user.service.UserService;

public class AdministristationInterceptor implements HandlerInterceptor {
	
	@Value("${COOKIE_NAME}")
	private String COOKIE_NAME;
	@Value("${REDIRECT_URL}")
	private String REDIRECT_URL;
	@Value("${ERROR_URL}")
	private String ERROR_URL;
	
	@Autowired
	private UserService userService;
	@Autowired
	private FunctionService functionService;
	
	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		String token = CookieUtils.getCookieValue(request, COOKIE_NAME);
		if(token == null || token.equals("")) {
			response.sendRedirect(REDIRECT_URL);
			return false;
		}
		
		User user = userService.findUserByToken(token);
		if(user == null) {
			response.sendRedirect(REDIRECT_URL);
			return false;
		}
		
		request.getSession().setAttribute("usr", user);
		
		//用户授权
		/*String currentURI = request.getRequestURI();
		System.out.println("日志------------》当前URI="+currentURI);
		Boolean result = addFunction2User(user.getId(), currentURI);
		
		if(!result) {
			response.sendRedirect(ERROR_URL);
			return false;
		}*/
		
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
	
	/**
	 * 用户授予权限
	 * @return
	 */
	private Boolean addFunction2User(long userId, String currentURI) {
		//查询所有权限的url地址list
		List<Function> functions = functionService.findFunctions();
		System.out.println("日志==================》查询所有的权限:"+functions);
		
		//判断当前请求uri是否存在于list，存在的话，需要权限的校验
		for (Function function : functions) {
			if(function.getPage().equals(currentURI)) {
				//判断该用户是否有此项权限
				Function result = functionService.findFunctionByUserIdAndURI(String.valueOf(userId), currentURI);
				if(result == null) {
					System.out.println("日志------------》用户userId="+userId+"没有访问URI="+currentURI+"权限");
					return false;
				} else {
					break;
				}
			} else {
				continue;
			}
		}
		
		return true;
	}

}
