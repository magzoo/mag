package com.example.magzoo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.widget.LinearLayout;

import java.util.concurrent.TimeUnit;

public class SplashScreen extends AppCompatActivity {


    public LinearLayout back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        getSupportActionBar().hide();

        back = findViewById(R.id.back);
//        back.setBackgroundResource(R.drawable.splash1);



        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
//                splashMove.cancel(true);
                Intent intent =  new Intent(SplashScreen.this, Login.class);
                startActivity(intent);
                finish();
            }
        }, 5000);

    }


    //fazer giff again


}