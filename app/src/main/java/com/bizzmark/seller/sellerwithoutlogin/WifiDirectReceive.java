package com.bizzmark.seller.sellerwithoutlogin;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bizzmark.seller.sellerwithoutlogin.Notifications.Notifications;
import com.bizzmark.seller.sellerwithoutlogin.Reports.Last_Ten_Transactions.LastTenTrans;
import com.bizzmark.seller.sellerwithoutlogin.My_Customers.MyCustomers;
import com.bizzmark.seller.sellerwithoutlogin.login.Login;
import com.bizzmark.seller.sellerwithoutlogin.sellerInfo.Seller_Basic_Information;
import com.bizzmark.seller.sellerwithoutlogin.sellerapp.ReportActivity;
import com.bizzmark.seller.sellerwithoutlogin.wifidirect_new.DeviceDetailFragment;
import com.bizzmark.seller.sellerwithoutlogin.wifidirect_new.DeviceListFragment;
import com.bizzmark.seller.sellerwithoutlogin.wifidirect_new.Settings;
import com.bizzmark.seller.sellerwithoutlogin.wifidirect_new.broadcastreceiver.WifiBroadCastReceiver;
import com.google.firebase.iid.FirebaseInstanceId;

import java.io.File;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import static com.bizzmark.seller.sellerwithoutlogin.R.id.nav_customerList;
//import static com.bizzmark.seller.sellerwithoutlogin.R.id.nav_notifications;
import static com.bizzmark.seller.sellerwithoutlogin.R.id.nav_notifications;
import static com.bizzmark.seller.sellerwithoutlogin.login.Login.SELLER_STORENAE;
import static com.bizzmark.seller.sellerwithoutlogin.util.UrlUtils.SEND_DEVICE_TOKEN;

public class WifiDirectReceive extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener, WifiP2pManager.ChannelListener, DeviceListFragment.DeviceActionListener {

    final Context context = this;
    public ImageView imgmenu,sellerimg;
    public ImageButton action_logout, action_Share;


    public TextView navStoreName,headerStoreName,versionName, versionCode;
    public static String storeName;
    public String statusN;

    public static final int REQUEST_READ_PERMISSION = 1;

    SwipeRefreshLayout refreshLayout;
    public static final String TAG = "smartpointseller";
    private boolean isWifiP2pEnabled = false;
    private WifiP2pManager manager;
    private boolean retryChannel = false;
    private final IntentFilter intentFilter = new IntentFilter();
    private WifiP2pManager.Channel channel;
    private BroadcastReceiver receiver = null;

    private Button btnRefresh,report;
    //Textview variable  for displaying device id
    public String version_Name;
    public int version_Code;
    TextView device_id;
    String deviceId;
    public void setIsWifiP2pEnabled(boolean isWifiP2pEnabled){
        this.isWifiP2pEnabled = isWifiP2pEnabled;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_direct_receive);

        // for enable wifi
        WifiManager wifi = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        wifi.setWifiEnabled(true);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View v = navigationView.getHeaderView(0);
        navigationView.setNavigationItemSelectedListener(this);

        imgmenu=(ImageView)findViewById(R.id.imgmenu);
        imgmenu.setOnClickListener(this);

        navStoreName = (TextView) v.findViewById(R.id.nav_storeName);
        headerStoreName =(TextView) findViewById(R.id.header_storeName);

        try {
            if (SELLER_STORENAE.isEmpty()){
                try {
                    new AlertDialog.Builder(WifiDirectReceive.this)
                            .setTitle("Session Expired")
                            .setMessage("Login Again")
                            .setCancelable(false)
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    SharedPreferences preferences = getSharedPreferences("STORE_DETAILS",Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = preferences.edit();
                                    editor.clear();
                                    editor.commit();
                                    finish();
                                    Intent i=new Intent(WifiDirectReceive.this,Login.class);
                                    startActivity(i);
                                }
                            }).create().show();
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
            else {
                headerStoreName.setText(SELLER_STORENAE);
                navStoreName.setText(SELLER_STORENAE);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        sellerimg=(ImageView)v.findViewById(R.id.sellerimg);
        sellerimg.setOnClickListener(this);

        version_Name = BuildConfig.VERSION_NAME;
        version_Code = BuildConfig.VERSION_CODE;

        versionName = (TextView)v.findViewById(R.id.version_Name);
        versionName.setText(version_Name);
        versionName.setEnabled(true);
//        versionCode = (TextView)findViewById(R.id.version_Code);
//        versionCode.setText(version_Code);
//        versionCode.setEnabled(true);
        btnRefresh=(Button)findViewById(R.id.btnRefresh);
        btnRefresh.setOnClickListener(this);

//        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeLoadCusts);
//        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                WifiManager wifi = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
//                wifi.setWifiEnabled(true);
//                refreshLayout.setRefreshing(false);
//                discoverPeers();
//            }
//        });

//        action_logout=(ImageButton)findViewById(R.id.action_logout);
//        action_logout.setOnClickListener(this);

        action_Share = (ImageButton)findViewById(R.id.action_Share);
        action_Share.setOnClickListener(this);

        report=(Button)findViewById(R.id.report);
        report.setOnClickListener(this);

        device_id=(TextView)v.findViewById(R.id.device_id);

        /* add necessary intent values to be matched.*/

        intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);

        manager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        channel =  manager.initialize(this,getMainLooper(),null);
//        cancelConnect();
        discoverPeers();
        runTimePermission();
        device_id.setText(deviceId);

        getDeviceID();


//        shareSmartPoints();
    }

    private void getDeviceID() {
        if (FirebaseInstanceId.getInstance().getToken() != null) {
            String deviceToken = FirebaseInstanceId.getInstance().getToken();
            Log.v("GCMID", deviceToken);
            sendDeviceToken(deviceToken);
        }
    }

    private void sendDeviceToken(final String deviceToken) {
        deviceId = getIMEIstring();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, SEND_DEVICE_TOKEN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(WifiDirectReceive.this, "Something went wrong, please try again", Toast.LENGTH_SHORT).show();
                //etPassword.setText("");
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("deviceId", deviceId);
                parameters.put("devicetoken", deviceToken);
                parameters.put("usertype", "seller");
                return parameters;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                300000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);

    }


/*Method for getting SmartPointS package*/

    public void shareSmartPoints(){
        PackageManager packageManager = getApplicationContext().getPackageManager();
        PackageInfo packageInfo;
        ApplicationInfo applicationInfo = null;

        try {

            packageInfo = packageManager.getPackageInfo("in.bizzmark.smartpoints_user", 0);
            applicationInfo = packageInfo.applicationInfo;
            shareAppByBluetooth(applicationInfo);
        }
        catch (PackageManager.NameNotFoundException e){
            e.printStackTrace();
        }
    }

/*Metjod for Share SmartPoints Apk Using Bluetooth*/

    private void shareAppByBluetooth(ApplicationInfo applicationInfo){
        try {
            String filePath =  applicationInfo.sourceDir;
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("*/*");
            /*Using only bluetooth to send application*/
            intent.setPackage("com.android.bluetooth");
            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(filePath)));
            startActivity(Intent.createChooser(intent,"Share app"));
            Toast.makeText(getApplicationContext(),"Share SmartPoints app",Toast.LENGTH_LONG).show();
        }
        catch (Exception e){
            e.printStackTrace();
        }
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
            deviceId = getIMEIstring();
        }
    }

    /*getting device Id*/

    private String getIMEIstring() {
        try {
            TelephonyManager telephonyManager = (TelephonyManager) this
                    .getSystemService(Context.TELEPHONY_SERVICE);
            deviceId = telephonyManager.getDeviceId();
        }catch (Throwable throwable){
            throwable.printStackTrace();
        }
        return deviceId;
    }

        /*getting run time permission*/

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case REQUEST_READ_PERMISSION:{
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

                    Toast.makeText(context,"Permission granted.",Toast.LENGTH_LONG).show();
                    getIMEIstring();
                    discoverPeers();
                }else {
                    Toast.makeText(context,"The app was not allowed to get your phone state. Hence, it cannot run. Please consider granting it this permission",Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    /*peers discovery*/

    public void discoverPeers(){
        if (!isWifiP2pEnabled){
      //      Toast.makeText(this, "Enable P2P from action bar button above or system settings",
        //            Toast.LENGTH_SHORT).show();
        }

        DeviceListFragment deviceListFragment = (DeviceListFragment) getFragmentManager()
                .findFragmentById(R.id.frag_list);
        deviceListFragment.onInitiateDiscovery();
        manager.discoverPeers(channel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
          //      Toast.makeText(WifiDirectReceive.this, "Discovery initiated", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int reason) {
//                Toast.makeText(WifiDirectReceive.this, "Discovery failed :" + reason,
//                        Toast.LENGTH_SHORT).show();

            }
        });
    }

    /*Cancel Invitation*/
    public void cancelConnect(){
        if (manager != null){
            manager.cancelConnect(channel, new WifiP2pManager.ActionListener() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onFailure(int reason) {

                }
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        receiver = new WifiBroadCastReceiver(manager,channel,this);
        registerReceiver(receiver,intentFilter);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        cancelConnect();
        discoverPeers();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }

    public void resetData(){
        DeviceListFragment fragmentList = (DeviceListFragment)
                getFragmentManager().findFragmentById(R.id.frag_list);
        DeviceDetailFragment fragmentDetails = (DeviceDetailFragment)
                getFragmentManager().findFragmentById(R.id.frag_detals);
        if (fragmentList != null){
            fragmentList.cleerPeers();
        }
        if (fragmentDetails != null){
            fragmentDetails.resetViews();
        }
    }
    @Override
    public void showDetails(WifiP2pDevice device) {

        DeviceDetailFragment fragment = (DeviceDetailFragment) getFragmentManager()
                .findFragmentById(R.id.frag_detals);
        fragment.showDetails(device);

    }

    @Override
    public void connect(WifiP2pConfig config) {
        manager.connect(channel, config, new WifiP2pManager.ActionListener() {

            @Override
            public void onSuccess() {
                // WiFiDirectBroadcastReceiver will notify us. Ignore for now.
            }

            @Override
            public void onFailure(int reason) {
//                Toast.makeText(WifiDirectReceive.this, "Connect failed. Retry.",
//                        Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void disconnect() {

        final DeviceDetailFragment fragment = (DeviceDetailFragment) getFragmentManager()
                .findFragmentById(R.id.frag_detals);
        fragment.resetViews();
        manager.removeGroup(channel, new WifiP2pManager.ActionListener() {

            @Override
            public void onFailure(int reasonCode) {
                Log.d(TAG, "Disconnect failed. Reason :" + reasonCode);
            }

            @Override
            public void onSuccess() {
                fragment.getView().setVisibility(View.GONE);
            }

        });

    }

    @Override
    public void onChannelDisconnected() {

        // we will try once more
        if (manager != null && !retryChannel) {
//            Toast.makeText(this, "Channel lost. Trying again", Toast.LENGTH_LONG).show();
            resetData();
            retryChannel = true;
            manager.initialize(this, getMainLooper(), this);
        } else {
//            Toast.makeText(this,
//                    "Severe! Channel is probably lost premanently. Try Disable/Re-Enable P2P.",
//                    Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void cancelDisconnectt() {

          /*
         * A cancel abort request by user. Disconnect i.e. removeGroup if
         * already connected. Else, request WifiP2pManager to abort the ongoing
         * request
         */
        if (manager != null) {
            final DeviceListFragment fragment = (DeviceListFragment) getFragmentManager()
                    .findFragmentById(R.id.frag_list);
            if (fragment.getDevice() == null
                    || fragment.getDevice().status == WifiP2pDevice.CONNECTED) {
                disconnect();
            } else if (fragment.getDevice().status == WifiP2pDevice.AVAILABLE
                    || fragment.getDevice().status == WifiP2pDevice.INVITED) {

                manager.cancelConnect(channel, new WifiP2pManager.ActionListener() {

                    @Override
                    public void onSuccess() {
//                        Toast.makeText(WifiDirectReceive.this, "Aborting connection",
//                                Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(int reasonCode) {
//                        Toast.makeText(WifiDirectReceive.this,
//                                "Connect abort request failed. Reason Code: " + reasonCode,
//                                Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }

    }

    /*clearing remembered groups */

    private void deletePersistentGroups(){

        try {
            Method[] methods = WifiP2pManager.class.getMethods();
            for (int i = 0; i < methods.length; i++) {
                if (methods[i].getName().equals("deletePersistentGroup")) {
                    // Delete any persistent group
                    for (int netid = 0; netid < 32; netid++) {
                        methods[i].invoke(manager, channel, netid, null);
                    }
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
        drawer.closeDrawer(GravityCompat.START);
        }  else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        //StopConnect();
                     }
                });
            AlertDialog alert = builder.create();
            alert.show();
        }

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater menuInflater = getMenuInflater();

        getMenuInflater().inflate(R.menu.wifi_direct_receive, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_report){
            Intent i=new Intent(WifiDirectReceive.this, ReportActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_terms_conditions) {
            Intent i = new Intent(getApplication(), Terms.class);
            startActivity(i);
        } else if (id == R.id.nav_privacy_policy) {
            Intent i=new Intent(getApplicationContext(),PrivacyPolicy.class);
            startActivity(i);
        }
        else if (id == nav_notifications){
            Intent intent = new Intent(getApplicationContext(), Notifications.class);
            startActivity(intent);
        }
        else if (id == nav_customerList){
            Intent intent = new Intent(getApplicationContext(), MyCustomers.class);
            startActivity(intent);
        } else if (id == R.id.nav_setting){
            Intent i = new Intent(getApplicationContext(), Settings.class);
            startActivity(i);
        } else if (id == R.id.nav_contact_us) {
            contactus();
        } else if (id == R.id.nav_exit){
            exit();
        } else if (id == R.id.nav_logOut){
            logout();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View v) {

        if (v == btnRefresh){
            Animation rotation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.buttonrotate);
            rotation.start();
            WifiManager wifi = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            wifi.setWifiEnabled(true);
            btnRefresh.startAnimation(rotation);
            cancelConnect();
            deletePersistentGroups();
            disconnect();
            discoverPeers();
        }
//        if (v==action_logout){
//            logout();
//        }
        if (v == imgmenu){
            slidemenu();
        }
        if (v == sellerimg){
            Intent i=new Intent(WifiDirectReceive.this, Seller_Basic_Information.class);
            startActivity(i);
        }
        if(v == report){
            Intent intent = new Intent(this,LastTenTrans.class);
            startActivity(intent);
        }

        if (v == action_Share){
            shareSmartPoints();
//            shareAppByBluetooth();
        }

    }

    /*method for slide menu button*/
    public void slidemenu(){
        DrawerLayout slider=(DrawerLayout)findViewById(R.id.drawer_layout);
        slider.openDrawer(Gravity.LEFT);
    }

    /* method for exit the app*/
    public void exit(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    /* method for contactus*/
    private void contactus() {

        try {

            AlertDialog.Builder builder1 = new AlertDialog.Builder(context);


            builder1.setMessage("For Qurries contact this email \n bizzmark.in@gmail.com");
            builder1.setCancelable(true);
            builder1.setIcon(R.drawable.ic_launcher);
            builder1.setPositiveButton("SEND FEEDBACK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                    dialog.dismiss();
                    startActivity(new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:bizzmark.in@gmail.com")));
                }
            });
            builder1.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            AlertDialog alert11 = builder1.create();
            alert11.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* method for logout*/
    public void logout(){
//        FirebaseUser user=firebaseAuth.getCurrentUser();
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setMessage("Are you sure to logout")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        accessToken=null;
                        SharedPreferences preferences = getSharedPreferences("STORE_DETAILS",Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.clear();
                        editor.commit();
                        finish();
                        Intent i=new Intent(WifiDirectReceive.this,Login.class);
                        startActivity(i);
                    }
                }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).create().show();

    }

}
