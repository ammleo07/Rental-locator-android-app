package com.example.projectlocator.Util.Retrofit;

/**
 * Created by alber on 28/11/2018.
 */

public class ApiUtils {

    public static final String BASE_URL = "http://192.168.1.7:8080/";

    public static RetrofitService getSOService() {
        return RetrofitClient.getClient(BASE_URL).create(RetrofitService.class);
    }
}
