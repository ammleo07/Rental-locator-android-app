package Util.Retrofit;

import java.util.List;

import Model.Comment;
import Model.CostEstimates;
import Model.House;
import Model.HouseOwnerForm;
import Model.Rentee;
import Model.RenteeForm;
import Model.SearchCriteria;
import Model.Transaction;
import Model.User;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by alber on 28/11/2018.
 */

public interface RetrofitService {

//    @GET("/answers?order=desc&sort=activity&site=stackoverflow")
//    Call<String> getAnswers();

    @GET("client/validateUserByString")
    Call<String> getAnswers(@Query("username") String username, @Query("password") String password);

    @GET("client/validate/username")
    Call<String> validateUsername(@Query("username") String username);

    @GET("client/rentee/get/profile")
    Call<RenteeForm> getProfile(@Query("username") String username);

    @POST("client/rentee/update/profile")
    Call<RenteeForm> updateProfile(@Body RenteeForm renteeForm);

    @POST("client/validateUser")
    Call<String> validateUser(@Body User user);

    @POST("client/forgot/password")
    Call<String> forgotPassword(@Body User user);

    @POST("client/send/notification/to/owner")
    Call<Transaction> sendInquiry(@Body Transaction transaction);

    @POST("client/set/transaction/status")
    Call<String> setStatus(@Body Transaction transaction);

    @GET("client/house/address/cities")
    Call<List<String>> getCities();

    @GET("client/house/types")
    Call<List<String>> getHouseTypes();

    @GET("client/house/address/barangay")
    Call<List<String>> getBarangay(@Query("city") String city);

    @POST("client/search/house")
    Call<List<House>> searchHouse(@Body SearchCriteria searchCriteria);


    @GET("client/view/house/details")
    Call<HouseOwnerForm> viewHouseDetails(@Query("id") int id);

    @GET("client/house/address/cost/estimates")
    Call<List<CostEstimates>> viewCostEstimates(@Query("addressId") String addressId);

    @GET("client/get/transactions")
    Call<List<Transaction>> getTransactions(@Query("username") String username);

    @POST("client/register/user/rentee")
    Call<String> saveUser(@Body RenteeForm rentee);

    @GET("client/comment/owner")
    Call<List<Comment>> viewCommentsByOwner(@Query("owner") String owner);

    @POST("client/comment/save")
    Call<String> saveComment(@Body Comment comment);
}
