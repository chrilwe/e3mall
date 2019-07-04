package com.taotao.comment.app.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.comment.app.utils.TaotaoUtils;
import com.taotao.es.search.model.Comment;
import com.taotao.es.search.model.EsSearchResultModel;
import com.taotao.es.search.service.EsSearchService;
/**
 * 评论系统服务
 * @author Administrator
 *
 */
@Controller
public class CommentAppController {
	
	@Autowired
	private EsSearchService esSearchService;
	
	/**
	 * 评论商品
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("/comment/add")
	@ResponseBody
	public TaotaoUtils addComment(HttpServletRequest request, Comment comment) {
		
		//调用es搜索库作为持久层
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String created = sdf.format(new Date());
			System.out.println("日志-----------》当前创建评论时间:"+created);
			
			comment.setCreated(created);
			comment.setId(UUID.randomUUID().toString());
			esSearchService.add(comment, String.valueOf(comment.getItemId()), "comment");
		} catch (Exception e) {
			e.printStackTrace();
			return TaotaoUtils.build(500, "评论商品发生未知异常");
		}
		
		return TaotaoUtils.ok();
	}
	
	
	/**
	 * 分页查询评论该商品的评论信息
	 * @throws Exception 
	 */
	@RequestMapping("/comment/query")
	@ResponseBody
	public List<Comment> findComments(long itemId) throws Exception {
		
		return esSearchService.findCommentsOrderByDate("comment", String.valueOf(itemId));
	}
}
