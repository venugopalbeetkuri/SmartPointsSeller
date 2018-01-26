package com.bizzmark.seller.sellerwithoutlogin.sellerInfo;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bizzmark.seller.sellerwithoutlogin.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.bizzmark.seller.sellerwithoutlogin.login.Login.SELLER_BRANCHID;
import static com.bizzmark.seller.sellerwithoutlogin.util.UrlUtils.BRANCH_BASIC_INFO;

public class Seller_Basic_Information extends AppCompatActivity implements View.OnClickListener {

    private ImageView backbut;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    SwipeRefreshLayout sellerInfoSwipe;
    String statusType,response;
    String InfoUrl = BRANCH_BASIC_INFO+SELLER_BRANCHID;
    private final String URL_DATA = InfoUrl;
    private List<SellerInfoList> sellerInfoLists;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seller_basic_information);
        backbut = (ImageView) findViewById(R.id.backbut);
        backbut.setOnClickListener(this);
        recyclerView = (RecyclerView) findViewById(R.id.sellerinforcView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        sellerInfoLists = new ArrayList<>();
        sellerInfoSwipe = (SwipeRefreshLayout)findViewById(R.id.sellerInfoSwipe);
        sellerInfoSwipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                sellerInfoSwipe.setRefreshing(false);
                getInfo();
            }
        });
        getInfo();
    }

    //Method for onbackbut click
    private void backbut(){
        Seller_Basic_Information.super.onBackPressed();
    }

    /*Method for getting seller info*/
    private void getInfo(){
        final  ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading data");
        progressDialog.show();
        sellerInfoLists.clear();
        StringRequest getSellerInfo = new StringRequest(Request.Method.GET,
                URL_DATA, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                progressDialog.dismiss();
                try {
                    JSONObject object = new JSONObject(s);
                    statusType  = object.getString("status_type");
                    if (statusType.equalsIgnoreCase("success")){
                        JSONArray array = object.getJSONArray("response");
                        for (int i=0 ; i<array.length(); i++){
                            JSONObject jsonObject = array.getJSONObject(i);
                            SellerInfoList list = new SellerInfoList(
                                    jsonObject.getString("store_name"),
                                    jsonObject.getString("store_id"),
                                    jsonObject.getString("branch_name"),
                                    jsonObject.getString("branch_id"),
                                    jsonObject.getString("points_percentage"),
                                    jsonObject.getString("points_value"),
                                    jsonObject.getString("store_email"),
                                    jsonObject.getString("store_address"));
                            sellerInfoLists.add(list);
                        }
                    }
                    else if (statusType.equalsIgnoreCase("error")){
                        progressDialog.dismiss();
                        try {
                            new AlertDialog.Builder(Seller_Basic_Information.this)
                                    .setTitle("Error")
                                    .setIcon(ResourcesCompat.getDrawable(getResources(),R.drawable.information_icon, null))
                                    .setMessage(response)
                                    .setCancelable(true)
                                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                            finish();
                                        }
                                    }).create().show();
                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }
                    }

                    adapter = new SellerInfoAdapter(sellerInfoLists,getApplicationContext());
                    recyclerView.setAdapter(adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
//                Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_LONG).show();
                try {
                    new AlertDialog.Builder(Seller_Basic_Information.this)
                            .setTitle("Error")
                            //.setIcon(ResourcesCompat.getDrawable(getResources(),R.drawable.error, null))
                            .setMessage("Something went wrong with Internet connection \n Please ensure Internet connection")
                            .setCancelable(true)
                            .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    getInfo();
                                }
                            })
                            .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    finish();
                                }
                            }).create().show();
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        getSellerInfo.setRetryPolicy(new DefaultRetryPolicy(
                300000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(getSellerInfo);
    }

    @Override
    public void onClick(View v) {

        if (v== backbut){
            backbut();
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
