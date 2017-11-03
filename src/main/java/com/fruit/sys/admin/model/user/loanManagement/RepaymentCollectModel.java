package com.fruit.sys.admin.model.user.loanManagement;

import java.io.Serializable;
import java.math.BigDecimal;

public class RepaymentCollectModel implements Serializable {

    private BigDecimal offerLoanAmount;    //借据总额

    private int offerLoanCount;    //借据总笔数

    private BigDecimal unpayAmount;    //待还款总额

    private int unpayCount;    //待还款笔数

    private BigDecimal repaymentAmount;    //已还款总额

    private int repaymentCount;    //已还款笔数

    private BigDecimal depositAmount;    //保证金代还款总额

    private int depositCount;    //保证金代还款笔数

    public BigDecimal getOfferLoanAmount() {
        return offerLoanAmount;
    }

    public void setOfferLoanAmount(BigDecimal offerLoanAmount) {
        this.offerLoanAmount = offerLoanAmount;
    }

    public int getOfferLoanCount() {
        return offerLoanCount;
    }

    public void setOfferLoanCount(int offerLoanCount) {
        this.offerLoanCount = offerLoanCount;
    }

    public BigDecimal getUnpayAmount() {
        return unpayAmount;
    }

    public void setUnpayAmount(BigDecimal unpayAmount) {
        this.unpayAmount = unpayAmount;
    }

    public int getUnpayCount() {
        return unpayCount;
    }

    public void setUnpayCount(int unpayCount) {
        this.unpayCount = unpayCount;
    }

    public BigDecimal getRepaymentAmount() {
        return repaymentAmount;
    }

    public void setRepaymentAmount(BigDecimal repaymentAmount) {
        this.repaymentAmount = repaymentAmount;
    }

    public int getRepaymentCount() {
        return repaymentCount;
    }

    public void setRepaymentCount(int repaymentCount) {
        this.repaymentCount = repaymentCount;
    }

    public BigDecimal getDepositAmount() {
        return depositAmount;
    }

    public void setDepositAmount(BigDecimal depositAmount) {
        this.depositAmount = depositAmount;
    }

    public int getDepositCount() {
        return depositCount;
    }

    public void setDepositCount(int depositCount) {
        this.depositCount = depositCount;
    }
}