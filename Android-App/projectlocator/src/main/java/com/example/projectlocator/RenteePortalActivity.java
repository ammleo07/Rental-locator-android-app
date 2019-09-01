package com.example.projectlocator;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
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

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

import Model.RenteeForm;
import Util.Retrofit.ApiUtils;
import Util.Retrofit.RetrofitService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RenteePortalActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    View portalForm,progressBar;
    RenteeForm renteeForm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rentee_portal);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        portalForm = findViewById(R.id.rentee_profile);
        progressBar = findViewById(R.id.rentee_portal_progress);
        portalForm.setVisibility(View.INVISIBLE);
        String username =getIntent().getStringExtra("username");
        getUserProfile(username);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_rentee);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view_rentee);
        navigationView.setNavigationItemSelectedListener(this);
    }

    public void getUserProfile(String username)
    {
        progressBar.setVisibility(View.VISIBLE);
        RetrofitService mService;
        SharedPreferences sharedpreferences =getSharedPreferences("user", Context.MODE_PRIVATE);
        ApiUtils.BASE_URL="http://" + sharedpreferences.getString("SERVER",null);
        mService= ApiUtils.getSOService();
        mService.getProfile(username).enqueue(new Callback<RenteeForm>() {

            @Override
            public void onResponse(Call<RenteeForm> call, Response<RenteeForm> response) {

                if(response.isSuccessful()) {
                    if(response.body() != null)
                    {
                        progressBar.setVisibility(View.INVISIBLE);
                        portalForm.setVisibility(View.VISIBLE);
                        TextView username = (TextView) findViewById(R.id.portal_rentee_username);
                        TextView fullName = (TextView) findViewById(R.id.portal_rentee_fullName);
                        TextView houseType = (TextView) findViewById(R.id.portal_rentee_house_type);
                        TextView contactNumber = (TextView) findViewById(R.id.portal_rentee_contact_number);
                        TextView priceRange = (TextView) findViewById(R.id.portal_rentee_price_range);
                        username.setText(response.body().getRentee().getUsername());
                        fullName.setText(response.body().getUser().getFirstName() + " " + response.body().getUser().getMiddleName() + " " + response.body().getUser().getLastName());
                        houseType.setText(response.body().getRentee().getHouseType());
                        contactNumber.setText(response.body().getRentee().getContactNumber());
                        priceRange.setText("Php " +Math.round(response.body().getRentee().getMinPriceRange()) + " - Php " + Math.round(response.body().getRentee().getMaxPriceRange()));
                        TextView renteeName = (TextView) findViewById(R.id.rentee_name);
                        TextView renteeUsername = (TextView) findViewById(R.id.rentee_username);
                        renteeName.setText(fullName.getText().toString());
                        renteeUsername.setText(username.getText().toString());
                        renteeForm = response.body();
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
            public void onFailure(Call<RenteeForm> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Unable to access the server:" + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    public void edit(View view)
    {
        Intent intent = new Intent(RenteePortalActivity.this, EditRenteeDetailsActivity.class);
        intent.putExtra("User", renteeForm);
        startActivity(intent);
    }

    public void viewMap(View view)
    {
        Intent intent = new Intent(RenteePortalActivity.this, MapTestActivity.class);
        //intent.putExtra("User", renteeForm);
        startActivityForResult(intent, 999);
        //startActivity(intent);
    }


    public void searchHouse(View view)
    {
        Toast.makeText(getApplicationContext(), "Please wait until the next page appears..." , Toast.LENGTH_LONG).show();
        getCities();
    }

    public void getCities()
    {
        RetrofitService mService;
        SharedPreferences sharedpreferences =getSharedPreferences("user", Context.MODE_PRIVATE);
        ApiUtils.BASE_URL="http://" + sharedpreferences.getString("SERVER",null);
        mService= ApiUtils.getSOService();
        mService.getCities().enqueue(new Callback<List<String>>() {

            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {

                if(response.isSuccessful()) {
                    if(response.body() != null)
                    {

                        String[] cities = response.body().toArray(new String[response.body().size()]);


                        Intent intent = new Intent(RenteePortalActivity.this, SearchHouseParamActivity.class);
                        intent.putExtra("cities",cities);
                        intent.putExtra("User",renteeForm);

                        startActivity(intent);
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "Error has been occured on the server" , Toast.LENGTH_LONG).show();

                    }

                }else {
                    int statusCode  = response.code();
                    Toast.makeText(getApplicationContext(), "Error has been occured on the server:" + statusCode , Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Unable to access the server:" + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 999) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                LatLng latLng = (LatLng) data.getParcelableExtra("picked_point");
                Toast.makeText(this, "Point Chosen: " + latLng.latitude + " " + latLng.longitude, Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_rentee);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            //super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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
            SharedPreferences sharedpreferences =getSharedPreferences("user", Context.MODE_PRIVATE);
            sharedpreferences.edit().clear();
            sharedpreferences.edit().commit();
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        //Toast.makeText(this, "selected id: " + id + ":" + R.id.nav_transaction_activity, Toast.LENGTH_LONG).show();

        if (id == R.id.nav_camera) {
            //Toast.makeText(this, "selected id: " + id + ":" + R.id.view_transaction_activity, Toast.LENGTH_LONG).show();
        } else if (id == R.id.nav_gallery) {
            //Toast.makeText(this, "selected id: " + id + ":" + R.id.view_transaction_activity, Toast.LENGTH_LONG).show();

        } else if (id == R.id.nav_slideshow) {
            //Toast.makeText(this, "selected id: " + id + ":" + R.id.view_transaction_activity, Toast.LENGTH_LONG).show();

        } else if (id == R.id.nav_transactions) {
            Intent intent = new Intent(RenteePortalActivity.this, ViewTransactionsActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_share) {
            //Toast.makeText(this, "selected id: " + id + ":" + R.id.view_transaction_activity, Toast.LENGTH_LONG).show();

        } else if (id == R.id.nav_logout) {
            SharedPreferences sharedpreferences =getSharedPreferences("user", Context.MODE_PRIVATE);
            sharedpreferences.edit().clear();
            sharedpreferences.edit().commit();
            finish();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_rentee);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
