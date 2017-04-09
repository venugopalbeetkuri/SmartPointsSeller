package com.bizzmark.seller.sellerwithoutlogin.login;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bizzmark.seller.sellerwithoutlogin.R;
import com.bizzmark.seller.sellerwithoutlogin.WifiDirectReceive;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Login extends AppCompatActivity implements View.OnClickListener {

    private Button buttonSignIn;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private TextView textViewSignup;
    private TextView policy,terms;
    //   private ImageView backbut;
    FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
    DatabaseReference database= FirebaseDatabase.getInstance().getReference();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        editTextEmail=(EditText)findViewById(R.id.editTextEmail);
        editTextPassword=(EditText)findViewById(R.id.editTextPassword);
        buttonSignIn=(Button)findViewById(R.id.buttonSignin);
        textViewSignup=(TextView)findViewById(R.id.textViewSignUp);
        policy=(TextView)findViewById(R.id.policy);
        terms=(TextView)findViewById(R.id.terms);
        if (firebaseAuth.getCurrentUser() != null){
            finish();
            Intent i=new Intent(Login.this,WifiDirectReceive.class);
            startActivity(i);
        }

        buttonSignIn.setOnClickListener(this);
        textViewSignup.setOnClickListener(this);
        policy.setOnClickListener(this);
        terms.setOnClickListener(this);

    }

    //method for user login
    private void userLogin(){
        final String email=editTextEmail.getText().toString().trim();
        final String password=editTextPassword.getText().toString().trim();

        //checking email id and password is empty or not

        if(TextUtils.isEmpty(email)){

            Vibrator v=(Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
            v.vibrate(300);
            Toast.makeText(this,"Please enter Login Id",Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(password)){
            Vibrator v=(Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
            v.vibrate(300);
            Toast.makeText(this,"Please enter Password", Toast.LENGTH_LONG).show();
            return;
        }

        //If email id and password is not empty

        final ProgressDialog progressDialog=ProgressDialog.show(Login.this,"Please wait","Processing",true);
        (firebaseAuth.signInWithEmailAndPassword(email,password)).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressDialog.dismiss();
                if (task.isSuccessful()){
                    Toast.makeText(Login.this,"SUCCESS",Toast.LENGTH_LONG).show();
                    editTextEmail.setText("");
                    editTextPassword.setText("");
                    finish();
                    Intent i=new Intent(Login.this,WifiDirectReceive.class);
                    startActivity(i);
                }
                else {

                    Log.e("ERROR",task.getException().toString());
                    Vibrator v=(Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
                    v.vibrate(300);
                    Toast.makeText(Login.this,task.getException().getMessage(),Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    // moving to Signup page
    private void userSignup(){
//        Intent i=new Intent(Login.this,SellerRegistration.class);
//        startActivity(i);
    }

    //method for dispalying terms

    public void terms(){
        final Dialog custom=new Dialog(Login.this);
        custom.setTitle("CUSTOM DIALOG");
        custom.requestWindowFeature(Window.FEATURE_NO_TITLE);
        custom.setContentView(R.layout.activity_terms);
        custom.setCanceledOnTouchOutside(true);
        custom.show();
    }

    //method for dispalying terms

    public void policy(){
        final Dialog custom=new Dialog(Login.this);
        custom.setTitle("CUSTOM DIALOG");
        custom.requestWindowFeature(Window.FEATURE_NO_TITLE);
        custom.setContentView(R.layout.activity_privacy_policy);
        custom.setCanceledOnTouchOutside(true);
        custom.show();
    }



    @Override
    public void onClick(View v) {

        if (v==buttonSignIn){
            userLogin();

        }
        if (v==textViewSignup){
            userSignup();
        }
        if(v==terms){
            terms();
            // backbut();
        }
        if(v==policy){
            policy();
            //  backbut();
        }

    }
}
