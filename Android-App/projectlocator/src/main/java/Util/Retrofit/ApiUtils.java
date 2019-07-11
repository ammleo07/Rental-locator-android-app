package Util.Retrofit;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.firebase.iid.FirebaseInstanceId;

/**
 * Created by alber on 28/11/2018.
 */

public class ApiUtils {

    //public static final String BASE_URL = "http://192.168.1.14:8080/";
    public static String BASE_URL = "http://192.168.43.245:8080/rental/";
    //public static final String BASE_URL = "http://localhost:8080/";


    public static RetrofitService getSOService() {
        return RetrofitClient.getClient(BASE_URL).create(RetrofitService.class);
    }

    public static RetrofitServiceHouseOwner getHomeOwnerService() {
        return RetrofitClient.getClient(BASE_URL).create(RetrofitServiceHouseOwner.class);
    }


}
