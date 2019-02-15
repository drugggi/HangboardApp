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

public class BenchmarkActivity extends AppCompatActivity {

    private BenchmarkWorkoutsAdapter workoutsAdapter;
    private BenchmarkHangboardAdapter hangboardAdapter;

    private Button copyBenchmarkButton;
    private CheckBox randomizeGripsCheckBox;

    private ListView hangboardNamesListView;
    private ListView benchmarksListView;
    private TextView benchmarkInfoTextView;
    private TextView animationTextView;

    private String[] hangboardNames;

    //private ArrayList<String> benchmarkDescriptions;
    //private ArrayList<TimeControls> benchmarkTimeControls;
    //private ArrayList<ArrayList<Hold>> benchmarkWorkoutHolds;

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
        hangboardNamesListView = findViewById(R.id.hangboardsListView);
        benchmarksListView = findViewById(R.id.benchmarksListView);
        benchmarkInfoTextView = findViewById(R.id.benchmarkInfoTextView);
        copyBenchmarkButton = findViewById(R.id.copyBenchmarkWorkout);
        randomizeGripsCheckBox = findViewById(R.id.randomizeGrips);
        animationTextView = findViewById(R.id.animationTextView);

        benchmarkInfoTextView.setVisibility(View.INVISIBLE);
        animationTextView.setVisibility(View.INVISIBLE);


       //  parceBenchmarkPrograms(hangboardPosition);

        hangboardAdapter = new BenchmarkHangboardAdapter(this);
        hangboardNamesListView.setAdapter(hangboardAdapter);

        Resources res = getResources();
        String[] benchmarkResources = res.getStringArray(HangboardResources.getBenchmarkResources(hangboardPosition));

        workoutsAdapter = new BenchmarkWorkoutsAdapter(BenchmarkActivity.this,hangboardPosition,benchmarkResources);
        benchmarksListView.setAdapter(workoutsAdapter);

        hangboardAdapter.setSelectedHangboard(hangboardPosition);

        hangboardNamesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                selectedBenchmark = -1;
                if (i == hangboardPosition) {
                    return;
                }

                hangboardPosition = i;
                // parceBenchmarkPrograms(hangboardPosition);

                Resources res = getResources();
                String[] benchmarkResources = res.getStringArray(HangboardResources.getBenchmarkResources(hangboardPosition));

                workoutsAdapter = new BenchmarkWorkoutsAdapter(BenchmarkActivity.this,hangboardPosition,benchmarkResources);
                benchmarksListView.setAdapter(workoutsAdapter);

                hangboardAdapter.setSelectedHangboard(hangboardPosition);
                hangboardAdapter.notifyDataSetChanged();


                if (benchmarkInfoTextView.getVisibility() == View.VISIBLE) {
                    hideTextViewAnimation();
                    benchmarkInfoTextView.setVisibility(View.INVISIBLE);
                }
                animationTextView.setVisibility(View.INVISIBLE);
                // benchmarksAdapter = new ArrayAdapter<String>(BenchmarkActivity.this,android.R.layout.simple_list_item_1,benchmarkDescriptions);
                // benchmarksListView.setAdapter(benchmarksAdapter);

            }
        });
//        Resources res = getResources();

        // String[] benchmarkResources = res.getStringArray(HangboardResources.getBenchmarkResources(hangboardPosition));

        //workoutsAdapter = new BenchmarkWorkoutsAdapter(this,0,benchmarkResources);
        //benchmarksListView.setAdapter(workoutsAdapter);
        // registerForContextMenu(benchmarksListView);

        // String benchmarkInfo = workoutsAdapter.getBenchmarkInfoText(selectedBenchmark);
        benchmarkInfoTextView.setText("");

        benchmarksListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                // RunAnimation2();
                // animationText needs to know former selected Benchmark position

                if (selectedBenchmark == i) {
                    return;
                }


                benchmarkInfoTextView.setVisibility(View.VISIBLE);
                String animationChangeText;
                if (selectedBenchmark != -1) {
                    animationChangeText = workoutsAdapter.getAnimationInfoText(selectedBenchmark, i);
                   // animationTextView.setVisibility(View.VISIBLE);
                } else {
                    animationChangeText = "";
                    popupTextViewAnimation();

                }
                selectedBenchmark = i;

                String benchmarkInfo = workoutsAdapter.getBenchmarkInfoText(selectedBenchmark);

                animationTextView.setText(animationChangeText);


                benchmarkDifferenceAnimation();

                benchmarkInfoTextView.setText(benchmarkInfo);
                // Animation animation = AnimationUtils.loadAnimation()

                // changeBenchmarkInfoText();


            }
        });

        copyBenchmarkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedBenchmark == -1) {
                    Toast.makeText(BenchmarkActivity.this, "Select a workout to copy", Toast.LENGTH_SHORT).show();
                    return;
                }


                int i = selectedBenchmark;

                String benchmarkHangboard = hangboardAdapter.getHangboardName(hangboardPosition);
                TimeControls benchmarkTimeControls = workoutsAdapter.getWorkoutTimeControls(selectedBenchmark);
                ArrayList<Hold> benchmarkWorkoutHolds = workoutsAdapter.getWorkoutHolds(selectedBenchmark);

                if (randomizeGripsCheckBox.isChecked() ) {

                    int index;
                    Random random = new Random();
                    int totalGrips = benchmarkWorkoutHolds.size()/2;
                    // Log.d("totalGrips",": " + totalGrips);

                    ArrayList<Hold> randomizedHolds = new ArrayList<>();

                    for (int rng = totalGrips - 1; rng >= 0; rng--)
                    {
                        index = random.nextInt(rng + 1);

                        randomizedHolds.add( benchmarkWorkoutHolds.get(2*index) );
                        benchmarkWorkoutHolds.remove(2*index);

                        randomizedHolds.add( benchmarkWorkoutHolds.get(2*index) );
                        benchmarkWorkoutHolds.remove(2*index);

                    }
                        // Log.d("RNGHOLD","SIZE: " + randomizedHolds.size() + "  BMSIZE " + benchmarkWorkoutHolds.size() ) ;
                    for (int j = 0 ; j < randomizedHolds.size() ; j = j+2) {
                       // Log.d("HOLDS",randomizedHolds.get(j).getHoldInfo( randomizedHolds.get(j+1) ).replace("\n"," ") );
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
/*
    private void changeBenchmarkInfoText() {

        TimeControls tempControls = benchmarkTimeControls.get(selectedBenchmark);

        int gripLaps = tempControls.getGripLaps();
        int sets = tempControls.getRoutineLaps();
        int hangs = tempControls.getHangLaps();

        int[] tempCompleted = new int[gripLaps * sets];

        for (int i = 0 ; i < tempCompleted.length ;  i++) {
            tempCompleted[i] = hangs;
        }

        CalculateWorkoutDetails benchmarkDetails = new CalculateWorkoutDetails(tempControls,
                benchmarkWorkoutHolds.get(selectedBenchmark),tempCompleted);

        String benchmarkInfo = "";
        String repeaters;
        if (tempControls.isRepeaters() ) { repeaters = "Repeaters"; }
        else {repeaters = "Single hangs"; }

        String intensity = "0." + (int)(100 * benchmarkDetails.getIntensity());
        String workoutPower = (int) benchmarkDetails.getWorkoutPower() + ".";
        workoutPower += (int) (100 * (benchmarkDetails.getWorkoutPower() - (int) benchmarkDetails.getWorkoutPower() ) );

        String workload = "" + (int) benchmarkDetails.getWorkload();

        benchmarkInfo += repeaters + "\n";
        benchmarkInfo += "Total time: " + tempControls.getTotalTime()/60 + "min\n";
        benchmarkInfo += "TUT: " + tempControls.getTimeUnderTension() + "s\n";
        benchmarkInfo += "Intensity: " + intensity +"\n";
        benchmarkInfo += "avg Difficulty: " + (int)benchmarkDetails.getAverageDifficutly() + "\n";
        benchmarkInfo += "Power: " + workoutPower + "\n";
        benchmarkInfo += "Workload: " + workload + "\n";
        benchmarkInfo += "Time Controls: \n" + tempControls.getTimeControlsAsJSONGString();

        benchmarkInfoTextView.setText(benchmarkInfo );


    }*/

/*    private void parceBenchmarkPrograms(int selectedHangboardPosition) {

        hangboardNames = HangboardResources.getHangboardNames();

        benchmarkDescriptions = new ArrayList<>();
        benchmarkTimeControls = new ArrayList<>();
        benchmarkWorkoutHolds = new ArrayList<>();

        Resources res = getResources();

        String[] allBenchmarks = res.getStringArray(HangboardResources.getBenchmarkResources(selectedHangboardPosition));
        // currentHangboard = new Hangboard(res,HangboardResources.getHangboardName(0));
        // currentHangboard.setGripAmount;


        for (int i = 0 ; i < allBenchmarks.length ; i++) {
            benchmarkDescriptions.add(allBenchmarks[i]);

            i++;
            TimeControls tempControls = new TimeControls();
            tempControls.setTimeControlsFromString(allBenchmarks[i]);
            benchmarkTimeControls.add(tempControls);

            i++;
            ArrayList<Hold> tempWorkoutHolds = new ArrayList<>();
            int[] tempHoldNumbers = parceStringToInt(allBenchmarks[i]);
            i++;
            int[] tempHoldGripTypes = parceStringToInt(allBenchmarks[i]);


            if (tempControls.getGripLaps()*2 != tempHoldNumbers.length ||
                    tempControls.getGripLaps()*2 != tempHoldGripTypes.length ) {

                Log.e("ERR","timecontrols griplaps different size than needed workoutholds at: " + i);
                Toast.makeText(this,"ERROr PARCE SIZES",Toast.LENGTH_LONG).show();
               //Log.d("DESC",benchmarkDescriptions.get(i));
                Log.d("time controls",tempControls.getTimeControlsAsJSONGString() );
                Log.d("Hold numbers",": " + tempHoldNumbers.length);
                Log.d("grip types",": " + tempHoldGripTypes.length);

                StringBuilder griptypes = new StringBuilder();
                for (int j = 0 ; j < tempHoldNumbers.length ; j++) {
                    griptypes.append("1,");
                }
                Log.d("wanted grip types",griptypes.toString() );

            }

            Hold tempHold;
            for (int j = 0 ; j < tempHoldNumbers.length ; j++ ) {

                tempHold = new Hold(tempHoldNumbers[j] );
                tempHold.setGripType(tempHoldGripTypes[j] );

                int holdDifficulty = HangboardResources.getHoldDifficulty(tempHold,hangboardNames[selectedHangboardPosition] );
                tempHold.setHoldValue(holdDifficulty);

                tempWorkoutHolds.add(tempHold);
            }

            benchmarkWorkoutHolds.add(tempWorkoutHolds);

          //   Log.d("benchmark","data line: " + i + " = " +allBenchmarks[i]);
        }

    }*/
/*

    public static int[] parceStringToInt(String arrayIntLine) {
        String[] parcedCompletedHangs = arrayIntLine.split(",");

        int[] completed = new int[parcedCompletedHangs.length];

        for (int i = 0; i < parcedCompletedHangs.length; i++) {
            completed[i] = Integer.parseInt(parcedCompletedHangs[i]);
        }

        return completed;
    }
*/


}
