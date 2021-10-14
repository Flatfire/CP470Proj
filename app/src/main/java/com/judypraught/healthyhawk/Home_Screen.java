package com.judypraught.healthyhawk;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Home_Screen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
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