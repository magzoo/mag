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
    private EditText userName;
    private TextView creationDate;
    private TextView modDate;

    private String lkEmail;
    private String lkUserName;
    private String lkPass;
    private Button btnSave;

    SharedPreferences sharedLogin;

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

        sharedLogin = getSharedPreferences("SHARED_PREFS", MODE_PRIVATE);
        lkPass = sharedLogin.getString("pass", "");
        lkEmail = sharedLogin.getString("email", "");

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUserInfo();
            }
        });

        setUserInfo();
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