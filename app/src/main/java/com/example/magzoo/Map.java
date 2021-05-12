package com.example.magzoo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class Map extends AppCompatActivity {

    private ImageButton btnCollection;
    private ImageButton btnAwards;
    private Button btnProfile;
    private Button btnLogout;
    private Button btnCredits;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        btnCollection = findViewById(R.id.btnCollection);
        btnAwards = findViewById(R.id.btnAwards);
        btnProfile = findViewById(R.id.btnProfile);
        btnLogout = findViewById(R.id.btnLogout);
        btnCredits = findViewById(R.id.btnCredits);


        btnCollection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =  new Intent(Map.this, Collection.class);
                startActivity(intent);
            }
        });

        btnAwards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =  new Intent(Map.this, Awards.class);
                startActivity(intent);
            }
        });

        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =  new Intent(Map.this, Profile.class);
                startActivity(intent);
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //limpar as shared preferences
                SharedPreferences sharedLogin = getSharedPreferences("SHARED_PREFS", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedLogin.edit();
                editor.putString("email", "");
                editor.putString("pass", "");
                editor.commit();

                Intent intent =  new Intent(Map.this, Login.class);
                startActivity(intent);
            }
        });

        btnCredits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =  new Intent(Map.this, Credits.class);
                startActivity(intent);
            }
        });
    }
}