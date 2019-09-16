package com.example.projectlocator;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;

import java.util.Arrays;
import java.util.List;

import Model.Rentee;
import Model.RenteeForm;
import Model.User;
import Util.Retrofit.ApiUtils;
import Util.Retrofit.RetrofitService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditRenteeDetailsActivity extends AppCompatActivity {

    RenteeForm renteeForm;
    Spinner houseType, renteeType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_rentee_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        renteeForm = (RenteeForm) (getIntent().getSerializableExtra("User"));
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
        String[] type = {"Select Type", "Student", "Worker", "Businessman"};
        final ArrayAdapter<String> spinnerArrayAdapterType = new ArrayAdapter<String>(getApplicationContext(),R.layout.spinner_item, Arrays.asList(type));
        spinnerArrayAdapterType.setDropDownViewResource(R.layout.spinner_item);
        renteeType = (Spinner) findViewById(R.id.edit_rentee_type);
        renteeType.setAdapter(spinnerArrayAdapterType);

        EditText username = (EditText) findViewById(R.id.edit_rentee_username);
        EditText password = (EditText) findViewById(R.id.edit_rentee_password);
        EditText firstName = (EditText) findViewById(R.id.edit_rentee_first_name);
        EditText middleName = (EditText) findViewById(R.id.edit_rentee_middle_name);
        EditText lastName = (EditText) findViewById(R.id.edit_rentee_last_name);
        EditText contactNumber = (EditText) findViewById(R.id.edit_rentee_contact_number);
        EditText fundType = (EditText) findViewById(R.id.edit_rentee_fund_type);
        EditText transpo = (EditText) findViewById(R.id.edit_rentee_old_transportation);
        //EditText houseType = (EditText) findViewById(R.id.edit_rentee_house_type);
        getHouseTypes();
        EditText minPrice = (EditText) findViewById(R.id.edit_rentee_min_price);
        EditText maxPrice = (EditText) findViewById(R.id.edit_rentee_max_price);
        EditText userId = (EditText) findViewById(R.id.edit_rentee_user_id);
        EditText renteeId = (EditText) findViewById(R.id.edit_rentee_rentee_id);
        username.setText(renteeForm.getUser().getUsername());
        password.setText(renteeForm.getUser().getPassword());
        firstName.setText(renteeForm.getUser().getFirstName());
        middleName.setText(renteeForm.getUser().getMiddleName());
        lastName.setText(renteeForm.getUser().getLastName());
        contactNumber.setText(renteeForm.getRentee().getContactNumber());
        //(renteeForm.getRentee().getHouseType() + "");
        minPrice.setText(renteeForm.getRentee().getMinPriceRange() + "");
        maxPrice.setText(renteeForm.getRentee().getMaxPriceRange() + "");
        userId.setText(renteeForm.getUser().getId() + "");
        renteeId.setText(renteeForm.getRentee().getId() + "");
        renteeType.setSelection(spinnerArrayAdapterType.getPosition(renteeForm.getRentee().getRenteeType()));
        fundType.setText(renteeForm.getRentee().getFundType());
        transpo.setText(renteeForm.getRentee().getOldTransportation() + "");



    }

    public void saveBtn(View v)
    {
        EditText username = (EditText) findViewById(R.id.edit_rentee_username);
        EditText password = (EditText) findViewById(R.id.edit_rentee_password);
        EditText firstName = (EditText) findViewById(R.id.edit_rentee_first_name);
        EditText middleName = (EditText) findViewById(R.id.edit_rentee_middle_name);
        EditText lastName = (EditText) findViewById(R.id.edit_rentee_last_name);
        EditText contactNumber = (EditText) findViewById(R.id.edit_rentee_contact_number);
        Spinner houseType = (Spinner) findViewById(R.id.edit_rentee_house_type);
        EditText minPrice = (EditText) findViewById(R.id.edit_rentee_min_price);
        EditText maxPrice = (EditText) findViewById(R.id.edit_rentee_max_price);
        EditText userId = (EditText) findViewById(R.id.edit_rentee_user_id);
        EditText renteeId = (EditText) findViewById(R.id.edit_rentee_rentee_id);
        EditText fundType = (EditText) findViewById(R.id.edit_rentee_fund_type);
        EditText transpo = (EditText) findViewById(R.id.edit_rentee_old_transportation);

        User user = new User();
        user.setUsername(username.getText().toString());
        user.setPassword(password.getText().toString());
        user.setFirstName(firstName.getText().toString());
        user.setMiddleName(middleName.getText().toString());
        user.setLastName(lastName.getText().toString());
        user.setId(Integer.parseInt(userId.getText().toString()));
        user.setUserType("Rentee");
        user.setIsActive("Y");
        user.setTokenId(FirebaseInstanceId.getInstance().getToken());

        Rentee rentee = new Rentee();
        rentee.setId(Integer.parseInt(renteeId.getText().toString()));
        rentee.setUsername(username.getText().toString());
        rentee.setHouseType(houseType.getSelectedItem().toString());
        rentee.setContactNumber(contactNumber.getText().toString());
        rentee.setMinPriceRange(Double.parseDouble(minPrice.getText().toString()));
        rentee.setMaxPriceRange(Double.parseDouble(maxPrice.getText().toString()));
        rentee.setRenteeType(renteeType.getSelectedItem().toString());
        rentee.setFundType(fundType.getText().toString());
        rentee.setOldTransportation(Double.parseDouble(transpo.getText().toString()));

        RenteeForm renteeForm = new RenteeForm();
        renteeForm.setUser(user);
        renteeForm.setRentee(rentee);
        save(renteeForm);
    }


    public void backBtn(View v)
    {
        Intent intent = new Intent(EditRenteeDetailsActivity.this, RenteePortalActivity.class);
        intent.putExtra("username", renteeForm.getUser().getUsername());
        startActivity(intent);
        finish();
    }

    public void save(final RenteeForm updatedrenteeForm)
    {
        updatedrenteeForm.getUser().setUserType("Rentee");
        RetrofitService mService;
        SharedPreferences sharedpreferences =getSharedPreferences("user", Context.MODE_PRIVATE);
        ApiUtils.BASE_URL="http://" + sharedpreferences.getString("SERVER",null);
        mService= ApiUtils.getSOService();
        mService.updateProfile(updatedrenteeForm).enqueue(new Callback<RenteeForm>() {

            @Override
            public void onResponse(Call<RenteeForm> call, Response<RenteeForm> response) {

                if(response.isSuccessful()) {
                    if(response.body() != null)
                    {
                        Toast.makeText(getApplicationContext(), "Profile sucessfully updated" , Toast.LENGTH_LONG).show();
                        renteeForm = updatedrenteeForm;
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

    public void getHouseTypes() {
        final Spinner houseType = (Spinner) findViewById(R.id.edit_rentee_house_type);
        RetrofitService mService;
        SharedPreferences sharedpreferences =getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        ApiUtils.BASE_URL="http://" + sharedpreferences.getString("SERVER",null);
        mService= ApiUtils.getSOService();
        mService.getHouseTypes().enqueue(new Callback<List<String>>() {

            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {

                if(response.isSuccessful()) {
                    if(response.body() != null)
                    {
                        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getApplicationContext(),R.layout.spinner_item,response.body());
                        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
                        houseType.setAdapter(spinnerArrayAdapter);
                        houseType.setSelection(spinnerArrayAdapter.getPosition(renteeForm.getRentee().getHouseType()));


                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "Please contact system administrator" , Toast.LENGTH_LONG).show();
                    }
                    Log.d("validate username", "username");
                }else {
                    int statusCode  = response.code();

                }
            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {

                Log.d("validate username", "error loading from API:" + t.getMessage());
                Toast.makeText(getApplicationContext(), "Unable to access the server:" + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

}
