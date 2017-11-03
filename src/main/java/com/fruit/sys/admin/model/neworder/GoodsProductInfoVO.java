package com.fruit.sys.admin.model.neworder;

import com.fruit.newOrder.biz.dto.GoodsProductPicDTO;

import java.util.Date;
import java.util.List;

public class GoodsProductInfoVO {

	private int id;

	private int categoryId;

	private int varietyId;

	private String name;

	private String enName;

	private String priceRangeCommon;

	private String priceRangeVip;

	private String description;

	private String editor;

	private int status;

	private Date addTime;

	private Date updateTime;

	private List<GoodsProductPicDTO> goodsProductPicDTOs;

	private List<GoodsCommodityInfoVO> goodsCommodityInfos;

	private static final long serialVersionUID = 1L;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name == null ? null : name.trim();
	}

	public String getEnName() {
		return enName;
	}

	public void setEnName(String enName) {
		this.enName = enName == null ? null : enName.trim();
	}

	public String getPriceRangeCommon() {
		return priceRangeCommon;
	}

	public void setPriceRangeCommon(String priceRangeCommon) {
		this.priceRangeCommon = priceRangeCommon == null ? null : priceRangeCommon.trim();
	}

	public String getPriceRangeVip() {
		return priceRangeVip;
	}

	public void setPriceRangeVip(String priceRangeVip) {
		this.priceRangeVip = priceRangeVip == null ? null : priceRangeVip.trim();
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

	public List<GoodsProductPicDTO> getGoodsProductPicDTOs() {
		return goodsProductPicDTOs;
	}

	public void setGoodsProductPicDTOs(List<GoodsProductPicDTO> goodsProductPicDTOs) {
		this.goodsProductPicDTOs = goodsProductPicDTOs;
	}

	public List<GoodsCommodityInfoVO> getGoodsCommodityInfos() {
		return goodsCommodityInfos;
	}

	public void setGoodsCommodityInfos(List<GoodsCommodityInfoVO> goodsCommodityInfos) {
		this.goodsCommodityInfos = goodsCommodityInfos;
	}
}
