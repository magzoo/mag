package com.example.magzoo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.magzoo.Utilities.GIFView;

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

        GIFView gif;
        gif =(GIFView) findViewById(R.id.gif);
        gif.setImageResource(R.drawable.splash);




        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                SharedPreferences sharedLogin = getSharedPreferences("SHARED_PREFS", MODE_PRIVATE);
                String email = sharedLogin.getString("email", "");
                String pass = sharedLogin.getString("pass", "");

                Log.d("bajoraz", "email: " + email);
                Log.d("bajoraz", "pass: " + pass);

                if(email.trim().isEmpty() || pass.trim().isEmpty()){
                    Log.d("bajoraz", "entrou");
                    Intent intent =  new Intent(SplashScreen.this, Login.class);
                    startActivity(intent);
                    finish();
                }else{
                    Log.d("bajoraz", "passou");
                    Intent intent =  new Intent(SplashScreen.this, Map.class);
                    startActivity(intent);
                    finish();
                }
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

                    time += (interval * 2);
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