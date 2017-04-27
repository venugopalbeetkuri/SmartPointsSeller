package com.bizzmark.seller.sellerwithoutlogin.util;

import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.HashMap;

/**
 * Created by Tharun on 27-02-2017.
 */

public class Utility {

    static Gson gson=null;

    static boolean testing=true;

    public static int totalEarnPoints;

    public static int totalRedeemPoints;

    public static int totalBillAmount;

    public static int totalDiscount;

    static TextView pointsGiven, totalsale, totaldiscount;

    public static synchronized Gson getGsonObject(){

        if (null==gson)
        {
            gson=new Gson();
        }
        return gson;
    }

    public static synchronized boolean isTesting(){
        return testing;
    }

    public static void updateReference(TextView pointsGiven,TextView totalsale,TextView totaldiscount){

        try {
            Utility.pointsGiven=pointsGiven;
            Utility.totaldiscount=totaldiscount;
            Utility.totalsale=totalsale;
        }catch (Exception e){
            e.printStackTrace();
        }
    }

//    public static void calculateTotal(String storeName){
//
//        try {
//            DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference();
//
//            DatabaseReference clientDatabase=databaseReference.child("client");
//
//            Query query=clientDatabase.child(storeName);
//
//
//            query.addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(DataSnapshot dataSnapshot) {
//
//
//                    Utility.totalBillAmount=0;
//                    Utility.totalDiscount = 0;
//                    Utility.totalBillAmount = 0;
//
//
//                    for (DataSnapshot timeStampSnapShot : dataSnapshot.getChildren()){
//
//                        HashMap<String, String>  timeStampKey=(HashMap)timeStampSnapShot.getValue();
//
//                        String type = timeStampKey.get("type");
//                        String pointsStr = timeStampKey.get("points");
//                        String billAmountStr = timeStampKey.get("billAmount");
//                        String discountAmountStr = timeStampKey.get("disCountAmount");
//
//
//                        int billAmount = Integer.parseInt(billAmountStr);
//                        Utility.totalBillAmount = Utility.totalBillAmount + billAmount;
//
//                    }
//
//                    Integer totalPoints = Utility.totalEarnPoints - Utility.totalRedeemPoints;
//                    Integer totalBillAmount = Utility.totalBillAmount;
//                    Integer totalDiscountmount = Utility.totalDiscount;
//                }
//
//                @Override
//                public void onCancelled(DatabaseError databaseError) {
//
//                }
//            });
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//    }
}
