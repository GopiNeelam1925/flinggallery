package com.arongeorgel.flinggallery.network;

import com.squareup.otto.Bus;

/**
 * Created by arongeorgel on 29/01/2015.
 */
public final class BusProvider {
    private static final Bus sInstance = new Bus();

    public static Bus getInstance() {
        return sInstance;
    }

    private BusProvider() {
        // No instances.
    }
}
