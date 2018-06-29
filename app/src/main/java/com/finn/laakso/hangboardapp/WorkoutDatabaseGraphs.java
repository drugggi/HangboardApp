package com.finn.laakso.hangboardapp;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

public class WorkoutDatabaseGraphs extends AppCompatActivity {

    MyDBHandler dbHandler;

    PieChart gripDistributionPieChart;
    BarChart difficultyBarChart;
    BarChart workoutDatesBarChart;
    HorizontalBarChart singleHangsOrRepeatersBarChart;
    LineChart timeUnderTensionLineChart;
    LineChart workoutTimeLineChart;
    LineChart workoutIntensityLineChart;
    LineChart workoutTUTandWTLineChart;

    ArrayList<ArrayList<Hold>> arrayList_workoutHolds;
    ArrayList<TimeControls> allTimeControls;
    ArrayList<Long> dates;
    ArrayList<int[]> completedArrayList;
    ArrayList<String> hangboards;

    ArrayList<Integer> effectiveWorkoutTUT;
    ArrayList<Integer> effectiveWorkoutTime;

    TextView generalInfoTextView;

    ArrayList<String> stringDates;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_database_graphs);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        dbHandler = new MyDBHandler(getApplicationContext(),null,null,1);

        generalInfoTextView = (TextView) findViewById(R.id.generalInfoTextView);

        // Sorted by latest workout at 0 and the first workout at last item
        effectiveWorkoutTime = new ArrayList<>();
        effectiveWorkoutTUT = new ArrayList<>();

        // Retrieving all the workout history data from SQLite database so that it can be presented
        // on the screen in graph form.

        Long startTime = System.currentTimeMillis();
        long breakTime = 0;

        retrieveDataFromDatabaseToArrayLists();

        breakTime = System.currentTimeMillis()- startTime;
        Log.e("time retrieve data: ", " " + breakTime + "ms");

        createWorkoutTUTandWTLineChart();

        // Needs some love, currently maybe stable
        stringDates= new ArrayList<>();
        createWorkoutDatesBarChart();

        createSingleHangsOrRepeatersBarChart();

        createWorkoutIntensityLineChart();

        createTotalWorkoutTimeLineChart();

        createTimeUnderTensionLineChart();

        createDifficultyBarChart();

        createGripDistributionPieChart();

        breakTime = System.currentTimeMillis()- startTime;
        Log.e("time to plot graphs: ", " " + breakTime + "ms");

/*
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

*/
    }

    public void createWorkoutTUTandWTLineChart() {
        workoutTUTandWTLineChart = (LineChart) findViewById(R.id.workoutTUTandWTLineChart);

        ArrayList<Entry> entriesTUT = new ArrayList<Entry>();
        ArrayList<Entry> entriesWT = new ArrayList<Entry>();

        int xCoord = 0;
        for (int i = effectiveWorkoutTime.size()-1 ; i >= 0 ; i--) {

            entriesTUT.add(new Entry(xCoord, ((float)effectiveWorkoutTUT.get(i)) ));
            entriesWT.add(new Entry(xCoord, ((float) effectiveWorkoutTime.get(i)) ));
            xCoord++;
        }

        String[] labels = new String[effectiveWorkoutTime.size()-1];
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



    }

    public void createWorkoutDatesBarChart() {
        workoutDatesBarChart = (BarChart) findViewById(R.id.workoutDatesBarChart);

        Cursor dbSortedCursor = dbHandler.getSortedContents();

        ArrayList<Long> datesByLongs = new ArrayList<>();
        ArrayList<Date> allDates = new ArrayList<>();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
/*
        while (dbSortedCursor.moveToNext() ) {
            datesByLongs.add(dbSortedCursor.getLong(1));
            allDates.add(new Date(dbSortedCursor.getLong(1)));
        }
*/
        ArrayList<BarEntry> entries = new ArrayList<>();

        long difference = 0L;
        ArrayList<Integer> dayDifferences = new ArrayList<>();

        // Calculate dates varaibles time difference by day
        for (int i = 0; i < dates.size() ; i ++) {

            // compare every date to the first dave by subtracting from it thats the difference
            difference = - (dates.get(dates.size()-1) - dates.get(i) )/( 1000*60*60*24);
            dayDifferences.add( (int) difference);

        }

        // Lets populate the entries by starting at day 0 (dayDifference = first record) and finishing
        // the latest workout day x and corresponding workoutTime
        for (int i = dayDifferences.size()-1; i >= 0 ; i--) {
            // Log.e("data point ", dayDifferences.get(i) + " : "+effectiveWorkoutTime.get(i)/60);
            entries.add(new BarEntry(dayDifferences.get(i) , (float)effectiveWorkoutTime.get(i)/60 ));
        }

        BarDataSet barDataSet = new BarDataSet(entries,"Workout time for each day from first to last (min)");
        barDataSet.setColors(ColorTemplate.VORDIPLOM_COLORS);
        BarData barData = new BarData(barDataSet);
        workoutDatesBarChart.setData(barData);

        // Lets populate the x axis that dont have workouts in them
        String[] labels = new String[dayDifferences.get(0) + 1];
        for (int i = 0 ; i < labels.length ; i++ ) {
            labels[i] = "day: " + i;
        }

        labels[labels.length-1] = sdf.format(dates.get(dates.size()-1));
        labels[0] = sdf.format(dates.get(0));

       // Collections.reverse(dayDifferences);
        int test = 0;
        for (int i = 0 ; i < dayDifferences.size()  ; i++) {

            test = dayDifferences.get(i);

            if (test < 0 || test >= labels.length) {
                Log.e("ERROR: " , test + " vs. " + labels.length);
                break;
            }

            labels[dayDifferences.get(i)] = sdf.format(dates.get(i));
            Log.e(" labels i",i + " :  " + sdf.format(dates.get(i)));
        }

        // BarData theData = new BarData(bardataset);

        Description desc = new Description();
        desc.setText("Workout day number");

        workoutDatesBarChart.setDescription(desc);
        XAxis xAxis = workoutDatesBarChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
        xAxis.setGranularity(1f);
        //xAxis.setGranularityEnabled(true);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        // SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");

       // Date resultdate = new Date(dbHandler.lookUpDate(1));

        // createRandomBarGraph(sdf.format(allDates.get(allDates.size()-1)),sdf.format(allDates.get(0)));

    }

    public void createRandomBarGraph(String stringDate1, String stringDate2) {

        workoutDatesBarChart = (BarChart) findViewById(R.id.workoutDatesBarChart);

        Random rng = new Random();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
        ArrayList<BarEntry> barEntries = new ArrayList<>();
        try {
            Date date1 = simpleDateFormat.parse(stringDate1);
            Date date2 = simpleDateFormat.parse(stringDate2);

            Calendar mDate1 = Calendar.getInstance();
            Calendar mDate2 = Calendar.getInstance();
            mDate1.clear();
            mDate2.clear();

            mDate1.setTime(date1);
            mDate2.setTime(date2);

            stringDates = new ArrayList<>();
            stringDates  = getList(mDate1,mDate2);

            float max = 0f;
            float value = 0f;
            for (int j = 0; j < stringDates.size() ; j++ ) {
                max = 100f;
                value = rng.nextFloat()*max;
                barEntries.add(new BarEntry(j,value));
            }


        } catch (ParseException e) {
            e.printStackTrace();
        }

        BarDataSet barDataSet = new BarDataSet(barEntries,"Dates");
        BarData barData = new BarData(barDataSet);
        workoutDatesBarChart.setData(barData);

        String[] labels = new String[6];

        labels[0] = "0 date";
        labels[1] = "1 date " ;
        labels[2] = "2 date ";
        labels[3] = "3 date";
        labels[4] = "4 date " ;
        labels[5] = "5 date ";
        // BarData theData = new BarData(bardataset);

        Description desc = new Description();
        desc.setText(" ");

        XAxis xAxis = workoutDatesBarChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
        //xAxis.setGranularity(1f);
        //xAxis.setGranularityEnabled(true);
        xAxis.setPosition(XAxis.XAxisPosition.TOP_INSIDE);


    }

    public ArrayList<String> getList(Calendar startDate, Calendar endDate) {
        ArrayList<String> list = new ArrayList<>();
        while (startDate.compareTo(endDate) <= 0) {
            list.add(getDate(startDate));
            startDate.add(Calendar.DAY_OF_MONTH,1);
        }
        return list;
    }

    public String getDate(Calendar cld) {
        String curDate = cld.get(Calendar.YEAR) + "/" + (cld.get(Calendar.MONTH)+1) + "/" +
                cld.get(Calendar.DAY_OF_MONTH);

        try  {
            Date date = new SimpleDateFormat("yyyy/MM/dd").parse(curDate);
            curDate = new SimpleDateFormat("yyyy/MM/dd").format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return curDate;
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

        BarDataSet bardataset = new BarDataSet(entries,"Number of workouts" );


        bardataset.setColors(ColorTemplate.VORDIPLOM_COLORS);


        String[] labels = new String[3];

        labels[0] = " ";
        labels[1] = "single hangs: " + singleHangsAmount;
        labels[2] = "repeaters. " + repeatersAmount;
        BarData theData = new BarData(bardataset);

        Description desc = new Description();
        desc.setText(" ");

        //singleHangsOrRepeatersBarChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels));
        singleHangsOrRepeatersBarChart.setData(theData);
        singleHangsOrRepeatersBarChart.setDescription(desc);

        XAxis xAxis = singleHangsOrRepeatersBarChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
        xAxis.setGranularity(1f);
        xAxis.setGranularityEnabled(true);
        xAxis.setPosition(XAxis.XAxisPosition.TOP_INSIDE);

    }

    public void createWorkoutIntensityLineChart() {
        workoutIntensityLineChart = (LineChart) findViewById(R.id.workoutIntensityChart);
        List<Entry> entries = new ArrayList<Entry>();

        float intensityPercent = 0;
        for (int i = 0 ; i < effectiveWorkoutTime.size() ; i++) {

            intensityPercent = (float) effectiveWorkoutTUT.get(i) / (float) effectiveWorkoutTime.get(i);

            entries.add(new Entry(i,intensityPercent));
        }

        LineDataSet linedataSet = new LineDataSet(entries, "Intensity per workout = workout time / time under tension");
        linedataSet.setColor(Color.MAGENTA);
        //linedataSet.setColors(ColorTemplate.JOYFUL_COLORS);

        LineData lineData = new LineData(linedataSet);


        if (effectiveWorkoutTime.size() > 30) {
            lineData.setValueTextSize(0f);

        }

        workoutIntensityLineChart.setData(lineData);
    }

    public void createTotalWorkoutTimeLineChart() {
         workoutTimeLineChart = (LineChart) findViewById(R.id.workoutTimeChart);

        List<Entry> entries = new ArrayList<Entry>();

        for (int i = 0 ; i < effectiveWorkoutTime.size() ; i++) {
            entries.add(new Entry(i,effectiveWorkoutTime.get(i)/60));
        }

        LineDataSet linedataSet = new LineDataSet(entries, "Total workout time in minutes");
        linedataSet.setColor(Color.DKGRAY);

        LineData lineData = new LineData(linedataSet);

        if (effectiveWorkoutTime.size() > 30) {
            lineData.setValueTextSize(0f);

        }

        workoutTimeLineChart.setData(lineData);

    }

    public void createTimeUnderTensionLineChart() {
        timeUnderTensionLineChart = (LineChart) findViewById(R.id.timeUnderTensionChart);

        List<Entry> entries = new ArrayList<Entry>();

        for (int i = 0 ; i < effectiveWorkoutTUT.size() ; i++) {
            entries.add(new Entry(i+1,effectiveWorkoutTUT.get(i)));
        }

        LineDataSet linedataSet = new LineDataSet(entries, "Time under tension in seconds for every workout");
        linedataSet.setColor(Color.CYAN);

        LineData lineData = new LineData(linedataSet);

        if (effectiveWorkoutTUT.size() > 30) {
            lineData.setValueTextSize(0f);

        }

        timeUnderTensionLineChart.setData(lineData);
        // timeUnderTensionLineChart.animateXY(1000,1000);


    }

    public void createDifficultyBarChart() {
        difficultyBarChart = (BarChart) findViewById(R.id.difficultyBarChart);
        //difficultyBarChart.setDrawBarShadow(true);

        ArrayList<BarEntry> entries = new ArrayList<>();

        int[] completed_seconds;
        int[] grip_difficulties;

        TreeMap<Integer,Integer> difficultyMap = new TreeMap<Integer, Integer>();

        // all hold in workout history
        for (int i = 0 ; i < arrayList_workoutHolds.size() ; i++ ) {

            // Holds used in a single workout
            completed_seconds = new int[allTimeControls.get(i).getGripLaps()];
            grip_difficulties = new int[allTimeControls.get(i).getGripLaps()];

            // lets get single workouts grip difficulties,
            for (int j = 0 ; j < grip_difficulties.length; j++) {
                grip_difficulties[j] = (arrayList_workoutHolds.get(i).get(2*j).getHoldValue() +
                        arrayList_workoutHolds.get(i).get(2*j+1).getHoldValue() ) / 2;
            }
            // lets get single workouts completed hangs in seconds
            for (int j = 0 ; j < completedArrayList.get(i).length ; j++) {
                completed_seconds[j % allTimeControls.get(i).getGripLaps()] += completedArrayList.get(i)[j] * allTimeControls.get(i).getTimeON();
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

            //printIntArray(grip_difficulties,"Grip Difficulties");
            //printIntArray(completed_seconds,"Completed seconds");

        }
        ArrayList<Integer> barColors = new ArrayList<Integer>();
        int increment = 200/difficultyMap.size();
        int alpha = 0;
        for(Map.Entry<Integer,Integer> entry: difficultyMap.entrySet()) {
            int key = entry.getKey();
            int value = entry.getValue();

            value = value;

            if ( value == 0) {continue;}
            alpha += increment;
            barColors.add(Color.argb(50+alpha,200,0,0));
            entries.add(new BarEntry(key,value));
            // Log.e("test","increment: " + alpha);
        }

        BarDataSet dataset = new BarDataSet(entries,"Difficulty");
        //dataset.setLabel("TEST LABEL");
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


    }

    private void printIntArray(int[] printedArray,String arrayInfo) {
        StringBuilder sb = new StringBuilder(" ");
        for (int i = 0; i < printedArray.length ; i++) {
            sb.append(printedArray[i]+ ",");
        }
        Log.e("printIntArray: ",arrayInfo + sb.toString());
    }

    private void printIntArrayList(ArrayList<Integer> printedArrayList, String arrayInfo) {
        StringBuilder sb = new StringBuilder(" ");

        for (int i = 0; i < printedArrayList.size() ; i++) {
            sb.append(printedArrayList.get(i)+ ",");
        }
        Log.e("printIntArray: ",arrayInfo + sb.toString());


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
        int total_grips = 0;

        int seconds_multiplier = 0;
        int hold_index = 0;

        Hold.grip_type gripType;

        // All Holds ever used
        for (int i = 0 ; i < arrayList_workoutHolds.size() ; i++ ) {

            // Holds used in a single workout
            for (int j = 0 ; j < completedArrayList.get(i).length ; j ++) {

                // multiplier depending if the hang was successfull or not
                seconds_multiplier = completedArrayList.get(i)[j] * allTimeControls.get(i).getTimeON();
                hold_index = 2*( j % allTimeControls.get(i).getGripLaps());

               // Log.e("workout Holds: index: ",hold_index + "   text: "+ arrayList_workoutHolds.get(i).get(hold_index).getHoldText());

                gripType = arrayList_workoutHolds.get(i).get(hold_index).getGripStyle();
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

        if (fourfinger == 0 || total_grips/fourfinger > 50) {other += fourfinger;}
         else {yValues.add(new PieEntry(fourfinger,"Four fingers")); }

        if (threefront == 0 || total_grips/threefront >50) {  other += threefront;  }
        else { yValues.add(new PieEntry(threefront,"Three front")); }

        if (threeback == 0 || total_grips/threeback > 50) { other += threeback;}
        else { yValues.add(new PieEntry(threeback,"Three back")); }

        if (twofront == 0 || total_grips/twofront > 50) {other += twofront; }
        else {yValues.add(new PieEntry(twofront,"Two middle"));}

            if (twomiddle == 0 || total_grips/twomiddle > 50) {other += twomiddle; }
        else {yValues.add(new PieEntry(twomiddle,"Two front")); }

            if (twoback  == 0 || total_grips/twoback > 50) {other += twoback; }
        else {yValues.add(new PieEntry(twoback,"Two back")); }

            if (middlefinger == 0 || total_grips/middlefinger > 50) {other += middlefinger; }
        else {yValues.add(new PieEntry(middlefinger,"Middle finger")); }

        yValues.add(new PieEntry(other,"other"));

        PieDataSet dataSet = new PieDataSet(yValues,"Grip distribution");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);

        PieData data = new PieData(dataSet);
        data.setValueTextSize(10f);
        data.setValueTextColor(Color.YELLOW);
        gripDistributionPieChart.setData(data);
        // gripDistributionPieChart.animateY(1000);

        Description desc = new Description();
        desc.setText("Grip distribution");


    }

    public void retrieveDataFromDatabaseToArrayLists() {

        int datapoints = dbHandler.lookUpWorkoutCount();

        arrayList_workoutHolds = new ArrayList<ArrayList<Hold>>();
        allTimeControls = new ArrayList<TimeControls>();
        dates = new ArrayList<Long>();
        completedArrayList = new ArrayList<int[]>();
        hangboards = new ArrayList<String>();

        StringBuilder generalInfo = new StringBuilder("Workouts in Database: " + datapoints + "\n");

        long total_workout_time = 0;
        long total_time_under_tension = 0;
        long total_hang_laps = 0;
        long total_successful_hangs = 0;

        long total_adjusted_time_under_tension= 0;

        long erased_workout_time = 0;
        int single_erased_workout_time = 0;

        
        // first item in SQLite database is at 1
        for (int i = 1 ; i <= datapoints ; i++) {
            arrayList_workoutHolds.add(dbHandler.lookUpHolds(i));
            allTimeControls.add(dbHandler.lookUpTimeControls(i));
            dates.add(dbHandler.lookUpDate(i));
            completedArrayList.add(dbHandler.lookUpCompletedHangs(i));
            hangboards.add(dbHandler.lookUpHangboard(i));

            single_erased_workout_time = 0;
            for (int k = completedArrayList.get(i-1).length - 1 ; k >= 0 && completedArrayList.get(i-1)[k] == 0 ; k--) {

                if (k % allTimeControls.get(i-1).getGripLaps() == 0) {
                    single_erased_workout_time = single_erased_workout_time + allTimeControls.get(i-1).getLongRestTime()
                            + allTimeControls.get(i-1).getHangLaps() * (allTimeControls.get(i-1).getTimeON()
                            + allTimeControls.get(i-1).getTimeOFF());
                    erased_workout_time += single_erased_workout_time;

                } else {
                    single_erased_workout_time = single_erased_workout_time + allTimeControls.get(i-1).getRestTime()
                            + allTimeControls.get(i-1).getHangLaps() * (allTimeControls.get(i-1).getTimeON()
                            + allTimeControls.get(i-1).getTimeOFF());
                    erased_workout_time += single_erased_workout_time;
                }
            }
             effectiveWorkoutTime.add(allTimeControls.get(i-1).getTotalTime() - single_erased_workout_time);

            total_workout_time += allTimeControls.get(i-1).getTotalTime();
            total_time_under_tension += allTimeControls.get(i-1).getTimeUnderTension();
            total_hang_laps += allTimeControls.get(i-1).getHangLaps()*allTimeControls.get(i-1).getGripLaps()*allTimeControls.get(i-1).getRoutineLaps();

            int single_time_under_tension = 0;
            for (int j = 0 ; j < completedArrayList.get(i-1).length ; j++) {
                single_time_under_tension += completedArrayList.get(i-1)[j]*allTimeControls.get(i-1).getTimeON();

                total_successful_hangs += completedArrayList.get(i-1)[j];
            }
            total_adjusted_time_under_tension += single_time_under_tension;

             effectiveWorkoutTUT.add(single_time_under_tension);
        }

       // printIntArrayList(effectiveWorkoutTime, "WorkoutTime");
        //printIntArrayList(effectiveWorkoutTUT,"workoutTUT");

        generalInfo.append("(not adjusted) Total workout time: " + total_workout_time + "s where " + erased_workout_time + "s were inactive\n");
        generalInfo.append("Total workout time: " + (total_workout_time - erased_workout_time) + "s\n");
        generalInfo.append("(not adjusted) Total time under tension " + total_time_under_tension + "s\n");
        generalInfo.append("Total time under tension " + total_adjusted_time_under_tension + "s\n");
        generalInfo.append("Total of " + total_hang_laps + "hangs where " + total_successful_hangs + " were successful so " + 100*total_successful_hangs/total_hang_laps + "% is the success rate\n");
        generalInfo.append("Erased workout time: " + erased_workout_time);
        generalInfoTextView.setText(generalInfo.toString());
    }


}
