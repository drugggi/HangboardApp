package com.finn.laakso.hangboardapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

public class WorkoutStatistics extends AppCompatActivity {

    TextView workoutInfoTextView;
    TextView holdInfoTextView;
    //GridView workoutGridView;

    Button editWorkoutButton;
    Button resetDBButton;

    ListView workoutHistoryListView;
    ArrayList<ArrayList<Hold>> arrayList_workoutHolds;
    ArrayList<TimeControls> allTimeControls;
    ArrayList<String> dates;
    Random rng;

    ArrayList<Hold> workoutHolds;
    TimeControls timeControls;
    String hangboardName;

    static final String[] numbers = new String[] {
            "A", "B", "C", "D", "E",
            "F", "G", "H", "I", "J",
            "K", "L", "M", "N", "O",
            "P", "Q", "R", "S", "T",
            "U", "V", "W", "X", "Y", "Z"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_statistics);
        rng = new Random();
        workoutInfoTextView = (TextView) findViewById(R.id.workoutInfoTextView);
        holdInfoTextView = (TextView) findViewById(R.id.holdInfoTextView);
        editWorkoutButton = (Button) findViewById(R.id.editWorkoutButton);
        resetDBButton = (Button) findViewById(R.id.testButton);

/*
        //workoutGridView = (GridView) findViewById(R.id.workoutGridView);

       // workoutGridView.setNumColumns(6);
        //workoutGridView.set

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, numbers);
        workoutGridView.setAdapter(adapter);

     workoutGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
         @Override
         public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
             Toast.makeText(getApplicationContext(),
                     ((TextView) view).getText(), Toast.LENGTH_SHORT).show();
         }
     });*/


        MyDBHandler dbHandler = new MyDBHandler(getApplicationContext(),null,null,1);


/*
        HangBoard testBoard = new HangBoard(getResources());

        workoutHistoryListView = (ListView) findViewById(R.id.workoutHistoryListView);
        final ArrayAdapter<String> gradeAdapter = new ArrayAdapter<String>(this, R.layout.gradetextview, testBoard.getGrades());
        workoutHistoryListView.setAdapter(gradeAdapter);*/

        // Holds that will be used in this workout program
        if (getIntent().hasExtra("com.finn.laakso.hangboardapp.HOLDS")) {
            workoutHolds = getIntent().getExtras().getParcelableArrayList("com.finn.laakso.hangboardapp.HOLDS");
        }

        // Hangboard image that user has selected
        if (getIntent().hasExtra("com.finn.laakso.hangboardapp.BOARDNAME")) {
            hangboardName = getIntent().getStringExtra("com.finn.laakso.hangboardapp.BOARDNAME");
        }

        // This Intent brings the time controls to the workout program
        if (getIntent().hasExtra("com.finn.laakso.hangboardapp.TIMECONTROLS")) {
            int[] time_controls = getIntent().getExtras().getIntArray("com.finn.laakso.hangboardapp.TIMECONTROLS");

            timeControls = new TimeControls();
            timeControls.setTimeControls(time_controls);

            // SECURITY CHECK, WILL MAKE SURE IN FUTURE TO NEVER HAPPEN
            if (timeControls.getGripLaps()*2 != workoutHolds.size()) {
                timeControls.setGripLaps(workoutHolds.size()/2);
            }
        }
        long time = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
        Date resultdate = new Date(time);
        //Log.e("long time: "," " + time);
        //Log.e("result date", sdf.format(resultdate));
        dbHandler.addHangboardWorkout(time,hangboardName, timeControls, workoutHolds);

        TimeControls rngControls = getRandomTimeControls();
        dbHandler.addHangboardWorkout(time- (long)rng.nextInt(1000*60*60*24*300),"RNG " + hangboardName,rngControls,getRandomWorkoutHolds(rngControls.getHangLaps()));

       // Log.d("before tc","tc next");
        allTimeControls = new ArrayList<TimeControls>();
        dates = new ArrayList<String>();
        arrayList_workoutHolds = new ArrayList<ArrayList<Hold>>();
       // Log.d("after tc","tc was");
        for (int i = 0; i <50;i++ ) {
            allTimeControls.add(getRandomTimeControls());
            dates.add(getRandomDate());
            arrayList_workoutHolds.add(getRandomWorkoutHolds(allTimeControls.get(i).getGripLaps()));
        }


/*
        java.sql.Date c = Calendar.getInstance().getTime();
        Toast.makeText(this,"arraylist sizee: " + arrayList_workoutHolds.size(),Toast.LENGTH_SHORT).show();

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(c);

        Toast.makeText(this, "arraylist 1 item size" +arrayList_workoutHolds.get(0).size(),Toast.LENGTH_LONG).show();

        //String[] dates = {formattedDate,"tuesday","wednesday"};
        // String[] hangboards = {"bm100","bm2000","zlag"};
        // String[] difficulty = {"hard","easy","etc"};
*/

        editWorkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent editWorkout = new Intent(getApplicationContext(), EditWorkoutInfo.class);

                // Lets pass the necessary information to WorkoutActivity; time controls, hangboard image, and used holds with grip information
                editWorkout.putExtra("com.finn.laakso.hangboardapp.TIMECONTROLS",timeControls.getTimeControlsIntArray() );
                editWorkout.putExtra("com.finn.laakso.hangboardapp.BOARDNAME",hangboardName);
                editWorkout.putParcelableArrayListExtra("com.finn.laakso.hangboardapp.HOLDS", workoutHolds);

                startActivity(editWorkout);
            }
        });

        final WorkoutHistoryAdapter workoutAdapter = new WorkoutHistoryAdapter(this,dates, allTimeControls,arrayList_workoutHolds);

        workoutHistoryListView = (ListView) findViewById(R.id.workoutHistoryListView);
        workoutHistoryListView.setAdapter(workoutAdapter);

        workoutHistoryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                MyDBHandler dbHandler = new MyDBHandler(getApplicationContext(),null,null,1);
                // dbHandler.addHangboardWorkout(allTimeControls.get(0).getTotalTime(),"Metolius",allTimeControls.get(position+1),arrayList_workoutHolds.get(position+1));

                // Get Selected items workoutmatrix
                // workoutInfoTextView.setText(workoutAdapter.getSelectedTimeControls(position).getGripMatrix(true));
                // String holdinfo = workoutAdapter.getSelectedHoldInfo(position);

                if ( position == 0) {
                    holdInfoTextView.setText("" + dbHandler.lookUpDate(position));

                }

                String he = "";
                ArrayList<Hold> test = dbHandler.lookUpHolds(position);
/*
                String text = "";
                for (int i = 0 ; i < test.size() ; i++) {

                    text = text + test.get(i).getHoldNumber() +test.get(i).getHoldText() +test.get(i).getHoldValue() + "\n";

                    Log.e("hld nro: " , ": " + test.get(i).getHoldNumber());
                    Log.e("grip type: " , ": " + test.get(i).getHoldText());
                    Log.e("hold value: " , ": " + test.get(i).getHoldValue());
                }
                workoutInfoTextView.setText(text);
                */
                TimeControls temp = dbHandler.lookUpTimeControls(position);
                //holdInfoTextView.setText(temp.getGripMatrix(true));

                // workoutGridView.setNumColumns(temp.getGripLaps());
                Log.e("griplaps"," " + temp.getGripLaps());
                //workoutGridView.set

                // ArrayList<String> testList = new ArrayList<>();
                String[] testList = new String[test.size()/2 * temp.getRoutineLaps()];

                for (int i = 0 ; i < testList.length ; i++) {

                    testList[i] = "" + temp.getHangLaps();
                }

                Log.e("testlistsize", " " + testList.length);

                /*
                int j = 0;
                    for (int i = 0; i < testList.length; i++) {
                        if (2 *j >= test.size()) {
                            j = 0;
                        }
                        testList[i] = test.get(2 * j).getHoldNumber() + " " + test.get(2 * j).getHoldText();
                        j++;
//                    testList.(test.get(i).getHoldNumber() + " " + test.get(i).getHoldText());
                    }
*/

                /*
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(WorkoutStatistics.this,
                        android.R.layout.simple_list_item_1, testList);
                workoutGridView.setAdapter(adapter);
*/
            }
        });

        resetDBButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDBHandler dbHandler = new MyDBHandler(getApplicationContext(),null,null,1);
                dbHandler.DELETEALL();

                Toast.makeText(WorkoutStatistics.this, "All DELETED, Happy now",Toast.LENGTH_LONG).show();


            }
        });

        holdInfoTextView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                MyDBHandler dbHandler = new MyDBHandler(getApplicationContext(),null,null,1);
                dbHandler.DELETEALL();

                Toast.makeText(WorkoutStatistics.this, "All DELETED, Happy now",Toast.LENGTH_LONG).show();


                return true;
            }
        });


    }

    private ArrayList<Hold> getRandomWorkoutHolds(int number_of_holds) {

        ArrayList<Hold> newHolds = new ArrayList<Hold>();
        for (int i = 0;i < number_of_holds; i++) {
            newHolds.add(getNewRandomHold());      // left hand
            newHolds.add(getNewRandomHold()); // right hand

        }
        return newHolds;
    }

    private Hold getNewRandomHold() {
        Hold newHold = new Hold(rng.nextInt(20)+1);
        newHold.setHoldValue(rng.nextInt(100)+1);
        int i_hold_bot_info = (rng.nextInt(8)+1)*10;


        i_hold_bot_info = i_hold_bot_info + rng.nextInt(1);
        newHold.setGripTypeAndSingleHang(i_hold_bot_info);
        return newHold;
    }

    private TimeControls getRandomTimeControls() {
       // Log.d("heh: ", "i next");
         int[] i = {rng.nextInt(30)+1,rng.nextInt(10)+1,rng.nextInt(9)+1,rng.nextInt(5),rng.nextInt(4)+1,rng.nextInt(200)+1,rng.nextInt(500)+1};
        //int h = rng.nextInt(50);
        //int[] i = {6,6,7,3,3,150,555};
        //Log.d("heh: ", "" + i.length);
        TimeControls randomTimeControls = new TimeControls();
        randomTimeControls.setTimeControls(i);
        // rng.nextInt()+1;
       // Log.d("heh: ", "" + i.length);
        return  randomTimeControls;
    }

    private String getRandomDate() {
        int paiva = rng.nextInt(30)+1;
        int vuosi = rng.nextInt(28)+1990;
        String kk = "Jun";

        int i = rng.nextInt(6);
        if (i == 0) {kk = "Jan"; }
        else if (i==1) {kk = "Feb"; }
        else if (i == 2) {kk = "May"; }
        else if (i==3) {kk = "Aug"; }
        else if ( i == 4) { kk = "Apr"; }

        return paiva + "-" + kk + "-" + vuosi;
    }
}
