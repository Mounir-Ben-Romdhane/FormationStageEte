package com.example.formationgroupe8;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.formationgroupe8.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity {

    private TextView goToSignIn;
    private EditText fullName, email, cin, phone, password;
    private Button btnSignUp;
    private String fullNameInput, emailInput, cinInput, phoneInput, passwordInput;

    private final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        getSupportActionBar().hide();

        //Affectations
        goToSignIn = findViewById(R.id.goToSignIn);
        fullName = findViewById(R.id.fullNameSignUp);
        email = findViewById(R.id.emailSignUp);
        cin = findViewById(R.id.cinSignUp);
        phone = findViewById(R.id.phoneSignUp);
        password = findViewById(R.id.passwordSignUp);
        btnSignUp = findViewById(R.id.btnSignUp);

        //firebase
        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);

        goToSignIn.setOnClickListener(v -> {
            startActivity(new Intent(SignUpActivity.this, SignInActivity.class));
        });

        btnSignUp.setOnClickListener(v -> {
            if(validate()) {
                progressDialog.setMessage("Please wait...!");
                progressDialog.show();
                firebaseAuth.createUserWithEmailAndPassword(emailInput, passwordInput).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        sendEmailVerification();
                    }else {
                        Toast.makeText(this, "Failed !", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                });
            }
        });
    }

    private void sendEmailVerification() {
        FirebaseUser logedUser = firebaseAuth.getCurrentUser();
        if (logedUser != null) {
            logedUser.sendEmailVerification().addOnCompleteListener(task -> {
               if (task.isSuccessful()) {
                   sendUserData();
                   progressDialog.dismiss();
                   startActivity(new Intent(SignUpActivity.this, SignInActivity.class));
                   firebaseAuth.signOut();
                   Toast.makeText(this, "Registration done ! Please check your email !", Toast.LENGTH_LONG).show();
                   finish();
               }else {
                   Toast.makeText(this, "Registration Failed !", Toast.LENGTH_SHORT).show();
                   progressDialog.dismiss();
               }
            });
        }
    }

    private void sendUserData() {
        User user1 = new User(fullNameInput,emailInput,cinInput,phoneInput);
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference myRef = firebaseDatabase.getReference("Users");
        myRef.child(Objects.requireNonNull(firebaseAuth.getUid())).setValue(user1);
    }

    private boolean validate() {
        boolean result = false;

        //get inputs
        fullNameInput = fullName.getText().toString().trim();
        emailInput = email.getText().toString().trim();
        cinInput = cin.getText().toString().trim();
        phoneInput = phone.getText().toString().trim();
        passwordInput = password.getText().toString().trim();
        
        if (fullNameInput.length() < 7) {
            fullName.setError("Full name invalid !");
        } else if (!isValidPattern(emailInput, EMAIL_PATTERN)) {
            email.setError("Email is invalid");
        } else if (cinInput.length() != 8) {
            cin.setError("CIN is invalid");
        } else if (phoneInput.length() != 8) {
            phone.setError("Phone is invalid");
        }else if(passwordInput.length() < 5){
            password.setError("Password is invalid");
        }else {
            result = true;
        }

        return result;
    }

    private boolean isValidPattern(String mot, String patternn) {
        Pattern pattern = Pattern.compile(patternn);
        Matcher matcher = pattern.matcher(mot);
        return matcher.matches();
    }


}