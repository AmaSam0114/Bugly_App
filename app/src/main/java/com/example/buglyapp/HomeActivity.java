package com.example.buglyapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.buglyapp.ml.ModelUnquant;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.IOException;
import java.nio.ByteBuffer;

public class HomeActivity extends AppCompatActivity {


     BottomNavigationView bottomNavigationView;
     private ImageButton imageBtn,captureBtn;
     private Button predictBtn;
     private ImageView imageView;
     private TextView result;
     private Bitmap bitmap;

    @SuppressLint({"WrongViewCast", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);




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
            bitmap = Bitmap.createScaledBitmap(bitmap,128,128,true);


             try {
                 ModelUnquant model = ModelUnquant.newInstance(getApplicationContext());

                 // Creates inputs for reference.
                 TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 224, 224, 3}, DataType.FLOAT32);

                 TensorImage tensorImage = new TensorImage(DataType.FLOAT32);
                 tensorImage.load(bitmap);
                 ByteBuffer byteBuffer = tensorImage.getBuffer();


                 inputFeature0.loadBuffer(byteBuffer);

                 // Runs model inference and gets result.
                 ModelUnquant.Outputs outputs = model.process(inputFeature0);
                 TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();

                 // Releases model resources if no longer used.
                 model.close();

                 result.setText(outputFeature0.getFloatArray()[0] + "\n" + outputFeature0.getFloatArray()[1] + "\n" + outputFeature0.getFloatArray()[2] + "\n" + outputFeature0.getFloatArray()[3] + "\n" + outputFeature0.getFloatArray()[4]);




             } catch (IOException e) {
                 // TODO Handle the exception
             }



         }
         });

    }






    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult( requestCode, resultCode, data);

        if(requestCode == 100)
        {
            imageView.setImageURI(data.getData());
            Uri uri = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
            }catch(IOException e){
                e.printStackTrace();
            }
        }
        else if (requestCode==12) {
            bitmap = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(bitmap);


        }

    }
}