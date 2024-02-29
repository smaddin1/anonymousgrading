package com.example.anonymousgrading;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AllToGO extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_to_go);

        Button Buttontologin = findViewById(R.id.button);
        Buttontologin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Define the action to be performed when the button is clicked
                // For example, you can start a new activity
                Intent intent = new Intent(AllToGO.this, MainActivity.class);
                startActivity(intent);
            }
        });
        Button addcoursebutton = findViewById(R.id.button2);
        addcoursebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Define the action to be performed when the button is clicked
                // For example, you can start a new activity
                Intent intent = new Intent(AllToGO.this, AddCourse.class);
                startActivity(intent);
            }
        });
        Button addexambutton = findViewById(R.id.button3);
        addexambutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Define the action to be performed when the button is clicked
                // For example, you can start a new activity
                Intent intent = new Intent(AllToGO.this, AddExam.class);
                startActivity(intent);
            }
        });
        Button barcodenamebutton = findViewById(R.id.button4);
        barcodenamebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Define the action to be performed when the button is clicked
                // For example, you can start a new activity
                Intent intent = new Intent(AllToGO.this, BarcodeName.class);
                startActivity(intent);
            }
        });
        Button scanexambutton = findViewById(R.id.button5);
        scanexambutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Define the action to be performed when the button is clicked
                // For example, you can start a new activity
                Intent intent = new Intent(AllToGO.this, ScanExamScreen.class);
                startActivity(intent);
            }
        });
    }
}