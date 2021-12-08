package com.cp470.healthyhawk;

import com.google.firebase.database.Exclude;

/**
 * Booking profile class used to construct user objects to be submitted to the
 * Firebase RTDB
 */
public class Booking_Profile {
    private String email;
    private String password;

    public Booking_Profile(){
    }

    public Booking_Profile(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
            return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


}
