package com.finn.laakso.hangboardapp;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

public class WorkoutStatistics extends AppCompatActivity {

    Button editWorkoutButton;
    Button resetDBButton;
    Button showGraphsButton;
    Button newEntryButton;

    CheckBox sortByDateCheckBox;

    Random rng;

    // Adapter that manages the workout history with the help of SQLite
    WorkoutHistoryAdapter workoutAdapter;
    ListView workoutHistoryListView;

    int positionGlobal = 0;
    MyDBHandler dbHandler;

    private static final int REQUEST_HANGS_COMPLETED = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_statistics);

        // Random needed for generating random workout data
        rng = new Random();

        editWorkoutButton = (Button) findViewById(R.id.editWorkoutButton);
        resetDBButton = (Button) findViewById(R.id.testButton);
        showGraphsButton = (Button) findViewById(R.id.showGraphsButton);
        newEntryButton = (Button) findViewById(R.id.newEntryButton);

        sortByDateCheckBox = (CheckBox) findViewById(R.id.sortByDateCheckBox);

        // DBHandler to store workout from Intent.
        dbHandler = new MyDBHandler(getApplicationContext(),null,null,1);

        // Temporary workout info to generate test workouts
        ArrayList<Hold> tempWorkoutHolds = new ArrayList<>();
        TimeControls tempTimeControls = new TimeControls();
        String tempHangboardName = "";
        int[] tempCompleted = new int[5];


        // Holds that will be used in this workout program
        if (getIntent().hasExtra("com.finn.laakso.hangboardapp.HOLDS")) {
            tempWorkoutHolds = getIntent().getExtras().getParcelableArrayList("com.finn.laakso.hangboardapp.HOLDS");

        }

        // Hangboard image that user has selected
        if (getIntent().hasExtra("com.finn.laakso.hangboardapp.BOARDNAME")) {
            tempHangboardName = getIntent().getStringExtra("com.finn.laakso.hangboardapp.BOARDNAME");
        }

        // This Intent brings the time controls to the workout program
        if (getIntent().hasExtra("com.finn.laakso.hangboardapp.TIMECONTROLS")) {
            int[] time_controls = getIntent().getExtras().getIntArray("com.finn.laakso.hangboardapp.TIMECONTROLS");

            tempTimeControls = new TimeControls();
            tempTimeControls.setTimeControls(time_controls);

            // SECURITY CHECK, WILL MAKE SURE IN FUTURE TO NEVER HAPPEN
            if (tempTimeControls.getGripLaps()*2 != tempWorkoutHolds.size()) {
                tempTimeControls.setGripLaps(tempWorkoutHolds.size()/2);
            }

            tempCompleted = new int[tempTimeControls.getGripLaps() * tempTimeControls.getRoutineLaps()];

            for (int i = 0; i < tempCompleted.length ; i++) {
                tempCompleted[i] = 0;
            }

        }

        if(getIntent().hasExtra("com.finn.laakso.hangboardapp.COMPLETEDHANGS")) {
            tempCompleted = getIntent().getExtras().getIntArray("com.finn.laakso.hangboardapp.COMPLETEDHANGS");
        }

        long time = System.currentTimeMillis();

        // Lets add workout information to database straight from the Intent.
        dbHandler.addHangboardWorkout(time,tempHangboardName, tempTimeControls, tempWorkoutHolds,tempCompleted);

        // Button just for generating random workout datapoins and testing purposes
       newEntryButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               long rngTime = System.currentTimeMillis();
               TimeControls rngControls = getRandomTimeControls();
               dbHandler.addHangboardWorkout(
                       rngTime- (long)1000*60*60*24*rng.nextInt(60),
                       "RNG HANGBOARD",
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

                Toast.makeText(WorkoutStatistics.this, "Sorting test ", Toast.LENGTH_SHORT).show();

                Cursor cursor = dbHandler.getListContents();
                Cursor sortedCursor = dbHandler.getSortedContents();

                if ( cursor.moveToFirst() && sortedCursor.moveToFirst() ) {


                    Long date1 = cursor.getLong(1);
                    Long sortedDate = sortedCursor.getLong(1);

                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");

                    Date resultdate = new Date(date1);
                    Date sortedDate1 = new Date(sortedDate);

                }
                workoutAdapter.notifyDataSetChanged();

            }
        });

        workoutAdapter = new WorkoutHistoryAdapter(this,dbHandler);

        workoutHistoryListView = (ListView) findViewById(R.id.workoutHistoryListView);
        workoutHistoryListView.setAdapter(workoutAdapter);
        registerForContextMenu(workoutHistoryListView);


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


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // User has updated the completed hangs information lets update that information to database too
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


        ArrayList<Hold> holds = dbHandler.lookUpHolds(selectedListViewPosition);
        Long date = dbHandler.lookUpDate(selectedListViewPosition);
        int[] completedHangs = dbHandler.lookUpCompletedHangs(selectedListViewPosition);
        TimeControls timeControls = dbHandler.lookUpTimeControls(selectedListViewPosition);
        String hangboardName = dbHandler.lookUpHangboard(selectedListViewPosition);

        if (selectedContextMenuItem == 0) {
            Intent editWorkout = new Intent(getApplicationContext(), EditWorkoutInfo.class);

            // Lets pass the necessary information to WorkoutActivity; time controls, hangboard image, and used holds with grip information
            editWorkout.putExtra("com.finn.laakso.hangboardapp.TIMECONTROLS",timeControls.getTimeControlsIntArray() );
            editWorkout.putExtra("com.finn.laakso.hangboardapp.BOARDNAME",hangboardName);
            editWorkout.putParcelableArrayListExtra("com.finn.laakso.hangboardapp.HOLDS", holds);
            editWorkout.putExtra("com.finn.laakso.hangboardapp.COMPLETEDHANGS",completedHangs);

            setResult(Activity.RESULT_OK,editWorkout);
            startActivityForResult(editWorkout, REQUEST_HANGS_COMPLETED);
        }
        else if (selectedContextMenuItem == 1) {

            //dbHandler.delete(positionGlobal);
            Toast.makeText(WorkoutStatistics.this, "EDITING DATE ", Toast.LENGTH_SHORT).show();
            // workoutAdapter.notifyDataSetChanged();
        }
        else if (selectedContextMenuItem == 2) {

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
