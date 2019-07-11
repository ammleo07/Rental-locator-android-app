package com.example.projectlocator;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import Model.Transaction;
import Util.RecyclerViewAdapterHouseGallery;
import Util.RecyclerViewAdapterTransactions;
import Util.Retrofit.ApiUtils;
import Util.Retrofit.RetrofitService;
import Util.SimpleDividerItemDecoration;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewTransactionsActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private RecyclerViewAdapterTransactions adapter;
    List<Transaction> transactionList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_transactions);
        recyclerView = (RecyclerView)findViewById(R.id.recycler_view_transactions);
        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(this));
        layoutManager = new LinearLayoutManager(ViewTransactionsActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        getTransaction();
//        if(transactionList != null) {
//        }
    }

    public void getTransaction()
    {
        try {

            SharedPreferences sharedpreferences =getSharedPreferences("user", Context.MODE_PRIVATE);
            String username=sharedpreferences.getString("username",null);
            //Toast.makeText(getApplicationContext(), "Loading Transaction List..", Toast.LENGTH_LONG).show();
            //SharedPreferences sharedpreferences =getSharedPreferences("user", Context.MODE_PRIVATE);
            ApiUtils.BASE_URL="http://" + sharedpreferences.getString("SERVER",null);
            RetrofitService mService= ApiUtils.getSOService();
            mService.getTransactions(username).enqueue(new Callback<List<Transaction>>() {

                @Override
                public void onResponse(Call<List<Transaction>> call, Response<List<Transaction>> response) {

                    if(response.isSuccessful())
                    {
                        if(response.body() != null) {
                            Toast.makeText(getApplicationContext(), "Loading Transaction List..", Toast.LENGTH_LONG).show();
                            transactionList = (List<Transaction>) response.body();

                            adapter = new RecyclerViewAdapterTransactions(ViewTransactionsActivity.this, transactionList);
                            recyclerView.setAdapter(adapter);
                        }

                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "There is error on loading transaction list", Toast.LENGTH_LONG).show();
                        transactionList=null;
                    }

                }

                @Override
                public void onFailure(Call<List<Transaction>> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), "Unable to access the server:" + t.getMessage(), Toast.LENGTH_LONG).show();
                    transactionList=null;
                }
            });


        }
        catch (Exception ex)
        {
            Toast.makeText(getApplicationContext(), "Error:" + ex.getMessage() , Toast.LENGTH_LONG).show();
            transactionList=null;
        }

    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}
