package com.fruit.sys.admin.model.order;

import java.math.BigDecimal;
import java.util.Map;

public class OrderContainerDetail {

	private long id;

	private String containerNo;

	private int productId;

	private String productName;

	private Map<String, String> productDetail;

	private BigDecimal price;

	private BigDecimal quantity;

	private BigDecimal totalPrice;

	private int status;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getProductId() {
		return productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public Map<String, String> getProductDetail() {
		return productDetail;
	}

	public void setProductDetail(Map<String, String> productDetail) {
		this.productDetail = productDetail;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public BigDecimal getQuantity() {
		return quantity;
	}

	public void setQuantity(BigDecimal quantity) {
		this.quantity = quantity;
	}

	public BigDecimal getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(BigDecimal totalPrice) {
		this.totalPrice = totalPrice;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getContainerNo() {
		return containerNo;
	}

	public void setContainerNo(String containerNo) {
		this.containerNo = containerNo;
	}

}
