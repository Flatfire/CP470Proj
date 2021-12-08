package com.cp470.healthyhawk;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class FragmentExerciseDetails extends Fragment {

    // Variables
    Exercise_Log exerciseLog;
    int position;

    // Constructor
    public FragmentExerciseDetails(Exercise_Log exercise_log, int i) {
        exerciseLog = exercise_log;
        position = i;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle savedInstanceState) {
        // Inflate the xml file for the fragment
        View view = inflater.inflate(R.layout.dialog_exercise_log_info, parent, false);
        // Get View objects
        Button buttonDelete = view.findViewById(R.id.buttonDialogDeleteActivity);
        TextView textType = view.findViewById(R.id.textDialogActivityType);
        TextView textStatNum = view.findViewById(R.id.textDialogActivityStatNum);
        TextView textStatName = view.findViewById(R.id.textDialogActivityStatName);
        TextView textDateTime = view.findViewById(R.id.textDialogActivityDateTime);
        // Setup Fragment using args
        Bundle args = getArguments();
        if (args != null) {
            // Make Button visible
            buttonDelete.setVisibility(View.VISIBLE);
            // Update View
            String type = args.getString(ExerciseLogDatabaseHelper.KEY_TYPE);
            String statNum = args.getString(ExerciseLogDatabaseHelper.KEY_STAT_NUM);
            String statName = args.getString(ExerciseLogDatabaseHelper.KEY_STAT_NAME);
            String dateTime = args.getString(ExerciseLogDatabaseHelper.KEY_DATE_TIME);
            textType.setText(type);
            textStatNum.setText(statNum);
            textStatName.setText(statName);
            textDateTime.setText(dateTime);
            // Set Button onClickListener
            buttonDelete.setOnClickListener((buttonView) -> {
                exerciseLog.deleteExercise(position, type, statNum, statName, dateTime);
                getActivity().finish();
            });
        }
        return view;
    }
}
