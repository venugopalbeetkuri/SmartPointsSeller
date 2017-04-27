package com.bizzmark.seller.sellerwithoutlogin.sellerapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import com.bizzmark.seller.sellerwithoutlogin.R;
import com.bizzmark.seller.sellerwithoutlogin.WifiDirectReceive;
import com.bizzmark.seller.sellerwithoutlogin.db.AcknowledgePoints;
import com.bizzmark.seller.sellerwithoutlogin.db.PointsBO;
import com.bizzmark.seller.sellerwithoutlogin.db.RedeemAcknowledgement;
import com.bizzmark.seller.sellerwithoutlogin.util.Utility;
import com.bizzmark.seller.sellerwithoutlogin.wifidirect_new.service.FileTransferService;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class RedeemPoints extends AppCompatActivity {

    TextView redeemPoints,billAmount,discountAmount,newBill;

//    String device_id,store_name,discount_amount,points,date_time,earn_type;

    String deviceid,storename,bill_amount,points_earn,datetime,earntype;

    Calendar c=Calendar.getInstance();

    Intent serviceIntent;

    SimpleDateFormat df=new SimpleDateFormat("yyyyMMddHHmmssSSS");

    final static String Log="Seller app";
    PointsBO pointsBO=null;
    String redeemString=null;
    String jsonAck=null;
    String remoteMacAddress=null;
    String discount_Amount = "null";
    String newBillAmount = "null";

    String acknowledgePoints;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_redeem_points);

        Intent intent=getIntent();
        redeemString=intent.getStringExtra("earnRedeemString");

        remoteMacAddress = intent.getStringExtra("remoteAddress");

    //    newBillAmount=intent.getStringExtra("newBillAmount");

        try {
            JSONObject jsonObject = new JSONObject(redeemString);
            deviceid = jsonObject.getString("deviceId");
            storename = jsonObject.getString("storeName");
            earntype = jsonObject.getString("type");
            datetime = jsonObject.getString("time");
            points_earn = jsonObject.getString("points");
            bill_amount = jsonObject.getString("disCountAmount");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Gson gson=new Gson();

        pointsBO=gson.fromJson(redeemString,PointsBO.class);

        billAmount=(TextView)findViewById(R.id.billAmount);
        redeemPoints=(TextView)findViewById(R.id.redeemPoints);
        discountAmount=(TextView)findViewById(R.id.discountAmount);
        newBill=(TextView)findViewById(R.id.newBillAmount);

//        billAmount.setText(pointsBO.getBillAmount());
//        redeemPoints.setText(pointsBO.getPoints());
//        discountAmount.setText(pointsBO.getPoints());
//        newBill.setText(newBillAmount);

        addListenerOnAcceptButton();
        addListenerOnCancelButton();

    }

    public void calNewBillAmount(){
        String newBillAmount=null;
    }

    private void addListenerOnCancelButton() {

        Button redeemCancelButton=(Button)findViewById(R.id.redeemCancelButton);
        redeemCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                arg0.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.animation));
                sendAcknowledgement(false);
                finish();
            }
        });
    }

    private void addListenerOnAcceptButton() {

        Button earnButton=(Button)findViewById(R.id.redeemButton);

        earnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                arg0.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.animation));

                //save to database
             //   saveToDataBase();

                // Send acknowledgement to customer.
                sendAcknowledgement(true);
                finish();
            }
        });
    }

    private void sendAcknowledgement(boolean success){

        RedeemAcknowledgement ack = new RedeemAcknowledgement();
//        ack.getNewBillAmount();
//        ack.getOldBillAmount();

        if (success){
            // ack=new AcknowledgePoints(redeemString);
            String status = "success";
//            ack = new AcknowledgePoints(status, deviceid, storename, billamount, points, type, time);
            ack.setStatus(status);
            ack.setDeviceId(deviceid);
            ack.setStoreName(storename);
            ack.setOldBillAmount(bill_amount);
//            ack.setPoints(points_earn);
            ack.setType(earntype);
            ack.setTime(datetime);
            ack.setDiscountAmount(discount_Amount);
            ack.setNewBillAmount(newBillAmount);
        }else {
            //ack=new AcknowledgePoints(redeemString);
            String status = "failure";
//            ack = new AcknowledgePoints(status, deviceid, storename, billamount, points, type, time);
            ack.setStatus(status);
            ack.setDeviceId(deviceid);
            ack.setStoreName(storename);
            ack.setOldBillAmount(bill_amount);
//            ack.setPoints(points_earn);
            ack.setType(earntype);
            ack.setTime(datetime);
            ack.setDiscountAmount(discount_Amount);
            ack.setNewBillAmount(newBillAmount);
        }

//        Gson gson= Utility.getGsonObject();
//        jsonAck=gson.toJson(ack);
        Gson gson = new Gson();
        acknowledgePoints = gson.toJson(ack);
        sendMessage();

        // After sending acknowledgement move to first screen.

//        Intent itt=new Intent(this, WifiDirectReceive.class);
//        startActivity(itt);
    }

    private void sendMessage(){

        try {

            boolean instance= FileTransferService.isInstanceCreated();
            if (instance==false){
                serviceIntent = new Intent(RedeemPoints.this,FileTransferService.class);
            }

            remoteMacAddress = getIntent().getStringExtra("GroupOwnerAddress");

            // Send msg to customer.
            serviceIntent.setAction(FileTransferService.ACTION_SEND_FILE);
            serviceIntent.putExtra(FileTransferService.EXTRAS_GROUP_OWNER_ADDRESS, remoteMacAddress);

            serviceIntent.putExtra(FileTransferService.MESSAGE, acknowledgePoints);

            android.util.Log.i("bizzmark", "Customer Address: " + remoteMacAddress);
            serviceIntent.putExtra(FileTransferService.EXTRAS_GROUP_OWNER_PORT, 9999);

            // Start service.
            startService(serviceIntent);
        }
        catch (Throwable th){
            th.printStackTrace();
        }
    }
}
