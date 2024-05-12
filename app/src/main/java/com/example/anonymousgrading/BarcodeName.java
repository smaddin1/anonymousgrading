package com.example.anonymousgrading;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

public class BarcodeName extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode_name);

        LinearLayout mainLayout = findViewById(R.id.barcodeList);
        SharedPreferences sharedPreferences = getSharedPreferences("ExamPrefs", MODE_PRIVATE);
        Map<String, ?> allEntries = sharedPreferences.getAll();






        Button buttontohome = findViewById(R.id.btnHome);



        //LinearLayout mainLayout = findViewById(R.id.barcodeList); // Your parent layout in activity_barcode_name.xml
//
//        for (int i = 0; i < studentNames.size(); i++) {
//            TextView studentNameTextView = new TextView(this);
//            studentNameTextView.setLayoutParams(new LinearLayout.LayoutParams(
//                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
//            studentNameTextView.setText(studentNames.get(i));
//            studentNameTextView.setTextSize(16);
//            studentNameTextView.setTextColor(Color.WHITE); // Set text color as needed
//
//            TextView barcodeTextView = new TextView(this);
//            barcodeTextView.setLayoutParams(new LinearLayout.LayoutParams(
//                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
//            barcodeTextView.setText(barcodes.get(i));
//            barcodeTextView.setTextSize(16);
//            barcodeTextView.setTextColor(Color.WHITE); // Set text color as needed
//
//
//            // Add the TextViews to the LinearLayout
//            linearLayout.addView(studentNameTextView);
//            linearLayout.addView(barcodeTextView);
//
//            // Create a new horizontal LinearLayout for each student name and barcode pair
//            LinearLayout horizontalLayout = new LinearLayout(this);
//            horizontalLayout.setOrientation(LinearLayout.HORIZONTAL);
//            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
//                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//            horizontalLayout.setLayoutParams(layoutParams);
//
//            // Set the margin between student name and barcode TextViews
//            int marginSize = getResources().getDimensionPixelSize(R.dimen.default_margin);
//            LinearLayout.LayoutParams textViewLayoutParams = new LinearLayout.LayoutParams(
//                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//            textViewLayoutParams.setMargins(0, 0, marginSize, 0); // Adjust the right margin as needed
//
//            // Create TextView for student name
//            TextView studentNameTextView = new TextView(this);
//            studentNameTextView.setText(studentNames.get(i));
//            studentNameTextView.setLayoutParams(textViewLayoutParams);
//            studentNameTextView.setTextColor(Color.WHITE); // Set text color to white
//
//
//            // Create TextView for barcode
//            TextView barcodeTextView = new TextView(this);
//            barcodeTextView.setText(barcodes.get(i));
//            barcodeTextView.setTextColor(Color.WHITE); // Set text color to white
//            // No need to set layout params again for the barcodeTextView if you want them to be consistent
//
//            // Add the TextViews to the horizontal LinearLayout
//            horizontalLayout.addView(studentNameTextView);
//            horizontalLayout.addView(barcodeTextView);
//
//            // Add the horizontal LinearLayout to the main layout
//            mainLayout.addView(horizontalLayout);
//
//        }
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            String studentIdentifier = entry.getKey();
            String barcodeFileName = entry.getValue().toString();

            LinearLayout horizontalLayout = new LinearLayout(this);
            horizontalLayout.setOrientation(LinearLayout.HORIZONTAL);

            TextView studentNameTextView = new TextView(this);
            studentNameTextView.setText(studentIdentifier); // Assuming identifier has the name
            studentNameTextView.setTextColor(Color.WHITE);
            horizontalLayout.addView(studentNameTextView);

            ImageView barcodeImageView = new ImageView(this);
            try {
                FileInputStream fis = openFileInput(barcodeFileName);
                Bitmap barcodeBitmap = BitmapFactory.decodeStream(fis);
                barcodeImageView.setImageBitmap(barcodeBitmap);
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
                // Handle the error, such as displaying a default image or error text
            }
            horizontalLayout.addView(barcodeImageView);

            mainLayout.addView(horizontalLayout);
        }



        buttontohome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Define the action to be performed when the button is clicked
                // For example, you can start a new activity
                Intent intent = new Intent(BarcodeName.this, AllToGO.class);
                startActivity(intent);
            }
        });
    }



}