package com.example.projectlocator;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import java.util.List;

import Model.CostEstimates;
import Util.RecyclerViewAdapterCostEstimates;
import Util.Retrofit.ApiUtils;
import Util.Retrofit.RetrofitService;
import Util.SimpleDividerItemDecoration;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewCostEstimatesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private RecyclerViewAdapterCostEstimates adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_cost_estimates);
        recyclerView = (RecyclerView)findViewById(R.id.recycler_view_cost_estimates);
        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(this));
        layoutManager = new LinearLayoutManager(ViewCostEstimatesActivity.this);
        recyclerView.setLayoutManager(layoutManager);

        String addressId = (getIntent().getStringExtra("addressId"));
        getCostEstimates(addressId);

    }

    public void getCostEstimates(String addressId)
    {
        RetrofitService mService;
        SharedPreferences sharedpreferences =getSharedPreferences("user", Context.MODE_PRIVATE);
        ApiUtils.BASE_URL="http://" + sharedpreferences.getString("SERVER",null);
        mService= ApiUtils.getSOService();
        mService.viewCostEstimates((addressId)).enqueue(new Callback<List<CostEstimates>>() {

            @Override
            public void onResponse(Call<List<CostEstimates>> call, Response<List<CostEstimates>> response) {

                if(response.isSuccessful()) {
                    if(response.body() != null)
                    {
                        adapter = new RecyclerViewAdapterCostEstimates(ViewCostEstimatesActivity.this, response.body());
                        recyclerView.setAdapter(adapter);
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
            public void onFailure(Call<List<CostEstimates>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Unable to access the server:" + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
        //Write your code here
        //Toast.makeText(getApplicationContext(), "Back press disabled!", Toast.LENGTH_SHORT).show();
    }
}
