package com.cp470.healthyhawk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

/**
 * Profile creator class used to generate new booking profiles based on user input and
 * existing data from Firebase RTDB
 */
public class Profile_Creator extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);
        Button registerButton = findViewById(R.id.bookingRegister);
        final EditText email = findViewById(R.id.bookingEmail);
        final EditText password = findViewById(R.id.confirmPassword);
        DAOprofile dao = new DAOprofile();
        DatabaseReference db = FirebaseDatabase.getInstance().getReference();
        // Sets actions for the registration button when activated
        registerButton.setOnClickListener(view -> {
            // Retrieve booking profiles
            db.child("Booking_Profile").get().addOnCompleteListener(task -> {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    // Check if user already exists
                    boolean registered = false;
                    DataSnapshot snapshot = task.getResult();
                    for(DataSnapshot Data: snapshot.getChildren() ) {
                        Booking_Profile info = Data.getValue(Booking_Profile.class);
                        String text = info.getEmail();
                        if (text.equals(email.getText().toString())) {
                            registered = true;
                            break;
                        }
                        else{
                            registered = false;
                        }
                    }
                    // Register user if account does not exist
                    if (!registered) {
                        Booking_Profile bookingProfile = new Booking_Profile(email.getText().toString(), password.getText().toString());
                        dao.add(bookingProfile);
                        Intent intent = new Intent(Profile_Creator.this, Login_Screen.class);
                        startActivityForResult(intent, 10);
                    }
                    // Inform user if they already have an account
                    else {
                        Toast.makeText(Profile_Creator.this, "User already exists", Toast.LENGTH_LONG).show();
                    }
                }
            });
        });

    }
}