package com.bizzmark.seller.sellerwithoutlogin.Reports.Redeem_Reports;

/**
 * Created by Tharun on 23-05-2017.
 */

public class RedeemFragTransList {

    private String redeemTransId;
    private String redeemOriginalBillReport;
    private String redeemBillpoints;
    private String redeemDiscountedAmount;
    private String redeemnewbillamount;
    private String redeemTransactionAt;

    public RedeemFragTransList(String redeemTransId, String redeemOriginalBillReport, String redeemBillpoints, String redeemDiscountedAmount, String redeemnewbillamount, String redeemTransactionAt) {
        this.redeemTransId = redeemTransId;
        this.redeemOriginalBillReport = redeemOriginalBillReport;
        this.redeemBillpoints = redeemBillpoints;
        this.redeemDiscountedAmount = redeemDiscountedAmount;
        this.redeemnewbillamount = redeemnewbillamount;
        this.redeemTransactionAt = redeemTransactionAt;
    }

    public String getRedeemTransId() {
        return redeemTransId;
    }

    public String getRedeemOriginalBillReport() {
        return redeemOriginalBillReport;
    }

    public String getRedeemBillpoints() {
        return redeemBillpoints;
    }

    public String getRedeemDiscountedAmount() {
        return redeemDiscountedAmount;
    }

    public String getRedeemnewbillamount() {
        return redeemnewbillamount;
    }

    public String getRedeemTransactionAt() {
        return redeemTransactionAt;
    }
}
