package com.finn.laakso.hangboardapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity {

    Switch repeaterSwitch;
    Switch timeInfoSwitch;

    TimeControls timeControls;

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

    int gripMultiplier;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // There are a lot more grips in single hangs program than repeaters, gripMultiplier
        // is used so that the grip progressbar is useful in both programs
        gripMultiplier = 1;

        // These TextViews are the visual reprseentation of the hangboard program, which hopefully
        // makes user understand time controls better
        mHangsTextView = (TextView) findViewById(R.id.mHangsTextView);
        mTimeONTextView = (TextView) findViewById(R.id.mTimeONTextView);
        mTimeOFFTextView = (TextView) findViewById(R.id.mTimeOFFTextView);
        matrixTextView = (TextView) findViewById(R.id.matrixTextView);

        repeaterSwitch = (Switch) findViewById(R.id.repeaterSwitch);
        timeInfoSwitch = (Switch) findViewById(R.id.showTimeSwitch);

        finishButton = (Button) findViewById(R.id.finishButton);


        repeaterSwitch = (Switch) findViewById(R.id.repeaterSwitch);

        // All the editable time control widgets
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

        // Lets get the time controls array from intent, so that the user can change them
        if (getIntent().hasExtra("com.finn.laakso.hangboardapp.TIMECONTROLS")) {
            int[] time_controls = getIntent().getExtras().getIntArray("com.finn.laakso.hangboardapp.TIMECONTROLS");

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

            gripSeekBar.setProgress(timeControls.getGripLaps()-1);
            hangSeekBar.setProgress(timeControls.getHangLaps()-1);
            timeONSeekBar.setProgress(timeControls.getTimeON()-1);
            timeOFFSeekBar.setProgress(timeControls.getTimeOFF());
            setsSeekBar.setProgress(timeControls.getRoutineLaps()-1);
            restSeekBar.setProgress(timeControls.getRestTime()/10);
            longRestSeekBar.setProgress(timeControls.getLongRestTime()/60);
        }

        // If hang laps are anything but 1, then workoutprogram is set to repeaters
        if (timeControls.getHangLaps() != 1) {
            repeaterSwitch.setText("Repeaters are: ON");
            repeaterSwitch.setChecked(true);
        }
        // If hang laps is set to 1, then workout program is single hangs, and we set off settings
        // that are only used in repeaters mode
        else {
            gripMultiplier = 6;
            gripSeekBar.setProgress((timeControls.getGripLaps()-1)/gripMultiplier);
            timeControls.setToRepeaters(false);

            hangSeekBar.setProgress(0);
            timeControls.setHangLaps(1);
            timeControls.setTimeOFF(0);
            timeOFFSeekBar.setProgress(0);
            repeaterSwitch.setText("Repeaters are: OFF");
            repeaterSwitch.setChecked(false);
            hangLapsEditText.setVisibility(View.INVISIBLE);
            hangSeekBar.setEnabled(false);
            timeOFFEditText.setVisibility(View.INVISIBLE);
            timeOFFSeekBar.setEnabled(false);

            // timeOFFLinearLayout.setVisibility(0);

        }


        // TextViews that tries to visualize the current workout
        matrixTextView.setText(timeControls.getGripMatrix(timeInfoSwitch.isChecked()));
        mHangsTextView.setText("" + timeControls.getHangLaps());
        mTimeONTextView.setText(timeControls.getTimeON()+"on");
        mTimeOFFTextView.setText(timeControls.getTimeOFF()+"off");

        repeaterSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    repeaterSwitch.setText("Repeaters are: ON");
                    timeControls.setToRepeaters(true);
                    gripMultiplier = 1;
                    hangLapsEditText.setVisibility(View.VISIBLE);
                    hangSeekBar.setEnabled(true);
                    timeOFFEditText.setVisibility(View.VISIBLE);
                    timeOFFSeekBar.setEnabled(true);
                    timeOFFSeekBar.setProgress(3);
                    timeOFFEditText.setText("" + 3);
                    timeControls.setTimeOFF(3);
                   // Toast.makeText(SettingsActivity.this,"Repeaters are: ON" ,Toast.LENGTH_LONG).show();
                }
                else {
                    repeaterSwitch.setText("Repeaters are: OFF");
                    timeControls.setToRepeaters(false);
                    hangSeekBar.setProgress(0);
                    timeControls.setHangLaps(1);
                    timeControls.setTimeOFF(0);
                    timeOFFSeekBar.setProgress(0);
                    gripMultiplier = 6;

                    updateProgramDisplay();

                    hangLapsEditText.setVisibility(View.INVISIBLE);
                    hangSeekBar.setEnabled(false);
                    timeOFFEditText.setVisibility(View.INVISIBLE);
                    timeOFFSeekBar.setEnabled(false);
                   // Toast.makeText(SettingsActivity.this,"Repeaters are: OFF" ,Toast.LENGTH_LONG).show();
                }

            }
        });

        // When switched, just update the visual display
        timeInfoSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                updateProgramDisplay();
            }
        });


        gripSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                gripLapsEditText.setText("" + (progress+1)*gripMultiplier);
                timeControls.setGripLaps((progress+1)*gripMultiplier);
                //matrixTextView.setText(timeControls.getGripMatrix(timeInfoSwitch.isChecked()));
                updateProgramDisplay();
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
                // mHangsTextView.setText("" + (progress+1) );
                updateProgramDisplay();
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
                // mTimeONTextView.setText(progress+1 + "on");
                updateProgramDisplay();
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
                //mTimeOFFTextView.setText(progress + "off");
                // if (progress == 0) { mTimeOFFTextView.setText(""); }
                updateProgramDisplay();
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
                //matrixTextView.setText(timeControls.getGripMatrix(timeInfoSwitch.isChecked()));
                updateProgramDisplay();
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
                updateProgramDisplay();
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
               // matrixTextView.setText(timeControls.getGripMatrix(timeInfoSwitch.isChecked()));
                updateProgramDisplay();
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

                    if ( i > 0 && i <= 100 ) {
                        timeControls.setGripLaps(i);
                        if (repeaterSwitch.isChecked() ) {
                            gripSeekBar.setProgress(i - 1);
                        }
                        else {
                            gripSeekBar.setProgress((i-1)/gripMultiplier);
                        }
                        updateProgramDisplay();
                    }
                    else {
                        gripLapsEditText.setText("" + timeControls.getGripLaps() );
                        Toast.makeText(SettingsActivity.this,"Number out of bounds, changes reverted",Toast.LENGTH_SHORT).show();
                    }
                   // Toast.makeText(SettingsActivity.this,"hehe: nyt muutettiin grips: " + timeControls.getGripLaps()  ,Toast.LENGTH_LONG).show();
                } catch (NumberFormatException nfe)
                {
                    gripLapsEditText.setText("" + timeControls.getGripLaps());
                    Toast.makeText(SettingsActivity.this,"Illegal number, changes reverted: " + nfe  ,Toast.LENGTH_LONG).show();
                }
                return false;
            }
        });

        hangLapsEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                // if (repeaterSwitch.isChecked()) {return false;}

                try {
                    int i = Integer.parseInt(hangLapsEditText.getText().toString());
                    if ( i > 0 && i <= 20 ) {
                        timeControls.setHangLaps(i);
                        hangSeekBar.setProgress(i-1);
                        updateProgramDisplay();
                    }
                    else { hangLapsEditText.setText("" + timeControls.getHangLaps() );
                        Toast.makeText(SettingsActivity.this,"Number out of bounds, changes reverted",Toast.LENGTH_SHORT).show();}

                } catch (NumberFormatException nfe)
                {
                    hangLapsEditText.setText("" + timeControls.getHangLaps());
                    Toast.makeText(SettingsActivity.this,"Illegal number, changes reverted: " + nfe  ,Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });

        timeONEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                try {
                    int i = Integer.parseInt(timeONEditText.getText().toString());
                    if ( i > 0 && i <= 60 ) {
                        timeControls.setTimeON(i);
                        timeONSeekBar.setProgress(i-1);
                        updateProgramDisplay();

                    }
                    else { timeONEditText.setText("" + timeControls.getTimeON() );
                        Toast.makeText(SettingsActivity.this,"Number out of bounds, changes reverted",Toast.LENGTH_SHORT).show();}

                } catch (NumberFormatException nfe)
                {
                    timeONEditText.setText("" + timeControls.getTimeON());
                    Toast.makeText(SettingsActivity.this,"Illegal number, changes reverted: " + nfe  ,Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });

        timeOFFEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                try {
                    int i = Integer.parseInt(timeOFFEditText.getText().toString());
                    if ( i >= 0 && i <= 200 ) {
                        timeControls.setTimeOFF(i);
                        timeOFFSeekBar.setProgress(i);
                        updateProgramDisplay();
                    }
                    else { timeOFFEditText.setText("" + timeControls.getTimeOFF() );
                        Toast.makeText(SettingsActivity.this,"Number out of bounds, changes reverted",Toast.LENGTH_SHORT).show();}

                } catch (NumberFormatException nfe)
                {
                    timeOFFEditText.setText("" + timeControls.getTimeOFF());
                    Toast.makeText(SettingsActivity.this,"Illegal number, changes reverted: " + nfe  ,Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });

        setsEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                try {
                    int i = Integer.parseInt(setsEditText.getText().toString());
                    if ( i > 0 && i <= 50 ) {
                        timeControls.setRoutineLaps(i);
                        setsSeekBar.setProgress(i-1);
                        updateProgramDisplay();
                    }
                    else { setsEditText.setText("" + timeControls.getRoutineLaps() );
                        Toast.makeText(SettingsActivity.this,"Number out of bounds, changes reverted",Toast.LENGTH_SHORT).show();}

                } catch (NumberFormatException nfe)
                {
                    setsEditText.setText("" + timeControls.getRoutineLaps());
                    Toast.makeText(SettingsActivity.this,"Illegal number, changes reverted: " + nfe  ,Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });

        restEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                try {
                    int i = Integer.parseInt(restEditText.getText().toString());
                    if ( i > 0 && i <= 500 ) {
                        timeControls.setRestTime(i);
                        restSeekBar.setProgress((i-1)/10);
                        updateProgramDisplay();
                    }
                    else { restEditText.setText("" + timeControls.getRestTime() );
                        Toast.makeText(SettingsActivity.this,"Number out of bounds, changes reverted",Toast.LENGTH_SHORT).show();}

                } catch (NumberFormatException nfe)
                {
                    restEditText.setText("" + timeControls.getRestTime());
                    Toast.makeText(SettingsActivity.this,"Illegal number, changes reverted: " + nfe  ,Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });
        longRestEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                try {
                    int i = Integer.parseInt(longRestEditText.getText().toString());
                    if ( i > 0 && i <= 1000 ) {
                        timeControls.setLongRestTime(i);
                        longRestSeekBar.setProgress((i-1)/60);
                        updateProgramDisplay();

                    }
                    else { longRestEditText.setText("" + timeControls.getLongRestTime() );
                        Toast.makeText(SettingsActivity.this,"Number out of bounds, changes reverted",Toast.LENGTH_SHORT).show();}

                } catch (NumberFormatException nfe)
                {
                    longRestEditText.setText("" + timeControls.getLongRestTime());
                    Toast.makeText(SettingsActivity.this,"Illegal number, changes reverted: " + nfe  ,Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });


        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resultIntent = new Intent();
                // resultIntent.putExtra("com.finn.laakso.hangboardapp.SETTINGS", 62);
                resultIntent.putExtra("com.finn.laakso.hangboardapp.SETTINGS", timeControls.getTimeControlsIntArray());
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

    private void updateProgramDisplay() {
        mHangsTextView.setText("" + timeControls.getHangLaps());
        mTimeONTextView.setText( timeControls.getTimeON()+"on");
        mTimeOFFTextView.setText(timeControls.getTimeOFF()+"off");
        matrixTextView.setText(timeControls.getGripMatrix(timeInfoSwitch.isChecked()));
    }

}