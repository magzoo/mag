package com.example.magzoo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.magzoo.Utilities.Utils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class Login extends AppCompatActivity {

    private Button btnSignup;
    private Button btnLogin;
    private EditText txtLoginEmail;
    private EditText txtLoginPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        btnSignup = findViewById(R.id.btnSignup);
        btnLogin = findViewById(R.id.btnLogin);
        txtLoginEmail = findViewById(R.id.txtLoginEmail);
        txtLoginPassword = findViewById(R.id.txtLoginPassword);

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =  new Intent(Login.this, Signup.class);
                startActivity(intent);
                finish();
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });

    }

    public void loginUser()
    {
        Log.d("bajoraz","login1");
        if(!txtLoginEmail.getText().toString().contains("@") || !txtLoginEmail.getText().toString().contains(".")) {
            Utils.toast(this, "Email Inválido");
        } else {
            sqlLoginUser();
        }
    }

    public void sqlLoginUser(){
        String msg = "";
        try{
            Connection connection = Utils.getConnection();
            if (connection == null) {
                msg = "Verifique a sua ligação à Internet!";
            }
            else {
                String email = txtLoginEmail.getText().toString();
                String pass = txtLoginPassword.getText().toString();

                // Change below query according to your own database.
                String query = "select [dbo].[fnLogin]( '" + email + "', '" + pass + "') as boolean";
                Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                if (rs.next()) {
                    if(rs.getString("boolean").equals("1")) {
                        SharedPreferences sharedLogin = getSharedPreferences("SHARED_PREFS", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedLogin.edit();
                        editor.putString("email", email);
                        editor.putString("pass", pass);
                        editor.commit();

                        Intent intent =  new Intent(Login.this, Map.class);
                        startActivity(intent);
                        finish();
                    }
                    else {
                        msg = "Credenciais Inválidas";
                    }
                }
            }
        }
        catch (Exception ex) {
            msg = ex.getMessage();
        }
        if(!msg.isEmpty()) {
            Utils.toast(this, msg);
        }
    }
}