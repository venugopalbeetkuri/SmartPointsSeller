package com.bizzmark.seller.sellerwithoutlogin.My_Customers;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bizzmark.seller.sellerwithoutlogin.R;
import com.bizzmark.seller.sellerwithoutlogin.WifiDirectReceive;
import com.bizzmark.seller.sellerwithoutlogin.util.UrlUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.bizzmark.seller.sellerwithoutlogin.login.Login.SELLER_BRANCHID;
import static com.bizzmark.seller.sellerwithoutlogin.login.Login.sellerBranchId;

public class MyCustomers extends AppCompatActivity implements View.OnClickListener {

    private ImageView backbut;
    private RecyclerView roView;
    private RecyclerView.Adapter adapter;
    SwipeRefreshLayout myCustswipe;
    String CUSTOMER_LIST_URL = UrlUtils.CUSTOMER_LIST_URL+SELLER_BRANCHID;
    private final String URL_DATA = CUSTOMER_LIST_URL;
    private List<MyCustomersList> myCustomersLists;
    String statusType,response;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_customers);
        backbut = (ImageView)findViewById(R.id.backbut);
        backbut.setOnClickListener(this);
        roView = (RecyclerView)findViewById(R.id.roView);
        roView.setHasFixedSize(true);
        roView.setLayoutManager(new LinearLayoutManager(this));
        myCustomersLists = new ArrayList<>();
        myCustswipe = (SwipeRefreshLayout) findViewById(R.id.myCustswipe);
        myCustswipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                myCustswipe.setRefreshing(false);
                loadCustomersList();
            }
        });
        loadCustomersList();
    }

    private void loadCustomersList() {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading Data");
        progressDialog.show();

        StringRequest loadList = new StringRequest(Request.Method.GET, URL_DATA,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        progressDialog.dismiss();
                        try {
                            JSONObject custList = new JSONObject(s);
                            statusType = custList.getString("status_type");

                            if (statusType.equalsIgnoreCase("success")) {
                                MyCustomersList list = new MyCustomersList(
                                        custList.getString("total_branch_visitors"),
                                        custList.getString("total_store_vistors")
                                );
                                myCustomersLists.add(list);
                                adapter = new MyCustomersRCAdapter(myCustomersLists, getApplicationContext());
                                roView.setAdapter(adapter);
                            }else if (statusType.equalsIgnoreCase("error")) {
                                response = custList.getString("response");
                                try {
                                    new AlertDialog.Builder(MyCustomers.this)
                                            .setTitle("Error")
                                            .setIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.cancel, null))
                                            .setMessage(response)
                                            .setCancelable(false)
                                            .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                    loadCustomersList();
                                                }
                                            })
                                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                    Intent i = new Intent(getApplicationContext(), WifiDirectReceive.class);
                                                    startActivity(i);
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
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                try {
                    new AlertDialog.Builder(MyCustomers.this)
                            .setTitle("Error")
                            //.setIcon(ResourcesCompat.getDrawable(getResources(),R.drawable.error,null))
                            .setMessage("Something wrong with Internet connetion \n Please ensure Internet connection")
                            .setCancelable(true)
                            .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    loadCustomersList();
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            Intent i = new Intent(getApplicationContext(), WifiDirectReceive.class);
                            startActivity(i);
                        }
                    }).create().show();
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        loadList.setRetryPolicy(new DefaultRetryPolicy(
                300000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(loadList);
    }

    @Override
    public void onClick(View v) {
        if (v == backbut){
            backbut();
        }
    }
    /*method for back arrow button*/
    private void backbut(){
        MyCustomers.super.onBackPressed();
    }
}
