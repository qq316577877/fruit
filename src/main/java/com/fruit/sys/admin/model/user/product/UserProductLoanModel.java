package com.fruit.sys.admin.model.user.product;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class UserProductLoanModel implements Serializable {
    private long id;

    private int userId;

    private int productId;

    private String productName;

    private BigDecimal productLoan;

    private int status;

    private Date addTime;

    private Date updateTime;

    private static final long serialVersionUID = 1L;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
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
        this.productName = productName == null ? null : productName.trim();
    }

    public BigDecimal getProductLoan() {
        return productLoan;
    }

    public void setProductLoan(BigDecimal productLoan) {
        this.productLoan = productLoan;
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

}