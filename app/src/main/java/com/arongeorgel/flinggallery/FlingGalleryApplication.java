package com.arongeorgel.flinggallery;

import android.app.Application;

import com.activeandroid.ActiveAndroid;
import com.arongeorgel.flinggallery.network.BusProvider;
import com.arongeorgel.flinggallery.network.FlingRestService;
import com.arongeorgel.flinggallery.network.NetworkConstants;
import com.arongeorgel.flinggallery.network.NetworkManager;
import com.arongeorgel.flinggallery.persistance.ImageBean;
import com.squareup.otto.Bus;

import retrofit.RestAdapter;

/**
 * Extension for android application in order to handle and initialize global values
 *
 * Created by arongeorgel on 27/01/2015.
 */
public class FlingGalleryApplication extends Application {
    private static FlingGalleryApplication sInstance;
    private NetworkManager mManager;
    private Bus mBus = BusProvider.getInstance();

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;

        mManager = new NetworkManager(buildApi(), mBus);
        mBus.register(mManager);
        initDb();

    }

    private void initDb() {
        com.activeandroid.Configuration.Builder builder = new com.activeandroid.Configuration.Builder(this);
        builder.addModelClass(ImageBean.class);

        ActiveAndroid.initialize(builder.create());
    }

    private FlingRestService buildApi() {
        RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(NetworkConstants.BASE_URL)
                .build();
        return adapter.create(FlingRestService.class);
    }

    /**
     *
     * @return {@link com.arongeorgel.flinggallery.FlingGalleryApplication} instance
     */
    public static FlingGalleryApplication getInstance() {
        return sInstance;
    }
}
