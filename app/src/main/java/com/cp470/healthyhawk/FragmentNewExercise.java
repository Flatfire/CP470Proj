package com.cp470.healthyhawk;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class FragmentNewExercise extends Fragment {

    // Variables
    Exercise_Log exerciseLog;
    Spinner spinnerType;
    TextView textViewQuantity;
    Spinner spinnerQuantityUnit;
    Button buttonSave;

    // Constructor
    public FragmentNewExercise(Exercise_Log exercise_log) {
        exerciseLog = exercise_log;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle savedInstanceState) {
        // Inflate the xml file for the fragment
        View view = inflater.inflate(R.layout.activity_add_new_exercise, parent, false);

        // Initialize layout variables
        spinnerType = view.findViewById(R.id.spinnerType);
        textViewQuantity = view.findViewById(R.id.textStatNum);
        spinnerQuantityUnit = view.findViewById(R.id.spinnerStatName);
        buttonSave = view.findViewById(R.id.buttonSaveNewExercise);

        // Set Save button onClick
        buttonSave.setOnClickListener(buttonView -> {
            // Validate data (rest have default values), better checking could certainly be done.
            if (!textViewQuantity.getText().toString().equals("")) {
                // Get current Date and Time
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("KK:mm aaa E, LLL. d, yyyy");

                // Get data values
                String activityType = spinnerType.getSelectedItem().toString();
                String activityStatNum = textViewQuantity.getText().toString(); // Quantity is just passed as String
                String activityStatName = spinnerQuantityUnit.getSelectedItem().toString();
                String activityDateTime = simpleDateFormat.format(calendar.getTime());

                // Add Exercise to log
                exerciseLog.addExercise(activityType, activityStatNum, activityStatName, activityDateTime);
                exerciseLog.reinitFragment();
            } else {
                Snackbar.make(getActivity().findViewById(R.id.layoutAddNewExercise), getString(R.string.no_quantity_entered), Snackbar.LENGTH_LONG)
                        .setAction("No Quantity Entered", null).show();
            }
        });
        return view;
    }
}
