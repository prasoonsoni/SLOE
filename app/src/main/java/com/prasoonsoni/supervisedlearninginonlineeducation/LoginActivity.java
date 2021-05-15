package com.prasoonsoni.supervisedlearninginonlineeducation;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private ImageView top, bottom;
    private TextView loginText, sloganText;
    private TextInputLayout emailLayout, passwordLayout;
    private EditText emailText, passwordText;
    private Button loginButton;
    private FirebaseAuth mAuth;
    private ProgressDialog pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        top = findViewById(R.id.topImage);
        bottom = findViewById(R.id.bottomImage);
        loginText = findViewById(R.id.loginText);
        sloganText = findViewById(R.id.sloganText);
        emailLayout = findViewById(R.id.emailLayout);
        passwordLayout = findViewById(R.id.passwordLayout);
        emailText = findViewById(R.id.emailText);
        passwordText = findViewById(R.id.passwordText);
        loginButton = findViewById(R.id.loginButton);

        // Animations
        top.animate().translationYBy(2000f).setDuration(1300);
        bottom.animate().translationYBy(-2000f).setDuration(1300);
        loginText.animate().translationXBy(2000f).setDuration(800);
        sloganText.animate().translationXBy(2000f).setDuration(1000);
        emailLayout.animate().translationXBy(2000f).setDuration(1200);
        passwordLayout.animate().translationXBy(2000f).setDuration(1400);
        loginButton.animate().translationXBy(2000f).setDuration(1600);

        pd = new ProgressDialog(this);
        pd.setIndeterminate(true);
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.setTitle("Please Wait");
        pd.setMessage("Logging in...");
        pd.setCancelable(false);

        loginButton.setOnClickListener(v -> {
            String email = emailText.getText().toString();
            String password = passwordText.getText().toString();
            emailText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    emailLayout.setError(null);
                }

                @Override
                public void afterTextChanged(Editable s) {
                    if(s.toString().isEmpty()){
                        emailLayout.setError("E-Mail cannot be empty.");
                    }
                }
            });
            passwordText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    passwordLayout.setError(null);
                }

                @Override
                public void afterTextChanged(Editable s) {
                    if(s.toString().isEmpty()){
                        passwordLayout.setError("Password cannot be empty");
                    }
                }
            });
            if(email.isEmpty() && password.isEmpty()){
                emailLayout.setError("E-Mail cannot be empty.");
                passwordLayout.setError("Password cannot be empty");
            } else if(email.isEmpty()){
                emailLayout.setError("E-Mail cannot be empty.");
            } else if(password.isEmpty()){
                passwordLayout.setError("Password cannot be empty");
            } else {
                pd.show();
                mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        pd.dismiss();
                        Toast.makeText(LoginActivity.this, "Successfully Logged in.", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();
                    } else {
                        pd.dismiss();
                        Toast.makeText(LoginActivity.this, "Login Failed : "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}