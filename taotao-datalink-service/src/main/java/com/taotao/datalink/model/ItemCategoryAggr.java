package com.taotao.datalink.model;

import java.io.Serializable;
import java.util.Date;

import com.taotao.itemservice.model.ItemCategory;

/**
 * 商品分类维度聚合类
 * @author Administrator
 *
 */
public class ItemCategoryAggr implements Serializable {
	private static final long serialVersionUID = 8085912042554965561L;

	private Long categoryId;

    private Long parentId;

    private String name;

    private Integer status;

    private Integer sortOrder;

    private long isParent;

    private Date created;

    private Date updated;
	
	private long itemId;

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(Integer sortOrder) {
		this.sortOrder = sortOrder;
	}

	public long getIsParent() {
		return isParent;
	}

	public void setIsParent(long isParent) {
		this.isParent = isParent;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Date getUpdated() {
		return updated;
	}

	public void setUpdated(Date updated) {
		this.updated = updated;
	}

	public long getItemId() {
		return itemId;
	}

	public void setItemId(long itemId) {
		this.itemId = itemId;
	}

	@Override
	public String toString() {
		return "ItemCategoryAggr [categoryId=" + categoryId + ", parentId="
				+ parentId + ", name=" + name + ", status=" + status
				+ ", sortOrder=" + sortOrder + ", isParent=" + isParent
				+ ", created=" + created + ", updated=" + updated + ", itemId="
				+ itemId + "]";
	}
	
}
