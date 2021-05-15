package com.example.magzoo;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import com.example.magzoo.Utilities.Utils;
import com.example.magzoo.data.Animal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsoluteLayout;
import android.widget.Button;
import android.widget.ImageButton;

import java.lang.reflect.Array;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class Map extends AppCompatActivity {

    private ImageButton btnCollection;
    private ImageButton btnAwards;
    private AbsoluteLayout layout;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        layout = findViewById(R.id.lnbackground);
        btnCollection = findViewById(R.id.btnCollection);
        btnAwards = findViewById(R.id.btnAwards);

        btnCollection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Map.this, Collection.class);
                startActivity(intent);
            }
        });
        btnAwards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Map.this, Awards.class);
                startActivity(intent);
            }
        });

        layout.setOnTouchListener(new View.OnTouchListener() {
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
//        Animal a = new Animal();
//        a.setId(1);
//        a.setButtonid("btnMonkey");
//        animals.put("btnMonkey", a);
//
//        a = new Animal();
//        a.setId(2);
//        a.setButtonid("btnCrocodile");
//        animals.put("btnCrocodile", a);
        HashMap<String, Integer> animals = sqlGetAnimals();
        for(int i =0; i<animals.size(); i++)
        {
            if(layout.getChildAt(i) instanceof Button)
            {
                Button btn = (Button)layout.getChildAt(i);
                String buttonId =  btn.getResources().getResourceName(btn.getId());
                //buttonId = buttonId.split("/")[1];
                Log.d("bajoraz", "buttonId: " + buttonId);
                int animalId = animals.get("1");
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(Map.this, CollectionDetails.class);
                        intent.putExtra("animalId", animalId);
                        intent.putExtra("origin", "map");
                        startActivity(intent);
                    }
                });
            }
        }
    }

    private void relocateLayout(float x, float y){
        float centroX = (float)this.getResources().getDisplayMetrics().widthPixels/2;
        float centroY = (float)this.getResources().getDisplayMetrics().heightPixels/2;

        float layoutX = layout.getX();
        float layoutY = layout.getY();

        layout.setX(layoutX-(x + layoutX - centroX));
        layout.setY(layoutY-(y + layoutY - centroY));
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

    private HashMap<String, Integer> sqlGetAnimals(){
        String msg = "";
        try
        {
            Connection connection = Utils.getConnection();

            if (connection == null)
            {
                msg = "Verifique a sua ligação à Internet!";
            }
            else
            {
                HashMap<String, Integer> animals = new HashMap<>();  //String = buttonid | Integer = id do animal
                String query = "select Id, ButtonId from Animal";
                Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                while (rs.next()) {
                    Log.d("bajoraz", "buttonIdsql: " + rs.getString("ButtonID"));
                    animals.put(rs.getString("ButtonID"), rs.getInt("Id"));
                }
                return animals;
            }
        }
        catch (Exception ex)
        {
            msg = ex.getMessage();
        }
        Utils.toast(this, msg);
        return  null;
    }
}