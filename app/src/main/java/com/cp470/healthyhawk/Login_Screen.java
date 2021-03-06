package com.cp470.healthyhawk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

import com.google.firebase.database.FirebaseDatabase;


public class Login_Screen extends AppCompatActivity {
    /**
     * Prep layout for login screen and set listeners for login interface
     * @param savedInstanceState Contains any extras or data passed to the activity
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);
        Button login  = findViewById(R.id.bookingLogin);
        Button register  = findViewById(R.id.bookingRegister);
        EditText confirmPass = findViewById(R.id.confirmPassword);
        final EditText email = findViewById(R.id.bookingEmail);
        final EditText password = findViewById(R.id.bookingPassword);
        Button returnLogin = findViewById(R.id.returnLogin);
        TextView infoMessage = findViewById(R.id.loginMessage);

        // Login verification
        login.setOnClickListener(view -> new getLoginData().execute());

        register.setOnClickListener( view -> {
            if (login.getVisibility() == View.VISIBLE){
                login.setVisibility(View.GONE);
                confirmPass.setVisibility(View.VISIBLE);
                returnLogin.setVisibility(View.VISIBLE);
                infoMessage.setVisibility(View.GONE);
            } else {
                // User registration block
                if (email.getText().toString().matches("") & password.getText().toString().matches("")) {
                    Toast.makeText(Login_Screen.this, "Please enter data", Toast.LENGTH_LONG).show();
                } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email.getText()).matches()) {
                    Toast.makeText(Login_Screen.this, "Invalid email address", Toast.LENGTH_LONG).show();
                } else {
                    //Clicking button calls AsyncTask retrieval task
                    register.setOnClickListener(view1 -> new getRegistrationData().execute());

                }
            }
        });
        returnLogin.setOnClickListener(view -> {
            login.setVisibility(View.VISIBLE);
            confirmPass.setVisibility(View.GONE);
            returnLogin.setVisibility(View.GONE);
        });
    }

    public class getLoginData extends AsyncTask<Void, Void, Void> {
        /**
         * Background retrieval and validation of user login data from Firebase RTDB
         * @param voids
         * @return
         */
        @Override
        protected Void doInBackground(Void... voids) {
            DatabaseReference db = FirebaseDatabase.getInstance().getReference();
            final EditText email = findViewById(R.id.bookingEmail);
            final EditText password = findViewById(R.id.bookingPassword);
            db.child("Booking_Profile").get().addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.e("firebase", "Error getting data", task.getException());
                    } else {
                        // Check if user already exists and password correct
                        boolean registered = false;
                        DataSnapshot snapshot = task.getResult();
                        for (DataSnapshot Data : snapshot.getChildren()) {
                            Booking_Profile info = Data.getValue(Booking_Profile.class);
                            String userEmail = info.getEmail();
                            String userPassword = info.getPassword();
                            if (userEmail.equals(email.getText().toString()) & userPassword.equals(password.getText().toString())) {
                                registered = true;
                                break;
                            } else {
                                registered = false;
                            }
                        }
                        // Register user if account does not exist
                        if (registered) {
                            Toast.makeText(Login_Screen.this, "Logged in", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(Login_Screen.this, Book_Facilities.class);
                            intent.putExtra("email", email.getText().toString());
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(Login_Screen.this, "Password incorrect or User nonexistent", Toast.LENGTH_LONG).show();
                        }
                    }
            });
            return null;
        }
    }

    private class getRegistrationData extends AsyncTask<Void, Void, Void>{
        /**
         * Background retrieval of user registration data for validation and sanity checks
         * @param voids
         * @return
         */
        @Override
        protected Void doInBackground(Void... voids) {
            DAOprofile dao = new DAOprofile();
            Button login  = findViewById(R.id.bookingLogin);
            EditText confirmPass = findViewById(R.id.confirmPassword);
            final EditText email = findViewById(R.id.bookingEmail);
            final EditText password = findViewById(R.id.bookingPassword);
            Button returnLogin = findViewById(R.id.returnLogin);
            TextView infoMessage = findViewById(R.id.loginMessage);
            DatabaseReference db = FirebaseDatabase.getInstance().getReference();
            db.child("Booking_Profile").get().addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.e("firebase", "Error getting data", task.getException());
                    } else {
                        // Check if user already exists
                        boolean registered = false;
                        DataSnapshot snapshot = task.getResult();
                        for (DataSnapshot Data : snapshot.getChildren()) {
                            Booking_Profile info = Data.getValue(Booking_Profile.class);
                            String text = info.getEmail();
                            if (text.equals(email.getText().toString())) {
                                registered = true;
                                break;
                            } else {
                                registered = false;
                            }
                        }
                        // Register user if account does not exist
                        if (!registered) {
                            Booking_Profile bookingProfile = new Booking_Profile(email.getText().toString(), password.getText().toString());
                            dao.add(bookingProfile);
                            login.setVisibility(View.VISIBLE);
                            confirmPass.setVisibility(View.GONE);
                            returnLogin.setVisibility(View.GONE);
                            infoMessage.setVisibility(View.VISIBLE);
                            infoMessage.setText("Registration successful. Please login below.");
                        }
                        // Inform user if they already have an account
                        else {
                            Toast.makeText(Login_Screen.this, "User already exists", Toast.LENGTH_LONG).show();
                        }
                    }
            });

            return null;
        }
    }



}