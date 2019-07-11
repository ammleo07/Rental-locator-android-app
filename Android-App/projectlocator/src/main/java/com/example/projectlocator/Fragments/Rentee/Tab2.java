package com.example.projectlocator.Fragments.Rentee;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.projectlocator.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import Util.Retrofit.ApiUtils;
import Util.Retrofit.RetrofitService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by alber on 14/04/2019.
 */

public class Tab2 extends Fragment {

    Spinner spinner;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_registration_rentee_tab2, container, false);

        getHouseTypes(rootView);

        return rootView;
    }

    public void getHouseTypes(final View view) {
        spinner = (Spinner) view.findViewById(R.id.houseType_list);
        RetrofitService mService;
        SharedPreferences sharedpreferences =view.getContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        ApiUtils.BASE_URL="http://" + sharedpreferences.getString("SERVER",null);
        mService= ApiUtils.getSOService();
        mService.getHouseTypes().enqueue(new Callback<List<String>>() {

            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {

                if(response.isSuccessful()) {
                    if(response.body() != null)
                    {
                        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(view.getContext(),R.layout.spinner_item,response.body());
                        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
                        spinner.setAdapter(spinnerArrayAdapter);

                    }
                    else
                    {
                        Toast.makeText(getContext(), "Please contact system administrator" , Toast.LENGTH_LONG).show();
                    }
                    Log.d("validate username", "username");
                }else {
                    int statusCode  = response.code();

                }
            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {

                Log.d("validate username", "error loading from API:" + t.getMessage());
                Toast.makeText(getContext(), "Unable to access the server:" + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }
}
