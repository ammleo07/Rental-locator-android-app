package com.example.projectlocator.Fragments.HouseOwner;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.Toast;

import com.example.projectlocator.R;

import Model.Comment;
import Util.PasswordValidator;
import Util.Retrofit.ApiUtils;
import Util.Retrofit.RetrofitService;
import Util.Retrofit.RetrofitServiceHouseOwner;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;
import static android.icu.text.UnicodeSet.CASE;

/**
 * Created by alber on 14/04/2019.
 */

public class Tab1 extends Fragment {

    EditText username,password;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_registration_house_owner_tab1, container, false);
        username = (EditText) rootView.findViewById(R.id.register_house_owner_username);

        username.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus)
                {
                    validateUsername(username.getText().toString(), rootView);
                }
            }
        });

        password=(EditText) rootView.findViewById(R.id.register_house_owner_password);
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
        final EditText txtUsername = (EditText) view.findViewById(R.id.register_house_owner_username);
        final boolean result=false;
        RetrofitServiceHouseOwner mService;
        SharedPreferences sharedpreferences =view.getContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        ApiUtils.BASE_URL="http://" + sharedpreferences.getString("SERVER",null);
        mService= ApiUtils.getHomeOwnerService();
        mService.validateUsername(username).enqueue(new Callback<String>() {

            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                if(response.isSuccessful()) {
                    if(response.body().equalsIgnoreCase("success"))
                    {

                    }
                    else
                    {
                        Toast.makeText(getContext(), "Your Username seems not available" , Toast.LENGTH_LONG).show();
                        txtUsername.setText("");
                        txtUsername.requestFocus();

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

        return result;
    }


}
