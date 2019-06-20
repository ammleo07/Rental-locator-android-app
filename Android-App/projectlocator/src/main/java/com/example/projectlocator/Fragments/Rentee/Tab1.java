package com.example.projectlocator.Fragments.Rentee;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

import com.example.projectlocator.R;
import com.example.projectlocator.RegistrationRenteeActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Model.HouseOwnerForm;
import Util.PasswordValidator;
import Util.Retrofit.ApiUtils;
import Util.Retrofit.RetrofitService;
import Util.Retrofit.RetrofitServiceHouseOwner;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by alber on 14/04/2019.
 */

public class Tab1 extends Fragment {

    EditText username, password;
    boolean res;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_registration_rentee_tab1, container, false);

        username = (EditText) rootView.findViewById(R.id.register_rentee_username);
        username.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus)
                {
                    validateUsername(username.getText().toString(), rootView);
                }
            }
        });

        password=(EditText) rootView.findViewById(R.id.register_rentee_password);
        password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if(!hasFocus)
                {
                    PasswordValidator passwordValidator = new PasswordValidator();
                    if(!passwordValidator.validate(password.getText().toString()))
                    {
                        Toast.makeText(getContext(), "Your password does not meet system requirements. Please retype a most secure password" , Toast.LENGTH_LONG).show();
                        password.setText("");
                        password.setFocusable(true);
                    }
                }
            }
        });


        return rootView;
    }

    public boolean validateUsername(final String username,View view) {
        final EditText txtUsername = (EditText) view.findViewById(R.id.register_rentee_username);
        final boolean result=false;
        RetrofitService mService;
        mService= ApiUtils.getSOService();
        mService.validateUsername(username).enqueue(new Callback<String>() {

            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                if(response.isSuccessful()) {
                    if(response.body().equalsIgnoreCase("success"))
                    {
                        res = true;
                    }
                    else
                    {
                        Toast.makeText(getContext(), "Your Username seems not available" , Toast.LENGTH_LONG).show();
                        txtUsername.setText("");
                        txtUsername.requestFocus();
                        res = false;
                    }
                    Log.d("validate username", "username");
                }else {
                    int statusCode  = response.code();

                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

                Log.d("validate username", "error loading from API:" + t.getMessage());
                Toast.makeText(getContext(), "Unable to access the server:" + t.getMessage(), Toast.LENGTH_LONG).show();
                txtUsername.setText("");
                txtUsername.requestFocus();
            }
        });

        return res;
    }
}
