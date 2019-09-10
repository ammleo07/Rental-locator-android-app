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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Arrays;
import java.util.List;

import Model.House;
import Model.HouseOwnerForm;
import Util.Retrofit.ApiUtils;
import Util.Retrofit.RetrofitService;
import Util.Retrofit.RetrofitServiceHouseOwner;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditHouseDetailsActivity extends AppCompatActivity {

    HouseOwnerForm ownerForm;
    Spinner houseType;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_house_details);
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
        EditText houseName = (EditText) findViewById(R.id.edit_house_owner_house_name);
        EditText description = (EditText) findViewById(R.id.edit_house_owner_house_description);
        EditText monthlyFee = (EditText) findViewById(R.id.edit_house_owner_month_fees);
        EditText numberOfSlots = (EditText) findViewById(R.id.edit_house_owner_number_of_slots);
        EditText houseId = (EditText) findViewById(R.id.edit_house_owner_house_id);
        CheckBox isNegotiable = (CheckBox) findViewById(R.id.edit_house_owner_is_negotiable);
        final Spinner boarderType = (Spinner) findViewById(R.id.edit_house_owner_boarder_type);
        String[] boarderType_list = {"Select Boarder Type", "Male", "Female", "Both"};
        final ArrayAdapter<String> spinnerArrayAdapterboarderType_list = new ArrayAdapter<String>(getApplicationContext(),R.layout.spinner_item, Arrays.asList(boarderType_list));
        spinnerArrayAdapterboarderType_list.setDropDownViewResource(R.layout.spinner_item);
        boarderType.setAdapter(spinnerArrayAdapterboarderType_list);

        houseId.setText(ownerForm.getHouse().getId() + "");
        houseName.setText(ownerForm.getHouse().getHouseName());
       //houseType.setText(ownerForm.getHouse().getHouseType() + "");
        getHouseTypes();
        monthlyFee.setText(ownerForm.getHouse().getMonthlyFee() + "");
        numberOfSlots.setText(ownerForm.getHouse().getNumberOfSlots() + "");
        description.setText(ownerForm.getHouse().getDescription());
        boarderType.setSelection(spinnerArrayAdapterboarderType_list.getPosition(ownerForm.getHouse().getBoarderType()));
        if(ownerForm.getHouse().getIsNegotiable().equalsIgnoreCase("Y"))
            isNegotiable.setChecked(true);
        else
            isNegotiable.setChecked(false);
    }

    public void back(View view)
    {
        Intent intent = new Intent(EditHouseDetailsActivity.this, EditHouseOwnerActivity.class);
        intent.putExtra("User", ownerForm);
        startActivity(intent);
        finish();
    }


    public void editAddress(View view)
    {
        Intent intent = new Intent(EditHouseDetailsActivity.this, EditHouseAddressActivity.class);
        intent.putExtra("User", ownerForm);
        startActivity(intent);
        finish();
    }

    public void saveBtn(View view)
    {
        EditText houseName = (EditText) findViewById(R.id.edit_house_owner_house_name);
        Spinner house_Type = (Spinner) findViewById(R.id.edit_house_owner_house_type);
        EditText monthlyFee = (EditText) findViewById(R.id.edit_house_owner_month_fees);
        EditText numberOfSlots = (EditText) findViewById(R.id.edit_house_owner_number_of_slots);
        EditText houseId = (EditText) findViewById(R.id.edit_house_owner_house_id);
        final Spinner boarderType = (Spinner) findViewById(R.id.edit_house_owner_boarder_type);
        CheckBox isNegotiable = (CheckBox) findViewById(R.id.edit_house_owner_is_negotiable);
        EditText description = (EditText) findViewById(R.id.edit_house_owner_house_description);


        House house = new House();
        house.setOwnerId(ownerForm.getHouse().getOwnerId());
        house.setId(Integer.parseInt(houseId.getText().toString()));
        house.setHouseName(houseName.getText().toString());
        house.setHouseType(house_Type.getSelectedItem().toString());
        house.setNumberOfSlots(Integer.parseInt(numberOfSlots.getText().toString()));
        house.setMonthlyFee(Double.parseDouble(monthlyFee.getText().toString()));
        house.setBoarderType(boarderType.getSelectedItem().toString());
        house.setIsNegotiable(isNegotiable.isChecked() ? "Y" : "N");
        house.setDescription(description.getText().toString());
        HouseOwnerForm form = new HouseOwnerForm();
        form.setHouse(house);
        save(form);


    }

    public void save(final HouseOwnerForm owner)
    {

        RetrofitServiceHouseOwner mService;
        SharedPreferences sharedpreferences =getSharedPreferences("user", Context.MODE_PRIVATE);
        ApiUtils.BASE_URL="http://" + sharedpreferences.getString("SERVER",null);
        mService= ApiUtils.getHomeOwnerService();
        mService.updateHouse(owner).enqueue(new Callback<HouseOwnerForm>() {

            @Override
            public void onResponse(Call<HouseOwnerForm> call, Response<HouseOwnerForm> response) {

                if(response.isSuccessful()) {
                    if(response.body() != null)
                    {
                        Toast.makeText(getApplicationContext(), "House Profile successfully updated" , Toast.LENGTH_LONG).show();
                        ownerForm.setHouse(owner.getHouse());

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

    public void getHouseTypes() {
        final Spinner houseType = (Spinner) findViewById(R.id.edit_house_owner_house_type);
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
                        houseType.setSelection(spinnerArrayAdapter.getPosition(ownerForm.getHouse().getHouseType()));


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
