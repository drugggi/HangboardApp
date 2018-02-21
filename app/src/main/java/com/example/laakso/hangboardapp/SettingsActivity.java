package com.example.laakso.hangboardapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity {


    TimeControls timeControls;
    Button testButton;
    Button finishButton;
    EditText gripLapsEditText;
    EditText hangLapsEditText;
    EditText timeONEditText;
    EditText timeOFFEditText;
    EditText setsEditText;
    EditText restEditText;
    EditText longRestEditText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        testButton = (Button) findViewById(R.id.testButton);
        finishButton = (Button) findViewById(R.id.finishButton);

        gripLapsEditText = (EditText) findViewById(R.id.gripLapsEditText);
        hangLapsEditText = (EditText) findViewById(R.id.hangLapsEditText);
        timeONEditText = (EditText) findViewById(R.id.timeONEditText);
        timeOFFEditText = (EditText) findViewById(R.id.timeOFFEditText);
        setsEditText= (EditText) findViewById(R.id.setsEditText);
        restEditText = (EditText) findViewById(R.id.restEditText);
        longRestEditText = (EditText) findViewById(R.id.longRestEditText);

        if (getIntent().hasExtra("com.example.laakso.hangboardapp.TIMECONTROLS")) {
            int[] time_controls = getIntent().getExtras().getIntArray("com.example.laakso.hangboardapp.TIMECONTROLS");

            timeControls = new TimeControls();
            // timeControls.setTimeControls(time_controls);
            timeControls.setTimeControls(time_controls);
            // Toast.makeText(SettingsActivity.this,"hehe: " + timeControls.getTimeON(),Toast.LENGTH_LONG).show();
             gripLapsEditText.setText("" + timeControls.getGripLaps());
             hangLapsEditText.setText("" + timeControls.getHangLaps());
             timeONEditText.setText(""+ timeControls.getTimeON());
             timeOFFEditText.setText("" + timeControls.getTimeOFF());
             setsEditText.setText("" + timeControls.getRoutineLaps());
             restEditText.setText("" + timeControls.getRestTime());
             longRestEditText.setText("" + timeControls.getLongRestTime());
             //gripLapsEditText.set
        }

        gripLapsEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                try {
                    int i = Integer.parseInt(gripLapsEditText.getText().toString());
                    if ( i > 0 && i <= 100 ) { timeControls.setGripLaps(i); }
                    else { gripLapsEditText.setText("" + timeControls.getGripLaps() ); }
                   // Toast.makeText(SettingsActivity.this,"hehe: nyt muutettiin grips: " + timeControls.getGripLaps()  ,Toast.LENGTH_LONG).show();
                } catch (NumberFormatException nfe)
                {
                    gripLapsEditText.setText("" + timeControls.getGripLaps());
                    //Toast.makeText(SettingsActivity.this,"numformatexception: " + nfe  ,Toast.LENGTH_LONG).show();
                }
                return false;
            }
        });

        hangLapsEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                try {
                    int i = Integer.parseInt(hangLapsEditText.getText().toString());
                    if ( i > 0 && i <= 20 ) { timeControls.setHangLaps(i); }
                    else { hangLapsEditText.setText("" + timeControls.getHangLaps() ); }

                } catch (NumberFormatException nfe)
                {
                    hangLapsEditText.setText("" + timeControls.getHangLaps());
                    Toast.makeText(SettingsActivity.this,"numformatexception: " + nfe  ,Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });

        timeONEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                try {
                    int i = Integer.parseInt(timeONEditText.getText().toString());
                    if ( i > 0 && i <= 60 ) { timeControls.setTimeON(i); }
                    else { timeONEditText.setText("" + timeControls.getTimeON() ); }

                } catch (NumberFormatException nfe)
                {
                    timeONEditText.setText("" + timeControls.getTimeON());
                    Toast.makeText(SettingsActivity.this,"numformatexception: " + nfe  ,Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });

        timeOFFEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                try {
                    int i = Integer.parseInt(timeOFFEditText.getText().toString());
                    if ( i > 0 && i <= 200 ) { timeControls.setTimeOFF(i); }
                    else { timeOFFEditText.setText("" + timeControls.getTimeOFF() ); }

                } catch (NumberFormatException nfe)
                {
                    timeOFFEditText.setText("" + timeControls.getTimeOFF());
                    Toast.makeText(SettingsActivity.this,"numformatexception: " + nfe  ,Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });

        setsEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                try {
                    int i = Integer.parseInt(setsEditText.getText().toString());
                    if ( i > 0 && i <= 50 ) { timeControls.setRoutineLaps(i); }
                    else { setsEditText.setText("" + timeControls.getRoutineLaps() ); }

                } catch (NumberFormatException nfe)
                {
                    setsEditText.setText("" + timeControls.getRoutineLaps());
                    Toast.makeText(SettingsActivity.this,"numformatexception: " + nfe  ,Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });

        restEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                try {
                    int i = Integer.parseInt(restEditText.getText().toString());
                    if ( i > 0 && i <= 500 ) { timeControls.setRestTime(i); }
                    else { restEditText.setText("" + timeControls.getRestTime() ); }

                } catch (NumberFormatException nfe)
                {
                    restEditText.setText("" + timeControls.getRestTime());
                    Toast.makeText(SettingsActivity.this,"numformatexception: " + nfe  ,Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });
        longRestEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                try {
                    int i = Integer.parseInt(longRestEditText.getText().toString());
                    if ( i > 0 && i <= 1000 ) { timeControls.setLongRestTime(i); }
                    else { longRestEditText.setText("" + timeControls.getLongRestTime() ); }

                } catch (NumberFormatException nfe)
                {
                    longRestEditText.setText("" + timeControls.getLongRestTime());
                    Toast.makeText(SettingsActivity.this,"numformatexception: " + nfe  ,Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });

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
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }
}
