package com.cp470.healthyhawk;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Home_Screen extends AppCompatActivity {
    // Constants
    public static final int LAUNCH_USER_INTRODUCTION = 0;
    public static final int LAUNCH_EXERCISE_LOG      = 11;
    public static final int LAUNCH_WEIGHT_LOG        = 12;
    public static final int LAUNCH_LOGIN             = 14;
    public static final int LAUNCH_BOOK_FACILITIES   = 13;

    // Variables
    ConstraintLayout layoutHomePage;
    LinearLayout layoutHomeNavigation;
    TextView textAge;
    TextView textGender;
    TextView textHeight;
    TextView textNickname;
    TextView textWeight;
    Toolbar toolbarMenuHome;

    /**
     * Build layout and initialize elements of home screen
     * @param savedInstanceState
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        // Initialize variables
        layoutHomePage = findViewById(R.id.layoutHomePage);
        layoutHomeNavigation = findViewById(R.id.layoutHomeNavigation);
        textAge = findViewById(R.id.textHomeAge);
        textGender = findViewById(R.id.textHomeGender);
        textHeight = findViewById(R.id.textHomeHeight);
        textNickname = findViewById(R.id.textHomeNickname);
        textWeight = findViewById(R.id.textHomeWeight);
        toolbarMenuHome = findViewById(R.id.toolbarMenuHome);

        // Check if User Data exists otherwise launch User Introduction Activity

        String preference_file_name = getString(R.string.preference_file_name);
        SharedPreferences mPrefs = getSharedPreferences(preference_file_name, MODE_PRIVATE);

        String key_is_registered = getString(R.string.preference_key_is_registered);
        // Get registration condition. Return false if not found.
        boolean is_registered = mPrefs.getBoolean(key_is_registered, false);

        // Either launch the User Introduction or load the data for Home Page
        if (!is_registered) {
            Intent intent = new Intent(Home_Screen.this, User_Introduction.class);
            startActivityForResult(intent, LAUNCH_USER_INTRODUCTION);
        } else {
            new homeData().execute();
        }

        // Setup Home Screen Navigation

        Button ExerciseLogButton = findViewById(R.id.ExerciseLogButton);
        ExerciseLogButton.setOnClickListener(view -> {
            Intent intent = new Intent(Home_Screen.this, Exercise_Log.class);
            startActivityForResult(intent, LAUNCH_EXERCISE_LOG);
        });

        Button WeightLogButton = findViewById(R.id.WeightLogButton);
        WeightLogButton.setOnClickListener(view -> {
            Intent intent = new Intent(Home_Screen.this, Weight_Log.class);
            startActivityForResult(intent, LAUNCH_WEIGHT_LOG);
        });

        Button BookFacilitiesButton = findViewById(R.id.BookFacilitiesButton);
        BookFacilitiesButton.setOnClickListener(view -> {
            Intent intent = new Intent(Home_Screen.this, Login_Screen.class);
            startActivityForResult(intent, LAUNCH_LOGIN);
        });

        // Set Toolbar Menu as Action Bar

        setSupportActionBar(toolbarMenuHome);
    }

    /**
     * // Check for data returned by activity results
     * @param requestCode code provided to returning activity
     * @param resultCode code returned by returning activity
     * @param data  data returned by returning activity
     */
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Check for appropriate response from user introduction activity
        if (requestCode == LAUNCH_USER_INTRODUCTION) {
            if (resultCode == Activity.RESULT_OK) {
                new homeData().execute();
            }
        }
    }

    /**
     * Produce a dialogue when android navigation back button is pressed that prompts the user to exit
     */
    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setMessage("Do you want to Exit?");
        builder.setPositiveButton("Yes", (dialog, which) -> {
            //if user pressed "yes", then he is allowed to exit from application
            finish();
        });
        builder.setNegativeButton("No", (dialog, which) -> {
            //if user select "No", just cancel this dialog and continue with app
            dialog.cancel();
        });
        AlertDialog alert=builder.create();
        alert.show();
    }

    /**
     * Inflate menu view from stored menu layout
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // inflate the home menu featuring Help button in Action Bar
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    /**
     * Triggers menu action based on user selection
     * @param item Selected menu item from home screen
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        // call the appropriate button for each item, else call super and do nothing extra
        switch (item.getItemId()) {
            case (R.id.action_help):
                // inflate Help layout to an AlertDialog
                LayoutInflater inflater = getLayoutInflater();
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setView(inflater.inflate(R.layout.dialog_app_help, null));
                AlertDialog alert = builder.create();
                alert.show();
                return true;
            case (R.id.action_exercise_log):
                intent = new Intent(Home_Screen.this, Exercise_Log.class);
                startActivityForResult(intent, LAUNCH_EXERCISE_LOG);
                return true;
            case (R.id.action_weight_log):
                intent = new Intent(Home_Screen.this, Weight_Log.class);
                startActivityForResult(intent, LAUNCH_WEIGHT_LOG);
                return true;
            case (R.id.action_book_facilities):
                intent = new Intent(Home_Screen.this, Login_Screen.class);
                startActivityForResult(intent, LAUNCH_LOGIN);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Get User data to populate homescreen
     */
    public class homeData extends AsyncTask<Void,Void,Void> {
        /**
         * Populates home screen with user data in background
         *
         * @param voids
         * @return
         */
        @Override
        protected Void doInBackground(Void... voids) {
            String preference_file_name = getString(R.string.preference_file_name);
            SharedPreferences mPrefs = getSharedPreferences(preference_file_name, MODE_PRIVATE);

            String key_nickname = getString(R.string.preference_key_nickname);
            String welcome_string = "Hi, " + mPrefs.getString(key_nickname, "");
            textNickname.setText(welcome_string);

            String key_age = getString(R.string.preference_key_age);
            String age =
                    mPrefs.getString(key_age, "0")
                            + " "
                            + "years";
            textAge.setText(age);

            // Show gender if not "Prefer not to say"
            String key_gender = getString(R.string.preference_key_gender);
            if (mPrefs.getString(key_gender, "Prefer not to say").compareTo("Prefer not to say")
                    != 0) {
                textGender.setText("Gender: " + mPrefs.getString(key_gender, ""));
            }

            String key_height = getString(R.string.preference_key_height);
            String key_height_unit = getString(R.string.preference_key_height_unit);
            String height =
                    mPrefs.getString(key_height, "0")
                            + " "
                            + mPrefs.getString(key_height_unit, "");
            textHeight.setText(height);

            String key_weight = getString(R.string.preference_key_weight);
            String key_weight_unit = getString(R.string.preference_key_weight_unit);
            String weight =
                    mPrefs.getString(key_weight, "0")
                            + " "
                            + mPrefs.getString(key_weight_unit, "");
            textWeight.setText(weight);
            return null;
        }
    }
}