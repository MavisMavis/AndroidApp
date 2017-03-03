package com.example.mytravelapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;

/**
 * Created by Mavis on 27/2/2017.
 */

public class DetailsActivity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_details);

        //set the back (up) button
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //find all our view components
        ImageView imageView = (ImageView) findViewById(R.id.details_image);
        TextView dateDetails = (TextView) findViewById(R.id.details_date);
        TextView titleDetails = (TextView) findViewById(R.id.details_title);
        TextView locationDetails = (TextView) findViewById(R.id.details_location);
        TextView infoDetails = (TextView) findViewById(R.id.details_info);
        Button editDiary = (Button) findViewById(R.id.btnEdit);
        Button deleteDiary = (Button) findViewById(R.id.btnDelete);

        // DB handler
        databaseHelper mydb;
        mydb = new databaseHelper(this);



        //collect our intent and populate our layout
        Intent intent = getIntent();

        String id = intent.getStringExtra("diaryID");
        String title = intent.getStringExtra("diaryTitle");
        String location = intent.getStringExtra("diaryLocation");
        String img = intent.getStringExtra("diaryImg");
        String info = intent.getStringExtra("diaryInfo");
        String date = intent.getStringExtra("diaryDate");


        //get the image associated with this property
        Bitmap decodeImage = decodeToBase64(img);


        //set elements
        imageView.setImageBitmap(decodeImage);
        dateDetails.setText(date);
        titleDetails.setText(title);
        locationDetails.setText(location);
        infoDetails.setText(info);


        // Set On Click Listener for buttons
        editDiary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        deleteDiary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mydb.deleteDiaryRecord(id);
            }
        });
    }

    //convert image bitmap to String (using Base 64)
    public String getStringImage(Bitmap bmp)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();

        return Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }

    //convert String to Bitmap format
    public static Bitmap decodeToBase64(String input)
    {
        byte[] decodeByte = Base64.decode(input, 0);

        return BitmapFactory.decodeByteArray(decodeByte, 0, decodeByte.length);
    }
}