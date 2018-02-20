package com.example.laakso.hangboardapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity {


    TimeControls timeControls;
    Button testButton;
    Button finishButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        testButton = (Button) findViewById(R.id.testButton);
        finishButton = (Button) findViewById(R.id.finishButton);

        if (getIntent().hasExtra("com.example.laakso.hangboardapp.TIMECONTROLS")) {
            int[] time_controls = getIntent().getExtras().getIntArray("com.example.laakso.hangboardapp.TIMECONTROLS");

            timeControls = new TimeControls();
            // timeControls.setTimeControls(time_controls);
            timeControls.setTimeControls(new int[] {6, 2, 5 ,3 , 1, 15, 150});

        }

        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeControls.setTimeON(timeControls.getTimeON()+1);
                Toast.makeText(SettingsActivity.this,"hehe: " + timeControls.getTimeON(),Toast.LENGTH_LONG).show();
            }
        });

        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resultIntent = new Intent();
                // resultIntent.putExtra("com.example.laakso.hangboardapp.SETTINGS", 62);
                resultIntent.putExtra("com.example.laakso.hangboardapp.SETTINGS", timeControls.getTimeControlsIntArray());
                setResult(Activity.RESULT_OK, resultIntent);


                finish();
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action" + timeControls.getGripLaps(), Snackbar.LENGTH_LONG)
                        .setAction("Action" + timeControls.getHangLapsSeconds(), null).show();
            }
        });
    }
}
