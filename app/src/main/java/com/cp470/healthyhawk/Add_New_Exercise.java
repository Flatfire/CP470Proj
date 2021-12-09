package com.cp470.healthyhawk;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Add_New_Exercise extends AppCompatActivity {

    // Variables
    Spinner spinnerType;
    TextView textViewQuantity;
    Spinner spinnerQuantityUnit;
    Button buttonSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_exercise);

        // Initialize layout variables
        spinnerType = findViewById(R.id.spinnerType);
        textViewQuantity = findViewById(R.id.textStatNum);
        spinnerQuantityUnit = findViewById(R.id.spinnerStatName);
        buttonSave = findViewById(R.id.buttonSaveNewExercise);

        // Set Save button onClick
        buttonSave.setOnClickListener(view -> {
            // Validate data (rest have default values), better checking could certainly be done.
            if (!textViewQuantity.getText().toString().equals("")) {
                // Get current Date and Time
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("KK:mm aaa E, LLL d, yyyy");

                // Get data values
                String activityType = spinnerType.getSelectedItem().toString();
                String activityStatNum = textViewQuantity.getText().toString(); // Quantity is just passed as String
                String activityStatName = spinnerQuantityUnit.getSelectedItem().toString();
                String activityDateTime = simpleDateFormat.format(calendar.getTime());

                // Save and return data as Result using Intent
                Intent result = new Intent();
                result.putExtra("activityType", activityType);
                result.putExtra("activityStatNum", activityStatNum);
                result.putExtra("activityStatName", activityStatName);
                result.putExtra("activityDateTime", activityDateTime);
                setResult(Activity.RESULT_OK, result);
                finish();
            } else {
                Snackbar.make(findViewById(R.id.layoutAddNewExercise), getString(R.string.no_quantity_entered), Snackbar.LENGTH_LONG)
                        .setAction("No Quantity Entered", null).show();
            }
        });
    }
}