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

    final static String log = "Seller app";

    int discountpoints;
    String earn_Points;

    PointsBO pointsBO=null;

    String device_id,store_name,bill_amount,points_earn,date_time,earn_type;

//    String device_Id,store_Name,bill_Amount,points_Earn,date_Time,earn_Type;

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

        /*save button to save data to database*/
        savetoDB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savetoDatabase();
            }
        });


        Intent intent = getIntent();
        earnString = intent.getStringExtra("earnRedeemString");

        /* Creating Gson for External Use*/

//        Gson gson = new Gson();
//        pointsBO = gson.fromJson(earnString,PointsBO.class);
//        bill_Amount = pointsBO.getBillAmount();
//        device_Id = pointsBO.getDeviceId();
//        store_Name = pointsBO.getStoreName();
//        points_Earn = pointsBO.getPoints();
//        date_Time = pointsBO.getTime();
//        earn_Type=pointsBO.setType("earn");

        /*creating Json object for converting bundle to individual strings*/

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

        earnPoints=(TextView)findViewById(R.id.earnPoints);
        billAmount = (TextView) findViewById(R.id.billamount);

        calPoints();

        billAmount.setText(bill_amount);
        earnPoints.setText(earn_Points);

        addListenerOnAcceptButton();
        addListenerOnCancelButton();

    }

/*enbling button function to cancel the transaction*/

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

    /*calculating points to billamount*/

    public void calPoints(){

        int i=Integer.parseInt(bill_amount);
        discountpoints=(i*10/100);
        earn_Points=Integer.toString(discountpoints);
    }

/* Enabling button fuction to accept the the transaction*/

    private void addListenerOnAcceptButton() {

        Button earnAcceptButton=(Button)findViewById(R.id.earnAcceptButton);
        earnAcceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                arg0.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.animation));
                /*saving to database*/
                savetoDatabase();

         // Send acknowledgement to client.
                sendAcknowledgement(true);

                finish();
            }
        });

    }


    private void sendAcknowledgement(boolean success){

          AcknowledgePoints ack = null;

        if(success) {

            ack = new AcknowledgePoints("success", earnString);
            sendMessage();
        } else {

            ack = new AcknowledgePoints("failure", earnString);
        }
    }
    private void sendMessage(){

        try {

            boolean instance=FileTransferService.isInstanceCreated();
             if (instance=false){
                 serviceIntent = new Intent(EarnPoints.this,FileTransferService.class);
             }

                // Send msg to customer.
                serviceIntent.setAction(FileTransferService.ACTION_SEND_FILE);
                serviceIntent.putExtra(FileTransferService.EXTRAS_GROUP_OWNER_ADDRESS, remoteMacAddress);

                serviceIntent.putExtra(FileTransferService.MESSAGE,  earnString);

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
        points = earn_Points;
        time = date_time;
        type=earn_type;

        DataBaseBackgroundTask dataBaseBackgroundTask=new DataBaseBackgroundTask(this);
        dataBaseBackgroundTask.execute(deviceid,storename,billamount,points,type,time);

    }
}
