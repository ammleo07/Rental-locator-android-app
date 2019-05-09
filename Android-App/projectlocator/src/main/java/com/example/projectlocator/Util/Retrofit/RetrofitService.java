package com.example.projectlocator.Util.Retrofit;

import Model.User;
import Model.UserLocation;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

/**
 * Created by alber on 28/11/2018.
 */

public interface RetrofitService {

//    @GET("/answers?order=desc&sort=activity&site=stackoverflow")
//    Call<String> getAnswers();

    @GET("client/validateUserByString")
    Call<String> getAnswers(@Query("username") String username, @Query("password") String password);

    @POST("client/validateUser")
    Call<String> validateUser(@Body User user);

    @POST("client/forwardLocations")
    Call<String> forwardLocations(@Body UserLocation userLocation);
}
