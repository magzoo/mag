package com.example.magzoo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.magzoo.Utilities.Utils;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class CollectionDetails extends AppCompatActivity {
    private static final int REQUEST_CODE_LOCATION_PERMISSION = 1;

    private ImageView img;
    private ImageView imgHabAtiv;
    private ImageView imgHabSocialLife;
    private ImageView imgHabDiet;
    private ImageView imgRepType;
    private ImageView imgRepEggsOffSpring;
    private ImageView imgRepIncubationGestation;
    private ImageView imgRepSexualMaturity;
    private ImageView imgMap;

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
    private TextView txtOvosCrias;
    private TextView txtRepEggsOffSpring;
    private TextView txtGestacaoIncubacao;
    private TextView txtRepIncubationGestation;
    private TextView txtRepSexualMaturity;
    private TextView txtConservation;
    private TextView txtDIstHabSummary;
    private TextView txtClasse;
    private TextView txtOrdem;
    private TextView txtFamilia;
    private TextView txtTamanho;
    private TextView txtPeso;

    private double distHabCoordinateX;
    private double distHabCoordinateY;

    private Button btnCollect;
    private Button btnDistance;

    private double latitude;
    private double longitude;
    private TextView txtDistance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection_details);
        btnCollect = findViewById(R.id.btnCollect);
        btnDistance = findViewById(R.id.btnDistance);

        int animalId = getIntent().getExtras().getInt("animalId");
        Log.d("bajoraz", "vindo do extraas: " + animalId);
        String origin = getIntent().getExtras().getString("origin");

        initializeElements();

        sqlGetAnimalDetails(animalId);


        btnCollect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sqlCollectAnimal(animalId);
                btnCollect.setEnabled(false);
                btnCollect.setText("Colecionado");
            }
        });
        btnDistance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                distancefromUser();
                view.setEnabled(false);
            }
        });



    }

    public void distancefromUser()
    {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(CollectionDetails.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 101);
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            LocationRequest locationRequest = new LocationRequest();
            locationRequest.setInterval(10000);
            locationRequest.setFastestInterval(3000);
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

            LocationServices.getFusedLocationProviderClient(CollectionDetails.this).requestLocationUpdates(locationRequest, new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    super.onLocationResult(locationResult);
                    LocationServices.getFusedLocationProviderClient(CollectionDetails.this).removeLocationUpdates(this);
                    if(locationResult != null && locationResult.getLocations().size() > 0){
                        int latestLocationIndex = locationResult.getLocations().size() - 1;
                        latitude = locationResult.getLocations().get(latestLocationIndex).getLatitude();
                        longitude = locationResult.getLocations().get(latestLocationIndex).getLongitude();
                        Double distance = distance(latitude, longitude, distHabCoordinateX, distHabCoordinateY);

                        StringBuilder sb = new StringBuilder();
                        sb.append("Voc??, encontra-se a ");
                        sb.append(Utils.round(distance,2));
                        sb.append("Km do habitat natural mais pr??ximo");
                        txtDistance.setText(sb.toString());
                        txtDistance.setVisibility(View.VISIBLE);
                    }
                }
            }, Looper.getMainLooper());
        }


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
        txtGestacaoIncubacao = findViewById(R.id.gestacaoIncubacao);
        imgRepIncubationGestation = findViewById(R.id.imgRepIncubationGestation);
        txtOvosCrias = findViewById(R.id.ovosCrias);
        txtRepIncubationGestation = findViewById(R.id.txtRepIncubationGestation);
        imgRepSexualMaturity = findViewById(R.id.imgRepSexualMaturity);
        txtRepSexualMaturity = findViewById(R.id.txtRepSexualMaturity);
        txtConservation = findViewById(R.id.txtConservation);
        txtDIstHabSummary = findViewById(R.id.txtDIstHabSummary);
        txtClasse = findViewById(R.id.txtClasse);
        txtOrdem = findViewById(R.id.txtOrdem);
        txtFamilia = findViewById(R.id.txtFamilia);
        txtTamanho = findViewById(R.id.txtTamanho);
        txtPeso = findViewById(R.id.txtPeso);
        txtDistance = findViewById(R.id.txtDistance);
        imgMap = findViewById(R.id.map);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 101) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(CollectionDetails.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 102);
            }
        }
        if (requestCode == 102) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                LocationRequest locationRequest = new LocationRequest();
                locationRequest.setInterval(10000);
                locationRequest.setFastestInterval(3000);
                locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

                LocationServices.getFusedLocationProviderClient(CollectionDetails.this).requestLocationUpdates(locationRequest, new LocationCallback() {
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        super.onLocationResult(locationResult);
                        LocationServices.getFusedLocationProviderClient(CollectionDetails.this).removeLocationUpdates(this);
                        if (locationResult != null && locationResult.getLocations().size() > 0) {
                            int latestLocationIndex = locationResult.getLocations().size() - 1;
                            latitude = locationResult.getLocations().get(latestLocationIndex).getLatitude();
                            longitude = locationResult.getLocations().get(latestLocationIndex).getLongitude();
                            Double distance = distance(latitude, longitude, distHabCoordinateX, distHabCoordinateY);

                            StringBuilder sb = new StringBuilder();
                            sb.append("Voc??, encontra-se a ");
                            sb.append(Utils.round(distance,2));
                            sb.append("Km do habitat natural mais pr??ximo");
                            txtDistance.setText(sb.toString());
                        }
                    }
                }, Looper.getMainLooper());
            }
        }
    }

    private double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return (dist*1.609344);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }



    private void sqlGetAnimalDetails(int animalId){
        String msg = "";
        try
        {
            Connection connection = Utils.getConnection();

            if (connection == null)
            {
                msg = "Verifique a sua liga????o ?? Internet!";
            }
            else
            {
                String query = "select Animal.*, FORMAT(UserAnimal.Date, 'dd/MM/yyyy') as CollectDate  from Animal FULL OUTER JOIN UserAnimal ON Animal.Id = UserAnimal.FK_Animal where Animal.Id =" +  animalId;
                Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                Log.d("bajoraz", "query executed");
                if (rs.next()) {
                    String animalImage = rs.getString("Icon");
                    if(animalImage!=null) {
                        if (!animalImage.equals("")) {
                            Bitmap bitmap = Utils.base64ToImg(animalImage);
                            img.setImageBitmap(bitmap);
                        }
                    }
                    txtName.setText(rs.getString("Name"));
                    txtSciName.setText(rs.getString("CientificName"));
                    txtRisk.setText(rs.getString("Risk"));
                    txtDescription.setText(rs.getString("Summary"));
                    txtClasse.setText(rs.getString("Class"));
                    txtOrdem.setText(rs.getString("Order"));
                    txtFamilia.setText(rs.getString("Family"));
                    String lenght = rs.getString("Length");
                    String height = rs.getString("Height");
                    if(lenght.equals("0")){
                        txtTamanho.setText("Altura: " + height);
                    }else if(height.equals("0")){
                        txtTamanho.setText("Comprimento: " + lenght);
                    }else {
                        txtTamanho.setText("Comprimento: " + lenght + "\nAltura: " + height);
                    }

                    String weightF = rs.getString("WeightF");
                    String weightM = rs.getString("WeightM");
                    if(weightF.equals("0")){
                        txtPeso.setText("Macho: " + weightM);
                    }else if(weightM.equals("0")){
                        txtPeso.setText("F??mea: " + weightF);
                    }else {
                        txtPeso.setText("F??mea: " + weightF + "\nMacho: " + weightM);
                    }

                    txtHabSummary.setText(rs.getString("HabSummary"));

                    String habActivity = rs.getString("HabActivity");
                    if(habActivity.equals("Noturna")){
                        imgHabAtiv.setImageResource(R.drawable.noturna);
                    }else if(habActivity.equals("Crepuscular")){
                        imgHabAtiv.setImageResource(R.drawable.crepuscular);
                    }else  if(habActivity.equals("Crepuscular e Diurna")){
                        imgHabAtiv.setImageResource(R.drawable.crepusculardiurna);
                    }else  if(habActivity.equals("Crepuscular e Noturna")){
                        imgHabAtiv.setImageResource(R.drawable.crepuscularnoturna);
                    }else  if(habActivity.equals("Diurna")){
                        imgHabAtiv.setImageResource(R.drawable.diurna);
                    }else  if(habActivity.equals("Diurna e Noturna")){
                        imgHabAtiv.setImageResource(R.drawable.diurnanoturna);
                    }

                    txtHabAtiv.setText(rs.getString("HabActivity"));

                    String habSocialLife = rs.getString("HabSocialLife");
                    if(habSocialLife.equals("Bando")){
                        imgHabSocialLife.setImageResource(R.drawable.bando);
                    }else if(habSocialLife.equals("Bando")){
                        imgHabSocialLife.setImageResource(R.drawable.casal);
                    }else if(habSocialLife.equals("Familiar")){
                        imgHabSocialLife.setImageResource(R.drawable.familiar);
                    }else if(habSocialLife.equals("Greg??ria")){
                        imgHabSocialLife.setImageResource(R.drawable.gregaria);
                    }else if(habSocialLife.equals("Solit??ria")){
                        imgHabSocialLife.setImageResource(R.drawable.solitario);
                    }
                    txtHabSocialLife.setText(rs.getString("HabSocialLife"));

                    String habDiet = rs.getString("HabDiet");
                    if(habDiet.equals("Carn??vora")){
                        imgHabDiet.setImageResource(R.drawable.carnivora);
                    }else if(habDiet.equals("Frug??vora")){
                        imgHabDiet.setImageResource(R.drawable.frugivera);
                    }else if(habDiet.equals("Frug??vora ou Gran??vora")){
                        imgHabDiet.setImageResource(R.drawable.frugiveragranivora);
                    }else if(habDiet.equals("Herb??vora")){
                        imgHabDiet.setImageResource(R.drawable.herbivora);
                    }else if(habDiet.equals("Herbivora ou Omn??vora")){
                        imgHabDiet.setImageResource(R.drawable.herbivoraomnivora);
                    }else if(habDiet.equals("Omn??vora")){
                        imgHabDiet.setImageResource(R.drawable.omnivora);
                    }else if(habDiet.equals("Omn??vora e Filtrador")){
                        imgHabDiet.setImageResource(R.drawable.omnivorafiltrador);
                    }else if(habDiet.equals("Pisc??vora")){
                        imgHabDiet.setImageResource(R.drawable.piscivora);
                    }
                    txtHabDiet.setText(rs.getString("HabDiet"));

                    txtRepSummary.setText(rs.getString("RepSummary"));

                    String repType = rs.getString("RepType");
                    if(repType.equals("Ov??para")){
                        imgRepType.setImageResource(R.drawable.ovipara);
                        txtOvosCrias.setText("Ovos");
                        txtGestacaoIncubacao.setText("Incuba????o");
                    }else if(repType.equals("Viv??para")){
                        imgRepType.setImageResource(R.drawable.vivipara);
                        txtOvosCrias.setText("N?? de Crias");
                        txtGestacaoIncubacao.setText("Gesta????o");
                    }
                    txtRepType.setText(rs.getString("RepType"));


                    imgRepEggsOffSpring.setImageResource(R.drawable.crias);
                    txtRepEggsOffSpring.setText(rs.getString("RepEggsOffSpring"));

                    imgRepIncubationGestation.setImageResource(R.drawable.gestacao);
                    txtRepIncubationGestation.setText(rs.getString("RepIncubationGestation"));

                    imgRepSexualMaturity.setImageResource(R.drawable.maturidade);
                    txtRepSexualMaturity.setText(rs.getString("RepSexualMaturity"));

                    txtConservation.setText(rs.getString("Conservation"));
                    txtDIstHabSummary.setText(rs.getString("DistHabSummary"));
                    distHabCoordinateX = Double.parseDouble(rs.getString("DistHabCoordinateX"));
                    distHabCoordinateY = Double.parseDouble(rs.getString("DistHabCoordinateY"));

                    String date = rs.getString("CollectDate");
                    if(date != null){
                        btnCollect.setEnabled(false);
                        btnCollect.setText("Colecionado a " + date);
                    }
                    String mapImage = rs.getString("Map");
                    if(mapImage!=null) {
                        if (!mapImage.equals("")) {
                            Bitmap bitmap = Utils.base64ToImg(mapImage);
                            imgMap.setImageBitmap(bitmap);
                        }
                    }
                }
            }
        }
        catch (Exception ex)
        {
            msg = ex.getMessage();
            Log.d("bajoraz", "erro " +ex.getMessage());
        }
        if(!msg.equals(""))
            Utils.toast(this, msg);
    }

    private void sqlCollectAnimal(int animalId){
        String msg = "";
        try
        {
            Connection connection = Utils.getConnection();

            if (connection == null)
            {
                msg = "Verifique a sua liga????o ?? Internet!";
            }
            else
            {
                SharedPreferences sharedLogin = getSharedPreferences("SHARED_PREFS", MODE_PRIVATE);
                String email = sharedLogin.getString("email", "");
                String query = "exec dbo.spInsertUserAnimal '" + email + "','" + animalId + "'";
                Statement stmt = connection.createStatement();
                stmt.executeQuery(query);
                /*if (rs.next()) {

                }*/
            }
            connection.close();
        }
        catch (Exception ex)
        {
            msg = ex.getMessage();
            Log.d("bajoraz", "erro " +ex.getMessage());
        }
        if(!msg.equals(""))
            Utils.toast(this, msg);
    }

}