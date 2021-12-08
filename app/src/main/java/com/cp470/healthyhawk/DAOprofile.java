package com.cp470.healthyhawk;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

/**
 * Direct access object for booking profiles
 * Used to get and add booking profiles to the Firebase RTDB
 */
public class DAOprofile {

    private DatabaseReference Reference;
    public DAOprofile(){
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        Reference = db.getReference(Booking_Profile.class.getSimpleName());

    }

    public Task<Void> add(Booking_Profile bookingProfile){
        return Reference.push().setValue(bookingProfile);
    }


}
