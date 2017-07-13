package com.bizzmark.seller.sellerwithoutlogin.login;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.InputType;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bizzmark.seller.sellerwithoutlogin.PrivacyPolicy;
import com.bizzmark.seller.sellerwithoutlogin.R;
import com.bizzmark.seller.sellerwithoutlogin.Terms;
import com.bizzmark.seller.sellerwithoutlogin.WifiDirectReceive;
import com.bizzmark.seller.sellerwithoutlogin.wifidirect_new.Settings;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.bizzmark.seller.sellerwithoutlogin.WifiDirectReceive.REQUEST_READ_PERMISSION;

public class Login extends AppCompatActivity implements View.OnClickListener{

    private EditText ETUSERNAME;
    private EditText ETPASSWORD;

    private TextView TVFORGET;
    private Button BTLOGIN;

    /* String for storing deviceid*/
    public static String sellerDeviceId;

    /*String for overlay requst code*/
    public final static int REQUEST_CODE = 10101;
    /*String for passing to php*/
    public static final String KEY_EMAIL = "email";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_DEVICE_ID = "deviceId";

    /*Strings for getting email and password*/
    public static String sellerEmail;
    public static String sellerPassword;

    /*String getting from loginphp*/
    public static String accessToken=null;
    public static String userType;
    public static String sellerName;
    public static String sellerMobile;
    public static String sellerStoreName;
    public static String sellerStoreId;
    public static String sellerBranchId;
    String statusType,response;

    public static String ACCESS_TOKEN,SELLER_EMAILID,SELLER_PASSWORD,SELLER_BRANCHID,SELLER_STOREID,SELLER_STORENAE;

    String AccessToken, SellerEmailId, SellerPassword, SellerBranchId, SellerStoreId,SellerStoreName;


    /*URL for Login*/
    public static final String loginURL="http://35.154.104.54/smartpoints/api/login";

    private CheckBox CBPASS;

    private TextView policy,terms;

    ConnectivityManager cm;
    NetworkInfo info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ETUSERNAME=(EditText)findViewById(R.id.etusername);

        ETPASSWORD=(EditText)findViewById(R.id.etpassword);

        BTLOGIN=(Button)findViewById(R.id.btlogin);

        TVFORGET=(TextView)findViewById(R.id.tvforget);

        CBPASS=(CheckBox)findViewById(R.id.cbpassword);

        policy =(TextView)findViewById(R.id.policy);

        terms = (TextView) findViewById(R.id.terms);

        BTLOGIN.setOnClickListener(this);

        TVFORGET.setOnClickListener(this);

        terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Login.this, Terms.class);
                startActivity(i);
            }
        });
        policy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Login.this, PrivacyPolicy.class);
                startActivity(i);
            }
        });

        //password hiding
        CBPASS.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked){
                    ETPASSWORD.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    ETPASSWORD.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });
        checkInternet();
    }

/*method for checking internet is available or not*/
    private void checkInternet() {
        cm = (ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);
        info = cm.getActiveNetworkInfo();
        if (info == null){
            alertDialog();
            // setContentView(R.layout.internet_layout);
            // finish();
        }else {
            SharedPreferences sharedPreferences = getApplication().getSharedPreferences("STORE_DETAILS",Context.MODE_PRIVATE);
            ACCESS_TOKEN = sharedPreferences.getString("Access_Token",AccessToken);
            SELLER_EMAILID = sharedPreferences.getString("Seller_Email",SellerEmailId);
            SELLER_PASSWORD = sharedPreferences.getString("Seller_Password",SellerPassword);
            SELLER_BRANCHID = sharedPreferences.getString("Seller_Branchid",SellerBranchId);
            SELLER_STOREID = sharedPreferences.getString("Seller_StoreId",SellerStoreId);
            SELLER_STORENAE = sharedPreferences.getString("Seller_StoreName",SellerStoreName);
            if (ACCESS_TOKEN!=null){
                finish();
                Intent i = new Intent(this,WifiDirectReceive.class);
                startActivity(i);
            }
        }
    }

    private void alertDialog() {
        new AlertDialog.Builder(this)
                .setTitle("No Internet")
                .setMessage("oops! no internet connection!")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).setCancelable(false).create().show();
    }
    /*getting runtime permission*/
    private void runTimePermission() {
        // runtime permission getting imi-string
        boolean hasPermissionLocation = (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED);
        if (!hasPermissionLocation) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_PHONE_STATE},
                    REQUEST_READ_PERMISSION);
        } else {
            // getting device id
            sellerDeviceId = getIMEIstring();
        }
    }

    /*method for getting device id*/
    private String getIMEIstring() {
        try {
            TelephonyManager telephonyManager = (TelephonyManager) this
                    .getSystemService(Context.TELEPHONY_SERVICE);
            sellerDeviceId = telephonyManager.getDeviceId();
        }catch (Throwable throwable){
            throwable.printStackTrace();
        }
        return sellerDeviceId;
    }

    /*geting email and password from editText*/
    private void gettingMailAndPassword(){
        final String email = ETUSERNAME.getText().toString().trim();
        final String password = ETPASSWORD.getText().toString().trim();

                //Checking if email and password are empty
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Please Enter Username", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please Enter Password", Toast.LENGTH_LONG).show();
            return;
        }

        sellerEmail = email;
        sellerPassword = password;
        /*if not empty moves to logIn()*/
    }

    /*Method for loginseller*/
    public void logIn(){
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Logining in");
        progressDialog.show();
        runTimePermission();

        gettingMailAndPassword();
        StringRequest loginRequest = new StringRequest(Request.Method.POST, loginURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        try {
                            JSONObject loginObj = new JSONObject(s);
                            statusType = loginObj.getString("status_type");

                            /*Checking status is success or not*/
                            if (statusType.equalsIgnoreCase("success")){
                                accessToken = loginObj.getString("access-token");
                                userType = loginObj.getString("usertype");
                                sellerName = loginObj.getString("name");
                                sellerMobile = loginObj.getString("mobile");
                                sellerStoreName = loginObj.getString("store_name");
                                sellerStoreId = loginObj.getString("store_id");
                                sellerBranchId = loginObj.getString("branch_id");
                                SharedPreferences.Editor editor = getApplication().getSharedPreferences("STORE_DETAILS",Context.MODE_PRIVATE).edit();
                                editor.putString("Access_Token",accessToken);
                                editor.putString("Seller_Email",sellerEmail);
                                editor.putString("Seller_Password",sellerPassword);
                                editor.putString("Seller_Branchid",sellerBranchId);
                                editor.putString("Seller_StoreId",sellerStoreId);
                                editor.putString("Seller_StoreName",sellerStoreName);
                                editor.commit();
                                AccessToken = accessToken;
                                SellerEmailId = sellerEmail;
                                SellerPassword = sellerPassword;
                                SellerBranchId = sellerBranchId;
                                SellerStoreId = sellerStoreId;
                                SellerStoreName = sellerStoreName;

                                SharedPreferences sharedPreferences = getApplication().getSharedPreferences("STORE_DETAILS",Context.MODE_PRIVATE);
                                ACCESS_TOKEN = sharedPreferences.getString("Access_Token",AccessToken);
                                SELLER_EMAILID = sharedPreferences.getString("Seller_Email",SellerEmailId);
                                SELLER_PASSWORD = sharedPreferences.getString("Seller_Password",SellerPassword);
                                SELLER_BRANCHID = sharedPreferences.getString("Seller_Branchid",SellerBranchId);
                                SELLER_STOREID = sharedPreferences.getString("Seller_StoreId",SellerStoreId);
                                SELLER_STORENAE = sharedPreferences.getString("Seller_StoreName",SellerStoreName);
                                progressDialog.dismiss();
                                finish();
//                                sharedPreference();
                                Intent intent = new Intent(Login.this,WifiDirectReceive.class);
                                startActivity(intent);
                            }
                            else if (statusType.equalsIgnoreCase("error")) {
                                response = loginObj.getString("response");
                                progressDialog.dismiss();
                                try {
                                    new AlertDialog.Builder(Login.this)
                                            .setTitle("Error")
                                            .setIcon(ResourcesCompat.getDrawable(getResources(),R.drawable.cancel,null))
                                            .setMessage(response)
                                            .setCancelable(false)
                                            .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                    logIn();
                                                }
                                            }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    }).create().show();

//                                    Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
                                }catch (Exception e){
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
                    new AlertDialog.Builder(Login.this)
                            .setTitle("Error")
                            .setIcon(ResourcesCompat.getDrawable(getResources(),R.drawable.error,null))
                            .setMessage("Something wrong with Internet Connection \n Please ensure Interner Connection")
                            .setCancelable(false)
                            .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    logIn();
                                }
                            }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).create().show();
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put(KEY_EMAIL , sellerEmail);
                parameters.put(KEY_PASSWORD, sellerPassword);
                parameters.put(KEY_DEVICE_ID, sellerDeviceId);
                return parameters;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(loginRequest);

    }

    @Override
    public void onClick(View view) {

        if(view == BTLOGIN){
            //Calling userLogin()method
            if (info != null) {
                    logIn();

            }else {
            alertDialog();
        }
        }

        if(view == TVFORGET){
            //Closing Current Task
//            finish();
            //Goto Forget password class
            startActivity(new Intent(this, ForgetPassword.class));

        }

    }
}
