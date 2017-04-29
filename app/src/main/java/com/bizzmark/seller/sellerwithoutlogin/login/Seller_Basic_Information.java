package com.bizzmark.seller.sellerwithoutlogin.login;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bizzmark.seller.sellerwithoutlogin.R;
import com.bizzmark.seller.sellerwithoutlogin.WifiDirectReceive;
import com.bizzmark.seller.sellerwithoutlogin.db.Retrofit.SellerProfile;
import com.bizzmark.seller.sellerwithoutlogin.sellerapp.EarnPoints;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class Seller_Basic_Information extends AppCompatActivity implements View.OnClickListener {

   // private EditText store_name,branch_id,branch_name,branch_mgname,branch_address,branch_discount;
    private EditText storename,email,brid,brname,brmgrname,braddress,brdiscount;
    private Button seller_infosave;
    private ImageView backbut;
    String store_name;

    public static final String ROOT_URL="https://wwwbizzmarkin.000webhostapp.com/";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seller_basic_information);
        seller_infosave=(Button)findViewById(R.id.seller_infosave);
        backbut=(ImageView)findViewById(R.id.backbut);

        storename = (EditText)findViewById(R.id.store_name);
        email = (EditText)findViewById(R.id.email);
        brid = (EditText)findViewById(R.id.branch_uid);
        brname = (EditText)findViewById(R.id.branch_name);
        brmgrname = (EditText)findViewById(R.id.branch_mgname);
        braddress = (EditText)findViewById(R.id.branch_address);
        brdiscount = (EditText)findViewById(R.id.branch_discount);

        backbut.setOnClickListener(this);

        seller_infosave.setOnClickListener(this);

        store_name = storename.getText().toString();


    }

    //Method for onbackbut click


    private void backbut(){
        Seller_Basic_Information.super.onBackPressed();
    }

    public void saveInfo(){

        RestAdapter adapter=new RestAdapter.Builder()
                .setEndpoint(ROOT_URL)//setting the Root URL
                .build(); //Finally building the adapter

        //creating object for our interface

        SellerProfile profile=adapter.create(SellerProfile.class);

        //Defing the method insertdata of our interface

        profile.saveProfile(storename.getText().toString(),
                email.getText().toString(),
                brid.getText().toString(),
                brname.getText().toString(),
                brmgrname.getText().toString(),
                braddress.getText().toString(),
                brdiscount.getText().toString(),
                new Callback<Response>() {
                    @Override
                    public void success(Response result, Response response) {
                        //on success we will read the servers output using bufferedreader
                        //creating a bufferedreader object

                        BufferedReader reader=null;

                        //An string to store output from the server

                        String output="";

                        try {
                            //initialize buffered reader

                            reader=new BufferedReader(new InputStreamReader(result.getBody().in()));

                            //reading output in the string
                            output=reader.readLine();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        //Displaying the output as a toast

                        Toast.makeText(Seller_Basic_Information.this,output,Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        //If any error occured displaying the error toast
                        Toast.makeText(Seller_Basic_Information.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                });
    }


    @Override
    public void onClick(View v) {

        if (v== backbut){
            backbut();
        }
        if (v== seller_infosave){
            saveInfo();
//            Intent intent = new Intent(getApplicationContext(), WifiDirectReceive.class);
//            intent.putExtra("header_storeName", store_name);
//            getApplicationContext().startActivity(intent);
            finish();
        }

    }
}
