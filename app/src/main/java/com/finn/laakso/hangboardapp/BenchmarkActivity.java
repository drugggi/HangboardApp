package com.finn.laakso.hangboardapp;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class BenchmarkActivity extends AppCompatActivity {

    private Button copyBenchmarkButton;
    private CheckBox randomizeGripsCheckBox;

    private ListView hangboardNamesListView;
    private ListView benchmarksListView;

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

        Toast.makeText(BenchmarkActivity.this,"Benchmark Activity (beta)", Toast.LENGTH_SHORT).show();

        hangboardNamesListView = findViewById(R.id.hangboardsListView);
        benchmarksListView = findViewById(R.id.benchmarksListView);
        copyBenchmarkButton = findViewById(R.id.copyBenchmarkWorkout);
        randomizeGripsCheckBox = findViewById(R.id.randomizeGrips);

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
                parceBenchmarkPrograms(hangboardPosition);

                benchmarksAdapter = new ArrayAdapter<String>(BenchmarkActivity.this,android.R.layout.simple_list_item_1,benchmarkDescriptions);

                benchmarksListView.setAdapter(benchmarksAdapter);

            }
        });

        benchmarksListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                
                selectedBenchmark = i;
                Toast.makeText(BenchmarkActivity.this,"position selected: " + i,Toast.LENGTH_SHORT).show();
            }
        });

        copyBenchmarkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (randomizeGripsCheckBox.isChecked() ) {
                    Toast.makeText(BenchmarkActivity.this, "copy benchmark and randomize grips ", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(BenchmarkActivity.this, "copy benchmark", Toast.LENGTH_SHORT).show();

                }
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

                Intent resultCopyBenchmarkIntent = new Intent();
                resultCopyBenchmarkIntent.putExtra("com.finn.laakso.hangboardapp.BOARDNAME",hangboardNames[hangboardPosition]);
                resultCopyBenchmarkIntent.putExtra("com.finn.laakso.hangboardapp.SETTINGS", benchmarkTimeControls.get(selectedBenchmark).getTimeControlsIntArray());
                resultCopyBenchmarkIntent.putParcelableArrayListExtra("com.finn.laakso.hangboardapp.HOLDS", benchmarkWorkoutHolds.get(selectedBenchmark));

                setResult(Activity.RESULT_OK, resultCopyBenchmarkIntent);

                finish();
            }
        });
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
            int[] tempHoldNumbers = JSONFetcher.parceCompletedHangs(allBenchmarks[i]);
            i++;
            int[] tempHoldGripTypes = JSONFetcher.parceCompletedHangs(allBenchmarks[i]);

            if (tempControls.getGripLaps()*2 != tempHoldNumbers.length ||
                    tempControls.getGripLaps()*2 != tempHoldGripTypes.length ) {
                Log.e("ERR","timecontrols griplaps different size than needed workoutholds at: " + i);

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

            Log.d("benchmark","data line: " + i + " = " +allBenchmarks[i]);
        }

        for (int i = 0 ; i <benchmarkDescriptions.size() ; i++) {
            Log.d("DESC",benchmarkDescriptions.get(i) );
            Log.d("TC",benchmarkTimeControls.get(i).getTimeControlsAsJSONGString() );

            String holds = "";
            String grips = "";
            String diffis = "";
            for (int j = 0 ; j < benchmarkWorkoutHolds.get(i).size() ; j=j+2) {

                Log.d("HOLDS",benchmarkWorkoutHolds.get(i).get(j).getHoldInfo( benchmarkWorkoutHolds.get(i).get(j+1) ).replace("\n"," ") );
                /*
                holds += benchmarkWorkoutHolds.get(i).get(j).getHoldNumber() + "";
                grips += benchmarkWorkoutHolds.get(i).get(j).getGripStyleInt() + "";
                diffis += benchmarkWorkoutHolds.get(i).get(j).getHoldValue() + "";
*/
            }
            Log.d("HANGS",holds +  "   " +grips + "    " + diffis);

        }

    }
}
