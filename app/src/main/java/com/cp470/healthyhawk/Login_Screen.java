package com.cp470.healthyhawk;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

public class Login_Screen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);
        TextView loginMessage = findViewById(R.id.loginMessage);
        EditText email = findViewById(R.id.bookingEmail);
        EditText password = findViewById(R.id.bookingPassword);
        EditText confirmPass = findViewById(R.id.confirmPassword);
        Button register = findViewById(R.id.bookingRegister);
        Button login = findViewById(R.id.bookingLogin);
        Button returnLogin = findViewById(R.id.returnLogin);

        register.setOnClickListener( view -> {
            if (login.getVisibility() == View.VISIBLE){
                login.setVisibility(View.GONE);
                confirmPass.setVisibility(View.VISIBLE);
                returnLogin.setVisibility(View.VISIBLE);
            }
            else{
                //TODO check if account is already registered
                if (true) {
                    //TODO perform registration task
                    login.setVisibility(View.VISIBLE);
                    confirmPass.setVisibility(View.GONE);
                    loginMessage.setVisibility(View.VISIBLE);
                    returnLogin.setVisibility(View.GONE);
                    loginMessage.setText("Registration Successful.\nPlease login.");
                    loginMessage.setTextColor(this.getColor(R.color.DarkGreen));
                }
            }
        });
        login.setOnClickListener(view -> {
            //TODO database check against email/password
            Intent bookingIntent = new Intent(Login_Screen.this, Book_Facilities.class);
            bookingIntent.putExtra("email", email.getText().toString());
            bookingIntent.putExtra("password", password.getText().toString());
            startActivity(bookingIntent);
        });
        returnLogin.setOnClickListener(view -> {
            login.setVisibility(View.VISIBLE);
            confirmPass.setVisibility(View.GONE);
            loginMessage.setVisibility(View.VISIBLE);
            returnLogin.setVisibility(View.GONE);
        });
    }
}