package com.fruit.sys.admin.model.user.credit;

import java.io.Serializable;
import java.math.BigDecimal;

public class CreditCollectModel implements Serializable {

    private BigDecimal creditAmount;    //授信总额

    private BigDecimal expenditureTotal;    //已用贷款

    private BigDecimal balanceTotal;    //可用额度

    public BigDecimal getCreditAmount() {
        return creditAmount;
    }

    public void setCreditAmount(BigDecimal creditAmount) {
        this.creditAmount = creditAmount;
    }

    public BigDecimal getExpenditureTotal() {
        return expenditureTotal;
    }

    public void setExpenditureTotal(BigDecimal expenditureTotal) {
        this.expenditureTotal = expenditureTotal;
    }

    public BigDecimal getBalanceTotal() {
        return balanceTotal;
    }

    public void setBalanceTotal(BigDecimal balanceTotal) {
        this.balanceTotal = balanceTotal;
    }
}