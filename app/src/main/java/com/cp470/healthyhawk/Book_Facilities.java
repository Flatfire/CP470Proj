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
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.lang.ref.Reference;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Book_Facilities extends AppCompatActivity {
    // Constants
    public static final int LAUNCH_LOGIN_FACILITIES = 21;
    public static final String BUILDINGS = "Buildings";
    // Variable declarations
    protected static DatabaseReference db = FirebaseDatabase.getInstance().getReference();
    protected String email;
    protected CalendarView bookCalendar;
    protected Date bookDate = new Date();
    protected Button bookingSubmit;
    protected Spinner pickStartHour;
    protected Spinner pickEndHour;
    protected Spinner pickStartHalf;
    protected Spinner pickEndHalf;
    protected Spinner fromAmPm;
    protected Spinner toAmPm;
    protected Spinner buildingSpinner;
    protected Spinner facilitySpinner;
    protected ArrayList<String> buildingArr = new ArrayList<String>();
    protected ArrayAdapter<String> buildingAdapter;
    protected ArrayList<String> facilityArr = new ArrayList<String>();
    protected ArrayAdapter<String> facilityAdapter;
    protected ArrayList<String> hours = new ArrayList<>();
    protected ArrayAdapter<String> hoursAdapter;
    protected ArrayList<String> halves = new ArrayList<>();
    protected ArrayAdapter<String> halvesAdapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_facilities);
        // Element assignment
        toAmPm = findViewById(R.id.spinnerToAmPm);
        fromAmPm = findViewById(R.id.spinnerFromAmPm);
        pickStartHour = findViewById(R.id.hourFrom);
        pickEndHour = findViewById(R.id.hourTo);
        pickStartHalf = findViewById(R.id.halfFrom);
        pickEndHalf = findViewById(R.id.halfTo);
        bookCalendar = findViewById(R.id.layoutDatePicker);
        bookingSubmit = findViewById(R.id.buttonSubmitBooking);
        facilitySpinner = findViewById(R.id.spinnerFacility);
        buildingSpinner = findViewById(R.id.spinnerBuilding);
        buildingAdapter = new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_spinner_item, buildingArr);
        buildingSpinner.setAdapter(buildingAdapter);
        buildingAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        facilityAdapter = new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_spinner_item, facilityArr);
        facilitySpinner.setAdapter(facilityAdapter);
        facilityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Generate hour intervals for spinner
        for (int i = 12; i > 0; i--){
            hours.add(""+i);
        }
        halves.add("00");
        halves.add("30");
        hoursAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item,hours);
        hoursAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        halvesAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item,halves);
        halvesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        pickStartHour.setAdapter(hoursAdapter);
        pickStartHalf.setAdapter(halvesAdapter);
        pickEndHour.setAdapter(hoursAdapter);
        pickEndHalf.setAdapter(halvesAdapter);
        hoursAdapter.notifyDataSetChanged();
        halvesAdapter.notifyDataSetChanged();
        pickStartHour.setSelection(0);
        pickStartHalf.setSelection(0);
        pickEndHour.setSelection(0);
        pickEndHalf.setSelection(0);
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
                                if (Data.child("name").getValue() != null) {
                                    Object t = Data.child("name").getValue();
                                    Log.i("facility", t.toString());
                                    facilityArr.add(t.toString());
                                }
                            }
                            facilityAdapter.notifyDataSetChanged();
                            facilitySpinner.setSelection(0);
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
                String bookFacility = facilitySpinner.getSelectedItem().toString();
                String[] date = bookDate.toString().split(" ");
                // DD MM YYYY
                String bookDay = date[2] + " " + date[1] + " " + date[5];
                // Create date format to be stored in database
                String startTime = formatTime(pickStartHour.getSelectedItem().toString() , pickStartHalf.getSelectedItem().toString(), fromAmPm.toString());
                String endTime = formatTime(pickEndHour.getSelectedItem().toString() , pickEndHalf.getSelectedItem().toString(), toAmPm.toString());

                db.child(bookBuilding).child("facility").child(bookFacility).child("bookings").child(bookDay).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        boolean bookingConflict = false;
                        if (!task.isSuccessful()) {
                            Log.e("firebase", "Error getting data", task.getException());
                        } else {
//                            DataSnapshot snapshot = task.getResult();
//                            for(DataSnapshot Data: snapshot.getChildren() ) {
//
//                            }
                            if (!bookingConflict){
                                Booking newBooking = new Booking(startTime,endTime,email);
                                db.child(bookBuilding).child("facility").child(bookFacility).child("bookings").child(bookDay).push().setValue(newBooking);
                            }
                        }
                    }
                });
            }
        });
    }
    protected String formatTime(String hour, String minute, String meridiem){
        // Converts 12 hour to 24 hour time stamps
        String fTime = hour+":"+minute;
        if (meridiem.equals("PM") && Integer.parseInt(hour) < 12){
            fTime = (Integer.parseInt(hour)+12)+":"+minute;
        }
        return fTime;
    }
}