package com.bizzmark.seller.sellerwithoutlogin.sellerapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bizzmark.seller.sellerwithoutlogin.R;
import com.bizzmark.seller.sellerwithoutlogin.db.AcknowledgePoints;
import com.bizzmark.seller.sellerwithoutlogin.db.AsyncTask.DataBaseBackgroundTask;
import com.bizzmark.seller.sellerwithoutlogin.db.PointsBO;
import com.bizzmark.seller.sellerwithoutlogin.db.Retrofit.InsertData;
import com.bizzmark.seller.sellerwithoutlogin.util.Utility;
import com.bizzmark.seller.sellerwithoutlogin.wifidirect_new.service.FileTransferService;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static com.bizzmark.seller.sellerwithoutlogin.WifiDirectReceive.storeName;

public class EarnPoints extends AppCompatActivity {

    Intent serviceIntent;
    TextView earnPoints,billAmount, earnHeader;

    Button savetoDB;

    final static String log = "Seller app";

    int discountpoints;
    String earn_Points, failureString;

    PointsBO pointsBO=null;
    String acknowledgePoints;

    public int i=0;

    String device_id,store_name,bill_amount,points_earn,date_time,earn_type;

    String deviceid,storename,billamount,points,time,type;

    //public static final String ROOT_URL="https://wwwbizzmarkin.000webhostapp.com/";

    Calendar c = Calendar.getInstance();
    SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmssSSS");

    private String CalPointsUrl;
    private String URL_DATA;
    String earnString = null;
//    String ackString = null;
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
                v.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.animation));
//                saveData();
            }
        });

        earnHeader = (TextView) findViewById(R.id.earnHeader);
        earnHeader.setText(storeName);

        Intent intent = getIntent();
        earnString = intent.getStringExtra("earnRedeemString");
        remoteMacAddress = intent.getStringExtra("remoteAddress");

        /* Creating Gson for External Use*/

        Gson gson = new Gson();
        pointsBO = gson.fromJson(earnString, PointsBO.class);
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

       // calPoints();

        calculatingEarnPoints();

        addListenerOnAcceptButton();
        addListenerOnCancelButton();

    }

    public void calculatingEarnPoints(){

        CalPointsUrl = "http://35.154.104.54/smartpoints/seller-api/preview-make-earn-transaction?storeId=1&customerDeviceId="+device_id+"&billAmount="+bill_amount;

        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                CalPointsUrl, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    billamount = jsonObject.getString("bill_amount");
                    points = jsonObject.getString("earned_points");
                    billAmount.setText(billamount);
                    earnPoints.setText(points);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
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

        i=Integer.parseInt(bill_amount);
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
  //              savetoDatabase();

         // Send acknowledgement to client.
                sendAcknowledgement(true);

                finish();
            }
        });

    }

//    String jsonACK = null;

    private void sendAcknowledgement(boolean success){

          AcknowledgePoints ack = new AcknowledgePoints();
        if(success) {
            String status = "success";
//            ack = new AcknowledgePoints(status, deviceid, storename, billamount, points, type, time);
            ack.setStatus(status);
            ack.setDeviceId(device_id);
            ack.setStoreName(store_name);
            ack.setBillAmount(bill_amount);
            ack.setPoints(earn_Points);
            ack.setType(earn_type);
            ack.setTime(date_time);
        } else {
            String status = "failure";
//          ack = new AcknowledgePoints(status, deviceid, storename, billamount, points, type, time);
            ack.setStatus(status);
            ack.setDeviceId(device_id);
            ack.setStoreName(store_name);
            ack.setBillAmount(bill_amount);
            ack.setPoints(earn_Points);
            ack.setType(earn_type);
            ack.setTime(date_time);
        }

        Gson gson = new Gson();
        acknowledgePoints = gson.toJson(ack);
//        Gson gson = Utility.getGsonObject();
//        jsonACK = gson.toJson(ack);
//        Gson gson = new Gson(ack,AcknowledgePoints);
        sendMessage();

    }
    private void sendMessage(){

        try {

            boolean instance=FileTransferService.isInstanceCreated();
             if (instance==false){
                 serviceIntent = new Intent(EarnPoints.this,FileTransferService.class);
             }


            remoteMacAddress = getIntent().getStringExtra("GroupOwnerAddress");

                // Send msg to customer.
                serviceIntent.setAction(FileTransferService.ACTION_SEND_FILE);
                serviceIntent.putExtra(FileTransferService.EXTRAS_GROUP_OWNER_ADDRESS, remoteMacAddress);

                serviceIntent.putExtra(FileTransferService.MESSAGE,  acknowledgePoints);

                Log.i("bizzmark", "Customer Address: " + remoteMacAddress);
                serviceIntent.putExtra(FileTransferService.EXTRAS_GROUP_OWNER_PORT, 9999);

                // Start service.
            startService(serviceIntent);

            }
        catch (Throwable th){
            th.printStackTrace();
        }

    }
    //saving data using AsyncTask

//    private void savetoDatabase(){
//
//        deviceid =device_id ;
//        storename = store_name;
//        billamount = bill_amount;
//        points = earn_Points;
//        time = date_time;
//        type=earn_type;
//
//        DataBaseBackgroundTask dataBaseBackgroundTask=new DataBaseBackgroundTask(this);
//        dataBaseBackgroundTask.execute(deviceid,storename,billamount,points,type,time);
//
//    }

    //saving data to mysql using retrofit

//    private void saveData(){
//        //here we will handle the Http request to insert user to mysql db
//        //creating RestAdapter
//        deviceid =device_id ;
//        storename = store_name;
//        billamount = bill_amount;
//        points = earn_Points;
//        time = date_time;
//        type=earn_type;
//
//        RestAdapter adapter=new RestAdapter.Builder()
//                .setEndpoint(ROOT_URL)//setting the Root URL
//                .build(); //Finally building the adapter
//
//        //creating object for our interface
//
//        InsertData data=adapter.create(InsertData.class);
//
//        //Defing the method insertdata of our interface
//
//        data.saveData(
//                //passing values
//                deviceid, storename, billamount, points, type, time,
//
//                //creating ananymous callback
//                new Callback<Response>() {
//                    @Override
//                    public void success(Response result, Response response) {
//
//                        //on success we will read the servers output using bufferedreader
//                        //creating a bufferedreader object
//
//                        BufferedReader reader=null;
//
//                        //An string to store output from the server
//
//                        String output="";
//
//                        try {
//                            //initialize buffered reader
//
//                            reader=new BufferedReader(new InputStreamReader(result.getBody().in()));
//
//                            //reading output in the string
//                            output=reader.readLine();
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//
//                        //Displaying the output as a toast
//
//                        Toast.makeText(EarnPoints.this,output,Toast.LENGTH_LONG).show();
//
//                    }
//
//                    @Override
//                    public void failure(RetrofitError error) {
//                        //If any error occured displaying the error toast
//                        Toast.makeText(EarnPoints.this,error.toString(),Toast.LENGTH_LONG).show();
//                    }
//                });
//
//    }
}
