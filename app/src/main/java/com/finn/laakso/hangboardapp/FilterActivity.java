package com.finn.laakso.hangboardapp;

import android.content.SharedPreferences;
import android.content.res.Resources;
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
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class FilterActivity extends AppCompatActivity {

    public static final int DEFAULT_MIN_DIFFICULTY = 0;
    public static final int DEFAULT_MAX_DIFFICULTY = 20;
    public static final Boolean[] DEFAULT_GRIPTYPES_ALLOWED = {true, true, true, true, true, true, true, true, true, true};
    public static final boolean DEFAULT_USE_EVERY_GRIP = true;
    public static final boolean DEFAULT_SORT_HOLDS = false;
    public static final int DEFAULT_ALTERNATE_FACTOR = 2;
    public static final int DEFAULT_SORT_ORDER = 0;
    public static final int DEFAULT_SORT_METHOD = 0;
    private static final int ASCENDING = 0, DESCENDING = 1;
    private static final int DIFFICULTY = 0, GRIPTYPE = 1, HOLDNUMBER = 2;


    private Hangboard exampleBoard;
    private String hangboardName;
    private ImageView hangboardImageView;
    private TextView holdsFoundTextView;

    private EditText minDifficultyEditText;
    private EditText maxDifficultyEditText;
    private EditText alternateFactorEditText;

    private SeekBar minDifficultySeekBar;
    private SeekBar maxDifficultySeekBar;
    private SeekBar alternateFactorSeekBar;

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

    private RadioGroup orderRadioGroup;
    private RadioGroup byWhatRadiogroup;
    private Button resetButton;
    private Button backButton;

    private SharedPreferences filterSettings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        hangboardImageView = (ImageView) findViewById(R.id.hangboardImageView);
        holdsFoundTextView = (TextView) findViewById(R.id.holdsFoundTextView);

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
       alternateFactorEditText = (EditText) findViewById(R.id.alternateFactorEditText);

       minDifficultySeekBar = (SeekBar) findViewById(R.id.minDifficultySeekBar);
       maxDifficultySeekBar = (SeekBar) findViewById(R.id.maxDifficultySeekBar);
       alternateFactorSeekBar = (SeekBar) findViewById(R.id.alternateFactorSeekBar);

       orderRadioGroup = (RadioGroup) findViewById(R.id.orderRadioGroup);
       byWhatRadiogroup = (RadioGroup) findViewById(R.id.byWhatRadioGroup);

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

        if (getIntent().hasExtra("com.finn.laakso.hangboardapp.BOARDIMAGE")) {
            int hangboardRes = getIntent().getIntExtra("com.finn.laakso.hangboardapp.BOARDIMAGE",0);
            hangboardName = HangboardResources.getHangboardStringName(hangboardRes);
            hangboardImageView.setImageResource(hangboardRes);

            // there has to be a better way to get new Hangboard(..)
            int pos = HangboardResources.getHangboardPosition(hangboardName);
            HangboardResources.hangboardName hb= HangboardResources.getHangboardName(pos);
            Resources res = getResources();
            exampleBoard = new Hangboard(res,hb);
        }
        fillSwitch.setChecked(filterSettings.getBoolean("fillGripTypesFilter",DEFAULT_USE_EVERY_GRIP));
        boolean sorting = filterSettings.getBoolean("sortWorkoutHoldsFilter",DEFAULT_SORT_HOLDS);
        sortSwitch.setChecked(sorting);
        setRadioButtonsEnabled(sorting);

        RadioButton temp = (RadioButton) findViewById(R.id.ascendingRadioButton);
        switch (filterSettings.getInt("sortOrderFilter",DEFAULT_SORT_ORDER) ) {
            case (ASCENDING):
                temp = (RadioButton) findViewById(R.id.ascendingRadioButton);
                break;
            case (DESCENDING):
                temp = (RadioButton) findViewById(R.id.descendingRadioButton);
                break;
        }

        temp.setChecked(true);
        switch (filterSettings.getInt("sortMethodFilter",DEFAULT_SORT_METHOD )) {
            case (DIFFICULTY):
                temp = (RadioButton) findViewById(R.id.difficultyRadioButton);
                break;
            case (GRIPTYPE):
                temp = (RadioButton) findViewById(R.id.griptypeRadioButton);
                break;
            case (HOLDNUMBER):
                temp = (RadioButton) findViewById(R.id.holdnumberRadioButton);
                break;
        }
        temp.setChecked(true);
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
                setRadioButtonsEnabled(b);
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

        int minDifficulty = filterSettings.getInt("minDifficultyFilter",DEFAULT_MIN_DIFFICULTY);
       int maxDifficulty = filterSettings.getInt("maxDifficultyFilter",DEFAULT_MAX_DIFFICULTY);
       int alternateFactor = filterSettings.getInt("alternateFactorFilter",DEFAULT_ALTERNATE_FACTOR);

       minDifficultySeekBar.setProgress(minDifficulty/5);
       maxDifficultySeekBar.setProgress(maxDifficulty/5);
        alternateFactorSeekBar.setProgress(alternateFactor);
       minDifficultyEditText.setText(""+minDifficulty);
       maxDifficultyEditText.setText(""+maxDifficulty);
       alternateFactorEditText.setText(""+alternateFactor);


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
                   updateFilterDisplay();
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
                   maxDifficultyEditText.setText("" + progress * 5);
                   SharedPreferences.Editor editor = filterSettings.edit();
                   editor.putInt("maxDifficultyFilter", seekBar.getProgress() * 5);
                   editor.apply();
                   updateFilterDisplay();
               } else {
                    maxDifficultySeekBar.setProgress(max/5);
                   Toast.makeText(FilterActivity.this,"maximum value should be bigger than minimum value, changes reverted",Toast.LENGTH_SHORT).show();
               }
           }
       });

       alternateFactorSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
           @Override
           public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

           }

           @Override
           public void onStartTrackingTouch(SeekBar seekBar) {

           }

           @Override
           public void onStopTrackingTouch(SeekBar seekBar) {
               int progress = seekBar.getProgress();

               alternateFactorEditText.setText(""+progress);
               SharedPreferences.Editor editor = filterSettings.edit();
               editor.putInt("alternateFactorFilter",progress);
               editor.apply();
               updateFilterDisplay();         }
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

       orderRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
           @Override
           public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
               SharedPreferences.Editor editor = filterSettings.edit();

               if (checkedId == R.id.ascendingRadioButton) {
                   editor.putInt("sortOrderFilter",ASCENDING);
               } else if (checkedId == R.id.descendingRadioButton) {
                   editor.putInt("sortOrderFilter",DESCENDING);
               }
               editor.apply();
           }
       });
       byWhatRadiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
           @Override
           public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
               SharedPreferences.Editor editor = filterSettings.edit();

               if (checkedId == R.id.difficultyRadioButton) {
                    editor.putInt("sortMethodFilter",DIFFICULTY);
               } else if (checkedId == R.id.griptypeRadioButton) {
                   editor.putInt("sortMethodFilter",GRIPTYPE);
               } else if (checkedId == R.id.holdnumberRadioButton) {
                   editor.putInt("sortMethodFilter",HOLDNUMBER);
               }
               editor.apply();
           }
       });
       resetButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               minDifficultyEditText.setText(""+DEFAULT_MIN_DIFFICULTY);
               minDifficultySeekBar.setProgress(DEFAULT_MIN_DIFFICULTY/5);
               maxDifficultyEditText.setText(""+DEFAULT_MAX_DIFFICULTY);
               maxDifficultySeekBar.setProgress(DEFAULT_MAX_DIFFICULTY/5);
               alternateFactorEditText.setText("" + DEFAULT_ALTERNATE_FACTOR);
               alternateFactorSeekBar.setProgress(DEFAULT_ALTERNATE_FACTOR);

               fourfingerCheckBox.setChecked(DEFAULT_GRIPTYPES_ALLOWED[0]);
               threefrontCheckBox.setChecked(DEFAULT_GRIPTYPES_ALLOWED[1]);
               threebackCheckBox.setChecked(DEFAULT_GRIPTYPES_ALLOWED[2]);
               twofrontCheckBox.setChecked(DEFAULT_GRIPTYPES_ALLOWED[3]);
               twomiddleCheckBox.setChecked(DEFAULT_GRIPTYPES_ALLOWED[4]);
               twobackCheckBox.setChecked(DEFAULT_GRIPTYPES_ALLOWED[5]);
               indexfingerCheckBox.setChecked(DEFAULT_GRIPTYPES_ALLOWED[6]);
               middlefingerCheckBox.setChecked(DEFAULT_GRIPTYPES_ALLOWED[7]);
               ringfingerCheckBox.setChecked(DEFAULT_GRIPTYPES_ALLOWED[8]);
               littlefingerCheckBox.setChecked(DEFAULT_GRIPTYPES_ALLOWED[9]);

               fillSwitch.setChecked(DEFAULT_USE_EVERY_GRIP);
               sortSwitch.setChecked(DEFAULT_SORT_HOLDS);
               SharedPreferences.Editor editor = filterSettings.edit();
               editor.putInt("minDifficultyFilter",DEFAULT_MIN_DIFFICULTY);
               editor.putInt("maxDifficultyFilter",DEFAULT_MAX_DIFFICULTY);
               editor.putInt("alternateFactorFilter",DEFAULT_ALTERNATE_FACTOR);
               for (int i = 0 ; i < DEFAULT_GRIPTYPES_ALLOWED.length ; i++) {
                   editor.putBoolean("gripType_"+i+"_Filter",DEFAULT_GRIPTYPES_ALLOWED[i]);
               }
               editor.putBoolean("fillGripTypesFilter",DEFAULT_USE_EVERY_GRIP);
               editor.putBoolean("sortWorkoutHoldsFilter",DEFAULT_SORT_HOLDS);
               editor.putInt("sortOrderFilter",DEFAULT_SORT_ORDER);
               editor.putInt("sortMethodFilter",DEFAULT_SORT_METHOD);

               RadioButton temp = (RadioButton) findViewById(R.id.ascendingRadioButton);
               temp.setChecked(true);
               temp = (RadioButton) findViewById(R.id.difficultyRadioButton);
               temp.setChecked(true);

               editor.apply();
               setRadioButtonsEnabled(DEFAULT_SORT_HOLDS);
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
                       updateFilterDisplay();
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
                       updateFilterDisplay();
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
       alternateFactorEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
           @Override
           public boolean onEditorAction(TextView textView, int actionID, KeyEvent keyEvent) {
               int alternateFactor = filterSettings.getInt("alternateFactorFilter",DEFAULT_ALTERNATE_FACTOR);

               try {
                   int i = Integer.parseInt(alternateFactorEditText.getText().toString() );

                   if (i >= 0 && i <= 10) {
                       SharedPreferences.Editor editor = filterSettings.edit();
                       editor.putInt("alternateFactorFilter",i);
                       editor.apply();
                       alternateFactorSeekBar.setProgress(i);
                       updateFilterDisplay();
                   }
               } catch (NumberFormatException e) {
                   alternateFactorEditText.setText("" + alternateFactor);
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
        updateFilterDisplay();
    }
    private void updateFilterDisplay() {
        ArrayList<Hold> holdsFound;
        ArrayList<Hold> holdsFoundAlternate;
        int max = filterSettings.getInt("maxDifficultyFilter",DEFAULT_MAX_DIFFICULTY);
        int min = filterSettings.getInt("minDifficultyFilter",DEFAULT_MIN_DIFFICULTY);
        int alternateFactor = filterSettings.getInt("alternateFactorFilter",DEFAULT_ALTERNATE_FACTOR);

        holdsFound = exampleBoard.getHoldsInRange(min,max,gripTypesAllowed);
        holdsFoundAlternate = exampleBoard.getAlternateHoldsInRange(min,max,alternateFactor,gripTypesAllowed);
        holdsFoundTextView.setText("Current hangboard: " + hangboardName + "\n" +
        "Different Holds found ("+min + "-"+ max + "): " + holdsFound.size()/2 + "\n" +
                "Holds found (alterante): " + holdsFoundAlternate.size()/2 );
    }
    private void setRadioButtonsEnabled(boolean sortinEnabled) {
        for (int i = 0; i < orderRadioGroup.getChildCount(); i++){
            orderRadioGroup.getChildAt(i).setEnabled(sortinEnabled);
        }
        for (int i = 0; i < byWhatRadiogroup.getChildCount(); i++) {
            byWhatRadiogroup.getChildAt(i).setEnabled(sortinEnabled);
        }
        float alpha = 0.5f;
        if (sortinEnabled) {
            alpha = 1f;
        }
        TextView temp = (TextView) findViewById(R.id.holdsinTextView);
        temp.setAlpha(alpha);
        temp = (TextView) findViewById(R.id.orderbyTextView);
        temp.setAlpha(alpha);
    }
}
