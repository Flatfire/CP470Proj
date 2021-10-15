package com.judypraught.healthyhawk;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class Home_Screen extends AppCompatActivity {
    // Variables
    LinearLayout layoutHomePage;
    LinearLayout layoutHomeNavigation;
    LinearLayout layoutUserIntroduction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);


        // Check if User Data exists and load appropriate activities

        String preference_file_name = getString(R.string.preference_file_name);
        SharedPreferences mPrefs = getSharedPreferences(preference_file_name, MODE_PRIVATE);

        String key_is_registered = getString(R.string.preference_key_is_registered);
        boolean is_registered = mPrefs.getBoolean(key_is_registered, false);

        layoutHomePage = findViewById(R.id.layoutHomePage);
        layoutHomeNavigation = findViewById(R.id.layoutHomeNavigation);
        layoutUserIntroduction = findViewById(R.id.layoutUserIntroduction);

        if (is_registered) {
            layoutHomePage.setVisibility(View.VISIBLE);
            layoutHomeNavigation.setVisibility(View.VISIBLE);
            layoutUserIntroduction.setVisibility(View.INVISIBLE);
        } else {
            layoutHomePage.setVisibility(View.INVISIBLE);
            layoutHomeNavigation.setVisibility(View.GONE);
            layoutUserIntroduction.setVisibility(View.VISIBLE);
        }


        // Home Navigation

        Button ExerciseLogButton = findViewById(R.id.ExerciseLogButton);
        ExerciseLogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home_Screen.this, Exercise_Log.class);
                startActivityForResult(intent, 10);
            }
        });

        Button WeightLogButton = findViewById(R.id.WeightLogButton);
        WeightLogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home_Screen.this, Weight_Log.class);
                startActivityForResult(intent, 10);
            }
        });

        Button BookFacilitiesButton = findViewById(R.id.BookFacilitiesButton);
        BookFacilitiesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home_Screen.this, Book_Facilities.class);
                startActivityForResult(intent, 10);
            }
        });
    }
}