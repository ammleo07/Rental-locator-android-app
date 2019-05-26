package com.example.projectlocator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import Model.House;
import Model.HouseOwnerForm;
import Util.RecyclerViewAdapter;
import Util.RecyclerViewAdapterHouseGallery;
import Util.SimpleDividerItemDecoration;

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

        ownerForm = (HouseOwnerForm) (getIntent().getSerializableExtra("User"));
        //List<String> data = new ArrayList<>();
        //data.add("data1");
        //data.add("data2");
        if(ownerForm.getHouse().getImagePath() != null) {
            adapter = new RecyclerViewAdapterHouseGallery(HouseGalleryActivity.this, ownerForm.getHouse().getImagePath());
            recyclerView.setAdapter(adapter);
        }

        //Picasso.with(getApplicationContext()).load("http://192.168.1.11:8080/resources/images/ama.png").resize(100,100).into(imageView);
    }
}
