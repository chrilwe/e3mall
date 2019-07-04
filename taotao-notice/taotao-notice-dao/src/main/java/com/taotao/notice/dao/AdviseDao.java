package com.taotao.notice.dao;

import com.taotao.notice.pojo.Advise;

public interface AdviseDao {
	public void updateAdvise(Advise advise);;
	public Advise findAdvise(long userId);
}
