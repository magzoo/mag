package com.example.magzoo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class CollectionDetails extends AppCompatActivity {

    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection_details);

        textView = findViewById(R.id.textView2);

        int id = getIntent().getExtras().getInt("idanimal");

        textView.setText("id: " + id);

    }
}