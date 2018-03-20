package com.example.laakso.hangboardapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
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

    TextView mHangsTextView;
    TextView mTimeONTextView;
    TextView mTimeOFFTextView;
    TextView matrixTextView;

    SeekBar gripSeekBar;
    SeekBar hangSeekBar;
    SeekBar timeONSeekBar;
    SeekBar timeOFFSeekBar;
    SeekBar setsSeekBar;
    SeekBar restSeekBar;
    SeekBar longRestSeekBar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        String[] valiaika = {"jees","moid","keijo","jen"};

        mHangsTextView = (TextView) findViewById(R.id.mHangsTextView);
        mTimeONTextView = (TextView) findViewById(R.id.mTimeONTextView);
        mTimeOFFTextView = (TextView) findViewById(R.id.mTimeOFFTextView);
        matrixTextView = (TextView) findViewById(R.id.matrixTextView);


        finishButton = (Button) findViewById(R.id.finishButton);

        gripLapsEditText = (EditText) findViewById(R.id.gripLapsEditText);
        hangLapsEditText = (EditText) findViewById(R.id.hangLapsEditText);
        timeONEditText = (EditText) findViewById(R.id.timeONEditText);
        timeOFFEditText = (EditText) findViewById(R.id.timeOFFEditText);
        setsEditText= (EditText) findViewById(R.id.setsEditText);
        restEditText = (EditText) findViewById(R.id.restEditText);
        longRestEditText = (EditText) findViewById(R.id.longRestEditText);

        gripSeekBar = (SeekBar) findViewById(R.id.gripSeekBar);
        hangSeekBar = (SeekBar) findViewById(R.id.hangSeekBar);
        timeONSeekBar = (SeekBar) findViewById(R.id.timeONSeekBar);
        timeOFFSeekBar = (SeekBar) findViewById(R.id.timeOFFSeekBar);
        setsSeekBar = (SeekBar) findViewById(R.id.setsSeekBar);
        restSeekBar = (SeekBar) findViewById(R.id.restSeekBar);
        longRestSeekBar = (SeekBar) findViewById(R.id.longRestSeekBar);

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


        matrixTextView.setText(timeControls.getGripMatrix());
        mHangsTextView.setText("" + timeControls.getHangLaps());
        mTimeONTextView.setText(timeControls.getTimeON()+"on");
        mTimeOFFTextView.setText(timeControls.getTimeOFF()+"off");

        gripSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                gripLapsEditText.setText("" + (progress+1));
                timeControls.setGripLaps(progress+1);
                matrixTextView.setText(timeControls.getGripMatrix());

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        hangSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                hangLapsEditText.setText("" + (progress+1));
                timeControls.setHangLaps(progress+1);
                mHangsTextView.setText("" + (progress+1) );
               // if (progress == 0) {mHangsTextView.setText(""); }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        timeONSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                timeONEditText.setText("" + (progress+1));
                timeControls.setTimeON(progress+1);
                mTimeONTextView.setText(progress+1 + "on");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        timeOFFSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                timeOFFEditText.setText("" + (progress));
                timeControls.setTimeOFF(progress);
                mTimeOFFTextView.setText(progress + "off");
                if (progress == 0) { mTimeOFFTextView.setText(""); }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        setsSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                setsEditText.setText("" + (progress+1));
                timeControls.setRoutineLaps(progress+1);
                matrixTextView.setText(timeControls.getGripMatrix());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        restSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                restEditText.setText("" + (progress+1)*10);
                timeControls.setRestTime((progress+1)*10);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        longRestSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                longRestEditText.setText("" + (progress+1)*60);
                timeControls.setLongRestTime((progress+1)*60);
                matrixTextView.setText(timeControls.getGripMatrix());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


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
                    if ( i >= 0 && i <= 200 ) { timeControls.setTimeOFF(i); }
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
