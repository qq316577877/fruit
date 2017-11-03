package com.fruit.sys.admin.model.order;

import java.util.List;

public class ProductInfoVo {

	private Integer id;

	private String name;

	private String enName;

	private int capacitySize;

	private String unit;

	private Integer status;

	private List<ProductPropertyVo> productDetails;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEnName() {
		return enName;
	}

	public void setEnName(String enName) {
		this.enName = enName;
	}

	public int getCapacitySize() {
		return capacitySize;
	}

	public void setCapacitySize(int capacitySize) {
		this.capacitySize = capacitySize;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public List<ProductPropertyVo> getProductDetails() {
		return productDetails;
	}

	public void setProductDetails(List<ProductPropertyVo> productDetails) {
		this.productDetails = productDetails;
	}
}
