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
    SplashMove splashMove;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        getSupportActionBar().hide();

        back = findViewById(R.id.back);
//        back.setBackgroundResource(R.drawable.splash1);
        splashMove =  new SplashMove();
        splashMove.execute();



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


    //fazer giff
    private class SplashMove extends AsyncTask
    {
        @Override
        protected Object doInBackground(Object[] objects) {
            while (true)
            {
                try
                {
                    while (true)
                    {
                        back.setBackgroundResource(R.drawable.splash1);
                        TimeUnit.MILLISECONDS.sleep(300);
                        back.setBackgroundResource(R.drawable.splash2);
                        TimeUnit.MILLISECONDS.sleep(300);
                        back.setBackgroundResource(R.drawable.splash3);
                        TimeUnit.MILLISECONDS.sleep(300);
                        back.setBackgroundResource(R.drawable.splash4);
                        TimeUnit.MILLISECONDS.sleep(300);
                        back.setBackgroundResource(R.drawable.splash5);
                        TimeUnit.MILLISECONDS.sleep(300);
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }

        }
    }

}