package com.bizzmark.seller.sellerwithoutlogin.Reports.Earn_Reports;

/**
 * Created by Tharun on 23-05-2017.
 */

public class EarnFragTansList {

    private String earnTransId;
    private String earnOriginalBillReport;
    private String earnBillpoints;
    private String earnTransactionAt;

    public EarnFragTansList(String earnTransId, String earnOriginalBillReport, String earnBillpoints, String earnTransactionAt) {
        this.earnTransId = earnTransId;
        this.earnOriginalBillReport = earnOriginalBillReport;
        this.earnBillpoints = earnBillpoints;
        this.earnTransactionAt = earnTransactionAt;
    }

    public String getEarnTransId() {
        return earnTransId;
    }

    public String getEarnOriginalBillReport() {
        return earnOriginalBillReport;
    }

    public String getEarnBillpoints() {
        return earnBillpoints;
    }

    public String getEarnTransactionAt() {
        return earnTransactionAt;
    }
}
