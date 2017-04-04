package com.bizzmark.seller.sellerwithoutlogin.wifidirect_new.adapter;

import android.content.Context;
import android.net.wifi.p2p.WifiP2pDevice;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.bizzmark.seller.sellerwithoutlogin.R;

import java.util.List;

public class WifiPeerListAdapter extends ArrayAdapter<WifiP2pDevice> {

    private List<WifiP2pDevice> items;

    public WifiPeerListAdapter(Context context, int textViewResourceId, List<WifiP2pDevice> objects) {
        super(context, textViewResourceId,objects);
        items = objects;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;
        if (view == null){
            LayoutInflater li = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = li.inflate(R.layout.row_devices,null);
        }

        WifiP2pDevice device = items.get(position);
        if (device != null){
            TextView top = (TextView) view.findViewById(R.id.tv_device_name);
            TextView bottom = (TextView) view.findViewById(R.id.tv_device_details);
            if (top != null){
                top.setText(device.deviceName);
            }
            if (bottom != null){
                bottom.setText(getDeviceStatus(device.status));
            }
        }

        return view ;
    }

    private String getDeviceStatus(int deviceStatus) {
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
}
