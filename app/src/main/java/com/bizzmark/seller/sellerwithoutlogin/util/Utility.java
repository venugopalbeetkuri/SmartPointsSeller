package com.bizzmark.seller.sellerwithoutlogin.util;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.os.Build;
import android.widget.TextView;

/*import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;*/
import com.google.gson.Gson;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public static Activity getActivity() {
        Class activityThreadClass = null;
        try {
            activityThreadClass = Class.forName("android.app.ActivityThread");

        Object activityThread = activityThreadClass.getMethod("currentActivityThread").invoke(null);
        Field activitiesField = activityThreadClass.getDeclaredField("mActivities");
        activitiesField.setAccessible(true);

        Map<Object, Object> activities = (Map<Object, Object>) activitiesField.get(activityThread);
        if (activities == null)
            return null;

        for (Object activityRecord : activities.values()) {
            Class activityRecordClass = activityRecord.getClass();
            Field pausedField = activityRecordClass.getDeclaredField("paused");
            pausedField.setAccessible(true);
            if (!pausedField.getBoolean(activityRecord)) {
                Field activityField = activityRecordClass.getDeclaredField("activity");
                activityField.setAccessible(true);
                Activity activity = (Activity) activityField.get(activityRecord);
                return activity;
            }
        }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean isAppIsInBackground(Context context) {
        boolean isInBackground = true;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (String activeProcess : processInfo.pkgList) {
                        if (activeProcess.equals(context.getPackageName())) {
                            isInBackground = false;
                        }
                    }
                }
            }
        } else {
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
            ComponentName componentInfo = taskInfo.get(0).topActivity;
            if (componentInfo.getPackageName().equals(context.getPackageName())) {
                isInBackground = false;
            }
        }

        return isInBackground;
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
