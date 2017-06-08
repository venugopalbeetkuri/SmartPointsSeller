package com.bizzmark.seller.sellerwithoutlogin.wifidirect_new;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.net.wifi.WpsInfo;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bizzmark.seller.sellerwithoutlogin.R;
import com.bizzmark.seller.sellerwithoutlogin.WifiDirectReceive;
import com.bizzmark.seller.sellerwithoutlogin.db.PointsBO;
import com.bizzmark.seller.sellerwithoutlogin.db.StoreBO;
import com.bizzmark.seller.sellerwithoutlogin.sellerapp.EarnPoints;
import com.bizzmark.seller.sellerwithoutlogin.sellerapp.RedeemPoints;
import com.bizzmark.seller.sellerwithoutlogin.wifidirect_new.service.FileTransferService;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.Channel;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import static android.R.attr.name;
import static android.R.attr.port;
import static android.R.attr.type;
import static com.bizzmark.seller.sellerwithoutlogin.login.Login.SELLER_STORENAE;
import static com.bizzmark.seller.sellerwithoutlogin.login.Login.sellerStoreName;

/**
 * Created by Tharun on 15-03-2017.
 */

public class DeviceDetailFragment extends Fragment implements WifiP2pManager.ConnectionInfoListener {

    protected static final int CHOOSE_FILE_RESULT_CODE = 20;
    private View mContentView = null;
    private WifiP2pDevice device;
    private WifiP2pInfo info;
    ProgressDialog progressDialog = null;

    private WifiP2pManager manager;
    private Channel channel;

    private FileServerAsyncTask mDataTask;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mContentView = inflater.inflate(R.layout.device_details, null);
        mContentView.findViewById(R.id.btn_connect).setVisibility(View.GONE);
        mContentView.findViewById(R.id.btn_disconnect).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((DeviceListFragment.DeviceActionListener) getActivity()).disconnect();
                mContentView.setVisibility(View.GONE);
            }
        });

        return mContentView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        // User has picked an image. Transfer it to group owner i.e peer using
        // FileTransferService.
//        Uri uri = data.getData();
//        TextView statusText = (TextView) mContentView.findViewById(R.id.status_text);
//        statusText.setText("Sending: " + uri);
//        Log.d(WifiDirectReceive.TAG, "Intent----------- " + uri);
//        Intent serviceIntent = new Intent(getActivity(), FileTransferService.class);
//        serviceIntent.setAction(FileTransferService.ACTION_SEND_FILE);
//        serviceIntent.putExtra(FileTransferService.EXTRAS_FILE_PATH, uri.toString());
//        serviceIntent.putExtra(FileTransferService.EXTRAS_GROUP_OWNER_ADDRESS,
//                info.groupOwnerAddress.getHostAddress());
//        serviceIntent.putExtra(FileTransferService.EXTRAS_GROUP_OWNER_PORT, 8988);
//        getActivity().startService(serviceIntent);

    }

    @Override
    public void onConnectionInfoAvailable(WifiP2pInfo info) {

        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        this.info = info;
        this.getView().setVisibility(View.VISIBLE);

        TextView view = (TextView) mContentView.findViewById(R.id.tv_group_owner);
        view.setText(getResources().getString(R.string.group_owner_text)) ;
        if (info.isGroupOwner == false){
            view.setText("No");
        }
        view = (TextView) mContentView.findViewById(R.id.tv_device_info);
//        view.setText("Group Owner IP - " + info.groupOwnerAddress.getHostAddress());


        // After the group negotiation, we assign the group owner as the file
        // server. The file server is single threaded, single connection server
        // socket.

        if (info.groupFormed && info.isGroupOwner) {

            new FileServerAsyncTask(getActivity(), mContentView.findViewById(R.id.status_text)).execute();
            mDataTask = new FileServerAsyncTask(DeviceDetailFragment.this);
            mDataTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }else if (info.groupFormed) {
            // The other device acts as the client. In this case, we enable the
            // get file button.
//            mContentView.findViewById(R.id.earnAcceptButton).setVisibility(View.VISIBLE);
//            ((TextView) mContentView.findViewById(R.id.status_text)).setText(getResources()
//                    .getString(R.string.client_text));
        }
    }

    public void showDetails(WifiP2pDevice device) {

    }

    public void resetViews() {

//        TextView view = (TextView) mContentView.findViewById(R.id.tv_device_address);
//        view.setText(R.string.empty);
//        view = (TextView) mContentView.findViewById(R.id.tv_device_info);
//        view.setText(R.string.empty);
//        view = (TextView) mContentView.findViewById(R.id.tv_group_owner);
//        view.setText(R.string.empty);
//        view = (TextView) mContentView.findViewById(R.id.status_text);
//        view.setText(R.string.empty);
    }


    ServerSocket serverSocket = null;

    private class FileServerAsyncTask extends AsyncTask<Void,Void,String> {

        String device_id,store_name,bill_amount,points_earn,date_time,earn_type;

        int ep = 0;
        String remoteAddress=null;

        private Context context;
        private TextView statusText;

        private DeviceDetailFragment activity;

        public FileServerAsyncTask(Context context, View statusText) {
            this.context = context;
            this.statusText = (TextView) statusText;
        }

        public FileServerAsyncTask(DeviceDetailFragment activity) {
            this.activity = activity;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... params) {

            try{

                if(null == serverSocket) {
                    Log.i("bizzmark", "data doing back");
                    serverSocket = new ServerSocket(8888);
                    serverSocket.setReuseAddress(true);
                }

                Log.i("bizzmark","Opening socket on 8888.");
                Socket client = serverSocket.accept();

                remoteAddress=((InetSocketAddress)client.getRemoteSocketAddress()).getAddress().getHostName();

                Log.i("bizzmark","Client connected.");
                InputStream inputStream = client.getInputStream();

                ByteArrayOutputStream baos = new ByteArrayOutputStream();

                int i;

                while ((i = inputStream.read()) != -1){
                    baos.write(i);
                }

                String str = baos.toString();

                return str;
            }catch (Exception e){
                e.printStackTrace();
                try {
                    if(null != serverSocket) {
                        //serverSocket.close();
                        //serverSocket = null;
                    }
                }catch (Throwable th){
                    th.printStackTrace();
                }
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {

            Log.i("bizzmark", "data on post execute.Result: " + result);

//            Toast.makeText(getActivity(), "From customer: " + result, Toast.LENGTH_SHORT).show();

            Log.i("bizzmark","data on post executr.Result: "+result);
            if(result != null) {

                try {
                    JSONObject obj = new JSONObject(result);
                    earn_type = obj.getString("type");
                    store_name = obj.getString("storeName");
                }catch (Exception e){
                    e.printStackTrace();
                }
                if (store_name.equalsIgnoreCase(SELLER_STORENAE)){

                    if (earn_type.equals("earn")) {
                        Intent intent = new Intent(getActivity(), EarnPoints.class);
                        intent.putExtra("earnRedeemString", result);
                        intent.putExtra("GroupOwnerAddress", remoteAddress);
                        getActivity().startActivity(intent);
                    } else if (earn_type.equals("redeem")) {
                        Intent intent = new Intent(getActivity(), RedeemPoints.class);
                        intent.putExtra("earnRedeemString", result);
                        intent.putExtra("GroupOwnerAddress", remoteAddress);
                        getActivity().startActivity(intent);
                    }
                }
                else if (!store_name.equalsIgnoreCase(SELLER_STORENAE)){

                    try {
                        new AlertDialog.Builder(getActivity())
                                .setMessage("Custmer scanned QR code is not valid ask him/her scan again")
                                .setCancelable(true)
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                }).create().show();
                    } catch (Exception e ){
                        e.printStackTrace();
                    }
//                    Toast.makeText(getActivity(),"Customer Scanned QR code not valid ",Toast.LENGTH_LONG).show();
                }
            }

        }
    }

    public static boolean copyFile(InputStream inputStream, OutputStream out) {

        byte buf[] = new byte[1024];
        int len;
        long startTime=System.currentTimeMillis();

        try {
            while ((len = inputStream.read(buf)) != -1) {
                out.write(buf, 0, len);
            }
            out.close();
            inputStream.close();
            long endTime=System.currentTimeMillis()-startTime;
            Log.v("","Time taken to transfer all bytes is : "+endTime);

        } catch (IOException e) {
            Log.d(WifiDirectReceive.TAG, e.toString());
            return false;
        }
        return true;
    }
}
