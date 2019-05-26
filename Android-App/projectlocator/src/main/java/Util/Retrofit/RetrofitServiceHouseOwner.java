package Util.Retrofit;

import android.database.Observable;

import Model.HouseOwnerForm;
import Model.RenteeForm;
import Model.User;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

/**
 * Created by alber on 28/11/2018.
 */

public interface RetrofitServiceHouseOwner {


    @POST("client/register/user/owner")
    Call<String> saveUser(@Body HouseOwnerForm houseOwnerForm);

    @GET("client/validate/username")
    Call<String> validateUsername(@Query("username") String username);

    @GET("client/owner/get/profile")
    Call<HouseOwnerForm> getProfile(@Query("username") String username);

    @POST("client/owner/update/profile")
    Call<HouseOwnerForm> updateProfile(@Body HouseOwnerForm houseOwnerForm);

    @POST("client/owner/update/house/profile")
    Call<HouseOwnerForm> updateHouse(@Body HouseOwnerForm houseOwnerForm);

    @POST("client/owner/update/house/address")
    Call<HouseOwnerForm> updateHouseAddress(@Body HouseOwnerForm houseOwnerForm);

    @Multipart
    @POST("client/upload/house/image")
    Observable<ResponseBody> uploadHouseImage(@Part("user_id") RequestBody id,
                                           @Part MultipartBody.Part image);

    @Multipart
    @POST("client/upload/house/image")
    Call<ResponseBody> upload(
            @Part("user_id") RequestBody description,
            @Part MultipartBody.Part file
    );
}
