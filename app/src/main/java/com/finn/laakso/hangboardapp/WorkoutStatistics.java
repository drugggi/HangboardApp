package com.finn.laakso.hangboardapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

public class WorkoutStatistics extends AppCompatActivity {

    //TextView workoutInfoTextView;
    //TextView holdInfoTextView;

    Button editWorkoutButton;
    Button resetDBButton;
    Button showGraphsButton;
    Button newEntryButton;

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
    MyDBHandler dbHandler;

    private static final int REQUEST_HANGS_COMPLETED = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.e("crash test: "," 1");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_statistics);
        rng = new Random();
       // workoutInfoTextView = (TextView) findViewById(R.id.workoutInfoTextView);
       // holdInfoTextView = (TextView) findViewById(R.id.holdInfoTextView);
        editWorkoutButton = (Button) findViewById(R.id.editWorkoutButton);
        resetDBButton = (Button) findViewById(R.id.testButton);
        showGraphsButton = (Button) findViewById(R.id.showGraphsButton);
        newEntryButton = (Button) findViewById(R.id.newEntryButton);

        // DBHander to store workout from Intent.
        dbHandler = new MyDBHandler(getApplicationContext(),null,null,1);
        //dbHandler.DELETEALL();

        // Holds that will be used in this workout program
        if (getIntent().hasExtra("com.finn.laakso.hangboardapp.HOLDS")) {
            workoutHolds = getIntent().getExtras().getParcelableArrayList("com.finn.laakso.hangboardapp.HOLDS");

        }
        Log.e("crash test: "," 2");
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
        Log.e("crash test: "," 3");
        if(getIntent().hasExtra("com.finn.laakso.hangboardapp.COMPLETEDHANGS")) {
            completed = getIntent().getExtras().getIntArray("com.finn.laakso.hangboardapp.COMPLETEDHANGS");
        }


        long time = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
        Date resultdate = new Date(time);
        Log.e("crash test: "," 4");
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
       newEntryButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               long rngTime = System.currentTimeMillis();
               TimeControls rngControls = getRandomTimeControls();
               dbHandler.addHangboardWorkout(
                       rngTime- (long)1000*60*60*24*rng.nextInt(300),
                       "RNG " + hangboardName,
                       rngControls,
                       getRandomWorkoutHolds(rngControls.getGripLaps()),
                       getCompletedRandomly(rngControls));

                workoutAdapter.notifyDataSetChanged();
           }
       });

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

        workoutAdapter = new WorkoutHistoryAdapter(this,dbHandler);

        workoutHistoryListView = (ListView) findViewById(R.id.workoutHistoryListView);
        workoutHistoryListView.setAdapter(workoutAdapter);
        registerForContextMenu(workoutHistoryListView);

        workoutHistoryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

               // dbHandler = new MyDBHandler(getApplicationContext(),null,null,1);

                //dbHandler.updateCompletedHangs(position);

                // dbHandler.addHangboardWorkout(allTimeControls.get(0).getTotalTime(),"Metolius",allTimeControls.get(position+1),arrayList_workoutHolds.get(position+1));

                // Get Selected items workoutmatrix
                // workoutInfoTextView.setText(workoutAdapter.getSelectedTimeControls(position).getGripMatrix(true));
                // String holdinfo = workoutAdapter.getSelectedHoldInfo(position);


                position++;

                positionGlobal = position;

                // ArrayList<Hold> test = dbHandler.lookUpHolds(position);
                selectedHolds = dbHandler.lookUpHolds(position);
                selectedDate = dbHandler.lookUpDate(position);
                selectedCompletedHangs = dbHandler.lookUpCompletedHangs(position);
                selectedTimeControls = dbHandler.lookUpTimeControls(position);
                selectedHangboardName = dbHandler.lookUpHangboard(position);

                // workoutInfoTextView.setText(selectedTimeControls.getTimeControlsAsString());
                // holdInfoTextView.setText(selectedTimeControls.getGripMatrix(false));

            }
        });

        workoutHistoryListView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

                if (v.getId() == R.id.workoutHistoryListView) {
                    //Toast.makeText(EditWorkoutInfo.this, "Context Menu Created ", Toast.LENGTH_SHORT).show();

                    AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;

                        menu.setHeaderTitle("Choose your edit");
                        menu.add(Menu.NONE, 0, 0, "edit workout");
                        menu.add(Menu.NONE, 1, 1, "edit date");
                        menu.add(Menu.NONE, 2, 2, "hide/delete workout");


                }

            }
        });



        showGraphsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (dbHandler.lookUpWorkoutCount() == 0) {
                    Toast.makeText(WorkoutStatistics.this,"Database empty, cannot show graphs",Toast.LENGTH_SHORT).show();
                }
                else {
                    Intent showDatabaseGraphs = new Intent(getApplicationContext(),WorkoutDatabaseGraphs.class);
                    startActivity(showDatabaseGraphs);
                }
            }
        });

        // Reset button for clearing all database entries, for testing purposes right now
        resetDBButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // MyDBHandler dbHandler = new MyDBHandler(getApplicationContext(),null,null,1);
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


                int[] completed = data.getIntArrayExtra("com.finn.laakso.hangboardapp.COMPLETEDHANGS");

                dbHandler.updateCompletedHangs(positionGlobal,completed);


            }
        }
        else {
            Toast.makeText(WorkoutStatistics.this," results not saved",Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {


        // return super.onContextItemSelected(item);
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();

        int selectedListViewPosition = info.position+1;
        positionGlobal = info.position + 1;
        int selectedContextMenuItem = item.getItemId();

        selectedHolds = dbHandler.lookUpHolds(selectedListViewPosition);

        selectedDate = dbHandler.lookUpDate(selectedListViewPosition);

        selectedCompletedHangs = dbHandler.lookUpCompletedHangs(selectedListViewPosition);
        selectedTimeControls = dbHandler.lookUpTimeControls(selectedListViewPosition);
        selectedHangboardName = dbHandler.lookUpHangboard(selectedListViewPosition);

        if (selectedContextMenuItem == 0) {
            Intent editWorkout = new Intent(getApplicationContext(), EditWorkoutInfo.class);

            // Lets pass the necessary information to WorkoutActivity; time controls, hangboard image, and used holds with grip information
            editWorkout.putExtra("com.finn.laakso.hangboardapp.TIMECONTROLS",selectedTimeControls.getTimeControlsIntArray() );
            editWorkout.putExtra("com.finn.laakso.hangboardapp.BOARDNAME",selectedHangboardName);
            editWorkout.putParcelableArrayListExtra("com.finn.laakso.hangboardapp.HOLDS", selectedHolds);
            editWorkout.putExtra("com.finn.laakso.hangboardapp.COMPLETEDHANGS",selectedCompletedHangs);

            setResult(Activity.RESULT_OK,editWorkout);
            startActivityForResult(editWorkout, REQUEST_HANGS_COMPLETED);
        }
        else if (selectedContextMenuItem == 1) {

            //dbHandler.delete(positionGlobal);
            Toast.makeText(WorkoutStatistics.this, "EDITING DATE ", Toast.LENGTH_SHORT).show();
            // workoutAdapter.notifyDataSetChanged();
        }
        else if (selectedContextMenuItem == 2) {
            //dbHandler.delete(positionGlobal);

/*
            Cursor data = dbHandler.getListContents();
            if (data.move(info.position+1)) {
                int deleteID = data.getInt(0);
                Log.e("deleting id"," ID: " + deleteID);
                dbHandler.delete(deleteID);
            }*/

            dbHandler.delete(info.position+1);

            Toast.makeText(WorkoutStatistics.this, "MAYBE WORKING AT LEAST NOT CRASHING!!!! DELETING ITEM NRO: " + positionGlobal, Toast.LENGTH_SHORT).show();
            workoutAdapter.notifyDataSetChanged();
        }

        return true;
    }


    // Randomizers for creating all the data that is neede for testing SQLite database
    // TimeControls, Holds, Dates
    private ArrayList<Hold> getRandomWorkoutHolds(int number_of_holds) {

        ArrayList<Hold> newHolds = new ArrayList<Hold>();

        Hold newLeftHandHold;
        Hold newRightHandHold;
        for (int i = 0;i < number_of_holds; i++) {
            newLeftHandHold = getNewRandomHold();
            newRightHandHold = getNewRandomHold();
            newRightHandHold.setGripStyle(newLeftHandHold.getGripStyle());

            newHolds.add(newLeftHandHold);      // left hand
            newHolds.add(newRightHandHold); // right hand

        }
        return newHolds;
    }

    public int[] getCompletedRandomly(TimeControls timeControls) {

        int[] rngCompleted = new int[timeControls.getGripLaps()*timeControls.getRoutineLaps()];
        Random rng = new Random();
        for (int i = 0; i < rngCompleted.length; i++) {
            rngCompleted[i] = rng.nextInt(timeControls.getHangLaps()+1);
        }
        return  rngCompleted;
    }

    private Hold getNewRandomHold() {
        Hold newHold = new Hold(rng.nextInt(20)+1);
        newHold.setHoldValue(rng.nextInt(40)+1);
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
