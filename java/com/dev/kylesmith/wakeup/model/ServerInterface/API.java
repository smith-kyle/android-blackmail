package com.dev.kylesmith.wakeup.model.ServerInterface;

import retrofit.Callback;
import retrofit.http.DELETE;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;

/**
 * Created by kylesmith on 8/4/15.
 */
public interface API {
    @PUT("/appt/{password}/{userID}/{timestamp}")
    String addAppt(@Path("password") String password,
                   @Path("userID") int userID,
                   @Path("timestamp") int timestamp,
                   Callback<SubscriptionResponse> callback);

    @DELETE("/appt/{password}/{apptHash}")
    String deleteAppt(@Path("password") String password,
                      @Path("apptHash") String apptHash,
                      Callback<SubscriptionResponse> callback);

    @PUT("/user/{password}/{shareType}/{firstName}/{lastName}/{email}/{accessToken}")
    String addUser(@Path("password") String password,
                   @Path("shareType") int shareType,
                   @Path("firstName") String firstName,
                   @Path("lastName") String lastName,
                   @Path("email") String email,
                   @Path("accessToken") String accessToken,
                   Callback<SubscriptionResponse> callback);

    @POST("/message/{password}/{userID}/{message}")
    String setMessage(@Path("password") String password,
                      @Path("userID") int userID,
                      @Path("message") String message,
                      Callback<SubscriptionResponse> callback);

    @FormUrlEncoded
    @POST("/photo/{password}/{userID}")
    String setPhoto(@Path("password") String password,
                    @Path("userID") int userID,
                    @Field("photo") String photo,
                    Callback<SubscriptionResponse> callback);
}
