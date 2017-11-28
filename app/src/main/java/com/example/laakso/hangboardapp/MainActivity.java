package com.example.laakso.hangboardapp;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Button startWorkout;
    Button randomizeBtn;
    Button timeControlBtn;


    TextView gripsTextView;
    ListView gradesListView;
    int grade_descr_position;
    int[] time_controls;

    Grips everyGrade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Grips class holds all the information about grades and holds and grips
        Resources res = getResources();
        everyGrade = new Grips(res);
        everyGrade.InitializeHolds(res);

        // 6 sets, 6 rounds  of 7on 3 off, 6 laps 150s rests, 600s long rest
        time_controls = new int[] {6, 6, 7 ,3 , 3, 150, 600};


        // Lets use ArrayAdapter to list all the grades in to grades ListView
        gradesListView = (ListView) findViewById(R.id.gradeListView);
        ArrayAdapter<String> gradeAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, everyGrade.getGrades());
        gradesListView.setAdapter(gradeAdapter);

        gripsTextView = (TextView) findViewById(R.id.gripsTextView);

        // Every time a grade is selected from the list, it show the workout (holds and grips) that it has
        gradesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    gripsTextView.setText(everyGrade.getGrip(position));
                    grade_descr_position = gradesListView.getPositionForView(view);
                   // Toast.makeText(MainActivity.this, ""+ gradesListView.getPositionForView(view), Toast.LENGTH_SHORT).show();

            }
        });

        // Attempts to launch an activity within our own app
        startWorkout = (Button) findViewById(R.id.startWorkoutBtn);
        startWorkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent workoutIntent = new Intent(getApplicationContext(), WorkoutActivity.class);
                //How to pass information to another activity, workout hangs and time controls
                workoutIntent.putExtra("com.example.laakso.hangboardapp.HANGLIST", everyGrade.getGrip(grade_descr_position));
                workoutIntent.putExtra("com.example.laakso.hangboardapp.TEST",time_controls);
                startActivity(workoutIntent);

            }
        });


        // Randomize Button listener that randomizes holds and grip inside Grips class method ranomizeGrips
        randomizeBtn = (Button) findViewById(R.id.randomizeBtn);
        randomizeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String kaijutus = everyGrade.randomizeGrips(grade_descr_position);
                gripsTextView.setText(everyGrade.getGrip(grade_descr_position));
                Toast.makeText(MainActivity.this, kaijutus,Toast.LENGTH_LONG).show();

            }
        });

        // timeControlBtn lets the user control the time controls that are running in the workout
        timeControlBtn = (Button) findViewById(R.id.timeControlBtn);
        timeControlBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Pops the controls over the Hangboard image
                LinearLayout timeControlsLayout = (LinearLayout) findViewById(R.id.restTimeControlLayout);
                ImageView hangboard = (ImageView) findViewById(R.id.hangBoardImageView);

                // Lets try to set new time parameters, if user has typed proper integers
                if (timeControlBtn.getText().equals("Set Time Controls") ) {

                    try {
                        EditText timeControlEditText = (EditText) findViewById(R.id.gripAmounEditText);
                        time_controls[0] = Integer.parseInt(timeControlEditText.getText().toString());
                        timeControlEditText = (EditText) findViewById(R.id.hangAmountEditText);
                        time_controls[1] = Integer.parseInt(timeControlEditText.getText().toString());
                        timeControlEditText = (EditText) findViewById(R.id.timeONeditText);
                        time_controls[2] = Integer.parseInt(timeControlEditText.getText().toString());
                        timeControlEditText = (EditText) findViewById(R.id.timeOFFeditText);
                        time_controls[3] = Integer.parseInt(timeControlEditText.getText().toString());

                        timeControlEditText = (EditText) findViewById(R.id.lapsAmountEditText);
                        time_controls[4] =  Integer.parseInt(timeControlEditText.getText().toString());
                        timeControlEditText = (EditText) findViewById(R.id.restTimeEditText);
                        time_controls[5] = Integer.parseInt(timeControlEditText.getText().toString());
                        timeControlEditText = (EditText) findViewById(R.id.longRestEditText);
                        time_controls[6] = Integer.parseInt(timeControlEditText.getText().toString());

                        hangboard.setVisibility(View.VISIBLE);
                        timeControlsLayout.setVisibility(View.INVISIBLE);
                        timeControlsLayout = (LinearLayout) findViewById(R.id.hangTimeControlLayout);
                        timeControlsLayout.setVisibility(View.INVISIBLE);
                        timeControlBtn.setText("Time Controls");
                    }
                    catch(NumberFormatException e)  {
                        Toast.makeText(MainActivity.this,"Fill all the inputs please",Toast.LENGTH_LONG).show();
                        EditText timeControlEditText = (EditText) findViewById(R.id.gripAmounEditText);
                        timeControlEditText.setText("6");
                        timeControlEditText = (EditText) findViewById(R.id.hangAmountEditText);
                        timeControlEditText.setText("6");
                        timeControlEditText = (EditText) findViewById(R.id.timeONeditText);
                        timeControlEditText.setText("7");
                        timeControlEditText = (EditText) findViewById(R.id.timeOFFeditText);
                        timeControlEditText.setText("3");
                        timeControlEditText = (EditText) findViewById(R.id.lapsAmountEditText);
                        timeControlEditText.setText("3");
                        timeControlEditText = (EditText) findViewById(R.id.restTimeEditText);
                        timeControlEditText.setText("150");
                        timeControlEditText = (EditText) findViewById(R.id.longRestEditText);
                        timeControlEditText.setText("600");
                    }


                }

                // Time controls has been set, so lets bring back hangboard image again.
                else {
                    timeControlsLayout.setVisibility(View.VISIBLE);
                    timeControlsLayout = (LinearLayout) findViewById(R.id.hangTimeControlLayout);
                    timeControlsLayout.setVisibility(View.VISIBLE);
                    hangboard.setVisibility(View.INVISIBLE);
                    timeControlBtn.setText("Set Time Controls");
                }


            }
        });

    }
}
