package com.judypraught.healthyhawk;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

public class User_Introduction extends AppCompatActivity {
    // Constants
    public static final int MIN_AGE = 13;
    public static final int MAX_AGE = 120;

    // Variables: Layout Objects
    LinearLayout layoutUserIntroduction;
    EditText textNickname;
    EditText textAge;
    Spinner spinnerGender;
    EditText textHeight;
    EditText textWeight;
    Spinner spinnerHeightUnit;
    Spinner spinnerWeightUnit;
    Button buttonSave;

    // Variables: Values
    String nickname;
    Integer age;
    String gender;
    Double height;
    Double weight;
    String heightUnit;
    String weightUnit;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_introduction);

        // Initialize variables
        layoutUserIntroduction = findViewById(R.id.layoutUserIntroduction);
        textNickname = findViewById(R.id.textNickname);
        textAge = findViewById(R.id.textAge);
        spinnerGender = findViewById(R.id.spinnerGender);
        textHeight = findViewById(R.id.textHeight);
        textWeight = findViewById(R.id.textWeight);
        spinnerHeightUnit = findViewById(R.id.spinnerHeightUnit);
        spinnerWeightUnit = findViewById(R.id.spinnerWeightUnit);
        buttonSave = findViewById(R.id.buttonSave);

        // Initialize Spinner dropdown options
        ArrayAdapter<CharSequence> adapterGender = ArrayAdapter.createFromResource(this, R.array.Genders, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> adapterHeightUnit = ArrayAdapter.createFromResource(this, R.array.HeightUnits, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> adapterWeightUnit = ArrayAdapter.createFromResource(this, R.array.WeightUnits, android.R.layout.simple_spinner_item);
        adapterGender.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterHeightUnit.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterWeightUnit.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGender.setAdapter(adapterGender);
        spinnerHeightUnit.setAdapter(adapterHeightUnit);
        spinnerWeightUnit.setAdapter(adapterWeightUnit);

        // Update dropdown selections using listeners
        spinnerGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                gender = parent.getSelectedItem().toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                gender = null;
            }
        });
        spinnerHeightUnit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                heightUnit = parent.getSelectedItem().toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                heightUnit = null;
            }
        });
        spinnerWeightUnit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                weightUnit = parent.getSelectedItem().toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                weightUnit = null;
            }
        });


        // Handle retrieving, validating, storing, and returning values
        buttonSave.setOnClickListener(view -> {
            // Retrieve
            nickname = textNickname.getText().toString();
            age = Integer.parseInt(textAge.getText().toString());
            height = Double.parseDouble(textHeight.getText().toString());
            weight = Double.parseDouble(textWeight.getText().toString());
            // gender, heightUnit, weightUnit - all updated by listeners

            // Validate (a better method for validation might be nice!)
            String message;
            if (age == null || age < MIN_AGE) {
                message = "You must be at least " + MIN_AGE + " to use this app.";
            } else if (age > MAX_AGE) {
                message = "You must be " + MAX_AGE + " or under to use this app.";
            } else if (gender == null) {
                message = "Please select a gender (or prefer not to say).";
            } else if (heightUnit == null) {
                message = "Please select a unit for height.";
            } else if (height < 0) {
                message = "Height cannot be negative.";
            } else if (weightUnit == null) {
                message = "Please select a unit for height.";
            } else if (weight < 0) {
                message = "Weight cannot be negative.";
            } else
                message = null;

            if (message != null) {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                return;
            }

            // Store
            String preference_file_name = getString(R.string.preference_file_name);
            SharedPreferences mPrefs = getSharedPreferences(preference_file_name, MODE_PRIVATE);
            SharedPreferences.Editor mEditor = mPrefs.edit();

            String key_nickname = getString(R.string.preference_key_nickname);
            String key_age = getString(R.string.preference_key_age);
            String key_gender = getString(R.string.preference_key_gender);
            String key_height = getString(R.string.preference_key_height);
            String key_weight = getString(R.string.preference_key_weight);
            String key_height_unit = getString(R.string.preference_key_height_unit);
            String key_weight_unit = getString(R.string.preference_key_weight_unit);
            String key_is_registered = getString(R.string.preference_key_is_registered);

            mEditor.putString(key_nickname, nickname);
            mEditor.putString(key_age, String.valueOf(age));
            mEditor.putString(key_gender, gender);
            mEditor.putString(key_height, String.valueOf(height));
            mEditor.putString(key_weight, String.valueOf(weight));
            mEditor.putString(key_height_unit, heightUnit);
            mEditor.putString(key_weight_unit, weightUnit);
            mEditor.putBoolean(key_is_registered, true);

            if (mEditor.commit() == false) {
                String commit_failed = getString(R.string.commit_preferences_failed);
                Toast.makeText(this, commit_failed, Toast.LENGTH_SHORT).show();
                return;
            }

            // Return
            Intent result = new Intent();
            String response_message = getString(R.string.successful_registration);
            result.putExtra("Response", response_message);
            setResult(Activity.RESULT_OK, result);
            finish();
        });
    }
}