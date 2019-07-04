package com.taotao.point.app.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.point.account.model.PointAccount;
import com.taotao.point.account.model.PointPageBean;
import com.taotao.point.account.service.PointAccountService;
import com.taotao.user.model.User;

/**
 * 积分服务app
 * @author Administrator
 *
 */
@Controller
public class PointAppController {
	
	@Autowired
	private PointAccountService pointAccountService;
	
	@RequestMapping("/point/myPoint")
	public String myPointPage(Model model, HttpServletRequest request) {
		User user = (User) request.getAttribute("u");
		
		PointAccount pointAccount = pointAccountService.findPointAmountByUserId(String.valueOf(user.getId()));
		
		model.addAttribute("pointAmount", pointAccount.getBalance());
		model.addAttribute("user", user);
		return "index";
	}
	
	/**
	 * 用户积分呢记录明细查询
	 * @return
	 */
	@RequestMapping("/point/getPointList")
	@ResponseBody
	public PointPageBean getUserPointList(Long userId, int page, int rows) {
		if(userId != null) {
			PointPageBean bean = pointAccountService.queryPointHistory((page - 1)*rows, rows, String.valueOf(userId));
			return bean;
		}
		return null;
	}
	
}
