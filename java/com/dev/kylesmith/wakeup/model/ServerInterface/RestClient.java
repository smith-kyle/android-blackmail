package com.dev.kylesmith.wakeup.model.ServerInterface;

import com.squareup.okhttp.OkHttpClient;

import retrofit.RestAdapter;
import retrofit.client.OkClient;

/**
 * Created by kylesmith on 8/4/15.
 */
public class RestClient {
    private static API REST_CLIENT;
    private static String baseURL = "http://blackmail.kylesmiff.com/api/v1";

    static{
        initRestClient();
    }

    public static API get(){
        return REST_CLIENT;
    }


    private static void initRestClient(){
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(baseURL)
                .setClient(new OkClient(new OkHttpClient()))
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
        REST_CLIENT = restAdapter.create(API.class);
    }
}
