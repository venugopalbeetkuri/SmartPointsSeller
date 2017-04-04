package com.bizzmark.seller.sellerwithoutlogin;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class Terms extends AppCompatActivity implements View.OnClickListener {

    private ImageView backbut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms);
        backbut=(ImageView)findViewById(R.id.backbut);
        backbut.setOnClickListener(this);
    }
//method for backbut

    private void backbut(){
        Terms.super.onBackPressed();
    }
    @Override
    public void onClick(View v) {
        backbut();
    }
}
