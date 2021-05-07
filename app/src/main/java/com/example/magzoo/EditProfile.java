package com.example.magzoo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
    private String email2;
    private String pass3;
    private EditText userName;
    private TextView creationDate;
    private TextView modDate;

    private Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        email = findViewById(R.id.editTextProfileEmail);
        userName = findViewById(R.id.editTextProfileName);
        pass = findViewById(R.id.editTextTextPassword);
        pass2 = findViewById(R.id.editTextTextPassword2);

        creationDate = findViewById(R.id.txtEditProfileCrtDate);
        modDate = findViewById(R.id.txtEditProfileModDate);
        btnSave = findViewById(R.id.btnSave);


        SharedPreferences sharedLogin = getSharedPreferences("SHARED_PREFS", MODE_PRIVATE);
        pass3 = sharedLogin.getString("pass", "");
        email2 = sharedLogin.getString("email", "");

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUserInfo();
            }
        });

        setUserInfo();
    }

    public void setUserInfo(){
        email.setText(email2);
        pass.setText(pass3);
        pass2.setText(pass3);

        ResultSet rs = Utils.getUserInfo(email2);
        if(rs !=null){
            try {
                if (rs.next()) {
                    userName.setText(rs.getString("Name"));
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
        //acabar estas verificações
        if(pass.toString().trim().equals(pass2.toString().trim())){
            if(pass.toString().trim().equals(pass3)){
                try{
                    Connection connection = Utils.getConnection();
                    if (connection == null) {
                        msg = "Verifique a sua ligação à Internet!";
                    }
                    else {
                        // Change below query according to your own database.
                        String query = "exec dbo.spEditUser '" + email2 + "','" + email.getText() + "','" + userName.getText() + "','" + pass.toString();
                        Statement stmt = connection.createStatement();
                        ResultSet rs = stmt.executeQuery(query);
                        if (rs.next()) {
                            email.setText(rs.getString("Email"));
                            userName.setText(rs.getString("Name"));
                            Log.d("bajoraz", "newName: " + rs.getString("Name"));
                            pass.setText(rs.getString("Password"));
                            pass2.setText(rs.getString("Password"));
                            modDate.setText(rs.getString("ModificationDate"));
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

    }
}