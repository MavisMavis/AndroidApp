package com.example.mytravelapp;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ContentActivity extends AppCompatActivity {
    databaseHelper mydb;
    public static final String date = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
    private static int RESULT_LOAD_IMG = 2;
    TextView todayDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        mydb = new databaseHelper(this);
        // Display the today's date at the content page
        todayDate = (TextView) findViewById(R.id.today_date);
        String today = todayDate.getText().toString() + date.toString();
        todayDate.setText(today);



        // Button to create new diary entry
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create intent to Open Image applications like Gallery, Google Photos
                //Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                  //      android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                // Start the Intent
                //startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
                Intent intent = new Intent(ContentActivity.this, NewDiary.class);
                startActivity(intent);
            }
        });
    }
/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.nav__bar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                //Write your code
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }*/
}
