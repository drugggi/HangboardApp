package com.finn.laakso.hangboardapp;

import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class BenchmarkActivity extends AppCompatActivity {

    private ListView hangboardNamesListView;
    private ListView benchmarksListViw;

    private String[] hangboardNames;

    private TimeControls timeControls;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().hide();
        setContentView(R.layout.activity_benchmark);

        Toast.makeText(BenchmarkActivity.this,"Benchmark Activity (beta)", Toast.LENGTH_SHORT).show();

        hangboardNamesListView = findViewById(R.id.hangboardsListView);
        benchmarksListViw = findViewById(R.id.benchmarksListView);

        hangboardNames = HangboardResources.getHangboardNames();

        String[] benchmarks = new String[] {"endurance 5A","crimp 7B","huutonauru"};

        Resources res = getResources();

        String[] bm1000Benchmarks = res.getStringArray(R.array.bm1000_benchmarks);
        // String holdNumbers = res.getStringArray()

        String benchmarkDescrption = bm1000Benchmarks[1];

        timeControls = new TimeControls();
        timeControls.setTimeControlsFromString(bm1000Benchmarks[1]);

        ArrayList<Hold> tempWorkoutHolds = new ArrayList<>();
        int[] tempHoldNumbers = JSONFetcher.parceCompletedHangs(bm1000Benchmarks[2]);
        int[] tempHoldGripTypes = JSONFetcher.parceCompletedHangs(bm1000Benchmarks[3]);
//        int[] tempHoldDifficulties = JSONFetcher.parceCompletedHangs(holdDifficulties);

        Hold tempHold;
        for (int j = 0 ; j < tempHoldNumbers.length ; j++ ) {

            tempHold = new Hold(tempHoldNumbers[j] );
            tempHold.setGripType(tempHoldGripTypes[j] );
  //          tempHold.setHoldValue(tempHoldDifficulties[j] );

            tempWorkoutHolds.add(tempHold);
        }

        Log.d("TC",timeControls.getTimeControlsAsJSONGString() );
        Log.d("Hld nro",bm1000Benchmarks[2] );
        Log.d("griptype",bm1000Benchmarks[3] );

        ListAdapter hangboardNamesAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, hangboardNames);
        ListAdapter benchmarksAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,benchmarks);

        hangboardNamesListView.setAdapter(hangboardNamesAdapter);
        benchmarksListViw.setAdapter(benchmarksAdapter);


    }
}
