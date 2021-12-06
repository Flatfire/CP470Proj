package com.cp470.healthyhawk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Book_Facilities extends AppCompatActivity {
    // Constants
    public static final int LAUNCH_LOGIN_FACILITIES = 21;
    public static final String BUILDINGS = "Buildings";
    // Variable declarations
    protected static DatabaseReference db = FirebaseDatabase.getInstance().getReference();
    protected static DAOprofile dao = new DAOprofile();
    protected String email;
    protected CalendarView bookCalendar;
    protected Date bookDate = new Date();
    protected Button bookingSubmit;
    protected Spinner buildingSpinner;
    protected Spinner facility;
    protected ArrayList<String> buildingArr = new ArrayList<String>();
    protected ArrayAdapter<String> buildingAdapter;
    protected ArrayList<String> facilityArr = new ArrayList<String>();
    protected ArrayAdapter<String> facilityAdapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_facilities);
        // Element assignment
        bookCalendar = findViewById(R.id.layoutDatePicker);
        bookingSubmit = findViewById(R.id.buttonSubmitBooking);
        facility = findViewById(R.id.spinnerFacility);
        buildingSpinner = findViewById(R.id.spinnerBuilding);
        buildingAdapter = new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_spinner_item, buildingArr);
        buildingSpinner.setAdapter(buildingAdapter);
        buildingAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        facilityAdapter = new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_spinner_item, facilityArr);
        facility.setAdapter(facilityAdapter);
        facilityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Grab user data from intent
        Intent intent = getIntent();
        email = intent.getStringExtra("email");

        // Set building spinner data
        db.child(BUILDINGS).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                } else {
                    Log.i("SPINNER", "Data for spinner retrieved");
                    DataSnapshot snapshot = task.getResult();
                    for(DataSnapshot Data: snapshot.getChildren() ) {
                        Object t = Data.child("name").getValue();
                        buildingArr.add(t.toString());
                    }
                    buildingAdapter.notifyDataSetChanged();
                    buildingSpinner.setSelection(0);
                }
            }
        });
        // Trigger for gathering facility spinner data
        buildingSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("ITEMSEL", buildingSpinner.getSelectedItem().toString() + " selected");
                facilityArr.clear();
                db.child(buildingSpinner.getSelectedItem().toString()).child("facility").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (!task.isSuccessful()) {
                            Log.e("firebase", "Error getting data", task.getException());
                        } else {
                            Log.i("SPINNER", "Data for facility spinner retrieved");
                            DataSnapshot snapshot = task.getResult();
                            Log.i("Num fac", ""+snapshot.getChildrenCount());
                            for(DataSnapshot Data: snapshot.getChildren() ) {
                                Object t = Data.child("name").getValue();
                                Log.i("facility",t.toString());
                                facilityArr.add(t.toString());
                            }
                            facilityAdapter.notifyDataSetChanged();
                            facility.setSelection(0);
                        }
                    }
                });
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });


        // Monitor calendar date selection
        bookCalendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
                Calendar c = Calendar.getInstance();
                c.set(i,i1,i2);
                bookDate = c.getTime();
            }
        });
        bookingSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String bookBuilding = buildingSpinner.getSelectedItem().toString();
                String bookFacility = buildingSpinner.getSelectedItem().toString();
                String[] date = bookDate.toString().split(" ");
                // DD MM YYYY
                String bookDay = date[2] + " " + date[1] + " " + date[5];
                Toast.makeText(getApplicationContext(),bookDay,Toast.LENGTH_SHORT).show();
                db.child(bookBuilding).child("facility").child(bookFacility).child("bookings").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (!task.isSuccessful()) {
                            Log.e("firebase", "Error getting data", task.getException());
                        } else {
                            DataSnapshot snapshot = task.getResult();
                            for(DataSnapshot Data: snapshot.getChildren() ) {

                            }
                        }
                    }
                });
            }
        });
    }
}