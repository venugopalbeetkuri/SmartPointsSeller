package com.bizzmark.seller.sellerwithoutlogin.My_Customers;

/**
 * Created by Tharun on 29-05-2017.
 */

public class MyCustomersList {

    private String branchVistors;
    private String storeVisitors;

    public MyCustomersList(String branchVistors, String storeVisitors) {
        this.branchVistors = branchVistors;
        this.storeVisitors = storeVisitors;
    }

    public String getBranchVistors() {
        return branchVistors;
    }

    public String getStoreVisitors() {
        return storeVisitors;
    }
}
