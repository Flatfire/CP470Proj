package com.cp470.healthyhawk;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Main_Activity extends AppCompatActivity {
    // Constants
    public static final int LAUNCH_USER_INTRODUCTION = 0;

    // Variables: Layout Objects
    Button buttonStartIntro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        // Initialize variables
        buttonStartIntro = findViewById(R.id.buttonStartIntro);

        // Set onClickListener
        buttonStartIntro.setOnClickListener(view -> {
            Intent intent = new Intent(Main_Activity.this, User_Introduction.class);
            startActivityForResult(intent, LAUNCH_USER_INTRODUCTION);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        // If User Data exists launch Home Screen Activity

        String preference_file_name = getString(R.string.preference_file_name);
        SharedPreferences mPrefs = getSharedPreferences(preference_file_name, MODE_PRIVATE);

        String key_is_registered = getString(R.string.preference_key_is_registered);
        // Get registration condition. Return false if not found.
        boolean is_registered = mPrefs.getBoolean(key_is_registered, false);

        // Launch the Home Page if already registered
        if (is_registered) {
            Intent intent = new Intent(Main_Activity.this, Home_Screen.class);
            startActivity(intent);
            finish();
        }
    }

    // Check for data returned by activity results
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Check for appropriate response from user introduction activity
        if (requestCode == LAUNCH_USER_INTRODUCTION) {
            if (resultCode == Activity.RESULT_OK) {
                Intent intent = new Intent(Main_Activity.this, Home_Screen.class);
                startActivity(intent);
            }
        }
    }
}