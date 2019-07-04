package com.taotao.notice.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taotao.notice.dao.AdviseDao;
import com.taotao.notice.pojo.Advise;
import com.taotao.notice.service.AdviseService;
/**
 * 用户反馈服务
 * @author Administrator
 *
 */
@Service
public class AdviseServiceImpl implements AdviseService {
	
	@Autowired
	private AdviseDao adviseDao;

	@Override
	public void updateAdvise(Advise advise) {
		adviseDao.updateAdvise(advise);
	}

	@Override
	public Advise findAdvise(long userId) {
		
		return adviseDao.findAdvise(userId);
	}

}
