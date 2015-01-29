package com.arongeorgel.flinggallery.network;

import com.arongeorgel.flinggallery.model.FlingImage;

import java.util.List;
import retrofit.Callback;

import retrofit.http.GET;

/**
 *
 * Created by aron georgel on 25/01/2015.
 */
public interface FlingRestService {

    @GET("/")
    public void getImageList(Callback<List<FlingImage>> callback);

}
