package com.shivamgaba.managerapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;

public class DriversActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drivers);

        recyclerView=(RecyclerView) findViewById(R.id.drivers_recycler_view);
        layoutManager= new LinearLayoutManager(DriversActivity.this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        adapter=new DriverAdapter(DriversActivity.this, (ArrayList<driver>) getIntent().getSerializableExtra("drivers"),getIntent().getStringArrayListExtra("onlineStatusList"));
        recyclerView.setAdapter(adapter);
    }
}
