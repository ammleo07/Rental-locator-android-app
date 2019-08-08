package com.example.projectlocator.Fragments.HouseOwner;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.projectlocator.R;

import java.util.Arrays;
import java.util.List;

import Util.Retrofit.ApiUtils;
import Util.Retrofit.RetrofitService;
import Util.Retrofit.RetrofitServiceHouseOwner;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by alber on 14/04/2019.
 */

public class Tab2 extends Fragment {

    Spinner houseType, streets, barangay;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_registration_house_owner_tab2, container, false);

        final EditText addressTown = (EditText) rootView.findViewById(R.id.register_house_address_town);
        final Spinner addressBrgy = (Spinner) rootView.findViewById(R.id.barangay_list);
        final Spinner addressStreet = (Spinner) rootView.findViewById(R.id.street_list);
        final Spinner numberOfSlots = (Spinner) rootView.findViewById(R.id.register_house_number_of_slots);
        final Spinner boarderType = (Spinner) rootView.findViewById(R.id.boarderType_list);


        String[] initialStreet = {"Select Street"};
        final ArrayAdapter<String> spinnerArrayAdapterStreet = new ArrayAdapter<String>(rootView.getContext(),R.layout.spinner_item, Arrays.asList(initialStreet));
        spinnerArrayAdapterStreet.setDropDownViewResource(R.layout.spinner_item);
        addressStreet.setAdapter(spinnerArrayAdapterStreet);

        String[] initialBrgy = {"Select Barangay"};
        final ArrayAdapter<String> spinnerArrayAdapterBrgy = new ArrayAdapter<String>(rootView.getContext(),R.layout.spinner_item, Arrays.asList(initialBrgy));
        spinnerArrayAdapterBrgy.setDropDownViewResource(R.layout.spinner_item);
        addressBrgy.setAdapter(spinnerArrayAdapterBrgy);

        String[] slots = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
        final ArrayAdapter<String> spinnerArrayAdapterSlots = new ArrayAdapter<String>(rootView.getContext(),R.layout.spinner_item, Arrays.asList(slots));
        spinnerArrayAdapterSlots.setDropDownViewResource(R.layout.spinner_item);
        numberOfSlots.setAdapter(spinnerArrayAdapterSlots);

        String[] boarderType_list = {"Select Boarder Type", "Male", "Female", "Both"};
        final ArrayAdapter<String> spinnerArrayAdapterboarderType_list = new ArrayAdapter<String>(rootView.getContext(),R.layout.spinner_item, Arrays.asList(boarderType_list));
        spinnerArrayAdapterboarderType_list.setDropDownViewResource(R.layout.spinner_item);
        boarderType.setAdapter(spinnerArrayAdapterboarderType_list);



        getHouseTypes(rootView);

        addressTown.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                getBarangay(rootView);
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });

        addressBrgy.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                getStreets(rootView);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        return rootView;
    }

    public void getHouseTypes(final View view) {
        houseType = (Spinner) view.findViewById(R.id.houseType_list);
        RetrofitService mService;
        SharedPreferences sharedpreferences =view.getContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        ApiUtils.BASE_URL="http://" + sharedpreferences.getString("SERVER",null);
        mService= ApiUtils.getSOService();
        mService.getHouseTypes().enqueue(new Callback<List<String>>() {

            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {

                if(response.isSuccessful()) {
                    if(response.body() != null)
                    {
                        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(view.getContext(),R.layout.spinner_item,response.body());
                        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
                        houseType.setAdapter(spinnerArrayAdapter);

                    }
                    else
                    {
                        Toast.makeText(getContext(), "Please contact system administrator" , Toast.LENGTH_LONG).show();
                    }
                    Log.d("validate username", "username");
                }else {
                    int statusCode  = response.code();

                }
            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {

                Log.d("validate username", "error loading from API:" + t.getMessage());
                Toast.makeText(getContext(), "Unable to access the server:" + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    public void getBarangay(final View view) {
        barangay = (Spinner) view.findViewById(R.id.barangay_list);
        EditText addressTown = (EditText) view.findViewById(R.id.register_house_address_town);
        String cityName = addressTown.getText().toString();
        Toast.makeText(getContext(), "city:" + addressTown.getText().toString(), Toast.LENGTH_LONG).show();
        RetrofitServiceHouseOwner mService;
        mService= ApiUtils.getHomeOwnerService();
        mService.getBarangayByCity(cityName).enqueue(new Callback<List<String>>() {

            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {

                if(response.isSuccessful()) {
                    if(response.body() != null)
                    {
                        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(view.getContext(),R.layout.spinner_item,response.body());
                        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
                        barangay.setAdapter(spinnerArrayAdapter);

                    }
                    else
                    {
                        Toast.makeText(getContext(), "Please contact system administrator" , Toast.LENGTH_LONG).show();
                    }
                    Log.d("validate username", "username");
                }else {
                    int statusCode  = response.code();

                }
            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {

                Log.d("validate username", "error loading from API:" + t.getMessage());
                Toast.makeText(getContext(), "Unable to access the server:" + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    public void getStreets(final View view) {
        streets = (Spinner) view.findViewById(R.id.street_list);
        EditText addressTown = (EditText) view.findViewById(R.id.register_house_address_town);
        Spinner addressBrgy = (Spinner) view.findViewById(R.id.barangay_list);
        String cityName = addressTown.getText().toString();
        //Toast.makeText(getContext(), "city:" + addressTown.getText().toString(), Toast.LENGTH_LONG).show();
        RetrofitServiceHouseOwner mService;
        mService= ApiUtils.getHomeOwnerService();
        mService.getStreetByBarangay(cityName,addressBrgy.getSelectedItem().toString()).enqueue(new Callback<List<String>>() {

            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {

                if(response.isSuccessful()) {
                    if(response.body() != null)
                    {
                        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(view.getContext(),R.layout.spinner_item,response.body());
                        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
                        streets.setAdapter(spinnerArrayAdapter);

                    }
                    else
                    {
                        Toast.makeText(getContext(), "Please contact system administrator" , Toast.LENGTH_LONG).show();
                    }
                    Log.d("validate username", "username");
                }else {
                    int statusCode  = response.code();

                }
            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {

                Log.d("validate username", "error loading from API:" + t.getMessage());
                Toast.makeText(getContext(), "Unable to access the server:" + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }





}
