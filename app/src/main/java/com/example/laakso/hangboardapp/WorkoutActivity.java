package com.example.laakso.hangboardapp;

import android.content.pm.ActivityInfo;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
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
    int current_set;
    workoutPart nowDoing = workoutPart.ALKULEPO;

    // int workout_starts_in = 30;
    // s and total_s are the shown seconds on screen
    // if s < 0 it is rest time
    int s = -30;
    // total_s is the total workout_time and will count down to zero
    int total_s = 0;
    ArrayList<Hold> workoutInfoTest;
    TextView gradeTextView;

    int i;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        // Sound files that will be played for every second that user hangs, and at the end finish sound
        final MediaPlayer playSound = MediaPlayer.create(this,R.raw.tick);
        final MediaPlayer playFinishSound = MediaPlayer.create(this,R.raw.finish_tick);
        setVolumeControlStream(AudioManager.STREAM_MUSIC);

        boardimage = (ImageView) findViewById(R.id.boardImageView);
        hangProgressBar = (ProgressBar) findViewById(R.id.hangProgressBar);
        pauseBtn = (Button) findViewById(R.id.pauseBtn);
        pauseBtn.setText("pause");
        gradeTextView = (TextView) findViewById(R.id.gradTextView);

        leftHandImage = (ImageView) findViewById(R.id.leftHandImageView);
        rightHandImage = (ImageView) findViewById(R.id.rightHandImageView);

        // Holds that will be used in this workout program
        if (getIntent().hasExtra("com.example.laakso.hangboardapp.HOLDS")) {
             workoutInfoTest = getIntent().getExtras().getParcelableArrayList("com.example.laakso.hangboardapp.HOLDS");
        }

        // Hangboard image that user has selected
        if (getIntent().hasExtra("com.example.laakso.hangboardapp.BOARDIMAGE")) {
            int image_resource = getIntent().getIntExtra("com.example.laakso.hangboardapp.BOARDIMAGE", 0);
            boardimage.setImageResource(image_resource);

        }

        // This Intent brings the time controls to the workout program
        if (getIntent().hasExtra("com.example.laakso.hangboardapp.TIMECONTROLS")) {
            int[] time_controls = getIntent().getExtras().getIntArray("com.example.laakso.hangboardapp.TIMECONTROLS");

            timeControls = new TimeControls();
            timeControls.setTimeControls(time_controls);
            //s= -5;
            // timeControls.setTimeControls(new int[] {6, 1, 10 ,0 , 1, 15, 150});  // IF YOU WANT TO CONTROL TIMECONTROLS FOR TESTIT PURPOSES

            // SECURITY CHECK, WILL MAKE SURE IN FUTURE TO NEVER HAPPEN
            if (timeControls.getGripLaps()*2 != workoutInfoTest.size()) {
                Toast.makeText(WorkoutActivity.this,timeControls.getGripLaps() + " ERROR!! Gripslaps and workoutInfoTEst sizes doesn't match " + workoutInfoTest.size(), Toast.LENGTH_LONG).show();
                timeControls.setGripLaps(workoutInfoTest.size()/2);
            }

            total_s = -s + timeControls.getTotalTime();

        }

        totalTimeChrono = (Chronometer) findViewById(R.id.totalTimeChrono);

        lapseTimeChrono = (Chronometer) findViewById(R.id.lapseTimeChrono);
        lapseTimeChrono.setTextColor(ColorStateList.valueOf(Color.GREEN));
        lapseTimeChrono.start();

        // Lets stop or start chronometer on user input
        i = -1;
        pauseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if ( pauseBtn.getText().equals("pause") ) {
                    lapseTimeChrono.stop();
                    pauseBtn.setText("start");
                }

                else {
                    pauseBtn.setText("pause");
                    lapseTimeChrono.start();
                }

            }
        });

        current_lap = 0;
        current_set = 1;

        // Progress our program for every tick that lapseTimeChrono produces
        lapseTimeChrono.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                s++;
                total_s--;
                lapseTimeChrono.setText("" + Math.abs(s) );
                totalTimeChrono.setText( total_s + "s left\n"+ current_set + ". set ("+ (current_lap+1) + "/" + timeControls.getGripLaps()+") ");

                switch (nowDoing) {
                    case ALKULEPO:

                        // At 25s mark lets show the next hang instructions
                        if ( s == -25) {
                            leftHandImage.setImageResource(R.drawable.threebackleft);
                            rightHandImage.setImageResource(R.drawable.twomiddleright);

                            updateGripDisplay();
                        }
                        if (s == -1) {nowDoing = workoutPart.WORKOUT;}

                        break;
                    case WORKOUT:
                        if( s == 0 ) {lapseTimeChrono.setText("GO");}

                        // If seconds in a hang lap (59s) has passed, it is REST time
                        if ( s == timeControls.getHangLapsSeconds() ) {
                            nowDoing = workoutPart.LEPO;
                             if (timeControls.getTimeOFF() == 0 ) { playFinishSound.start(); }
                            hangProgressBar.setProgress(0);
                            current_lap++;
                            s--;

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
                            // Toast.makeText(WorkoutActivity.this,s + "onandoff: " +timeControls.getTimeONandOFF()+ " timeon: "+ timeControls.getTimeON(),Toast.LENGTH_LONG).show();
                            if (s%timeControls.getTimeONandOFF() == timeControls.getTimeON() ) {
                                playFinishSound.start();
                                Hold tempHold = workoutInfoTest.get(current_lap*2);
                                workoutInfoTest.set(current_lap*2, workoutInfoTest.get(current_lap*2+1));
                                workoutInfoTest.set(current_lap*2+1,tempHold);
                                updateGripDisplay();
                            }
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

                             }


                        if (s == -1) {nowDoing = workoutPart.WORKOUT;}

                        break;
                    case PITKALEPO:
                        // This if statmenet will be called once becouse s is positive and will be negative
                        if (s >= timeControls.getHangLapsSeconds()) {
                            current_set++;
                            current_lap = 0;
                            hangProgressBar.setProgress(0);
                            lapseTimeChrono.setTextColor(ColorStateList.valueOf(Color.GREEN));
                            updateGripDisplay();
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

        // Lets get the coordinates for the next hand images
        leftHandImage.setX(workoutInfoTest.get(current_lap*2 ).getLeftCoordX()*multiplier_w + 10);
        leftHandImage.setY(workoutInfoTest.get(current_lap*2).getLeftCoordY()*multiplier_h);
        rightHandImage.setX(workoutInfoTest.get(current_lap*2 + 1).getRightCoordX()*multiplier_w + 10);
        rightHandImage.setY(workoutInfoTest.get(current_lap*2 + 1).getRightCoordY()*multiplier_h);

        // Lets get the correct descrption to next hold and grip
        String texti =workoutInfoTest.get(2*current_lap).getHoldInfo(workoutInfoTest.get(2*current_lap+1));
        texti = texti.replaceAll("\n",", ");
        gradeTextView.setText(texti);

    }

}
