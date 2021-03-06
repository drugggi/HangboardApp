package com.finn.laakso.hangboardapp;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class TimeControlsActivity extends AppCompatActivity {

    private Switch repeaterSwitch;
    private Switch timeInfoSwitch;

    private TimeControls timeControls;

    private Button finishButton;
    private Button cancelButton;
    private Button savePreferencesButton;
    private Button loadPreferencesButton;


    private EditText gripLapsEditText;
    private EditText hangLapsEditText;
    private EditText timeONEditText;
    private EditText timeOFFEditText;
    private EditText setsEditText;
    private EditText restEditText;
    private EditText longRestEditText;

    private TextView mHangsTextView;
    private TextView mTimeONTextView;
    private TextView mTimeOFFTextView;
    private TextView matrixTextView;
    private TextView preferencesTextView;

    private SeekBar gripSeekBar;
    private SeekBar hangSeekBar;
    private SeekBar timeONSeekBar;
    private SeekBar timeOFFSeekBar;
    private SeekBar setsSeekBar;
    private SeekBar restSeekBar;
    private SeekBar longRestSeekBar;

    //private int gripMultiplier;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putIntArray("workoutactivity_timecontrolsintarray", timeControls.getTimeControlsIntArray());

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timecontrols);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        // There are a lot more grips in single hangs program than repeaters, gripMultiplier
        // is used so that the grip progressbar is useful in both programs
        // gripMultiplier = 1;

        // These TextViews are the visual representation of the hangboard program, which hopefully
        // makes user understand time controls better
        mHangsTextView = (TextView) findViewById(R.id.mHangsTextView);
        mTimeONTextView = (TextView) findViewById(R.id.mTimeONTextView);
        mTimeOFFTextView = (TextView) findViewById(R.id.mTimeOFFTextView);
        matrixTextView = (TextView) findViewById(R.id.matrixTextView);
        preferencesTextView = (TextView) findViewById(R.id.preferenceTextView);

        repeaterSwitch = (Switch) findViewById(R.id.repeaterSwitch);
        timeInfoSwitch = (Switch) findViewById(R.id.showTimeSwitch);

        finishButton = (Button) findViewById(R.id.finishButton);
        cancelButton = (Button) findViewById(R.id.cancelButton);
        savePreferencesButton = (Button) findViewById(R.id.saveButton);
        loadPreferencesButton = (Button) findViewById(R.id.loadButton);

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


        }

        if (savedInstanceState != null) {
            timeControls.setTimeControls(savedInstanceState.getIntArray("workoutactivity_timecontrolsintarray"));
        }


        savePreferencesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String statusString = "Preferences Loaded!";
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(v.getContext());
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("savePreferences", statusString);

                editor.putBoolean("isRepeaters",timeControls.isRepeaters() );
                editor.putInt("grips",timeControls.getGripLaps() );
                editor.putInt("repetitions", timeControls.getHangLaps() );

                editor.putInt("timeON",timeControls.getTimeON() );
                editor.putInt("timeOFF", timeControls.getTimeOFF() );
                editor.putInt("sets",timeControls.getRoutineLaps() );

                editor.putInt("restTime",timeControls.getRestTime() );
                editor.putInt("longRestTime", timeControls.getLongRestTime() );

                String preferenceText = "Saved time controls: " + timeControls.getTimeControlsAsString();

                preferencesTextView.setText(preferenceText );

                editor.apply();
            }
        });

        loadPreferencesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(v.getContext());
                //String statusString = prefs.getString("save time controls","No time controls saved yet");

                //boolean isRepeaters = prefs.getBoolean("isRepeaters", true);

                int grips = prefs.getInt("grips",6);
                int reps = prefs.getInt("repetitions",6);
                int timeON = prefs.getInt("timeON",7);
                int timeOFF = prefs.getInt("timeOFF",3);
                int sets = prefs.getInt("sets",3);

                int rest = prefs.getInt("restTime",150);
                int longRest =  prefs.getInt("longRestTime",360);

                timeControls.setTimeControls(new int[]{grips, reps, timeON, timeOFF, sets, rest, longRest});

                updateTimeControlsDisplay();
                updateProgramDisplay();
            }
        });

        repeaterSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    repeaterSwitch.setText("Repeaters are: ON");

                    timeControls.setHangLaps(6);
                    timeControls.setTimeON(7);
                    timeControls.setTimeOFF(3);

                    //gripMultiplier = 1;
                    hangLapsEditText.setEnabled(true);
                    hangSeekBar.setEnabled(true);
                    timeOFFEditText.setEnabled(true);
                    timeOFFSeekBar.setEnabled(true);

                    hangSeekBar.setProgress(4);
                    timeONSeekBar.setProgress(6);
                    timeOFFSeekBar.setProgress(3);
                    hangLapsEditText.setText("" + timeControls.getHangLaps() );
                    timeONEditText.setText(""+timeControls.getTimeON() );
                    timeOFFEditText.setText("" + timeControls.getTimeOFF() );
                    
                }
                else {
                    repeaterSwitch.setText("Repeaters are: OFF");
                    timeControls.setHangLaps(1);
                    timeControls.setTimeON(10);
                    timeControls.setTimeOFF(0);

                    hangSeekBar.setProgress(0);
                    timeONSeekBar.setProgress(9);
                    timeOFFSeekBar.setProgress(0);


                    hangLapsEditText.setText("" + timeControls.getHangLaps() );
                    timeONEditText.setText("" + timeControls.getTimeON() );
                    timeOFFEditText.setText("" + timeControls.getTimeOFF() );

                    hangLapsEditText.setEnabled(false);
                    hangSeekBar.setEnabled(false);
                    timeOFFEditText.setEnabled(false);
                    timeOFFSeekBar.setEnabled(false);
                }
                updateProgramDisplay();

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

                gripLapsEditText.setText("" + (progress+1));
                 // timeControls.setGripLaps((progress+1));

                updateProgramDisplay();

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int progress = seekBar.getProgress() + 1;

                timeControls.setGripLaps(progress);

                updateProgramDisplay();
            }
        });

        hangSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {


                // Messy, if repeatersSwitch is single hangs mode progress would update wrong number on
                // text fields,
                if (repeaterSwitch.isChecked() ) {
                    hangLapsEditText.setText("" + (progress+2));
                    mHangsTextView.setText("" + (progress + 2));
                }
                else {
                    hangLapsEditText.setText("" + 1);
                    mHangsTextView.setText("" + 1);
                }
                //updateProgramDisplay();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // Consider putting timeControls editing here, after user has made the move
                int progress = seekBar.getProgress() + 2 ;
                timeControls.setHangLaps(progress);

                updateProgramDisplay();
            }
        });

        timeONSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                timeONEditText.setText("" + (progress+1));

                mTimeONTextView.setText((progress+1)+"on");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int progress = seekBar.getProgress() + 1;
                timeControls.setTimeON(progress);
                updateProgramDisplay();
            }
        });

        timeOFFSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                timeOFFEditText.setText("" + (progress));

                mTimeOFFTextView.setText(progress+ "off");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int progress = seekBar.getProgress();
                timeControls.setTimeOFF(progress);

                updateProgramDisplay();

            }
        });
        setsSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                setsEditText.setText("" + (progress+1));

                if (progress == 0) {
                    longRestEditText.setEnabled(false);
                    longRestSeekBar.setEnabled(false);

                }
                else  {
                    longRestEditText.setEnabled(true);
                    longRestSeekBar.setEnabled(true);
                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int progress = seekBar.getProgress() + 1;

                timeControls.setRoutineLaps(progress);

                updateProgramDisplay();

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
                int progress = (seekBar.getProgress() + 1)  * 10;

                timeControls.setRestTime(progress);

            }
        });
        longRestSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                longRestEditText.setText("" + (progress+1)*60);
               // timeControls.setLongRestTime((progress+1)*60);
                //updateProgramDisplay();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int progress = (seekBar.getProgress()+1)*60;

                timeControls.setLongRestTime(progress);
                updateProgramDisplay();

            }
        });


        gripLapsEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                try {
                    int i = Integer.parseInt(gripLapsEditText.getText().toString());

                    if ( i > 0 && i <= 100 ) {
                        timeControls.setGripLaps(i);

                        //
                        if (i < 30) {
                            gripSeekBar.setProgress(i-1);
                        }

                        updateProgramDisplay();
                    }
                    else {
                        gripLapsEditText.setText("" + timeControls.getGripLaps() );
                        Toast.makeText(TimeControlsActivity.this,"Number out of bounds, changes reverted",Toast.LENGTH_SHORT).show();
                    }
                } catch (NumberFormatException nfe)
                {
                    gripLapsEditText.setText("" + timeControls.getGripLaps());
                    Toast.makeText(TimeControlsActivity.this,"Illegal number, changes reverted" ,Toast.LENGTH_LONG).show();
                }
                return false;
            }
        });

        hangLapsEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {


                try {
                    int i = Integer.parseInt(hangLapsEditText.getText().toString());
                    if ( i > 0 && i <= 20 ) {
                        // single hangs
                        if (i == 1) {
                            repeaterSwitch.setChecked(false);
                        }
                        else {
                            if (i < 13) {
                                hangSeekBar.setProgress(i-2);
                            }

                            timeControls.setHangLaps(i);
                        }
                        updateProgramDisplay();
                    }
                    else { hangLapsEditText.setText("" + timeControls.getHangLaps() );
                        Toast.makeText(TimeControlsActivity.this,"Number out of bounds, changes reverted",Toast.LENGTH_SHORT).show();}

                } catch (NumberFormatException nfe)
                {
                    hangLapsEditText.setText("" + timeControls.getHangLaps());
                    Toast.makeText(TimeControlsActivity.this,"Illegal number, changes reverted",Toast.LENGTH_SHORT).show();
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

                        if ( i < 22 ) {
                            timeONSeekBar.setProgress(i-1);
                        }

                        updateProgramDisplay();

                    }
                    else { timeONEditText.setText("" + timeControls.getTimeON() );
                        Toast.makeText(TimeControlsActivity.this,"Number out of bounds, changes reverted",Toast.LENGTH_SHORT).show();}

                } catch (NumberFormatException nfe)
                {
                    timeONEditText.setText("" + timeControls.getTimeON());
                    Toast.makeText(TimeControlsActivity.this,"Illegal number, changes reverted",Toast.LENGTH_SHORT).show();
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

                        if ( i < 11) {
                            timeOFFSeekBar.setProgress(i);
                        }

                        updateProgramDisplay();
                    }
                    else { timeOFFEditText.setText("" + timeControls.getTimeOFF() );
                        Toast.makeText(TimeControlsActivity.this,"Number out of bounds, changes reverted",Toast.LENGTH_SHORT).show();}

                } catch (NumberFormatException nfe)
                {
                    timeOFFEditText.setText("" + timeControls.getTimeOFF());
                    Toast.makeText(TimeControlsActivity.this,"Illegal number, changes reverted",Toast.LENGTH_SHORT).show();
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

                        if (i < 8) {
                            setsSeekBar.setProgress(i-1);
                        }

                        updateProgramDisplay();
                    }
                    else { setsEditText.setText("" + timeControls.getRoutineLaps() );
                        Toast.makeText(TimeControlsActivity.this,"Number out of bounds, changes reverted",Toast.LENGTH_SHORT).show();}

                } catch (NumberFormatException nfe)
                {
                    setsEditText.setText("" + timeControls.getRoutineLaps());
                    Toast.makeText(TimeControlsActivity.this,"Illegal number, changes reverted",Toast.LENGTH_SHORT).show();
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

                        updateProgramDisplay();
                    }
                    else { restEditText.setText("" + timeControls.getRestTime() );
                        Toast.makeText(TimeControlsActivity.this,"Number out of bounds, changes reverted",Toast.LENGTH_SHORT).show();}

                } catch (NumberFormatException nfe)
                {
                    restEditText.setText("" + timeControls.getRestTime());
                    Toast.makeText(TimeControlsActivity.this,"Illegal number, changes reverted",Toast.LENGTH_SHORT).show();
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

                        updateProgramDisplay();

                    }
                    else { longRestEditText.setText("" + timeControls.getLongRestTime() );
                        Toast.makeText(TimeControlsActivity.this,"Number out of bounds, changes reverted",Toast.LENGTH_SHORT).show();}

                } catch (NumberFormatException nfe)
                {
                    longRestEditText.setText("" + timeControls.getLongRestTime());
                    Toast.makeText(TimeControlsActivity.this,"Illegal number, changes reverted",Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });


        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent resultIntent = new Intent();
                resultIntent.putExtra("com.finn.laakso.hangboardapp.SETTINGS", timeControls.getTimeControlsIntArray());
                setResult(Activity.RESULT_OK, resultIntent);

                finish();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

        // puts the saved preferences to TextView so that user can see what time controls are saved
        updatePreferenceTextView();

        // puts settings editTexts and progressbars into right positions
        updateTimeControlsDisplay();

        // put Workout information matrix up to date
        updateProgramDisplay();

    }
    // puts the saved preferences to TextView so that user can see what time controls are saved
    private void updatePreferenceTextView() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(TimeControlsActivity.this);

        int grips = prefs.getInt("grips",6);
        int reps = prefs.getInt("repetitions",6);
        int timeON = prefs.getInt("timeON",7);
        int timeOFF = prefs.getInt("timeOFF",3);
        int sets = prefs.getInt("sets",3);

        int rest = prefs.getInt("restTime",150);
        int longRest =  prefs.getInt("longRestTime",360);

        TimeControls tempTimeControls = new TimeControls();

        tempTimeControls.setTimeControls(new int[]{grips, reps, timeON, timeOFF, sets, rest, longRest});
        // tempTimeControls.setToRepeaters(isRepeaters);

        String preferenceText = "Saved time controls: " + tempTimeControls.getTimeControlsAsString();
        preferencesTextView.setText(preferenceText);

    }

    // updateTimeControlsDisplay sets all the EditTexts and SeekBars on right status based on
    // time controls that timeControls holds, its a bit tricky because setting SeekBar to some place
    // triggers onSeekBarchange listener which also updates information
    private void updateTimeControlsDisplay() {
        int[] tempControls = timeControls.getTimeControlsIntArray();

        // If hang laps are anything but 1, then workoutprogram is set to repeaters
        if (timeControls.getHangLaps() != 1) {
            // this will mess mess with initial timeControls
            repeaterSwitch.setChecked(true);
            //gripMultiplier = 1;


            hangLapsEditText.setEnabled(true);
            hangSeekBar.setEnabled(true);
            timeOFFEditText.setEnabled(true);
            timeOFFSeekBar.setEnabled(true);

        }
            // If hang laps is set to 1, then workout program is single hangs, and we set off settings
            // that are only used in repeaters mode
        else {
            //this will mess with initial time controls thats why we stored them in int[] tempControls
            repeaterSwitch.setChecked(false);

            hangLapsEditText.setEnabled(false);
            hangSeekBar.setEnabled(false);
            timeOFFEditText.setEnabled(false);
            timeOFFSeekBar.setEnabled(false);

        }

        timeControls.setTimeControls(tempControls);

        gripSeekBar.setProgress((timeControls.getGripLaps()-1) );
        hangSeekBar.setProgress(timeControls.getHangLaps()-2);
        timeONSeekBar.setProgress(timeControls.getTimeON()-1);
        timeOFFSeekBar.setProgress(timeControls.getTimeOFF());
        setsSeekBar.setProgress(timeControls.getRoutineLaps()-1);
        restSeekBar.setProgress(timeControls.getRestTime()/10-1);
        longRestSeekBar.setProgress(timeControls.getLongRestTime()/60-1);

        timeControls.setTimeControls(tempControls);

        gripLapsEditText.setText("" + timeControls.getGripLaps());
        hangLapsEditText.setText("" + timeControls.getHangLaps());
        timeONEditText.setText(""+ timeControls.getTimeON());
        timeOFFEditText.setText("" + timeControls.getTimeOFF());
        setsEditText.setText("" + timeControls.getRoutineLaps());
        restEditText.setText("" + timeControls.getRestTime());
        longRestEditText.setText("" + timeControls.getLongRestTime());

    }

    private void updateProgramDisplay() {
        mHangsTextView.setText("" + timeControls.getHangLaps());
        mTimeONTextView.setText( timeControls.getTimeON()+"on");
        mTimeOFFTextView.setText(timeControls.getTimeOFF()+"off");
        matrixTextView.setText(timeControls.getGripMatrix(timeInfoSwitch.isChecked()));
    }

}
