package com.bizzmark.seller.sellerwithoutlogin.sellerapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bizzmark.seller.sellerwithoutlogin.R;
import com.bizzmark.seller.sellerwithoutlogin.login.Seller_Basic_Information;

public class ReportActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView backbut;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.report_activity);
        backbut=(ImageView)findViewById(R.id.backbut);
        backbut.setOnClickListener(this);


    }

    private void backbut(){
        ReportActivity.super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        if (v== backbut){
            backbut();
        }
    }
}
