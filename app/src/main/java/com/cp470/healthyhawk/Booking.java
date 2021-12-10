package com.cp470.healthyhawk;

/**
 * Booking class to hold and store booking data
 * Used to push new booking data to firebase
 */
public class Booking {
    String startTime;
    String endTime;
    String email;

    public Booking(){}
    public Booking(String startTime, String endTime, String email){
        this.email = email;
        this.startTime = startTime;
        this.endTime = endTime;
    }
    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String email) {
        this.endTime = endTime;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
