package com.bizzmark.seller.sellerwithoutlogin.sellerapp;

import android.app.LauncherActivity;
import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
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

import static com.bizzmark.seller.sellerwithoutlogin.WifiDirectReceive.storeName;

public class LastTenTransactions extends AppCompatActivity implements View.OnClickListener {

    private ImageView backbut;
    TextView lastTransHeader;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    String sellerEmail="seller@smartpoints.com";
    String Url="http://35.154.104.54/smartpoints/seller-api/last-10-transactions?sellerEmail="+sellerEmail;
    private final String URL_DATA=Url;

    private List<TransactionList> transactionLists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.last_ten_transactions_activity);
        backbut=(ImageView)findViewById(R.id.backbut);
        backbut.setOnClickListener(this);

        recyclerView = (RecyclerView) findViewById(R.id.rdView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        lastTransHeader = (TextView) findViewById(R.id.lastTransHeader);
        lastTransHeader.setText(storeName);
        transactionLists = new ArrayList<>();
        loadRecyclerViewData();
    }

    private  void loadRecyclerViewData(){

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading Data");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                URL_DATA,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        progressDialog.dismiss();

                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            JSONArray array = jsonObject.getJSONArray("response");

                            for (int i=0; i<array.length(); i++){
                                JSONObject o = array.getJSONObject(i);
                                TransactionList list = new TransactionList(
                                        o.getString("original_bill_amount"),
                                        o.getString("type"),
                                        o.getString("points"),
                                        o.getString("discounted_bill_amount"),
                                        o.getString("transacted_at"),
                                        o.getString("user_device_id"),
                                        o.getString("store_name")
                                );

                                transactionLists.add(list);
                            }

                            adapter = new RedeemAdapter(transactionLists,getApplicationContext());
                            recyclerView.setAdapter(adapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void backbut(){
        LastTenTransactions.super.onBackPressed();
    }

    @Override
    public void onClick(View v) {

        if (v== backbut){
            backbut();
        }
    }
}
