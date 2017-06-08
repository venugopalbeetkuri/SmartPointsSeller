package com.bizzmark.seller.sellerwithoutlogin.Reports.Last_Ten_Transactions;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

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

import static com.bizzmark.seller.sellerwithoutlogin.login.Login.SELLER_EMAILID;

/**
 * Created by Tharun on 22-05-2017.
 */

public class LastTenTrans extends AppCompatActivity implements View.OnClickListener {
    private ImageView backbut;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    LinearLayout linear1;
    Button retry;
    SwipeRefreshLayout lastTenTranSwipe;
    String  status_type, response;
    public static String transType, oldBillAmount;
    String Url="http://35.154.104.54/smartpoints/seller-api/last-10-transactions?sellerEmail="+SELLER_EMAILID;
    private final String URL_DATA=Url;
    private List<LastTenTransactionsList> tenTransactionsLists;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.last_ten_transactions_activity);
        backbut = (ImageView) findViewById(R.id.backbut);
        backbut.setOnClickListener(this);
        recyclerView = (RecyclerView) findViewById(R.id.rdView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        tenTransactionsLists = new ArrayList<>();
        lastTenTranSwipe = (SwipeRefreshLayout) findViewById(R.id.lastTenTranSwipe);
        lastTenTranSwipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                lastTenTranSwipe.setRefreshing(false);
                loadTenTransactions();
            }
        });
        loadTenTransactions();

    }

    private void loadTenTransactions(){
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading Data");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                URL_DATA, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    status_type = jsonObject.getString("status_type");
                    response = jsonObject.getString("response");
                    if (status_type.equalsIgnoreCase("success")) {
                        JSONArray array = jsonObject.getJSONArray("response");
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject object = array.getJSONObject(i);
                            transType = object.getString("type");
                            oldBillAmount = object.getString("original_bill_amount");
                            LastTenTransactionsList list = new LastTenTransactionsList(
                                    object.getString("transaction_id"),
                                    object.getString("type"),
                                    object.getString("original_bill_amount"),
                                    object.getString("points"),
                                    object.getString("store_name"),
                                    object.getString("discounted_bill_amount"),
                                    object.getString("transacted_at"));
                            tenTransactionsLists.add(list);
                        }
                    } else if (status_type.equalsIgnoreCase("error")) {
                        progressDialog.dismiss();
                        try {
                            new AlertDialog.Builder(LastTenTrans.this)
                                    .setTitle("Error")
                                    .setIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.information_icon, null))
                                    .setMessage(response)
                                    .setCancelable(true)
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
//                                            Intent i = new Intent(LastTenTrans.this, WifiDirectReceive.class);
//                                            startActivity(i);
                                            finish();
                                        }
                                    }).create().show();
//                        Toast.makeText(getApplicationContext(),response,Toast.LENGTH_LONG).show();
                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                    adapter = new LastTenTransactionsAdapter(tenTransactionsLists,getApplicationContext());
                    recyclerView.setAdapter(adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();

                try {
                    new AlertDialog.Builder(LastTenTrans.this)
                            .setTitle("Error")
                            .setIcon(ResourcesCompat.getDrawable(getResources(),R.drawable.error,null))
                            .setMessage("Something went wrong with Internet connection \n Please ensure Internet connection")
                            .setCancelable(true)
                            .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    loadTenTransactions();
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
//                Toast.makeText(getApplicationContext(),"Some Thing Went Wrong Please Try Again",Toast.LENGTH_LONG).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void backbut(){
        LastTenTrans.super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        if (v == backbut){
            backbut();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
