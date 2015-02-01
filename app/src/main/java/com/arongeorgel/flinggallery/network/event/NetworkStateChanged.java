package com.arongeorgel.flinggallery.network.event;

/**
 * Network state event
 * Created by arongeorgel on 31/01/2015.
 */
public class NetworkStateChanged {
    private boolean mConnected;

    public NetworkStateChanged(boolean connected) {
        mConnected = connected;
    }

    public boolean isConnected() {
        return mConnected;
    }
}
