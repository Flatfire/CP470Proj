package com.cp470.healthyhawk;

import android.content.ContentValues;
import android.content.DialogInterface;
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

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.SimpleDateFormat;
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
    final int NUM_MAX_GRAPH_DATA_POINTS = 30;
    DataPoint[] weightDataPoints;
    DataPoint[] goalDataPoints;
    LineGraphSeries<DataPoint> weightSeries;
    LineGraphSeries<DataPoint> goalWeightSeries;

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

        // Populating the graph, default shows recent seven data points
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
            builder.setPositiveButton(R.string.LogWeightDialogSave, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.dismiss();
                    EditText userWeightEditText = logWeightDialog.findViewById(R.id.dialogUserWeightInput);
                    updateCurrentWeight(userWeightEditText.getText().toString());
                }
            });
            builder.setNegativeButton(R.string.LogWeightDialogCancel, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                }
            });
            builder.show();
        });

        // Set New Goal Button
        setGoalWeightButton = findViewById(R.id.logGoalWeightButton);
        setGoalWeightButton.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(Weight_Log.this);
            LayoutInflater inflater = Weight_Log.this.getLayoutInflater();
            View goalWeightDialog = inflater.inflate(R.layout.dialog_log_goal_weight, null);

            builder.setView(goalWeightDialog);
            builder.setPositiveButton(R.string.LogWeightDialogSave, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.dismiss();
                    EditText userGoalWeightEditText = goalWeightDialog.findViewById(R.id.dialogUserGoalWeightInput);
                    updateGoalWeight(userGoalWeightEditText.getText().toString());
                }
            });
            builder.setNegativeButton(R.string.LogWeightDialogCancel, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                }
            });
            builder.show();
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cursor.close();
        db.close();
    }

    private void setupGraphView() {
        // Set up graph title
        weightProgressChart.setTitle(getString(R.string.WeightChartTitle));

        // Graph legend set up
        LegendRenderer lr = weightProgressChart.getLegendRenderer();
        lr.setVisible(true);
        lr.setAlign(LegendRenderer.LegendAlign.TOP);
        lr.setBackgroundColor(Color.TRANSPARENT);

        // Graph grid set up
        GridLabelRenderer glr = weightProgressChart.getGridLabelRenderer();
        glr.setGridStyle(GridLabelRenderer.GridStyle.HORIZONTAL);
        glr.setHorizontalLabelsVisible(false);
        glr.setPadding(32);

        // Create two series of data: users weight from db, and SharedPreferences goal weight
        // Get db related variables set up
        cursor = db.rawQuery("SELECT * FROM " + Weight_Database_Helper.TABLE_NAME + ";", null);
        int columnIndex = cursor.getColumnIndex(Weight_Database_Helper.KEY_WEIGHT);
        cursor.moveToFirst();

        // Get the data point arrays for each series set up
        int numRows = cursor.getCount();
        weightDataPoints = new DataPoint[numRows];
        goalDataPoints = new DataPoint[numRows];

        // Adding every weight logged on graph, graphview allows for users to zoom in/out
        double goalWeight = Double.parseDouble(mPrefs.getString(key_weight_goal, "0.0"));
        int i = 0;
        while (!cursor.isAfterLast()) {
            goalDataPoints[i] = new DataPoint(i, goalWeight);
            weightDataPoints[i] = new DataPoint(i, cursor.getFloat(columnIndex));
            ++i;
            cursor.moveToNext();
        }
        cursor.close();

        // Add the respective data points to the graph
        weightSeries = new LineGraphSeries<>(weightDataPoints);
        weightSeries.setTitle("Current Weight");
        weightSeries.setColor(Color.rgb(51, 0, 114)); //purple_a
        weightProgressChart.addSeries(weightSeries);

        goalWeightSeries = new LineGraphSeries<>(goalDataPoints);
        goalWeightSeries.setTitle("Goal");
        goalWeightSeries.setColor(Color.GREEN);
        weightProgressChart.addSeries(goalWeightSeries);
    }

    private void updateGraphView(String weight) {
        // goalWeight data point added to extend the goal line in graph
        double goalWeight = Double.parseDouble(mPrefs.getString(key_weight_goal, "0.0"));
        double weightInput = Double.parseDouble(weight);

        DataPoint newWeightDataPoint = new DataPoint(weightDataPoints.length, weightInput);
        DataPoint newGoalDataPoint = new DataPoint(goalDataPoints.length, goalWeight);

        weightSeries.appendData(newWeightDataPoint, false, NUM_MAX_GRAPH_DATA_POINTS);
        goalWeightSeries.appendData(newGoalDataPoint, false, NUM_MAX_GRAPH_DATA_POINTS);

        // Let graph view know data has updated, to re-render
        weightProgressChart.onDataChanged(false, false);
    }

    private void updateGraphViewGoalLineOnly() {
        double updatedGoalWeight = Double.parseDouble(mPrefs.getString(key_weight_goal, "0.0"));
        for (int i = 0; i < goalDataPoints.length; i++) {
            goalDataPoints[i] = new DataPoint(i, updatedGoalWeight);
        }
        goalWeightSeries.resetData(goalDataPoints); // calls onDataChanged internally
    }

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
            return;
        }

        // Update the goal line on the GraphView
        updateGraphViewGoalLineOnly();
    }
}