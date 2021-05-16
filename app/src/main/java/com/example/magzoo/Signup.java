package com.example.magzoo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.magzoo.Utilities.Utils;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class Signup extends AppCompatActivity {

    private Button btnSignup;
    private EditText txtEmail;
    private EditText txtUsername;
    private EditText txtPassword;
    private EditText txtConfirmPassword;
    private ImageView imgSignup;
    private ImageButton btnAddImgSignup;
    private Dialog dialog;
    private Bitmap newImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        getSupportActionBar().hide();

        btnSignup = findViewById(R.id.btnSignup);
        txtEmail =  findViewById(R.id.txtLoginEmail);
        txtUsername = findViewById(R.id.txtUsername);
        txtPassword =  findViewById(R.id.txtPassword);
        txtConfirmPassword = findViewById(R.id.txtConfirmPassword);
        imgSignup = findViewById(R.id.imgSignup);
        btnAddImgSignup = findViewById(R.id.btnAddImgSignup);

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createUser();
            }
        });

        btnAddImgSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(Signup.this);

                LayoutInflater inflater = getLayoutInflater();

                View dialogView= inflater.inflate(R.layout.custom_dialog, null);
                dialogBuilder.setView(dialogView);
                dialogBuilder.setTitle("Adicionar/Alterar Imagem");

                Button btncamera = (Button)dialogView.findViewById(R.id.btncamera);
                Button btnstorage = (Button)dialogView.findViewById(R.id.btnstorage);

                btncamera.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(ContextCompat.checkSelfPermission(Signup.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(Signup.this, new String[]{Manifest.permission.CAMERA}, 100);
                        }
                        if (ContextCompat.checkSelfPermission(Signup.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(intent, 100);
                            dialog.cancel();
                        }
                    }
                });

                btnstorage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(ContextCompat.checkSelfPermission(Signup.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(Signup.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 101);
                        }
                        if (ContextCompat.checkSelfPermission(Signup.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(intent, 101);
                            dialog.cancel();
                        }

                    }
                });
                dialogBuilder.setCancelable(true);
                dialog = dialogBuilder.create();
                dialog.show();
            }


        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == 100) {
            if (ContextCompat.checkSelfPermission(Signup.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 100);
            }
        }
        if(requestCode == 101){
            if (ContextCompat.checkSelfPermission(Signup.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 101);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 100){
            if(data != null)
            {
                newImage = (Bitmap)data.getExtras().get("data");
                imgSignup.setImageBitmap(newImage);
            }
        }
        if(requestCode == 101 && resultCode == RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            try {
                InputStream imageStream = getContentResolver().openInputStream(selectedImage);
                newImage= BitmapFactory.decodeStream(imageStream);
            } catch (FileNotFoundException e) {
                Utils.toast(Signup.this, "Imagem inválida");
            }
            imgSignup.setImageBitmap(newImage);
            Log.d("bajoraz", "image: " + newImage);
        }

    }

    public void createUser()
    {
        if(!txtEmail.getText().toString().contains("@") || !txtEmail.getText().toString().contains("."))
        {
            Utils.toast(this, "Email Inválido");
        }
        else if (!txtPassword.getText().toString().equals(txtConfirmPassword.getText().toString()))
        {
            Utils.toast(this, "Passwords não Correspondem");
        }
        else
        {
            sqlInsertUser();
        }

    }


    public void sqlInsertUser()
    {
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
                String userName = txtUsername.getText().toString();
                String email = txtEmail.getText().toString();
                String pass = txtPassword.getText().toString();

                String query = "[dbo].[insertUser] '" + userName + "', '" + email + "','" + pass + "','" + Utils.imgToBase64(newImage) + "'";
                Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                if (rs.next()) {
                    if(rs.getString("erro").equals("ok"))
                    {
                        SharedPreferences sharedLogin = getSharedPreferences("SHARED_PREFS", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedLogin.edit();
                        editor.putString("email", email);
                        editor.putString("pass", pass);
                        editor.commit();

                        Intent intent =  new Intent(Signup.this, Map.class);
                        startActivity(intent);
                        finish();
                    }
                    else
                    {
                        msg = rs.getString("erro");
                    }
                }
                else
                {
                    msg = "Credenciais Inválidas";
                }

            }
            connection.close();
        }
        catch (Exception ex)
        {
            msg = ex.getMessage();
        }
        Utils.toast(this, msg);
    }



    @Override
    public void onBackPressed() {
        Intent intent =  new Intent(Signup.this, Login.class);
        startActivity(intent);
        finish();

    }

}