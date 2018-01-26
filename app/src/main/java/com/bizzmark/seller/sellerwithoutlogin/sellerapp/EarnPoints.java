package com.bizzmark.seller.sellerwithoutlogin.sellerapp;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bizzmark.seller.sellerwithoutlogin.R;
import com.bizzmark.seller.sellerwithoutlogin.Reports.Last_Ten_Transactions.LastTenTrans;
import com.bizzmark.seller.sellerwithoutlogin.WifiDirectReceive;
import com.bizzmark.seller.sellerwithoutlogin.db.AcknowledgePoints;

import com.bizzmark.seller.sellerwithoutlogin.db.PointsBO;

import com.bizzmark.seller.sellerwithoutlogin.wifidirect_new.service.FileTransferService;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import static com.bizzmark.seller.sellerwithoutlogin.login.Login.SELLER_BRANCHID;
import static com.bizzmark.seller.sellerwithoutlogin.login.Login.SELLER_STOREID;
import static com.bizzmark.seller.sellerwithoutlogin.login.Login.SELLER_STORENAE;

public class EarnPoints extends AppCompatActivity {

    Intent serviceIntent;
    TextView earnPoints,billAmount, earnHeader;

    final static String log = "Seller app";

    PointsBO pointsBO=null;
    String acknowledgePoints;
    public int i=0;

    Button earnAcceptButton;
    /*String Used in jsonobject to separete data */
    String device_id,store_name,bill_amount,date_time,earn_type;
    /*Strings used in calculatingEarnPoints()*/
    String billamount,points;

    /*String for transaction Id*/
    String transId;

    /*Strings used in insertEarnTransToDB()*/
    String status_type, response;

    String calStatusType,calResponse;

    /*Strings for URL storing*/
    private String CalPointsUrl,InsertUrl;

    /*string for ack response*/
    public String ackResponse;

    /*This string for storing data received from customer and used at onCreate()*/
    String earnString = null;

    /*String used for storing Customer Remote Address and used at onCreate()*/
    String remoteMacAddress = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_earn_points);

        earnHeader = (TextView) findViewById(R.id.earnHeader);
        earnHeader.setText(SELLER_STORENAE);

        /*getting data from DeviceDetailFragment class in onPostExecute()*/
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

        calculatingEarnPoints();
        addListenerOnAcceptButton();
        addListenerOnCancelButton();

    }

    /*back method*/
    private void backbut(){
        EarnPoints.super.onBackPressed();
    }


    /*Calucating points for given bill amount*/
    public void calculatingEarnPoints(){
        CalPointsUrl = "http://bizzmark.in/smartpoints/seller-api/preview-make-earn-transaction?storeId="+SELLER_STOREID+"&customerDeviceId="+device_id+"&billAmount="+bill_amount;
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                CalPointsUrl, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    calStatusType = jsonObject.getString("status_type");
                    if (calStatusType.equalsIgnoreCase("success")) {
                        billamount = jsonObject.getString("bill_amount");
                        points = jsonObject.getString("earned_points");
                        billAmount.setText(billamount);
                        earnPoints.setText(points);
                    }
                    else if (calStatusType.equalsIgnoreCase("error")){
                        calResponse = jsonObject.getString("response");
                        ackResponse = calResponse;
                        sendAcknowledgement(false);
                        try{
                            new AlertDialog.Builder(EarnPoints.this)
                                    .setTitle("Error")
                                    .setIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.cancel, null))
                                    .setMessage(calResponse)
                                    .setCancelable(false)
                                    .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                            finish();
//                                            Intent i =new Intent(EarnPoints.this, WifiDirectReceive.class);
//                                            startActivity(i);
                                        }
                                    }).create().show();
                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                try {
                    new AlertDialog.Builder(EarnPoints.this)
                            .setTitle("Something Wrong While Calculating Points")
                            //.setIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.error, null))
                            .setMessage("Please ensure Internet connection")
                            .setCancelable(false)
                            .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    calculatingEarnPoints();
                                }
                            })
                            .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    sendAcknowledgement(false);
//                                    Intent i = new Intent(EarnPoints.this,WifiDirectReceive.class);
//                                    startActivity(i);
                                    finish();
                                }
                            }).create().show();
                }
                catch (Exception e){
                    e.printStackTrace();
                }
//                Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    /*Inseting Data into database*/
    public void insertEarnTransToDB(){
        calculatingEarnPoints();
        InsertUrl = "http://bizzmark.in/smartpoints/seller-api/make-earn-transaction?branchId="+SELLER_BRANCHID+"&customerDeviceId="+device_id+"&billAmount="+billamount;
        StringRequest insertRequest = new StringRequest(Request.Method.GET,
                InsertUrl, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject object = new JSONObject(s);
                    status_type = object.getString("status_type");

                    //earned_points = object.getString("earned_points");
                    if (status_type.equalsIgnoreCase("success") ){
                        transId = object.getString("transaction_id");
//                        earnAcceptButton.setVisibility(View.GONE);
                        sendAcknowledgement(true);
                        try {
                            new AlertDialog.Builder(EarnPoints.this)
                                    .setTitle("Transaction Successful")
                                    .setIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.checked, null))
                                    .setMessage("If the Customer has not Received Acknowledgement please show him this message")
                                    .setCancelable(false)
                                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                            finish();
                                        }
                                    })
                                    .setNegativeButton("View Transaction", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                            finish();
                                            startActivity(new Intent(EarnPoints.this, LastTenTrans.class));
                                        }
                                    }).create().show();
                           // finish();
                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }

//                        Toast.makeText(getApplicationContext(),"Record Inserted",Toast.LENGTH_LONG).show();
                    }
                    else if (status_type.equalsIgnoreCase("error")){
                        response = object.getString("response");
                        ackResponse = response;
                        sendAcknowledgement(false);
                        try {
                            new AlertDialog.Builder(EarnPoints.this)
                                    .setTitle("Transaction Canceled")
                                    .setIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.cancel, null))
                                    .setMessage(response+"\n If Customer won't Receive Acknowledgment show this Message")
                                    .setCancelable(false)
                                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                            finish();
                                        }
                                    }).create().show();
                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }
//                        Toast.makeText(getApplicationContext(),"Failed to Inserting",Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try {
                    new AlertDialog.Builder(EarnPoints.this)
                            .setTitle("Error")
                            //.setIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.error, null))
                            .setMessage("Something went wrong with Internet connection \n Please ensure Internet connection")
                            .setCancelable(false)
                            .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    insertEarnTransToDB();
                                }
                            })
                            .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    sendAcknowledgement(false);
                                    finish();
                                }
                            }).create().show();
                }
                catch (Exception e){
                    e.printStackTrace();
                }
//                Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        insertRequest.setRetryPolicy(new DefaultRetryPolicy(
                300000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(insertRequest);
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

    /* Enabling button fuction to accept the transaction*/
    private void addListenerOnAcceptButton() {
        earnAcceptButton=(Button)findViewById(R.id.earnAcceptButton);
        final Button earnCancelButton = (Button) findViewById(R.id.earnCancelButton);
        earnAcceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                arg0.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.animation));
                earnAcceptButton.setVisibility(View.GONE);
                earnCancelButton.setVisibility(View.GONE);
                /*saving to database*/
                insertEarnTransToDB();

         // Send acknowledgement to client.
           /*     sendAcknowledgement(true);*/
//                finish();
            }
        });
    }

    /*Method for Intializing Acknowledgement to send customer*/
    private void sendAcknowledgement(boolean success){
          AcknowledgePoints ack = new AcknowledgePoints();
        if(success) {
            String status = "success";
            ack.setStatus(status);
            ack.setDeviceId(device_id);
            ack.setStoreName(SELLER_STORENAE);
            ack.setBillAmount(bill_amount);
            ack.setPoints(points);
            ack.setType(earn_type);
            ack.setTime(date_time);
            ack.setBranchId(SELLER_BRANCHID);
            ack.setStoreId(SELLER_STOREID);
            ack.setTransId(transId);
            ack.setResponse(ackResponse);
        } else {
            String status = "failure";
            ack.setStatus(status);
            ack.setDeviceId(device_id);
            ack.setStoreName(SELLER_STORENAE);
            ack.setBillAmount(bill_amount);
            ack.setPoints(points);
            ack.setType(earn_type);
            ack.setTime(date_time);
            ack.setBranchId(SELLER_BRANCHID);
            ack.setStoreId(SELLER_STOREID);
            ack.setTransId(transId);
            ack.setResponse(ackResponse);
        }

        Gson gson = new Gson();
        acknowledgePoints = gson.toJson(ack);

        /*Calling sendMessaege() for sending ack to cuatomer*/
        sendMessage();
    }

    /*Method fro sendMessage() used to sending ack message to customer*/
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
