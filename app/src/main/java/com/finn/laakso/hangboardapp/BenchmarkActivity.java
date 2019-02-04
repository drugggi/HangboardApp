package com.finn.laakso.hangboardapp;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

public class BenchmarkActivity extends AppCompatActivity {

    private BenchmarkWorkoutsAdapter benchmarkWorkoutsAdapter;

    private Button copyBenchmarkButton;
    private CheckBox randomizeGripsCheckBox;

    private ListView hangboardNamesListView;
    private ListView benchmarksListView;
    private TextView benchmarkInfoTextView;
    private TextView animationTextView;

    private ListAdapter hangboardNamesAdapter;
    private ListAdapter benchmarksAdapter;

    private String[] hangboardNames;

    private ArrayList<String> benchmarkDescriptions;
    private ArrayList<TimeControls> benchmarkTimeControls;
    private ArrayList<ArrayList<Hold>> benchmarkWorkoutHolds;

    private int hangboardPosition = 0;
    private int selectedBenchmark = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().hide();
        setContentView(R.layout.activity_benchmark);

        Toast.makeText(BenchmarkActivity.this,"Benchmark workouts (beta test)", Toast.LENGTH_SHORT).show();

        hangboardNamesListView = findViewById(R.id.hangboardsListView);
        benchmarksListView = findViewById(R.id.benchmarksListView);
        benchmarkInfoTextView = findViewById(R.id.benchmarkInfoTextView);
        copyBenchmarkButton = findViewById(R.id.copyBenchmarkWorkout);
        randomizeGripsCheckBox = findViewById(R.id.randomizeGrips);
        animationTextView = findViewById(R.id.animationTextView);

        parceBenchmarkPrograms(hangboardPosition);
        hangboardNamesAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, hangboardNames);
        benchmarksAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,benchmarkDescriptions);

        hangboardNamesListView.setAdapter(hangboardNamesAdapter);
        benchmarksListView.setAdapter(benchmarksAdapter);

        hangboardNamesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                hangboardPosition = i;
                selectedBenchmark = 0;
                // parceBenchmarkPrograms(hangboardPosition);

                Resources res = getResources();

                String[] benchmarkResources = res.getStringArray(HangboardResources.getBenchmarkResources(hangboardPosition));

                benchmarkWorkoutsAdapter = new BenchmarkWorkoutsAdapter(BenchmarkActivity.this,hangboardPosition,benchmarkResources);
                benchmarksListView.setAdapter(benchmarkWorkoutsAdapter);

                // benchmarksAdapter = new ArrayAdapter<String>(BenchmarkActivity.this,android.R.layout.simple_list_item_1,benchmarkDescriptions);
                // benchmarksListView.setAdapter(benchmarksAdapter);

            }
        });
        Resources res = getResources();

        String[] benchmarkResources = res.getStringArray(HangboardResources.getBenchmarkResources(hangboardPosition));

        benchmarkWorkoutsAdapter = new BenchmarkWorkoutsAdapter(this,0,benchmarkResources);
        benchmarksListView.setAdapter(benchmarkWorkoutsAdapter);
        registerForContextMenu(benchmarksListView);

        String benchmarkInfo = benchmarkWorkoutsAdapter.getBenchmarkInfoText(selectedBenchmark);
        benchmarkInfoTextView.setText(benchmarkInfo);
        benchmarksListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                // RunAnimation2();
                // animationText needs to know former selected Benchmark position
                String animationChangeText = benchmarkWorkoutsAdapter.getAnimationInfoText(selectedBenchmark,i);

                selectedBenchmark = i;

                String benchmarkInfo = benchmarkWorkoutsAdapter.getBenchmarkInfoText(selectedBenchmark);
                benchmarkInfoTextView.setText(benchmarkInfo);

                animationTextView.setText(animationChangeText);


               RunAnimation();
                // Animation animation = AnimationUtils.loadAnimation()

                // changeBenchmarkInfoText();


            }
        });

        copyBenchmarkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int i = selectedBenchmark;
                Log.d("DESC",benchmarkDescriptions.get(i) );
                Log.d("TC",benchmarkTimeControls.get(i).getTimeControlsAsJSONGString() );

                String holds = "";
                String grips = "";
                String diffis = "";
                for (int j = 0 ; j < benchmarkWorkoutHolds.get(i).size() ; j=j+2) {

                    Log.d("HOLDS",benchmarkWorkoutHolds.get(i).get(j).getHoldInfo( benchmarkWorkoutHolds.get(i).get(j+1) ).replace("\n"," ") );

                }

                Log.d("HANGS",holds +  "   " +grips + "    " + diffis);

                if (randomizeGripsCheckBox.isChecked() ) {
                    Toast.makeText(BenchmarkActivity.this, "copy benchmark and randomize grips ", Toast.LENGTH_SHORT).show();

                    int index;
                    Random random = new Random();
                    int totalGrips = benchmarkWorkoutHolds.get(selectedBenchmark).size()/2;
                    Log.d("totalGrips",": " + totalGrips);

                    ArrayList<Hold> randomizedHolds = new ArrayList<>();

                    for (int rng = totalGrips - 1; rng >= 0; rng--)
                    {
                        index = random.nextInt(rng + 1);

                        randomizedHolds.add( benchmarkWorkoutHolds.get(selectedBenchmark).get(2*index) );
                        benchmarkWorkoutHolds.get(selectedBenchmark).remove(2*index);

                        randomizedHolds.add( benchmarkWorkoutHolds.get(selectedBenchmark).get(2*index) );
                        benchmarkWorkoutHolds.get(selectedBenchmark).remove(2*index);

                    }
                        Log.d("RNGHOLD","SIZE: " + randomizedHolds.size() + "  BMSIZE " + benchmarkWorkoutHolds.get(selectedBenchmark).size() ) ;
                    for (int j = 0 ; j < randomizedHolds.size() ; j = j+2) {
                        Log.d("HOLDS",randomizedHolds.get(j).getHoldInfo( randomizedHolds.get(j+1) ).replace("\n"," ") );
                    }
                    Intent resultCopyBenchmarkIntent = new Intent();
                    resultCopyBenchmarkIntent.putExtra("com.finn.laakso.hangboardapp.BOARDNAME", hangboardNames[hangboardPosition]);
                    resultCopyBenchmarkIntent.putExtra("com.finn.laakso.hangboardapp.SETTINGS", benchmarkTimeControls.get(selectedBenchmark).getTimeControlsIntArray());
                    resultCopyBenchmarkIntent.putParcelableArrayListExtra("com.finn.laakso.hangboardapp.HOLDS", randomizedHolds);

                    setResult(Activity.RESULT_OK, resultCopyBenchmarkIntent);

                    finish();

                }
                else {
                    Toast.makeText(BenchmarkActivity.this, "copy benchmark", Toast.LENGTH_SHORT).show();

                    Intent resultCopyBenchmarkIntent = new Intent();
                    resultCopyBenchmarkIntent.putExtra("com.finn.laakso.hangboardapp.BOARDNAME", hangboardNames[hangboardPosition]);
                    resultCopyBenchmarkIntent.putExtra("com.finn.laakso.hangboardapp.SETTINGS", benchmarkTimeControls.get(selectedBenchmark).getTimeControlsIntArray());
                    resultCopyBenchmarkIntent.putParcelableArrayListExtra("com.finn.laakso.hangboardapp.HOLDS", benchmarkWorkoutHolds.get(selectedBenchmark));

                    setResult(Activity.RESULT_OK, resultCopyBenchmarkIntent);

                    finish();
                }
            }
        });
    }

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


    }

    private void parceBenchmarkPrograms(int selectedHangboardPosition) {

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

     /*   for (int i = 0 ; i <benchmarkDescriptions.size() ; i++) {
            Log.d("DESC",benchmarkDescriptions.get(i) );
            Log.d("TC",benchmarkTimeControls.get(i).getTimeControlsAsJSONGString() );

            String holds = "";
            String grips = "";
            String diffis = "";
            for (int j = 0 ; j < benchmarkWorkoutHolds.get(i).size() ; j=j+2) {

                Log.d("HOLDS",benchmarkWorkoutHolds.get(i).get(j).getHoldInfo( benchmarkWorkoutHolds.get(i).get(j+1) ).replace("\n"," ") );

             //   holds += benchmarkWorkoutHolds.get(i).get(j).getHoldNumber() + "";
              //  grips += benchmarkWorkoutHolds.get(i).get(j).getGripStyleInt() + "";
              //  diffis += benchmarkWorkoutHolds.get(i).get(j).getHoldValue() + "";

            }
            Log.d("HANGS",holds +  "   " +grips + "    " + diffis);

        }
*/
    }

    public static int[] parceStringToInt(String arrayIntLine) {
        String[] parcedCompletedHangs = arrayIntLine.split(",");

        int[] completed = new int[parcedCompletedHangs.length];

        for (int i = 0; i < parcedCompletedHangs.length; i++) {
            completed[i] = Integer.parseInt(parcedCompletedHangs[i]);
        }

        return completed;
    }

    private void RunAnimation2()
    {
        Animation a = AnimationUtils.loadAnimation(this, R.anim.test_animation2);
        a.reset();
        TextView tv = (TextView) findViewById(R.id.benchmarkInfoTextView);
        tv.clearAnimation();
        tv.startAnimation(a);
    }

    private void RunAnimation()
    {
        Animation a = AnimationUtils.loadAnimation(this, R.anim.test_animation);
        a.reset();
        TextView tv = (TextView) findViewById(R.id.animationTextView);
        tv.clearAnimation();
        tv.startAnimation(a);
    }
}
