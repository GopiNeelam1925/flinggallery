package com.arongeorgel.flinggallery.network.event;

import com.arongeorgel.flinggallery.model.FlingImage;
import com.arongeorgel.flinggallery.persistance.ImageBean;

import java.util.List;

/**
 * Created by arongeorgel on 29/01/2015.
 */
public class PhotosLoadedEvent {
    private List<ImageBean> mImageList;

    public PhotosLoadedEvent(List<ImageBean> imageList) {
        mImageList = imageList;
    }

    public List<ImageBean> getImageList() {
        return mImageList;
    }

    public void setImageList(List<ImageBean> imageList) {
        mImageList = imageList;
    }
}
