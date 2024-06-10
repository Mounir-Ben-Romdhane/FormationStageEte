package com.example.formationgroupe8;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class ForgetPasswordActivity extends AppCompatActivity {

    private Button goToSignIn, btnForget;
    private EditText email;
    private String emailString;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        getSupportActionBar().hide();

        goToSignIn = findViewById(R.id.goToSignInFromForget);
        btnForget = findViewById(R.id.btnForget);
        email = findViewById(R.id.emailForget);

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);

        goToSignIn.setOnClickListener(v -> {
            startActivity(new Intent(ForgetPasswordActivity.this, SignInActivity.class));
        });

        btnForget.setOnClickListener(v -> {
            emailString = email.getText().toString().trim();
            if (emailString.isEmpty()) {
                email.setError("Email is required !");
            }else {
                progressDialog.setMessage("Please wait");
                progressDialog.show();
                firebaseAuth.sendPasswordResetEmail(emailString).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Password reset email sent !", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                        startActivity(new Intent(ForgetPasswordActivity.this, SignInActivity.class));
                        finish();
                    }else {
                        Toast.makeText(this, "error!", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                });
            }
        });
    }
}