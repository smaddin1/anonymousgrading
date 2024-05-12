package com.example.anonymousgrading;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.amplifyframework.AmplifyException;
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin;
import com.amplifyframework.core.Amplify;

public class MainActivity extends AppCompatActivity {
    private EditText emailEditText;
    private EditText passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Amplify
        try {
            Amplify.addPlugin(new AWSCognitoAuthPlugin());
            Amplify.configure(getApplicationContext());
            Log.i("MainActivity", "Amplify has been initialized");
        } catch (AmplifyException e) {
            Log.e("MainActivity", "Failed to initialize Amplify", e);
        }

        emailEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);

        Button loginButton = findViewById(R.id.login);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });

        TextView registerButton = findViewById(R.id.registerPrompt);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    private void loginUser() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Email or password cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        Amplify.Auth.signIn(
                email,
                password,
                result -> {
                    if (result.isSignInComplete()) {
                        runOnUiThread(() -> {
                            Toast.makeText(getApplicationContext(), "Login successful", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(MainActivity.this, AllToGO.class);
                            startActivity(intent);
                            finish();
                        });
                    } else {
                        runOnUiThread(() -> Toast.makeText(getApplicationContext(), "Login not complete, please try again", Toast.LENGTH_LONG).show());
                    }
                },
                error -> runOnUiThread(() -> Toast.makeText(getApplicationContext(), "Login failed: " + error.getMessage(), Toast.LENGTH_LONG).show())
        );
    }
}
