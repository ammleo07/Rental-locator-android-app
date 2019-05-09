package com.example.projectlocator;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import Model.House;
import Model.Houses;
import Model.RenteeForm;
import Model.SearchCriteria;
import Util.Retrofit.ApiUtils;
import Util.Retrofit.RetrofitService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchHouseParamActivity extends AppCompatActivity {

    RenteeForm renteeForm;
    View brgyRow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.activity_search_house_param);

        final Spinner spinner = (Spinner) findViewById(R.id.spinner);
        renteeForm = (RenteeForm) (getIntent().getSerializableExtra("User"));
        // Initializing a String Array
        String[] plants = new String[]{
                "Select an item...",
                "California sycamore",
                "Mountain mahogany",
                "Butterfly weed",
                "Carrot weed"
        };

        String[] cities = getIntent().getStringArrayExtra("cities");
        TextView houseType = (TextView) findViewById(R.id.search_house_param_house_type);
        TextView priceRange = (TextView) findViewById(R.id.search_house_param_price_range);
        brgyRow = findViewById(R.id.brgy_row);

        houseType.setText(renteeForm.getRentee().getHouseType());
        priceRange.setText(renteeForm.getRentee().getMinPriceRange() +"to" + renteeForm.getRentee().getMaxPriceRange());
        //Toast.makeText(getApplicationContext(), cities.toString() , Toast.LENGTH_LONG).show();
        final List<String> finalCities = new ArrayList<>(Arrays.asList(getIntent().getStringArrayExtra("cities")));

        // Initializing an ArrayAdapter
        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                this,R.layout.spinner_item,finalCities){
            @Override
            public boolean isEnabled(int position){
                if(position == 0)
                {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                }
                else
                {
                    return true;
                }
            }
            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0){
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                }
                else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        spinner.setAdapter(spinnerArrayAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItemText = (String) parent.getItemAtPosition(position);
                // If user change the default selection
                // First item is disable and it is used for hint
                if(position > 0){
                    // Notify the selected item text
//                    Toast.makeText
//                            (getApplicationContext(), "Selected : " + selectedItemText, Toast.LENGTH_SHORT)
//                            .show();
                    getBarangay(selectedItemText);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void getBarangay(String city)
    {
        RetrofitService mService;
        mService= ApiUtils.getSOService();
        mService.getBarangay(city).enqueue(new Callback<List<String>>() {

            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {

                if(response.isSuccessful()) {
                    if(response.body() != null)
                    {

                        String[] brgy = response.body().toArray(new String[response.body().size()]);
                        final Spinner spinner = (Spinner) findViewById(R.id.spinner_bgry);
                        // Initializing an ArrayAdapter
                        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getApplicationContext(),R.layout.spinner_item,Arrays.asList(brgy)){
                            @Override
                            public boolean isEnabled(int position){
                                if(position == 0)
                                {
                                    // Disable the first item from Spinner
                                    // First item will be use for hint
                                    return false;
                                }
                                else
                                {
                                    return true;
                                }
                            }
                            @Override
                            public View getDropDownView(int position, View convertView,
                                                        ViewGroup parent) {
                                View view = super.getDropDownView(position, convertView, parent);
                                TextView tv = (TextView) view;
                                if(position == 0){
                                    // Set the hint text color gray
                                    tv.setTextColor(Color.GRAY);
                                }
                                else {
                                    tv.setTextColor(Color.BLACK);
                                }
                                return view;
                            }
                        };
                        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
                        spinner.setAdapter(spinnerArrayAdapter);

                        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                String selectedItemText = (String) parent.getItemAtPosition(position);
                                // If user change the default selection
                                // First item is disable and it is used for hint
                                if(position > 0){

                                    brgyRow.setVisibility(1);
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });

                        brgyRow.setVisibility(1);

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

    public void back(View v)
    {
        finish();
    }

    public void search(View v)
    {
        SearchCriteria searchCriteria = new SearchCriteria();
        searchCriteria.setHouseType(renteeForm.getRentee().getHouseType());
        searchCriteria.setMinPrice(renteeForm.getRentee().getMinPriceRange());
        searchCriteria.setMaxPrice(renteeForm.getRentee().getMaxPriceRange());
        final Spinner spinner_brgy = (Spinner) findViewById(R.id.spinner_bgry);
        final Spinner spinner_city = (Spinner) findViewById(R.id.spinner);
        searchCriteria.setBrgy(spinner_brgy.getSelectedItem().toString());
        searchCriteria.setCity(spinner_city.getSelectedItem().toString());

            RetrofitService mService;
            mService= ApiUtils.getSOService();
            mService.searchHouse(searchCriteria).enqueue(new Callback<List<House>>() {

                @Override
                public void onResponse(Call<List<House>> call, Response<List<House>> response) {

                    if(response.isSuccessful()) {
                        if(response.body() != null)
                        {
//                            Intent intent = new Intent(SearchHouseParamActivity.this,SearchResultActivity.class);
                            Intent intent = new Intent(SearchHouseParamActivity.this,SearchResult2Activity.class);
                            //intent.putExtra("Houses", houses);
                            intent.putExtra("Houses", (Serializable) response.body());
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
                public void onFailure(Call<List<House>> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), "Unable to access the server:" + t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });

    }


}