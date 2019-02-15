package com.finn.laakso.hangboardapp;

import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

public class SettingsActivity extends AppCompatActivity {

    private Button resetButton;
    private Switch helpSwitch;

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
            actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.ceramic)));
            // actionBar.setBackgroundDrawable();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        setContentView(R.layout.activity_settings);

        prefSettings = PreferenceManager.getDefaultSharedPreferences(this);

        boolean helpSwitchPosition = prefSettings.getBoolean("helpSwitch",true);

        resetButton = findViewById(R.id.resetButton);
        helpSwitch = findViewById(R.id.helpSwitch);

        helpSwitch.setChecked(helpSwitchPosition);

        helpSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences.Editor editor = prefSettings.edit();
                editor.putBoolean("helpSwitch",isChecked);
                editor.apply();
            }
        });

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                helpSwitch.setChecked(true);
            }
        });

    }
}
