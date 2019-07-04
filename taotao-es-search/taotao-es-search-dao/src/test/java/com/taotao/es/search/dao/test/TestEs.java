package com.taotao.es.search.dao.test;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.taotao.es.search.dao.EsSearchDao;
import com.taotao.es.search.dao.impl.EsSearchDaoImpl;
import com.taotao.es.search.model.Comment;
import com.taotao.es.search.model.EsSearchResultModel;

public class TestEs {
	public static void main(String[] args) throws Exception {
		EsSearchDao dao = new EsSearchDaoImpl();
		/*EsSearchResultModel model = new EsSearchResultModel();
		model.setCategoryName("水果");
		model.setId("563536");
		model.setImage("image");
		model.setPrice(10l);
		model.setSellPoint("好吃!!");
		model.setTitle("测试标题");
		model.setStatus(0);
		
		dao.addDocument(model, "taotao", "fresh");*/
		
		/*dao.updateItemStatus("item_index", "item_type", 0, 6);*/
		
		System.out.println(dao.queryString("火龙", 0, 10, 1, "item_index"));
		/*dao.updateDocument(model);*/
		
		/*Comment comment = new Comment();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String created = sdf.format(new Date());
		System.out.println("日志-----------》当前创建评论时间:"+created);
		
		comment.setCreated(created);
		comment.setId(UUID.randomUUID().toString());
		comment.setItemId(536563l);
		comment.setMessage("评论内容");
		comment.setUserId(6l);
		dao.addDocument(comment, "563536", "comment");*/
		
		System.out.println(dao.findCommentsOrderByDate("comment", "563536"));
	}
}
