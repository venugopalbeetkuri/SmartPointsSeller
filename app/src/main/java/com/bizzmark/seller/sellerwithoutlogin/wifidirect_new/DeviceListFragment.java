package com.bizzmark.seller.sellerwithoutlogin.wifidirect_new;

import android.app.ListFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bizzmark.seller.sellerwithoutlogin.R;
import com.bizzmark.seller.sellerwithoutlogin.WifiDirectReceive;
import com.bizzmark.seller.sellerwithoutlogin.wifidirect_new.adapter.WifiPeerListAdapter;

import java.util.ArrayList;
import java.util.List;

public class DeviceListFragment extends ListFragment implements WifiP2pManager.PeerListListener {

    private List<WifiP2pDevice> peers = new ArrayList<WifiP2pDevice>();
    ProgressDialog progressDialog = null;
    View mContentView = null;
    private WifiP2pDevice device;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.setListAdapter(new WifiPeerListAdapter(getActivity(), R.layout.row_devices, peers));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContentView = inflater.inflate(R.layout.device_list, null);
        return mContentView;
    }

    public WifiP2pDevice getDevice() {
        return device;
    }

    private static String getDeviceStatus(int deviceStatus) {
        switch (deviceStatus) {
            case WifiP2pDevice.AVAILABLE:
                return "Available";
            case WifiP2pDevice.INVITED:
                return "Invited";
            case WifiP2pDevice.CONNECTED:
                return "Connected";
            case WifiP2pDevice.FAILED:
                return "Failed";
            case WifiP2pDevice.UNAVAILABLE:
                return "Unavailable";
            default:
                return "Unknown";
        }
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        WifiP2pDevice device = (WifiP2pDevice) getListAdapter().getItem(position);
        ((DeviceActionListener) getActivity()).showDetails(device);
    }

    public class WifiPeerListAdapter extends ArrayAdapter<WifiP2pDevice> {

        private List<WifiP2pDevice> items;

        public WifiPeerListAdapter(Context context, int textViewResourceId, List<WifiP2pDevice> objects) {
            super(context, textViewResourceId, objects);
            items = objects;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View view = convertView;
            if (view == null) {
                LayoutInflater li = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = li.inflate(R.layout.row_devices, null);
            }

            WifiP2pDevice device = items.get(position);
            if (device != null) {
                TextView top = (TextView) view.findViewById(R.id.tv_device_name);
                TextView bottom = (TextView) view.findViewById(R.id.tv_device_details);
                if (top != null) {
                    top.setText(device.deviceName);
                    if (getDeviceStatus(device.status).equals("Connected")){
                       top.setBackgroundColor(getResources().getColor(R.color.GREEN));
                        top.setTextColor(Color.WHITE);

                    }
                    else if(getDeviceStatus(device.status).equals("Invited")){
                        top.setBackgroundColor(getResources().getColor(R.color.RED));
                        top.setTextColor(Color.WHITE);
                    }else {
                        top.setBackgroundColor(Color.BLUE);
                        top.setTextColor(Color.WHITE);
                    }
                }
                if (bottom != null) {
                    bottom.setText(getDeviceStatus(device.status));
                    if (getDeviceStatus(device.status).equals("Connected")){
                        //card_bg.setBackgroundColor(Color.GREEN);
                        bottom.setBackgroundColor(getResources().getColor(R.color.GREEN));
                        bottom.setTextColor(Color.WHITE);
                    }
                    else if(getDeviceStatus(device.status).equals("Invited")){
                        bottom.setBackgroundColor(getResources().getColor(R.color.RED));
                        bottom.setTextColor(Color.WHITE);
                    }else {
                        bottom.setBackgroundColor(Color.BLUE);
                        bottom.setTextColor(Color.WHITE);
                    }
                }
                if (getDeviceStatus(device.status).equals("Connected")){
                }
            }

            return view;
        }

    }

    public void upDateThisDevice(WifiP2pDevice device) {

        this.device = device;
        TextView view = (TextView) mContentView.findViewById(R.id.my_name);
        view.setText(device.deviceName);
        view = (TextView) mContentView.findViewById(R.id.my_status);
        view.setText(getDeviceStatus(device.status));
    }


    @Override
    public void onPeersAvailable(WifiP2pDeviceList peerList) {

        if (progressDialog != null && progressDialog.isShowing()){
            progressDialog.dismiss();
        }
        peers.clear();
        peers.addAll(peerList.getDeviceList());
        ((WifiPeerListAdapter) getListAdapter()).notifyDataSetChanged();

        if (peers.size() == 0){
            Log.d(WifiDirectReceive.TAG, "No device found");
            return;
        }
    }

    public void cleerPeers(){
        peers.clear();
        ((WifiPeerListAdapter) getListAdapter()).notifyDataSetChanged();
    }

    public void onInitiateDiscovery(){
        if (progressDialog != null && progressDialog.isShowing()){
            progressDialog.dismiss();
        }

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Press back to cancel");
        progressDialog.setMessage("finding Customers");
        progressDialog.show();
    }


    public interface DeviceActionListener {

        void showDetails(WifiP2pDevice device);

        void cancelDisconnectt();

        void connect(WifiP2pConfig config);

        void disconnect();
    }
}
