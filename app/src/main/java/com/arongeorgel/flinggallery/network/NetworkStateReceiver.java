package com.arongeorgel.flinggallery.network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.arongeorgel.flinggallery.network.event.NetworkStateChanged;

/**
 * Broadcast receiver used to receive events when internet connections has changed
 *
 * Created by arongeorgel on 31/01/2015.
 */
public class NetworkStateReceiver extends BroadcastReceiver {

    // post event if there is no Internet connection
    public void onReceive(Context context, Intent intent) {
        if (intent.getExtras() != null) {
            NetworkInfo ni = (NetworkInfo) intent.getExtras().get(ConnectivityManager.EXTRA_NETWORK_INFO);
            if (ni != null && ni.getState() == NetworkInfo.State.CONNECTED) {
                BusProvider.getInstance().post(new NetworkStateChanged(true));
            } else if (intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, Boolean.FALSE)) {
                BusProvider.getInstance().post(new NetworkStateChanged(true));

            }
        }
    }
}
