package com.taotao.notice.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taotao.notice.dao.NoticeDao;
import com.taotao.notice.pojo.Notice;
import com.taotao.notice.service.NoticeService;
/**
 * 公告消息服务
 * @author Administrator
 *
 */
@Service
public class NoticeServiceImpl implements NoticeService {
	
	@Autowired
	private NoticeDao noticeDao;

	@Override
	public void updateNotice(Notice notice) {
		noticeDao.updateNotice(notice);
	}

	@Override
	public Notice findNotice() {
		
		return noticeDao.findNotice();
	}

}
