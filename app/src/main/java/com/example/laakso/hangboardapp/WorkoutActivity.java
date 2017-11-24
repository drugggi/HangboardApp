package com.example.laakso.hangboardapp;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Scanner;

public class WorkoutActivity extends AppCompatActivity {

    Chronometer totalTimeChrono;
    Chronometer lapseTimeChrono;
    ProgressBar hangProgressBar;
    enum workoutPart {ALKULEPO, WORKOUT, LEPO, PITKALEPO};
    Button pauseBtn;

    String hold_and_grip;
    int hang_laps = 6;
    int routine_laps = 3;
    int time_on = 7;
    int time_off = 3;
    int time_total = time_on + time_off;
    workoutPart nowDoing = workoutPart.ALKULEPO;
    String[] holdsgrips = new String[6];

    int workout_starts_in = 30;
    int s = -30;
    int total_s = -800;

    int[] time_controls;
    TextView gradeTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout);

        hangProgressBar = (ProgressBar) findViewById(R.id.hangProgressBar);
        pauseBtn = (Button) findViewById(R.id.pauseBtn);
        pauseBtn.setText("pause");
        gradeTextView = (TextView) findViewById(R.id.gradTextView);

        // If Intent has extra information, lets get it
        if (getIntent().hasExtra("com.example.laakso.hangboardapp.HANGLIST")) {
           //  TextView gradeTextView = (TextView) findViewById(R.id.gradTextView);
            hold_and_grip = getIntent().getExtras().getString("com.example.laakso.hangboardapp.HANGLIST");

            Scanner in = new Scanner(hold_and_grip);

            // Lets put hang instruction to String table that will be presented as hangboard program goes on
            hang_laps = 5;
            while (in.hasNextLine() && hang_laps > -1) {
                holdsgrips[hang_laps] = in.nextLine();
                hang_laps--;
            }
            hang_laps = 6;

            // lets put firts instructions to the screen gradeTextView
            gradeTextView.setText(holdsgrips[hang_laps - 1]);
        }

        // This Intent brings the time controls to the workout program
        if (getIntent().hasExtra("com.example.laakso.hangboardapp.TEST")) {
            time_controls = getIntent().getExtras().getIntArray("com.example.laakso.hangboardapp.TEST");
            total_s = time_total*time_controls[0] + 15*time_controls[1] + 2*time_controls[2] -s ;
            Toast.makeText(WorkoutActivity.this, "hangs: " + time_controls[0] + " rest_time: " +
                    time_controls[1] + " long_rest: " + time_controls[2] + "total_time: " + total_s, Toast.LENGTH_LONG).show();

            //  TESTAUSTA VARTEN TIME_CONTROLS 0 SÄÄTÖÄ!!
            time_controls[0] = time_controls[0] * time_total;

        }
        Toast.makeText(WorkoutActivity.this, "timeconrol0: " + time_controls[0],Toast.LENGTH_LONG).show();
        totalTimeChrono = (Chronometer) findViewById(R.id.totalTimeChrono);
        totalTimeChrono.setText(""+ Math.abs(total_s));

  //      totalTimeChrono.setCountDown(Boolean.TRUE); // This crashes the phone MOTO G3gen 6.0 Marshmellow

        // totalTimeChrono.start();

        lapseTimeChrono = (Chronometer) findViewById(R.id.lapseTimeChrono);
        lapseTimeChrono.setBase(SystemClock.elapsedRealtime() + workout_starts_in);
        lapseTimeChrono.setTextColor(ColorStateList.valueOf(Color.GREEN));
        lapseTimeChrono.start();


        // Lets stop or start chronometer on user input
        pauseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Check if chronometer is active lets stop it and store pause time
                if ( pauseBtn.getText().equals("pause") ) {
                    // pause_time = lapseTimeChrono.getBase() - SystemClock.elapsedRealtime();
                    lapseTimeChrono.stop();
                    pauseBtn.setText("start");

                }
                // Chrono meter has been stopped, lets set the basetime when it was stopped
                else {
                    pauseBtn.setText("pause");
                    // lapseTimeChrono.setBase(SystemClock.elapsedRealtime() + pause_time);
                    lapseTimeChrono.start();
                }


            }
        });

        final MediaPlayer playSound = MediaPlayer.create(this,R.raw.tick);
        final MediaPlayer playFinishSound = MediaPlayer.create(this,R.raw.finish_tick);

        // Progress our program for every tick that lapseTimeChrono produces
        lapseTimeChrono.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                // long time = SystemClock.elapsedRealtime() - chronometer.getBase();
                // int s = (int) (time /1000);
                s++;
                total_s--;
                // String ss = "" + Math.abs(s);
                lapseTimeChrono.setText("" + Math.abs(s) );
                totalTimeChrono.setText("Time left: " + total_s);

                // Every lap we change the text that informs user in what hold to hang on 0-6
                //if (hang_laps < 7 && hang_laps > 0) {
                //    gradeTextView.setText(holdsgrips[hang_laps - 1]);
                // }
                // If one full hang round (10 seconds) has passed, lets reset the colors and bar




                switch (nowDoing) {
                    case ALKULEPO:
                        if (s == -1) {nowDoing = workoutPart.WORKOUT;}

                        break;
                    case WORKOUT:
                        if( s == 0 ) {lapseTimeChrono.setText("GO");}

                        // If 59 seconds has passed, it is REST time
                        if ( s == time_controls[0]-1 ) {
                            nowDoing = workoutPart.LEPO;
                            hangProgressBar.setProgress(0);
                            hang_laps--;

                            if (hang_laps == 0) {nowDoing = workoutPart.PITKALEPO; }
                            break;
                        }

                        //If the first digit is less than seven its hanging time and lets indicate
                        // that putting progressbar and ChronoTimer on color RED
                        if ((s%time_total) < time_on) {
                            playSound.start();
                            hangProgressBar.setProgress(( (s%time_total)*100) / time_total);
                            hangProgressBar.setProgressTintList(ColorStateList.valueOf(Color.RED));
                            lapseTimeChrono.setTextColor(ColorStateList.valueOf(Color.RED));
                        }

                        // If the first digit is 7 it is rest time for three seconds,
                        else {
                            if (s%time_total == time_on) {playFinishSound.start(); }
                            hangProgressBar.setProgress(( (s%time_total)*100) / time_total);
                            hangProgressBar.setProgressTintList(ColorStateList.valueOf(Color.GREEN));
                            lapseTimeChrono.setTextColor(ColorStateList.valueOf(Color.GREEN));
                        }

                        break;
                    case LEPO:

                        if (s>20) {
                            hangProgressBar.setProgress(0);
                            lapseTimeChrono.setTextColor(ColorStateList.valueOf(Color.GREEN));
                            s = -time_controls[1];
                            gradeTextView.setText(holdsgrips[hang_laps - 1]);
                        // lapseTimeChrono.stop();
                            // set TimeChrono to start minus 10 seconds, 999 to prevent 10->8 seconds jump
                        // lapseTimeChrono.setBase(SystemClock.elapsedRealtime() + time_controls[1]);
                            // lapseTimeChrono.setText(ss);
                        // lapseTimeChrono.start();
                             }

                        if (s == -1) {nowDoing = workoutPart.WORKOUT;}

                        break;
                    case PITKALEPO:
                        if (s>20) {
                            hang_laps = 6;
                            hangProgressBar.setProgress(0);
                            lapseTimeChrono.setTextColor(ColorStateList.valueOf(Color.GREEN));
                            // lapseTimeChrono.setBase(SystemClock.elapsedRealtime() + time_controls[2]);
                            s = -time_controls[2];
                            gradeTextView.setText(holdsgrips[hang_laps - 1]);
                        }

                        if (routine_laps == 1) {
                            lapseTimeChrono.stop();
                            totalTimeChrono.stop();
                        }

                        if (s == -1) {nowDoing = workoutPart.WORKOUT;
                            routine_laps--;}

                        break;

                }



            }
        });


    }

}
