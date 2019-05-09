package com.example.projectlocator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.List;

import Model.House;
import Util.RecyclerViewAdapter;
import Util.SimpleDividerItemDecoration;

public class SearchResult2Activity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private RecyclerViewAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result2);

        recyclerView = (RecyclerView)findViewById(R.id.recycler_view);
        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(this));
        layoutManager = new LinearLayoutManager(SearchResult2Activity.this);
        recyclerView.setLayoutManager(layoutManager);

        List<House> posts = (List<House>) (getIntent().getSerializableExtra("Houses"));

        adapter = new RecyclerViewAdapter(SearchResult2Activity.this, posts);
        recyclerView.setAdapter(adapter);
    }
}
