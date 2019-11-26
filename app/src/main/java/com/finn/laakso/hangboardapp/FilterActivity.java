package com.finn.laakso.hangboardapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Toast;

public class FilterActivity extends AppCompatActivity {

    public static final int DEFAULT_MIN_DIFFICULTY = 0;
    public static final int DEFAULT_MAX_DIFFICULTY = 20;

    private Button resetButton;
    private Button backButton;

    private EditText minDifficultyEditText;
    private EditText maxDifficultyEditText;

    private SeekBar minDifficultySeekBar;
    private SeekBar maxDifficultySeekBar;

    private SharedPreferences filterSettings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

       resetButton = (Button) findViewById(R.id.resetButton);
       backButton = (Button) findViewById(R.id.backButton);

       minDifficultyEditText = (EditText) findViewById(R.id.minDifficultyEditText);
       maxDifficultyEditText = (EditText) findViewById(R.id.maxDifficultyEditText);

       minDifficultySeekBar = (SeekBar) findViewById(R.id.minDifficultySeekBar);
       maxDifficultySeekBar = (SeekBar) findViewById(R.id.maxDifficultySeekBar);

       filterSettings = PreferenceManager.getDefaultSharedPreferences(this);

       int minDifficulty = filterSettings.getInt("minDifficultyFilter",DEFAULT_MIN_DIFFICULTY);
       int maxDifficulty = filterSettings.getInt("maxDifficultyFilter",DEFAULT_MAX_DIFFICULTY);

       minDifficultySeekBar.setProgress(minDifficulty/5);
       maxDifficultySeekBar.setProgress(maxDifficulty/5);
       minDifficultyEditText.setText(""+minDifficulty);
       maxDifficultyEditText.setText(""+maxDifficulty);

       minDifficultySeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
           @Override
           public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
               minDifficultyEditText.setText(""+i*5);
           }

           @Override
           public void onStartTrackingTouch(SeekBar seekBar) {

           }

           @Override
           public void onStopTrackingTouch(SeekBar seekBar) {
               SharedPreferences.Editor editor = filterSettings.edit();
               editor.putInt("minDifficultyFilter",seekBar.getProgress()*5 );
               editor.apply();

           }
       });

       maxDifficultySeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
           @Override
           public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
               maxDifficultyEditText.setText(""+i*5);
           }

           @Override
           public void onStartTrackingTouch(SeekBar seekBar) {

           }

           @Override
           public void onStopTrackingTouch(SeekBar seekBar) {

               SharedPreferences.Editor editor = filterSettings.edit();
               editor.putInt("maxDifficultyFilter",seekBar.getProgress()*5 );
               editor.apply();
           }
       });

       backButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               finish();
           }
       });

       resetButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Toast.makeText(FilterActivity.this,"reset to defaults",Toast.LENGTH_LONG).show();
           }
       });
/*
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
    }
}
