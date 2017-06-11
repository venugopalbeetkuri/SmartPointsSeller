package com.bizzmark.seller.sellerwithoutlogin.db;

/**
 * Created by Tharun on 26-04-2017.
 */

public class RedeemAcknowledgement {

    String status;
    String earnRedeemString;
    String type;
  //  String billAmount;
    String storeName;
    String points;
    String deviceId;
    String discountAmount;
    String time;
    String oldBillAmount;
    String newBillAmount;
    String branchId;
    String storeId;
    String transId;

    public RedeemAcknowledgement() {
    }

    public RedeemAcknowledgement(String status,String earnRedeemString){
        this.status=status;
        this.earnRedeemString=earnRedeemString;
    }

    public RedeemAcknowledgement(String status,
                                 String type,
                                 String oldBillAmount,
                                 String newBillAmount,
                                 String storeName,
                                 String deviceId,
                                 String discountAmount,
                                 String time,
                                 String branchId,
                                 String storeId,
                                 String points,
                                 String transId) {
        this.status = status;
        this.type = type;
        this.oldBillAmount = oldBillAmount;
        this.newBillAmount = newBillAmount;
        this.storeName = storeName;
        this.deviceId = deviceId;
        this.discountAmount = discountAmount;
        this.time = time;
        this.branchId = branchId;
        this.storeId = storeId;
        this.points = points;
        this.transId = transId;
    }

    public String getTransId() {
        return transId;
    }

    public void setTransId(String transId) {
        this.transId = transId;
    }

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

//    public String getBillAmount() {
//        return billAmount;
//    }
//
//    public void setBillAmount(String billAmount) {
//        this.billAmount = billAmount;
//    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

//    public String getPoints() {
//        return points;
//    }
//
//    public void setPoints(String points) {
//        this.points = points;
//    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(String discountAmount) {
        this.discountAmount = discountAmount;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getOldBillAmount() {
        return oldBillAmount;
    }

    public void setOldBillAmount(String oldBillAmount) {
        this.oldBillAmount = oldBillAmount;
    }

    public String getNewBillAmount() {
        return newBillAmount;
    }

    public void setNewBillAmount(String newBillAmount) {
        this.newBillAmount = newBillAmount;
    }



    public String getEarnRedeemString() {  return  earnRedeemString;   }

    public void setEarnRedeemString(String earnRedeemString) {

        this.earnRedeemString=earnRedeemString;
    }

    public String getBranchId() {
        return branchId;
    }

    public void setBranchId(String branchId) {
        this.branchId = branchId;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }
}
