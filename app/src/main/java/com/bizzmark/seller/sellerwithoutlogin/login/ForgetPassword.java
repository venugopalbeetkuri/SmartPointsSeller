package com.bizzmark.seller.sellerwithoutlogin.login;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bizzmark.seller.sellerwithoutlogin.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
//import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONException;
import org.json.JSONObject;

import static com.bizzmark.seller.sellerwithoutlogin.WifiDirectReceive.storeName;

public class ForgetPassword extends AppCompatActivity implements View.OnClickListener {

    ImageButton ibforgetback;
    private Button btforgetsubmit;
    private EditText etforgetemail;
    private String ForgetPassURL;
    private String statusType,response;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        ibforgetback = (ImageButton)findViewById(R.id.ibforgetback);
        ibforgetback.setOnClickListener(this);
        etforgetemail = (EditText) findViewById(R.id.etforgetemail);
        btforgetsubmit = (Button)findViewById(R.id.btforgetsubmit);
        btforgetsubmit.setOnClickListener(this);
    }

    public void backBut(){
//        ForgetPassword.super.onBackPressed();
        finish();
        startActivity(new Intent(ForgetPassword.this,Login.class));
    }

    private void resetPassword(){
        String emailAddress = etforgetemail.getText().toString().trim();
        /*checking edit box is empty or not*/
        if (TextUtils.isEmpty(emailAddress)){
            try {
                new AlertDialog.Builder(ForgetPassword.this)
                        .setMessage("Enter Valid Email Address")
                        .setCancelable(true)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).create().show();
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
        else {
            ForgetPassURL = "http://35.154.104.54/smartpoints/api/request-password-reset?userEmail="+emailAddress;
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Loading");
            progressDialog.show();
            StringRequest passwordRequest = new StringRequest(Request.Method.GET,
                    ForgetPassURL, new Response.Listener<String>() {
                @Override
                public void onResponse(String s) {
                    progressDialog.dismiss();

                    try {
                        JSONObject jsonObject = new JSONObject(s);
                        statusType = jsonObject.getString("status_type");
                        response=jsonObject.getString("response");
                        if (statusType.equalsIgnoreCase("success")){
                            try {
                                new AlertDialog.Builder(ForgetPassword.this)
                                        .setTitle("Success")
                                        .setIcon(ResourcesCompat.getDrawable(getResources(),R.drawable.checked, null))
                                        .setMessage(response)
                                        .setCancelable(true)
                                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                                finish();
                                                startActivity(new Intent(ForgetPassword.this,Login.class));
                                            }
                                        })
                                        .setNegativeButton("Goto Gmail", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                                finish();
                                                startActivity(new Intent(ForgetPassword.this,Gmail.class));
                                            }
                                        }).create().show();
                            }
                            catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                        else if (statusType.equalsIgnoreCase("error")){
                            try {
                                new AlertDialog.Builder(ForgetPassword.this)
                                        .setTitle("Error")
                                        .setIcon(ResourcesCompat.getDrawable(getResources(),R.drawable.cancel, null))
                                        .setMessage(response)
                                        .setCancelable(true)
                                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                                finish();
                                                startActivity(new Intent(ForgetPassword.this,Login.class));
                                            }
                                        }).create().show();
                            }
                            catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.dismiss();
                    try {
                        new AlertDialog.Builder(ForgetPassword.this)
                                .setTitle("Error")
                                .setIcon(ResourcesCompat.getDrawable(getResources(),R.drawable.error, null))
                                .setMessage("Something went wrong with Internet connection \n Please ensure Internet connection")
                                .setCancelable(true)
                                .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        resetPassword();
                                    }
                                })
                                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        finish();
                                        startActivity(new Intent(ForgetPassword.this,Login.class));
                                    }
                                }).create().show();

                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });
            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            requestQueue.add(passwordRequest);
        }
    }

    @Override
    public void onClick(View view) {

        if (view == ibforgetback){
            backBut();
        }

        if (view == btforgetsubmit){
            resetPassword();
        }
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        finish();
        startActivity(new Intent(ForgetPassword.this,Login.class));
    }
}
