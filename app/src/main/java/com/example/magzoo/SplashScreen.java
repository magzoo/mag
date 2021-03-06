package com.example.magzoo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.magzoo.Utilities.GIFView;
import com.example.magzoo.Utilities.Utils;
import com.example.magzoo.data.Animal;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
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

                Connection connection = Utils.getConnection();

//                List<Bitmap> bitmaps = new ArrayList<>();
//
//                int id = 3;
//                for(Bitmap bmp : bitmaps)
//                {
//                    String icon= Utils.imgToBase64(bmp);
//                    insertImg(connection, icon, id);
//                    id++;
//                }



                try {
                    connection.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }

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

//    public void insertImg(Connection connection, String pic, int id){
//
//
//
//
//
//        if (connection != null)
//        {
//            ArrayList<Animal> animals = new ArrayList<>();
//            String query = "UPDATE [dbo].[Animal] SET [Map] = '"+pic+"' WHERE id=" + id + " select 1 ";
//
//
//            Statement stmt = null;
//            try {
//                stmt = connection.createStatement();
//                stmt.executeQuery(query);
//                Utils.toast(this, "inserted");
//
//            }
//            catch (SQLException throwables)
//            {
//                Log.d("bajoraz", throwables.toString());
//            }
//        }
//    }

}