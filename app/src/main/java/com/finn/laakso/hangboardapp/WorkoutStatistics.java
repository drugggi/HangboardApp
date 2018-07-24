package com.finn.laakso.hangboardapp;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class WorkoutStatistics extends AppCompatActivity {

    Button editWorkoutButton;
    Button resetDBButton;
    Button showGraphsButton;
    Button newEntryButton;

    CheckBox showHiddenWorkoutsCheckBox;

    private DatePickerDialog.OnDateSetListener dateSetListener;

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

        showHiddenWorkoutsCheckBox = (CheckBox) findViewById(R.id.showHiddenCheckBox);

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
                       getCompletedALL(rngControls));

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

        workoutAdapter = new WorkoutHistoryAdapter(this,dbHandler,false);

        workoutHistoryListView = (ListView) findViewById(R.id.workoutHistoryListView);
        workoutHistoryListView.setAdapter(workoutAdapter);
        registerForContextMenu(workoutHistoryListView);


        workoutHistoryListView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

                if (v.getId() == R.id.workoutHistoryListView) {
                    //Toast.makeText(EditWorkoutInfo.this, "Context Menu Created ", Toast.LENGTH_SHORT).show();

                    AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;

                    // Show different context menu if all workout are shown (hidden too)
                    if (showHiddenWorkoutsCheckBox.isChecked() ) {

                        menu.setHeaderTitle("Choose your edit");
                        menu.add(Menu.NONE, 0, 0, "edit workout");
                        menu.add(Menu.NONE, 1, 1, "hide/unhide workout");
                        menu.add(Menu.NONE, 2,2,"edit date");
                        menu.add(Menu.NONE, 3, 3, "delete workout");
                    }
                    // Context menu when hidden workout are not shown
                    else {           menu.setHeaderTitle("Choose your edit");
                        menu.add(Menu.NONE, 0, 0, "edit workout");
                        menu.add(Menu.NONE, 1, 1, "hide workout");
                        menu.add(Menu.NONE, 2,2,"edit date");
                              // Can't delete unhidden workouts
                        // menu.add(Menu.NONE, 3, 3, "delete workout");
                    }

                }

            }
        });

        showHiddenWorkoutsCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Toast.makeText(WorkoutStatistics.this,"is checked true, show hidden workouts",Toast.LENGTH_SHORT).show();

                    workoutAdapter = new WorkoutHistoryAdapter(WorkoutStatistics.this,dbHandler,isChecked);

                    workoutHistoryListView = (ListView) findViewById(R.id.workoutHistoryListView);
                    workoutHistoryListView.setAdapter(workoutAdapter);
                    //registerForContextMenu(workoutHistoryListView);

                }
                else {
                    Toast.makeText(WorkoutStatistics.this,"is checked false, hide hidden workouts",Toast.LENGTH_SHORT).show();

                    workoutAdapter = new WorkoutHistoryAdapter(WorkoutStatistics.this,dbHandler,isChecked);

                    workoutHistoryListView = (ListView) findViewById(R.id.workoutHistoryListView);
                    workoutHistoryListView.setAdapter(workoutAdapter);
                   // registerForContextMenu(workoutHistoryListView);

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

        dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                // month = month + 1;

                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.YEAR, year);
                cal.set(Calendar.MONTH, month);
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                Long timeInMillis = cal.getTimeInMillis();

                boolean includeHidden = showHiddenWorkoutsCheckBox.isChecked();

                dbHandler.updateDate(positionGlobal,timeInMillis,includeHidden);

                workoutAdapter.notifyDataSetChanged();

                Log.e("millis time"," " + timeInMillis);
               // Toast.makeText(WorkoutStatistics.this, "date picker set listenr",Toast.LENGTH_LONG).show();
            }
        };


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // User has updated the completed hangs information lets update that information to database too
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_HANGS_COMPLETED) {
            Toast.makeText(WorkoutStatistics.this," results ok",Toast.LENGTH_SHORT).show();

            if (getIntent().hasExtra("com.finn.laakso.hangboardapp.COMPLETEDHANGS")) {

                int[] completed = data.getIntArrayExtra("com.finn.laakso.hangboardapp.COMPLETEDHANGS");

                boolean includeHidden = showHiddenWorkoutsCheckBox.isChecked();
                dbHandler.updateCompletedHangs(positionGlobal,completed,includeHidden);

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

        boolean includeHidden = showHiddenWorkoutsCheckBox.isChecked();

        ArrayList<Hold> holds = dbHandler.lookUpHolds(selectedListViewPosition,includeHidden);
       // Long date = dbHandler.lookUpDate(selectedListViewPosition);
        int[] completedHangs = dbHandler.lookUpCompletedHangs(selectedListViewPosition, includeHidden);
        TimeControls timeControls = dbHandler.lookUpTimeControls(selectedListViewPosition, includeHidden);
        String hangboardName = dbHandler.lookUpHangboard(selectedListViewPosition, includeHidden);

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

            dbHandler.hideWorkoutNumber(selectedListViewPosition,includeHidden);

            workoutAdapter.notifyDataSetChanged();

        }
        else if (selectedContextMenuItem == 2) {

            Toast.makeText(WorkoutStatistics.this, "EDITING DATE" + positionGlobal, Toast.LENGTH_SHORT).show();

            long timeInMillis = dbHandler.lookUpDate(selectedListViewPosition,includeHidden);

            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(timeInMillis);
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH);
            int day = cal.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog dialog = new DatePickerDialog(
                    WorkoutStatistics.this,
                    android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                    dateSetListener,
                    year,month,day);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();


        }



        else if (selectedContextMenuItem == 3) {

            if (dbHandler.lookUpIsHidden(selectedListViewPosition)) {
                dbHandler.delete(selectedListViewPosition);

                Toast.makeText(WorkoutStatistics.this, "DELETING: " + positionGlobal, Toast.LENGTH_SHORT).show();
                workoutAdapter.notifyDataSetChanged();
            }
            else {
                Toast.makeText(WorkoutStatistics.this, "To delete a Workout you must hide it first", Toast.LENGTH_SHORT).show();
            }

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

    public int[] getCompletedALL(TimeControls timeControls) {

        int[] allCompleted = new int[timeControls.getGripLaps()*timeControls.getRoutineLaps()];
        for (int i = 0; i < allCompleted.length; i++) {
            allCompleted[i] = timeControls.getHangLaps();
        }
        return  allCompleted;
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

        int hang_laps = rng.nextInt(6)+1;
        int grip_laps = rng.nextInt(6)+2;
        if (hang_laps == 1) { grip_laps=rng.nextInt(15)+10; }

        int routine_laps=rng.nextInt(4)+1;
        int time_on = 7;
        int time_off= 3;

        if (hang_laps == 1) {time_on = 10; time_off = 0; }
        // private int time_total = time_on + time_off;
         int rest = rng.nextInt(130)+20;
        int long_rest = rng.nextInt(60)*(rng.nextInt(5)+1 );
        int[] i = {grip_laps,hang_laps,time_on,time_off,routine_laps,rest,long_rest};

         // int[] i = {rng.nextInt(30)+1,rng.nextInt(10)+1,rng.nextInt(9)+1,rng.nextInt(5),rng.nextInt(4)+1,rng.nextInt(200)+1,rng.nextInt(500)+1};

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
