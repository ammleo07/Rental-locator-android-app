package com.example.projectlocator;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;
import java.util.Locale;

import Model.House;
import Model.HouseOwnerForm;
import Util.Retrofit.ApiUtils;
import Util.Retrofit.RetrofitServiceHouseOwner;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditHouseAddressActivity extends AppCompatActivity {

    HouseOwnerForm ownerForm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_house_address);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ownerForm = (HouseOwnerForm) (getIntent().getSerializableExtra("User"));
        populateData();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    public void populateData()
    {
        EditText houseNo = (EditText) findViewById(R.id.edit_house_address_house_no);
        EditText street = (EditText) findViewById(R.id.edit_house_address_street);
        EditText brgy = (EditText) findViewById(R.id.edit_house_address_brgy);
        EditText town = (EditText) findViewById(R.id.edit_house_address_town);
        EditText addressId = (EditText) findViewById(R.id.edit_house_address_id);
        EditText latitude = (EditText) findViewById(R.id.edit_house_address_latitude);
        EditText longitude = (EditText) findViewById(R.id.edit_house_address_longitude);


        houseNo.setText(ownerForm.getAddress().getHouseNo() + "");
        street.setText(ownerForm.getAddress().getStreet());
        brgy.setText(ownerForm.getAddress().getBarangay() + "");
        town.setText(ownerForm.getAddress().getTown() + "");
        addressId.setText(ownerForm.getAddress().getId() + "");
        latitude.setText(ownerForm.getAddress().getLatitude() + "");
        longitude.setText(ownerForm.getAddress().getLongitude() + "");

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 999) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                LatLng latLng = (LatLng) data.getParcelableExtra("picked_point");
                Toast.makeText(this, "Point Chosen: " + latLng.latitude + " " + latLng.longitude, Toast.LENGTH_LONG).show();
                EditText latitude = (EditText) findViewById(R.id.edit_house_address_latitude);
                EditText longitude = (EditText) findViewById(R.id.edit_house_address_longitude);
                EditText town = (EditText) findViewById(R.id.edit_house_address_town);

                latitude.setText(latLng.latitude + "");
                longitude.setText(latLng.longitude + "");

                try {
                    town.setText(getAddressFromLatLng(latLng.latitude, latLng.longitude));
                }
                catch (Exception ex)
                {

                }
            }
        }
    }

    public void viewMap(View view)
    {
        Intent intent = new Intent(EditHouseAddressActivity.this, MapTestActivity.class);
        startActivityForResult(intent, 999);

    }


    public String getAddressFromLatLng(double lat,double lng) throws Exception
    {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
        String cityName = addresses.get(0).getLocality();
        String stateName = addresses.get(0).getCountryName();
        String streetName = addresses.get(0).getThoroughfare();
        //Toast.makeText(getApplicationContext(), cityName + ":" + stateName + ":" + addresses.get(0).getThoroughfare() , Toast.LENGTH_LONG).show();
        return cityName;
    }

    public void back(View view)
    {
        Intent intent = new Intent(EditHouseAddressActivity.this, EditHouseDetailsActivity.class);
        intent.putExtra("User", ownerForm);
        startActivity(intent);
        finish();
    }

    public void saveBtn(View view)
    {
        EditText houseNo = (EditText) findViewById(R.id.edit_house_address_house_no);
        EditText street = (EditText) findViewById(R.id.edit_house_address_street);
        EditText brgy = (EditText) findViewById(R.id.edit_house_address_brgy);
        EditText town = (EditText) findViewById(R.id.edit_house_address_town);
        EditText addressId = (EditText) findViewById(R.id.edit_house_address_id);
        EditText latitude = (EditText) findViewById(R.id.edit_house_address_latitude);
        EditText longitude = (EditText) findViewById(R.id.edit_house_address_longitude);

        Model.Address address = new Model.Address();
        address.setHouseNo(houseNo.getText().toString());
        address.setStreet(street.getText().toString());
        address.setBarangay(brgy.getText().toString());
        address.setTown(town.getText().toString());
        address.setProvince("");
        address.setId(Integer.parseInt(addressId.getText().toString()));
        address.setLatitude(Double.parseDouble(latitude.getText().toString()));
        address.setLongitude(Double.parseDouble(longitude.getText().toString()));
        address.setEntityId(ownerForm.getAddress().getEntityId());
        HouseOwnerForm form = new HouseOwnerForm();
        form.setAddress(address);
        save(form);

    }

    public void save(final HouseOwnerForm owner)
    {

        RetrofitServiceHouseOwner mService;
        SharedPreferences sharedpreferences =getSharedPreferences("user", Context.MODE_PRIVATE);
        ApiUtils.BASE_URL="http://" + sharedpreferences.getString("SERVER",null);
        mService= ApiUtils.getHomeOwnerService();
        mService.updateHouseAddress(owner).enqueue(new Callback<HouseOwnerForm>() {

            @Override
            public void onResponse(Call<HouseOwnerForm> call, Response<HouseOwnerForm> response) {

                if(response.isSuccessful()) {
                    if(response.body() != null)
                    {
                        Toast.makeText(getApplicationContext(), "House Address successfully updated" , Toast.LENGTH_LONG).show();
                        ownerForm.setAddress(owner.getAddress());

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


}
