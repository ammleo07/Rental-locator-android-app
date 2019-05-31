package com.example.projectlocator;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

import Model.HouseOwnerForm;
import Model.RenteeForm;
import Util.Retrofit.ApiUtils;
import Util.Retrofit.RetrofitService;
import Util.Retrofit.RetrofitServiceHouseOwner;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HouseOwnerPortalActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    View portalForm,progressBar;
    String fileSelected;
    HouseOwnerForm ownerForm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_house_owner_portal);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        String username =getIntent().getStringExtra("username");
        portalForm = findViewById(R.id.house_owner_profile);
        progressBar = findViewById(R.id.house_owner_portal_progress);
        getUserProfile(username);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    public void getUserProfile(String username)
    {
        progressBar.setVisibility(View.VISIBLE);
        RetrofitServiceHouseOwner mService;
        mService= ApiUtils.getHomeOwnerService();
        mService.getProfile(username).enqueue(new Callback<HouseOwnerForm>() {

            @Override
            public void onResponse(Call<HouseOwnerForm> call, Response<HouseOwnerForm> response) {

                if(response.isSuccessful()) {
                    if(response.body() != null)
                    {
                        progressBar.setVisibility(View.INVISIBLE);
                        portalForm.setVisibility(View.VISIBLE);
                        TextView username = (TextView) findViewById(R.id.portal_house_owner_username);
                        TextView fullName = (TextView) findViewById(R.id.portal_house_owner_fullName);
                        TextView houseType = (TextView) findViewById(R.id.portal_house_owner_house_type);
                        TextView contactNumber = (TextView) findViewById(R.id.portal_house_owner_contact_number);
                        TextView monthlyFees = (TextView) findViewById(R.id.portal_house_owner_monthly_fees);
                        TextView numberOfSlots = (TextView) findViewById(R.id.portal_house_owner_house_number_slots);
                        TextView address = (TextView) findViewById(R.id.portal_house_owner_house_address);
                        TextView houseName = (TextView) findViewById(R.id.portal_house_owner_house_name);
                        username.setText(response.body().getUser().getUsername());
                        fullName.setText(response.body().getUser().getFirstName() + " " + response.body().getUser().getMiddleName() + " " + response.body().getUser().getLastName());
                        houseType.setText(response.body().getHouse().getHouseType());
                        contactNumber.setText(response.body().getHouseOwner().getContactNumber());
                        monthlyFees.setText(response.body().getHouse().getMonthlyFee() + "");
                        numberOfSlots.setText(response.body().getHouse().getNumberOfSlots() + "");
                        address.setText(response.body().getAddress().getFullAddress());
                        houseName.setText(response.body().getHouse().getHouseName());

                        ownerForm = response.body();
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



    public void edit(View view)
    {
        Intent intent = new Intent(HouseOwnerPortalActivity.this, EditHouseOwnerActivity.class);
        intent.putExtra("User", ownerForm);
        startActivity(intent);
    }

    public void capture(View view)
    {
        Toast.makeText(getApplicationContext(), "Capture..", Toast.LENGTH_LONG).show();
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            //super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.house_owner_portal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            Intent intent = new Intent(HouseOwnerPortalActivity.this, UploadHouseImageActivity.class);
            intent.putExtra("User", ownerForm);
            startActivity(intent);
        } else if (id == R.id.nav_gallery) {
            Intent intent = new Intent(HouseOwnerPortalActivity.this, HouseGalleryActivity.class);
            intent.putExtra("User", ownerForm);
            startActivity(intent);

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {
            Intent intent = new Intent(HouseOwnerPortalActivity.this, EditHouseOwnerActivity.class);
            intent.putExtra("User", ownerForm);
            startActivity(intent);

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
