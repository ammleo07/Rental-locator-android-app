package com.example.projectlocator;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import Model.House;
import Model.HouseOwner;
import Model.HouseOwnerForm;
import Model.RenteeForm;
import Model.User;
import Util.Retrofit.ApiUtils;
import Util.Retrofit.RetrofitService;
import Util.Retrofit.RetrofitServiceHouseOwner;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditHouseOwnerActivity extends AppCompatActivity {

    HouseOwnerForm ownerForm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_house_owner);
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
        EditText username = (EditText) findViewById(R.id.edit_house_owner_username);
        EditText password = (EditText) findViewById(R.id.edit_house_owner_password);
        EditText firstName = (EditText) findViewById(R.id.edit_house_owner_first_name);
        EditText middleName = (EditText) findViewById(R.id.edit_house_owner_middle_name);
        EditText lastName = (EditText) findViewById(R.id.edit_house_owner_last_name);
        EditText contactNumber = (EditText) findViewById(R.id.edit_house_owner_contact_number);

        EditText userId = (EditText) findViewById(R.id.edit_house_owner_user_id);
        EditText ownerId = (EditText) findViewById(R.id.edit_house_owner_owner_id);
        username.setText(ownerForm.getUser().getUsername());
        password.setText(ownerForm.getUser().getPassword());
        firstName.setText(ownerForm.getUser().getFirstName());
        middleName.setText(ownerForm.getUser().getMiddleName());
        lastName.setText(ownerForm.getUser().getLastName());
        contactNumber.setText(ownerForm.getHouseOwner().getContactNumber());
        userId.setText(ownerForm.getUser().getId() + "");
        ownerId.setText(ownerForm.getHouseOwner().getId() + "");

    }

    public void saveBtn(View v)
    {
        EditText username = (EditText) findViewById(R.id.edit_house_owner_username);
        EditText password = (EditText) findViewById(R.id.edit_house_owner_password);
        EditText firstName = (EditText) findViewById(R.id.edit_house_owner_first_name);
        EditText middleName = (EditText) findViewById(R.id.edit_house_owner_middle_name);
        EditText lastName = (EditText) findViewById(R.id.edit_house_owner_last_name);
        EditText contactNumber = (EditText) findViewById(R.id.edit_house_owner_contact_number);
//        EditText houseType = (EditText) findViewById(R.id.edit_house_owner_house_type);
//        EditText monthlyFee = (EditText) findViewById(R.id.edit_house_owner_month_fees);
//        EditText numberOfSlots = (EditText) findViewById(R.id.edit_house_owner_number_of_slots);
        EditText userId = (EditText) findViewById(R.id.edit_house_owner_user_id);
        EditText ownerId = (EditText) findViewById(R.id.edit_house_owner_owner_id);

        User user = new User();
        user.setUsername(username.getText().toString());
        user.setPassword(password.getText().toString());
        user.setFirstName(firstName.getText().toString());
        user.setMiddleName(middleName.getText().toString());
        user.setLastName(lastName.getText().toString());
        user.setId(Integer.parseInt(userId.getText().toString()));
        user.setUserType("House Owner");

        HouseOwner owner = new HouseOwner();
        owner.setId(Integer.parseInt(ownerId.getText().toString()));
        owner.setContactNumber(contactNumber.getText().toString());
        owner.setUsername(username.getText().toString());
        owner.setUserId(Integer.parseInt(userId.getText().toString()));

//        House house = new House();
//        house.setHouseType(houseType.getText().toString());
//        house.setMonthlyFee(Double.parseDouble(monthlyFee.getText().toString()));
//        house.setNumberOfSlots(Integer.parseInt(numberOfSlots.getText().toString()));

        HouseOwnerForm houseOwnerForm = new HouseOwnerForm();
        houseOwnerForm.setUser(user);
        houseOwnerForm.setHouseOwner(owner);
//        houseOwnerForm.setHouse(house);
        save(houseOwnerForm);
    }

    public void editHouse(View view)
    {
        Intent intent = new Intent(EditHouseOwnerActivity.this, EditHouseDetailsActivity.class);
        intent.putExtra("User", ownerForm);
        startActivity(intent);
    }

    public void back(View view)
    {
        Intent intent = new Intent(EditHouseOwnerActivity.this, HouseOwnerPortalActivity.class);
        intent.putExtra("username", ownerForm.getUser().getUsername());
        startActivity(intent);
        finish();
    }

    public void save(final HouseOwnerForm owner)
    {
        RetrofitServiceHouseOwner mService;
        mService= ApiUtils.getHomeOwnerService();
        mService.updateProfile(owner).enqueue(new Callback<HouseOwnerForm>() {

            @Override
            public void onResponse(Call<HouseOwnerForm> call, Response<HouseOwnerForm> response) {

                if(response.isSuccessful()) {
                    if(response.body() != null)
                    {
                        Toast.makeText(getApplicationContext(), "Profile sucessfully updated" , Toast.LENGTH_LONG).show();
                        ownerForm.setUser(owner.getUser());
                        ownerForm.setHouseOwner(owner.getHouseOwner());
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
