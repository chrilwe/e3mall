package com.taotao.search.model;

import java.io.Serializable;
import java.util.List;

import com.taotao.search.model.SearchModel;

/**
 * 搜索结果类
 * @author Administrator
 *
 */
public class SearchResultModel implements Serializable {
	private Long recordCount;
	
	private int totalPages;
	
	private List<SearchModel> itemList;

	

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
