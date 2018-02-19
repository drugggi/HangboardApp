package com.example.laakso.hangboardapp;

import android.content.pm.ActivityInfo;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class WorkoutActivity extends AppCompatActivity {

    Chronometer totalTimeChrono;
    Chronometer lapseTimeChrono;
    ProgressBar hangProgressBar;
     ImageView boardimage;
     ImageView leftHandImage;
     ImageView rightHandImage;
    enum workoutPart {ALKULEPO, WORKOUT, LEPO, PITKALEPO};
    Button pauseBtn;

    TimeControls timeControls;
    int current_lap;
    // SHOULD PROBABLY CREATE A DATA CLASS FOR THESE WORKOUT TIME CONTROLS
    /* int grip_laps = 6;
    int hang_laps = 6;
    int routine_laps = 3;
    int time_on = 7;
    int time_off = 3;
    int time_total = time_on + time_off;
    int rest = 150;
    int long_rest = 600;*/
    workoutPart nowDoing = workoutPart.ALKULEPO;

    int workout_starts_in = 30;
    int s = -30;
    int total_s = -800;
    ArrayList<String> workoutInfo;
    ArrayList<Hold> workoutInfoTest;
    TextView gradeTextView;

    int i;
    int hold_coordinates[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        final MediaPlayer playSound = MediaPlayer.create(this,R.raw.tick);
        final MediaPlayer playFinishSound = MediaPlayer.create(this,R.raw.finish_tick);

        boardimage = (ImageView) findViewById(R.id.boardImageView);


        hangProgressBar = (ProgressBar) findViewById(R.id.hangProgressBar);
        pauseBtn = (Button) findViewById(R.id.pauseBtn);
        pauseBtn.setText("pause");
        gradeTextView = (TextView) findViewById(R.id.gradTextView);

        if (getIntent().hasExtra("com.example.laakso.hangboardapp.HOLDS")) {
             workoutInfoTest = getIntent().getExtras().getParcelableArrayList("com.example.laakso.hangboardapp.HOLDS");
        }

        if (getIntent().hasExtra("com.example.laakso.hangboardapp.COORDINATES")) {
            hold_coordinates = getIntent().getExtras().getIntArray("com.example.laakso.hangboardapp.COORDINATES");
        }

        if (getIntent().hasExtra("com.example.laakso.hangboardapp.BOARDIMAGE")) {
            int image_resource = getIntent().getIntExtra("com.example.laakso.hangboardapp.BOARDIMAGE", 0);
            boardimage.setImageResource(image_resource);

        }

        // If Intent has extra information, lets get it HANGLIST should contain the hang and grip information
        if (getIntent().hasExtra("com.example.laakso.hangboardapp.HANGLIST")) {
           workoutInfo = new ArrayList<String>();
           workoutInfo = getIntent().getStringArrayListExtra("com.example.laakso.hangboardapp.HANGLIST");

            int i = 0;
            while (i < workoutInfo.size() ) {
                 workoutInfo.set(i, workoutInfo.get(i).replace("\n", " ") );
                 ++i;
            }

            i = 0;
            /*
            while (i < workoutInfo.size() ) {
                Toast.makeText(WorkoutActivity.this,workoutInfo.get(i),Toast.LENGTH_LONG).show();
                i++;
            }*/

            gradeTextView.setText(workoutInfo.get(0));
        }

        // This Intent brings the time controls to the workout program
        if (getIntent().hasExtra("com.example.laakso.hangboardapp.TIMECONTROLS")) {
            int[] time_controls = getIntent().getExtras().getIntArray("com.example.laakso.hangboardapp.TIMECONTROLS");
            // total_s = time_total*time_controls[0] + 15*time_controls[1] + 2*time_controls[2] -s ;

            timeControls = new TimeControls();
            timeControls.setTimeControls(time_controls);
            // timeControls.setTimeControls(new int[] {6, 2, 5 ,3 , 1, 15, 150});

            // Toast.makeText(WorkoutActivity.this,"WI size "+ workoutInfo.size() + " griplaps " + timeControls.getGripLaps(),Toast.LENGTH_LONG).show();
            // grip_laps = time_controls[0];
            // SECURITY CHECK, WILL MAKE SURE IN FUTURE TO NEVER HAPPEN
            //if (timeControls.getGripLaps() > workoutInfo.size() ) { timeControls.setGripLaps(workoutInfo.size() ); }
            if (timeControls.getGripLaps()*2 != workoutInfoTest.size()) {
                Toast.makeText(WorkoutActivity.this,timeControls.getGripLaps() + " ERROR!! Gripslaps and workoutInfoTEst sizes doesn't match " + workoutInfoTest.size(), Toast.LENGTH_LONG).show();
                timeControls.setGripLaps(workoutInfoTest.size()/2);
            }
/*
            time_on = time_controls[2];
            time_off = time_controls[3];
            time_total = time_on + time_off;
            hang_laps = time_controls[1] * time_total;
            routine_laps = time_controls[4];
            rest = time_controls[5];
            long_rest = time_controls[6];*/
            // 6 sets, 6 rounds  of 7on 3 off, 6 laps 150s rests, 600s long rest
            //  TESTAUSTA VARTEN TIME_CONTROLS 0 SÄÄTÖÄ!!
            // total_s = workout_starts_in + (hang_laps*grip_laps+(grip_laps - 1)*rest) * routine_laps  + (routine_laps - 1)*long_rest;

//            grip_laps = timeControls.getGripLaps();
            total_s = workout_starts_in + timeControls.getTotalTime();

        }
       // Toast.makeText(WorkoutActivity.this, "timeconrol0: " + time_controls[0],Toast.LENGTH_LONG).show();
        totalTimeChrono = (Chronometer) findViewById(R.id.totalTimeChrono);
        totalTimeChrono.setText(""+ Math.abs(total_s));

        lapseTimeChrono = (Chronometer) findViewById(R.id.lapseTimeChrono);
        lapseTimeChrono.setBase(SystemClock.elapsedRealtime() + workout_starts_in);
        lapseTimeChrono.setTextColor(ColorStateList.valueOf(Color.GREEN));
        lapseTimeChrono.start();

        // Lets stop or start chronometer on user input
        i = -1;
        pauseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Check if chronometer is active lets stop it and store pause time
                Float multiplyer_w = boardimage.getWidth() / 350F;
                Float multiplyer_h = boardimage.getHeight() / 150F;
                /* if (i < hold_coordinates.length/ 5 ) {
                    leftHandImage.setX((hold_coordinates[i*5+1]+3) * multiplyer_w);
                    leftHandImage.setY(hold_coordinates[i*5+2] * multiplyer_h);
                    rightHandImage.setX((hold_coordinates[i*5+3]+3) * multiplyer_w);
                    rightHandImage.setY(hold_coordinates[i*5+4] * multiplyer_h);
                    i++;
                }
                else {
                    i=0;
                }


                if ( i+1 < workoutInfoTest.size() ) {
                    i++;
                }
                else {
                    i=0;
                }*/
                if ( pauseBtn.getText().equals("pause") ) {
                    lapseTimeChrono.stop();
                    pauseBtn.setText("start");

                    //leftHandImage.setX(leftHandImage.getX()+ 5);
                    //leftHandImage.setY(leftHandImage.getY()+ 2);

                    //leftHandImage.setX(workoutInfoTest.get(i).getLeftCoordX()*multiplyer_w);
                    //leftHandImage.setY(workoutInfoTest.get(i).getLeftCoordY()*multiplyer_h);
                    //gradeTextView.setText("LEFTHANDINFO hldnro"+ workoutInfoTest.get(i).getHoldNumber()+ " difficulty: " +workoutInfoTest.get(i).getHoldValue() + " hand: "
                    //+workoutInfoTest.get(i).getHoldText());
                    // Toast.makeText(WorkoutActivity.this,"LEFT X: "+ workoutInfoTest.get(i).getLeftCoordX() + " Y: " + workoutInfoTest.get(i).getLeftCoordY(),Toast.LENGTH_LONG ).show();

                }
                // Chrono meter has been stopped, lets set the basetime when it was stopped
                else {
                    pauseBtn.setText("pause");
                    lapseTimeChrono.start();

                    //rightHandImage.setX(rightHandImage.getX()+ 5);
                    //rightHandImage.setY(rightHandImage.getY()+ 3);

                    //rightHandImage.setX(workoutInfoTest.get(i).getRightCoordX()*multiplyer_w);
                    //rightHandImage.setY(workoutInfoTest.get(i).getRightCoordY()*multiplyer_h);

//                     gradeTextView.setText("RIGHTHANDINFO hldnro"+ workoutInfoTest.get(i).getHoldNumber()+ " difficulty: " +workoutInfoTest.get(i).getHoldValue()+ " hand: "
  //                           +workoutInfoTest.get(i).getHoldText());
                   // Toast.makeText(WorkoutActivity.this,"length X: "+ workoutInfoTest.get(i).getRightCoordX() + " Y: " + workoutInfoTest.get(i).getRightCoordY(),Toast.LENGTH_LONG ).show();

                }

            }
        });

        current_lap = 0;

        // Progress our program for every tick that lapseTimeChrono produces
        lapseTimeChrono.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                s++;
                total_s--;
                lapseTimeChrono.setText("" + Math.abs(s) );
                totalTimeChrono.setText("Time left: " + total_s);



                switch (nowDoing) {
                    case ALKULEPO:
                        if ( s == -25) {
                            leftHandImage = (ImageView) findViewById(R.id.leftHandImageView);
                            rightHandImage = (ImageView) findViewById(R.id.rightHandImageView);
                            leftHandImage.setImageResource(R.drawable.threebackleft);
                            rightHandImage.setImageResource(R.drawable.twomiddleright);

                            updateGripDisplay();
                        }
                        if (s == -1) {nowDoing = workoutPart.WORKOUT;}

                        break;
                    case WORKOUT:
                        if( s == 0 ) {lapseTimeChrono.setText("GO");}

                        // If seconds in a hang lap (59s) has passed, it is REST time
                        if ( s == timeControls.getHangLapsSeconds() - 1 ) {
                            nowDoing = workoutPart.LEPO;
                            hangProgressBar.setProgress(0);
                            current_lap++;

                            if (current_lap == timeControls.getGripLaps()) {nowDoing = workoutPart.PITKALEPO; }
                            break;
                        }

                        //If the first digit is less than seven its hanging time and lets indicate
                        // that putting progressbar and ChronoTimer on color RED
                        if ( s%timeControls.getTimeONandOFF()  < timeControls.getTimeON() ) {
                            playSound.start();
                            hangProgressBar.setProgress(( (s%timeControls.getTimeONandOFF())*100) / timeControls.getTimeONandOFF());
                            hangProgressBar.setProgressTintList(ColorStateList.valueOf(Color.RED));
                            lapseTimeChrono.setTextColor(ColorStateList.valueOf(Color.RED));
                        }

                        // If the first digit is 7 it is rest time for three seconds,
                        else {
                            if (s%timeControls.getTimeONandOFF() == timeControls.getTimeON() ) {playFinishSound.start(); }
                            hangProgressBar.setProgress(( (s%timeControls.getTimeONandOFF())*100) / timeControls.getTimeONandOFF());
                            hangProgressBar.setProgressTintList(ColorStateList.valueOf(Color.GREEN));
                            lapseTimeChrono.setTextColor(ColorStateList.valueOf(Color.GREEN));
                        }

                        break;
                    case LEPO:

                        // This if statmenet will be called only once because s is positive and will be negative thereafter
                        if (s >= timeControls.getHangLapsSeconds()) {
                            hangProgressBar.setProgress(0);
                            lapseTimeChrono.setTextColor(ColorStateList.valueOf(Color.GREEN));
                            s = -timeControls.getRestTime();
                            // gradeTextView.setText(workoutInfo.get(current_lap));
                            updateGripDisplay();
/*
                            leftHandImage.setImageResource(workoutInfoTest.get(current_lap*2 ).getGripImage(true));
                            rightHandImage.setImageResource(workoutInfoTest.get(current_lap*2 + 1).getGripImage(false));
                            leftHandImage.setX(workoutInfoTest.get(current_lap*2 ).getLeftCoordX()*multiplier_w);
                            leftHandImage.setY(workoutInfoTest.get(current_lap*2).getLeftCoordY()*multiplier_h);
                            rightHandImage.setX(workoutInfoTest.get(current_lap*2 + 1).getRightCoordX()*multiplier_w);
                            rightHandImage.setY(workoutInfoTest.get(current_lap*2 + 1).getRightCoordY()*multiplier_h);
                            gradeTextView.setText("HOLD: "+ workoutInfoTest.get(current_lap*2).getHoldNumber()+ "/" + workoutInfoTest.get(current_lap*2 + 1).getHoldNumber()
                                    + " Grip: "+workoutInfoTest.get(current_lap*2).getHoldText() + " difficulty: " + workoutInfoTest.get(current_lap*2).getHoldValue());*/
                             }


                        if (s == -1) {nowDoing = workoutPart.WORKOUT;}

                        break;
                    case PITKALEPO:
                        // This if statmenet will be called once becouse s is positive and will be negative
                        if (s >= timeControls.getHangLapsSeconds()) {
                            current_lap = 0;
                            hangProgressBar.setProgress(0);
                            lapseTimeChrono.setTextColor(ColorStateList.valueOf(Color.GREEN));

                            s = -timeControls.getLongRestTime();
                            // gradeTextView.setText(workoutInfo.get(current_lap));
                        }

                        if (timeControls.getRoutineLaps() == 1) {
                            lapseTimeChrono.stop();
                            totalTimeChrono.stop();
                        }

                        if (s == -1) {nowDoing = workoutPart.WORKOUT;
                            timeControls.setRoutineLaps(timeControls.getRoutineLaps() - 1);
                        }

                        break;

                }



            }
        });


    }
    private void updateGripDisplay() {

        Float multiplier_w = boardimage.getWidth() / 350F;
        Float multiplier_h = boardimage.getHeight() / 150F;
        // Toast.makeText(WorkoutActivity.this,"length X: "+ multiplier_w+ " Y: " + multiplier_h,Toast.LENGTH_LONG ).show();

        leftHandImage.setImageResource(workoutInfoTest.get(current_lap*2 ).getGripImage(true));
        rightHandImage.setImageResource(workoutInfoTest.get(current_lap*2 + 1).getGripImage(false));

        leftHandImage.setX(workoutInfoTest.get(current_lap*2 ).getLeftCoordX()*multiplier_w + 10);
        leftHandImage.setY(workoutInfoTest.get(current_lap*2).getLeftCoordY()*multiplier_h);
        rightHandImage.setX(workoutInfoTest.get(current_lap*2 + 1).getRightCoordX()*multiplier_w + 10);
        rightHandImage.setY(workoutInfoTest.get(current_lap*2 + 1).getRightCoordY()*multiplier_h);
        if (workoutInfoTest.get(current_lap*2).getHoldNumber() == workoutInfoTest.get(current_lap*2 + 1).getHoldNumber()) {
            gradeTextView.setText("HOLD: " + workoutInfoTest.get(current_lap * 2).getHoldNumber() +
                    " GRIP: " + workoutInfoTest.get(current_lap * 2).getHoldText() + " difficulty: " + workoutInfoTest.get(current_lap * 2).getHoldValue());
        }
        else {
            gradeTextView.setText("HOLD: " + workoutInfoTest.get(current_lap * 2).getHoldNumber() + "/" + workoutInfoTest.get(current_lap * 2 + 1).getHoldNumber()
                    + " Alternate GRIP: " + workoutInfoTest.get(current_lap * 2).getHoldText() + " difficulty: " + workoutInfoTest.get(current_lap * 2).getHoldValue());
        }
    }

}
