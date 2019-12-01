package com.finn.laakso.hangboardapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class FilterActivity extends AppCompatActivity {

    public static final int DEFAULT_MIN_DIFFICULTY = 0;
    public static final int DEFAULT_MAX_DIFFICULTY = 20;
    public static final Boolean[] DEFAULT_GRIPTYPES_ALLOWED = {true, true, true, true, true, true, true, true, true, true};
    public static final boolean DEFAULT_USE_EVERY_GRIP = true;
    public static final boolean DEFAULT_SORT_HOLDS = false;


    private EditText minDifficultyEditText;
    private EditText maxDifficultyEditText;

    private SeekBar minDifficultySeekBar;
    private SeekBar maxDifficultySeekBar;

    private CheckBox fourfingerCheckBox;
    private CheckBox threefrontCheckBox;
    private CheckBox threebackCheckBox;
    private CheckBox twofrontCheckBox;
    private CheckBox twomiddleCheckBox;
    private CheckBox twobackCheckBox;
    private CheckBox indexfingerCheckBox;
    private CheckBox middlefingerCheckBox;
    private CheckBox ringfingerCheckBox;
    private CheckBox littlefingerCheckBox;
    private Boolean[] gripTypesAllowed;

    private Switch fillSwitch;
    private Switch sortSwitch;

    private Button resetButton;
    private Button backButton;

    private SharedPreferences filterSettings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fourfingerCheckBox = (CheckBox) findViewById(R.id.fourfingerCheckBox);
        threefrontCheckBox = (CheckBox) findViewById(R.id.threefrontCheckBox);
        threebackCheckBox = (CheckBox) findViewById(R.id.threebackCheckBox);
        twofrontCheckBox = (CheckBox) findViewById(R.id.twofrontCheckBox);
        twomiddleCheckBox = (CheckBox) findViewById(R.id.twomiddleCheckBox);
        twobackCheckBox = (CheckBox) findViewById(R.id.twobackCheckBox);
        indexfingerCheckBox = (CheckBox) findViewById(R.id.indexfingerCheckBox);
        middlefingerCheckBox = (CheckBox) findViewById(R.id.middlefingerCheckBox);
        ringfingerCheckBox = (CheckBox) findViewById(R.id.ringfingerCheckBox);
        littlefingerCheckBox = (CheckBox) findViewById(R.id.littlefingerCheckBox);
        gripTypesAllowed = new Boolean[DEFAULT_GRIPTYPES_ALLOWED.length];

        fillSwitch = (Switch) findViewById(R.id.fillGripTypesSwitch);
        sortSwitch = (Switch) findViewById(R.id.sortHoldsSwitch);


       minDifficultyEditText = (EditText) findViewById(R.id.minDifficultyEditText);
       maxDifficultyEditText = (EditText) findViewById(R.id.maxDifficultyEditText);

       minDifficultySeekBar = (SeekBar) findViewById(R.id.minDifficultySeekBar);
       maxDifficultySeekBar = (SeekBar) findViewById(R.id.maxDifficultySeekBar);

        resetButton = (Button) findViewById(R.id.resetButton);
        backButton = (Button) findViewById(R.id.backButton);

       filterSettings = PreferenceManager.getDefaultSharedPreferences(this);
/*

        SharedPreferences.Editor editor = filterSettings.edit();
        for (int i = 0 ; i < gripTypesAllowed.length ; i++) {
            editor.putBoolean("gripType_"+i+"_Filter",gripTypesAllowed[i]);
        }
        editor.apply();

*/

        fillSwitch.setChecked(filterSettings.getBoolean("fillGripTypesFilter",DEFAULT_USE_EVERY_GRIP));
        sortSwitch.setChecked(filterSettings.getBoolean("sortWorkoutHoldsFilter",DEFAULT_SORT_HOLDS));

        fillSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                SharedPreferences.Editor editor = filterSettings.edit();
                editor.putBoolean("fillGripTypesFilter",b );
                editor.apply();
            }
        });

        sortSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                SharedPreferences.Editor editor = filterSettings.edit();
                editor.putBoolean("sortWorkoutHoldsFilter",b);
                editor.apply();
            }
        });

        for (int i = 0 ; i < gripTypesAllowed.length ; i++) {
            gripTypesAllowed[i] = filterSettings.getBoolean("gripType_"+i+"_Filter",DEFAULT_GRIPTYPES_ALLOWED[i]);
        }

        fourfingerCheckBox.setChecked(gripTypesAllowed[0]);
        threefrontCheckBox.setChecked(gripTypesAllowed[1]);
        threebackCheckBox.setChecked(gripTypesAllowed[2]);
        twofrontCheckBox.setChecked(gripTypesAllowed[3]);
        twomiddleCheckBox.setChecked(gripTypesAllowed[4]);
        twobackCheckBox.setChecked(gripTypesAllowed[5]);
        indexfingerCheckBox.setChecked(gripTypesAllowed[6]);
        middlefingerCheckBox.setChecked(gripTypesAllowed[7]);
        ringfingerCheckBox.setChecked(gripTypesAllowed[8]);
        littlefingerCheckBox.setChecked(gripTypesAllowed[9]);

        fourfingerCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                gripTypesAllowed[0] = b;
            }
        });

        threefrontCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                gripTypesAllowed[1] = b;
            }
        });

        threebackCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                gripTypesAllowed[2] = b;
            }
        });

        twofrontCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                gripTypesAllowed[3] = b;
            }
        });

        twomiddleCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                gripTypesAllowed[4] = b;
            }
        });

        twobackCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                gripTypesAllowed[5] = b;
            }
        });

        indexfingerCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                gripTypesAllowed[6] = b;
            }
        });

        middlefingerCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                gripTypesAllowed[7] = b;
            }
        });

        ringfingerCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                gripTypesAllowed[8] = b;
            }
        });

        littlefingerCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                gripTypesAllowed[9] = b;
            }
        });
        final int minDifficulty = filterSettings.getInt("minDifficultyFilter",DEFAULT_MIN_DIFFICULTY);
       int maxDifficulty = filterSettings.getInt("maxDifficultyFilter",DEFAULT_MAX_DIFFICULTY);

       minDifficultySeekBar.setProgress(minDifficulty/5);
       maxDifficultySeekBar.setProgress(maxDifficulty/5);
       minDifficultyEditText.setText(""+minDifficulty);
       maxDifficultyEditText.setText(""+maxDifficulty);

       minDifficultySeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
           @Override
           public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
           }

           @Override
           public void onStartTrackingTouch(SeekBar seekBar) {

           }

           @Override
           public void onStopTrackingTouch(SeekBar seekBar) {
               int progress = seekBar.getProgress();
               int max = filterSettings.getInt("maxDifficultyFilter",DEFAULT_MAX_DIFFICULTY);
               int min = filterSettings.getInt("minDifficultyFilter",DEFAULT_MIN_DIFFICULTY);

               if (progress*5 < max) {
                   minDifficultyEditText.setText("" + seekBar.getProgress() * 5);
                   SharedPreferences.Editor editor = filterSettings.edit();
                   editor.putInt("minDifficultyFilter", seekBar.getProgress() * 5);
                   editor.apply();
               } else {
                   minDifficultySeekBar.setProgress(min/5);
                   Toast.makeText(FilterActivity.this,"minimum value should be smaller than maximum value, changes reverted",Toast.LENGTH_SHORT).show();
               }

           }
       });

       maxDifficultySeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
           @Override
           public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
           }

           @Override
           public void onStartTrackingTouch(SeekBar seekBar) {

           }

           @Override
           public void onStopTrackingTouch(SeekBar seekBar) {

               int progress = seekBar.getProgress();
               int max = filterSettings.getInt("maxDifficultyFilter",DEFAULT_MAX_DIFFICULTY);
               int min = filterSettings.getInt("minDifficultyFilter",DEFAULT_MIN_DIFFICULTY);

               if (progress*5 > min) {
                   maxDifficultyEditText.setText("" + seekBar.getProgress() * 5);
                   SharedPreferences.Editor editor = filterSettings.edit();
                   editor.putInt("maxDifficultyFilter", seekBar.getProgress() * 5);
                   editor.apply();
               } else {
                    maxDifficultySeekBar.setProgress(max/5);
                   Toast.makeText(FilterActivity.this,"maximum value should be bigger than minimum value, changes reverted",Toast.LENGTH_SHORT).show();
               }
           }
       });

       backButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {

               SharedPreferences.Editor editor = filterSettings.edit();
               for (int i = 0 ; i < gripTypesAllowed.length ; i++) {
                   editor.putBoolean("gripType_"+i+"_Filter",gripTypesAllowed[i]);
                   // Log.d("TEST"," " + gripTypesAllowed[i]);
               }
               editor.apply();

               finish();
           }
       });

       resetButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Toast.makeText(FilterActivity.this,"reset to defaults",Toast.LENGTH_LONG).show();
           }
       });

       minDifficultyEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
           @Override
           public boolean onEditorAction(TextView textView, int actionID, KeyEvent keyEvent) {
               int max = filterSettings.getInt("maxDifficultyFilter",DEFAULT_MAX_DIFFICULTY);
               int min = filterSettings.getInt("minDifficultyFilter",DEFAULT_MIN_DIFFICULTY);
               try {
                   int i = Integer.parseInt(minDifficultyEditText.getText().toString());

                   if (i >= 0 && i < 1000 && i < max) {
                       SharedPreferences.Editor editor = filterSettings.edit();
                       editor.putInt("minDifficultyFilter",i);
                       editor.apply();
                       minDifficultySeekBar.setProgress(i/5);
                   }
                   else if (i >= max) {
                       minDifficultyEditText.setText(""+min);
                       Toast.makeText(FilterActivity.this,"minimum value should be smaller that max value, changes reverted",Toast.LENGTH_SHORT).show();
                   }
                   else {
                       minDifficultyEditText.setText(""+min);
                       Toast.makeText(FilterActivity.this,"Number out of bounds, changes reverted",Toast.LENGTH_SHORT).show();
                   }
               } catch (NumberFormatException nfe) {
                   minDifficultyEditText.setText(""+min);
                   Toast.makeText(FilterActivity.this,"Illegal number, changes reverted" ,Toast.LENGTH_LONG).show();
               }
               return false;
           }
       });
       maxDifficultyEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
           @Override
           public boolean onEditorAction(TextView textView, int actionID, KeyEvent keyEvent) {
               int max = filterSettings.getInt("maxDifficultyFilter",DEFAULT_MAX_DIFFICULTY);
               int min = filterSettings.getInt("minDifficultyFilter",DEFAULT_MIN_DIFFICULTY);
               try {
                   int i = Integer.parseInt(maxDifficultyEditText.getText().toString() );

                   if (i > 0 && i < 10000 && i > min) {
                       SharedPreferences.Editor editor = filterSettings.edit();
                       editor.putInt("maxDifficultyFilter",i);
                       editor.apply();
                       maxDifficultySeekBar.setProgress(i/5);
                   }
                   else if (i <= min) {
                       maxDifficultyEditText.setText(""+max);
                       Toast.makeText(FilterActivity.this,"maximum value should be bigger than minimum value, changes reverted",Toast.LENGTH_SHORT).show();
                   }
                   else {
                       maxDifficultyEditText.setText(""+max);
                       Toast.makeText(FilterActivity.this,"Number out of bounds, changes reverted",Toast.LENGTH_SHORT).show();
                   }
               } catch (NumberFormatException nfe) {
                   maxDifficultyEditText.setText(""+max);
                   Toast.makeText(FilterActivity.this,"Illegal number, changes reverted" ,Toast.LENGTH_LONG).show();
               }
               return false;
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
