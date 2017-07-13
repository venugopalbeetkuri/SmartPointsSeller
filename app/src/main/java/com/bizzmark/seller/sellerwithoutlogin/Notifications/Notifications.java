package com.bizzmark.seller.sellerwithoutlogin.Notifications;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.bizzmark.seller.sellerwithoutlogin.R;
import com.bizzmark.seller.sellerwithoutlogin.Reports.Last_Ten_Transactions.LastTenTrans;

public class Notifications extends AppCompatActivity implements View.OnClickListener {

    TextView txtCount;
    EditText txtNotification;
    private ImageView backbut;
    Spinner typeSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);
        backbut = (ImageView) findViewById(R.id.backbut);
        backbut.setOnClickListener(this);
        typeSpinner = (Spinner)findViewById(R.id.typeSpinner);
        typeSpinner.setVisibility(View.GONE);
        txtCount = (TextView)findViewById(R.id.txtCount);
        txtNotification = (EditText)findViewById(R.id.txtNotification);
        txtNotification.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int txtLength = txtNotification.length();
                String txtConvert = String.valueOf(txtLength);
                txtCount.setText(txtConvert);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
    private void backbut(){
        Notifications.super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        if (v == backbut){
            backbut();
        }
    }
}
