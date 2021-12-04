package com.cp470.healthyhawk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Profile_Creator extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_creator);
        Button registerButton = findViewById(R.id.RegisterButton);
        final EditText email = findViewById(R.id.editTextTextEmailAddress);
        final EditText password = findViewById(R.id.editTextTextPassword);
        DAOprofile dao = new DAOprofile();
        FirebaseDatabase db = FirebaseDatabase.getInstance();

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Query query = db.getReference().child("Booking_Profile");
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        boolean toggle = false;
                        for(DataSnapshot Data: snapshot.getChildren() ) {
                            Booking_Profile info = Data.getValue(Booking_Profile.class);
                            String text = info.getEmail();
                            if (text.equals(email.getText().toString())) {
                                toggle = true;
                                break;
                            }
                            else{
                                toggle = false;
                            }
                        }
                        if (!toggle) {
                            Booking_Profile bookingProfile = new Booking_Profile(email.getText().toString(), password.getText().toString());
                            dao.add(bookingProfile);
                            Intent intent = new Intent(Profile_Creator.this, Login_Screen.class);
                            startActivityForResult(intent, 10);
                        }
                        else {
                            Toast.makeText(Profile_Creator.this, "User already exists", Toast.LENGTH_LONG).show();
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });

    }
}