package com.taotao.administration.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class PageAppController {
	
	@RequestMapping("/{url}")
	public String showManagerPage(@PathVariable("url")String url) {
		
		return url;
	}
}
