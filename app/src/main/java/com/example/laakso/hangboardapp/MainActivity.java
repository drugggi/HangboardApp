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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView gradesListView;
    ListView holdsListView;
    ArrayAdapter<String> holdsAdapter;
    int grade_descr_position;
    int hang_descr_position;

    Button startWorkout;
    Button randomizeBtn;
    Button timeControlBtn;

    CheckBox RepeatersBox;
    TextView durationTextView;
    SeekBar durationSeekBar;

    ImageView leftFingerImage;
    ImageView rightFingerImage;
    ImageView fingerImage;

    ViewPager viewPager;
    CustomSwipeAdapter adapter;

    HangBoard everyBoard;
    TimeControls timeControls;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // HandImages that show the type of grip used in a hold, usually what fingers are used
        leftFingerImage = (ImageView) findViewById(R.id.leftFingerImageView);
        rightFingerImage = (ImageView) findViewById(R.id.rightFingerImageView);

        fingerImage = (ImageView) findViewById(R.id.templateFingerImageView);
        fingerImage.setImageResource(R.drawable.finger_template);
        // fingerImage.setVisibility(View.INVISIBLE); // TESTING PURPOSES


        // HangBoard class holds all the information about grades and holds and grips
        final Resources res = getResources();
        everyBoard = new HangBoard(res);
        everyBoard.InitializeHolds(res);
        everyBoard.setGripAmount(6,0);

        //Default hangboard program (65min)
        timeControls = new TimeControls();
        timeControls.setTimeControls(new int[] {6, 6, 7 ,3 , 3, 150, 360});

        // Lets use ArrayAdapter to list all the grades in to grades ListView
        gradesListView = (ListView) findViewById(R.id.gradeListView);
        ArrayAdapter<String> gradeAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, everyBoard.getGrades());
        gradesListView.setAdapter(gradeAdapter);

        // HOLDSADAPTER IS ALLREADY USING R.LAYOUT.MYTESTVIEW. CONSIDER MAKING IT MUCH MORE SWEETER FOR EXAMPLE HOLD AND GRIPS LEFT AND DIFFICULTY RIGHT
        holdsListView = (ListView) findViewById(R.id.holdsListView);
        holdsAdapter = new  ArrayAdapter<String>(this, R.layout.mytextview, everyBoard.setGrips(0));
        holdsListView.setAdapter(holdsAdapter);

        // Lets use CustomSwipeAdapter to show different hangboards in a swipeable fashion
        viewPager = (ViewPager)findViewById(R.id.view_pager);
        adapter = new CustomSwipeAdapter(this);
        viewPager.setAdapter(adapter);

        // ViewPager for showing and swiping different HangBoards.
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                rightFingerImage.setVisibility(View.INVISIBLE);
                leftFingerImage.setVisibility(View.INVISIBLE);

                // Lets change the HangBoard for every swipe
                everyBoard.NewBoard(res,CustomSwipeAdapter.getHangBoard(position));
                // Every HangBoard has different unique holds
                everyBoard.InitializeHolds(res);
                holdsAdapter = new  ArrayAdapter<String>(MainActivity.this ,
                        R.layout.mytextview , everyBoard.setGrips(grade_descr_position));
                holdsListView.setAdapter(holdsAdapter);
                durationSeekBar.setProgress(3);

                randomizeBtn.setText("Randomize ALL");
                hang_descr_position = 0;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        // Every time a grade is selected from the list, it shows the workout (holds and grips)
        // that it has except in Test progression program
        gradesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (durationSeekBar.getProgress() == 4) {
                    Toast.makeText(MainActivity.this, "There is no grades in \"TEST progression\" program", Toast.LENGTH_LONG).show();
                    return;
                }
                rightFingerImage.setVisibility(View.INVISIBLE);
                leftFingerImage.setVisibility(View.INVISIBLE);

                grade_descr_position = gradesListView.getPositionForView(view);

                holdsAdapter = new  ArrayAdapter<String>(MainActivity.this ,
                        R.layout.mytextview , everyBoard.setGrips(grade_descr_position));
                holdsListView.setAdapter(holdsAdapter);

                randomizeBtn.setText("Randomize ALL");
                hang_descr_position = 0;

                // THIS IS ONLY FOR TESTING HAND IMAGES POSITION PURPOSES
                float x;
                if (position % 2 != 0) {
                x = fingerImage.getX() + position * 3; }
                else { x = fingerImage.getX() - position * 3; }
                fingerImage.setX(x+5);

            }
        });

        // Every time a hold is pressed on the holdsList, update to randomize button only
        // to randomize that hold. And everyBoard to show that Hold's picture
        holdsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                hang_descr_position = position+1;
                randomizeBtn.setText("Randomize Hold: " + (hang_descr_position) );

                rightFingerImage.setVisibility(View.VISIBLE);
                leftFingerImage.setVisibility(View.VISIBLE);

                ImageView imageView = (ImageView) findViewById(R.id.image_view);
                // Hopefully this multiplier works in every android device
                Float multiplyer_w = imageView.getWidth() / 350F;
                Float multiplyer_h = imageView.getHeight() / 150F;

                leftFingerImage.setImageResource(everyBoard.getLeftFingerImage(position));
                leftFingerImage.setX(everyBoard.getCoordLeftX(position)* multiplyer_w);
                leftFingerImage.setY(everyBoard.getCoordLeftY(position)* multiplyer_h);

                rightFingerImage.setImageResource(everyBoard.getRightFingerImage(position));
                rightFingerImage.setX(everyBoard.getCoordRightX(position)*multiplyer_w);
                rightFingerImage.setY(everyBoard.getCoordRightY(position)*multiplyer_h);


                // THIS IS ONLY FOR TESTING HAND IMAGES POSITION PURPOSES
                float y;
                if (position % 2 != 0) {
                y = fingerImage.getY() + position*3; }
                else {y = fingerImage.getY() - position*3; }
                fingerImage.setY(y+5);

            }
        });


        // Attempts to launch an activity within our own app
        startWorkout = (Button) findViewById(R.id.startWorkoutBtn);
        startWorkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent workoutIntent = new Intent(getApplicationContext(), WorkoutActivity.class);

                // DELETE THESE TWO
                workoutIntent.putStringArrayListExtra("com.example.laakso.hangboardapp.HANGLIST", everyBoard.GetGripList() );
                workoutIntent.putExtra("com.example.laakso.hangboardapp.COORDINATES", everyBoard.getCoordinates());

                // Lets pass the necessary information to WorkoutActivity; time controls, hangboard image, and used holds with grip information
                workoutIntent.putExtra("com.example.laakso.hangboardapp.TIMECONTROLS",timeControls.getTimeControlsIntArray() );
                workoutIntent.putExtra("com.example.laakso.hangboardapp.BOARDIMAGE",adapter.getImageResource(viewPager.getCurrentItem()));
                ArrayList<Hold> currentHoldList = everyBoard.getCurrentHoldList();
                workoutIntent.putParcelableArrayListExtra("com.example.laakso.hangboardapp.HOLDS", currentHoldList);

                startActivity(workoutIntent);
            }
        });


        // RandomizeButton listener that randomizes hold or holds that user wants
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


            }
        });

        // THIS WILL IN FUTURE LAUNCH SETTINGS ACTIVITY THAT HAS ALL THE SETTINGS NEEDED
        // timeControlBtn lets the user control the time controls that are running in the workout
        timeControlBtn = (Button) findViewById(R.id.timeControlBtn);
        timeControlBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              // fingerImage.
                // Toast.makeText(MainActivity.this,"X: "+ fingerImage.getWidth()+ " Y: " + fingerImage.getHeight(),Toast.LENGTH_LONG ).show();
                Toast.makeText(MainActivity.this,"X/1.5: "+ fingerImage.getX()/1.5+ " Y/1.5: " + fingerImage.getY()/1.5 ,Toast.LENGTH_LONG ).show();
            // Toast.makeText(MainActivity.this, " Make new activity with time controls etc", Toast.LENGTH_SHORT).show();


            }
        });

        durationTextView = (TextView) findViewById(R.id.durationTextView);
        durationSeekBar = (SeekBar) findViewById(R.id.durationSeekBar);

        RepeatersBox = (CheckBox) findViewById(R.id.repeatersCheckBox);
        RepeatersBox.setChecked(true);

        // There are two main types of hang programs called repeaters or single hangs
        RepeatersBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    timeControls.changeTimeToRepeaters();
                    everyBoard.setGripAmount(timeControls.getGripLaps(),grade_descr_position);
                    holdsAdapter = new  ArrayAdapter<String>(MainActivity.this ,
                            R.layout.mytextview , everyBoard.getGrips());
                    holdsListView.setAdapter(holdsAdapter);

                    durationSeekBar.setProgress(3);

                } else {
                    timeControls.changeTimeToSingleHangs();
                    everyBoard.setGripAmount(timeControls.getGripLaps(),grade_descr_position);
                    holdsAdapter = new  ArrayAdapter<String>(MainActivity.this ,
                            R.layout.mytextview , everyBoard.getGrips());


                    holdsListView.setAdapter(holdsAdapter);
                    // Toast.makeText(MainActivity.this, "Single hangs", Toast.LENGTH_SHORT).show();
                }
                Toast.makeText(MainActivity.this, timeControls.getTimeControlsAsString(),Toast.LENGTH_LONG).show();
            }
        });


        durationSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress == 4) {
                    durationTextView.setText("TEST progression");

                    timeControls.setProgramBasedOnTime(20 + progress * 15);



                    everyBoard.sortHoldByDifficulty();
                    timeControls.setGripLaps(everyBoard.getCurrentHoldListSize()/2 );
                    // everyBoard.setGripAmount(timeControls.getGripLaps(),grade_descr_position);

                }
                else {
                    //NOT WORKING WITH 50MIN WORKOUT
                    durationTextView.setText("Duration: " + (20 + progress * 15) + "min");
                    timeControls.setProgramBasedOnTime(20 + progress * 15);
                    everyBoard.setGripAmount(timeControls.getGripLaps(),grade_descr_position);

                }


                holdsAdapter = new  ArrayAdapter<String>(MainActivity.this ,
                        R.layout.mytextview , everyBoard.getGrips());
                holdsListView.setAdapter(holdsAdapter);
               // Toast.makeText(MainActivity.this, timeControls.getTimeControlsAsString(),Toast.LENGTH_LONG).show();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {


            }
        });


    }
}
