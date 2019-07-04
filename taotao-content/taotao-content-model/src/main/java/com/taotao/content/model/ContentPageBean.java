package com.taotao.content.model;

import java.io.Serializable;
import java.util.List;

/**
 * 分页结果的封装类
 * @author Administrator
 *
 */
public class ContentPageBean implements Serializable {
	
	private static final long serialVersionUID = 589311028815219540L;

	private int total;
	
	private List rows;

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public List getRows() {
		return rows;
	}

	public void setRows(List rows) {
		this.rows = rows;
	}
	
	
}
