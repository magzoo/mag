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
import android.database.Cursor;
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
import android.widget.TextView;

import com.example.magzoo.Utilities.Utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
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

    private SharedPreferences sharedLogin;

    private ImageView imgprofile;
    private ImageButton btnAddImg;
    private Bitmap newImage;

    private Dialog dialog;

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


                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(EditProfile.this);

                LayoutInflater inflater = getLayoutInflater();

                View dialogView= inflater.inflate(R.layout.custom_dialog, null);
                dialogBuilder.setView(dialogView);
                dialogBuilder.setTitle("Adicionar/Alterar Imagem");

                Button btncamera = (Button)dialogView.findViewById(R.id.btncamera);
                Button btnstorage = (Button)dialogView.findViewById(R.id.btnstorage);

                btncamera.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(ContextCompat.checkSelfPermission(EditProfile.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(EditProfile.this, new String[]{Manifest.permission.CAMERA}, 100);
                        }
                        if (ContextCompat.checkSelfPermission(EditProfile.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(intent, 100);
                            dialog.cancel();
                        }
                    }
                });

                btnstorage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(ContextCompat.checkSelfPermission(EditProfile.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(EditProfile.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 101);
                        }
                        if (ContextCompat.checkSelfPermission(EditProfile.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
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
        if(requestCode == 100) {
            if (ContextCompat.checkSelfPermission(EditProfile.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 100);
            }
        }
        if(requestCode == 101){
            if (ContextCompat.checkSelfPermission(EditProfile.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
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
                imgprofile.setImageBitmap(newImage);
            }
        }
        if(requestCode == 101 && resultCode == RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            try {
                InputStream imageStream = getContentResolver().openInputStream(selectedImage);
                newImage= BitmapFactory.decodeStream(imageStream);
            } catch (FileNotFoundException e) {
                Utils.toast(EditProfile.this, "Imagem inválida");
            }
            imgprofile.setImageBitmap(newImage);
            Log.d("bajoraz", "image: " + newImage);
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

                    String encodedimg = rs.getString("Profile_Picture");
                    if(encodedimg!=null)
                    {
                        if(!encodedimg.equals("")){
                            Bitmap bitmap = Utils.base64ToImg(encodedimg);
                            imgprofile.setImageBitmap(bitmap);
                        }

                    }
                    userName.setText(rs.getString("Name"));
                    lkUserName = rs.getString("Name");
                    creationDate.setText(rs.getString("CreationDate"));
                    modDate.setText(rs.getString("ModificationDate"));
                }
            } catch (SQLException throwables) {
                Log.d("bajoraz", throwables.toString());
            }
        }
    }

    public void saveUserInfo(){
        String msg = "";
        if(pass.getText().toString().trim().equals(pass2.getText().toString().trim())){

            if(!pass.getText().toString().trim().equals(lkPass) || !email.getText().toString().trim().equals(lkEmail) || !userName.getText().toString().trim().equals(lkUserName) || newImage!=null){
                try{
                    Connection connection = Utils.getConnection();
                    if (connection == null) {
                        msg = "Verifique a sua ligação à Internet!";
                    }
                    else {
                        // Change below query according to your own database.
                        String query = "";
                        if(newImage !=null){
                            Log.d("bajoraz", "ola1");
                            query = "exec dbo.spEditUser '" + lkEmail + "','" + email.getText() + "','" + userName.getText() + "','" + pass.getText().toString() + "','" + Utils.imgToBase64(newImage) + "'";
                        }else{
                            Log.d("bajoraz", "ola2");
                            query = "exec dbo.spEditUser '" + lkEmail + "','" + email.getText() + "','" + userName.getText() + "','" + pass.getText().toString() + "', NULL";
                        }
                        Statement stmt = connection.createStatement();
                        ResultSet rs = stmt.executeQuery(query);
                        if (rs.next())
                        {
                            modDate.setText(rs.getString("ModificationDate"));

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