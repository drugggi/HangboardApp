package com.finn.laakso.hangboardapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class WorkoutDetailsActivity extends AppCompatActivity {

    private WorkoutDBHandler dbHandler;

    // workout info from SQLite database
    private int id;
    private long date;
    private String hangboardName;
    private TimeControls timeControls;
    private ArrayList<Hold> workoutHolds;
    private int[] completed;
    private boolean isHidden;
    private String description;
    private CalculateWorkoutDetails calculatedDetails;

    // workout info that requires calculation usually involving completed matrix

    private TextView workoutDetailsTextView;
    private TextView calculatedDetailsTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_workout_details);

        workoutDetailsTextView = (TextView) findViewById(R.id.workoutDetailsTextView);
        calculatedDetailsTextView = (TextView) findViewById(R.id.calculatedDetailsTextView);


        dbHandler = new WorkoutDBHandler(getApplicationContext(),null,null,1);

        int databasePosition=0;
        if (getIntent().hasExtra("com.finn.laakso.hangboardapp.DBPOSITION")) {
            databasePosition = getIntent().getIntExtra("com.finn.laakso.hangboardapp.DBPOSITION",0);
        }

        isHidden = true;
        if (getIntent().hasExtra("com.finn.laakso.hangboardapp.ISHIDDEN")) {
            isHidden = getIntent().getExtras().getBoolean("com.finn.laakso.hangboardapp.ISHIDDEN");
        }

        if (databasePosition != 0) {
            id = dbHandler.lookUpId(databasePosition,isHidden);
            date = dbHandler.lookUpDate(databasePosition,isHidden);
            hangboardName = dbHandler.lookUpHangboard(databasePosition,isHidden);
            workoutHolds = dbHandler.lookUpWorkoutHolds(databasePosition,isHidden);
            timeControls = dbHandler.lookUpTimeControls(databasePosition,isHidden);
            completed = dbHandler.lookUpCompletedHangs(databasePosition,isHidden);
            description = dbHandler.lookUpWorkoutDescription(databasePosition,isHidden);

        }
        DateFormat dateFormat = SimpleDateFormat.getDateInstance();
        Date resultdate = new Date(date);

        StringBuilder workoutDetailsBuilder = new StringBuilder("ID: ").append(id).append("\n");
        workoutDetailsBuilder.append("Date: ").append(dateFormat.format(resultdate)).append("\n");
        workoutDetailsBuilder.append("Hangboard Name: ").append( hangboardName).append("\n");
        workoutDetailsBuilder.append("Time Controls:\n    ").append(timeControls.getTimeControlsAsString() ).append("\n");
        workoutDetailsBuilder.append("Holds:\n").append(getWorkoutHoldsInfo(workoutHolds) );

        workoutDetailsBuilder.append("Completed Hangs Matrix: \n").append(getCompletedMatrix(completed)).append("\n");
        workoutDetailsBuilder.append("Description: ").append(description).append("\n");
        workoutDetailsBuilder.append("Hidden workout (warm up, test, etc.): ").append(isHidden).append("\n");

        workoutDetailsTextView.setText(workoutDetailsBuilder.toString());

        calculatedDetails = new CalculateWorkoutDetails(timeControls,workoutHolds,completed);


        StringBuilder calculatedDetailsBuilder = new StringBuilder();
        calculatedDetailsBuilder.append("Workout Time:          ").append(timeControls.getTotalTime() ).append("s\n");
        calculatedDetailsBuilder.append("Workout Time adjusted: ").append(calculatedDetails.getAdjustedWorkoutTime() ).append("s\n    (Times of failed hangs at the end of workout are removed. I.e. workout is stopped early.\n");
        calculatedDetailsBuilder.append("Unused Workout Time: ").append(calculatedDetails.getUnusedWorkoutTime() ).append("s\n");
        calculatedDetailsBuilder.append("Time Under Tension:           ").append(timeControls.getTimeUnderTension() ).append("s\n");
        calculatedDetailsBuilder.append("Time Under Tension adjusted: ").append(calculatedDetails.getAdjustedTUT() ).append("s\n    (Times of failed hangs are obviously not part of time under tension\n");
        calculatedDetailsBuilder.append("Completed Hangs: ").append(calculatedDetails.getCompletedHangs() ).append("/").append(calculatedDetails.getTotalHangs() ).append("\n");
        calculatedDetailsBuilder.append("Successful hang percent: ").append(calculatedDetails.getSuccessfulHangRate() ).append("%\n");
        calculatedDetailsBuilder.append("Average Difficulty per hang: ").append(calculatedDetails.getAverageDifficutly() ).append(" (avg D)\n");
        calculatedDetailsBuilder.append("Workout intensity: ").append(calculatedDetails.getIntensity() ).append(" (TUT/WT)\n");
        calculatedDetailsBuilder.append("Total workload: ").append(calculatedDetails.getWorkload() ).append(" (avg D*TUT)\n");
        calculatedDetailsBuilder.append("Workout power: ").append(calculatedDetails.getWorkoutPower() ).append(" (avg D*TUT)/WT\n");

        calculatedDetailsTextView.setText(calculatedDetailsBuilder.toString());
    }

    private String getCompletedMatrix(int[] completed) {
        String hangs ="" + timeControls.getHangLaps();
        StringBuilder completedBuilder = new StringBuilder();

        for (int i = 0;i < completed.length ; i++) {
            completedBuilder.append("    ").append(completed[i]).append("/").append(hangs);

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
            holdsInfoBuilder.append("    ").append(tempText.replaceAll("\n",", ") ).append("\n");

        }
        return holdsInfoBuilder.toString();
    }

}
