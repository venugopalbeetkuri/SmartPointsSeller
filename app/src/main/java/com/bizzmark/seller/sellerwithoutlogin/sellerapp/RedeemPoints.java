package com.bizzmark.seller.sellerwithoutlogin.sellerapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.wifi.WifiManager;
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
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bizzmark.seller.sellerwithoutlogin.R;
import com.bizzmark.seller.sellerwithoutlogin.Reports.Last_Ten_Transactions.LastTenTrans;
import com.bizzmark.seller.sellerwithoutlogin.WifiDirectReceive;
import com.bizzmark.seller.sellerwithoutlogin.db.PointsBO;
import com.bizzmark.seller.sellerwithoutlogin.db.RedeemAcknowledgement;

import com.bizzmark.seller.sellerwithoutlogin.wifidirect_new.service.FileTransferService;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import static com.bizzmark.seller.sellerwithoutlogin.login.Login.SELLER_BRANCHID;
import static com.bizzmark.seller.sellerwithoutlogin.login.Login.SELLER_STOREID;
import static com.bizzmark.seller.sellerwithoutlogin.login.Login.SELLER_STORENAE;

public class RedeemPoints extends AppCompatActivity {

    TextView redeemPoints,billAmount,discountAmount,newBill, redeemHeader;

    String deviceid,storename,bill_amount,points_earn,datetime,earntype;

    /*Strings for URL storing*/
    private String CalRedeemPointsUrl,InsertRedeemUrl;

    /*Strings used in validatingRedeemPoints()*/
    String redeemedBillamount,redeemedPoints,originalBillAmount,discountedAmount,availablePoints;

    /*Strings used in validatingRedeemPoints()*/
    String status_type, response;

    /*String for transId*/
    String transId;

    /*String for response*/
    public String ackResponse;

    Intent serviceIntent;

    final static String Log="Seller app";
    PointsBO pointsBO=null;
    String redeemString=null;
    String remoteMacAddress=null;

    String acknowledgePoints;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_redeem_points);
        redeemHeader = (TextView) findViewById(R.id.redeemHeader);
        redeemHeader.setText(SELLER_STORENAE);

        Intent intent=getIntent();
        redeemString=intent.getStringExtra("earnRedeemString");

        remoteMacAddress = intent.getStringExtra("remoteAddress");


        try {
            JSONObject jsonObject = new JSONObject(redeemString);
            deviceid = jsonObject.getString("deviceId");
            storename = jsonObject.getString("storeName");
            earntype = jsonObject.getString("type");
            datetime = jsonObject.getString("time");
            points_earn = jsonObject.getString("points");
            bill_amount = jsonObject.getString("billAmount");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Gson gson=new Gson();

        pointsBO=gson.fromJson(redeemString,PointsBO.class);

        billAmount=(TextView)findViewById(R.id.billAmount);
        redeemPoints=(TextView)findViewById(R.id.redeemPoints);
        discountAmount=(TextView)findViewById(R.id.discountAmount);
        newBill=(TextView)findViewById(R.id.newBillAmount);

        validatingRedeemPoints();

        addListenerOnAcceptButton();
        addListenerOnCancelButton();

    }
    /*Validating given points from user*/
    private void validatingRedeemPoints() {
        CalRedeemPointsUrl = "http://bizzmark.in/smartpoints/seller-api/preview-make-redeem-transaction?branchId="+SELLER_BRANCHID+"&customerDeviceId="+deviceid+"&billAmount="+bill_amount+"&wishedRedeemPoints="+points_earn;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, CalRedeemPointsUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {

                        try {
                            JSONObject redeemObject = new JSONObject(s);
                            status_type = redeemObject.getString("status_type");
                            if (status_type.equalsIgnoreCase("success")){
                                originalBillAmount = redeemObject.getString("bill_amount");
                                redeemedPoints = redeemObject.getString("wishedPoints");
                                discountedAmount = redeemObject.getString("discount");
                                redeemedBillamount = redeemObject.getString("discountedPrice");
                                availablePoints = redeemObject.getString("available_points");
                                billAmount.setText(originalBillAmount);
                                redeemPoints.setText(redeemedPoints);
                                discountAmount.setText(discountedAmount);
                                newBill.setText(redeemedBillamount);
                            }else if (status_type.equalsIgnoreCase("error")){
                                response = redeemObject.getString("response");
                                ackResponse = response;
                                originalBillAmount = redeemObject.getString("bill_amount");
                                redeemedPoints = redeemObject.getString("wishedPoints");
                                availablePoints = redeemObject.getString("available_points");
                                billAmount.setText(originalBillAmount);
                                redeemPoints.setText(redeemedPoints);
                                try {
                                    sendAcknowledgement(false);
                                    new AlertDialog.Builder(RedeemPoints.this)
                                            .setTitle("Invalid Transaction")
                                            .setIcon(ResourcesCompat.getDrawable(getResources(),R.drawable.cancel, null))
                                            .setMessage(response)
                                            .setCancelable(false)
                                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                    finish();
                                                }
                                            }).create().show();
                                }
                                catch (Exception e ){
                                    e.printStackTrace();
                                }
//                                Toast.makeText(getApplicationContext(),response,Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try {
                    new AlertDialog.Builder(RedeemPoints.this)
                            .setTitle("Something Wrong While Calculating Points")
                            //.setIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.error, null))
                            .setMessage("Please Check your Internet connection or It is an server problem please try again")
                            .setCancelable(false)
                            .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    validatingRedeemPoints();
                                }
                            })
                            .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    sendAcknowledgement(false);
//                                    Intent i = new Intent(RedeemPoints.this, WifiDirectReceive.class);
//                                    startActivity(i);
                                    finish();
                                }
                            }).create().show();
                } catch (Exception e){
                    e.printStackTrace();
                }
//                Toast.makeText(getApplicationContext(),"Some Thing Went Wrong Please Try Again",Toast.LENGTH_LONG).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                300000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }

    /*Inseting Data into database*/
    private void insertRedeemTransToDB(){
        validatingRedeemPoints();
        InsertRedeemUrl = "http://bizzmark.in/smartpoints/seller-api/make-redeem-transaction?branchId="+SELLER_BRANCHID+"&customerDeviceId="+deviceid+"&billAmount="+originalBillAmount+"&wishedRedeemPoints="+redeemedPoints;

        final StringRequest insertRequest = new StringRequest(Request.Method.GET, InsertRedeemUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        try {
                            JSONObject rdobj = new JSONObject(s);
                            status_type = rdobj.getString("status_type");
                            if (status_type.equalsIgnoreCase("success")){
                                transId = rdobj.getString("transaction_id");
                                sendAcknowledgement(true);
                                try {
                                    new AlertDialog.Builder(RedeemPoints.this)
                                            .setTitle("Transaction Successful")
                                            .setIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.checked, null))
                                            .setMessage(" If yhe Customer has not Received Acknowledgement please show this message")
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
                                                    startActivity(new Intent(RedeemPoints.this, LastTenTrans.class));

                                                }
                                            }).create().show();
                                }
                                catch (Exception e){
                                    e.printStackTrace();
                                }
//                                Toast.makeText(getApplicationContext(),"Record Inserted",Toast.LENGTH_LONG).show();
                            }
                            else if (status_type.equalsIgnoreCase("error")){
                                response = rdobj.getString("response");
                                ackResponse = response;
                                sendAcknowledgement(false);
                                try {
                                    new AlertDialog.Builder(RedeemPoints.this)
                                            .setTitle("Transaction Canceled")
                                            .setIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.cancel, null))
                                            .setMessage(response+"\nIf the Customer has not Received Acknowledgment show this Message")
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
//                                Toast.makeText(getApplicationContext(),response,Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try {
                    new AlertDialog.Builder(RedeemPoints.this)
                            .setTitle("Error")
                            //.setIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.error, null))
                            .setMessage("Something went wrong with Internet connection \n Please ensure Internet connection")
                            .setCancelable(false)
                            .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    insertRedeemTransToDB();
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
            }
        });
        RequestQueue requestqueue = Volley.newRequestQueue(getApplicationContext());
        insertRequest.setRetryPolicy(new DefaultRetryPolicy(
                300000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestqueue.add(insertRequest);
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

        final Button earnButton=(Button)findViewById(R.id.redeemButton);
        final Button redeemCancelButton=(Button)findViewById(R.id.redeemCancelButton);

        earnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                arg0.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.animation));

                earnButton.setVisibility(View.GONE);
                redeemCancelButton.setVisibility(View.GONE);
                //save to database
                insertRedeemTransToDB();
                // Send acknowledgement to customer.
                //sendAcknowledgement(true);

            }
        });
    }

    private void sendAcknowledgement(boolean success){

        RedeemAcknowledgement ack = new RedeemAcknowledgement();


        if (success){

            String status = "success";
            ack.setStatus(status);
            ack.setDeviceId(deviceid);
            ack.setStoreName(SELLER_STORENAE);
            ack.setOldBillAmount(originalBillAmount);
            ack.setType(earntype);
            ack.setTime(datetime);
            ack.setDiscountAmount(discountedAmount);
            ack.setNewBillAmount(redeemedBillamount);
            ack.setBranchId(SELLER_BRANCHID);
            ack.setStoreId(SELLER_STOREID);
            ack.setPoints(redeemedPoints);
            ack.setTransId(transId);
            ack.setResponse(ackResponse);
        }else {

            String status = "failure";
            ack.setStatus(status);
            ack.setDeviceId(deviceid);
            ack.setStoreName(SELLER_STORENAE);
            ack.setOldBillAmount(originalBillAmount);
            ack.setType(earntype);
            ack.setTime(datetime);
            ack.setDiscountAmount(discountedAmount);
            ack.setNewBillAmount(redeemedBillamount);
            ack.setBranchId(SELLER_BRANCHID);
            ack.setStoreId(SELLER_STOREID);
            ack.setPoints(redeemedPoints);
            ack.setTransId(transId);
            ack.setResponse(ackResponse);
        }

        Gson gson = new Gson();
        acknowledgePoints = gson.toJson(ack);
        sendMessage();
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
