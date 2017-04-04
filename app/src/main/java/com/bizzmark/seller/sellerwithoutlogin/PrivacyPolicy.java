package com.bizzmark.seller.sellerwithoutlogin;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class PrivacyPolicy extends AppCompatActivity implements View.OnClickListener {

    private ImageView backbut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_policy);
        backbut=(ImageView)findViewById(R.id.backbut);
        backbut.setOnClickListener(this);
    }

//method for backbut

    private void backbut(){
        PrivacyPolicy.super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        if (v==backbut){
            backbut();
        }
    }
}
