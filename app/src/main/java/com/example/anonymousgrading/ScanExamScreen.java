package com.example.anonymousgrading;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;



import android.Manifest;
import android.content.pm.PackageManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.Map;

public class ScanExamScreen extends AppCompatActivity {
    private EditText gradeInput;
    private String currentStudentBarcode; // Holds the current student's barcode
    private TextView studentInfoTextView; // TextView to display the student info
    private static final int REQUEST_CAMERA_PERMISSION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_exam_screen);



        studentInfoTextView = findViewById(R.id.studentInfoTextView); // Make sure you have a TextView with this ID in your layout


        Button scanButton = findViewById(R.id.scanButton);
        gradeInput = findViewById(R.id.gradeInput);
        Button submitGradeButton = findViewById(R.id.submitGradeButton);


        Button button2home = findViewById(R.id.btnHome2);

        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Initiate scan
                checkPermissionAndScan();
            }
        });

        submitGradeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Here, handle the submission of the grade
                // For example, save the grade associated with the scanned barcode
                // Here, handle the submission of the grade
                // For example, save the grade associated with the scanned barcode
                String grade = gradeInput.getText().toString().trim();

                if (!grade.isEmpty() && currentStudentBarcode != null) {
                    // Obtain the SharedPreferences for grades
                    SharedPreferences sharedPreferences = getSharedPreferences("ExamPrefs", MODE_PRIVATE);
                    String studentName = sharedPreferences.getString(currentStudentBarcode, "");

                    SharedPreferences gradesPrefs = getSharedPreferences("GradesPrefs", MODE_PRIVATE);
                    SharedPreferences.Editor editor = gradesPrefs.edit();

                    // Store the student name along with the grade in a composite string, separated by a delimiter, for example ":".
                    String compositeValue = studentName + ":" + grade;
                    editor.putString(currentStudentBarcode, compositeValue);
                    editor.apply();

                    Toast.makeText(ScanExamScreen.this, "Grade submitted for " + studentName + ".", Toast.LENGTH_SHORT).show();

                    // Clear currentStudentBarcode and gradeInput for next use
                    currentStudentBarcode = null;
                    gradeInput.setText("");
                    studentInfoTextView.setText("");
                } else {
                    Toast.makeText(ScanExamScreen.this, "Please enter a grade and scan a student barcode.", Toast.LENGTH_SHORT).show();
                }


            }
        });

//
//        // Assuming 'barcode' is the key you are using to fetch the composite data
//        SharedPreferences gradesPrefs = getSharedPreferences("GradesPrefs", MODE_PRIVATE);
//        String compositeValue = gradesPrefs.getString(barcode, "");
//        String[] parts = compositeValue.split(":");
//        if (parts.length == 2) {
//            String studentName = parts[0];
//            String studentGrade = parts[1];
//            // Now you have the student's name and grade separated
//            // You can use them as needed
//        }









        button2home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Define the action to be performed when the button is clicked
                // For example, you can start a new activity
                Intent intent = new Intent(ScanExamScreen.this, AllToGO.class);
                startActivity(intent);
            }
        });
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("currentStudentBarcode", currentStudentBarcode);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        currentStudentBarcode = savedInstanceState.getString("currentStudentBarcode");
    }


    private void checkPermissionAndScan() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            new IntentIntegrator(this).initiateScan(); // ZXing library's integration
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},
                    REQUEST_CAMERA_PERMISSION);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                new IntentIntegrator(this).initiateScan(); // Permission granted, initiate scan
            } else {
                Toast.makeText(this, "Camera permission is required to scan barcodes.", Toast.LENGTH_LONG).show();
            }
        }
    }


    private void findStudentDetails(String scannedBarcode) {
        SharedPreferences sharedPreferences = getSharedPreferences("ExamPrefs", MODE_PRIVATE);
        Map<String, ?> allEntries = sharedPreferences.getAll();

        String studentName = null;
        String studentId = null;

        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            if (scannedBarcode.equals(entry.getKey())) {
                // We found a match for the barcode
                studentId = scannedBarcode;// The barcode itself
                studentName = getStudentNameFromBarcode(scannedBarcode); // The student name associated with this barcode
                break;
            }
        }

        if (studentName != null && studentId != null) {
            // Display student name and ID and allow grade entry
            Toast.makeText(this, "Student ID: " + studentId + "\nName: " + studentName, Toast.LENGTH_LONG).show();
            enableGradeEntryForStudent(studentId);
        } else {
            // Handle case where the barcode does not match any student
            Toast.makeText(this, "No matching student found for the scanned barcode.", Toast.LENGTH_LONG).show();
        }
    }

    private void enableGradeEntryForStudent(String barcode) {
        currentStudentBarcode = barcode; // Use barcode as the unique identifier

        // Now let's update the UI with the student's information
        String studentName = getStudentNameFromBarcode(barcode);
        if (studentName != null) {
            studentInfoTextView.setText("Grading for: " + barcode);
            gradeInput.setEnabled(true); // Enable the grade input field
            gradeInput.requestFocus(); // Optionally, move the focus to the grade input
        } else {
            // If we don't have a student name, it means the barcode wasn't recognized
            studentInfoTextView.setText("No student found for this barcode.");
            gradeInput.setEnabled(false); // Disable the grade input field
        }
    }
    private String getStudentNameFromBarcode(String barcode) {
        SharedPreferences sharedPreferences = getSharedPreferences("ExamPrefs", MODE_PRIVATE);
        return sharedPreferences.getString(barcode, null); // Returns the student name, or null if not found
    }








    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result = (IntentResult) IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                // Handle the scan result
                String scannedBarcode = result.getContents();
                // Implement your logic here. For example:
                // - Look up the student associated with this barcode
                // - Allow grade entry for that student
                //Toast.makeText(this, "Scanned: " + barcode, Toast.LENGTH_LONG).show();
                // Now find the student details using the scanned barcode
                findStudentDetails(scannedBarcode);

            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }




}