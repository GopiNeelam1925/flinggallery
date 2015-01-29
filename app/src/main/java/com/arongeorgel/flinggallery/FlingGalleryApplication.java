package com.arongeorgel.flinggallery;

import android.app.Application;

import com.activeandroid.ActiveAndroid;
import com.arongeorgel.flinggallery.network.BusProvider;
import com.arongeorgel.flinggallery.network.FlingRestService;
import com.arongeorgel.flinggallery.network.NetworkConstants;
import com.arongeorgel.flinggallery.network.NetworkManager;
import com.arongeorgel.flinggallery.persistance.ImageBean;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.squareup.otto.Bus;

import retrofit.RestAdapter;

/**
 * Extension for android application in order to handle and initialize global values
 *
 * Created by arongeorgel on 27/01/2015.
 */
public class FlingGalleryApplication extends Application {
    private static FlingGalleryApplication sInstance;
    private static final int MAX_CACHE_IMAGES = 100;
    private static final int IMAGE_CACHE_SIZE = 100 * 1024 * 1024;

    private DisplayImageOptions defaultImageOptions;
    private NetworkManager mManager;
    private Bus mBus = BusProvider.getInstance();

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;

        mManager = new NetworkManager(buildApi(), mBus);
        mBus.register(mManager);
        initImageCache();
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
     * initialize image cache here
     * current we are using AUIL - https://github.com/nostra13/Android-Universal-Image-Loader
     */
    private void initImageCache() {
        defaultImageOptions = new DisplayImageOptions.Builder()
                .cacheOnDisk(false)
                .cacheInMemory(true)
                .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
                .displayer(new FadeInBitmapDisplayer(300))
                .resetViewBeforeLoading(true)
                .build();

        ImageLoaderConfiguration loaderConfig = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .defaultDisplayImageOptions(defaultImageOptions)
                .threadPoolSize(5)
                .threadPriority(Thread.MAX_PRIORITY - 1)
                .memoryCache(new LruMemoryCache(200))

                .build();
        ImageLoader.getInstance().init(loaderConfig);
    }

    /**
     *
     * @return {@link com.arongeorgel.flinggallery.FlingGalleryApplication} instance
     */
    public static FlingGalleryApplication getInstance() {
        return sInstance;
    }

    /**
     *
     * @return default options for used image cache library
     */
    public DisplayImageOptions getDefaultImageOptions() {
        return defaultImageOptions;
    }
}
