package com.example.formationgroupe8;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

public class HomeActivity extends AppCompatActivity {

    private TextView email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        email = findViewById(R.id.emailSharedP);
        password = findViewById(R.id.passwordSharedP);

        //local storage
        SharedPreferences preferences = getSharedPreferences("test", MODE_PRIVATE);

        String e = preferences.getString("emailll","");

        email.setText("aaaa"+e);

        password.setText(preferences.getString("passwordd",""));

    }
}