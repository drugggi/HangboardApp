package com.finn.laakso.hangboardapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

    Button editWorkoutButton;
    Button resetDBButton;

    // Parameters to hold all the information of workout history
    // consider dropping these or moving to where the graphs are displayed
    ArrayList<ArrayList<Hold>> arrayList_workoutHolds;
    ArrayList<TimeControls> allTimeControls;
    ArrayList<String> dates;
    Random rng;
    ArrayList<int[]> completedArrayList;

    ArrayList<Hold> workoutHolds;
    TimeControls timeControls;
    String hangboardName;
    int[] completed;

    ArrayList<Hold> selectedHolds;
    TimeControls selectedTimeControls;
    int[] selectedCompletedHangs;
    long selectedDate;
    String selectedHangboardName;

    // Adapter that manages the workout history with the help of SQLite
    WorkoutHistoryAdapter workoutAdapter;
    ListView workoutHistoryListView;

    int positionGlobal = 0;

    private static final int REQUEST_HANGS_COMPLETED = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_statistics);
        rng = new Random();
        workoutInfoTextView = (TextView) findViewById(R.id.workoutInfoTextView);
        holdInfoTextView = (TextView) findViewById(R.id.holdInfoTextView);
        editWorkoutButton = (Button) findViewById(R.id.editWorkoutButton);
        resetDBButton = (Button) findViewById(R.id.testButton);

        // DBHander to store workout from Intent.
        MyDBHandler dbHandler = new MyDBHandler(getApplicationContext(),null,null,1);

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

            completed = new int[timeControls.getGripLaps() * timeControls.getRoutineLaps()];

            for (int i = 0; i < completed.length ; i++) {
                completed[i] = 0;
            }

        }

        if(getIntent().hasExtra("com.finn.laakso.hangboardapp.COMPLETEDHANGS")) {
            completed = getIntent().getExtras().getIntArray("com.finn.laakso.hangboardapp.COMPLETEDHANGS");
        }


        long time = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
        Date resultdate = new Date(time);

        // Lets add workout information to database straight from the intent.
        dbHandler.addHangboardWorkout(time,hangboardName, timeControls, workoutHolds,completed);

        // CREATES RANDOMIZED WORKOUT FOR TESTING PURPOSES
        //TimeControls rngControls = getRandomTimeControls();
        //dbHandler.addHangboardWorkout(time- (long)rng.nextInt(1000*60*60*24*300),"RNG " + hangboardName,rngControls,getRandomWorkoutHolds(rngControls.getHangLaps()),completed);


        // Creates randomized workouthistory for 50 workouts for variables not used right now, for testing purposes
       /*
        allTimeControls = new ArrayList<TimeControls>();
        dates = new ArrayList<String>();
        arrayList_workoutHolds = new ArrayList<ArrayList<Hold>>();
       // Log.d("after tc","tc was");
        for (int i = 0; i <50;i++ ) {
            allTimeControls.add(getRandomTimeControls());
            dates.add(getRandomDate());
            arrayList_workoutHolds.add(getRandomWorkoutHolds(allTimeControls.get(i).getGripLaps()));
        }
*/

        // Click listener for editing single workout
        editWorkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (positionGlobal == 0) { return; }

                Intent editWorkout = new Intent(getApplicationContext(), EditWorkoutInfo.class);

                // Lets pass the necessary information to WorkoutActivity; time controls, hangboard image, and used holds with grip information
                editWorkout.putExtra("com.finn.laakso.hangboardapp.TIMECONTROLS",selectedTimeControls.getTimeControlsIntArray() );
                editWorkout.putExtra("com.finn.laakso.hangboardapp.BOARDNAME",selectedHangboardName);
                editWorkout.putParcelableArrayListExtra("com.finn.laakso.hangboardapp.HOLDS", selectedHolds);
                editWorkout.putExtra("com.finn.laakso.hangboardapp.COMPLETEDHANGS",selectedCompletedHangs);

                setResult(Activity.RESULT_OK,editWorkout);
                startActivityForResult(editWorkout, REQUEST_HANGS_COMPLETED);

            }
        });

        workoutAdapter = new WorkoutHistoryAdapter(this,dates, allTimeControls,arrayList_workoutHolds);

        workoutHistoryListView = (ListView) findViewById(R.id.workoutHistoryListView);
        workoutHistoryListView.setAdapter(workoutAdapter);

        workoutHistoryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                MyDBHandler dbHandler = new MyDBHandler(getApplicationContext(),null,null,1);

                //dbHandler.updateCompletedHangs(position);

                // dbHandler.addHangboardWorkout(allTimeControls.get(0).getTotalTime(),"Metolius",allTimeControls.get(position+1),arrayList_workoutHolds.get(position+1));

                // Get Selected items workoutmatrix
                // workoutInfoTextView.setText(workoutAdapter.getSelectedTimeControls(position).getGripMatrix(true));
                // String holdinfo = workoutAdapter.getSelectedHoldInfo(position);

                if ( position == 0) {
                    holdInfoTextView.setText("" + dbHandler.lookUpDate(position));
                }

                position++;

                positionGlobal = position;


                String he = "";
                ArrayList<Hold> test = dbHandler.lookUpHolds(position);
                selectedHolds = dbHandler.lookUpHolds(position);
                selectedDate = dbHandler.lookUpDate(position);
                selectedCompletedHangs = dbHandler.lookUpCompletedHangs(position);
                selectedTimeControls = dbHandler.lookUpTimeControls(position);
                selectedHangboardName = dbHandler.lookUpHangboard(position);

                workoutInfoTextView.setText(selectedTimeControls.getTimeControlsAsString());
                holdInfoTextView.setText(selectedTimeControls.getGripMatrix(false));

            }
        });

        // Reset button for clearing all database entries, for testing purposes right now
        resetDBButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDBHandler dbHandler = new MyDBHandler(getApplicationContext(),null,null,1);
                dbHandler.DELETEALL();

                Toast.makeText(WorkoutStatistics.this, "All DELETED, Happy now",Toast.LENGTH_LONG).show();
                workoutAdapter.notifyDataSetChanged();

            }
        });

        // Clearing the database, button is now used instead.
        /*
        holdInfoTextView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                MyDBHandler dbHandler = new MyDBHandler(getApplicationContext(),null,null,1);
                dbHandler.DELETEALL();

                Toast.makeText(WorkoutStatistics.this, "All DELETED, Happy now",Toast.LENGTH_LONG).show();


                return true;
            }
        });
*/

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_HANGS_COMPLETED) {
            Toast.makeText(WorkoutStatistics.this," results ok",Toast.LENGTH_SHORT).show();

            if (getIntent().hasExtra("com.finn.laakso.hangboardapp.COMPLETEDHANGS")) {

               // Toast.makeText(WorkoutStatistics.this," oli intentti",Toast.LENGTH_SHORT).show();

                int[] completed = data.getIntArrayExtra("com.finn.laakso.hangboardapp.COMPLETEDHANGS");

               // String terver = getIntent().getStringExtra("com.finn.laakso.hangboardapp.TERVEHDYS");

               // Toast.makeText(WorkoutStatistics.this," oli intentti" + terver,Toast.LENGTH_SHORT).show();


                MyDBHandler dbHandler = new MyDBHandler(getApplicationContext(),null,null,1);

                int[] temp = dbHandler.lookUpCompletedHangs(positionGlobal);

                dbHandler.updateCompletedHangs(positionGlobal,completed);

                temp = dbHandler.lookUpCompletedHangs(positionGlobal);


                //dbHandler.updateCompletedHangs(2,completed);

            }
        }
        else {
            Toast.makeText(WorkoutStatistics.this," results not saved",Toast.LENGTH_SHORT).show();
        }

            /*
            int[] i = data.getIntArrayExtra("com.finn.laakso.hangboardapp.SETTINGS");

            // If Grip laps amount has been changed we have to randomize new grips, otherwise lets
            // keep the old grips that user has maybe liked
            if (i[0] != timeControls.getGripLaps()) {
                timeControls.setTimeControls(i);
                everyBoard.setGripAmount(timeControls.getGripLaps(), grade_descr_position);
                holdsAdapter = new ArrayAdapter<String>(MainActivity.this,
                        R.layout.mytextview, everyBoard.getGrips());
                holdsListView.setAdapter(holdsAdapter);
                hang_descr_position = 0;
            } else {
                timeControls.setTimeControls(i);
            }

            //Disable the slider and check box, so that those are accidentally changed
            Toast.makeText(MainActivity.this, "Settings applied, pre made time controls disabled ", Toast.LENGTH_SHORT).show();
            repeatersBox.setVisibility(View.INVISIBLE);
            durationSeekBar.setVisibility(View.INVISIBLE);
            durationTextView.setText("Duration: " + timeControls.getTotalTime() / 60 + "min");

        } // Enabling them when settings are not saved, in future must be made more intuitive.
        else {
            Toast.makeText(MainActivity.this, "Settings not applied, pre made time controls enabled", Toast.LENGTH_SHORT).show();
            repeatersBox.setVisibility(View.VISIBLE);
            durationSeekBar.setVisibility(View.VISIBLE);
            durationTextView.setText("Duration: " + timeControls.getTotalTime() / 60 + "min");
        }
*/

    }

    // Randomizers for creating all the data that is neede for testing SQLite database
    // TimeControls, Holds, Dates
    private ArrayList<Hold> getRandomWorkoutHolds(int number_of_holds) {

        ArrayList<Hold> newHolds = new ArrayList<Hold>();
        for (int i = 0;i < number_of_holds; i++) {
            newHolds.add(getNewRandomHold());      // left hand
            newHolds.add(getNewRandomHold()); // right hand

        }
        return newHolds;
    }

    public void setCompletedRandomly(int[] completed) {
        Random rng = new Random();
        for (int i = 0; i < completed.length; i++) {
            completed[i] = rng.nextInt(timeControls.getHangLaps());
        }
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
