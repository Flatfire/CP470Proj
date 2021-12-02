package com.cp470.healthyhawk;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;

public class Weight_Log extends AppCompatActivity {

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

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weight_log);

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

        // Populating the graph, default shows week view
        weightProgressChart = findViewById(R.id.weightProgressChart);


        // Setting onClickListeners
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

    private void updateCurrentWeight(String weight) {
        String weightString = weight + " " + mPrefs.getString(key_weight_unit, "");
        currentWeightText.setText(weightString);

        SharedPreferences.Editor mEditor = mPrefs.edit();
        mEditor.putString(key_weight, weight);

        if (!mEditor.commit()) {
            String commit_failed = getString(R.string.commit_preferences_failed);
            Toast.makeText(this, commit_failed, Toast.LENGTH_SHORT).show();
        }
    }

    private void updateGoalWeight(String goalWeight) {
        String goalWeightString = goalWeight + " " + mPrefs.getString(key_weight_unit, "");
        goalWeightText.setText(goalWeightString);

        SharedPreferences.Editor mEditor = mPrefs.edit();
        mEditor.putString(key_weight_goal, goalWeight);

        if (!mEditor.commit()) {
            String commit_failed = getString(R.string.commit_preferences_failed);
            Toast.makeText(this, commit_failed, Toast.LENGTH_SHORT).show();
        }
    }
}