package com.cp470.healthyhawk;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Home_Screen extends AppCompatActivity {
    // Constants
    public static final int LAUNCH_USER_INTRODUCTION = 0;
    public static final int LAUNCH_EXERCISE_LOG      = 11;
    public static final int LAUNCH_WEIGHT_LOG        = 12;
    public static final int LAUNCH_BOOK_FACILITIES   = 13;

    // Variables
    LinearLayout layoutHomePage;
    LinearLayout layoutHomeNavigation;
    TextView textAge;
    TextView textGender;
    TextView textHeight;
    TextView textNickname;
    TextView textWeight;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        // Initialize variables
        layoutHomePage = findViewById(R.id.layoutHomePage);
        layoutHomeNavigation = findViewById(R.id.layoutHomeNavigation);
        textAge = findViewById(R.id.textHomeAge);
        textGender = findViewById(R.id.textHomeGender);
        textHeight = findViewById(R.id.textHomeHeight);
        textNickname = findViewById(R.id.textHomeNickname);
        textWeight = findViewById(R.id.textHomeWeight);

        // Check if User Data exists otherwise launch User Introduction Activity

        String preference_file_name = getString(R.string.preference_file_name);
        SharedPreferences mPrefs = getSharedPreferences(preference_file_name, MODE_PRIVATE);

        String key_is_registered = getString(R.string.preference_key_is_registered);
        // Get registration condition. Return false if not found.
        boolean is_registered = mPrefs.getBoolean(key_is_registered, false);

        // Either launch the User Introduction or load the data for Home Page
        if (!is_registered) {
            Intent intent = new Intent(Home_Screen.this, User_Introduction.class);
            startActivityForResult(intent, LAUNCH_USER_INTRODUCTION);
        } else {
            populateHomeScreen();
        }

        // Setup Home Screen Navigation

        Button ExerciseLogButton = findViewById(R.id.ExerciseLogButton);
        ExerciseLogButton.setOnClickListener(view -> {
            Intent intent = new Intent(Home_Screen.this, Exercise_Log.class);
            startActivityForResult(intent, LAUNCH_EXERCISE_LOG);
        });

        Button WeightLogButton = findViewById(R.id.WeightLogButton);
        WeightLogButton.setOnClickListener(view -> {
            Intent intent = new Intent(Home_Screen.this, Weight_Log.class);
            startActivityForResult(intent, LAUNCH_WEIGHT_LOG);
        });

        Button BookFacilitiesButton = findViewById(R.id.BookFacilitiesButton);
        BookFacilitiesButton.setOnClickListener(view -> {
            Intent intent = new Intent(Home_Screen.this, Book_Facilities.class);
            startActivityForResult(intent, LAUNCH_BOOK_FACILITIES);
        });
    }
    // Check for data returned by activity results
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Check for appropriate response from user introduction activity
        if (requestCode == LAUNCH_USER_INTRODUCTION) {
            if (resultCode == Activity.RESULT_OK) {
                populateHomeScreen();
            }
        }
    }

    private void populateHomeScreen() {
        String preference_file_name = getString(R.string.preference_file_name);
        SharedPreferences mPrefs = getSharedPreferences(preference_file_name, MODE_PRIVATE);

        String key_nickname = getString(R.string.preference_key_nickname);
        String welcome_string = "Welcome back, " + mPrefs.getString(key_nickname,"");
        textNickname.setText(welcome_string);

        String key_age = getString(R.string.preference_key_age);
        textAge.setText("Age: " + mPrefs.getString(key_age, "0"));

        // Show gender if not "Prefer not to say"
        String key_gender = getString(R.string.preference_key_gender);
        if (mPrefs.getString(key_gender, "Prefer not to say").compareTo("Prefer not to say")
                != 0) {
            textGender.setText("Gender: " + mPrefs.getString(key_gender, ""));
        }

        String key_height = getString(R.string.preference_key_height);
        String key_height_unit = getString(R.string.preference_key_height_unit);
        String height = "Height: "
                + mPrefs.getString(key_height, "0")
                + " "
                + mPrefs.getString(key_height_unit, "");
        textHeight.setText(height);

        String key_weight = getString(R.string.preference_key_weight);
        String key_weight_unit = getString(R.string.preference_key_weight_unit);
        String weight = "Weight: "
                + mPrefs.getString(key_weight, "0")
                + " "
                + mPrefs.getString(key_weight_unit, "");
        textWeight.setText(weight);
    }
}