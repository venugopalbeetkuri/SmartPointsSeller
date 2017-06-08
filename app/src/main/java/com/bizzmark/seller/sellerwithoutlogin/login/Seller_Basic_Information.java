package com.bizzmark.seller.sellerwithoutlogin.login;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bizzmark.seller.sellerwithoutlogin.R;
import com.bizzmark.seller.sellerwithoutlogin.WifiDirectReceive;
import com.bizzmark.seller.sellerwithoutlogin.sellerapp.EarnPoints;

import static com.bizzmark.seller.sellerwithoutlogin.WifiDirectReceive.storeName;

public class Seller_Basic_Information extends AppCompatActivity implements View.OnClickListener {

   // private EditText store_name,branch_id,branch_name,branch_mgname,branch_address,branch_discount;
    private EditText storename,email,brid,brname,brmgrname,braddress,brdiscount;
    private Button seller_infosave;
    private ImageView backbut;
    String store_name;
    TextView sellerInfoHeader;
//    public static final String ROOT_URL="https://wwwbizzmarkin.000webhostapp.com/";

    TextView sellerStoreName, sellerStoreId, sellerBranchName, sellerBranchId, sellerPointsPercentage, sellerPointsValue, storeEmail, storeAddress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seller_basic_information);
     /*   seller_infosave=(Button)findViewById(R.id.seller_infosave);*/
        backbut=(ImageView)findViewById(R.id.backbut);

       /* storename = (EditText)findViewById(R.id.store_name);
        email = (EditText)findViewById(R.id.email);
        brid = (EditText)findViewById(R.id.branch_uid);
        brname = (EditText)findViewById(R.id.branch_name);
        brmgrname = (EditText)findViewById(R.id.branch_mgname);
        braddress = (EditText)findViewById(R.id.branch_address);
        brdiscount = (EditText)findViewById(R.id.branch_discount);
        sellerInfoHeader = (TextView) findViewById(R.id.sellerInfoHeader);
        sellerInfoHeader.setText(storeName);*/
        backbut.setOnClickListener(this);

/*        seller_infosave.setOnClickListener(this);

        store_name = storename.getText().toString();*/

        sellerStoreName = (TextView)findViewById(R.id.sellerStoreName);
        sellerStoreId = (TextView)findViewById(R.id.sellerStoreId);
        sellerBranchName = (TextView)findViewById(R.id.sellerBranchName);
        sellerBranchId = (TextView)findViewById(R.id.sellerBranchId);
        sellerPointsPercentage = (TextView)findViewById(R.id.sellerPointsPercentage);
        sellerPointsValue = (TextView)findViewById(R.id.sellerPointsValue);
        storeEmail = (TextView)findViewById(R.id.storeEmail);
        storeAddress = (TextView)findViewById(R.id.storeAddress);

    }

    //Method for onbackbut click


    private void backbut(){
        Seller_Basic_Information.super.onBackPressed();
    }

/*    public void saveInfo(){

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
    }*/


    @Override
    public void onClick(View v) {

        if (v== backbut){
            backbut();
        }
     /*   if (v== seller_infosave){
//            saveInfo();
//            Intent intent = new Intent(getApplicationContext(), WifiDirectReceive.class);
//            intent.putExtra("header_storeName", store_name);
//            getApplicationContext().startActivity(intent);
            finish();
        }*/

    }
}
