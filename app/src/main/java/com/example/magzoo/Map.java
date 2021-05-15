package com.example.magzoo;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.magzoo.Utilities.Utils;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MotionEventCompat;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsoluteLayout;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

public class Map extends AppCompatActivity {

    private ImageButton btnCollection;
    private ImageButton btnAwards;
    private AbsoluteLayout Layout;

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
        Log.d("bajoraz", "coord6");

        Layout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() ==  MotionEvent.ACTION_CANCEL)
                {
                    return false;
                }
                if (event.getAction() == MotionEvent.ACTION_UP)
                {
                    Log.d("bajoraz", "if2");
                    float x = event.getX();
                    float y = event.getY();
                    Log.d("bajoraz", "coordX Y: " + Utils.convertPixelsToDp(Map.this, x) + " " + Utils.convertPixelsToDp(Map.this, y));
                    relocateLayout(x, y);
                    return false;
                }
                return true;

//                int action = event.getAction();
//
//                switch(action) {
//                    case (MotionEvent.ACTION_DOWN) :
//                        Log.d("bajoraz","Action was DOWN");
//                        return true;
//                    case (MotionEvent.ACTION_MOVE) :
//                        Log.d("bajoraz","Action was MOVE");
//                        return true;
//                    case (MotionEvent.ACTION_UP) :
//
//                        Log.d("bajoraz","Action was UP");
//                        return true;
//                    case (MotionEvent.ACTION_CANCEL) :
//                        Log.d("bajoraz","Action was CANCEL");
//                        return true;
//                    case (MotionEvent.ACTION_OUTSIDE) :
//                        Log.d("bajoraz","Movement occurred outside bounds " +
//                                "of current screen element");
//                        return true;
//                    default :
//                        return false;
//                }


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
        Log.d("bajoraz", "Cx: " + centroX);
        Log.d("bajoraz", "Cy: " + centroY);


        float layoutX = Layout.getX();
        float layoutY = Layout.getY();
        Log.d("bajoraz", "Lx: " + layoutX);
        Log.d("bajoraz", "Ly: " + layoutY);


        Layout.setX(layoutX-(x + layoutX - centroX));
        Layout.setY(layoutY-(y + layoutY - centroY));
        //Layout.setY(layoutY+20);
        Log.d("bajoraz","ResultY: " + (layoutY-(y - layoutY + centroY)));
        Log.d("bajoraz", "layoutY: " + Layout.getY());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        Intent intent = null;
        switch (id)
        {
            case R.id.creditos:
                 intent = new Intent(Map.this, Credits.class);
                break;
            case R.id.perfil:
                intent = new Intent(Map.this, Profile.class);
                break;
            case R.id.logout:
                SharedPreferences sharedLogin = getSharedPreferences("SHARED_PREFS", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedLogin.edit();
                editor.putString("email", "");
                editor.putString("pass", "");
                editor.commit();

                intent = new Intent(Map.this, Login.class);
                break;
        }
        if (intent!=null)
            startActivity(intent);
        
        return true;
    }
}