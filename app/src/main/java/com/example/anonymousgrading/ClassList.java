package com.example.anonymousgrading;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ClassList extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.class_list);

        SharedPreferences sharedPreferences = getSharedPreferences("CoursePrefs", MODE_PRIVATE);
        String courseName = sharedPreferences.getString("CourseName", "Default Course Name");
        String instructorName = sharedPreferences.getString("InstructorName", "Default Instructor Name");
        // Get the SharedPreferences that contains the grades
        SharedPreferences gradesPrefs = getSharedPreferences("GradesPrefs", MODE_PRIVATE);




        // Extracting data from intent
//        String courseName = getIntent().getStringExtra("courseName");
//        String instructorName = getIntent().getStringExtra("instructorName");
        //String csvContent = getIntent().getStringExtra("csvContent");

        //  TextViews for courseName and instructorName in your layout
        TextView courseNameTextView = findViewById(R.id.courseNameTextView);
        TextView instructorNameTextView = findViewById(R.id.instructorNameTextView);

        courseNameTextView.setText(courseName);
        instructorNameTextView.setText(instructorName);

        List<String> studentIds = new ArrayList<>(Arrays.asList(sharedPreferences.getString("StudentIDs", "").split(",")));

        ArrayList<String> studentDetails = new ArrayList<>();
        ArrayList<String> studentGradesDetails = new ArrayList<>();
        for (String id : studentIds) {
            String name = sharedPreferences.getString(id, null);
            if (name != null) {
                // Get the grade for the current student
                String grade = gradesPrefs.getString(id, "No grade"); // Default to "No grade" if not found
                studentGradesDetails.add(name + ", " + id + ", Grade: " + grade); // Combining name, ID, and grade
                studentDetails.add(name + ", " + id); // Combining name and ID for display
            }
        }


        // Display the CSV content

        //displayCsvContent(studentDetails);
        displayCsvContent(studentGradesDetails);
        refreshStudentList();





        Button toHomeButton = findViewById(R.id.btnHome);
        toHomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Define the action to be performed when the button is clicked
                // For example, you can start a new activity
                Intent intent = new Intent(ClassList.this, AllToGO.class);
                startActivity(intent);
            }
        });
    }

    // Modify the displayCsvContent method to directly use the passed ArrayList<String>
    private void displayCsvContent(ArrayList<String> studentGradesDetails) {
        ListView studentsListView = findViewById(R.id.studentsListView);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, studentGradesDetails) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text = (TextView) view.findViewById(android.R.id.text1);
                text.setTextColor(Color.WHITE); // Set the text color to white
                return view;
            }
        };
        studentsListView.setAdapter(adapter);
        adapter.notifyDataSetChanged();


    }
    @Override
    protected void onResume() {
        super.onResume();
        // Call the method to refresh the list here
        refreshStudentList();
    }

    private void refreshStudentList() {
        SharedPreferences sharedPreferences = getSharedPreferences("CoursePrefs", MODE_PRIVATE);
        SharedPreferences gradesPrefs = getSharedPreferences("GradesPrefs", MODE_PRIVATE);
        List<String> studentIds = new ArrayList<>(Arrays.asList(gradesPrefs.getString("StudentIDs", "").split(",")));
        ArrayList<String> studentGradesDetails = new ArrayList<>();

        for (String id : studentIds) {
            String name = sharedPreferences.getString(id, null);
            if (name != null) {
                String grade = gradesPrefs.getString(id, "No grade"); // Default to "No grade" if not found
                studentGradesDetails.add(name + ", " + id + ", Grade: " + grade); // Combining name, ID, and grade
            }
        }

        displayCsvContent(studentGradesDetails);
    }



}
