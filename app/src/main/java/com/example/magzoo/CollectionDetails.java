package com.example.magzoo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class CollectionDetails extends AppCompatActivity {

    private ImageView img;
    private ImageView imgHabAtiv;
    private ImageView imgHabSocialLife;
    private ImageView imgHabDiet;
    private ImageView imgRepType;
    private ImageView imgRepEggsOffSpring;
    private ImageView imgRepIncubationGestation;
    private ImageView imgRepSexualMaturity;

    private TextView txtRisk;
    private TextView txtName;
    private TextView txtSciName;
    private TextView txtDescription;
    private TextView txtHabSummary;
    private TextView txtHabAtiv;
    private TextView txtHabSocialLife;
    private TextView txtHabDiet;
    private TextView txtRepSummary;
    private TextView txtRepType;
    private TextView txtRepEggsOffSpring;
    private TextView txtRepIncubationGestation;
    private TextView txtRepSexualMaturity;
    private TextView txtConservation;
    private TextView txtClasse;
    private TextView txtOrdem;
    private TextView txtFamilia;
    private TextView txtComprimento;
    private TextView txtPeso;


    private Button btnCollect;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection_details);

        int id = getIntent().getExtras().getInt("idanimal");
        String origin = getIntent().getExtras().getString("origin");

        initializeElements();

        btnCollect = findViewById(R.id.btnCollect);

        btnCollect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnCollect.setEnabled(false);
                btnCollect.setText("Colecionado");
            }
        });


    }

    private void initializeElements() {
        img = findViewById(R.id.img);
        txtRisk = findViewById(R.id.txtRisk);
        txtName = findViewById(R.id.txtName);
        txtSciName = findViewById(R.id.txtSciName);
        txtDescription = findViewById(R.id.txtDescription);
        txtHabSummary = findViewById(R.id.txtHabSummary);
        txtHabAtiv = findViewById(R.id.txtHabAtiv);
        imgHabAtiv = findViewById(R.id.imgHabAtiv);
        txtHabSocialLife = findViewById(R.id.txtHabSocialLife);
        imgHabSocialLife = findViewById(R.id.imgHabSocialLife);
        txtHabDiet = findViewById(R.id.txtHabDiet);
        imgHabDiet = findViewById(R.id.imgHabDiet);
        txtRepSummary = findViewById(R.id.txtRepSummary);
        imgRepType = findViewById(R.id.imgRepType);
        txtRepType = findViewById(R.id.txtRepType);
        imgRepEggsOffSpring = findViewById(R.id.imgRepEggsOffSpring);
        txtRepEggsOffSpring = findViewById(R.id.txtRepEggsOffSpring);
        imgRepIncubationGestation = findViewById(R.id.imgRepIncubationGestation);
        txtRepIncubationGestation = findViewById(R.id.txtRepIncubationGestation);
        imgRepSexualMaturity = findViewById(R.id.imgRepSexualMaturity);
        txtRepSexualMaturity = findViewById(R.id.txtRepSexualMaturity);
        txtConservation = findViewById(R.id.txtConservation);
        txtClasse = findViewById(R.id.txtClasse);
        txtOrdem = findViewById(R.id.txtOrdem);
        txtFamilia = findViewById(R.id.txtFamilia);
        txtComprimento = findViewById(R.id.txtComprimento);
        txtPeso = findViewById(R.id.txtPeso);
    }




}