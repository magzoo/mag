package com.example.magzoo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.magzoo.Utilities.Utils;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Profile extends AppCompatActivity {
    private TextView email;
    private TextView userName;
    private TextView creationDate;
    private ImageView imgProfile;

    private Button btnEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        email = findViewById(R.id.txtViewEmail);
        userName = findViewById(R.id.txtViewName);
        creationDate = findViewById(R.id.txtCreationDate);
        imgProfile = findViewById(R.id.imgProfile);

        SharedPreferences sharedLogin = getSharedPreferences("SHARED_PREFS", MODE_PRIVATE);
        email.setText(sharedLogin.getString("email", ""));

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

        ResultSet rs = Utils.getUserInfo(email.getText().toString());
        if(rs !=null){
            try {
                if (rs.next()) {
                    userName.setText(rs.getString("Name"));
                    creationDate.setText(rs.getString("CreationDate"));
                    String encodedimg = rs.getString("Profile_Picture");
                    if(encodedimg !=null)
                    {
                        Bitmap bitmap = Utils.base64ToImg(encodedimg);
                        imgProfile.setImageBitmap(bitmap);
                    }
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }
}