package com.cp470.healthyhawk;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class Book_Facilities extends AppCompatActivity {
    // Constants
    public static final int LAUNCH_LOGIN_FACILITIES = 21;
    protected String email;
    protected String password;
    protected Date date;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_facilities);
        Intent intent = getIntent();
        email = intent.getStringExtra("email");
        password = intent.getStringExtra("password");
        Button book = findViewById(R.id.buttonSubmitBooking);
        book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CalendarView datePicker = findViewById(R.id.layoutDatePicker);
                long day  = datePicker.getDate();
                Date date = new Date(day);
                String dateValue = date.toString();
                Toast.makeText(Book_Facilities.this, dateValue, Toast.LENGTH_LONG).show();
            }
        });



    }
}