package com.bizzmark.seller.sellerwithoutlogin.sellerapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bizzmark.seller.sellerwithoutlogin.R;

public class LastTenTransactions extends AppCompatActivity implements View.OnClickListener {

    private ImageView backbut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.last_ten_transactions_activity);
        backbut=(ImageView)findViewById(R.id.backbut);
        backbut.setOnClickListener(this);

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
