package com.example.buglyapp;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class FirstAidActivity extends AppCompatActivity {

    private TextView first_aid;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_aid);

        String text1 = getIntent().getStringExtra("text1");

        first_aid = (TextView) findViewById(R.id.first_aid);

        DatabaseReference database = FirebaseDatabase.getInstance().getReference();

// Create a query to retrieve the related information based on the text value
        Query query = database.child("First_Aid").orderByChild("Insect Bite").equalTo(text1);

// Attach a ValueEventListener to the query to retrieve the results
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    // Retrieve the information related to the text value
                    String infoValue = snapshot.child("first_aid").getValue(String.class);
                    // Update the TextView in the second activity with the related information
                    TextView infoText = findViewById(R.id.first_aid);
                    infoText.setText(infoValue);



                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle the error
            }
        });


    }
}