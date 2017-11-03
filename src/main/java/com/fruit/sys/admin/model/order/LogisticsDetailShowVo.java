package com.fruit.sys.admin.model.order;

import java.math.BigDecimal;
import java.util.List;

import com.fruit.sys.admin.model.user.account.AddressVo;

public class LogisticsDetailShowVo {

	private long id;

	private int logisticsType;

	private int tradeType;

	private int preClearance;

	private int clearance;

	private EnterpriseInfoVo clearanceCompany;

	private int insurance;

	private EnterpriseInfoVo innerExpress;

	private EnterpriseInfoVo outerExpress;

	private DeliveryInfoVo deliveryAddress;

	private String contractUrl;

	private String voucherUrl;

	private int payType;

	private Integer needLoan;

	private List<OrderContainerFee> feeList;

	private List<AddressVo> deliveryAddressList;

	private BigDecimal advance;

	private BigDecimal restPay;

	private String backMemo;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getLogisticsType() {
		return logisticsType;
	}

	public void setLogisticsType(int logisticsType) {
		this.logisticsType = logisticsType;
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

	public EnterpriseInfoVo getClearanceCompany() {
		return clearanceCompany;
	}

	public void setClearanceCompany(EnterpriseInfoVo clearanceCompany) {
		this.clearanceCompany = clearanceCompany;
	}

	public int getInsurance() {
		return insurance;
	}

	public void setInsurance(int insurance) {
		this.insurance = insurance;
	}

	public EnterpriseInfoVo getInnerExpress() {
		return innerExpress;
	}

	public void setInnerExpress(EnterpriseInfoVo innerExpress) {
		this.innerExpress = innerExpress;
	}

	public EnterpriseInfoVo getOuterExpress() {
		return outerExpress;
	}

	public void setOuterExpress(EnterpriseInfoVo outerExpress) {
		this.outerExpress = outerExpress;
	}

	public DeliveryInfoVo getDeliveryAddress() {
		return deliveryAddress;
	}

	public void setDeliveryAddress(DeliveryInfoVo deliveryAddress) {
		this.deliveryAddress = deliveryAddress;
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

	public int getPayType() {
		return payType;
	}

	public void setPayType(int payType) {
		this.payType = payType;
	}

	public Integer getNeedLoan() {
		return needLoan;
	}

	public void setNeedLoan(Integer needLoan) {
		this.needLoan = needLoan;
	}

	public List<OrderContainerFee> getFeeList() {
		return feeList;
	}

	public void setFeeList(List<OrderContainerFee> feeList) {
		this.feeList = feeList;
	}

	public List<AddressVo> getDeliveryAddressList() {
		return deliveryAddressList;
	}

	public void setDeliveryAddressList(List<AddressVo> deliveryAddressList) {
		this.deliveryAddressList = deliveryAddressList;
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

	public String getBackMemo() {
		return backMemo;
	}

	public void setBackMemo(String backMemo) {
		this.backMemo = backMemo;
	}
}
