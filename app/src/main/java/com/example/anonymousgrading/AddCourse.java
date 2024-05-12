package com.example.anonymousgrading;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class AddCourse extends AppCompatActivity {
    private static final int PICK_CSV_FILE = 1;
    private TextView csvContentsTextView;
    private EditText courseName, instructorName;
    private Button addClassButton;
    private String csvContent = ""; // To hold the CSV content


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);

        csvContentsTextView = findViewById(R.id.csvContentsTextView);
        courseName = findViewById(R.id.courseName); // Assuming you have this in your layout
        instructorName= findViewById(R.id.instructorName); // Assuming you have this in your layout
        addClassButton = findViewById(R.id.AddClassButton); // Assuming you have this in your layout




        Button uploadRosterButton = findViewById(R.id.uploadRosterButton);
        uploadRosterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("text/csv");
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(intent, PICK_CSV_FILE);
            }
        });

        addClassButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences sharedPreferences = getSharedPreferences("CoursePrefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("CourseName", courseName.getText().toString());
                editor.putString("InstructorName", instructorName.getText().toString());


                String courseNamestr = courseName.getText().toString();
                String instructorNamestr = instructorName.getText().toString();
                // Now you need to pass these along with the CSV content to the ClassList activity
                Intent intent = new Intent(AddCourse.this, ClassList.class);
                intent.putExtra("courseName", courseNamestr);
                intent.putExtra("instructorName", instructorNamestr);
                intent.putExtra("csvContent", csvContent);
                startActivity(intent);

                editor.apply();

            }
        });



        Button loginButton = findViewById(R.id.btnHome);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Define the action to be performed when the button is clicked
                // For example, you can start a new activity
                Intent intent = new Intent(AddCourse.this, AllToGO.class);
                startActivityForResult(intent,PICK_CSV_FILE);
            }
        });


    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        super.onActivityResult(requestCode, resultCode, resultData);
        if (requestCode == PICK_CSV_FILE && resultCode == Activity.RESULT_OK) {
            if (resultData != null) {
                Uri uri = resultData.getData();
                parseCsvAndSave(uri);
            }
        }
    }

    private void parseCsvAndSave(Uri csvUri) {
        StringBuilder csvContentBuilder = new StringBuilder();
        ArrayList<String> studentIds = new ArrayList<>(); // List to keep track of all student IDs

        try {
            InputStream inputStream = getContentResolver().openInputStream(csvUri);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            SharedPreferences sharedPreferences = getSharedPreferences("CoursePrefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();

            while ((line = reader.readLine()) != null) {
                // Assuming CSV format is: Name,ID
                String[] tokens = line.split(",");
                if(tokens.length >= 2) { // Check if the line has at least two tokens
                    String name = tokens[0];
                    String id = tokens[1];
                    studentIds.add(id); // Add the ID to the list
                    editor.putString(id, name); // Use ID as key, Name as value
                    // Add line to CSV content string
                    csvContentBuilder.append(name).append(", ").append(id).append("\n");



                }
            }
            editor.putString("StudentIDs", TextUtils.join(",", studentIds));
            editor.apply();
            // Example CSV format: Name,ID,Barcode



            inputStream.close();
            // Convert StringBuilder to String and assign to csvContent
            csvContent = csvContentBuilder.toString();
            // Update TextView with CSV content
            csvContentsTextView.setText(csvContent.toString());




        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error reading CSV file", Toast.LENGTH_SHORT).show();
        }
    }


}