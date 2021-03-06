package com.example.projectlocator;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;

import Model.HouseOwnerForm;
import Model.RenteeForm;
import Model.Transaction;
import Model.User;
import Util.Retrofit.ApiUtils;
import Util.Retrofit.RetrofitService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewHouseDetailsActivity extends AppCompatActivity {

    String numbertoContact, oldTransportation;;
    HouseOwnerForm houseOwnerForm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_house_details);

        houseOwnerForm = (HouseOwnerForm) getIntent().getSerializableExtra("House");
        TextView houseName = (TextView) findViewById(R.id.view_house_details_house_name);
        TextView houseType = (TextView) findViewById(R.id.view_house_details_house_type);
        TextView housePrice = (TextView) findViewById(R.id.view_house_details_house_price);
        TextView address = (TextView) findViewById(R.id.view_house_details_house_address);
        TextView latitude = (TextView) findViewById(R.id.view_house_details_house_latitude);
        TextView longitude = (TextView) findViewById(R.id.view_house_details_house_longitude);
        TextView houseOwner = (TextView) findViewById(R.id.view_house_details_house_owner);
        TextView contactNumber = (TextView) findViewById(R.id.view_house_details_house_owner_contact_number);
        TextView callButton = (TextView) findViewById(R.id.view_house_details_call);
        TextView isFlood = (TextView) findViewById(R.id.view_house_details_is_Flood);
        TextView isCrime = (TextView) findViewById(R.id.view_house_details_is_Crime);
        TextView addressId = (TextView) findViewById(R.id.view_house_details_address_id);

        //Toast.makeText(getApplicationContext(), "Name:" + houseOwnerForm.getHouse().getHouseName() , Toast.LENGTH_LONG).show();
        houseName.setText("House Name:" + houseOwnerForm.getHouse().getHouseName());
        houseType.setText("House Type:" + houseOwnerForm.getHouse().getHouseType());
        housePrice.setText("Monthly Rental: Php "+ Math.round(houseOwnerForm.getHouse().getMonthlyFee()) + " Negotiable?:"
                + (houseOwnerForm.getHouse().getIsNegotiable().equalsIgnoreCase("Y") ? "Yes" : "No"));
        address.setText("Address:"+ houseOwnerForm.getAddress().getFullAddress());
        latitude.setText("Latitude: " + houseOwnerForm.getAddress().getLatitude() + "");
        longitude.setText("Longitude: "+houseOwnerForm.getAddress().getLongitude() + "");
        houseOwner.setText("Owner: "+houseOwnerForm.getUser().getFirstName() + " " + houseOwnerForm.getUser().getLastName());
        contactNumber.setText("Owner's Contact Number: "+houseOwnerForm.getHouseOwner().getContactNumber());
        numbertoContact = houseOwnerForm.getHouseOwner().getContactNumber();
        callButton.setText("Call " + houseOwnerForm.getHouseOwner().getContactNumber());
        isFlood.setText("Is Prone to Flood: " + (houseOwnerForm.getAddress().getIsFlood() == null ? "No" :houseOwnerForm.getAddress().getIsFlood()));
        //isCrime.setText("Is Prone to Crime: " + (houseOwnerForm.getAddress().getIsCrime() == null ? "No" : houseOwnerForm.getAddress().getIsCrime()));
        isCrime.setText("Brief Description: " + houseOwnerForm.getHouse().getDescription());
        addressId.setText(houseOwnerForm.getAddress().getId() + "");

        //Toast.makeText(getApplicationContext(), "Token:" + FirebaseInstanceId.getInstance().getToken() , Toast.LENGTH_LONG).show();
    }

    public void makeCall(View v)
    {
        try {

            Intent callIntent = new Intent(Intent.ACTION_DIAL);
            callIntent.setData(Uri.parse("tel:" + numbertoContact));

            if (ActivityCompat.checkSelfPermission(ViewHouseDetailsActivity.this,
                    Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            startActivity(callIntent);

        }
        catch (Exception ex)
        {
            Toast.makeText(getApplicationContext(), "Error:" + ex.getMessage() , Toast.LENGTH_LONG).show();

        }

    }

    public void notifyOwner(View v)
    {
        try {

            SharedPreferences sharedpreferences =getSharedPreferences("user", Context.MODE_PRIVATE);
            String username=sharedpreferences.getString("username",null);
            Transaction transaction = new Transaction();
            transaction.setHouseOwner(houseOwnerForm.getHouseOwner().getUsername());
            transaction.setOwnerTokenId(houseOwnerForm.getUser().getTokenId());
            transaction.setRentee(username);
            transaction.setRenteeTokenId(sharedpreferences.getString("tokenId",null));
            transaction.setOrigin("rentee");

            Toast.makeText(getApplicationContext(), sharedpreferences.getString("username",null) +  " was sending Inquiry..", Toast.LENGTH_LONG).show();
            //SharedPreferences sharedpreferences =getSharedPreferences("user", Context.MODE_PRIVATE);
            ApiUtils.BASE_URL="http://" + sharedpreferences.getString("SERVER",null);
            RetrofitService mService= ApiUtils.getSOService();
            mService.sendInquiry(transaction).enqueue(new Callback<Transaction>() {

                @Override
                public void onResponse(Call<Transaction> call, Response<Transaction> response) {

                    if(response.isSuccessful() && response.body() != null)
                    {
                        Toast.makeText(getApplicationContext(), "Inquiry is now sent to owner", Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "There is error on sending inquiry", Toast.LENGTH_LONG).show();
                    }

                }

                @Override
                public void onFailure(Call<Transaction> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), "Unable to access the server:" + t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });


        }
        catch (Exception ex)
        {
            Toast.makeText(getApplicationContext(), "Error:" + ex.getMessage() , Toast.LENGTH_LONG).show();

        }

    }

    public void viewDirection(View v)
    {
        try {
            getUserProfile();

        }
        catch (Exception ex)
        {
            Toast.makeText(getApplicationContext(), "Error:" + ex.getMessage() , Toast.LENGTH_LONG).show();

        }

    }


    public void viewGallery(View v)
    {
        try {

            Intent intent = new Intent(ViewHouseDetailsActivity.this, HouseGalleryActivity.class);
            intent.putExtra("username", houseOwnerForm.getUser().getUsername());
            startActivity(intent);

        }
        catch (Exception ex)
        {
            Toast.makeText(getApplicationContext(), "Error:" + ex.getMessage() , Toast.LENGTH_LONG).show();

        }

    }

    public void viewCostEstimates(View v)
    {
        try {

            Intent intent = new Intent(ViewHouseDetailsActivity.this, ViewCostEstimatesActivity.class);
            intent.putExtra("username", houseOwnerForm.getUser().getUsername() + "");
            startActivity(intent);

        }
        catch (Exception ex)
        {
            Toast.makeText(getApplicationContext(), "Error:" + ex.getMessage() , Toast.LENGTH_LONG).show();

        }

    }

    public void getUserProfile()
    {
        RetrofitService mService;
        SharedPreferences sharedpreferences =getSharedPreferences("user", Context.MODE_PRIVATE);
        ApiUtils.BASE_URL="http://" + sharedpreferences.getString("SERVER",null);
        mService= ApiUtils.getSOService();
        mService.getProfile(sharedpreferences.getString("username",null)).enqueue(new Callback<RenteeForm>() {

            @Override
            public void onResponse(Call<RenteeForm> call, Response<RenteeForm> response) {

                if(response.isSuccessful()) {
                    if(response.body() != null)
                    {
                        Intent intent = new Intent(ViewHouseDetailsActivity.this,MapLocationActivity.class);
                        intent.putExtra("House", houseOwnerForm);
                        intent.putExtra("Transporation", response.body().getRentee().getOldTransportation());
                        startActivity(intent);
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

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }


}
