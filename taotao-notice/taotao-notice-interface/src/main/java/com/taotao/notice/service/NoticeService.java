package com.taotao.notice.service;

import com.taotao.notice.pojo.Notice;

public interface NoticeService {
	public void updateNotice(Notice notice);
	public Notice findNotice();
}
