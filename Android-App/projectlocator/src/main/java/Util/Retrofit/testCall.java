package Util.Retrofit;

import android.util.Log;

import retrofit2.Callback;

/**
 * Created by alber on 28/11/2018.
 */

//public class testCall {
//
//    private RetrofitService mService = ApiUtils.getSOService();
//
//     mService.getAnswers().enqueue(new Callback<String>() {
//        @Override
//        public void onResponse(Call<String> call, Response<String> response) {
//
//            if(response.isSuccessful()) {
//                mAdapter.updateAnswers(response.body().getItems());
//                Log.d("RenteePortalActivity", "posts loaded from API");
//            }else {
//                int statusCode  = response.code();
//                // handle request errors depending on status code
//            }
//        }
//
//        @Override
//        public void onFailure(Call<String> call, Throwable t) {
//            showErrorMessage();
//            Log.d("RenteePortalActivity", "error loading from API");
//
//        }
//    });
//}
