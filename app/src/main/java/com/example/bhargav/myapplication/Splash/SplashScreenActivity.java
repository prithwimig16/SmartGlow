package com.example.bhargav.myapplication.Splash;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.bhargav.myapplication.BlueTooth.BlueToothActivity;
import com.example.bhargav.myapplication.Home.HomeActivity;
import com.example.bhargav.myapplication.R;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        int secondsDelayed = 3;
        new Handler().postDelayed(new Runnable() {
            public void run() {
                startActivity(new Intent(SplashScreenActivity.this, BlueToothActivity.class));
                finish();
            }
        }, secondsDelayed * 1000);
    }

    }

