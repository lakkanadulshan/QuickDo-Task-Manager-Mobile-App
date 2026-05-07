package com.example.quickdo;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashActivity extends AppCompatActivity {

    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            // already logged in
            startActivity(new Intent(this, MainActivity.class));
        } else {
            // not logged in
            startActivity(new Intent(this, LoginActivity.class));
        }

        finish();
    }
}