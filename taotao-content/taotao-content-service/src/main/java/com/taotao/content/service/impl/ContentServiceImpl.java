package com.taotao.content.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import com.taotao.content.mapper.ContentMapper;
import com.taotao.content.model.Content;
import com.taotao.content.model.ContentPageBean;
import com.taotao.content.service.ContentService;
import com.taotao.content.utils.JsonUtils;
/**
 * 广告内容服务
 * @author Administrator
 *
 */
@Service
public class ContentServiceImpl implements ContentService {
	
	@Autowired
	private ContentMapper contentMapper;
	@Autowired
	private JedisPool jedisPool;
	
	/**
	 * 添加广告记录
	 */
	@Override
	@Transactional
	public void addContent(Content content) {
		contentMapper.addContent(content);
		Jedis jedis = jedisPool.getResource();
		jedis.set("content_" + content.getId(), JsonUtils.objectToJson(content));
	}
	
	/**
	 * 删除广告记录
	 */
	@Override
	@Transactional
	public void deleteContent(long id) {
		contentMapper.deleteContent(id);
		Jedis jedis = jedisPool.getResource();
		jedis.del("content_" + id);
	}
	
	/**
	 * 更新广告记录
	 */
	@Override
	@Transactional
	public void updateContent(Content content) {
		Jedis jedis = jedisPool.getResource();
		jedis.del("content_" + content.getId());
		contentMapper.updateContent(content);
	}
	
	/**
	 * 根据id查询广告记录
	 */
	@Override
	public Content findContentById(long id) {
		
		return contentMapper.findContentById(id);
	}
	
	/**
	 * 分页查询广告内容
	 */
	@Override
	public ContentPageBean queryContents(int from, int to) {
		List<Content> contents = contentMapper.findContents(from, to);
		int total = contentMapper.count();
		ContentPageBean pageBean = new ContentPageBean();
		pageBean.setTotal(total);
		pageBean.setRows(contents);
		return pageBean;
	}
	
	/**
	 * 批量删除
	 */
	@Override
	@Transactional
	public void deleteContents(String ids) {
		String[] split = ids.split(",");
		for (String id : split) {
			contentMapper.deleteContent(Long.parseLong(id));
		}
	}
	
	/**
	 * 根据cid查询
	 */
	@Override
	public List<Content> findContentsByCid(long contentCategoryId) {
		
		return contentMapper.findContentsByCId(contentCategoryId);
	}

}
