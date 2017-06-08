package com.bizzmark.seller.sellerwithoutlogin.Splash;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import com.bizzmark.seller.sellerwithoutlogin.R;
import com.bizzmark.seller.sellerwithoutlogin.login.Login;

public class Splash extends AppCompatActivity {

    ProgressBar splashProgressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        splashProgressBar = (ProgressBar) findViewById(R.id.splashProgressBar);
        splashProgressBar.setVisibility(View.VISIBLE);
        Thread thread = new Thread(){
            public void run(){
                try {
                    sleep(2000);
                    splashProgressBar.setVisibility(View.INVISIBLE);
                }
                catch (Exception e){
                    e.printStackTrace();
                }
                finally {

                    Intent intent = new Intent(Splash.this, Login.class);
                    startActivity(intent);
                }
            }
        };
        thread.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
