package com.bizzmark.seller.sellerwithoutlogin.sellerapp;

import android.content.Intent;
import android.net.wifi.p2p.WifiP2pInfo;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bizzmark.seller.sellerwithoutlogin.R;
import com.bizzmark.seller.sellerwithoutlogin.db.AcknowledgePoints;
import com.bizzmark.seller.sellerwithoutlogin.db.DataBaseBackgroundTask;
import com.bizzmark.seller.sellerwithoutlogin.db.PointsBO;
import com.bizzmark.seller.sellerwithoutlogin.wifidirect_new.DeviceDetailFragment;
import com.bizzmark.seller.sellerwithoutlogin.wifidirect_new.service.FileTransferService;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class EarnPoints extends AppCompatActivity {

    Intent serviceIntent;
    TextView earnPoints,billAmount;

    Button savetoDB;

 //   String ep;
    final static String log = "Seller app";
//    WifiP2pInfo info;

    int discountpoints;

    PointsBO pointsBO=null;

    String device_id,store_name,bill_amount,points_earn,date_time,earn_type;


    String deviceid,storename,billamount,points,time,type;

    Calendar c = Calendar.getInstance();
    SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmssSSS");

    String earnString = null;
    String remoteMacAddress = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_earn_points);

        savetoDB=(Button)findViewById(R.id.savetoDB);
        savetoDB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savetoDatabase();
      //          finish();
            }
        });


        //info=WifiP2pInfo;
        Intent intent = getIntent();
        earnString = intent.getStringExtra("earnRedeemString");
     //   remoteMacAddress = intent.getStringExtra("GroupOwnerAddress");

//        Gson gson = new Gson();
//        pointsBO = gson.fromJson(earnString,PointsBO.class);
//        bill_amount = pointsBO.getBillAmount();
//        device_id = pointsBO.getDeviceId();
//        store_name = pointsBO.getStoreName();
//        points_earn = pointsBO.getPoints();
//        date_time = pointsBO.getTime();
//        earn_type=pointsBO.setType("earn");

        try{
            JSONObject obj= new JSONObject(earnString);
            device_id =obj.getString("deviceId");
            earn_type=obj.getString("type");
            bill_amount=obj.getString("billAmount");
            store_name=obj.getString("storeName");
            date_time=obj.getString("time");

        }catch (Exception e){
            e.printStackTrace();
        }



//        Toast.makeText(this, "Json :" +device_id+store_name+bill_amount+points_earn+date_time, Toast.LENGTH_SHORT).show();


  //      String storeName = pointsBO.getStoreName();

        earnPoints=(TextView)findViewById(R.id.earnPoints);
        billAmount = (TextView) findViewById(R.id.billamount);

        billAmount.setText(bill_amount);
        //calPoints();
       // ep=Integer.toString(discountpoints);
     //   earnPoints.setText(pointsBO.getPoints());
    //    billAmount.setText(pointsBO.getBillAmount());

        addListenerOnAcceptButton();
        addListenerOnCancelButton();
      //  addListenerOnsaveToDBButton();
    }


    private void addListenerOnCancelButton() {

        Button earnCancelButton = (Button) findViewById(R.id.earnCancelButton);
        earnCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                arg0.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.animation));

                // Send acknowledgement to client.
                sendAcknowledgement(false);

                finish();

            }
        });

    }

    //calculating points to billamount

    public void calPoints(){

        int i=Integer.parseInt(earnString);
        discountpoints=(i*10/100);
    }

    private void addListenerOnAcceptButton() {

        Button earnAcceptButton=(Button)findViewById(R.id.earnAcceptButton);
        earnAcceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                arg0.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.animation));

   //             savetoDatabase();

           // Send acknowledgement to client.
                sendAcknowledgement(true);

                finish();
            }
        });

    }

    String jsonACK = null;

    private void sendAcknowledgement(boolean success){

          AcknowledgePoints ack = null;

  //      String sendText="hai";

        if(success) {

            ack = new AcknowledgePoints("success", earnString);
            sendMessage();
        } else {

            ack = new AcknowledgePoints("failure", earnString);
        }

       // Gson gson = Util

      //  sendMessage();
    }
    private void sendMessage(){

        try {

            boolean instance=FileTransferService.isInstanceCreated();
             if (instance=false){
                 serviceIntent = new Intent(EarnPoints.this,FileTransferService.class);
             }

         //   Intent serviceIntent = new Intent(EarnPoints.this,FileTransferService.class);

                // Send msg to customer.
                serviceIntent.setAction(FileTransferService.ACTION_SEND_FILE);
                serviceIntent.putExtra(FileTransferService.EXTRAS_GROUP_OWNER_ADDRESS, remoteMacAddress);

           String sendText="hai";

                serviceIntent.putExtra(FileTransferService.MESSAGE,  sendText);

                Log.i("bizzmark", "Customer Address: " + remoteMacAddress);
                serviceIntent.putExtra(FileTransferService.EXTRAS_GROUP_OWNER_PORT, 9999);

                // Start service.
            startService(serviceIntent);

            }
        catch (Throwable th){
            th.printStackTrace();
        }

    }

    private void savetoDatabase(){

        deviceid =device_id ;
        storename = store_name;
        billamount = bill_amount;
        points = "0";
        time = date_time;
    //    type=earn_type;


        DataBaseBackgroundTask dataBaseBackgroundTask=new DataBaseBackgroundTask(this);
        dataBaseBackgroundTask.execute(deviceid,storename,billamount,points,time);
     //   finish();

    }
}
