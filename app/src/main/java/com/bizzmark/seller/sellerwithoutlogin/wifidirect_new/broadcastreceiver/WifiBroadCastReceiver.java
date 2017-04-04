package com.bizzmark.seller.sellerwithoutlogin.wifidirect_new.broadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pManager;
import android.util.Log;

import com.bizzmark.seller.sellerwithoutlogin.R;
import com.bizzmark.seller.sellerwithoutlogin.WifiDirectReceive;
import com.bizzmark.seller.sellerwithoutlogin.wifidirect_new.DeviceDetailFragment;
import com.bizzmark.seller.sellerwithoutlogin.wifidirect_new.DeviceListFragment;

/**
 * Created by Tharun on 15-03-2017.
 */

public class WifiBroadCastReceiver  extends BroadcastReceiver {

    private WifiP2pManager manager;
    private WifiP2pManager.Channel channel;
    private WifiDirectReceive activity;


    public WifiBroadCastReceiver(WifiP2pManager manager,
                                 WifiP2pManager.Channel channel,
                                 WifiDirectReceive activity) {

        this.manager = manager;
        this.channel =  channel;
        this.activity = activity;

    }

    @Override
    public void onReceive(Context context, Intent intent) {

        String action = intent.getAction();
        if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)){

            // UI update to indicate wifi p2p status.
            int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1);
            if (state == WifiP2pManager.WIFI_P2P_STATE_ENABLED){
                // Wifi Direct mode is enabled
                activity.setIsWifiP2pEnabled(true);
            }else {
                activity.setIsWifiP2pEnabled(false);
                activity.resetData();
            }
            Log.d(WifiDirectReceive.TAG,"P2p state changed - " + state);
        }else if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)){
            if (manager != null){
                manager.requestPeers(channel, (WifiP2pManager.PeerListListener) activity.getFragmentManager()
                        .findFragmentById(R.id.frag_list));
            }
            Log.d(WifiDirectReceive.TAG, "P2p peers changed");
        }else if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)){
            if (manager == null){
                return;
            }

            NetworkInfo networkInfo = (NetworkInfo) intent.getParcelableExtra(WifiP2pManager.EXTRA_NETWORK_INFO);
            if (networkInfo.isConnected()){
                DeviceDetailFragment deviceDetailFragment = (DeviceDetailFragment)
                        activity.getFragmentManager().findFragmentById(R.id.frag_detals);
                manager.requestConnectionInfo(channel,deviceDetailFragment);
            }else {
                activity.resetData();
            }
        }else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action)){

            DeviceListFragment deviceListFragment = (DeviceListFragment)
                    activity.getFragmentManager().findFragmentById(R.id.frag_list);

            deviceListFragment.upDateThisDevice((WifiP2pDevice)
                    intent.getParcelableExtra(WifiP2pManager.EXTRA_WIFI_P2P_DEVICE));
        }

    }
}
