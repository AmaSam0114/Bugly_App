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
import com.google.firebase.storage.StorageReference;

public class SymptomsActivity extends AppCompatActivity {

    private StorageReference storageReference;
    private TextView mainsy,othersy;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_symptoms);

        String text = getIntent().getStringExtra("text");

        mainsy = (TextView) findViewById(R.id.main);
        othersy = (TextView) findViewById(R.id.other);



        DatabaseReference database = FirebaseDatabase.getInstance().getReference();

// Create a query to retrieve the related information based on the text value
        Query query = database.child("Symptoms").orderByChild("Insect Bite").equalTo(text);

// Attach a ValueEventListener to the query to retrieve the results
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    // Retrieve the information related to the text value
                    String infoValue = snapshot.child("Main Symptoms").getValue(String.class);
                    // Update the TextView in the second activity with the related information
                    TextView infoText = findViewById(R.id.main);
                    infoText.setText(infoValue);

                    String infoValue2 = snapshot.child("Other Symptoms").getValue(String.class);
                    // Update the TextView in the second activity with the related information
                    TextView infoText2 = findViewById(R.id.other);
                    infoText2.setText(infoValue2);



                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle the error
            }
        });



    }
}