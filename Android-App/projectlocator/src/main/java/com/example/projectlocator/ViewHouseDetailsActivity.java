package com.example.projectlocator;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import Model.HouseOwnerForm;

public class ViewHouseDetailsActivity extends AppCompatActivity {

    String numbertoContact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_house_details);

        HouseOwnerForm houseOwnerForm = (HouseOwnerForm) getIntent().getSerializableExtra("House");
        TextView houseName = (TextView) findViewById(R.id.view_house_details_house_name);
        TextView houseType = (TextView) findViewById(R.id.view_house_details_house_type);
        TextView housePrice = (TextView) findViewById(R.id.view_house_details_house_price);
        TextView address = (TextView) findViewById(R.id.view_house_details_house_address);
        TextView latitude = (TextView) findViewById(R.id.view_house_details_house_latitude);
        TextView longitude = (TextView) findViewById(R.id.view_house_details_house_longitude);
        TextView houseOwner = (TextView) findViewById(R.id.view_house_details_house_owner);
        TextView contactNumber = (TextView) findViewById(R.id.view_house_details_house_owner_contact_number);
        TextView callButton = (TextView) findViewById(R.id.view_house_details_call);

        //Toast.makeText(getApplicationContext(), "Name:" + houseOwnerForm.getHouse().getHouseName() , Toast.LENGTH_LONG).show();
        houseName.setText("House Name:" + houseOwnerForm.getHouse().getHouseName());
        houseType.setText("House Type:" + houseOwnerForm.getHouse().getHouseType());
        housePrice.setText("Monthly Fees:"+houseOwnerForm.getHouse().getMonthlyFee() + "");
        address.setText("Address:"+ houseOwnerForm.getAddress().getFullAddress());
        latitude.setText("Latitude:" + houseOwnerForm.getAddress().getLatitude() + "");
        longitude.setText("Longitude:"+houseOwnerForm.getAddress().getLongitude() + "");
        houseOwner.setText("Owner:"+houseOwnerForm.getUser().getFirstName() + " " + houseOwnerForm.getUser().getLastName());
        contactNumber.setText("Owner's Contact Number:"+houseOwnerForm.getHouseOwner().getContactNumber());
        numbertoContact = houseOwnerForm.getHouseOwner().getContactNumber();
        callButton.setText("Call " + houseOwnerForm.getHouseOwner().getContactNumber());
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

    public void viewDirection(View v)
    {
        try {

            Intent intent = new Intent(ViewHouseDetailsActivity.this,MapsHouseDirectionActivity.class);
            startActivity(intent);

        }
        catch (Exception ex)
        {
            Toast.makeText(getApplicationContext(), "Error:" + ex.getMessage() , Toast.LENGTH_LONG).show();

        }

    }

}