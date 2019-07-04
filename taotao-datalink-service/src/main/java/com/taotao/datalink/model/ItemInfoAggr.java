package com.taotao.datalink.model;

import java.io.Serializable;
import java.util.Date;

import com.taotao.itemservice.model.Item;
import com.taotao.itemservice.model.ItemProperty;
import com.taotao.itemservice.model.ItemSpecification;

/**
 * 商品基本信息维度的聚合类
 * @author Administrator
 *
 */
public class ItemInfoAggr implements Serializable {
	private String title;

    private String sellPoint;

    private Long price;

    private Integer num;

    private String barcode;

    private String image;

    private Long cid;

    private Integer status;
	
	private Long itemSpecificationId;

    private Long itemId;

    private Date updated;

    private String paramData;
	
    private long propertyId;
	
	private String propertyName;
	
	private String propertyValue;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSellPoint() {
		return sellPoint;
	}

	public void setSellPoint(String sellPoint) {
		this.sellPoint = sellPoint;
	}

	public Long getPrice() {
		return price;
	}

	public void setPrice(Long price) {
		this.price = price;
	}

	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public Long getCid() {
		return cid;
	}

	public void setCid(Long cid) {
		this.cid = cid;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Long getItemSpecificationId() {
		return itemSpecificationId;
	}

	public void setItemSpecificationId(Long itemSpecificationId) {
		this.itemSpecificationId = itemSpecificationId;
	}

	public Long getItemId() {
		return itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
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

	public long getPropertyId() {
		return propertyId;
	}

	public void setPropertyId(long propertyId) {
		this.propertyId = propertyId;
	}

	public String getPropertyName() {
		return propertyName;
	}

	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}

	public String getPropertyValue() {
		return propertyValue;
	}

	public void setPropertyValue(String propertyValue) {
		this.propertyValue = propertyValue;
	}

	@Override
	public String toString() {
		return "ItemInfoAggr [title=" + title + ", sellPoint=" + sellPoint
				+ ", price=" + price + ", num=" + num + ", barcode=" + barcode
				+ ", image=" + image + ", cid=" + cid + ", status=" + status
				+ ", itemSpecificationId=" + itemSpecificationId + ", itemId="
				+ itemId + ", updated=" + updated + ", paramData=" + paramData
				+ ", propertyId=" + propertyId + ", propertyName="
				+ propertyName + ", propertyValue=" + propertyValue + "]";
	}
}
