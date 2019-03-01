package com.finn.laakso.hangboardapp;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

// BenchmarkActivity shows premade workouts from Strings.xml to user.User can see workout's parameters
// and compare them to other workouts. User can select the workout that he/she wants to do
public class BenchmarkActivity extends AppCompatActivity {

    private BenchmarkWorkoutsAdapter workoutsAdapter;
    private BenchmarkHangboardAdapter hangboardAdapter;

    private Button copyBenchmarkButton;
    private CheckBox randomizeGripsCheckBox;

    private ListView hangboardNamesListView;
    private ListView benchmarksListView;
    private TextView benchmarkInfoTextView;
    private TextView animationTextView;

    // These variables comes from MainActivity. BenchmarkActivity provides a way to examine MainActivity's
    // workout parameters
    TimeControls mainTimeControls;
    ArrayList<Hold> mainWorkoutHolds;
    String mainHangboard;
    String benchmarkInfo;

    private int hangboardPosition = 0;
    private int selectedBenchmark = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().hide();
        setContentView(R.layout.activity_benchmark);

        if (PreferenceManager.getDefaultSharedPreferences(this).getBoolean("helpSwitch",true)) {
            Toast.makeText(BenchmarkActivity.this, "Pre made workouts (beta test)", Toast.LENGTH_SHORT).show();
        }

        // Workout variables that comes from MainActivity, these are showsn when correct hangboard
        // is selected and no pre made workouts is selected.
        if (getIntent().hasExtra("com.finn.laakso.hangboardapp.HANGBOARDNAME")) {
            mainHangboard = getIntent().getStringExtra("com.finn.laakso.hangboardapp.HANGBOARDNAME");
            hangboardPosition = HangboardResources.getHangboardPosition(mainHangboard);
        }
        mainTimeControls = new TimeControls();
        benchmarkInfo = "";
        if (getIntent().hasExtra("com.finn.laakso.hangboardapp.HOLDS") &&
        getIntent().hasExtra("com.finn.laakso.hangboardapp.TIMECONTROLS")) {
            mainTimeControls.setTimeControls(getIntent().getIntArrayExtra("com.finn.laakso.hangboardapp.TIMECONTROLS") );
            mainWorkoutHolds = getIntent().getParcelableArrayListExtra("com.finn.laakso.hangboardapp.HOLDS");
            benchmarkInfo = BenchmarkWorkoutsAdapter.getBenchmarkInfo(mainTimeControls,mainWorkoutHolds);
        }


        hangboardNamesListView = findViewById(R.id.hangboardsListView);
        benchmarksListView = findViewById(R.id.benchmarksListView);
        benchmarkInfoTextView = findViewById(R.id.benchmarkInfoTextView);
        copyBenchmarkButton = findViewById(R.id.copyBenchmarkWorkout);
        randomizeGripsCheckBox = findViewById(R.id.randomizeGrips);
        animationTextView = findViewById(R.id.animationTextView);
        animationTextView.setVisibility(View.INVISIBLE);

        hangboardAdapter = new BenchmarkHangboardAdapter(this);
        hangboardNamesListView.setAdapter(hangboardAdapter);

        // All the premade workouts in a huge string, parced in BenchmarkWorkoutsAdapter
        Resources res = getResources();
        String[] benchmarkResources = res.getStringArray(HangboardResources.getBenchmarkResources(hangboardPosition));

        workoutsAdapter = new BenchmarkWorkoutsAdapter(BenchmarkActivity.this,hangboardPosition,benchmarkResources);
        benchmarksListView.setAdapter(workoutsAdapter);

        hangboardAdapter.setSelectedHangboard(hangboardPosition);

        // When hangboard is selected, those hangboard's pre made workouts are shown
        hangboardNamesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                // do nothing if already selected and no premade workouts selected
                if (hangboardPosition == i && selectedBenchmark == -1) {
                    return;
                }

                // retrieve, parce and show pre made workouts
                Resources res = getResources();
                String[] benchmarkResources = res.getStringArray(HangboardResources.getBenchmarkResources(i));
                workoutsAdapter = new BenchmarkWorkoutsAdapter(BenchmarkActivity.this,i,benchmarkResources);
                benchmarksListView.setAdapter(workoutsAdapter);

                hangboardAdapter.setSelectedHangboard(i);

                if (i != hangboardPosition) {
                    hangboardAdapter.notifyDataSetChanged();
                }


                // If user selects the same hangboard that was in mainActivity, lets show that
                // workout's info in benchmarkInfoTextview
                if (hangboardAdapter.getHangboardName(i).equals(mainHangboard) ) {

                    if (benchmarkInfoTextView.getVisibility() == View.INVISIBLE) {
                        popupTextViewAnimation();
                        benchmarkInfoTextView.setVisibility(View.VISIBLE);
                    }
                    benchmarkInfoTextView.setText(benchmarkInfo);

                    //Only show change if it came from same hangboard
                    if (hangboardPosition == i && selectedBenchmark != -1) {
                        String animationText = workoutsAdapter.getAnimationInfo(workoutsAdapter.getWorkoutTimeControls(i),
                                workoutsAdapter.getWorkoutHolds(i), mainTimeControls, mainWorkoutHolds);
                        animationTextView.setText(animationText);
                        benchmarkDifferenceAnimation();
                    }
                }

                // else we hide the textview
                else if (benchmarkInfoTextView.getVisibility() == View.VISIBLE) {
                    hideTextViewAnimation();
                    benchmarkInfoTextView.setVisibility(View.INVISIBLE);
                    animationTextView.setVisibility(View.INVISIBLE);

                }
                hangboardPosition = i;
                selectedBenchmark = -1;

            }
        });

        // benchmarkInfoTextView shows all kind of interesting info from pre made workout
        benchmarkInfoTextView.setText(benchmarkInfo);
        benchmarksListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                if (selectedBenchmark == i) {
                    return;
                }

                // lets check if we are comparing two pre made workouts or workout that came from MainActivity
                // or if textView was not visible, we are not comparing to anything
                String animationChangeText="";
                if (benchmarkInfoTextView.getVisibility() == View.VISIBLE) {
                    if (selectedBenchmark != -1) {
                       // animationChangeText = workoutsAdapter.getAnimationInfoText(selectedBenchmark, i);

                        animationChangeText = workoutsAdapter.getAnimationInfo(workoutsAdapter.getWorkoutTimeControls(selectedBenchmark),
                                workoutsAdapter.getWorkoutHolds(selectedBenchmark),
                                workoutsAdapter.getWorkoutTimeControls(i), workoutsAdapter.getWorkoutHolds(i));

                    } else {
                        animationChangeText = workoutsAdapter.getAnimationInfo(mainTimeControls, mainWorkoutHolds
                                , workoutsAdapter.getWorkoutTimeControls(i), workoutsAdapter.getWorkoutHolds(i));
                    }
                }

                benchmarkInfoTextView.setVisibility(View.VISIBLE);
                selectedBenchmark = i;

                String benchmarkInfo = workoutsAdapter.getBenchmarkInfoText(selectedBenchmark);
                animationTextView.setText(animationChangeText);

                benchmarkDifferenceAnimation();
                benchmarkInfoTextView.setText(benchmarkInfo);

            }
        });

        // Transfer workout info from BenchmarkWorkoutsAdapter to MainActivity
        copyBenchmarkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedBenchmark == -1) {
                    Toast.makeText(BenchmarkActivity.this, "Select a workout to copy", Toast.LENGTH_SHORT).show();
                    return;
                }

                String benchmarkHangboard = hangboardAdapter.getHangboardName(hangboardPosition);
                TimeControls benchmarkTimeControls = workoutsAdapter.getWorkoutTimeControls(selectedBenchmark);
                ArrayList<Hold> benchmarkWorkoutHolds = workoutsAdapter.getWorkoutHolds(selectedBenchmark);

                // When doing the same benchmark workout over and over again, it is nice that hold are
                // not always in the same order
                if (randomizeGripsCheckBox.isChecked() ) {

                    int index;
                    Random random = new Random();
                    int totalGrips = benchmarkWorkoutHolds.size()/2;

                    ArrayList<Hold> randomizedHolds = new ArrayList<>();

                    for (int rng = totalGrips - 1; rng >= 0; rng--)
                    {
                        index = random.nextInt(rng + 1);

                        randomizedHolds.add( benchmarkWorkoutHolds.get(2*index) );
                        benchmarkWorkoutHolds.remove(2*index);

                        randomizedHolds.add( benchmarkWorkoutHolds.get(2*index) );
                        benchmarkWorkoutHolds.remove(2*index);
                    }

                    Intent resultCopyBenchmarkIntent = new Intent();
                    resultCopyBenchmarkIntent.putExtra("com.finn.laakso.hangboardapp.BOARDNAME", benchmarkHangboard);
                    resultCopyBenchmarkIntent.putExtra("com.finn.laakso.hangboardapp.SETTINGS", benchmarkTimeControls.getTimeControlsIntArray());
                    resultCopyBenchmarkIntent.putParcelableArrayListExtra("com.finn.laakso.hangboardapp.HOLDS", randomizedHolds);

                    setResult(Activity.RESULT_OK, resultCopyBenchmarkIntent);

                    finish();

                }
                else {

                    Intent resultCopyBenchmarkIntent = new Intent();
                    resultCopyBenchmarkIntent.putExtra("com.finn.laakso.hangboardapp.BOARDNAME", benchmarkHangboard);
                    resultCopyBenchmarkIntent.putExtra("com.finn.laakso.hangboardapp.SETTINGS", benchmarkTimeControls.getTimeControlsIntArray());
                    resultCopyBenchmarkIntent.putParcelableArrayListExtra("com.finn.laakso.hangboardapp.HOLDS", benchmarkWorkoutHolds);

                    setResult(Activity.RESULT_OK, resultCopyBenchmarkIntent);

                    finish();
                }
            }
        });
    }

    private void hideTextViewAnimation() {
        Animation a = AnimationUtils.loadAnimation(this, R.anim.hide_benchmark_textview);
        a.reset();
        TextView tv = (TextView) findViewById(R.id.benchmarkInfoTextView);
        tv.clearAnimation();
        tv.startAnimation(a);
    }

    private void popupTextViewAnimation()
    {
        Animation a = AnimationUtils.loadAnimation(this, R.anim.popup_benchmark_textview);
        a.reset();
        TextView tv = (TextView) findViewById(R.id.benchmarkInfoTextView);
        tv.clearAnimation();
        tv.startAnimation(a);
    }

    private void benchmarkDifferenceAnimation()
    {
        Animation a = AnimationUtils.loadAnimation(this, R.anim.animate_benchmark_difference);
        a.reset();
        TextView tv = (TextView) findViewById(R.id.animationTextView);
        tv.clearAnimation();
        tv.startAnimation(a);
    }


}
