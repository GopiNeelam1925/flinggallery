package com.arongeorgel.flinggallery.network.event;

import com.arongeorgel.flinggallery.model.FlingImage;

import java.util.List;

/**
 *
 * Created by arongeorgel on 29/01/2015.
 */
public class PhotosLoadedEvent {
    private List<FlingImage> mImageList;

    public PhotosLoadedEvent(List<FlingImage> imageList) {
        mImageList = imageList;
    }

    public List<FlingImage> getImageList() {
        return mImageList;
    }

    public void setImageList(List<FlingImage> imageList) {
        mImageList = imageList;
    }
}
