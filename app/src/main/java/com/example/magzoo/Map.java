package com.example.magzoo;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.magzoo.Utilities.Utils;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

public class Map extends AppCompatActivity {

    private ImageButton btnCollection;
    private ImageButton btnAwards;
    private Button btnProfile;
    private Button btnLogout;
    private Button btnCredits;
    private LinearLayout Layout;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Layout = findViewById(R.id.lnbackground);
        Log.d("bajoraz", "coord1" + Layout);
        btnCollection = findViewById(R.id.btnCollection);
        btnAwards = findViewById(R.id.btnAwards);
        btnProfile = findViewById(R.id.btnProfile);
        btnLogout = findViewById(R.id.btnLogout);
        btnCredits = findViewById(R.id.btnCredits);
        Log.d("bajoraz", "coord1");

        btnCollection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Map.this, Collection.class);
                startActivity(intent);
            }
        });
        Log.d("bajoraz", "coord2");
        btnAwards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Map.this, Awards.class);
                startActivity(intent);
            }
        });
        Log.d("bajoraz", "coord3");
        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Map.this, Profile.class);
                startActivity(intent);
            }
        });
        Log.d("bajoraz", "coord4");
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //limpar as shared preferences
                SharedPreferences sharedLogin = getSharedPreferences("SHARED_PREFS", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedLogin.edit();
                editor.putString("email", "");
                editor.putString("pass", "");
                editor.commit();

                Intent intent = new Intent(Map.this, Login.class);
                startActivity(intent);
            }
        });
        Log.d("bajoraz", "coord5");
        btnCredits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Map.this, Credits.class);
                startActivity(intent);
            }
        });
        Log.d("bajoraz", "coord6");

        Layout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.d("bajoraz", "if1");
                if(event.getAction() == MotionEvent.ACTION_UP){
                    Log.d("bajoraz", "if2");
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        Log.d("bajoraz", "if3");
                        float x = event.getX();
                        float y = event.getY();
                        Log.d("bajoraz", "coordX Y: " + Utils.convertPixelsToDp(Map.this, x) + " " + Utils.convertPixelsToDp(Map.this, y));
                        relocateLayout(x, y);
                    }
                    return true;
                }
                return false;
            }
        });
        Log.d("bajoraz", "coord7");
        /*
        layout.setX(this.getResources().getDisplayMetrics().widthPixels/2);
        layout.setY(this.getResources().getDisplayMetrics().heightPixels/2);

        Log.d("bajoraz", "W" + this.getResources().getDisplayMetrics().widthPixels);
        Log.d("bajoraz", "H" + this.getResources().getDisplayMetrics().heightPixels);
        */
    }

    private void relocateLayout(float x, float y){
        float centroX = (float)this.getResources().getDisplayMetrics().widthPixels/2;
        float centroY = (float)this.getResources().getDisplayMetrics().heightPixels/2;

        float layoutX = Layout.getX();
        float layoutY = Layout.getY();
        Layout.setX(layoutX-(x - layoutX + centroX));
        Layout.setY(layoutY-(y - layoutY + centroY));
    }

}