package com.taotao.point.account.model;

import java.io.Serializable;
import java.util.List;

public class PointPageBean implements Serializable {
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
	@Override
	public String toString() {
		return "PointPageBean [total=" + total + ", rows=" + rows + "]";
	}
	
}
