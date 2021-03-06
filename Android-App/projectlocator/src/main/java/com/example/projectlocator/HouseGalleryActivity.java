package com.example.projectlocator;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import Model.House;
import Model.HouseOwnerForm;
import Util.RecyclerViewAdapter;
import Util.RecyclerViewAdapterHouseGallery;
import Util.Retrofit.ApiUtils;
import Util.Retrofit.RetrofitServiceHouseOwner;
import Util.SimpleDividerItemDecoration;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HouseGalleryActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private RecyclerViewAdapterHouseGallery adapter;
    HouseOwnerForm ownerForm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_house_gallery);
        //ImageView imageView = (ImageView) findViewById(R.id.imageView2);
        recyclerView = (RecyclerView)findViewById(R.id.recycler_view_house_gallery);
        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(this));
        layoutManager = new LinearLayoutManager(HouseGalleryActivity.this);
        recyclerView.setLayoutManager(layoutManager);

        //ownerForm = (HouseOwnerForm) (getIntent().getSerializableExtra("User"));
        String username = (getIntent().getStringExtra("username"));
        getUserProfile(username);


    }

    public void getUserProfile(String username)
    {
        RetrofitServiceHouseOwner mService;
        SharedPreferences sharedpreferences =getSharedPreferences("user", Context.MODE_PRIVATE);
        ApiUtils.BASE_URL="http://" + sharedpreferences.getString("SERVER",null);
        mService= ApiUtils.getHomeOwnerService();
        mService.getProfile(username).enqueue(new Callback<HouseOwnerForm>() {

            @Override
            public void onResponse(Call<HouseOwnerForm> call, Response<HouseOwnerForm> response) {

                if(response.isSuccessful()) {
                    if(response.body() != null)
                    {
                        ownerForm = response.body();
                        if(ownerForm.getHouse().getImagePath() != null) {
                            adapter = new RecyclerViewAdapterHouseGallery(HouseGalleryActivity.this, ownerForm.getHouse().getImagePath());
                            recyclerView.setAdapter(adapter);
                        }
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "Error has been occured on the server" , Toast.LENGTH_LONG).show();

                    }

                }else {
                    int statusCode  = response.code();
                }
            }

            @Override
            public void onFailure(Call<HouseOwnerForm> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Unable to access the server:" + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

}
