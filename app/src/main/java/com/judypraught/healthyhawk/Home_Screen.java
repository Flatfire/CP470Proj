package com.judypraught.healthyhawk;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;

public class Home_Screen extends AppCompatActivity {
    // Constants
    public static final int LAUNCH_USER_INTRODUCTION = 0;
    public static final int LAUNCH_EXERCISE_LOG      = 11;
    public static final int LAUNCH_WEIGHT_LOG        = 12;
    public static final int LAUNCH_BOOK_FACILITIES   = 13;

    // Variables
    LinearLayout layoutHomePage;
    LinearLayout layoutHomeNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        // Initialize variables
        layoutHomePage = findViewById(R.id.layoutHomePage);
        layoutHomeNavigation = findViewById(R.id.layoutHomeNavigation);


        // Check if User Data exists otherwise launch User Introduction Activity

        String preference_file_name = getString(R.string.preference_file_name);
        SharedPreferences mPrefs = getSharedPreferences(preference_file_name, MODE_PRIVATE);

        String key_is_registered = getString(R.string.preference_key_is_registered);
        boolean is_registered = mPrefs.getBoolean(key_is_registered, false);

        if (!is_registered) {
            Intent intent = new Intent(Home_Screen.this, User_Introduction.class);
            startActivityForResult(intent, LAUNCH_USER_INTRODUCTION);
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
}