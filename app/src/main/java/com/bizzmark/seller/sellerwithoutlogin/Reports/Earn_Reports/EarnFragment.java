package com.bizzmark.seller.sellerwithoutlogin.Reports.Earn_Reports;

import android.app.DatePickerDialog;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ProgressBar;
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.bizzmark.seller.sellerwithoutlogin.login.Login.SELLER_BRANCHID;
import static com.bizzmark.seller.sellerwithoutlogin.login.Login.SELLER_EMAILID;
import static com.bizzmark.seller.sellerwithoutlogin.login.Login.sellerEmail;

/**
 * Created by Tharun on 12-04-2017.
 */

public class EarnFragment extends Fragment implements View.OnClickListener {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    ProgressBar progressBar1;
    SwipeRefreshLayout earnFragSwipe;
    int pageR, limitInt,n=1;
    String limit, page;
//    String Url = "http://35.154.104.54/smartpoints/seller-api/earn-transactions-with-search?sellerEmail="+SELLER_EMAILID;

    String Url = "http://35.154.104.54/smartpoints/seller-api/branch-earn-transactions?branchId="+SELLER_BRANCHID;
    private String URL_DATA=Url;

    private List<EarnFragTansList> earnFragTansLists;

    private int requestCount =1;

    TextView tvFromDate,tvToDate;
    public String fromDate, toDate;

    String transType, status_type, response;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.report_earn_fragment,container,false);
        findViewById(view);
        return view;
    }

    private void findViewById(View view) {

        tvFromDate=(TextView)view.findViewById(R.id.tvFromDate);
        tvToDate=(TextView)view.findViewById(R.id.tvToDate);

//        progressBar1=(ProgressBar) view.findViewById(R.id.progressBar1);

        recyclerView = (RecyclerView) view.findViewById(R.id.rcEarnView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        earnFragSwipe = (SwipeRefreshLayout) view.findViewById(R.id.earnFragSwipe);
        earnFragSwipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                earnFragSwipe.setRefreshing(false);
                loadRecyclerViewData();
            }
        });
        earnFragTansLists = new ArrayList<>();
        loadRecyclerViewData();
        tvFromDate.setOnClickListener(this);
        tvToDate.setOnClickListener(this);

    }

    public void loadRecyclerViewData(){
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading Data");
        progressDialog.show();

        earnFragTansLists.clear();
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                URL_DATA, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                progressDialog.dismiss();

                try {
                    JSONObject jsonObject = new JSONObject(s);
                    status_type = jsonObject.getString("status_type");

                    if (status_type.equalsIgnoreCase("success")) {
                        JSONArray array = jsonObject.getJSONArray("response");

                        for (int i = 0; i < array.length(); i++) {
                            JSONObject o = array.getJSONObject(i);
                            EarnFragTansList list = new EarnFragTansList(
                                    o.getString("transaction_id"),
                                    o.getString("original_bill_amount"),
                                    o.getString("points"),
                                    o.getString("transacted_at")
                            );

                            earnFragTansLists.add(list);
                        }
                    } else if (status_type.equalsIgnoreCase("error")) {
                        response = jsonObject.getString("response");
                        try {
                            new AlertDialog.Builder(getContext())
                                    .setTitle("Error")
                                    .setIcon(ResourcesCompat.getDrawable(getResources(),R.drawable.information_icon,null))
                                    .setMessage(response)
                                    .setCancelable(true)
                                    .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                            Intent i = new Intent(getActivity(), WifiDirectReceive.class);
                                            startActivity(i);
                                        }
                                    }).create().show();
//                        Toast.makeText(getActivity(),response,Toast.LENGTH_LONG).show();
                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                    adapter = new EarnFragAdapter(earnFragTansLists,getActivity());
                    recyclerView.setAdapter(adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
//                        progressBar1.setVisibility(View.GONE);get
                        try {
                            new AlertDialog.Builder(getContext())
                                    .setTitle("Error")
                                    .setIcon(ResourcesCompat.getDrawable(getResources(),R.drawable.error,null))
                                    .setMessage("Something wrong with Internet connection \n Please ensure Internet connection")
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
                                            Intent i = new Intent(getActivity(), WifiDirectReceive.class);
                                            startActivity(i);
                                        }
                                    }).create().show();
//                        Toast.makeText(getActivity(),"Some Thing Went Wrong Please Try Again",Toast.LENGTH_LONG).show();
                        }
                        catch (Exception e ){
                            e.printStackTrace();
                        }
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
//        n=n+1;
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
//        String dateFilterUrl ="http://35.154.104.54/smartpoints/seller-api/earn-transactions-with-search?sellerEmail="+SELLER_EMAILID+"&fromDate="+fromDate+"&toDate="+toDate;
        String dateFilterUrl ="http://35.154.104.54/smartpoints/seller-api/branch-earn-transactions-with-search?branchId="+SELLER_BRANCHID+"&fromDate="+fromDate+"&toDate="+toDate;
        URL_DATA = dateFilterUrl;

        loadRecyclerViewData();
    }

    public void pageLimit(){
        limitInt = 1;
        limit = String.valueOf(limitInt);
        pageR = 1;
        for (int j=1; j<=120; j++){
            earnFragTansLists.clear();
            page = String.valueOf(j);
            String pageLimit ="http://35.154.104.54/smartpoints/seller-api/earn-transactions-with-search?sellerEmail="+sellerEmail+"&page="+page+"&limit="+limit;
            URL_DATA = pageLimit;
            loadRecyclerViewData();
        }

    }
}
