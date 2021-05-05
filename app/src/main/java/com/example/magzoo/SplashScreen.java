package com.example.magzoo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
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


    private class splashMove extends AsyncTask {
        @Override
        protected String doInBackground(Object[] objects) {
            Log.d("splashcenas", "splashmove");

            int time = 0;
            int interval = 300;
            try {
                while (time < 5000) {
                    Log.d("splashcenas", "while");

                    time += (interval * 5);
                    back.setBackgroundResource(R.drawable.splash1);
                    TimeUnit.MILLISECONDS.sleep(interval);
                    back.setBackgroundResource(R.drawable.splash2);
                    TimeUnit.MILLISECONDS.sleep(interval);
                    back.setBackgroundResource(R.drawable.splash3);
                    TimeUnit.MILLISECONDS.sleep(interval);
                    back.setBackgroundResource(R.drawable.splash4);
                    TimeUnit.MILLISECONDS.sleep(interval);
                    back.setBackgroundResource(R.drawable.splash5);
                    TimeUnit.MILLISECONDS.sleep(interval);
                }
                Intent intent = new Intent(SplashScreen.this, Login.class);
                startActivity(intent);
                finish();
            } catch (Exception e) {
                Log.d("splashcenas", "Erro: " + e.getMessage());
            }

            return "done";
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
        }
    }
    //fazer giff again


}