package com.fruit.sys.admin.model.neworder;

import com.fruit.sys.admin.model.user.account.AddressVo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class ContainerInfoVO implements Serializable {
    private long id;

    private String containerNo;

    private String containerName;

    private String containerSerialNo;

    private String orderNo;

    private BigDecimal totalQuantity;

    private BigDecimal loanAmount;

    private BigDecimal productAmount;

    private BigDecimal agencyAmount;

    private BigDecimal insuranceAmount;

    private BigDecimal totalPrice;

    private int deliveryId;

    private AddressVo deliveryAdress;

    private Date deliveryTime;

    private Date preReceiveTime;

    private Date clearanceTime;

    private Date receiveTime;

    private int deliveryType;

    private String deliveryTypeDesc;

    private int tradeType;

    private int preClearance;

    private int clearance;

    private int clearanceCompanyId;

    private int insurance;

    private int innerExpressId;

    private int outerExpressId;

    private String lockId;

    private List<ContainerGoodsInfoVO> containerGoodsList;

    private int status;

    private String statusDesc;

    private String loanStatusDesc;

    private Date addTime;

    private String addTimeStr;

    private int updateUser;

    private Date updateTime;

    private BigDecimal orderLoanAmount;

    private BigDecimal orderLoaned;

    private String submitTimeStr;

    private static final long serialVersionUID = 1L;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getContainerNo() {
        return containerNo;
    }

    public void setContainerNo(String containerNo) {
        this.containerNo = containerNo == null ? null : containerNo.trim();
    }

    public String getContainerSerialNo() {
        return containerSerialNo;
    }

    public void setContainerSerialNo(String containerSerialNo) {
        this.containerSerialNo = containerSerialNo == null ? null : containerSerialNo.trim();
    }

    public String getContainerName() {
        return containerName;
    }

    public void setContainerName(String containerName) {
        this.containerName = containerName;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo == null ? null : orderNo.trim();
    }

    public BigDecimal getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(BigDecimal totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public BigDecimal getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(BigDecimal loanAmount) {
        this.loanAmount = loanAmount;
    }

    public BigDecimal getProductAmount() {
        return productAmount;
    }

    public void setProductAmount(BigDecimal productAmount) {
        this.productAmount = productAmount;
    }

    public BigDecimal getAgencyAmount() {
        return agencyAmount;
    }

    public void setAgencyAmount(BigDecimal agencyAmount) {
        this.agencyAmount = agencyAmount;
    }

    public BigDecimal getInsuranceAmount() {
        return insuranceAmount;
    }

    public void setInsuranceAmount(BigDecimal insuranceAmount) {
        this.insuranceAmount = insuranceAmount;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public int getDeliveryId() {
        return deliveryId;
    }

    public void setDeliveryId(int deliveryId) {
        this.deliveryId = deliveryId;
    }

    public Date getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(Date deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public Date getPreReceiveTime() {
        return preReceiveTime;
    }

    public void setPreReceiveTime(Date preReceiveTime) {
        this.preReceiveTime = preReceiveTime;
    }

    public Date getClearanceTime() {
        return clearanceTime;
    }

    public void setClearanceTime(Date clearanceTime) {
        this.clearanceTime = clearanceTime;
    }

    public Date getReceiveTime() {
        return receiveTime;
    }

    public void setReceiveTime(Date receiveTime) {
        this.receiveTime = receiveTime;
    }

    public int getDeliveryType() {
        return deliveryType;
    }

    public void setDeliveryType(int deliveryType) {
        this.deliveryType = deliveryType;
    }

    public int getTradeType() {
        return tradeType;
    }

    public void setTradeType(int tradeType) {
        this.tradeType = tradeType;
    }

    public int getPreClearance() {
        return preClearance;
    }

    public void setPreClearance(int preClearance) {
        this.preClearance = preClearance;
    }

    public int getClearance() {
        return clearance;
    }

    public void setClearance(int clearance) {
        this.clearance = clearance;
    }

    public int getClearanceCompanyId() {
        return clearanceCompanyId;
    }

    public void setClearanceCompanyId(int clearanceCompanyId) {
        this.clearanceCompanyId = clearanceCompanyId;
    }

    public int getInsurance() {
        return insurance;
    }

    public void setInsurance(int insurance) {
        this.insurance = insurance;
    }

    public int getInnerExpressId() {
        return innerExpressId;
    }

    public void setInnerExpressId(int innerExpressId) {
        this.innerExpressId = innerExpressId;
    }

    public int getOuterExpressId() {
        return outerExpressId;
    }

    public void setOuterExpressId(int outerExpressId) {
        this.outerExpressId = outerExpressId;
    }

    public String getLockId() {
        return lockId;
    }

    public void setLockId(String lockId) {
        this.lockId = lockId == null ? null : lockId.trim();
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getStatusDesc() {
        return statusDesc;
    }

    public void setStatusDesc(String statusDesc) {
        this.statusDesc = statusDesc;
    }

    public String getLoanStatusDesc() {
        return loanStatusDesc;
    }

    public void setLoanStatusDesc(String loanStatusDesc) {
        this.loanStatusDesc = loanStatusDesc;
    }

    public Date getAddTime() {
        return addTime;
    }

    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }

    public int getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(int updateUser) {
        this.updateUser = updateUser;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public AddressVo getDeliveryAdress() {
        return deliveryAdress;
    }

    public void setDeliveryAdress(AddressVo deliveryAdress) {
        this.deliveryAdress = deliveryAdress;
    }

    public String getDeliveryTypeDesc() {
        return deliveryTypeDesc;
    }

    public void setDeliveryTypeDesc(String deliveryTypeDesc) {
        this.deliveryTypeDesc = deliveryTypeDesc;
    }

    public List<ContainerGoodsInfoVO> getContainerGoodsList() {
        return containerGoodsList;
    }

    public void setContainerGoodsList(List<ContainerGoodsInfoVO> containerGoodsList) {
        this.containerGoodsList = containerGoodsList;
    }

    public String getAddTimeStr() {
        return addTimeStr;
    }

    public void setAddTimeStr(String addTimeStr) {
        this.addTimeStr = addTimeStr;
    }

    public BigDecimal getOrderLoanAmount() {
        return orderLoanAmount;
    }

    public void setOrderLoanAmount(BigDecimal orderLoanAmount) {
        this.orderLoanAmount = orderLoanAmount;
    }

    public BigDecimal getOrderLoaned() {
        return orderLoaned;
    }

    public void setOrderLoaned(BigDecimal orderLoaned) {
        this.orderLoaned = orderLoaned;
    }

    public String getSubmitTimeStr() {
        return submitTimeStr;
    }

    public void setSubmitTimeStr(String submitTimeStr) {
        this.submitTimeStr = submitTimeStr;
    }
}