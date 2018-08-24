package com.finn.laakso.hangboardapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

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

    //private LineChart workoutDifficultyPerSecondLineChart;
    //private BarChart workoutDifficultyPerSecondBarChart;

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
            Toast.makeText(this,"pos saatiin",Toast.LENGTH_SHORT).show();
            id = dbHandler.lookUpId(databasePosition,isHidden);
            date = dbHandler.lookUpDate(databasePosition,isHidden);
            hangboardName = dbHandler.lookUpHangboard(databasePosition,isHidden);
            workoutHolds = dbHandler.lookUpWorkoutHolds(databasePosition,isHidden);
            timeControls = dbHandler.lookUpTimeControls(databasePosition,isHidden);
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

        workoutDetailsBuilder.append("Completed Hangs Matrix: \n" +getCompletedMatrix(completed) +  "\n");
        workoutDetailsBuilder.append("Description: " + description + "\n");
        workoutDetailsBuilder.append("Hidden workout (warm up, test, etc.): " + isHidden + "\n");

        workoutDetailsTextView.setText(workoutDetailsBuilder.toString());

        calculatedDetails = new CalculateWorkoutDetails(timeControls,workoutHolds,completed);


        StringBuilder calculatedDetailsBuilder = new StringBuilder();
        calculatedDetailsBuilder.append("Workout Time:          " + timeControls.getTotalTime() + "s\n");
        calculatedDetailsBuilder.append("Workout Time adjusted: " + calculatedDetails.getAdjustedWorkoutTime() + "s\n    (Times of failed hangs at the end of workout are removed. I.e. workout is stopped early.\n");
        calculatedDetailsBuilder.append("Unused Workout Time: " + calculatedDetails.getUnusedWorkoutTime() + "s\n");
        calculatedDetailsBuilder.append("Time Under Tension:           " + timeControls.getTimeUnderTension() + "s\n");
        calculatedDetailsBuilder.append("Time Under Tension adjusted: " + calculatedDetails.getAdjustedTUT() + "s\n    (Times of failed hangs are obviously not part of time under tension\n");
        calculatedDetailsBuilder.append("Completed Hangs: " + calculatedDetails.getCompletedHangs() + "/" + calculatedDetails.getTotalHangs() + "\n");
        calculatedDetailsBuilder.append("Successful hang percent: " + calculatedDetails.getSuccessfulHangRate() + "%\n");
        calculatedDetailsBuilder.append("Average Difficulty per hang: " + calculatedDetails.getAverageDifficutly() + "(avg D)\n");
        calculatedDetailsBuilder.append("Workout intensity: " +calculatedDetails.getIntensity() +  " (TUT/WT)\n");
        calculatedDetailsBuilder.append("Total workload: " + calculatedDetails.getWorkload() + " (avg D*TUT)\n");
        calculatedDetailsBuilder.append("Workout power: " + calculatedDetails.getWorkoutPower() + " (avg D*TUT)/WT\n");

        calculatedDetailsTextView.setText(calculatedDetailsBuilder.toString());

        //createDifficultyPerSecondBarChart();
        //createDifficultyPerSecondLineChart();
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


    /*
    private void createDifficultyPerSecondBarChart() {
        workoutDifficultyPerSecondBarChart = (BarChart) findViewById(R.id.workoutDifficultyPerSecondBarChart);
        ArrayList<BarEntry> successfulEntries = new ArrayList<>();
        ArrayList<BarEntry> failedEntries = new ArrayList<>();

        ArrayList<Float> y = new ArrayList<>();
        ArrayList<Float> x = new ArrayList<>();

        int holdDifficulties[] = calculatedDetails.getHoldDifficulties();

        int seconds = 1;
        for (int laps = 0; laps < timeControls.getRoutineLaps() ; laps ++) {
            for (int grips = 0; grips < timeControls.getGripLaps(); grips++) {

                for (int hangs = 0; hangs < timeControls.getHangLaps(); hangs++) {
                    Log.e("laps & grips","laps " + laps + "  grips: " + grips);
                    Log.e("hang & completed","hangs: " + hangs + "  comp[] = " + completed[(laps)*(grips+1)]  );
                    for (int time_on = 0; time_on < timeControls.getTimeON(); time_on++) {
                        //Log.e("laps & grips","laps " + laps + "  grips: " + grips);
                       // Log.e("hang & completed","hangs: " + hangs + "  comp[] = " + completed[hangs]  );
                        if (hangs < completed[(laps)*(timeControls.getGripLaps()-1 ) + grips] ) {
                            successfulEntries.add(new BarEntry(seconds, holdDifficulties[grips]));
                        }
                        else {
                            failedEntries.add(new BarEntry(seconds, holdDifficulties[grips]));
                        }
                        seconds++;
                    }
                    for (int time_off = 0; time_off < timeControls.getTimeOFF(); time_off++) {
                        //workoutDifficultyEntries.add(new BarEntry(seconds, 0));
                        seconds++;

                    }

                }
                for (int rest = 0; rest < timeControls.getRestTime(); rest++) {
                    //workoutDifficultyEntries.add(new BarEntry(seconds, 0));
                    seconds++;
                }
            }
            for (int long_rest = 0; long_rest < timeControls.getRestTime(); long_rest++) {
                //workoutDifficultyEntries.add(new BarEntry(seconds, 0));
                seconds++;
            }
        }
        Log.e("entries","size: " + successfulEntries.size());

        ArrayList<IBarDataSet> barDataSets = new ArrayList<>();
        BarDataSet successfulBarDataSet = new BarDataSet(successfulEntries,"Successful hangs");
        BarDataSet failedBarDataSet = new BarDataSet(failedEntries,"Failed hangs");
        failedBarDataSet.setColor(Color.RED);

        barDataSets.add(successfulBarDataSet );
        barDataSets.add(failedBarDataSet);
        // BarDataSet dataset = new BarDataSet(workoutDifficultyEntries,"Difficulty");

        //dataset.setValueTextSize(0f);

        //dataset.setValueTextSize(0f);
        //dataset.setBarBorderWidth(1f);



        BarData data = new BarData(barDataSets);

        data.setBarWidth(1f);

        workoutDifficultyPerSecondBarChart.setData(data);
        Description desc = new Description();
        desc.setText("seconds spent on each difficulty level");
        workoutDifficultyPerSecondBarChart.setDescription(desc);

        workoutDifficultyPerSecondBarChart.setDrawValueAboveBar(true);
        workoutDifficultyPerSecondBarChart.setFitBars(false);

        XAxis xAxis = workoutDifficultyPerSecondBarChart.getXAxis();
        xAxis.setGranularity(1f);
        xAxis.setGranularityEnabled(true);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

       workoutDifficultyPerSecondBarChart.invalidate();


    }

    private void createDifficultyPerSecondLineChart() {
        workoutDifficultyPerSecondLineChart = (LineChart) findViewById(R.id.workoutDifficultyPerSecondLineChart);
        ArrayList<Entry> workoutDifficultyEntries = new ArrayList<>();
        ArrayList<Entry> linearRegressionEntries = new ArrayList<>();

        ArrayList<Float> y = new ArrayList<>();
        ArrayList<Float> x = new ArrayList<>();

        int holdDifficulties[] = calculatedDetails.getHoldDifficulties();

        int seconds = 1;
            for (int laps = 0; laps < timeControls.getRoutineLaps() ; laps ++) {
                for (int grips = 0; grips < timeControls.getGripLaps(); grips++) {

                    for (int hangs = 0; hangs < timeControls.getHangLaps(); hangs++) {

                        for (int time_on = 0; time_on < timeControls.getTimeON(); time_on++) {
                            workoutDifficultyEntries.add(new Entry(seconds, holdDifficulties[grips]));
                            seconds++;
                        }
                        for (int time_off = 0; time_off < timeControls.getTimeOFF(); time_off++) {
                            workoutDifficultyEntries.add(new Entry(seconds, 0));
                            seconds++;

                        }

                    }
                    for (int rest = 0; rest < timeControls.getRestTime(); rest++) {
                        workoutDifficultyEntries.add(new Entry(seconds, 0));
                        seconds++;
                    }
                }
                for (int long_rest = 0; long_rest < timeControls.getRestTime(); long_rest++) {
                    workoutDifficultyEntries.add(new Entry(seconds, 0));
                    seconds++;
                }
            }
        Log.e("entries","size: " + workoutDifficultyEntries.size());

        ArrayList<ILineDataSet> lineDataSets = new ArrayList<>();

        LineDataSet lineDataSetDifficulties = new LineDataSet(workoutDifficultyEntries,"Workout power (avg D*TUT/WT)");

        lineDataSets.add(lineDataSetDifficulties);

        LineData lineData = new LineData(lineDataSets);

        lineData.setValueTextSize(10f);
        workoutDifficultyPerSecondLineChart.setData(lineData);

        Description desc = new Description();
        desc.setText("Workout number");
        workoutDifficultyPerSecondLineChart.setDescription(desc);

       // XAxis xAxis = workoutDifficultyPerSecondLineChart.getXAxis();
        //xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));

        //xAxis.setPosition(XAxis.XAxisPosition.BOTTOM_INSIDE);

        workoutDifficultyPerSecondLineChart.invalidate();


    }
*/



}
