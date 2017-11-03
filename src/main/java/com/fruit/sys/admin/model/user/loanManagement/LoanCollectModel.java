package com.fruit.sys.admin.model.user.loanManagement;

import java.io.Serializable;
import java.math.BigDecimal;

public class LoanCollectModel implements Serializable {

    private BigDecimal applyAmount;    //申请总额

    private BigDecimal applyFee;    //申请总服务费

    private int applyCount;    //申请总笔数

    private BigDecimal pendingAmount;    //待放款总额

    private BigDecimal pendingFee;    //待放款服务费

    private int pendingCount;    //待放款笔数

    private BigDecimal repaymentAmount;    //已放款总额

    private BigDecimal repaymentFee;    //已放款服务费

    private int repaymentCount;    //已放款笔数

    public BigDecimal getApplyAmount() {
        return applyAmount;
    }

    public void setApplyAmount(BigDecimal applyAmount) {
        this.applyAmount = applyAmount;
    }

    public BigDecimal getApplyFee() {
        return applyFee;
    }

    public void setApplyFee(BigDecimal applyFee) {
        this.applyFee = applyFee;
    }

    public int getApplyCount() {
        return applyCount;
    }

    public void setApplyCount(int applyCount) {
        this.applyCount = applyCount;
    }

    public BigDecimal getPendingAmount() {
        return pendingAmount;
    }

    public void setPendingAmount(BigDecimal pendingAmount) {
        this.pendingAmount = pendingAmount;
    }

    public BigDecimal getPendingFee() {
        return pendingFee;
    }

    public void setPendingFee(BigDecimal pendingFee) {
        this.pendingFee = pendingFee;
    }

    public int getPendingCount() {
        return pendingCount;
    }

    public void setPendingCount(int pendingCount) {
        this.pendingCount = pendingCount;
    }

    public BigDecimal getRepaymentAmount() {
        return repaymentAmount;
    }

    public void setRepaymentAmount(BigDecimal repaymentAmount) {
        this.repaymentAmount = repaymentAmount;
    }

    public BigDecimal getRepaymentFee() {
        return repaymentFee;
    }

    public void setRepaymentFee(BigDecimal repaymentFee) {
        this.repaymentFee = repaymentFee;
    }

    public int getRepaymentCount() {
        return repaymentCount;
    }

    public void setRepaymentCount(int repaymentCount) {
        this.repaymentCount = repaymentCount;
    }
}