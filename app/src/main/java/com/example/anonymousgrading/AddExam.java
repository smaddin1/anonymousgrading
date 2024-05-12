package com.example.anonymousgrading;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class AddExam extends AppCompatActivity {
    private EditText examNameEditText;
    private EditText examDateEditText;
    private Button addExamButton;
    private Button homeButton;
    private DatePickerDialog datePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_exam);

        examNameEditText = findViewById(R.id.examName);
        examDateEditText = findViewById(R.id.examDate);
        addExamButton = findViewById(R.id.addExamButton);

        // Initialize DatePickerDialog
        initDatePicker();

        // When the examDateEditText is clicked, show the DatePickerDialog
        examDateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerDialog.show();
            }
        });

        addExamButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Ensure the exam name and date are not empty
                if (validateInput()) {
                    // Logic to add the exam (e.g., saving to a database or SharedPreferences)
                    // Example: Retrieving student names and generating barcodes
                    SharedPreferences sharedPreferences = getSharedPreferences("ExamPrefs", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();

                    ArrayList<String> studentNames = getStudentNames();
                    ArrayList<String> barcodeFileNames = new ArrayList<>();

                    for (String studentName : studentNames) {
                        // Generate a unique identifier for the student, such as an email or ID
                        String studentIdentifier = studentName; // Replace this with actual unique identifier
                        String barcodeFileName = saveBarcodeImage(studentIdentifier);

                        if (barcodeFileName != null) {
                            barcodeFileNames.add(barcodeFileName);
                            // We store the barcode file name using the student's identifier as the key
                            editor.putString(studentIdentifier, barcodeFileName);
                        }

                    }
                    editor.apply();
                    // Prepare to pass data to BarcodeName activity
                    Intent intent = new Intent(AddExam.this, BarcodeName.class);

                    startActivity(intent);

                }
            }
            //SharedPreferences mydata=getSharedPreferences("CoursePrefs")
        });


        Button toHomeButton = findViewById(R.id.btnHome);
        toHomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Define the action to be performed when the button is clicked
                // For example, you can start a new activity
                Intent intent = new Intent(AddExam.this, AllToGO.class);
                startActivity(intent);
            }
        });
    }

    private boolean validateInput() {
        if (examNameEditText.getText().toString().trim().isEmpty()) {
            examNameEditText.setError("Please enter the exam name");
            return false;
        }
        if (examDateEditText.getText().toString().trim().isEmpty()) {
            examDateEditText.setError("Please pick the exam date");
            return false;
        }
        return true;
    }

    private void initDatePicker() {
        Calendar calendar = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        // Note: January is month 0.
                        String date = (month + 1) + "/" + dayOfMonth + "/" + year;
                        examDateEditText.setText(date);
                    }
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
    }
    private Bitmap generateBarcode(String text) throws WriterException {
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        BitMatrix bitMatrix = multiFormatWriter.encode(text, BarcodeFormat.CODE_128, 600, 300);
        BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
        return barcodeEncoder.createBitmap(bitMatrix);
    }

    private String saveBarcodeImage(String barcodeText) {
        try {
            Bitmap barcodeBitmap = generateBarcode(barcodeText);
            // Save bitmap to internal storage
            String fileName = "barcode_" + barcodeText + ".png";
            FileOutputStream fos = openFileOutput(fileName, MODE_PRIVATE);
            barcodeBitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();

            return fileName; // The name of the file saved
        } catch (WriterException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }


//    private ArrayList<String> generateBarcodesForStudents(int numberOfStudents) {
//        ArrayList<String> barcodes = new ArrayList<>();
//        for (int i = 0; i < numberOfStudents; i++) {
//            // Example: Generate simple numeric barcodes
//            // use code 128 writer and generate a barcode image bitmap format
//            barcodes.add(String.valueOf(1000 + i));
//        }
//        return barcodes;
//    }

    private ArrayList<String> getStudentNames() {
        ArrayList<String> studentNames = new ArrayList<>();
        SharedPreferences sharedPreferences = getSharedPreferences("CoursePrefs", MODE_PRIVATE);
        // Retrieve the saved list of IDs
        String savedStudentIds = sharedPreferences.getString("StudentIDs", "");
        if (!savedStudentIds.isEmpty()) {
            List<String> studentIds = Arrays.asList(savedStudentIds.split(","));
            for (String id : studentIds) {
                // For each ID, retrieve and add the corresponding name to the list
                String name = sharedPreferences.getString(id, null);
                if (name != null) {
                    studentNames.add(name);
                }
            }
        }
        return studentNames;
    }




}