package com.finn.laakso.hangboardapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class WorkoutHistory extends AppCompatActivity {

    Button editWorkoutButton;
    Button resetDBButton;
    Button showGraphsButton;
    Button newEntryButton;

    EditText hangboardNameEditText;

    CheckBox showHiddenWorkoutsCheckBox;

    private DatePickerDialog.OnDateSetListener dateSetListener;

    Random rng;
    int DELETEALLCOUNTER;

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
        DELETEALLCOUNTER = 3;

        editWorkoutButton = (Button) findViewById(R.id.editWorkoutButton);
        resetDBButton = (Button) findViewById(R.id.testButton);
        showGraphsButton = (Button) findViewById(R.id.showGraphsButton);
        newEntryButton = (Button) findViewById(R.id.newEntryButton);

        showHiddenWorkoutsCheckBox = (CheckBox) findViewById(R.id.showHiddenCheckBox);
        showHiddenWorkoutsCheckBox.setChecked(false);

        // DBHandler to store workout from Intent.
        dbHandler = new MyDBHandler(getApplicationContext(),null,null,1);
        // dbHandler.DELETEALL();
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

        String workoutDescription = "Temp desc";
        if (getIntent().hasExtra("com.finn.laakso.hangboardapp.DESCRIPTION")) {
            workoutDescription = getIntent().getExtras().getString("com.finn.laakso.hangboardapp.DESCRIPTION");
            Toast.makeText(this, "olihan siellä", Toast.LENGTH_SHORT).show();

            long time = System.currentTimeMillis();

            // Lets add workout information to database straight from the Intent.
            dbHandler.addHangboardWorkout(time, tempHangboardName, tempTimeControls, tempWorkoutHolds, tempCompleted, workoutDescription);
        }
        // Button just for generating random workout datapoins and testing purposes
       newEntryButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               long rngTime = System.currentTimeMillis();
               TimeControls rngControls = getRandomTimeControls();
               dbHandler.addHangboardWorkout(
                       rngTime- (long)1000*60*60*24*rng.nextInt(60),
                       getRandomHangboard(),
                       rngControls,
                       getRandomWorkoutHolds(rngControls.getGripLaps()),
                       getCompletedALL(rngControls),
                       getRandomWorkoutDescription());

                workoutAdapter.notifyDataSetChanged();
           }
       });

        // Click listener for editing single workout
        editWorkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(WorkoutHistory.this, "Sorting test ", Toast.LENGTH_SHORT).show();

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

        boolean includeHidden = showHiddenWorkoutsCheckBox.isChecked();
        workoutAdapter = new WorkoutHistoryAdapter(this,dbHandler,includeHidden);

        workoutHistoryListView = (ListView) findViewById(R.id.workoutHistoryListView);
        workoutHistoryListView.setAdapter(workoutAdapter);
        registerForContextMenu(workoutHistoryListView);


        workoutHistoryListView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

                if (showHiddenWorkoutsCheckBox.isChecked() != workoutAdapter.getShowHiddenStatus())  {
                    Log.e("ERR"," error: boolean value showhidden differs");
                    int h = 1;
                }
                else {
                    Log.e("OK","showHidden value same");
                }


                if (v.getId() == R.id.workoutHistoryListView) {
                    //Toast.makeText(EditWorkoutInfo.this, "Context Menu Created ", Toast.LENGTH_SHORT).show();

                    AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;

                    // Show different context menu if all workout are shown (hidden too)
                    if (showHiddenWorkoutsCheckBox.isChecked() ) {

                        menu.setHeaderTitle("Choose your edit");
                        menu.add(Menu.NONE, 0, 0, "edit workout");
                        menu.add(Menu.NONE, 1, 1, "hide/unhide workout");
                        menu.add(Menu.NONE, 2,2,"edit date");
                        menu.add(Menu.NONE,3,3,"edit hangboard name");
                        menu.add(Menu.NONE, 4, 4, "delete workout");
                    }
                    // Context menu when hidden workout are not shown
                    else {           menu.setHeaderTitle("Choose your edit");
                        menu.add(Menu.NONE, 0, 0, "edit workout");
                        menu.add(Menu.NONE, 1, 1, "hide workout");
                        menu.add(Menu.NONE, 2,2,"edit date");
                        menu.add(Menu.NONE,3,3,"edit hangboard name");
                              // Can't delete unhidden workouts
                        // menu.add(Menu.NONE, 4, 4, "delete workout");
                    }

                }

            }
        });

        showHiddenWorkoutsCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Toast.makeText(WorkoutHistory.this,"is checked true, show hidden workouts",Toast.LENGTH_SHORT).show();

                    workoutAdapter = new WorkoutHistoryAdapter(WorkoutHistory.this,dbHandler,isChecked);

                    workoutHistoryListView = (ListView) findViewById(R.id.workoutHistoryListView);
                    workoutHistoryListView.setAdapter(workoutAdapter);
                    //registerForContextMenu(workoutHistoryListView);

                }
                else {
                    Toast.makeText(WorkoutHistory.this,"is checked false, hide hidden workouts",Toast.LENGTH_SHORT).show();

                    workoutAdapter = new WorkoutHistoryAdapter(WorkoutHistory.this,dbHandler,isChecked);

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
                    Toast.makeText(WorkoutHistory.this,"Database empty, cannot show graphs",Toast.LENGTH_SHORT).show();
                }
                else {
                    Intent showDatabaseGraphs = new Intent(getApplicationContext(),WorkoutStatistics.class);
                    startActivity(showDatabaseGraphs);
                }
            }
        });

        // Reset button for clearing all database entries, for testing purposes right now
        resetDBButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // MyDBHandler dbHandler = new MyDBHandler(getApplicationContext(),null,null,1);
                // dbHandler.DELETEALL();

                if (DELETEALLCOUNTER == 0) {
                    dbHandler.DELETEALL();
                    Toast.makeText(WorkoutHistory.this, "All DELETED, Happy now", Toast.LENGTH_LONG).show();
                    DELETEALLCOUNTER = 3;
                }
                else   {
                    DELETEALLCOUNTER--;
                    Toast.makeText(WorkoutHistory.this, "SOON ALL WILL BE GONE, countdown: " + DELETEALLCOUNTER, Toast.LENGTH_LONG).show();
                }

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
               // Toast.makeText(WorkoutHistory.this, "date picker set listenr",Toast.LENGTH_LONG).show();
            }
        };


        Log.e("WO statistics","is checked: " + showHiddenWorkoutsCheckBox.isChecked());

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // User has updated the completed hangs information lets update that information to database too
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_HANGS_COMPLETED) {
            Toast.makeText(WorkoutHistory.this," results ok",Toast.LENGTH_SHORT).show();

            boolean includeHidden = showHiddenWorkoutsCheckBox.isChecked();

            if (data.hasExtra("com.finn.laakso.hangboardapp.COMPLETEDHANGS")) {

                int[] completed = data.getIntArrayExtra("com.finn.laakso.hangboardapp.COMPLETEDHANGS");
                dbHandler.updateCompletedHangs(positionGlobal,completed,includeHidden);
            }

            if (data.hasExtra("com.finn.laakso.hangboardapp.DESCRIPTION")) {

                String desc = data.getStringExtra("com.finn.laakso.hangboardapp.DESCRIPTION");
                dbHandler.updateWorkoutDescription(positionGlobal,desc,includeHidden);

            }
            workoutAdapter.notifyDataSetChanged();
        }


        else {
            Toast.makeText(WorkoutHistory.this," results not saved",Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        // return super.onContextItemSelected(item);
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();

        int selectedListViewPosition = info.position+1;
        positionGlobal = info.position + 1;
        int selectedContextMenuItem = item.getItemId();

        final boolean includeHidden = showHiddenWorkoutsCheckBox.isChecked();
        Log.e("WO statistics 2","is checked: " + showHiddenWorkoutsCheckBox.isChecked());

        ArrayList<Hold> holds = dbHandler.lookUpHolds(selectedListViewPosition,includeHidden);
        //Long date = dbHandler.lookUpDate(selectedListViewPosition, includeHidden);
        int[] completedHangs = dbHandler.lookUpCompletedHangs(selectedListViewPosition, includeHidden);
        TimeControls timeControls = dbHandler.lookUpTimeControls(selectedListViewPosition, includeHidden);
        String hangboardName = dbHandler.lookUpHangboard(selectedListViewPosition, includeHidden);
        String desc = dbHandler.lookUpWorkoutDescription(selectedListViewPosition, includeHidden);

        if (selectedContextMenuItem == 0) {
            Intent editWorkout = new Intent(getApplicationContext(), EditWorkoutInfo.class);

            // Lets pass the necessary information to WorkoutActivity; time controls, hangboard image, and used holds with grip information
            editWorkout.putExtra("com.finn.laakso.hangboardapp.TIMECONTROLS",timeControls.getTimeControlsIntArray() );
            editWorkout.putExtra("com.finn.laakso.hangboardapp.BOARDNAME",hangboardName);
            editWorkout.putParcelableArrayListExtra("com.finn.laakso.hangboardapp.HOLDS", holds);
            editWorkout.putExtra("com.finn.laakso.hangboardapp.COMPLETEDHANGS",completedHangs);
            editWorkout.putExtra("com.finn.laakso.hangboardapp.DESCRIPTION",desc);

            setResult(Activity.RESULT_OK,editWorkout);
            startActivityForResult(editWorkout, REQUEST_HANGS_COMPLETED);
        }
        else if (selectedContextMenuItem == 1) {

            dbHandler.hideWorkoutNumber(selectedListViewPosition,includeHidden);

            workoutAdapter.notifyDataSetChanged();

        }
        else if (selectedContextMenuItem == 2) {

            Toast.makeText(WorkoutHistory.this, "EDITING DATE" + positionGlobal, Toast.LENGTH_SHORT).show();

            long timeInMillis = dbHandler.lookUpDate(selectedListViewPosition,includeHidden);

            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(timeInMillis);
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH);
            int day = cal.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog dialog = new DatePickerDialog(
                    WorkoutHistory.this,
                    android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                    dateSetListener,
                    year,month,day);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();


        }

        else if (selectedContextMenuItem == 3) {
            Toast.makeText(WorkoutHistory.this, "editing hangboard name " + positionGlobal, Toast.LENGTH_SHORT).show();

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Hangboard name");
            builder.setIcon(R.drawable.ic_launcher_background);
            builder.setMessage("edit hangboard name");

            hangboardNameEditText = new EditText(this);
            hangboardNameEditText.setText(hangboardName);
            builder.setView(hangboardNameEditText);

            //Set positive button
            builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String newBoardName = hangboardNameEditText.getText().toString();
                    if (newBoardName.length() > 25) {
                        newBoardName = newBoardName.substring(0,25);

                        Toast.makeText(WorkoutHistory.this,"Please use under 25 characters to describe hangboard name.",Toast.LENGTH_LONG).show();
                    }
                    boolean includeHidden = showHiddenWorkoutsCheckBox.isChecked();
                    dbHandler.updateHangboardName(positionGlobal,newBoardName,includeHidden);

                    workoutAdapter.notifyDataSetChanged();

                }
            });

            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            AlertDialog ad = builder.create();
            ad.show();

        }

        else if (selectedContextMenuItem == 4) {

            if (dbHandler.lookUpIsHidden(selectedListViewPosition)) {
                dbHandler.delete(selectedListViewPosition);

                Toast.makeText(WorkoutHistory.this, "DELETING: " + positionGlobal, Toast.LENGTH_SHORT).show();
                workoutAdapter.notifyDataSetChanged();
            }
            else {
                Toast.makeText(WorkoutHistory.this, "To delete a Workout you must hide it first", Toast.LENGTH_SHORT).show();
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

        if (showHiddenWorkoutsCheckBox.isChecked() != workoutAdapter.getShowHiddenStatus())  {
            Log.e("ERR"," error: boolean value showhidden differs");
            int h = 1;
        }
        else {
            Log.e("OK","showHidden value same");
        }

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

    private String getRandomHangboard() {
        String rngBoard="rng failed board";
        int rngSeed = rng.nextInt(15);
        CustomSwipeAdapter.hangboard rngHB = CustomSwipeAdapter.getHangBoard(rngSeed);

        rngBoard = "RNG " + CustomSwipeAdapter.getHangboardName(rngHB);

        return rngBoard;
    }

    private String getRandomWorkoutDescription() {
        String rngString= "This paragraph describes my frustration towards my inability to articulate " +
                "what is so wrong about workouts and their timing in mornings and evenings, even though " +
                "I do not hate parallel or even or even odd values that is to say this rambling and " +
                "mumbo jumbo no. 5 will make this even tho yes no very good bad yes yes very good I am " +
                "alpha king kong gorilla finger strength is stronger than triceps in lizards limbs " +
                "mental gift is higher than gods will to fly towards should stability and tendon " +
                "soreness Much needed break timer laps are coming or sets and reps four fingers though" +
                " three front often and three back yes why not middle finger one armer and sandwich THEEND";


        StringBuilder rngDesc= new StringBuilder("rng: ");
        String[] rngStringList = rngString.split(" ");
        for (int i = rng.nextInt(15)+1 ; i >=0 ; i--) {
            //Log.e("kierros",": " + i);

            int rngIndex = rng.nextInt(rngStringList.length);

            rngDesc.append(rngStringList[rngIndex]);
            rngDesc.append(" ");

            // int start = rng.nextInt(rngString.length()-10);
            // int end = start + rng.nextInt(10);

//            rngDesc.append(rngString.substring(start,end));
        }

        return rngDesc.toString();
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