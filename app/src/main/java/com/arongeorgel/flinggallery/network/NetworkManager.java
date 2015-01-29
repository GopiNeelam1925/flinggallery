package com.arongeorgel.flinggallery.network;

import android.util.Log;

import com.arongeorgel.flinggallery.model.FlingImage;
import com.arongeorgel.flinggallery.network.event.ApiErrorEvent;
import com.arongeorgel.flinggallery.network.event.LoadPhotosEvent;
import com.arongeorgel.flinggallery.network.event.PhotosLoadedEvent;
import com.arongeorgel.flinggallery.persistance.ImageBean;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Manage requests to the server
 *
 * Created by arongeorgel on 29/01/2015.
 */
public class NetworkManager {
    private FlingRestService mRestService;
    private Bus mBus;

    public NetworkManager(FlingRestService restService, Bus bus) {
        this.mRestService = restService;
        this.mBus = bus;
    }

    @Subscribe
    public void onGetPhotoList(LoadPhotosEvent event) {
        Log.wtf("TAG", " we have event in manager");
        mRestService.getImageList(new Callback<List<FlingImage>>() {
            @Override
            public void success(List<FlingImage> imageList, Response response) {
                List<ImageBean> beanList = new ArrayList<ImageBean>();
                for(FlingImage image : imageList) {
                    ImageBean bean = new ImageBean(image.getId(), image.getImageId(), image.getTitle(),
                            image.getUserId(), image.getUserName());
                    bean.save();
                    beanList.add(bean);
                }
                mBus.post(new PhotosLoadedEvent(beanList));
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(NetworkConstants.TAG, "Error on requesting photo list " + error.getUrl()
                        + " with message " + error.getMessage());
                mBus.post(new ApiErrorEvent(error.getMessage()));
            }
        });
    }

}
