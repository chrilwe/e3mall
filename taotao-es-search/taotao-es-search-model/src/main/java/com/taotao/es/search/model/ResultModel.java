package com.taotao.es.search.model;

import java.io.Serializable;
import java.util.List;

public class ResultModel implements Serializable {
	private Long recordCount;
	private int totalPages;
	private List<EsSearchResultModel> itemList;
	public Long getRecordCount() {
		return recordCount;
	}
	public void setRecordCount(Long recordCount) {
		this.recordCount = recordCount;
	}
	public int getTotalPages() {
		return totalPages;
	}
	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}
	public List<EsSearchResultModel> getItemList() {
		return itemList;
	}
	public void setItemList(List<EsSearchResultModel> itemList) {
		this.itemList = itemList;
	}
	@Override
	public String toString() {
		return "ResultModel [recordCount=" + recordCount + ", totalPages="
				+ totalPages + ", itemList=" + itemList + "]";
	}
	
}
