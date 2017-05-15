package com.bizzmark.seller.sellerwithoutlogin.sellerapp;

import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
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
import java.util.Calendar;
import java.util.List;

/**
 * Created by Tharun on 12-04-2017.
 */

public class RedeemFragment extends Fragment implements View.OnClickListener {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    String sellerEmail = "seller@smartpoints.com";
    //String status, pageR, limit;
    String Url = "http://35.154.104.54/smartpoints/seller-api/redeem-transactions-with-search?sellerEmail="+sellerEmail;
    private String URL_DATA=Url;

    private List<TransactionList> transactionLists;

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

        recyclerView = (RecyclerView) view.findViewById(R.id.redeemView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        transactionLists = new ArrayList<>();
        loadRecyclerViewData();

    }

    public void loadRecyclerViewData(){
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading Data");
        progressDialog.show();

        transactionLists.clear();

        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                URL_DATA, new Response.Listener<String>() {
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

                    adapter = new RedeemAdapter(transactionLists,getActivity());
                    recyclerView.setAdapter(adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(getActivity(),error.getMessage(),Toast.LENGTH_LONG).show();
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
        String dateFilterUrl ="http://35.154.104.54/smartpoints/seller-api/redeem-transactions-with-search?sellerEmail="+sellerEmail+"&fromDate="+fromDate+"&toDate="+toDate;
        URL_DATA = dateFilterUrl;
        loadRecyclerViewData();
    }
}
