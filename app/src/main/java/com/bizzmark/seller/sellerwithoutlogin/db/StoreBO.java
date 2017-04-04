package com.bizzmark.seller.sellerwithoutlogin.db;

/**
 * Created by Tharun on 23-02-2017.
 */

public class StoreBO {

    String emailId;
    static String storeName;
    static String percentage;

    public StoreBO(String storeEmail, String storeName, String percentage){
        this.emailId = storeEmail;
        this.storeName = storeName;
        this.percentage = percentage;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public static String getStoreName() { return storeName; }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public static String getPercentage() {return percentage;}

    public void  setPercentage(String percentage) { this.percentage=percentage; }


}
