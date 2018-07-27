package com.finn.laakso.hangboardapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class WorkoutDetails extends AppCompatActivity {

    private MyDBHandler dbHandler;

    // workout info from SQLite database
    private int id;
    private long date;
    private String hangboardName;
    private TimeControls timeControls;
    private ArrayList<Hold> workoutHolds;
    private int[] completed;
    private boolean isHidden;
    private String description;

    // workout info that requires calculation usually involving completed matrix

    private int adjustedWorkoutTime;
    private int adjustedTUT;

    private TextView workoutDetailsTextView;
    private TextView calculatedDetailsTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_details);

        workoutDetailsTextView = (TextView) findViewById(R.id.workoutDetailsTextView);
        calculatedDetailsTextView = (TextView) findViewById(R.id.calculatedDetailsTextView);

        dbHandler = new MyDBHandler(getApplicationContext(),null,null,1);

        int databasePosition=0;
        if (getIntent().hasExtra("com.finn.laakso.hangboardapp.DBPOSITION")) {
            databasePosition = getIntent().getIntExtra("com.finn.laakso.hangboardapp.DBPOSITION",0);
        }

        isHidden = true;
        if (getIntent().hasExtra("com.finn.laakso.hangboardapp.ISHIDDEN")) {
            isHidden = getIntent().getExtras().getBoolean("com.finn.laakso.hangboardapp.ISHIDDEN");
        }

        if (databasePosition != 0) {
            Toast.makeText(this,"pos saatiin",Toast.LENGTH_SHORT).show();
            id = dbHandler.lookUpId(databasePosition,isHidden);
            date = dbHandler.lookUpDate(databasePosition,isHidden);
            hangboardName = dbHandler.lookUpHangboard(databasePosition,isHidden);
            timeControls = dbHandler.lookUpTimeControls(databasePosition,isHidden);
            workoutHolds = dbHandler.lookUpHolds(databasePosition,isHidden);
            completed = dbHandler.lookUpCompletedHangs(databasePosition,isHidden);
            description = dbHandler.lookUpWorkoutDescription(databasePosition,isHidden);

        }

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
        Date resultdate = new Date(date);

        StringBuilder workoutDetailsBuilder = new StringBuilder("ID: " + id + "\n");
        workoutDetailsBuilder.append("Date: " + sdf.format(resultdate) + "     (" + date + "ms)\n");
        workoutDetailsBuilder.append("Hangboard Name: " + hangboardName + "\n");
        workoutDetailsBuilder.append("Time Controls:\n    " + timeControls.getTimeControlsAsString() + "\n");
        workoutDetailsBuilder.append("Holds:\n" + getWorkoutHoldsInfo(workoutHolds) );

        workoutDetailsBuilder.append("Completed: \n" +getCompletedMatrix(completed) +  "\n");
        workoutDetailsBuilder.append("Description: " + description + "\n");
        workoutDetailsBuilder.append("Is Hidden: " + isHidden + "\n");

        workoutDetailsTextView.setText(workoutDetailsBuilder.toString());



        adjustedWorkoutTime= timeControls.getTotalTime();
        adjustedTUT = timeControls.getTimeUnderTension();

        StringBuilder calculatedDetailsBuilder = new StringBuilder();
        calculatedDetailsBuilder.append("Workout Time:          " + timeControls.getTotalTime() + "\n");
        calculatedDetailsBuilder.append("Workout Time adjusted: " + adjustedWorkoutTime + "\n    (Times of failed hangs at the end of workout are removed. I.e. workout is stopped early.\n");
        calculatedDetailsBuilder.append("Time Under Tension:           " + timeControls.getTimeUnderTension() + "\n");
        calculatedDetailsBuilder.append("Time Under Tension: adjusted: " + adjustedTUT + "\n    (Times of failed hangs are obviously not part of time under tension");

        calculatedDetailsTextView.setText(calculatedDetailsBuilder.toString());
    }




    private String getCompletedMatrix(int[] completed) {
        String hangs ="" + timeControls.getHangLaps();
        StringBuilder completedBuilder = new StringBuilder();

        for (int i = 0;i < completed.length ; i++) {
            completedBuilder.append("    " + completed[i] + "/" + hangs);

            if ((i+1) % timeControls.getGripLaps() == 0) {
                completedBuilder.append("\n");
            }


        }
        return  completedBuilder.toString();
    }

    private String getWorkoutHoldsInfo(ArrayList<Hold> workoutHolds) {
        StringBuilder holdsInfoBuilder = new StringBuilder();
        String tempText;

        for( int i = 0; i < workoutHolds.size() ; i = i+2) {

            tempText = workoutHolds.get(i).getHoldInfo(workoutHolds.get(i+1));
            holdsInfoBuilder.append("    " + tempText.replaceAll("\n",", ") + "\n");

        }
        return holdsInfoBuilder.toString();
    }

}
