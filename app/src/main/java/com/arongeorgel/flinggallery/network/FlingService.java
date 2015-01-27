package com.arongeorgel.flinggallery.network;

import com.arongeorgel.flinggallery.model.FlingImage;

import java.util.List;

import retrofit.http.GET;

/**
 *
 * Created by aron georgel on 25/01/2015.
 */
public interface FlingService {

    @GET("/")
    public List<FlingImage> listImages();

}
