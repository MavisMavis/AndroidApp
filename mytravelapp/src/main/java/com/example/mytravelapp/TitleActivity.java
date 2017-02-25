package com.example.mytravelapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class TitleActivity extends AppCompatActivity {
    private Button btnLogin;
    private Button btnSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_title);

        btnLogin = (Button) findViewById(R.id.login);
        btnSignUp = (Button) findViewById(R.id.signup);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginView();
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signupView();
            }
        });
    }

    private void loginView(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    private void signupView(){
        Intent intent = new Intent(this, SignUp.class);
        startActivity(intent);
    }
}
// main page activity
