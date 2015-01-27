package com.arongeorgel.flinggallery;

import android.app.Application;
import android.os.AsyncTask;

import com.nostra13.universalimageloader.cache.memory.impl.LimitedAgeMemoryCache;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

/**
 * Created by arongeorgel on 27/01/2015.
 */
public class FlingGalleryApplication extends Application {
    private static FlingGalleryApplication sInstance;
    private static final int MAX_CACHE_IMAGES = 100;
    private static final int IMAGE_CACHE_SIZE = 100 * 1024 * 1024;

    private DisplayImageOptions defaultImageOptions;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;

        initImageCache();
    }

    /**
     * initialize image cache here
     * current we are using AUIL - https://github.com/nostra13/Android-Universal-Image-Loader
     */
    private void initImageCache() {
        defaultImageOptions = new DisplayImageOptions.Builder()
                .cacheOnDisk(false)
                .cacheInMemory(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .displayer(new FadeInBitmapDisplayer(300))
                .resetViewBeforeLoading(true)
                .build();

        ImageLoaderConfiguration loaderConfig = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .defaultDisplayImageOptions(defaultImageOptions)
                .memoryCache(new WeakMemoryCache())
                .memoryCacheSize(IMAGE_CACHE_SIZE)
                .taskExecutor(AsyncTask.THREAD_POOL_EXECUTOR)
                .threadPoolSize(5)
                .build();
        ImageLoader.getInstance().init(loaderConfig);
    }

    public static FlingGalleryApplication getInstance() {
        return sInstance;
    }

    public DisplayImageOptions getDefaultImageOptions() {
        return defaultImageOptions;
    }
}
