package com.shivamgaba.managerapp;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class Profile extends AppCompatActivity {

    TextView managerName,managerEmailId, managerPhoneNumber;
    ImageView managerPic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        managerName=findViewById(R.id.managerName);
        managerEmailId=findViewById(R.id.managerEmailId);
        managerPhoneNumber=findViewById(R.id.managerPhoneNumber);
        managerPic=findViewById(R.id.managerPic);

        managerName.setText("Name : " + getIntent().getStringExtra("managerName"));
        managerPhoneNumber.setText("Contact Number : " + getIntent().getStringExtra("managerPhoneNumber"));
        managerEmailId.setText("Email Id : " + getIntent().getStringExtra("managerEmailId"));
        Glide.with(getApplicationContext()).load(getIntent().getStringExtra("managerPicUrl")).into(managerPic);
    }
}