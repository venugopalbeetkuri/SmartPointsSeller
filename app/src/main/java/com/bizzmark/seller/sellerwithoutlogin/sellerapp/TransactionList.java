package com.bizzmark.seller.sellerwithoutlogin.sellerapp;

/**
 * Created by Tharun on 05-05-2017.
 */

public class TransactionList {

    private String billReport;
    private String billDateAndTime;
    private String billPoints;
    private String billDeviceId;
    private String storename;
    private String type;
    private String discounted_bill_amount;

    public TransactionList(String billReport, String billDateAndTime, String billPoints, String billDeviceId, String storename, String type) {
        this.billReport = billReport;
        this.billDateAndTime = billDateAndTime;
        this.billPoints = billPoints;
        this.billDeviceId = billDeviceId;
        this.storename = storename;
        this.type = type;
    }

    public TransactionList(String billDeviceId, String billReport, String billDateAndTime, String billPoints,
                           String type, String discounted_bill_amount, String storename) {
        this.billReport = billReport;
        this.billDateAndTime = billDateAndTime;
        this.billPoints = billPoints;
        this.billDeviceId = billDeviceId;
        this.storename = storename;
        this.type = type;
        this.discounted_bill_amount = discounted_bill_amount;
    }

    public String getBillReport() {
        return billReport;
    }

    public String getBillDateAndTime() {
        return billDateAndTime;
    }

    public String getBillPoints() {
        return billPoints;
    }

    public String getBillDeviceId() {
        return billDeviceId;
    }

    public String getStorename() {
        return storename;
    }

    public String getType() { return type; }

    public String getDiscounted_bill_amount() { return discounted_bill_amount; }
}
