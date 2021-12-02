package com.cp470.healthyhawk;

import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import com.cp470.healthyhawk.databinding.ActivityExerciseLogBinding;

public class Exercise_Log extends AppCompatActivity {

    private ActivityExerciseLogBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityExerciseLogBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.fabAddExercise.setOnClickListener(view ->
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
            .setAction("Action", null).show());
    }
}