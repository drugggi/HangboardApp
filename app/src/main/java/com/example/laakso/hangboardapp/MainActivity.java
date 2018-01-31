package com.example.laakso.hangboardapp;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Button startWorkout;
    Button randomizeBtn;
    Button timeControlBtn;
    CheckBox RepeatersBox;

    ViewPager viewPager;
    CustomSwipeAdapter adapter;

    ListView gradesListView;
    ListView holdsListView;
    int grade_descr_position;
    int hang_descr_position;
    int[] time_controls;

    HangBoard everyBoard;
    TimeControls timeControls;

    ArrayAdapter<String> holdsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // HangBoard class holds all the information about grades and holds and grips
        final Resources res = getResources();
        everyBoard = new HangBoard(res);
        everyBoard.InitializeHolds(res);

        // 6 sets, 6 rounds  of 7on 3 off, 6 laps 150s rests, 600s long rest

        // time_controls = new int[] {6, 6, 7 ,3 , 3, 150, 600};
        time_controls = new int[] {6, 6, 3 ,7 , 3, 150, 600};
        timeControls = new TimeControls();
        timeControls.setTimeControls(new int[] {6, 6, 3 ,7 , 3, 150, 360});

        /*Toast.makeText(MainActivity.this," " + timeControls.getHangLaps() + timeControls.getGripLaps() +
        timeControls.getTimeON() + timeControls.getTimeOFF() + timeControls.getRoutineLaps() + timeControls.getRestTime() +
                timeControls.getLongRestTime(), Toast.LENGTH_LONG).show(); */

        // Lets use ArrayAdapter to list all the grades in to grades ListView
        gradesListView = (ListView) findViewById(R.id.gradeListView);
        ArrayAdapter<String> gradeAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, everyBoard.getGrades());
        gradesListView.setAdapter(gradeAdapter);

        holdsListView = (ListView) findViewById(R.id.holdsListView);
        holdsAdapter = new  ArrayAdapter<String>(this, R.layout.mytextview, everyBoard.setGrips(0));
        holdsListView.setAdapter(holdsAdapter);


        // Lets use CustomSwipeAdapter to show different hangboards in a swipeable fashion
        viewPager = (ViewPager)findViewById(R.id.view_pager);
        adapter = new CustomSwipeAdapter(this);
        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
               // Toast.makeText(MainActivity.this,"Hangboard position: " + position,Toast.LENGTH_SHORT).show();
            }

            // WHEN A NEW BOARD IS SWIPED GET NEW STARTING HOLDS AND GRIPS
            // A LOT OF WORK TO DO STILL RANSOMIZE AND ALL
            @Override
            public void onPageSelected(int position) {
                // Toast.makeText(MainActivity.this,"Hangboard page Selected: " + position,Toast.LENGTH_SHORT).show();
                everyBoard.NewBoard(res,CustomSwipeAdapter.getHangBoard(position));
                everyBoard.InitializeHolds(res);
                holdsAdapter = new  ArrayAdapter<String>(MainActivity.this ,
                        R.layout.mytextview , everyBoard.setGrips(grade_descr_position));

                holdsListView.setAdapter(holdsAdapter);
                // holdsAdapter.clear();
                // holdsAdapter.addAll(everyBoard.getGrips());

                randomizeBtn.setText("Randomize ALL");
                hang_descr_position = 0;

                //Toast.makeText(MainActivity.this," " + timeControls.getHangLaps() +" " +  timeControls.getGripLaps() +
                  //      timeControls.getTimeON() + timeControls.getTimeOFF() + timeControls.getRoutineLaps() + timeControls.getRestTime() +
                    //    timeControls.getLongRestTime(), Toast.LENGTH_LONG).show();

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                // Toast.makeText(MainActivity.this,"ScrollStateChanged ",Toast.LENGTH_SHORT ).show();
            }
        });

        // Every time a grade is selected from the list, it shows the workout (holds and grips) that it has
        gradesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                grade_descr_position = gradesListView.getPositionForView(view);
                holdsAdapter = new  ArrayAdapter<String>(MainActivity.this ,
                        R.layout.mytextview , everyBoard.setGrips(grade_descr_position));

                holdsListView.setAdapter(holdsAdapter);

                randomizeBtn.setText("Randomize ALL");
                hang_descr_position = 0;


            }
        });

        // Everytime a hold is pressed on the holdsList, update to randomize only that hold.
        holdsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                hang_descr_position = position+1;
                randomizeBtn.setText("Randomize Hold: " + (hang_descr_position) );
            }
        });

        // Attempts to launch an activity within our own app
        startWorkout = (Button) findViewById(R.id.startWorkoutBtn);
        startWorkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent workoutIntent = new Intent(getApplicationContext(), WorkoutActivity.class);
                //How to pass information to another activity, workout hangs and time controls
                // HANGLIST contains hang descritpions and TEST timecontrols
                workoutIntent.putStringArrayListExtra("com.example.laakso.hangboardapp.HANGLIST", everyBoard.GetGripList() );
                workoutIntent.putExtra("com.example.laakso.hangboardapp.TIMECONTROLS",timeControls.getTimeControlsIntArray() );
                workoutIntent.putExtra("com.example.laakso.hangboardapp.BOARDIMAGE",adapter.getImageResource(viewPager.getCurrentItem()));
                startActivity(workoutIntent);
            }
        });


        // RandomizeButton listener that randomizes holds and grips inside Grips class method ranomizeGrips
        randomizeBtn = (Button) findViewById(R.id.randomizeBtn);
        randomizeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if ( hang_descr_position == 0 ) {
                    everyBoard.randomizeGrips(grade_descr_position);
                }
                else {
                    everyBoard.randomizeGrip(grade_descr_position,hang_descr_position-1);
                }

                ArrayAdapter<String> holdsAdapter = new  ArrayAdapter<String>(MainActivity.this ,
                        R.layout.mytextview , everyBoard.getGrips());
                holdsListView.setAdapter(holdsAdapter);
                // gripsTextView.setText(everyBoard.getGrip(grade_descr_position));
               // Toast.makeText(MainActivity.this, kaijutus,Toast.LENGTH_LONG).show();

            }
        });

        // timeControlBtn lets the user control the time controls that are running in the workout
        timeControlBtn = (Button) findViewById(R.id.timeControlBtn);
        timeControlBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            Toast.makeText(MainActivity.this, " Make new activity with time controls etc", Toast.LENGTH_SHORT).show();


            }
        });

        RepeatersBox = (CheckBox) findViewById(R.id.repeatersCheckBox);
        RepeatersBox.setChecked(true);
        RepeatersBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    timeControls.changeTimeToRepeaters();
                    everyBoard.setGripAmount(timeControls.getGripLaps(),grade_descr_position);
                    holdsAdapter = new  ArrayAdapter<String>(MainActivity.this ,
                            R.layout.mytextview , everyBoard.getGrips());

                    holdsListView.setAdapter(holdsAdapter);

                    Toast.makeText(MainActivity.this, "Repeaters."+ timeControls.getTotalTime(), Toast.LENGTH_SHORT).show();
                } else {
                    timeControls.changeTimeToSingleHangs();
                    everyBoard.setGripAmount(timeControls.getGripLaps(),grade_descr_position);
                    holdsAdapter = new  ArrayAdapter<String>(MainActivity.this ,
                            R.layout.mytextview , everyBoard.getGrips());

                    holdsListView.setAdapter(holdsAdapter);
                    Toast.makeText(MainActivity.this, "Single hangs", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
