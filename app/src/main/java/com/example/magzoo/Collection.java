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

        ArrayList<Animal> animals= sqlGetAnimals();
        fillCollection(animals);
    }

    private void fillCollection(List<Animal> animals) {
        boolean isCollected = false;
        for(Animal animal: animals)
        {

            LinearLayout ln =  new LinearLayout(this);
            ln.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, Utils.convertToDps(this, 200)));
            //BitmapDrawable ob = new BitmapDrawable(getResources(), animal.getIcon());
            //ln.setBackground(ob);
            ln.setBackgroundResource(R.drawable.animaldefault);
            ln.setGravity(Gravity.CENTER);
            ln.setOrientation(LinearLayout.VERTICAL);
            isCollected = animal.isCollected();

            if(!isCollected) {
                ln.setAlpha(0.8f);
            }else{
                ln.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.d("bajoraz", "entrou no foreach8");
                        Intent intent = new Intent(Collection.this, CollectionDetails.class);
                        Log.d("bajoraz", "animalId: " + animal.getId());
                        intent.putExtra("animalId", animal.getId());
                        intent.putExtra("origin", "collection");
                        startActivity(intent);
                    }
                });
            }



            TextView textView = new TextView(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            params.weight = 1;
            textView.setLayoutParams(params);
            if(!isCollected) {
                textView.setText("Animal ainda não está colecionado");
            }
            textView.setGravity(Gravity.CENTER);
            textView.setTextSize(20);
            textView.setTextColor(getResources().getColor(R.color.black));

            ln.addView(textView);

            LinearLayout lninside = new LinearLayout(this);
            LinearLayout.LayoutParams lnparams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lninside.setLayoutParams(lnparams);
            lninside.setGravity(Gravity.BOTTOM);
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
                String query = "select Animal.Id, Animal.[Name], FORMAT(UserAnimal.Date, 'dd/MM/yyyy') as CollectDate from Animal FULL OUTER JOIN UserAnimal ON Animal.Id = UserAnimal.FK_Animal";
                Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                while (rs.next()) {
                    /*String animalImage = rs.getString("Icon");
                    if(animalImage !=null)
                    {
                        if(!animalImage.equals("")){
                            Bitmap bitmap = Utils.base64ToImg(animalImage);*/
                            boolean isCollected = false;
                            if(rs.getString("CollectDate") != null){
                                isCollected = true;
                            }
                            animals.add(new Animal(rs.getInt("Id"), rs.getString("Name"), isCollected));
                     /*   }
                    }*/

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