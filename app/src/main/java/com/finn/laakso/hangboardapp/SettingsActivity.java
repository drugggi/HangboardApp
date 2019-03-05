package com.finn.laakso.hangboardapp;

import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity {

    public static final float DEFAULT_TIMER_SIZE = 1.0f;
    public static final int DEFAULT_START_TIME = 30;
    public static final boolean DEFAULT_SHOW_MESSAGES = true;

    private Button resetButton,okButton;

    private Switch helpSwitch;
    private EditText startTimeEditText,timerSizeEditText;

    private SharedPreferences prefSettings;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            android.support.v7.app.ActionBar actionBar = getSupportActionBar();
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setTitle(Html.fromHtml("<font color='#3E0E1F'>Settings</font>"));

            actionBar.setLogo(R.drawable.gripgrading48x);
            actionBar.setDisplayUseLogoEnabled(true);
            actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.slate)));
            // actionBar.setBackgroundDrawable();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        setContentView(R.layout.activity_settings);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        prefSettings = PreferenceManager.getDefaultSharedPreferences(this);


        boolean helpSwitchPosition = prefSettings.getBoolean("helpSwitch",true);
        int startTime = prefSettings.getInt("workoutStartTime",DEFAULT_START_TIME);
        float timerSize = prefSettings.getFloat("workoutTimerSize",DEFAULT_TIMER_SIZE);

        resetButton = findViewById(R.id.resetButton);
        okButton = findViewById(R.id.okButton);
        helpSwitch = findViewById(R.id.helpSwitch);
        startTimeEditText = findViewById(R.id.startTimeEditText);
        timerSizeEditText = findViewById(R.id.timerSizeEditText);

        helpSwitch.setChecked(helpSwitchPosition);
        startTimeEditText.setText(""+startTime);
        timerSizeEditText.setText(""+timerSize);
        //startTimeEditText.clearFocus();

        helpSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences.Editor editor = prefSettings.edit();
                editor.putBoolean("helpSwitch",isChecked);
                editor.apply();
            }
        });

        startTimeEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {


                try {
                    int newStartTime = Integer.parseInt(startTimeEditText.getText().toString() );
                    if ( newStartTime > 1 && newStartTime <= 1000 ) {
                        SharedPreferences.Editor editor = prefSettings.edit();
                        editor.putInt("workoutStartTime",newStartTime);
                        editor.apply();
                    }
                    else {
                        startTimeEditText.setText("" + prefSettings.getInt("workoutStartTime",DEFAULT_START_TIME));
                        Toast.makeText(SettingsActivity.this,"Number out of bounds, changes reverted",Toast.LENGTH_SHORT).show();}

                } catch (NumberFormatException nfe)
                {
                    startTimeEditText.setText("" + prefSettings.getInt("workoutStartTime",DEFAULT_START_TIME));
                    Toast.makeText(SettingsActivity.this,"Illegal number, changes reverted",Toast.LENGTH_SHORT).show();
                }

                startTimeEditText.clearFocus();
                return false;
            }
        });

        timerSizeEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {

                try {
                    float newTimerSize = Float.parseFloat(timerSizeEditText.getText().toString() );

                    if ( newTimerSize >= 0.2f && newTimerSize <= 3.0 ) {
                        SharedPreferences.Editor editor = prefSettings.edit();
                        editor.putFloat("workoutTimerSize",newTimerSize);
                        editor.apply();
                    }
                    else {
                        timerSizeEditText.setText("" + prefSettings.getFloat("workoutTimerSize",DEFAULT_TIMER_SIZE));
                        Toast.makeText(SettingsActivity.this,"Number out of bounds, changes reverted",Toast.LENGTH_SHORT).show();}

                } catch (NumberFormatException nfe)
                {
                    timerSizeEditText.setText("" + prefSettings.getFloat("workoutTimerSize",DEFAULT_TIMER_SIZE));
                    Toast.makeText(SettingsActivity.this,"Illegal number, changes reverted",Toast.LENGTH_SHORT).show();
                }


                timerSizeEditText.clearFocus();
                return false;
            }
        });


        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                helpSwitch.setChecked(DEFAULT_SHOW_MESSAGES);

                SharedPreferences.Editor editor = prefSettings.edit();
                editor.putInt("workoutStartTime",DEFAULT_START_TIME);
                startTimeEditText.setText(""+DEFAULT_START_TIME);

                //SharedPreferences.Editor editor = prefSettings.edit();
                editor.putFloat("workoutTimerSize",DEFAULT_TIMER_SIZE);
                timerSizeEditText.setText(""+DEFAULT_TIMER_SIZE);

                editor.apply();
            }
        });


        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}
