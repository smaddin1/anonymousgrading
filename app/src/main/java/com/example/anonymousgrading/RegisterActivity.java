package com.example.anonymousgrading;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.amplifyframework.core.Amplify;
import com.amplifyframework.auth.AuthUserAttributeKey;
import com.amplifyframework.auth.options.AuthSignUpOptions;


public class RegisterActivity extends AppCompatActivity {
    private EditText usernameEditText;
    private EditText passwordEditText;
    private EditText reEnterPasswordEditText;
    private EditText verificationCodeEditText;
    private Button registerButton;
    private Button verifyButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        usernameEditText = findViewById(R.id.registerUsername);
        passwordEditText = findViewById(R.id.registerPassword);
        reEnterPasswordEditText = findViewById(R.id.reregisterPassword);
        verificationCodeEditText = findViewById(R.id.verificationCode);
        registerButton = findViewById(R.id.register);
        verifyButton = findViewById(R.id.verify);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });

        verifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyUser();
            }
        });

        TextView loginButton = findViewById(R.id.loginPrompt);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void registerUser() {
        String email = usernameEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String reEnterPassword = reEnterPasswordEditText.getText().toString().trim();

        if (!password.equals(reEnterPassword)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        Amplify.Auth.signUp(
                email,
                password,
                AuthSignUpOptions.builder().userAttribute(AuthUserAttributeKey.email(), email).build(),
                result -> runOnUiThread(() -> {
                    Toast.makeText(getApplicationContext(), "Verification code sent to " + email, Toast.LENGTH_LONG).show();

                }),
                error -> runOnUiThread(() -> Toast.makeText(getApplicationContext(), "Sign up failed: " + error.getMessage(), Toast.LENGTH_LONG).show())
        );
    }

    private void verifyUser() {
        String email = usernameEditText.getText().toString().trim();
        String verificationCode = verificationCodeEditText.getText().toString().trim();

        Amplify.Auth.confirmSignUp(
                email,
                verificationCode,
                result -> runOnUiThread(() -> {
                    if (result.isSignUpComplete()) {
                        Toast.makeText(getApplicationContext(), "Registration successful", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "Verification incomplete, please try again", Toast.LENGTH_SHORT).show();
                    }
                }),
                error -> runOnUiThread(() -> Toast.makeText(getApplicationContext(), "Verification failed: " + error.getMessage(), Toast.LENGTH_LONG).show())
        );
    }
}
