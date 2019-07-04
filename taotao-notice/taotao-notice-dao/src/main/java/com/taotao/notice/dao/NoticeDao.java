package com.taotao.notice.dao;

import com.taotao.notice.pojo.Notice;

public interface NoticeDao {
	public void addNotice(Notice notice);//添加公告信息
	public void updateNotice(Notice notice);//更新公告信息
	public Notice findNotice();//查询公告信息
}
