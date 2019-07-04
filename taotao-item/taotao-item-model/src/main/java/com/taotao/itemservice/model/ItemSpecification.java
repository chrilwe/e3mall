package com.taotao.itemservice.model;

import java.io.Serializable;
import java.util.Date;
/**
 * 【商品规格】
 * @author Administrator
 *
 */
public class ItemSpecification implements Serializable {
	private Long id;

    private Long itemId;

    private Date created;

    private Date updated;

    private String paramData;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getItemId() {
		return itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
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

	public String getParamData() {
		return paramData;
	}

	public void setParamData(String paramData) {
		this.paramData = paramData;
	}

	@Override
	public String toString() {
		return "ItemSpecification [id=" + id + ", itemId=" + itemId
				+ ", created=" + created + ", updated=" + updated
				+ ", paramData=" + paramData + "]";
	}
    
}
