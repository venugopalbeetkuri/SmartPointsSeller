package com.bizzmark.seller.sellerwithoutlogin.sellerInfo;

/**
 * Created by Tharun on 10-06-2017.
 */

public class SellerInfoList {

    public String sellerStoreName;
    public String sellerStoreId;
    public String sellerBranchName;
    public String sellerBranchId;
    public String storePointsPercentage;
    public String storePointsValue;
    public String sellerStoreEmail;
    public String sellerStoreAddress;

    public SellerInfoList(String sellerStoreName, String sellerStoreId, String sellerBranchName, String sellerBranchId, String storePointsPercentage, String storePointsValue, String sellerStoreEmail, String sellerStoreAddress) {
        this.sellerStoreName = sellerStoreName;
        this.sellerStoreId = sellerStoreId;
        this.sellerBranchName = sellerBranchName;
        this.sellerBranchId = sellerBranchId;
        this.storePointsPercentage = storePointsPercentage;
        this.storePointsValue = storePointsValue;
        this.sellerStoreEmail = sellerStoreEmail;
        this.sellerStoreAddress = sellerStoreAddress;
    }

    public String getSellerStoreName() {
        return sellerStoreName;
    }

    public String getSellerStoreId() {
        return sellerStoreId;
    }

    public String getSellerBranchName() {
        return sellerBranchName;
    }

    public String getSellerBranchId() {
        return sellerBranchId;
    }

    public String getStorePointsPercentage() {
        return storePointsPercentage;
    }

    public String getStorePointsValue() {
        return storePointsValue;
    }

    public String getSellerStoreEmail() {
        return sellerStoreEmail;
    }

    public String getSellerStoreAddress() {
        return sellerStoreAddress;
    }
}
