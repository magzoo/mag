package com.example.magzoo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.magzoo.Utilities.Utils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class Signup extends AppCompatActivity {

    private Button btnSignup;
    private EditText txtEmail;
    private EditText txtUsername;
    private EditText txtPassword;
    private EditText txtConfirmPassword;


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


        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createUser();
            }
        });

    }

    public void createUser()
    {
        if(!txtEmail.getText().toString().contains("@") || !txtEmail.getText().toString().contains("."))
        {
            Utils.toast(this, "Email Inválido");
        }
        else if (!txtPassword.getText().toString().equals(txtPassword.getText().toString()))
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

                // Change below query according to your own database.
                String query = "[dbo].[insertUser] '" + userName + "', '" + email + "','" + pass + "'";
                Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                if (rs.next()) {
                    if(rs.getString("erro").equals("ok"))
                    {
                        SharedPreferences sharedLogin = getSharedPreferences("SHARED_PREFS", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedLogin.edit();
                        editor.putString("email", email);
                        editor.putString("pass", pass);

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