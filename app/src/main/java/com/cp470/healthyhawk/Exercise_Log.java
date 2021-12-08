package com.cp470.healthyhawk;

import android.app.Activity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.cp470.healthyhawk.databinding.ActivityExerciseLogBinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Exercise_Log extends AppCompatActivity {
    // Constants
    public static final String ACTIVITY_NAME = "Exercise_Log";
    public static final int LAUNCH_NEW_EXERCISE = 31;

    // Variables
    ListView listView;
    ExerciseLogDatabaseHelper dbHelper;
    SQLiteDatabase db;
    Cursor cursor;
    SimpleAdapter adapter;

    ArrayList<Map<String,Object>> activityList;
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

        // Setup Fab to launch Add_New_Exercise Activity
        ActivityExerciseLogBinding binding = ActivityExerciseLogBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.fabAddExercise.setOnClickListener(view -> {
            Intent intent = new Intent(Exercise_Log.this, Add_New_Exercise.class);
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

        // Display message if no reported activities
        if (activityType.size() == 0) {
            Snackbar.make(findViewById(R.id.layoutExerciseLog), getString(R.string.no_exercise_log_history), Snackbar.LENGTH_LONG)
                    .setAction("No Exercise History", null).show();
        }

        // Fill list with maps of data from ArrayLists
        activityList = new ArrayList<>();
        for(int i=0; i < activityType.size(); i++) {
            Map<String,Object> listItemMap = new HashMap<>();
            listItemMap.put("activityType", activityType.get(i));
            listItemMap.put("activityStatNum", activityStatNum.get(i));
            listItemMap.put("activityStatName", activityStatName.get(i));
            listItemMap.put("activityDateTime", activityDateTime.get(i));
            activityList.add(listItemMap);
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
            AlertDialog alert;

            // Update TextViews
            TextView textType = inflatedView.findViewById(R.id.textDialogActivityType);
            TextView textStatNum = inflatedView.findViewById(R.id.textDialogActivityStatNum);
            TextView textStatName =  inflatedView.findViewById(R.id.textDialogActivityStatName);
            TextView textDateTime =  inflatedView.findViewById(R.id.textDialogActivityDateTime);
            textType.setText(clickedItemType);
            textStatNum.setText(clickedItemStatNum);
            textStatName.setText(clickedItemStatName);
            textDateTime.setText(clickedItemDateTime);

            // Update Button onClick
//            Button buttonDeleteActivity = inflatedView.findViewById(R.id.buttonDialogDeleteActivity);
//            buttonDeleteActivity.setOnClickListener(buttonView -> {
            builder.setNegativeButton(R.string.delete, (dialogInterface, i) -> {
                // Delete from ArrayLists
                activityType.remove(position);
                activityStatNum.remove(position);
                activityStatName.remove(position);
                activityDateTime.remove(position);
                activityList.remove(position);
                // Delete from database
                db.delete(
                    ExerciseLogDatabaseHelper.TABLE_NAME,
                    ExerciseLogDatabaseHelper.KEY_TYPE + "=\"" + clickedItemType + "\" and "
                        + ExerciseLogDatabaseHelper.KEY_STAT_NUM + "=\"" + clickedItemStatNum + "\" and "
                        + ExerciseLogDatabaseHelper.KEY_STAT_NAME + "=\"" + clickedItemStatName + "\" and "
                        + ExerciseLogDatabaseHelper.KEY_DATE_TIME + "=\"" + clickedItemDateTime + "\""
                    , null);
                // Update View
                adapter.notifyDataSetChanged();
            });

            // Build AlertDialog
            alert = builder.create();
            // Fix Button width to match parent
            alert.setOnShowListener(dialogInterface -> {
                Button button = alert.getButton(DialogInterface.BUTTON_NEGATIVE);
                ViewGroup.LayoutParams params = button.getLayoutParams();
                params.width = ViewGroup.LayoutParams.MATCH_PARENT;
                button.setLayoutParams(params);
            });
            // Show AlertDialog
            alert.show();
        });

        // Update View
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onActivityResult(int requestCode, int responseCode, Intent data) {
        super.onActivityResult(requestCode, responseCode, data);
        if(requestCode == LAUNCH_NEW_EXERCISE && responseCode == Activity.RESULT_OK) {
            // After returning from Add New Exercise
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
            db.insert(ExerciseLogDatabaseHelper.TABLE_NAME, "Not Given", cValues);

            // Store in ArrayLists
            activityType.add(newItemActivityType);
            activityStatNum.add(newItemActivityStatNum);
            activityStatName.add(newItemActivityStatName);
            activityDateTime.add(newItemActivityDateTime);

            // Add new activity to activityList maps of data from ArrayLists
            Map<String,Object> listItemMap = new HashMap<>();
            listItemMap.put("activityType", newItemActivityType);
            listItemMap.put("activityStatNum", newItemActivityStatNum);
            listItemMap.put("activityStatName", newItemActivityStatName);
            listItemMap.put("activityDateTime", newItemActivityDateTime);
            activityList.add(listItemMap);

            // Update View
            adapter.notifyDataSetChanged(); // restarts the process of getCount and getView
        }
    }

    @Override
    protected void onDestroy() {
        Log.i(ACTIVITY_NAME, "In onDestroy()");
        // Close db
        db.close();
        dbHelper.close();
        super.onDestroy();
    }
}

