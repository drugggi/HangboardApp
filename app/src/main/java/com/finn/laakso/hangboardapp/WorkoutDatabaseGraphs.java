package com.finn.laakso.hangboardapp;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WorkoutDatabaseGraphs extends AppCompatActivity {

    MyDBHandler dbHandler;

    private float[] yData = {25.6f,10.6f,66.5f,32f,23.2f,24.4f,32.3f,3.2f};
    private String[] xData = {"yksi","kaksi","kolme","neljä,","ivisi","kuusi","seitsäm","kasi"};
    PieChart pieChart;
    PieChart pieChart2;
    PieChart pieChart3;

    PieChart pieChart4;

    LineChart chart;

    Random random;
    ArrayList<BarEntry> barEntries;

    PieChart gripDistributionPieChart;

    ArrayList<ArrayList<Hold>> arrayList_workoutHolds;
    ArrayList<TimeControls> allTimeControls;
    ArrayList<Long> dates;
    ArrayList<int[]> completedArrayList;
    ArrayList<String> hangboards;

    Random rng;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_database_graphs);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        rng = new Random();
        dbHandler = new MyDBHandler(getApplicationContext(),null,null,1);
        int count = dbHandler.lookUpWorkoutCount();

        Toast.makeText(this,"count: " + count,Toast.LENGTH_SHORT).show();

        // Retrieving all the workout history data from SQLite database so that it can be presented
        // on the screen in graph form.

        retrieveDataFromDatabaseToArrayLists();

        createGripDistributionPieChart();

        Log.e("timeControls size"," " + allTimeControls.size());

        pieChart= (PieChart) findViewById(R.id.piechart);
        pieChart2= (PieChart) findViewById(R.id.piechart2);
        pieChart3= (PieChart) findViewById(R.id.piechart3);
        pieChart4= (PieChart) findViewById(R.id.idPieChart);
        chart = (LineChart) findViewById(R.id.chart);

        List<Entry> entries = new ArrayList<Entry>();

        int j = 0;
        for (TimeControls singleTUT: allTimeControls) {
            entries.add(new Entry(j++,rng.nextInt(100)));
        }
        /*
        entries.add(new Entry(1,5));
        entries.add(new Entry(2,15));
        entries.add(new Entry(3,21));
        entries.add(new Entry(4,17));
        entries.add(new Entry(5,52));
        entries.add(new Entry(6,54));
        entries.add(new Entry(7,35));
        entries.add(new Entry(8,11));
        entries.add(new Entry(9,87));
        entries.add(new Entry(10,12)); */

        LineDataSet linedataSet = new LineDataSet(entries, "testi");
        linedataSet.setColor(Color.BLUE);

        LineData lineData = new LineData(linedataSet);
        chart.setData(lineData);
        chart.invalidate();


        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);
        pieChart.setExtraOffsets(5,10,5,5);

        pieChart.setDragDecelerationFrictionCoef(0.95f);

        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(Color.WHITE);
        pieChart.setTransparentCircleRadius(61f);

        ArrayList<PieEntry> yValues = new ArrayList<>();

        yValues.add(new PieEntry(34f,"partyA"));
        yValues.add(new PieEntry(23f,"usa"));
        yValues.add(new PieEntry(14f,"uk"));
        yValues.add(new PieEntry(35f,"neljä"));
        yValues.add(new PieEntry(40f,"pviisA"));
        yValues.add(new PieEntry(23f,"kuus"));



        PieDataSet dataSet = new PieDataSet(yValues,"Countries");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);
        dataSet.setColors(ColorTemplate.JOYFUL_COLORS);

        PieData data = new PieData(dataSet);
        data.setValueTextSize(10f);
        data.setValueTextColor(Color.YELLOW);
        pieChart.setData(data);
        pieChart2.setData(data);
        pieChart3.setData(data);

       //  LineChart chart = (LineChart) findViewById(R.id.chart);
        //LineChart chart = (LineChart) findViewById(R.id.chart);
        pieChart4.setRotationEnabled(true);
        pieChart4.setHoleRadius(25f);
        pieChart4.setTransparentCircleAlpha(0);
        pieChart4.setCenterText("super cool chart");
pieChart4.setCenterTextSize(10);

        ArrayList<PieEntry> yEntrys = new ArrayList<>();
        ArrayList<String> xEntrys = new ArrayList<>();

        for (int i = 0 ; i < yData.length ; i++ ) {
            yEntrys.add(new PieEntry(yData[i],i));
        }

        for (int i = 0 ; i < xData.length ; i++ ) {
            xEntrys.add(xData[i]);
        }

        // Create data set
        PieDataSet pieDataSet = new PieDataSet(yEntrys,"employee sales");
        pieDataSet.setSliceSpace(2f);
        pieDataSet.setValueTextSize(12f);

        // create pie data object
        PieData piedata = new PieData(pieDataSet);
        pieChart4.setData(piedata);
        pieChart4.invalidate();




        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


    }

    public void createGripDistributionPieChart() {
        gripDistributionPieChart = (PieChart) findViewById(R.id.gripDistributionPieChart);
        gripDistributionPieChart.setCenterText("Each grip type used in seconds");

        ArrayList<PieEntry> yValues = new ArrayList<>();


        int fourfinger = 0;
        int threefront = 0;
        int threeback= 0;
        int twofront = 0;
        int twomiddle = 0;
        int twoback = 0;
        int middlefinger = 0;
        int other = 0;

        int seconds_multiplier = 0;
        int hold_index = 0;

        Hold.grip_type gripType;

        // All Holds ever used
        for (int i = 0 ; i < arrayList_workoutHolds.size() ; i++ ) {

            // Holds used in a single workout
            for (int j = 0 ; j < completedArrayList.get(i).length ; j ++) {

                // multiplier depending if the hang were successfull or not
                seconds_multiplier = completedArrayList.get(i)[j] * allTimeControls.get(i).getTimeON();
                hold_index = 2*( j % allTimeControls.get(i).getGripLaps());

               // Log.e("workout Holds: index: ",hold_index + "   text: "+ arrayList_workoutHolds.get(i).get(hold_index).getHoldText());

                gripType = arrayList_workoutHolds.get(i).get(hold_index).getGripStyle();
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
        /*
        for (ArrayList<Hold> holds: arrayList_workoutHolds) {
            for (Hold current_hold: holds) {
                gripType = current_hold.getGripStyle();
                if (gripType == Hold.grip_type.FOUR_FINGER) { fourfinger++; }
                else if (gripType == Hold.grip_type.THREE_FRONT) {threefront++; }
                else if ( gripType == Hold.grip_type.THREE_BACK) {threeback++; }
                else {twomiddle++; }
            }
        }*/

        yValues.add(new PieEntry(fourfinger,"Four fingers"));
        yValues.add(new PieEntry(threefront,"Three front"));
        yValues.add(new PieEntry(threeback,"Three back"));
        yValues.add(new PieEntry(twomiddle,"Two middle"));
        yValues.add(new PieEntry(twofront,"Two front"));
        yValues.add(new PieEntry(twoback,"Two back"));
        yValues.add(new PieEntry(middlefinger,"Middle finger"));
        yValues.add(new PieEntry(other,"other"));

        PieDataSet dataSet = new PieDataSet(yValues,"Grip distribution");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);

        PieData data = new PieData(dataSet);
        data.setValueTextSize(10f);
        data.setValueTextColor(Color.YELLOW);
        gripDistributionPieChart.setData(data);


    }

    public void retrieveDataFromDatabaseToArrayLists() {

        int datapoints = dbHandler.lookUpWorkoutCount();

        arrayList_workoutHolds = new ArrayList<ArrayList<Hold>>();
        allTimeControls = new ArrayList<TimeControls>();
        dates = new ArrayList<Long>();
        completedArrayList = new ArrayList<int[]>();
        hangboards = new ArrayList<String>();

        // first item in SQLite database is at 1
        for (int i = 1 ; i <= datapoints ; i++) {
            arrayList_workoutHolds.add(dbHandler.lookUpHolds(i));
            allTimeControls.add(dbHandler.lookUpTimeControls(i));
            dates.add(dbHandler.lookUpDate(i));
            completedArrayList.add(dbHandler.lookUpCompletedHangs(i));
            hangboards.add(dbHandler.lookUpHangboard(i));

        }
    }


}
