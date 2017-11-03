package com.fruit.sys.admin.model.order;

import java.math.BigDecimal;
import java.util.List;

public class OrderLogistics {

	private long id;

	private String orderNo;

	private int type;

	private int tradeType;

	private int preClearance;

	private int clearance;

	private int clearanceCompanyId;

	private int insurance;

	private int innerExpressId;

	private int outerExpressId;

	private String contractUrl;

	private String voucherUrl;

	private int payType;

	private int status;

	private Integer needLoan;

	private List<ApplyLoanInfoVo> loadInfo;

	private Integer deliveryId;

	private List<OrderContainerFee> feeList;

	private String backMemo;
	
	private BigDecimal advance;

    private BigDecimal restPay;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
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

	public String getContractUrl() {
		return contractUrl;
	}

	public void setContractUrl(String contractUrl) {
		this.contractUrl = contractUrl;
	}

	public String getVoucherUrl() {
		return voucherUrl;
	}

	public void setVoucherUrl(String voucherUrl) {
		this.voucherUrl = voucherUrl;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Integer getNeedLoan() {
		return needLoan;
	}

	public void setNeedLoan(Integer needLoan) {
		this.needLoan = needLoan;
	}

	public int getPayType() {
		return payType;
	}

	public void setPayType(int payType) {
		this.payType = payType;
	}

	public Integer getDeliveryId() {
		return deliveryId;
	}

	public void setDeliveryId(Integer deliveryId) {
		this.deliveryId = deliveryId;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public List<OrderContainerFee> getFeeList() {
		return feeList;
	}

	public void setFeeList(List<OrderContainerFee> feeList) {
		this.feeList = feeList;
	}

	public String getBackMemo() {
		return backMemo;
	}

	public void setBackMemo(String backMemo) {
		this.backMemo = backMemo;
	}

	public List<ApplyLoanInfoVo> getLoadInfo() {
		return loadInfo;
	}

	public void setLoadInfo(List<ApplyLoanInfoVo> loadInfo) {
		this.loadInfo = loadInfo;
	}

	public BigDecimal getAdvance() {
		return advance;
	}

	public void setAdvance(BigDecimal advance) {
		this.advance = advance;
	}

	public BigDecimal getRestPay() {
		return restPay;
	}

	public void setRestPay(BigDecimal restPay) {
		this.restPay = restPay;
	}
}
