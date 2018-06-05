package com.finn.laakso.hangboardapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class WorkoutStatistics extends AppCompatActivity {

    ListView workoutHistoryListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_statistics);

        HangBoard testBoard = new HangBoard(getResources());

        workoutHistoryListView = (ListView) findViewById(R.id.workoutHistoryListView);
        final ArrayAdapter<String> gradeAdapter = new ArrayAdapter<String>(this, R.layout.gradetextview, testBoard.getGrades());
        workoutHistoryListView.setAdapter(gradeAdapter);

    }
}
