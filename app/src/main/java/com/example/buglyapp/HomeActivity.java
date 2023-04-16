package com.example.buglyapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.buglyapp.ml.Model1;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;

public class HomeActivity extends AppCompatActivity {


     BottomNavigationView bottomNavigationView;
     private ImageButton imageBtn,captureBtn;
     private Button predictBtn,infoBtn,locationBtn;
     private ImageView imageView;
     private TextView result;
     private Bitmap bitmap;
     int imageSize = 224;



    @SuppressLint({"WrongViewCast", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

          String[] labels = new String[5];
          int cnt=0;
        try {
            BufferedReader bufferedReader= new BufferedReader(new InputStreamReader(getAssets().open("labels.txt")));
            String line = bufferedReader.readLine();
            while (line!=null){
                labels[cnt]=line;
                cnt++;
                line = bufferedReader.readLine();

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setSelectedItemId(R.id.bottom_home);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.bottom_home:
                        return true;
                    case R.id.bottom_records:
                        startActivity(new Intent(getApplicationContext(), RecordsActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.bottom_profile:
                        startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                        overridePendingTransition(0, 0);
                        return true;

                }
                return false;

            }
        });

      imageView = (ImageView) findViewById(R.id.imageView_getphoto);
      result = (TextView) findViewById(R.id.results);
      imageBtn = (ImageButton) findViewById(R.id.imageBtn);
      captureBtn = (ImageButton) findViewById(R.id.captureBtn);
      predictBtn = (Button) findViewById(R.id.predict);
      infoBtn = (Button) findViewById(R.id.button_insectInformation);
        locationBtn = (Button) findViewById(R.id.button_NearestHospital);


      imageBtn.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
              intent.setType("image/*");
              startActivityForResult(intent,100);
          }
      });

      captureBtn.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
              startActivityForResult(intent,12);
          }
      });
        predictBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                try {
                    Model1 model = Model1.newInstance(getApplicationContext());

                    // Creates inputs for reference.
                    TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 224, 224, 3}, DataType.FLOAT32);
                    bitmap = Bitmap.createScaledBitmap(bitmap, 448, 448, true);
                  //  inputFeature0.loadBuffer(TensorImage.fromBitmap(bitmap).getBuffer());

                    final ByteBuffer byteBuffer = TensorImage.fromBitmap(bitmap).getBuffer();
                    //display the bytebuffer in log
                    Log.e("ByteBuffer",byteBuffer.toString());
                    Log.e("InputFeatureBuffer", inputFeature0.getBuffer().toString());
                    inputFeature0.loadBuffer(byteBuffer);

                    // Runs model inference and gets result.
                    Model1.Outputs outputs = model.process(inputFeature0);
                    TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();
                    TensorImage ti = new TensorImage(DataType.FLOAT32);
                    ti.load(bitmap);
                    result.setText(labels[getMax(outputFeature0.getFloatArray())] + "");
                    // Releases model resources if no longer used.
                    model.close();
                } catch (IOException e) {
                    // TODO Handle the exception
                }

            }
        });

      infoBtn.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              // Get the text from the TextView
              String text = result.getText().toString();

              // Create a new Intent
              Intent intent = new Intent(HomeActivity.this, Insect_Info.class);

              // Pass the text to the second activity
              intent.putExtra("text", text);

              startActivity(intent);
          }
      });
        locationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, LocationActivity.class);
                startActivity(intent);
            }
        });

    }


    int getMax(float[] arr){
        int max = 0;
        for (int i=0; i<arr.length; i++){
            if(arr[i] > arr[max]) max = i;

        }
        return max;
    }






    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult( requestCode, resultCode, data);

        if(requestCode == 100)
        {
            imageView.setImageURI(data.getData());
            Uri uri = data.getData();
           // bitmap = Bitmap.createScaledBitmap(bitmap,imageSize,imageSize,false);


            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
            }catch(IOException e){
                e.printStackTrace();
            }
        }
        else if (requestCode==12) {
            bitmap = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(bitmap);

            //bitmap = Bitmap.createScaledBitmap(bitmap,imageSize,imageSize,false);


        }

    }


}