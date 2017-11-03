package com.fruit.sys.admin.model.order;

import java.util.List;

import com.fruit.order.biz.dto.ProductPropertyValueDTO;

public class ProductPropertyVo {

	private String name;
	
	private String engName;
	
	private List<ProductPropertyValueDTO> values;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getEngName() {
		return engName;
	}

	public void setEngName(String engName) {
		this.engName = engName;
	}

	public List<ProductPropertyValueDTO> getValues() {
		return values;
	}

	public void setValues(List<ProductPropertyValueDTO> values) {
		this.values = values;
	}

}
