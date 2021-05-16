package com.example.magzoo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.magzoo.Utilities.Utils;
import com.example.magzoo.data.Animal;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Collection extends AppCompatActivity {

    private LinearLayout collection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);

        collection = findViewById(R.id.collection);

        /*List<Animal> animals = new ArrayList<>();
        Animal a = new Animal();
        a.setId(1);
        a.setName("piranha vermelha");
        a.setIcon(BitmapFactory.decodeResource(getResources(),R.drawable.ratomate));
        animals.add(a);

        a = new Animal();
        a.setId(2);
        a.setName("ratomate");
        a.setIcon(BitmapFactory.decodeResource(getResources(),R.drawable.piranhavermelha));
        animals.add(a);*/

        ArrayList<Animal> animals= sqlGetAnimals();
        Log.d("bajoraz", "animals: " + animals);
        fillCollection(animals);
    }

    private void fillCollection(List<Animal> animals) {
        for(Animal animal: animals)
        {
            LinearLayout ln =  new LinearLayout(this);
            ln.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, Utils.convertToDps(this, 200)));
            ln.setOrientation(LinearLayout.HORIZONTAL);
            ln.setGravity(Gravity.BOTTOM);
            BitmapDrawable ob = new BitmapDrawable(getResources(), animal.getIcon());
            ln.setBackground(ob);
            ln.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Collection.this, CollectionDetails.class);
                    intent.putExtra("idanimal", animal.getId());
                    intent.putExtra("origin", "collection");
                    startActivity(intent);
                }
            });

            LinearLayout lninside = new LinearLayout(this);
            lninside.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            lninside.setOrientation(LinearLayout.HORIZONTAL);
            lninside.setBackgroundColor(Color.parseColor("#88FFFFFF"));

            TextView txt = new TextView(this);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(Utils.convertToDps(this, 20), 0, 0, 0);
            txt.setTextSize(20);
            txt.setLayoutParams(layoutParams);

            txt.setText(animal.getName());

            lninside.addView(txt);

            ln.addView(lninside);

            collection.addView(ln);
        }
    }

    private ArrayList<Animal> sqlGetAnimals(){
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
                ArrayList<Animal> animals = new ArrayList<>();
                String query = "select Id, [Name], Icon from Animal";
                Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                while (rs.next()) {
                    String animalImage = rs.getString("Icon");
                    if(animalImage !=null)
                    {
                        if(!animalImage.equals("")){
                            Bitmap bitmap = Utils.base64ToImg(animalImage);
                            animals.add(new Animal(rs.getInt("Id"), rs.getString("Name"), bitmap));
                        }
                    }

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