package com.cp470.healthyhawk;

import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Weight_Log extends AppCompatActivity {

    // Database variables
    Weight_Database_Helper weightDBHelper;
    SQLiteDatabase db;
    Cursor cursor;

    // SharedPreferences variables
    String preference_file_name;
    SharedPreferences mPrefs;
    String key_weight_goal;
    String key_weight;
    String key_weight_unit;

    // UI Components of activity
    Button setWeightButton;
    Button setGoalWeightButton;
    GraphView weightProgressChart;
    TextView currentWeightText;
    TextView goalWeightText;

    // GraphView specific variables
    final int NUM_MAX_GRAPH_DATA_POINTS = 14;
    ArrayList<DataPoint> weightDataPoints;
    LineGraphSeries<DataPoint> weightSeries;

    /**
     * Set layout and implement UI element handlers
     * @param savedInstanceState
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weight_log);

        // Getting access to database
        weightDBHelper = new Weight_Database_Helper(this);
        db = weightDBHelper.getWritableDatabase();

        // Setting up the SharedPreferences
        preference_file_name = getString(R.string.preference_file_name);
        mPrefs = getSharedPreferences(preference_file_name, MODE_PRIVATE);
        key_weight_goal = getString(R.string.preference_key_weight_goal);
        key_weight = getString(R.string.preference_key_weight);
        key_weight_unit = getString(R.string.preference_key_weight_unit);

        // Populating the current and goal weight
        String currentWeightString = mPrefs.getString(key_weight,"0.0") + " " + mPrefs.getString(key_weight_unit, "");
        currentWeightText = findViewById(R.id.textWeightLogCurrent);
        currentWeightText.setText(currentWeightString);

        String goalWeightString = mPrefs.getString(key_weight_goal,"0.0") + " " + mPrefs.getString(key_weight_unit, "");
        goalWeightText = findViewById(R.id.textWeightLogGoal);
        goalWeightText.setText(goalWeightString);

        // Populating the graph, shows recent 14 data points
        weightProgressChart = findViewById(R.id.weightProgressChart);
        setupGraphView();

        // Initializing onClickListeners
        // Log Weight Button
        setWeightButton = findViewById(R.id.logWeightButton);
        setWeightButton.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(Weight_Log.this);
            LayoutInflater inflater = Weight_Log.this.getLayoutInflater();
            View logWeightDialog = inflater.inflate(R.layout.dialog_log_weight, null);

            builder.setView(logWeightDialog);
            builder.setPositiveButton(R.string.LogWeightDialogSave, (dialog, id) -> {
                dialog.dismiss();
                EditText userWeightEditText = logWeightDialog.findViewById(R.id.dialogUserWeightInput);
                updateCurrentWeight(userWeightEditText.getText().toString());
            });
            builder.setNegativeButton(R.string.LogWeightDialogCancel, (dialog, id) -> dialog.cancel());
            builder.show();
        });

        // Set New Goal Button
        setGoalWeightButton = findViewById(R.id.logGoalWeightButton);
        setGoalWeightButton.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(Weight_Log.this);
            LayoutInflater inflater = Weight_Log.this.getLayoutInflater();
            View goalWeightDialog = inflater.inflate(R.layout.dialog_log_goal_weight, null);

            builder.setView(goalWeightDialog);
            builder.setPositiveButton(R.string.LogWeightDialogSave, (dialog, id) -> {
                dialog.dismiss();
                EditText userGoalWeightEditText = goalWeightDialog.findViewById(R.id.dialogUserGoalWeightInput);
                updateGoalWeight(userGoalWeightEditText.getText().toString());
            });
            builder.setNegativeButton(R.string.LogWeightDialogCancel, (dialog, id) -> dialog.cancel());
            builder.show();
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cursor.close();
        db.close();
    }

    /**
     * Set graph layout and load data from local database
     * Queries the local SQLite database to retrieve existing weight data
     */
    private void setupGraphView() {
        // Set up graph title
        weightProgressChart.setTitle(getString(R.string.WeightChartTitle));

        // Graph grid set up
        GridLabelRenderer glr = weightProgressChart.getGridLabelRenderer();
        glr.setGridStyle(GridLabelRenderer.GridStyle.HORIZONTAL);
        glr.setHorizontalLabelsVisible(false);
        glr.setPadding(36);

        // Graph axis size set up, allowing for appended weight to be rendered properly
        Viewport vp = weightProgressChart.getViewport();
        vp.setXAxisBoundsManual(true);
        vp.setMinX(0);
        vp.setMaxX(NUM_MAX_GRAPH_DATA_POINTS);
        vp.setScrollable(true);

        // Create a series of data, users weight from db (if it exists)
        // Get db related variables set up
        cursor = db.rawQuery("SELECT * FROM " + Weight_Database_Helper.TABLE_NAME + ";", null);
        int columnIndex = cursor.getColumnIndex(Weight_Database_Helper.KEY_WEIGHT);
        cursor.moveToFirst();

        // Get the data point arraylist set up
        weightDataPoints = new ArrayList<>();

        // Converting every weight logged into arraylist of DataPoints
        int i = 0;
        while (!cursor.isAfterLast()) {
            weightDataPoints.add(new DataPoint(i, cursor.getFloat(columnIndex)));
            ++i;
            cursor.moveToNext();
        }
        cursor.close();

        if (weightDataPoints.isEmpty()) {
            Snackbar.make(findViewById(R.id.WeightLogLayout), R.string.WeightLogEmptyMessage, Snackbar.LENGTH_LONG)
                    .show();
        }

        // Convert the datapoint arraylist to array
        DataPoint[] weightDatapointArray = new DataPoint[weightDataPoints.size()];
        weightDatapointArray = weightDataPoints.toArray(weightDatapointArray);

        // Create series of data with array of datapoints
        weightSeries = new LineGraphSeries<>(weightDatapointArray);
        weightSeries.setColor(Color.rgb(51, 0, 114)); //purple_a
        weightSeries.setDrawDataPoints(true);

        // Add series to graph
        weightProgressChart.addSeries(weightSeries);
        weightProgressChart.getViewport().scrollToEnd(); // show most recent 14 weight logs
    }

    /**
     * Takes new weight value and adds it to the array of datapoints to be displayed by the graph
     * @param weight New weight datapoint input by user
     */
    private void updateGraphView(String weight) {
        // Take input and add it to the arraylist of datapoints
        double weightInput = Double.parseDouble(weight);
        DataPoint newWeightDataPoint = new DataPoint(weightDataPoints.size(), weightInput);
        weightDataPoints.add(newWeightDataPoint);

        // Append to series, and update graph rendering
        weightSeries.appendData(newWeightDataPoint, true, NUM_MAX_GRAPH_DATA_POINTS);
        weightProgressChart.getViewport().scrollToEnd(); // call it again to properly render
    }

    /**
     * Updates current weight value in local prefs and displays up to date weight value to user
     * @param weight New current weight as input by user
     */
    private void updateCurrentWeight(String weight) {
        // Update the TextView
        String weightString = weight + " " + mPrefs.getString(key_weight_unit, "");
        currentWeightText.setText(weightString);

        // Update the SharedPreferences
        SharedPreferences.Editor mEditor = mPrefs.edit();
        mEditor.putString(key_weight, weight);
        if (!mEditor.commit()) {
            String commit_failed = getString(R.string.commit_preferences_failed);
            Toast.makeText(this, commit_failed, Toast.LENGTH_SHORT).show();
            return;
        }

        // Add logged weight into db
        SimpleDateFormat dateWeightLoggedFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date weightDateLogged = new Date();
        ContentValues weightLoggedValues = new ContentValues();
        weightLoggedValues.put(Weight_Database_Helper.KEY_DATE_WEIGHT_LOGGED, dateWeightLoggedFormat.format(weightDateLogged));
        weightLoggedValues.put(Weight_Database_Helper.KEY_WEIGHT, Float.parseFloat(weight));
        db.insert(Weight_Database_Helper.TABLE_NAME, null, weightLoggedValues);

        // Update the GraphView with new weight
        updateGraphView(weight);
    }

    /**
     * Sets new goal weight to be displayed in graph
     * @param goalWeight new goal weight value as input by the user
     */
    private void updateGoalWeight(String goalWeight) {
        // Update the TextView
        String goalWeightString = goalWeight + " " + mPrefs.getString(key_weight_unit, "");
        goalWeightText.setText(goalWeightString);

        // Update the SharedPreferences
        SharedPreferences.Editor mEditor = mPrefs.edit();
        mEditor.putString(key_weight_goal, goalWeight);
        if (!mEditor.commit()) {
            String commit_failed = getString(R.string.commit_preferences_failed);
            Toast.makeText(this, commit_failed, Toast.LENGTH_SHORT).show();
        }
    }
}