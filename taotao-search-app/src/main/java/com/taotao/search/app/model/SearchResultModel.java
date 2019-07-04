package com.taotao.search.app.model;

import java.util.List;

import com.taotao.search.model.SearchModel;

/**
 * 搜索结果类
 * @author Administrator
 *
 */
public class SearchResultModel {
	private long recordCount;
	
	private int totalPages;
	
	private List<SearchModel> itemList;

	public long getRecordCount() {
		return recordCount;
	}

	public void setRecordCount(long recordCount) {
		this.recordCount = recordCount;
	}

	public int getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}

	public List<SearchModel> getItemList() {
		return itemList;
	}

	public void setItemList(List<SearchModel> itemList) {
		this.itemList = itemList;
	}

	@Override
	public String toString() {
		return "SearchResultModel [recordCount=" + recordCount
				+ ", totalPages=" + totalPages + ", itemList=" + itemList + "]";
	}
	
}
