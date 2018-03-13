package com.bizzmark.seller.sellerwithoutlogin.fcm;

/**
 * Created by Venu on 1/6/2016.
 */

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.bizzmark.seller.sellerwithoutlogin.R;
import com.bizzmark.seller.sellerwithoutlogin.Splash.Splash;
import com.bizzmark.seller.sellerwithoutlogin.sellerapp.EarnPoints;
import com.bizzmark.seller.sellerwithoutlogin.sellerapp.RedeemPoints;
import com.bizzmark.seller.sellerwithoutlogin.util.Utility;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;




public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MessagingService";
    private int mNotificationType;


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Map<String, String> params = remoteMessage.getData();

        if (params.size() > 0) {

            //Calling method to generate notification
                JSONObject obj=new JSONObject(params);
                if(Utility.isAppIsInBackground(this))
                    sendNotification(obj);
                else{
                   // Log.v("Testing","App is in foreground");
                    String message;
                    String  billAmount = obj.optString("billAmount");
                    String title;
                    String type= null;
                    try {
                        type = obj.getString("type");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    message="Customer is requesting "+type+" For the bill amount "+billAmount;
                    title=type+" Request";
                    final Intent intent;
                    if(type!=null && type.equals("redeem")){
                        intent = new Intent(this, RedeemPoints.class);
                        intent.putExtra("earnRedeemString", obj.toString());
                        intent.putExtra("FromNotification",true);
                    }else{
                        intent = new Intent(this, EarnPoints.class);
                        intent.putExtra("earnRedeemString", obj.toString());
                        intent.putExtra("FromNotification",true);
                    }


                    AlertDialog.Builder dialogue=new AlertDialog.Builder(Utility.getActivity());
                    dialogue.setMessage(message);
                    dialogue.setTitle(title);
                    dialogue.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(intent);
                        }
                    });
                    dialogue.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    dialogue.show();
                }
        } else {
            Log.e(TAG, "Push Failure:: ");
        }
    }


    private void sendNotification(JSONObject obj) {
        PendingIntent pendingIntent = null;


        try {
            String message;
            String  billAmount = obj.optString("billAmount");
            String title;
            String type=obj.getString("type");
            message="Customer is requesting "+type+" For the bill amount "+billAmount;
            title=type+" Request";
            Intent intent;
            if(type.equals("redeem")){
                intent = new Intent(this, RedeemPoints.class);
                intent.putExtra("earnRedeemString", obj.toString());
                intent.putExtra("FromNotification",true);
            }else{
                intent = new Intent(this, EarnPoints.class);
                intent.putExtra("earnRedeemString", obj.toString());
                intent.putExtra("FromNotification",true);
            }
           // Intent intent = new Intent(this, Splash.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            pendingIntent = PendingIntent.getActivity(this, 0, intent,
                    PendingIntent.FLAG_ONE_SHOT);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder;
        notificationBuilder= new NotificationCompat.Builder(this)
                    .setSmallIcon(R.drawable.smartpoints_logo)
                    .setContentTitle(title)
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.smartpoints_logo))
                    .setContentText(message)
                    .setAutoCancel(true)
                    .setStyle(new NotificationCompat.BigTextStyle().setBigContentTitle(title))
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                    .setSound(defaultSoundUri)
                    .setContentIntent(pendingIntent);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notificationBuilder.build());

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}