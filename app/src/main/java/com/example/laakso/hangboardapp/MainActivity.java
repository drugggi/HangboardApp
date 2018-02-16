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
        leftFingerImage = (ImageView) findViewById(R.id.leftFingerImageView);
        leftFingerImage.setImageResource(R.drawable.fourfingerleft);
        rightFingerImage = (ImageView) findViewById(R.id.rightFingerImageView);
         rightFingerImage.setImageResource(R.drawable.fourfingerright);

        fingerImage = (ImageView) findViewById(R.id.templateFingerImageView);
         fingerImage.setImageResource(R.drawable.finger_template);
        // fingerImage.setVisibility(View.INVISIBLE);


        // HangBoard class holds all the information about grades and holds and grips
        final Resources res = getResources();
        everyBoard = new HangBoard(res);
        everyBoard.InitializeHolds(res);
       everyBoard.setGripAmount(6,0);

        // 6 sets, 6 rounds  of 7on 3 off, 6 laps 150s rests, 600s long rest

        // time_controls = new int[] {6, 6, 7 ,3 , 3, 150, 600};
       //time_controls = new int[] {6, 6, 3 ,7 , 3, 150, 600};
        timeControls = new TimeControls();
        timeControls.setTimeControls(new int[] {6, 6, 7 ,3 , 3, 150, 360});

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
        // TextView testi = (TextView) findViewById(R.id.textView);

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

                rightFingerImage.setVisibility(View.INVISIBLE);
                leftFingerImage.setVisibility(View.INVISIBLE);

                everyBoard.NewBoard(res,CustomSwipeAdapter.getHangBoard(position));
                everyBoard.InitializeHolds(res);
                holdsAdapter = new  ArrayAdapter<String>(MainActivity.this ,
                        R.layout.mytextview , everyBoard.setGrips(grade_descr_position));

                holdsListView.setAdapter(holdsAdapter);
                // holdsAdapter.clear();
                // holdsAdapter.addAll(everyBoard.getGrips());
                durationSeekBar.setProgress(3);

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

                float x;
                if (position % 2 != 0) {
                x = fingerImage.getX() + position * 3; }
                else { x = fingerImage.getX() - position * 3; }
                fingerImage.setX(x+5);

                // Toast.makeText(MainActivity.this,"X: "+ fingerImage.getX()+ " Y: " + fingerImage.getY(),Toast.LENGTH_SHORT ).show();

            }
        });

        // Everytime a hold is pressed on the holdsList, update to randomize only that hold.
        holdsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                hang_descr_position = position+1;
                randomizeBtn.setText("Randomize Hold: " + (hang_descr_position) );

                rightFingerImage.setVisibility(View.VISIBLE);
                leftFingerImage.setVisibility(View.VISIBLE);

                ImageView imageView = (ImageView) findViewById(R.id.image_view);
//                ImageView risti = (ImageView) findViewById(R.id.ristiImageView);

//                Drawable drawable = imageView.getDrawable();
  //              Rect imageBounds = drawable.getBounds();

                Float multiplyer_w = imageView.getWidth() / 350F;
                Float multiplyer_h = imageView.getHeight() / 150F;

                // Toast.makeText(MainActivity.this,"risti Height: " + imageView.getHeight() + " risti Width: " + imageView.getWidth(),Toast.LENGTH_LONG ).show();

                //fingerImage.setX(everyBoard.getCoordLeftX(position) * multiplyer_w);
                //fingerImage.setY(everyBoard.getCoordLeftY(position) * multiplyer_h);

//                Toast.makeText(MainActivity.this,"multi Height: " + multiplyer_h + "multi Width: " + multiplyer_w,Toast.LENGTH_LONG ).show();
/*
                // imageBounds.height();

                // Toast.makeText(MainActivity.this,"iBounds Height: " + imageBounds.height() + "iBounds Width: " + imageBounds.width(),Toast.LENGTH_LONG ).show();
                Toast.makeText(MainActivity.this,"drawable Height: " + drawable.getIntrinsicHeight() + " drawable Width: " + drawable.getIntrinsicWidth(),Toast.LENGTH_LONG ).show();
                */
                leftFingerImage.setImageResource(everyBoard.getLeftFingerImage(position));
                leftFingerImage.setX(everyBoard.getCoordLeftX(position)* multiplyer_w);
                leftFingerImage.setY(everyBoard.getCoordLeftY(position)* multiplyer_h);



                rightFingerImage.setImageResource(everyBoard.getRightFingerImage(position));
                rightFingerImage.setX(everyBoard.getCoordRightX(position)*multiplyer_w);
                rightFingerImage.setY(everyBoard.getCoordRightY(position)*multiplyer_h);

                // Toast.makeText(MainActivity.this,multiplyer_w + " rX: "+ everyBoard.getCoordRightX(position)+" "+ multiplyer_h+ " rY: " + everyBoard.getCoordRightY(position),Toast.LENGTH_SHORT ).show();
  /*
                if (position == 0) { leftFingerImage.setX(20); leftFingerImage.setY(45); rightFingerImage.setX(840); rightFingerImage.setY(45); }
                if (position == 1) { leftFingerImage.setX(210); leftFingerImage.setY(70); rightFingerImage.setX(660); rightFingerImage.setY(70); }
                if (position == 2) { leftFingerImage.setX(360); leftFingerImage.setY(45); rightFingerImage.setX(510); rightFingerImage.setY(45); }
                if (position == 3) { leftFingerImage.setX(20); leftFingerImage.setY(141); rightFingerImage.setX(840); rightFingerImage.setY(141); }
                if (position == 4) { leftFingerImage.setX(10); leftFingerImage.setY(220); rightFingerImage.setX(850); rightFingerImage.setY(220); }
                if (position == 5) { leftFingerImage.setX(100); leftFingerImage.setY(307); rightFingerImage.setX(760); rightFingerImage.setY(307); }
*/
                float y;
                if (position % 2 != 0) {
                y = fingerImage.getY() + position*3; }
                else {y = fingerImage.getY() - position*3; }
                fingerImage.setY(y+5);


               //  Toast.makeText(MainActivity.this,"X: "+ fingerImage.getX()+ " Y: " + fingerImage.getY(),Toast.LENGTH_SHORT ).show();

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
                workoutIntent.putExtra("com.example.laakso.hangboardapp.COORDINATES", everyBoard.getCoordinates());

                //ArrayList<HoldValue> currentHangList = new ArrayList<HoldValue>();
                ArrayList<HoldValue> currentHoldList = everyBoard.getCurrentHoldList();
                workoutIntent.putParcelableArrayListExtra("com.example.laakso.hangboardapp.HOLDVALUES", currentHoldList);
/*
                HoldValue testiValue = new HoldValue(1);
                testiValue.setHoldValue(15);
                testiValue.setHoldCoordinates(res.getIntArray(R.array.bm1000_coordinates) );
                testiValue.setGripTypeAndSingleHang(61);

                 workoutIntent.putExtra("com.example.laakso.hangboardapp.HOLDVALUE",testiValue);*/
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


                    // Toast.makeText(MainActivity.this, "Repeaters."+ timeControls.getTotalTime(), Toast.LENGTH_SHORT).show();
                } else {
                    timeControls.changeTimeToSingleHangs();
                    everyBoard.setGripAmount(timeControls.getGripLaps(),grade_descr_position);
                    holdsAdapter = new  ArrayAdapter<String>(MainActivity.this ,
                            R.layout.mytextview , everyBoard.getGrips());

                    // durationSeekBar.setProgress(3);

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
                Toast.makeText(MainActivity.this, timeControls.getTimeControlsAsString(),Toast.LENGTH_LONG).show();
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
