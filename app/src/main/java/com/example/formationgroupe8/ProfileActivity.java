package com.example.formationgroupe8;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {

    private EditText fullName, email, cin, phone;
    private Button btnEditProfile, btnSignOut;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseUser logedUser;
    private DatabaseReference reference;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        fullName = findViewById(R.id.fullNameProfile);
        email = findViewById(R.id.emailProfile);
        cin = findViewById(R.id.cinProfile);
        phone = findViewById(R.id.phoneProfile);
        btnEditProfile = findViewById(R.id.btnEditProfile);
        btnSignOut = findViewById(R.id.btnSignOut);


        firebaseAuth =FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        logedUser = firebaseAuth.getCurrentUser();
        reference = firebaseDatabase.getReference().child("Users").child(logedUser.getUid());
        progressDialog = new ProgressDialog(this);
        SharedPreferences preferences = getSharedPreferences("checkBox", MODE_PRIVATE);

        progressDialog.setMessage("Please wait...!");
        progressDialog.show();


        //from firebase to app
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //get values from firebase
                String fullNameFirebase = snapshot.child("fullName").getValue().toString();
                String emailFirebase = snapshot.child("email").getValue().toString();
                String cinFirebase = snapshot.child("cin").getValue().toString();
                String phoneFirebase = snapshot.child("phone").getValue().toString();

                //desplay values
                fullName.setText(fullNameFirebase);
                email.setText(emailFirebase);
                cin.setText(cinFirebase);
                phone.setText(phoneFirebase);
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ProfileActivity.this, "error !", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });

        //from app to firebase
        btnEditProfile.setOnClickListener(v -> {
            String updatedFullName = fullName.getText().toString().trim();
            String updatedCin = cin.getText().toString().trim();
            String updatedphone = phone.getText().toString().trim();
            reference.child("fullName").setValue(updatedFullName);
            reference.child("cin").setValue(updatedCin);
            reference.child("phone").setValue(updatedphone);
            Toast.makeText(this, "Your data has been changed successfully !", Toast.LENGTH_SHORT).show();
            email.clearFocus();
            cin.clearFocus();
            phone.clearFocus();
        });

        btnSignOut.setOnClickListener(v -> {
            startActivity(new Intent(ProfileActivity.this, SignInActivity.class));
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("rememberMe", false);
            editor.apply();
            firebaseAuth.signOut();
        });
    }
}