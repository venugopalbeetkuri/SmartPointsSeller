package com.bizzmark.seller.sellerwithoutlogin.db;

/**
 * Created by Tharun on 25-02-2017.
 */

public class SellerBasicInformation {

   // public String uid;
    public String name;
    public String mgname;
    public String address;
    public String discount;

    public SellerBasicInformation(){

    }

    public SellerBasicInformation(String name,String mgname,String address, String discount){
       // this.uid=uid;
        this.name=name;
        this.mgname=mgname;
        this.address=address;
        this.discount=discount;
    }

}
