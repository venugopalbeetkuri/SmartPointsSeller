package com.bizzmark.seller.sellerwithoutlogin.Reports.Redeem_Reports;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bizzmark.seller.sellerwithoutlogin.R;
import com.bizzmark.seller.sellerwithoutlogin.WifiDirectReceive;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.bizzmark.seller.sellerwithoutlogin.login.Login.KEY_EMAIL;
import static com.bizzmark.seller.sellerwithoutlogin.login.Login.SELLER_EMAILID;
import static com.bizzmark.seller.sellerwithoutlogin.login.Login.accessToken;
import static com.bizzmark.seller.sellerwithoutlogin.login.Login.sellerEmail;

/**
 * Created by Tharun on 12-04-2017.
 */

public class RedeemFragment extends Fragment implements View.OnClickListener {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    String Url = "http://35.154.104.54/smartpoints/seller-api/redeem-transactions-with-search?sellerEmail="+SELLER_EMAILID;
    private String URL_DATA=Url;

    String transType, status_type, response;

    SwipeRefreshLayout redeemTransSwipe;

    private List<RedeemFragTransList> redeemFragTransLists;

    TextView tvToDate;
    TextView tvFromDate;

    public String fromDate, toDate;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.report_redeem_fragment,container,false);
        findViewById(view);
        return view;

    }

    private void findViewById(View view) {

        tvFromDate=(TextView) view.findViewById(R.id.tvFromDate);
        tvToDate=(TextView)view.findViewById(R.id.tvToDate);

        tvFromDate.setOnClickListener(this);
        tvToDate.setOnClickListener(this);

        redeemTransSwipe = (SwipeRefreshLayout) view.findViewById(R.id.redeemTransSwipe);
        redeemTransSwipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                redeemTransSwipe.setRefreshing(false);
                loadRecyclerViewData();
            }
        });

        recyclerView = (RecyclerView) view.findViewById(R.id.redeemView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        redeemFragTransLists = new ArrayList<>();
        loadRecyclerViewData();

    }

    public void loadRecyclerViewData(){
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading Data");
        progressDialog.show();
        redeemFragTransLists.clear();

        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                URL_DATA, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(s);
//                    JSONArray array = jsonObject.getJSONArray("response");
                    status_type = jsonObject.getString("status_type");

                    if (status_type.equalsIgnoreCase("success")) {
                        JSONArray array = jsonObject.getJSONArray("response");
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject o = array.getJSONObject(i);
                            RedeemFragTransList list = new RedeemFragTransList(
                                    o.getString("transaction_id"),
                                    o.getString("original_bill_amount"),
                                    o.getString("points"),
                                    o.getString("store_name"),
                                    o.getString("discounted_bill_amount"),
                                    o.getString("transacted_at")
                            );

                            redeemFragTransLists.add(list);
                        }
                    } else if (status_type.equalsIgnoreCase("error")){
                        response = jsonObject.getString("response");
                        try {
                            new AlertDialog.Builder(getContext())
                                    .setMessage(response+"In Redeem")
                                    .setCancelable(true)
                                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    }).create().show();
                        } catch (Exception e){
                            e.printStackTrace();
                        }
//                        Toast.makeText(getActivity(),response+"In Redeem",Toast.LENGTH_LONG).show();
                    }
                    adapter = new RedeemAdapter(redeemFragTransLists,getActivity());
                    recyclerView.setAdapter(adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                new AlertDialog.Builder(getContext())
                        .setMessage("Something wrong In Redeem Url")
                        .setCancelable(true)
                        .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                loadRecyclerViewData();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).create().show();
//                Toast.makeText(getActivity(),"Something Went Wrong Please Try Again",Toast.LENGTH_LONG).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }

    @Override
    public void onClick(View v) {

        if (v == tvFromDate) {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            DatePickerDialog dialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    tvFromDate.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                    fromDate = tvFromDate.getText().toString();
                }
            }, year, month, day);
            dialog.show();
        }

        if (v == tvToDate){
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            DatePickerDialog dialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    tvToDate.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                    toDate = tvToDate.getText().toString();
                    dateFilter();
                }
            }, year, month, day);
            dialog.show();
        }
    }
    public void dateFilter(){
        String dateFilterUrl ="http://35.154.104.54/smartpoints/seller-api/redeem-transactions-with-search?sellerEmail="+SELLER_EMAILID+"&fromDate="+fromDate+"&toDate="+toDate;
        URL_DATA = dateFilterUrl;
        loadRecyclerViewData();
    }
}
