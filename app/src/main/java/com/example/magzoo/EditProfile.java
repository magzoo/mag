package com.example.magzoo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.magzoo.Utilities.Utils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class EditProfile extends AppCompatActivity {
    private EditText email;
    private EditText pass;
    private EditText pass2;
    private EditText userName;
    private TextView creationDate;
    private TextView modDate;

    private String lkEmail;
    private String lkUserName;
    private String lkPass;
    private Button btnSave;

    SharedPreferences sharedLogin;


    ImageView imgprofile;
    private ImageButton btnAddImg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        email = findViewById(R.id.editTextProfileEmail);
        userName = findViewById(R.id.editTextProfileName);
        pass = findViewById(R.id.editTextTextPassword);
        pass2 = findViewById(R.id.editTextTextPassword2);
        imgprofile = findViewById(R.id.imgprofile);
        btnAddImg = findViewById(R.id.btnAddImg);

        creationDate = findViewById(R.id.txtEditProfileCrtDate);
        modDate = findViewById(R.id.txtEditProfileModDate);
        btnSave = findViewById(R.id.btnSave);

        sharedLogin = getSharedPreferences("SHARED_PREFS", MODE_PRIVATE);
        lkPass = sharedLogin.getString("pass", "");
        lkEmail = sharedLogin.getString("email", "");


        btnAddImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ContextCompat.checkSelfPermission(EditProfile.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(EditProfile.this, new String[]{Manifest.permission.CAMERA}, 100);
                }
                if (ContextCompat.checkSelfPermission(EditProfile.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, 100);
                }
            }


        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUserInfo();
            }
        });

        setUserInfo();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (ContextCompat.checkSelfPermission(EditProfile.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, 100);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 100){
            Bitmap capturedImage = (Bitmap)data.getExtras().get("data");
            imgprofile.setImageBitmap(capturedImage);
        }
    }


    private void updateLkVariables(){
        lkEmail = email.getText().toString().trim();
        lkUserName = userName.getText().toString().trim();
        lkPass = pass.getText().toString().trim();
    }

    public void setUserInfo(){
        email.setText(lkEmail);
        pass.setText(lkPass);
        pass2.setText(lkPass);

        ResultSet rs = Utils.getUserInfo(lkEmail);
        if(rs !=null){
            try {
                if (rs.next()) {
                    userName.setText(rs.getString("Name"));
                    lkUserName = rs.getString("Name");
                    creationDate.setText("Data de Criação: \n\t" + rs.getString("CreationDate"));
                    modDate.setText("Data de Modificação: \n\t" + rs.getString("ModificationDate"));
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    public void saveUserInfo(){
        String msg = "";
        if(pass.getText().toString().trim().equals(pass2.getText().toString().trim())){

            if(!pass.getText().toString().trim().equals(lkPass) || !email.getText().toString().trim().equals(lkEmail) || !userName.getText().toString().trim().equals(lkUserName)){
                try{
                    Connection connection = Utils.getConnection();
                    if (connection == null) {
                        msg = "Verifique a sua ligação à Internet!";
                    }
                    else {
                        // Change below query according to your own database.
                        String query = "exec dbo.spEditUser '" + lkEmail + "','" + email.getText() + "','" + userName.getText() + "','" + pass.getText().toString() + "'";
                        Statement stmt = connection.createStatement();
                        ResultSet rs = stmt.executeQuery(query);
                        if (rs.next()) {
                            email.setText(rs.getString("Email"));
                            userName.setText(rs.getString("Name"));
                            pass.setText(pass.getText().toString());
                            pass2.setText(pass.getText().toString());
                            modDate.setText("Data de Modificação: \n\t" + rs.getString("ModificationDate"));

                            updateLkVariables();

                            SharedPreferences.Editor editor = sharedLogin.edit();
                            editor.putString("email", rs.getString("Email"));
                            editor.putString("pass", pass.getText().toString());
                            editor.commit();
                            msg = "Alterações guardadas com sucesso!";
                        }
                    }
                }
                catch (Exception ex) {
                    msg = ex.getMessage();
                }
            }else{msg = "Não há nada a ser guardado";}
        }else{msg = "As passwords não são iguais";}
        if(!msg.isEmpty()) {
            Utils.toast(this, msg);
        }
    }
}