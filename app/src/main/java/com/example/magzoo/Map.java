package com.example.magzoo;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

import com.example.magzoo.Utilities.Utils;
import com.example.magzoo.data.Animal;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Debug;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsoluteLayout;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import static java.lang.Math.sqrt;

public class Map extends AppCompatActivity {

    private ImageButton btnCollection;
    private ImageButton btnAwards;
    private AbsoluteLayout layout;
    private MenuItem item1;
    private MenuItem item2;
    private Menu menu;
    private ConstraintLayout backmap;

    private SensorManager sensorManager;
    private Sensor lightsensor;
    private SensorEventListener lightEventListener;
    private static final int DARKLIMIT = 400;
    private static final int LIGHTLIMIT = 100;
    private float centroX;
    private float centroY;


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
        backmap = findViewById(R.id.backmap);

        centroX = (float)this.getResources().getDisplayMetrics().widthPixels/2;
        centroY = (float)this.getResources().getDisplayMetrics().heightPixels/2;

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
                Intent intent = new Intent(Map.this, UnderConstruction.class);
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
                    float x = event.getX();
                    float y = event.getY();
//                    Log.d("bajoraz", "coordX Y: " + Utils.convertPixelsToDp(Map.this, x) + " " + Utils.convertPixelsToDp(Map.this, y));
                    relocateLayout(x, y);
                    return false;
                }
                return true;
            }
        });

        HashMap<String, Integer> animals = sqlGetAnimals();
        for(int i =0; i<layout.getChildCount(); i++)
        {
            if(layout.getChildAt(i) instanceof Button)
            {
                Button btn = (Button)layout.getChildAt(i);
                String buttonId =  btn.getResources().getResourceName(btn.getId());
                buttonId = buttonId.split("/")[1];
                if(animals.get(buttonId) != null) {
                    int animalId = animals.get(buttonId);
                    Log.d("bajoraz", "animalId: " + animalId);
                    Log.d("bajoraz", "buttonId: " + buttonId);
                    btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (checkAnimalInRange((btn.getX() + btn.getHeight() * 0.5), (btn.getY() + btn.getWidth() * 0.5))) {
                                Intent intent = new Intent(Map.this, CollectionDetails.class);
                                Log.d("bajoraz", "animalId: " + animalId);
                                intent.putExtra("animalId", animalId);
                                intent.putExtra("origin", "map");
                                startActivity(intent);
                            }else{
                                Utils.toast(Map.this, "Animal fora de alcance!");
                            }

                        }
                    });
                }
                else
                    {
                    btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (checkAnimalInRange((btn.getX() + btn.getHeight() * 0.5), (btn.getY() + btn.getWidth() * 0.5))) {
                                Intent intent = new Intent(Map.this, UnderConstruction.class);
                                startActivity(intent);
                            }else{
                                Utils.toast(Map.this, "Animal fora de alcance!");
                            }
                        }
                    });
                }

            }
        }

        SharedPreferences prefs = getSharedPreferences("SHARED_PREFS", MODE_PRIVATE);

        if(prefs.getString("mode", "").equals(""))
        {
            SharedPreferences.Editor editor;
            editor = prefs.edit();
            editor.putString("mode", "Manual");
            editor.putString("mode2", "Light Mode");
            editor.commit();
        }
        else if(prefs.getString("mode", "").equals("Automático"))
        {
            startLuminositySensor();
        }
        else
        {
            stopLuminositySensor();
        }

        changeTheme();

    }

    public void startLuminositySensor()
    {
        if(sensorManager==null)
            loadLuminositySensor();
        sensorManager.registerListener(lightEventListener, lightsensor, SensorManager.SENSOR_DELAY_FASTEST);
    }
    public void loadLuminositySensor()
    {

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        lightsensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);

        if(lightsensor != null) {


            lightEventListener = new SensorEventListener() {
                @Override
                public void onSensorChanged(SensorEvent event) {
                    float value = event.values[0];

                    if(value < LIGHTLIMIT)
                    {
                        backmap.setBackgroundResource(R.color.themegrey);
                    }
                    else if(value > DARKLIMIT)
                    {
                        backmap.setBackgroundResource(R.color.white);
                    }

                }

                @Override
                public void onAccuracyChanged(Sensor sensor, int accuracy) {

                }
            };
        }
    }
    public void stopLuminositySensor()
    {

        if(sensorManager!=null)
            sensorManager.unregisterListener(lightEventListener, sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT));
    }

    private void relocateLayout(float x, float y){
        float layoutX = layout.getX();
        float layoutY = layout.getY();

        layout.setX(layoutX-(x + layoutX - centroX));
        layout.setY(layoutY-(y + layoutY - centroY));

//        Log.d("bajoraz", "cateto1: " + Math.abs((x + layoutX - centroX)));
//        Log.d("bajoraz", "cateto2: " + Math.abs((y + layoutY - centroY)));
    }

    private boolean checkAnimalInRange(double x, double y){
        float layoutX = layout.getX();
        float layoutY = layout.getY();

//        Log.d("bajoraz", "cateto1func: " + Math.abs((x + layoutX - centroX)));
//        Log.d("bajoraz", "cateto2func: " + Math.abs((y + layoutY - centroY)));

        double touchRadious = StrictMath.hypot(Math.abs((x + layoutX - centroX)), Math.abs((y + layoutY - centroY)));


        if(touchRadious<350){
            return true;
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);

        this.menu = menu;
        loadModeOptions(menu);

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
            case R.id.mode:
                SharedPreferences.Editor editor = getSharedPreferences("SHARED_PREFS", MODE_PRIVATE).edit();
                if(item.getTitle().equals("Manual"))
                {
                    editor.putString("mode", "Automático");
                    startLuminositySensor();
                }
                else
                {
                    editor.putString("mode", "Manual");
                    stopLuminositySensor();
                }
                editor.commit();
                loadModeOptions(menu);
                break;
            case R.id.mode2:
                SharedPreferences.Editor editor1 = getSharedPreferences("SHARED_PREFS", MODE_PRIVATE).edit();
                if(item.getTitle().equals("Dark Mode"))
                {
                    editor1.putString("mode2", "Light Mode");
                }
                else
                {
                    editor1.putString("mode2", "Dark Mode");
                }
                editor1.commit();
                loadModeOptions(menu);
                changeTheme();
                break;
            case R.id.logout:
                SharedPreferences.Editor editor2 = getSharedPreferences("SHARED_PREFS", MODE_PRIVATE).edit();
                editor2.putString("email", "");
                editor2.putString("pass", "");
                editor2.commit();

                intent = new Intent(Map.this, Login.class);
                break;
        }
        if (intent!=null)
            startActivity(intent);
        
        return true;
    }

    private void loadModeOptions(Menu menu) {
        SharedPreferences sharedLogin = getSharedPreferences("SHARED_PREFS", MODE_PRIVATE);
        String mode = null;
        String mode2 = null;

        mode = sharedLogin.getString("mode", "");
        mode2 = sharedLogin.getString("mode2", "");

        item1 = menu.findItem(R.id.mode);
        item2 = menu.findItem(R.id.mode2);
        if(mode.equals(""))
        {
            SharedPreferences.Editor editor;
            editor = sharedLogin.edit();
            editor.putString("mode", "Manual");
            editor.putString("mode2", "Light Mode");
            editor.commit();
        }
        else if(mode.equals("Manual"))
        {
            item2.setEnabled(true);
            changeTheme();
        }
        else
        {
            item2.setEnabled(false);
        }

        item1.setTitle(mode);
        item2.setTitle(mode2);
    }

    public void changeTheme()
    {
        SharedPreferences prefs = getSharedPreferences("SHARED_PREFS", MODE_PRIVATE);
        if(prefs.getString("mode2", "").equals("Light Mode"))
        {
            backmap.setBackgroundResource(R.color.white);
        }
        else if(prefs.getString("mode2", "").equals("Dark Mode"))
        {
            backmap.setBackgroundResource(R.color.themegrey);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(sensorManager!=null)
//            sensorManager.registerListener(lightEventListener, lightsensor, SensorManager.SENSOR_DELAY_FASTEST);
            startLuminositySensor();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(sensorManager!=null)
//            sensorManager.unregisterListener(lightEventListener);
            stopLuminositySensor();
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
                    animals.put(rs.getString("ButtonID"), rs.getInt("Id"));
                }
                return animals;
            }
            connection.close();
        }
        catch (Exception ex)
        {
            msg = ex.getMessage();
        }
        Utils.toast(this, msg);
        return  null;
    }
}