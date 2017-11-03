package com.fruit.sys.admin.model.neworder;


import com.fruit.newOrder.biz.dto.GoodsCommodityPicDTO;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class GoodsCommodityInfoVO {

	private int id;

	private String name;

	private int categoryId;

	private int varietyId;

	private BigDecimal priceLow;

	private BigDecimal priceHigh;

	private BigDecimal priceActual;

	private String unit;

	private String description;

	private String editor;

	private int status;

	private Date addTime;

	private Date updateTime;

	private int sortid;

	private List<GoodsCommodityPicDTO> goodsCommodityPicDTOs;

	private static final long serialVersionUID = 1L;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name == null ? null : name.trim();
	}

	public int getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}

	public int getVarietyId() {
		return varietyId;
	}

	public void setVarietyId(int varietyId) {
		this.varietyId = varietyId;
	}

	public BigDecimal getPriceLow() {
		return priceLow;
	}

	public void setPriceLow(BigDecimal priceLow) {
		this.priceLow = priceLow;
	}

	public BigDecimal getPriceHigh() {
		return priceHigh;
	}

	public void setPriceHigh(BigDecimal priceHigh) {
		this.priceHigh = priceHigh;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit == null ? null : unit.trim();
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description == null ? null : description.trim();
	}

	public String getEditor() {
		return editor;
	}

	public void setEditor(String editor) {
		this.editor = editor == null ? null : editor.trim();
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Date getAddTime() {
		return addTime;
	}

	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public List<GoodsCommodityPicDTO> getGoodsCommodityPicDTOs() {
		return goodsCommodityPicDTOs;
	}

	public void setGoodsCommodityPicDTOs(List<GoodsCommodityPicDTO> goodsCommodityPicDTOs) {
		this.goodsCommodityPicDTOs = goodsCommodityPicDTOs;
	}

	public BigDecimal getPriceActual() {
		return priceActual;
	}

	public void setPriceActual(BigDecimal priceActual) {
		this.priceActual = priceActual;
	}

	public int getSortid() {
		return sortid;
	}

	public void setSortid(int sortid) {
		this.sortid = sortid;
	}
}
