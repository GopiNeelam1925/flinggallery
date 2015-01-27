package com.arongeorgel.flinggallery.network;

import com.arongeorgel.flinggallery.BuildConfig;

/**
 * Created by arongeorgel on 25/01/2015.
 */
public class NetworkConstants {
    // in order to be easily extensible keep this constants here
    public static final String HTTP_SCHEMA = "http://";
    public static final String HTTPS_SCHEMA = "https://";
    public static final String API_ENDPOINT = "challenge.superfling.com";
    public static final String BASE_URL = BuildConfig.DEBUG ?
            HTTP_SCHEMA + API_ENDPOINT : HTTP_SCHEMA + API_ENDPOINT;


}
