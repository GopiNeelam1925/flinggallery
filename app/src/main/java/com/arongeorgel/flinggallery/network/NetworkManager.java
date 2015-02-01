package com.arongeorgel.flinggallery.network;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

import com.arongeorgel.flinggallery.FlingGalleryApplication;
import com.arongeorgel.flinggallery.model.FlingImage;
import com.arongeorgel.flinggallery.network.event.ApiErrorEvent;
import com.arongeorgel.flinggallery.network.event.LoadPhotosEvent;
import com.arongeorgel.flinggallery.network.event.PhotosLoadedEvent;
import com.arongeorgel.flinggallery.persistence.ImageBean;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
                for(FlingImage image : imageList) {
                    ImageBean bean = ImageBean.getImage(image.getId());

                    if(bean == null) {
                        bean = new ImageBean(image.getId(), image.getImageId(), image.getTitle(),
                                image.getUserId(), image.getUserName());
                        bean.save();
                    } else {
                        // update existing image
                        bean.setImageId(image.getImageId());
                        bean.setTitle(image.getTitle());
                        bean.setUserId(image.getUserId());
                        bean.setUserName(image.getUserName());
                        bean.save();
                    }

                }
                new PreFetchImages(imageList).execute();
                mBus.post(new PhotosLoadedEvent(imageList));
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(NetworkConstants.TAG, "Error on requesting photo list " + error.getUrl()
                        + " with message " + error.getMessage());
                mBus.post(new ApiErrorEvent(error.getMessage()));
            }
        });
    }

    /**
     * Prefetch images and save some data about bitmaps in database
     */
    private static class PreFetchImages extends AsyncTask<Void, Void, Void> {
        private List<FlingImage> imageList;

        private PreFetchImages(List<FlingImage> imageList) {
            this.imageList = imageList;
        }

        @Override
        protected Void doInBackground(Void... params) {

            for(final FlingImage image : imageList) {
                final String imgUrl = NetworkConstants.BASE_URL + NetworkConstants.PHOTO_PATH
                        + "/" + image.getImageId();
                Picasso.with(FlingGalleryApplication.getInstance().getApplicationContext())
                        .load(imgUrl)
                        .transform(new Transformation() {
                            @Override
                            public Bitmap transform(Bitmap bitmap) {
                                ImageBean bean = ImageBean.getImage(image.getId());
                                if (bean != null) {
                                    // update size and width
                                    bean.setImageSize(bitmap.getByteCount());
                                    bean.setImageWidth(bitmap.getWidth());
                                    bean.save();
                                }

                                return bitmap;
                            }

                            @Override
                            public String key() {
                                return imgUrl;
                            }
                        })
                        .fetch();

            }
            return null;
        }
    }
}
