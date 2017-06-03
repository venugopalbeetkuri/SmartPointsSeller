package com.bizzmark.seller.sellerwithoutlogin.Reports.Last_Ten_Transactions;

/**
 * Created by Tharun on 22-05-2017.
 */

public class LastTenTransactionsList {

    private String transId;
    private String lastTenTransType;
    private String originalBillReport;
    private String recentTranBillpoints;
    private String discountedAmount;
    private String newbillamount;
    private String transactionAt;

    public LastTenTransactionsList(String transId, String lastTenTransType, String originalBillReport, String recentTranBillpoints, String discountedAmount, String newbillamount, String transactionAt) {
        this.transId = transId;
        this.lastTenTransType = lastTenTransType;
        this.originalBillReport = originalBillReport;
        this.recentTranBillpoints = recentTranBillpoints;
        this.discountedAmount = discountedAmount;
        this.newbillamount = newbillamount;
        this.transactionAt = transactionAt;
    }

    public String getTransId() {
        return transId;
    }

    public String getLastTenTransType() {
        return lastTenTransType;
    }

    public String getOriginalBillReport() {
        return originalBillReport;
    }

    public String getRecentTranBillpoints() {
        return recentTranBillpoints;
    }

    public String getDiscountedAmount() {
        return discountedAmount;
    }

    public String getNewbillamount() {
        return newbillamount;
    }

    public String getTransactionAt() {
        return transactionAt;
    }
}
