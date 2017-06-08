package com.bizzmark.seller.sellerwithoutlogin.wifidirect_new;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bizzmark.seller.sellerwithoutlogin.R;
import com.bizzmark.seller.sellerwithoutlogin.login.ForgetPassword;

import static com.bizzmark.seller.sellerwithoutlogin.WifiDirectReceive.storeName;
import static com.bizzmark.seller.sellerwithoutlogin.login.Login.SELLER_STORENAE;

public class Settings extends AppCompatActivity implements View.OnClickListener {

    Button rename, resetPassword;
    WifiP2pManager.Channel mChannel;
    private WifiP2pManager manager;
    String myNewDeviceName = "nothingNew";
    String TAGF = "SPAWRKS";
    Context mContext;
    TextView settingHeader;
    private ImageView backbut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        rename=(Button)findViewById(R.id.rename);
        resetPassword=(Button)findViewById(R.id.resetPassword);
        resetPassword.setEnabled(false);
        settingHeader = (TextView) findViewById(R.id.settingHeader);
        settingHeader.setText(SELLER_STORENAE);
        backbut=(ImageView)findViewById(R.id.backbut);
        backbut.setOnClickListener(this);
        rename.setOnClickListener(this);
//        resetPassword.setOnClickListener(this);
    }

    private void backbut(){
        Settings.super.onBackPressed();
    }

    public void performMagic(String input)
    {
        // inner class needs to have access to new name
        myNewDeviceName = input;
        //  This will get the WifiDirect manager for use
        manager=(WifiP2pManager)getSystemService(Context.WIFI_P2P_SERVICE);
        if (manager!=null)
        {
            mChannel=manager.initialize(this,getMainLooper(),null);
            if (mChannel==null)
            {
                //Failure to set up connection
                Log.e(TAGF, "Failed to set up connection with wifi p2p service");
                manager=null;
            }
            else
            {
                Log.e(TAGF, "mWifiP2pManager is null !");
            }
            //  Setup for using the reflection API to actually call the methods we want

            int numberOfParams = 3;
            Class[] methodParameters = new Class[numberOfParams];
            methodParameters[0] = WifiP2pManager.Channel.class;
            methodParameters[1] = String.class;
            methodParameters[2] = WifiP2pManager.ActionListener.class;


            Object arglist[] = new Object[numberOfParams];
            arglist[0] = mChannel;
            arglist[1] = myNewDeviceName;
            arglist[2] = new WifiP2pManager.ActionListener()
            {
                @Override
                public void onSuccess()
                {
                    String resultString = "Changed to " + myNewDeviceName;
                    Log.e("SECRETAPI", resultString);
                    Toast.makeText(getApplicationContext(), resultString, Toast.LENGTH_LONG).show();
                }

                @Override
                public void onFailure(int reason)
                {
                    String resultString = "Fail reason: " + String.valueOf(reason);
                    Log.e("SECRETAPI",resultString);
                    Toast.makeText(getApplicationContext(), resultString, Toast.LENGTH_LONG).show();
                }
            };
            //  Now we use the reflection API to call a method we normally wouldnt have access to.
            ReflectionUtils.executePrivateMethod(manager,WifiP2pManager.class,"setDeviceName",methodParameters,arglist);
        }

    }

    @Override
    public void onClick(View v) {

        if ( v == rename){
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1)
            {
                // Marshmallow+
                startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS), 0);
            }

            else
            {
                /*final AlertDialog.Builder alert = new AlertDialog.Builder(Settings.this);
                final EditText input = new EditText(Settings.this);
                input.setSingleLine();
                input.setPadding(50, 0, 50, 0);

                alert.setTitle("Rename Device");
                alert.setMessage(" Enter the Device Name :");
                alert.setView(input);
                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String value = input.getText().toString().trim();
                        performMagic(value);

                    }
                });

                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.cancel();
                    }
                });
                alert.setCancelable(false);
                alert.create();
                alert.show();*/
            }

        }

        if ( v == resetPassword){
            Intent intent = new Intent(this, ForgetPassword.class);
            startActivity(intent);
        }

        if (v== backbut){
            backbut();
        }

    }
}
