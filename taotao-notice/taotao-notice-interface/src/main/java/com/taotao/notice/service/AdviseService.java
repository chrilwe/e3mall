package com.taotao.notice.service;

import com.taotao.notice.pojo.Advise;

public interface AdviseService {
	public void updateAdvise(Advise advise);
	public Advise findAdvise(long userId);
}
