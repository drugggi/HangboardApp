package com.finn.laakso.hangboardapp;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

public class WorkoutStatisticsActivity extends AppCompatActivity {

    private MyDBHandler dbHandler;

    private CombinedChart totalWorkloadCombinedChart;

    private TextView workoutsInfoTextView;
    private TextView generalInfoTextView;

    private PieChart gripDistributionPieChart;
    private PieChart hangboardDistributionPieChart;
    private BarChart difficultyBarChart;
    private BarChart workoutDatesBarChart;
    private BarChart totalWorkloadBarChart;
    private HorizontalBarChart singleHangsOrRepeatersBarChart;
    // private LineChart timeUnderTensionLineChart;
   // private LineChart workoutTimeLineChart;
    private LineChart workoutIntensityLineChart;
    private LineChart workoutTUTandWTLineChart;
    private LineChart averageDifficultyPerHang;
    private LineChart workoutPowerLineChart;
    private LineChart scaledLineChart;

    private ArrayList<ArrayList<Hold>> allWorkoutsHolds;
    private ArrayList<TimeControls> allTimeControls;
    private ArrayList<Long> allDates;
    private ArrayList<int[]> allCompletedHangs;
    private ArrayList<String> allHangboards;

    private ArrayList<CalculateWorkoutDetails> allCalculatedDetails;

    private ArrayList<String> stringDates;

    private boolean includeHidden;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_statistics);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        includeHidden = false;

        if (getIntent().hasExtra("com.finn.laakso.hangboardapp.SHOWHIDDEN")) {
            includeHidden = getIntent().getExtras().getBoolean("com.finn.laakso.hangboardapp.SHOWHIDDEN");
        }

        dbHandler = new MyDBHandler(getApplicationContext(),null,null,1);

        new RetrieveDataFromDatabase().execute();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();



            }
        });

    }


    private class RetrieveDataFromDatabase extends AsyncTask {
        protected void onPreExecute() {


        }

        @Override
        protected Object doInBackground(Object[] objects) {

            allDates = new ArrayList<Long>();
            allHangboards = new ArrayList<String>();
            allTimeControls = new ArrayList<TimeControls>();
            allWorkoutsHolds = new ArrayList<ArrayList<Hold>>();
            allCompletedHangs = new ArrayList<int[]>();

            allCalculatedDetails = new ArrayList<CalculateWorkoutDetails>();

            allDates = dbHandler.lookUpAllDates(includeHidden);
            allHangboards = dbHandler.lookUpAllHangboards(includeHidden);
            allTimeControls = dbHandler.lookUpAllTimeControls(includeHidden);
            allWorkoutsHolds = dbHandler.lookUpAllWorkoutHolds(includeHidden);
            allCompletedHangs = dbHandler.lookUpAllCompletedHangs(includeHidden);

            CalculateWorkoutDetails tempDetails;
            for( int i = 0; i < allDates.size() ; i++) {

                tempDetails = new CalculateWorkoutDetails(allTimeControls.get(i), allWorkoutsHolds.get(i), allCompletedHangs.get(i));
                allCalculatedDetails.add(tempDetails);
            }

            return null;
        }



        protected void onPostExecute(Object objects) {

            // Lets check that all arrays are the same size, or else a fatal error will occur
            if ((allDates.size() == allHangboards.size()) == (allTimeControls.size() ==
                    allWorkoutsHolds.size()) == (allCompletedHangs.size() == allCalculatedDetails.size()) ) {

                createWorkoutsInfoTextViews();

                createSingleHangsOrRepeatersBarChart();
                createGripDistributionPieChart();
                createDifficultyBarChart();
                createWorkoutDatesBarChart();
                createWorkoutTUTandWTLineChart();
                createWorkoutIntensityLineChart();
                createAverageDifficultyPerHangLineChart();
                // createTotalWorkloadBarChart();
                createTotalWorkloadCombinedChart();
                createWorkoutPowerLineChart();
                createScaledLineChart();
                createHangboardDistributionPieChart();



            }

        }


    }

    public void createTotalWorkloadCombinedChart() {
        totalWorkloadCombinedChart = (CombinedChart) findViewById(R.id.totalWorkloadCombinedChart);

        totalWorkloadCombinedChart.getDescription().setText("Total workload for each workout (avg D*TUT)");
        totalWorkloadCombinedChart.setDrawOrder(new CombinedChart.DrawOrder[]{
                CombinedChart.DrawOrder.BAR,  CombinedChart.DrawOrder.LINE
        });

        ArrayList<BarEntry> workloadEntries = new ArrayList<>();
        ArrayList<Integer> barColors = new ArrayList<Integer>();

        ArrayList<Entry> linearRegressionEntries = new ArrayList<>();

        ArrayList<Float> y = new ArrayList<>();
        ArrayList<Float> x = new ArrayList<>();

        int xCoord = 0;
        float maxValue = 0;
        for (int i = allCalculatedDetails.size()-1 ; i >= 0 ; i--) {

            workloadEntries.add(new BarEntry(xCoord,allCalculatedDetails.get(i).getWorkload() ) );
            if (maxValue < allCalculatedDetails.get(i).getWorkload() ) {
                maxValue = allCalculatedDetails.get(i).getWorkload();
            }
            xCoord++;
            x.add((float) xCoord );
            y.add(allCalculatedDetails.get(i).getWorkload() );

        }

        LinearRegression workloadRegression = new LinearRegression(x,y);

        linearRegressionEntries.add(new Entry(0,workloadRegression.predict(0)));
        linearRegressionEntries.add(new Entry(xCoord-1,workloadRegression.predict(xCoord-1)));

        String[] labels = new String[allCalculatedDetails.size()-1];
        for (int i = 0 ; i < labels.length ; i++ ) {
            labels[i] = "WO: " + (i+1);

        }
        float adjustment;
        float currentWorkload ;
        int alpha;
        for (int i = allCalculatedDetails.size()-1 ; i >= 0 ; i--) {
            currentWorkload = allCalculatedDetails.get(i).getWorkload();

            adjustment = 200*currentWorkload / maxValue;
            alpha = (int) adjustment;
            barColors.add(Color.argb(50+alpha,0,0,200));
        }

        String regressionLabel = "Progression line: positive";
        if (workloadRegression.slope() < 0) {
            regressionLabel = "Progression line: negative";
        }

        LineDataSet lineDataSet = new LineDataSet(linearRegressionEntries,regressionLabel);
        lineDataSet.setLineWidth(1f);
        lineDataSet.setDrawCircles(false);
        lineDataSet.setValueTextSize(0f);
        lineDataSet.setColor(Color.BLUE);
        BarDataSet barDataSet = new BarDataSet(workloadEntries,"Workload");

        barDataSet.setColors(barColors);

        LineData lineData = new LineData(lineDataSet);
        BarData barData = new BarData(barDataSet);

        CombinedData data = new CombinedData();

        //data.setData( generateLineData());
        data.setData(barData);
        data.setData(lineData);
        //xAxis.setAxisMaximum(data.getXMax() + 0.25f);

        XAxis xAxis = totalWorkloadCombinedChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
        xAxis.setGranularity(1f);
        //xAxis.setGranularityEnabled(true);
        xAxis.setPosition(XAxis.XAxisPosition.TOP);

        totalWorkloadCombinedChart.setData(data);
        totalWorkloadCombinedChart.invalidate();
    }


    public void createWorkoutIntensityLineChart() {
        workoutIntensityLineChart = (LineChart) findViewById(R.id.workoutIntensityChart);
        List<Entry> intensityEntries = new ArrayList<Entry>();
        ArrayList<Entry> linearRegressionEntries = new ArrayList<>();

        ArrayList<Float> y = new ArrayList<>();
        ArrayList<Float> x = new ArrayList<>();

        int xCoord = 0;
        for (int i = allCalculatedDetails.size() - 1  ; i >= 0 ; i--) {
            intensityEntries.add(new Entry(xCoord,allCalculatedDetails.get(i).getIntensity()));
            xCoord++;

            x.add((float) xCoord );
            y.add(allCalculatedDetails.get(i).getIntensity());
        }

        LinearRegression intensityRegression = new LinearRegression(x,y);

        linearRegressionEntries.add(new Entry(0,intensityRegression.predict(0)));
        linearRegressionEntries.add(new Entry(xCoord-1,intensityRegression.predict(xCoord-1)));

        String[] labels = new String[allCalculatedDetails.size()-1];
        for (int i = 0 ; i < labels.length ; i++ ) {
            labels[i] = "WO: " + (i+1);
        }

        String slope = "positive";
        if ( intensityRegression.slope() < 0 ) {
            slope = "negative";
        }

        ArrayList<ILineDataSet> lineDataSets = new ArrayList<>();

        LineDataSet lineDataIntensityRegression = new LineDataSet(linearRegressionEntries,"progression line: " + slope);
        lineDataIntensityRegression.setDrawCircles(false);
        lineDataIntensityRegression.setColors(Color.MAGENTA);
        lineDataIntensityRegression.setLineWidth(1f);
        lineDataIntensityRegression.setValueTextSize(0f);
        LineDataSet linedataSetIntensity = new LineDataSet(intensityEntries, "Intensity (time under tension / workout time)");
        linedataSetIntensity.setColor(Color.MAGENTA);
        linedataSetIntensity.setLineWidth(2f);


        lineDataSets.add(linedataSetIntensity);
        lineDataSets.add(lineDataIntensityRegression);

        LineData lineData = new LineData(lineDataSets);

        lineData.setValueTextSize(12f);
        if (allCalculatedDetails.size() > 30) {
            lineData.setValueTextSize(0f);

        }

        workoutIntensityLineChart.setData(lineData);

        Description desc = new Description();
        desc.setText("Workout number");
        workoutIntensityLineChart.setDescription(desc);

        XAxis xAxis = workoutIntensityLineChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));

        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM_INSIDE);

        workoutIntensityLineChart.invalidate();

    }

    public void createAverageDifficultyPerHangLineChart() {
        averageDifficultyPerHang = (LineChart) findViewById(R.id.averageDifficultyPerHang);

        ArrayList<Entry> difficultyEntries = new ArrayList<>();
        ArrayList<Entry> linearRegressionEntries = new ArrayList<>();

        ArrayList<Float> y = new ArrayList<>();
        ArrayList<Float> x = new ArrayList<>();

        int xCoord = 0;
        for (int i = allCalculatedDetails.size() -1 ; i >= 0 ; i-- ) {
            difficultyEntries.add(new Entry(xCoord,allCalculatedDetails.get(i).getAverageDifficutly() ));
            xCoord++;

            y.add(allCalculatedDetails.get(i).getAverageDifficutly() );
            x.add((float)xCoord);
        }

        LinearRegression powerRegressionLine = new LinearRegression(x,y);

        linearRegressionEntries.add(new Entry(0,powerRegressionLine.predict(0)));
        linearRegressionEntries.add(new Entry(xCoord - 1,powerRegressionLine.predict(xCoord-1)));

        String[] labels = new String[allCalculatedDetails.size()-1];
        for (int i = 0 ; i < labels.length ; i++ ) {
            labels[i] = "WO: " + (i+1);
        }

        String slope = "positive";
        if (powerRegressionLine.slope() < 0) {
            slope = "negative";
        }

        ArrayList<ILineDataSet> lineDataSets = new ArrayList<>();

        LineDataSet lineDataSetTUT = new LineDataSet(difficultyEntries,"Average difficulty per hang");
        LineDataSet lineDataSetLR = new LineDataSet(linearRegressionEntries,"Progression line: " + slope);
        lineDataSetLR.setDrawCircles(false);
        lineDataSetTUT.setColor(Color.YELLOW);
        lineDataSetLR.setColor(Color.YELLOW);
        lineDataSetTUT.setLineWidth(2f);
        lineDataSetLR.setLineWidth(1f);
        lineDataSetLR.setValueTextSize(0f);

        lineDataSets.add(lineDataSetTUT);
        lineDataSets.add(lineDataSetLR);

        LineData lineData = new LineData(lineDataSets);

        lineData.setValueTextSize(10f);
        averageDifficultyPerHang.setData(lineData);

        Description desc = new Description();
        desc.setText("Workout number");
        averageDifficultyPerHang.setDescription(desc);

        XAxis xAxis = averageDifficultyPerHang.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));

        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM_INSIDE);

        averageDifficultyPerHang.invalidate();

    }

    private void createWorkoutPowerLineChart() {
        workoutPowerLineChart = (LineChart) findViewById(R.id.workoutPowerLineChart);

        ArrayList<Entry> workoutPowerEntries = new ArrayList<>();
        ArrayList<Entry> linearRegressionEntries = new ArrayList<>();

        ArrayList<Float> y = new ArrayList<>();
        ArrayList<Float> x = new ArrayList<>();

        int xCoord = 0;
        for (int i = allCalculatedDetails.size() -1 ; i >= 0 ; i-- ) {
            workoutPowerEntries.add(new Entry(xCoord,allCalculatedDetails.get(i).getWorkoutPower() ));
            xCoord++;
            y.add(allCalculatedDetails.get(i).getWorkoutPower());
            x.add((float)xCoord);
        }

        LinearRegression powerRegressionLine = new LinearRegression(x,y);

        linearRegressionEntries.add(new Entry(0,powerRegressionLine.predict(0)));
        linearRegressionEntries.add(new Entry(xCoord - 1,powerRegressionLine.predict(xCoord-1)));

        String[] labels = new String[allCalculatedDetails.size()-1];
        for (int i = 0 ; i < labels.length ; i++ ) {
            labels[i] = "WO: " + (i+1);
        }

        String regressionLabel = "Progression line: positive";
        if (powerRegressionLine.slope() < 0 ) {
            regressionLabel = "Progression line: negative";
        }

        ArrayList<ILineDataSet> lineDataSets = new ArrayList<>();

        LineDataSet lineDataSetTUT = new LineDataSet(workoutPowerEntries,"Workout power (avg D*TUT/WT)");
        LineDataSet lineDataSetLR = new LineDataSet(linearRegressionEntries,regressionLabel);
        lineDataSetLR.setValueTextSize(0f);

        lineDataSetLR.setDrawCircles(false);
        lineDataSetLR.setColor(Color.CYAN);
        lineDataSetTUT.setColor(Color.CYAN);

        lineDataSetLR.setLineWidth(1f);
        lineDataSetTUT.setLineWidth(2f);

        lineDataSets.add(lineDataSetTUT);
        lineDataSets.add(lineDataSetLR);

        LineData lineData = new LineData(lineDataSets);

        lineData.setValueTextSize(10f);
        workoutPowerLineChart.setData(lineData);

        Description desc = new Description();
        desc.setText("Workout number");
        workoutPowerLineChart.setDescription(desc);

        XAxis xAxis = workoutPowerLineChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));

        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM_INSIDE);

        workoutPowerLineChart.invalidate();


    }

    public void createScaledLineChart() {
        scaledLineChart = (LineChart) findViewById(R.id.scaledLineChart);

        ArrayList<Entry> entriesIntensity = new ArrayList<>();
        ArrayList<Entry> entriesAvgDifficulty = new ArrayList<>();
        ArrayList<Entry> entriesWorkload = new ArrayList<>();
        ArrayList<Entry> entriesPower = new ArrayList<>();


        float minIntensity = Float.MAX_VALUE;
        float maxIntensity = 0;
        float minAvgDifficulty = Float.MAX_VALUE;
        float maxAvgDifficulty = 0;
        float minWorkload = Float.MAX_VALUE;
        float maxWorkload = 0;
        float minPower = Float.MAX_VALUE;
        float maxPower = 0;

        for (int i = 0 ; i < allCalculatedDetails.size() ; i++) {
            if (minIntensity > allCalculatedDetails.get(i).getIntensity() ) {
                minIntensity = allCalculatedDetails.get(i).getIntensity();
            }
            if (maxIntensity < allCalculatedDetails.get(i).getIntensity() ) {
                maxIntensity = allCalculatedDetails.get(i).getIntensity();
            }

            if (minAvgDifficulty > allCalculatedDetails.get(i).getAverageDifficutly() ) {
                minAvgDifficulty = allCalculatedDetails.get(i).getAverageDifficutly();
            }
            if (maxAvgDifficulty < allCalculatedDetails.get(i).getAverageDifficutly() ) {
                maxAvgDifficulty = allCalculatedDetails.get(i).getAverageDifficutly();
            }

            if (minWorkload > allCalculatedDetails.get(i).getWorkload() ) {
                minWorkload = allCalculatedDetails.get(i).getWorkload();
            }
            if (maxWorkload < allCalculatedDetails.get(i).getWorkload() ) {
                maxWorkload = allCalculatedDetails.get(i).getWorkload();
            }

            if (minPower > allCalculatedDetails.get(i).getWorkoutPower() ) {
                minPower = allCalculatedDetails.get(i).getWorkoutPower();
            }
            if (maxPower < allCalculatedDetails.get(i).getWorkoutPower() ) {
                maxPower = allCalculatedDetails.get(i).getWorkoutPower();
            }


        }

        maxIntensity = maxIntensity - minIntensity;
        maxAvgDifficulty = maxAvgDifficulty - minAvgDifficulty;
        maxWorkload = maxWorkload - minWorkload;
        maxPower = maxPower - minPower;

        int xCoord = 0;

        float tempIntensity;
        float tempWorkload;
        float tempPower;
        float tempAvgDifficulty;
        for (int i = allCalculatedDetails.size()-1 ; i >= 0 ; i--) {

            tempIntensity = (allCalculatedDetails.get(i).getIntensity() - minIntensity) / maxIntensity;
            tempAvgDifficulty = (allCalculatedDetails.get(i).getAverageDifficutly() - minAvgDifficulty) / maxAvgDifficulty;
            tempWorkload = (allCalculatedDetails.get(i).getWorkload() - minWorkload) / maxWorkload;
            tempPower = (allCalculatedDetails.get(i).getWorkoutPower() - minPower) / maxPower;

            entriesIntensity.add(new Entry(xCoord, tempIntensity ));
            entriesAvgDifficulty.add(new Entry(xCoord, tempAvgDifficulty ));
            entriesWorkload.add(new Entry(xCoord, tempWorkload ));
            entriesPower.add(new Entry(xCoord, tempPower));

            xCoord++;
        }

        String[] labels = new String[allCalculatedDetails.size()-1];
        for (int i = 0 ; i < labels.length ; i++ ) {
            labels[i] = "WO: " + (i+1);
        }

        ArrayList<ILineDataSet> lineDataSets = new ArrayList<>();


        LineDataSet lineDataSetIntensity = new LineDataSet(entriesIntensity,"Intensity (TUT/WT)");
        lineDataSetIntensity.setColor(Color.MAGENTA);
        LineDataSet lineDataSetAvgDifficulty = new LineDataSet(entriesAvgDifficulty,"Average Difficulty (avg D)");
        lineDataSetAvgDifficulty.setColors(Color.YELLOW);
        LineDataSet lineDataSetWorkload = new LineDataSet(entriesWorkload,"total Workload(avg D*TUT)");
        lineDataSetWorkload.setColors(Color.BLUE);
        LineDataSet lineDataSetPower = new LineDataSet(entriesPower,"Workout power (avg D*TUT/WT)");
        lineDataSetPower.setColor(Color.CYAN);

        //lineDataSetAvgDifficulty.setLineWidth(-1000f);
        lineDataSets.add(lineDataSetIntensity);
        lineDataSets.add(lineDataSetAvgDifficulty);
        lineDataSets.add(lineDataSetWorkload);
        lineDataSets.add(lineDataSetPower);



        LineData lineData = new LineData(lineDataSets);
        lineData.setDrawValues(false);


        lineData.setValueTextSize(10f);
        scaledLineChart.setData(lineData);

        Description desc = new Description();
        desc.setText("Scaled values");
        scaledLineChart.setDescription(desc);

        XAxis xAxis = scaledLineChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));

        xAxis.setPosition(XAxis.XAxisPosition.TOP);

        scaledLineChart.invalidate();
        //workoutTUTandWTLineChart.animateX(1500);

    }

    public void createWorkoutTUTandWTLineChart() {
        workoutTUTandWTLineChart = (LineChart) findViewById(R.id.workoutTUTandWTLineChart);

        ArrayList<Entry> entriesTUT = new ArrayList<Entry>();
        ArrayList<Entry> entriesWT = new ArrayList<Entry>();


        int xCoord = 0;
        for (int i = allCalculatedDetails.size()-1 ; i >= 0 ; i--) {
            entriesTUT.add(new Entry(xCoord, ((float) allCalculatedDetails.get(i).getAdjustedTUT()) ));
            entriesWT.add(new Entry(xCoord, ((float) allCalculatedDetails.get(i).getAdjustedWorkoutTime()) ));
            xCoord++;
        }

        String[] labels = new String[allCalculatedDetails.size()-1];
        for (int i = 0 ; i < labels.length ; i++ ) {
            labels[i] = "WO: " + (i+1);
        }

        ArrayList<ILineDataSet> lineDataSets = new ArrayList<>();

        LineDataSet lineDataSetTUT = new LineDataSet(entriesTUT,"Time under tension (seconds)");
        lineDataSetTUT.setColor(Color.RED);
        LineDataSet lineDataSetWT = new LineDataSet(entriesWT,"Workout time (seconds)");
        lineDataSetWT.setColors(Color.GREEN);

        lineDataSets.add(lineDataSetTUT);
        lineDataSets.add(lineDataSetWT);

        LineData lineData = new LineData(lineDataSets);


        lineData.setValueTextSize(10f);
        workoutTUTandWTLineChart.setData(lineData);

        Description desc = new Description();
        desc.setText("Workout number");
        workoutTUTandWTLineChart.setDescription(desc);

        XAxis xAxis = workoutTUTandWTLineChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));

        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM_INSIDE);


        workoutTUTandWTLineChart.invalidate();
        workoutTUTandWTLineChart.animateX(1500);


    }
/*
    public void createTotalWorkloadBarChart() {
        totalWorkloadBarChart = (BarChart) findViewById(R.id.totalWorkloadBarChart);

        ArrayList<BarEntry> workloadEntries = new ArrayList<>();
        ArrayList<Integer> barColors = new ArrayList<Integer>();

        int xCoord = 0;
        float maxValue = 0;
        for (int i = allCalculatedDetails.size()-1 ; i >= 0 ; i--) {
            //entries.add(new BarEntry(dayDifferences.get(i) , (float)allCalculatedDetails.get(i).getAdjustedWorkoutTime()/60 ));
            workloadEntries.add(new BarEntry(xCoord,allCalculatedDetails.get(i).getWorkload() ) );
            if (maxValue < allCalculatedDetails.get(i).getWorkload() ) {
                maxValue = allCalculatedDetails.get(i).getWorkload();
            }
            xCoord++;
        }


        String[] labels = new String[allCalculatedDetails.size()-1];
        for (int i = 0 ; i < labels.length ; i++ ) {
            labels[i] = "WO: " + (i+1);

        }
        float adjustment;
        float currentWorkload ;
        int alpha;
        for (int i = allCalculatedDetails.size()-1 ; i >= 0 ; i--) {
            currentWorkload = allCalculatedDetails.get(i).getWorkload();

            adjustment = 200*currentWorkload / maxValue;
            alpha = (int) adjustment;
            barColors.add(Color.argb(50+alpha,0,0,200));
        }

        BarDataSet barDataSet = new BarDataSet(workloadEntries,"Total workload for each workout (avg D*TUT)");
        //barDataSet.setColors(ColorTemplate.VORDIPLOM_COLORS);
        //barDataSet.setColor(Color.BLUE);
        barDataSet.setColors(barColors);

        BarData barData = new BarData(barDataSet);
        totalWorkloadBarChart.setData(barData);

        Description desc = new Description();
        desc.setText("Total workload for each workout (avg D*TUT)");

        totalWorkloadBarChart.setDescription(desc);
        XAxis xAxis = totalWorkloadBarChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
        xAxis.setGranularity(1f);
        //xAxis.setGranularityEnabled(true);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        totalWorkloadBarChart.invalidate();



    }
*/
    public void createWorkoutDatesBarChart() {
        workoutDatesBarChart = (BarChart) findViewById(R.id.workoutDatesBarChart);

        //SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        DateFormat dateFormatTest = SimpleDateFormat.getDateInstance();
        ArrayList<BarEntry> entries = new ArrayList<>();

        long difference;
        ArrayList<Integer> dayDifferences = new ArrayList<>();

        // Calculate dates variables time difference by day
        for (int i = 0; i < allDates.size() ; i ++) {

            // compare every date to the first day by subtracting from it, that tells the difference
            difference = - (allDates.get(allDates.size()-1) - allDates.get(i) )/( 1000*60*60*24);
            dayDifferences.add( (int) difference);

        }

        // Lets populate the entries by starting at day 0 (dayDifference = first record) and finishing
        // the latest workout day x and corresponding workoutTime
        for (int i = dayDifferences.size()-1; i >= 0 ; i--) {
            // Log.e("data point ", dayDifferences.get(i) + " : "+effectiveWorkoutTime.get(i)/60);
            entries.add(new BarEntry(dayDifferences.get(i) , (float)allCalculatedDetails.get(i).getAdjustedWorkoutTime()/60 ));
        }

        BarDataSet barDataSet = new BarDataSet(entries,"Workout time for each day from first to last (min)");
        //barDataSet.setColors(ColorTemplate.VORDIPLOM_COLORS);
        barDataSet.setColor(Color.DKGRAY);
        BarData barData = new BarData(barDataSet);
        workoutDatesBarChart.setData(barData);

        // Lets populate the x axis with day numbers from first workout day
        String[] labels = new String[dayDifferences.get(0) + 1];
        for (int i = 0 ; i < labels.length ; i++ ) {
            labels[i] = "day: " + i;
        }

        labels[labels.length-1] = dateFormatTest.format(allDates.get(allDates.size()-1));
        labels[0] = dateFormatTest.format(allDates.get(0));

       // Collections.reverse(dayDifferences);

        for (int i = 0 ; i < dayDifferences.size()  ; i++) {

            labels[dayDifferences.get(i)] = dateFormatTest.format(allDates.get(i));
            // Log.e(" labels i",i + " :  " + sdf.format(dates.get(i)));
        }
        Description desc = new Description();
        desc.setText("Workout day number");

        workoutDatesBarChart.setDescription(desc);
        XAxis xAxis = workoutDatesBarChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
        xAxis.setGranularity(1f);
        //xAxis.setGranularityEnabled(true);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        workoutDatesBarChart.invalidate();

    }

    public void createSingleHangsOrRepeatersBarChart() {

        singleHangsOrRepeatersBarChart = (HorizontalBarChart) findViewById(R.id.singleHangsOrRepeatersBarChart);

        int repeatersAmount = 0;
        int singleHangsAmount = 0;
        for ( int i = 0 ; i < allTimeControls.size() ; i++) {
            if (allTimeControls.get(i).getHangLaps() == 1) {
                singleHangsAmount++;
            } else {
                repeatersAmount++;
            }
        }
        singleHangsOrRepeatersBarChart.getXAxis().setDrawGridLines(false);
        singleHangsOrRepeatersBarChart.getAxisLeft().setDrawGridLines(false);

        ArrayList<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(1,singleHangsAmount));
        entries.add(new BarEntry(2,repeatersAmount));
        entries.add(new BarEntry(1,0));
        entries.add(new BarEntry(2,0));

        BarDataSet bardataset = new BarDataSet(entries," " );
        bardataset.setLabel("Workouts done: " +(singleHangsAmount + repeatersAmount));


        bardataset.setColors(ColorTemplate.VORDIPLOM_COLORS);


        String[] labels = new String[3];

        labels[0] = " ";
        labels[1] = "Single hangs: " + singleHangsAmount;
        labels[2] = "Repeaters: " + repeatersAmount;
       // labels[3] = "total: " + (singleHangsAmount + repeatersAmount);
        BarData theData = new BarData(bardataset);

        theData.setValueTextSize(0f);

        Description desc = new Description();
        desc.setText(" ");

        singleHangsOrRepeatersBarChart.setData(theData);
        singleHangsOrRepeatersBarChart.setDescription(desc);

        XAxis xAxis = singleHangsOrRepeatersBarChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
        xAxis.setGranularity(1f);
        xAxis.setGranularityEnabled(true);
        xAxis.setPosition(XAxis.XAxisPosition.TOP_INSIDE);

        singleHangsOrRepeatersBarChart.invalidate();
        singleHangsOrRepeatersBarChart.animateY(500);

    }

    public void createDifficultyBarChart() {
        difficultyBarChart = (BarChart) findViewById(R.id.difficultyBarChart);
        //difficultyBarChart.setDrawBarShadow(true);

        ArrayList<BarEntry> entries = new ArrayList<>();

        int[] completed_seconds;
        int[] grip_difficulties;

        // Why TreeMap?
        TreeMap<Integer,Integer> difficultyMap = new TreeMap<Integer, Integer>();

        // all hold in workout history
        for (int i = 0 ; i < allWorkoutsHolds.size() ; i++ ) {

            // Holds used in a single workout
            completed_seconds = new int[allTimeControls.get(i).getGripLaps()];
            grip_difficulties = new int[allTimeControls.get(i).getGripLaps()];

            // lets get single workouts grip difficulties,
            for (int j = 0 ; j < grip_difficulties.length; j++) {
                grip_difficulties[j] = (allWorkoutsHolds.get(i).get(2*j).getHoldValue() +
                        allWorkoutsHolds.get(i).get(2*j+1).getHoldValue() ) / 2;
            }
            // lets get single workouts completed hangs in seconds
            for (int j = 0 ; j < allCompletedHangs.get(i).length ; j++) {
                completed_seconds[j % allTimeControls.get(i).getGripLaps()] += allCompletedHangs.get(i)[j] * allTimeControls.get(i).getTimeON();
            }

            // lets put both in TreeMap, so that same difficulty level is only once and seconds is summed in that level
            for (int j = 0; j < grip_difficulties.length ; j++) {
                // entries.add(new BarEntry(grip_difficulties[j],completed_seconds[j]));

                if (difficultyMap.containsKey(grip_difficulties[j])) {
                    difficultyMap.put(grip_difficulties[j], difficultyMap.get(grip_difficulties[j]) + completed_seconds[j]);
                }
                else {
                    difficultyMap.put(grip_difficulties[j],completed_seconds[j]);
                }

            }


        }

        // Calculate different for every difficulty level from easiest to hardest
        ArrayList<Integer> barColors = new ArrayList<Integer>();
        int color_increment = 200/difficultyMap.size();
        int alpha = 50;
        for(Map.Entry<Integer,Integer> entry: difficultyMap.entrySet()) {
            int key = entry.getKey();
            int value = entry.getValue();

            if ( value == 0) {continue;} // custom hold
            alpha += color_increment;
            barColors.add(Color.argb(alpha,200,0,0));
            entries.add(new BarEntry(key,value));

        }

        BarDataSet dataset = new BarDataSet(entries,"Difficulty");

        dataset.setValueTextSize(0f);

        dataset.setBarBorderWidth(1f);

        dataset.setColors( barColors);

        BarData data = new BarData(dataset);

        difficultyBarChart.setData(data);
        Description desc = new Description();
        desc.setText("seconds spent on each difficulty level");
        difficultyBarChart.setDescription(desc);

        difficultyBarChart.setDrawValueAboveBar(true);
        difficultyBarChart.setFitBars(false);

        XAxis xAxis = difficultyBarChart.getXAxis();
        xAxis.setGranularity(1f);
        xAxis.setGranularityEnabled(true);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        difficultyBarChart.invalidate();
        difficultyBarChart.animateY(3000);


    }

    public void createHangboardDistributionPieChart() {
        hangboardDistributionPieChart = (PieChart) findViewById(R.id.hangboardDistributionPieChart);
        hangboardDistributionPieChart.setCenterText("Hangboards used");

        Map<String,Integer> hangboardMap = new HashMap();
        ArrayList<PieEntry> yValues = new ArrayList<>();
        ArrayList<Integer> barColors = new ArrayList<Integer>();
        Random rng = new Random();

        for (String hangboardName: allHangboards) {

            if (hangboardMap.containsKey(hangboardName)) {
                hangboardMap.put(hangboardName, hangboardMap.get(hangboardName)+1);
            }
            else {
                hangboardMap.put(hangboardName, 1);
            }
        }

        float total = allHangboards.size();
        float percent;

        for(Map.Entry<String,Integer> entry: hangboardMap.entrySet()) {
            String name = entry.getKey();
            int amount = entry.getValue();

            percent = 100* amount / total ;

            yValues.add(new PieEntry(percent,name));
            barColors.add(rng.nextInt() );

        }

        PieDataSet dataSet = new PieDataSet(yValues,"Hangboard distribution");


        dataSet.setSliceSpace(5f);
        dataSet.setSelectionShift(8f);
        //dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        dataSet.setColors(barColors);
        dataSet.setValueLineColor(Color.BLACK);
        PieData data = new PieData(dataSet);
        data.setValueTextSize(12f);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextColor(Color.BLUE);

        hangboardDistributionPieChart.setData(data);
        hangboardDistributionPieChart.setHoleColor(android.R.color.darker_gray);

        // gripDistributionPieChart.animateY(1000);
        hangboardDistributionPieChart.setTransparentCircleRadius(35f);
        hangboardDistributionPieChart.setHoleRadius(25f);
        hangboardDistributionPieChart.setEntryLabelColor(Color.BLACK);
        Description desc = new Description();
        desc.setText("Hangboard distribution");
        hangboardDistributionPieChart.setDescription(desc);
        hangboardDistributionPieChart.invalidate();


    }

    public void createGripDistributionPieChart() {
        gripDistributionPieChart = (PieChart) findViewById(R.id.gripDistributionPieChart);
        gripDistributionPieChart.setCenterText("Each grip type used (seconds)");

        ArrayList<PieEntry> yValues = new ArrayList<>();

        int fourfinger = 0;
        int threefront = 0;
        int threeback= 0;
        int twofront = 0;
        int twomiddle = 0;
        int twoback = 0;
        int middlefinger = 0;
        int other = 0;
        int total_grips = 0;

        int seconds_multiplier;
        int hold_index;

        Hold.grip_type gripType;

        // All Holds ever used
        for (int i = 0 ; i < allWorkoutsHolds.size() ; i++ ) {

            // Holds used in a single workout
            for (int j = 0 ; j < allCompletedHangs.get(i).length ; j ++) {

                // multiplier depending if the hang was successfull or not
                seconds_multiplier = allCompletedHangs.get(i)[j] * allTimeControls.get(i).getTimeON();
                hold_index = 2*( j % allTimeControls.get(i).getGripLaps());

               // Log.e("workout Holds: index: ",hold_index + "   text: "+ allWorkoutsHolds.get(i).get(hold_index).getHoldText());

                gripType = allWorkoutsHolds.get(i).get(hold_index).getGripStyle();
                total_grips += seconds_multiplier;
                if (gripType == Hold.grip_type.FOUR_FINGER) { fourfinger += seconds_multiplier; }
                else if (gripType == Hold.grip_type.THREE_FRONT) {threefront += seconds_multiplier; }
                else if ( gripType == Hold.grip_type.THREE_BACK) {threeback += seconds_multiplier; }
                else if ( gripType == Hold.grip_type.TWO_FRONT){twofront += seconds_multiplier; }
                else if ( gripType == Hold.grip_type.TWO_MIDDLE){twomiddle += seconds_multiplier; }
                else if ( gripType == Hold.grip_type.TWO_BACK){twoback += seconds_multiplier; }
                else if ( gripType == Hold.grip_type.MIDDLE_FINGER){middlefinger += seconds_multiplier; }
                else {other+= seconds_multiplier; }
            }
        }
        ArrayList<Integer> barColors = new ArrayList<Integer>();
        if (fourfinger != 0) {
            yValues.add(new PieEntry(fourfinger,"Four fingers"));
            //barColors.add(Color.argb(255,200,0,0));
            barColors.add(Color.argb(255,104,159,56));
        }
        if (threefront != 0) {
            yValues.add(new PieEntry(threefront,"Three front"));
            barColors.add(Color.argb(255,0,172,193));
        }
        if (threeback != 0) {
            yValues.add(new PieEntry(threeback,"Three back"));
            barColors.add(Color.argb(255,126,87,194));
        }
        if (twofront != 0) {yValues.add(new PieEntry(twofront,"Two front"));
            barColors.add(Color.argb(255,251,140,0));
        }
        if (twomiddle != 0) {yValues.add(new PieEntry(twofront,"Two middle"));
            barColors.add(Color.argb(255,255,112,67));
        }
        if (twoback != 0) {
            yValues.add(new PieEntry(twofront,"Two back"));
            barColors.add(Color.argb(255,255,202,40));
        }
        if (middlefinger != 0) {
            yValues.add(new PieEntry(twofront,"Middle finger"));
            barColors.add(Color.argb(255,156,39,176));
        }
        if (other != 0) {
            yValues.add(new PieEntry(other,"Other"));
            barColors.add(Color.argb(255,158,158,158));
        }

        PieDataSet dataSet = new PieDataSet(yValues," ");

        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);
        //dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        dataSet.setColors(barColors);
        PieData data = new PieData(dataSet);

        data.setValueTextSize(10f);
        data.setValueTextColor(Color.YELLOW);
        gripDistributionPieChart.setData(data);
        // gripDistributionPieChart.animateY(1000);
        gripDistributionPieChart.setHoleColor(android.R.color.darker_gray);
        Description desc = new Description();
        desc.setText("Grip distribution (seconds)");
        //desc.setTextColor(Color.argb(255,104,159,56));
        desc.setTextSize(15f);

        gripDistributionPieChart.setDescription(desc);
        gripDistributionPieChart.invalidate();
        gripDistributionPieChart.animateY(1500);

    }

   private void createWorkoutsInfoTextViews() {
       workoutsInfoTextView = (TextView) findViewById(R.id.workoutInfoTextView);
       generalInfoTextView = (TextView) findViewById(R.id.generalInfoTextView);

       StringBuilder workoutsInfo = new StringBuilder();
       StringBuilder generalInfo = new StringBuilder();

       int totalWorkoutsDone = allCalculatedDetails.size();
       int totalWorkoutTime = 0;
       int totalAdjustedWorkoutTime = 0;
       int totalWorkoutTUT = 0;
       int totalAdjustedTUT = 0;
       int totalUnusedTime = 0;

       int totalHangs = 0;
       int totalCompletedHangs = 0;

       Float averageIntensity = 0f;
       int averageDifficulty = 0;
       int averageWorkload = 0;
       int totalDifficultiesSum = 0;

       for (int i = 0; i < allCalculatedDetails.size() ; i++) {
           totalHangs += allCalculatedDetails.get(i).getTotalHangs();
           totalCompletedHangs += allCalculatedDetails.get(i).getCompletedHangs();

           totalWorkoutTime += allTimeControls.get(i).getTotalTime();
           totalAdjustedWorkoutTime += allCalculatedDetails.get(i).getAdjustedWorkoutTime();

           totalWorkoutTUT += allTimeControls.get(i).getTimeUnderTension();
           totalAdjustedTUT += allCalculatedDetails.get(i).getAdjustedTUT();

           totalUnusedTime += allCalculatedDetails.get(i).getUnusedWorkoutTime();
           averageIntensity += allCalculatedDetails.get(i).getIntensity();

           averageDifficulty += allCalculatedDetails.get(i).getDifficultiesSum();
           averageWorkload += allCalculatedDetails.get(i).getWorkload();

           totalDifficultiesSum += allCalculatedDetails.get(i).getDifficultiesSum();
       }

       int hangSuccessRate = 100* totalCompletedHangs / totalHangs;

       totalWorkoutTime = totalWorkoutTime / 60;
       totalAdjustedWorkoutTime = totalAdjustedWorkoutTime / 60;
       totalWorkoutTUT = totalWorkoutTUT / 60;
       totalAdjustedTUT = totalAdjustedTUT / 60;
       totalUnusedTime = totalUnusedTime / 60;

       workoutsInfo.append("Workouts done: ").append(totalWorkoutsDone).append("\n");
       workoutsInfo.append("Total hangs: ").append(totalHangs).append("\n");
       workoutsInfo.append("Total hangs completed").append(totalCompletedHangs).append("\n");
       workoutsInfo.append("Success rate: ").append(hangSuccessRate).append("% \n");
       workoutsInfo.append("Total workout time: ").append(totalWorkoutTime).append("min\n");
       workoutsInfo.append("Adjusted workout time: ").append(totalAdjustedWorkoutTime).append("min\n");
       workoutsInfo.append("Total TUT: ").append(totalWorkoutTUT).append("min\n");
       workoutsInfo.append("Adjusted TUT: ").append(totalAdjustedTUT).append("min\n");

       workoutsInfoTextView.setText(workoutsInfo.toString() );

       averageIntensity = averageIntensity / totalWorkoutsDone;

       if (totalCompletedHangs != 0) {
           averageWorkload = totalDifficultiesSum / totalCompletedHangs;
       } else { averageWorkload = 0; }
       generalInfo.append("Total unused time: ").append(totalUnusedTime).append("min\n");
       generalInfo.append("Average intensity: ").append(String.format(java.util.Locale.US,"%.3f",averageIntensity)).append("\n");
       generalInfo.append("Average difficulty per workout: ").append(averageDifficulty).append("\n");
       generalInfo.append("Average workload per workout: ").append(averageWorkload).append("\n");
       generalInfo.append("Average difficulty per minute: ").append("\n");
       generalInfo.append("Average workout power: ").append("\n");
       generalInfo.append("First workout date: ").append("\n");
       generalInfo.append("Average workouts per week: ").append("\n");


       generalInfoTextView.setText(generalInfo.toString() );



   }


}
