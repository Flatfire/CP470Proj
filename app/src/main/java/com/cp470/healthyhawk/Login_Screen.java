package com.cp470.healthyhawk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;


public class Login_Screen extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button LoginButton = findViewById(R.id.LoginButton);
        Button Profile_Creator_Button = findViewById(R.id.RegisterButton);
        final EditText email = findViewById(R.id.editTextTextEmailAddress);
        FirebaseDatabase db =FirebaseDatabase.getInstance();

        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Query query = db.getReference().child("Booking_Profile").orderByChild("email").equalTo(email.getText().toString()).limitToFirst(1);

                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot Data: snapshot.getChildren() ){
                            Booking_Profile info = Data.getValue(Booking_Profile.class);
                            String text = info.getEmail();
                            Toast.makeText(Login_Screen.this, "user:" + text, Toast.LENGTH_LONG).show();
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                Intent intent =  new Intent(Login_Screen.this, Home_Screen.class);
                startActivityForResult(intent, 10);


            }
        });

        Profile_Creator_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login_Screen.this, Profile_Creator.class);
                startActivityForResult(intent, 10);
            }
        });


    }
}