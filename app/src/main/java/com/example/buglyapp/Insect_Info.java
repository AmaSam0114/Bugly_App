package com.example.buglyapp;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Insect_Info extends AppCompatActivity {

    private ImageView insect_image;
    private TextView insectNameTV,insectInfoTV;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insect_info);


        insect_image = (ImageView) findViewById(R.id.insect_image);
        insectNameTV = (TextView) findViewById(R.id.insectName);
        insectInfoTV = (TextView) findViewById(R.id.insectInfo);

    }


}