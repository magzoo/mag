package com.example.magzoo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.magzoo.Utilities.Utils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Profile extends AppCompatActivity {
    private TextView email;
    private String email2;
    private TextView userName;
    private TextView creationDate;
    private ImageView profilePicture;

    private Button btnEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        email = findViewById(R.id.txtViewEmail);
        userName = findViewById(R.id.txtViewName);
        creationDate = findViewById(R.id.txtCreationDate);
        profilePicture = findViewById(R.id.profilePicture);

        SharedPreferences sharedLogin = getSharedPreferences("SHARED_PREFS", MODE_PRIVATE);
        email2 = sharedLogin.getString("email", "");
        email.setText(email2);

        setUserInfo();

        btnEdit = findViewById(R.id.btnEdit);
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =  new Intent(Profile.this, EditProfile.class);
                startActivity(intent);
            }
        });
    }

    public void setUserInfo(){

        ResultSet rs = Utils.getUserInfo(email2);
        if(rs !=null){
            try {
                if (rs.next()) {
                    Log.d("bajoraz","ola");
                    userName.setText(rs.getString("Name"));
                    Log.d("bajoraz","ola1");
                    creationDate.setText("Data de Criação: \n\t" + rs.getString("CreationDate"));
                    Log.d("bajoraz","ola2");
                    String image = rs.getString("Profile_Picture");
                    Log.d("bajoraz","ola3" + image);

                    byte[] bytes = Base64.decode(image, Base64.DEFAULT);
                    Log.d("bajoraz","ola4: " + bytes);
                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    Log.d("bajoraz","ola5" + bitmap);
                    profilePicture.setImageBitmap(bitmap);
                    Log.d("bajoraz","ola6");
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }
}