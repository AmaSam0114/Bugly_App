package com.example.buglyapp;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.ImageView;
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

import java.time.Instant;

public class Insect_Info extends AppCompatActivity {

    private StorageReference storageReference;
    private ImageView insect_image;
    private TextView insectNameTV,insectInfoTV;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insect_info);

        String textValue = getIntent().getStringExtra("text_value");

       // insect_image = (ImageView) findViewById(R.id.insect_image);
        insectNameTV = (TextView) findViewById(R.id.insectName);
        insectInfoTV = (TextView) findViewById(R.id.insectInfo);


        DatabaseReference database = FirebaseDatabase.getInstance().getReference();

// Create a query to retrieve the related information based on the text value
        Query query = database.child("Insect").orderByChild("Insect Bite").equalTo(textValue);

// Attach a ValueEventListener to the query to retrieve the results
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            private Instant Glide;

            @SuppressLint("NewApi")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    // Retrieve the information related to the text value
                    String infoValue = snapshot.child("Insect Name").getValue(String.class);
                    // Update the TextView in the second activity with the related information
                    TextView infoText = findViewById(R.id.insectName);
                    infoText.setText(infoValue);

                    String infoValue2 = snapshot.child("Info").getValue(String.class);
                    // Update the TextView in the second activity with the related information
                    TextView infoText2 = findViewById(R.id.insectInfo);
                    infoText2.setText(infoValue2);

                  //  String infoValue3 = snapshot.child("Image").getValue(String.class);
                    // Update the TextView in the second activity with the related information
                    //ImageView infoText3 = findViewById(R.id.insect_image);
                   // infoText3.setImageResource(Integer.parseInt(infoValue3));
                    //infoText3.setImageURI(Uri.parse(infoValue3));




                   





                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle the error
            }
        });


    }


}
