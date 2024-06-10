package com.example.formationgroupe8;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    //Declaration des variables
    private ImageView image;
    private Button btnShow, btnHide;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Affectation des views
        image = findViewById(R.id.imageToHideAndShow);
        btnShow = findViewById(R.id.btnShow);
        btnHide = findViewById(R.id.btnHide);

        //First case
        btnShow.setOnClickListener(v -> {
            //Action after click to button
            image.setVisibility(View.VISIBLE);
        });

        //Second case
        btnHide.setOnClickListener(v -> {
            image.setVisibility(View.INVISIBLE);
        });

    }
}