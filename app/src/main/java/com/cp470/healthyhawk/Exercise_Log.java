package com.cp470.healthyhawk;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.cp470.healthyhawk.databinding.ActivityExerciseLogBinding;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Exercise_Log extends AppCompatActivity {
    // Constants
    public static final String ACTIVITY_NAME = "Exercise_Log";
    public static final int LAUNCH_NEW_EXERCISE = 31;

    // Variables
    private ActivityExerciseLogBinding binding;
    ListView listView;
    ExerciseLogDatabaseHelper dbHelper;
    SQLiteDatabase db;
    Cursor cursor;
    SimpleAdapter adapter;

    ArrayList<String> activityType;
    ArrayList<String> activityStatNum;
    ArrayList<String> activityStatName;
    ArrayList<String> activityDateTime;
    int indexType;
    int indexStatNum;
    int indexStatName;
    int indexDateTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Setup fab
        binding = ActivityExerciseLogBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.fabAddExercise.setOnClickListener(view -> {
            Intent intent = new Intent(Exercise_Log.this, Exercise_Log.class);
            startActivityForResult(intent, LAUNCH_NEW_EXERCISE);
        });


        // Init ArrayLists for activity data points
        activityType = new ArrayList<>();
        activityStatNum = new ArrayList<>();
        activityStatName = new ArrayList<>();
        activityDateTime = new ArrayList<>();

        // Setup log database
        dbHelper = new ExerciseLogDatabaseHelper(this);
        db = dbHelper.getWritableDatabase();

        cursor = db.rawQuery(ExerciseLogDatabaseHelper.DATABASE_SELECT_ALL, null);
        indexType = cursor.getColumnIndex(ExerciseLogDatabaseHelper.KEY_TYPE);
        indexStatNum = cursor.getColumnIndex(ExerciseLogDatabaseHelper.KEY_STAT_NUM);
        indexStatName = cursor.getColumnIndex(ExerciseLogDatabaseHelper.KEY_STAT_NAME);
        indexDateTime = cursor.getColumnIndex(ExerciseLogDatabaseHelper.KEY_DATE_TIME);
        cursor.moveToFirst(); // position cursor to start - apparently very important :eyes:

        // Fill arrays with data from database
        while (!cursor.isAfterLast()) {
            activityType.add(cursor.getString(indexType));
            activityStatNum.add(cursor.getString(indexStatNum));
            activityStatName.add(cursor.getString(indexStatName));
            activityDateTime.add(cursor.getString(indexDateTime));
            cursor.moveToNext();
        }

        // Print additional db info
        int colCount = cursor.getColumnCount();
        Log.i(ACTIVITY_NAME, "Cursor's column count = " + colCount);
        for (int i=0; i<colCount; i++) {
            Log.i(ACTIVITY_NAME, "Column Name: " + cursor.getColumnName(i));
        }
        cursor.close();

        // Setup ListView using SimpleAdapter

        // Help used from following link to extend logic from simple 1d String List to HashMap:
        // https://www.dev2qa.com/android-listview-example/

        listView = findViewById(R.id.listviewExerciseLog);

        ArrayList<Map<String,Object>> activityList = new ArrayList<>();

        // Fill map with data from ArrayLists
        int len = activityType.size();
        for(int i=0; i < len; i++) {
            Map<String,Object> listItemMap = new HashMap<>();
            listItemMap.put("activityType", activityType.get(i));
            listItemMap.put("activityStatNum", activityStatNum.get(i));
            listItemMap.put("activityStatName", activityStatName.get(i));
            listItemMap.put("activityDateTime", activityDateTime.get(i));
            activityList.add(listItemMap);
        }
        // Display message if no reported activities
        if (len == 0) {
            Snackbar.make(findViewById(R.id.layoutExerciseLog), getString(R.string.no_exercise_log_history), Snackbar.LENGTH_LONG)
                    .setAction("Display No Exercise Log History", null).show();
        }

        // Use SimpleAdapter to manage items
        adapter = new SimpleAdapter(
                this, activityList, R.layout.layout_single_activity,
                new String[]{"activityType","activityStatNum","activityStatName"},
                new int[]{R.id.textActivityType, R.id.textActivityStatNum, R.id.textActivityStatName});
        listView.setAdapter(adapter);

        // Display popup with more info when item is pressed
        listView.setOnItemClickListener((adapterView, view, position, id) -> {
            HashMap clickedItemMap = (HashMap)adapterView.getAdapter().getItem(position);

            String clickedItemType = (String)clickedItemMap.get("activityType");
            String clickedItemStatNum = (String)clickedItemMap.get("activityStatNum");
            String clickedItemStatName = (String)clickedItemMap.get("activityStatName");
            String clickedItemDateTime = (String)clickedItemMap.get("activityDateTime");

            // Inflate Activity Info to an AlertDialog
            LayoutInflater inflater = getLayoutInflater();
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            View inflatedView = inflater.inflate(R.layout.dialog_exercise_log_info, null);
            builder.setView(inflatedView);

            // Update TextViews
            TextView textType = inflatedView.findViewById(R.id.textDialogActivityType);
            TextView textStatNum = inflatedView.findViewById(R.id.textDialogActivityStatNum);
            TextView textStatName =  inflatedView.findViewById(R.id.textDialogActivityStatName);
            TextView textDateTime =  inflatedView.findViewById(R.id.textDialogActivityDateTime);
            textType.setText(clickedItemType);
            textStatNum.setText(clickedItemStatNum);
            textStatName.setText(clickedItemStatName);
            textDateTime.setText(clickedItemDateTime);

            // Show Dialog
            AlertDialog alert = builder.create();
            alert.show();
            Log.i("KEY", "Your selected item is " + clickedItemType + ", " + clickedItemStatNum + " " + clickedItemStatName);
        });
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onActivityResult(int requestCode, int responseCode, Intent data) {
        super.onActivityResult(requestCode, responseCode, data);
        if(requestCode == LAUNCH_NEW_EXERCISE && responseCode == Activity.RESULT_OK) {
            Log.i(ACTIVITY_NAME, "Returned to Exercise_Log.onActivityResult");

            // Get new exercise data
            String newItemActivityType = data.getStringExtra("activityType");
            String newItemActivityStatNum = data.getStringExtra("activityStatNum");
            String newItemActivityStatName = data.getStringExtra("activityStatName");
            String newItemActivityDateTime = data.getStringExtra("activityDateTime");

            // Store in database
            ContentValues cValues = new ContentValues();
            cValues.put(ExerciseLogDatabaseHelper.KEY_TYPE, newItemActivityType);
            cValues.put(ExerciseLogDatabaseHelper.KEY_STAT_NUM, newItemActivityStatNum);
            cValues.put(ExerciseLogDatabaseHelper.KEY_STAT_NAME, newItemActivityStatName);
            cValues.put(ExerciseLogDatabaseHelper.KEY_DATE_TIME, newItemActivityDateTime);
            long insertId = db.insert(ExerciseLogDatabaseHelper.TABLE_NAME, "Not Given", cValues);

            // Store in ArrayLists
            activityType.add(cursor.getString(indexType));
            activityStatNum.add(cursor.getString(indexStatNum));
            activityStatName.add(cursor.getString(indexStatName));
            activityDateTime.add(cursor.getString(indexDateTime));

            // Update View
            adapter.notifyDataSetChanged(); // restarts the process of getCount and getView
        }
    }

    @Override
    protected void onDestroy() {
        // Close db
        Log.i(ACTIVITY_NAME, "In onDestroy()");
        db.close();
        dbHelper.close();
        super.onDestroy();
    }
}