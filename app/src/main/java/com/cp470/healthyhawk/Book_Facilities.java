package com.cp470.healthyhawk;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Book_Facilities extends AppCompatActivity {
    // Constants
    public static final int LAUNCH_LOGIN_FACILITIES = 21;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_facilities);
        Button LoginToBookButton = findViewById(R.id.LoginToBook);
        LoginToBookButton.setOnClickListener(view -> {
            Intent intent =  new Intent(Book_Facilities.this, Login_Screen.class);
            startActivityForResult(intent, LAUNCH_LOGIN_FACILITIES);
        });
    }
}