package com.taotao.itemservice.model;

import java.io.Serializable;
import java.util.Date;
/**
 * 商品分类
 * @author Administrator
 *
 */
public class ItemCategory implements Serializable {
	private Long id;

    private Long parentId;

    private String name;

    private Integer status;

    private Integer sortOrder;

    private long isParent;

    private Date created;

    private Date updated;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	@Override
	public String toString() {
		return "ItemCategory [id=" + id + ", parentId=" + parentId + ", name="
				+ name + ", status=" + status + ", sortOrder=" + sortOrder
				+ ", isParent=" + isParent + ", created=" + created
				+ ", updated=" + updated + "]";
	}
    
}
