package com.dev.kylesmith.wakeup.model.ServerInterface;

import retrofit.http.DELETE;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;

/**
 * Created by kylesmith on 8/3/15.
 */
public interface BMService {
    @PUT("/appt/{password}/{userID}/{timestamp}")
    String addAppt(@Path("password") String password,
                   @Path("userID") int userID,
                   @Path("timestamp") int timestamp);

    @DELETE("/appt/{password}/{apptHash}")
    String deleteAppt(@Path("password") String password, @Path("apptHash") String apptHash);

    @PUT("/user/{password}/{shareType}/{firstName}/{lastName}/{email}/{accessToken}")
    String addUser(@Path("password") String password,
                   @Path("shareType") int shareType,
                   @Path("firstName") String firstName,
                   @Path("lastName") String lastName,
                   @Path("email") String email,
                   @Path("accessToken") String accessToken);

    @POST("/message/{password}/{userID}/{message}")
    String setMessage(@Path("password") String password,
                   @Path("userID") int userID,
                   @Path("message") String message);

    @FormUrlEncoded
    @POST("/photo/{password}/{userID}")
    String setPhoto(@Path("password") String password,
                   @Path("userID") int userID,
                   @Field("photo") String photo);
}
