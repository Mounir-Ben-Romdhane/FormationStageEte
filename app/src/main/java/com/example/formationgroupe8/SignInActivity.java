package com.example.formationgroupe8;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignInActivity extends AppCompatActivity {

    //Declarations
    private TextView goToForget, goToSignUp;
    private EditText email, password;
    private CheckBox rememberMe;
    private Button btnSignIn;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    private String emailInput, passwordInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        getSupportActionBar().hide();

        //affectations
        goToForget = findViewById(R.id.goToForget);
        goToSignUp = findViewById(R.id.goToSignUp);
        email = findViewById(R.id.emailSignIn);
        password = findViewById(R.id.passwordSignIn);
        rememberMe = findViewById(R.id.rememberMe);
        btnSignIn = findViewById(R.id.btnSignIn);

        //firebase
        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);

        //local storage
        SharedPreferences preferences = getSharedPreferences("checkBox", MODE_PRIVATE);
        SharedPreferences preferences1 = getSharedPreferences("test", MODE_PRIVATE);

        boolean resCheckBox = preferences.getBoolean("rememberMe", false);

        if (resCheckBox) {
            startActivity(new Intent(SignInActivity.this, ProfileActivity.class));
        }

        rememberMe.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (buttonView.isChecked()) {
                SharedPreferences.Editor editor = preferences.edit();

                SharedPreferences.Editor editor1 = preferences1.edit();
                editor1.putString("emailll", email.getText().toString().trim());
                editor.putBoolean("rememberMe", true);

                editor1.putString("passwordd", password.getText().toString().trim());
                editor.apply();
            }else {
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean("rememberMe", false);
                editor.putString("emailll", email.getText().toString().trim());
                editor.putString("passwordd", password.getText().toString().trim());
                editor.apply();
            }
        });

        //Actions
        goToForget.setOnClickListener(v -> {
            startActivity(new Intent(SignInActivity.this, ForgetPasswordActivity.class));
        });

        goToSignUp.setOnClickListener(v -> {
            startActivity(new Intent(SignInActivity.this, SignUpActivity.class));
        });

        btnSignIn.setOnClickListener(v -> {
            progressDialog.setMessage("Please wait...!");
            progressDialog.show();

            //get user inputs
            emailInput = email.getText().toString().trim();
            passwordInput = password.getText().toString().trim();

            //login
            firebaseAuth.signInWithEmailAndPassword(emailInput, passwordInput).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    checkEmailVerfication();
                }else {
                    Toast.makeText(this, "Failed to login !", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            });
        });

    }

    private void checkEmailVerfication() {
        FirebaseUser logedUser = firebaseAuth.getCurrentUser();
        if (logedUser != null) {
            if (logedUser.isEmailVerified()) {
                startActivity(new Intent(SignInActivity.this, ProfileActivity.class));
                progressDialog.dismiss();
                finish();
            }else {
                Toast.makeText(this, "Please verify your email", Toast.LENGTH_SHORT).show();
                logedUser.sendEmailVerification();
                firebaseAuth.signOut();
                progressDialog.dismiss();
            }
        }
    }
}