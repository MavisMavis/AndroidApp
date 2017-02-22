package com.example.mytravelapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

public class TitleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_title);

        btnLogin = (Button) findViewById(R.id.login);
        btnSignup = (Button) findViewById(R.id.signup);


    }
}
// main page activity
