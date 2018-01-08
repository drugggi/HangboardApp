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
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

public class WorkoutActivity extends AppCompatActivity {

    Chronometer totalTimeChrono;
    Chronometer lapseTimeChrono;
    ProgressBar hangProgressBar;
    enum workoutPart {ALKULEPO, WORKOUT, LEPO, PITKALEPO};
    Button pauseBtn;

    String hold_and_grip;
    int grip_laps = 6;
    int hang_laps = 6;
    int routine_laps = 3;
    int time_on = 7;
    int time_off = 3;
    int time_total = time_on + time_off;
    int rest = 150;
    int long_rest = 600;
    workoutPart nowDoing = workoutPart.ALKULEPO;
    // String[] holdsgrips = new String[6];

    int workout_starts_in = 30;
    int s = -30;
    int total_s = -800;
    ArrayList<String> workoutInfo;
    int[] time_controls;
    TextView gradeTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        hangProgressBar = (ProgressBar) findViewById(R.id.hangProgressBar);
        pauseBtn = (Button) findViewById(R.id.pauseBtn);
        pauseBtn.setText("pause");
        gradeTextView = (TextView) findViewById(R.id.gradTextView);

        // If Intent has extra information, lets get it
        if (getIntent().hasExtra("com.example.laakso.hangboardapp.HANGLIST")) {
           workoutInfo = new ArrayList<String>();
           workoutInfo = getIntent().getStringArrayListExtra("com.example.laakso.hangboardapp.HANGLIST");

            /*
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

            // lets put first instructions to the gradeTextView

            gradeTextView.setText(holdsgrips[hang_laps - 1]);
            */

            int i = 0;
            while (i < workoutInfo.size() ) {
                 workoutInfo.set(i, workoutInfo.get(i).replace("\n", " ") );
                 ++i;
            }


            gradeTextView.setText(workoutInfo.get(0));
        }

        // This Intent brings the time controls to the workout program
        if (getIntent().hasExtra("com.example.laakso.hangboardapp.TIMECONTROLS")) {
            time_controls = getIntent().getExtras().getIntArray("com.example.laakso.hangboardapp.TIMECONTROLS");
            // total_s = time_total*time_controls[0] + 15*time_controls[1] + 2*time_controls[2] -s ;


            grip_laps = time_controls[0];
            // SECURITY CHECK, WILL MAKE SURE IN FUTURE TO NEVER HAPPEN
            if (grip_laps > workoutInfo.size() ) { grip_laps = workoutInfo.size(); }

            time_on = time_controls[2];
            time_off = time_controls[3];
            time_total = time_on + time_off;
            hang_laps = time_controls[1] * time_total;
            routine_laps = time_controls[4];
            rest = time_controls[5];
            long_rest = time_controls[6];
            // 6 sets, 6 rounds  of 7on 3 off, 6 laps 150s rests, 600s long rest
            //  TESTAUSTA VARTEN TIME_CONTROLS 0 SÄÄTÖÄ!!
            total_s = workout_starts_in + (hang_laps*grip_laps+(grip_laps - 1)*rest) * routine_laps  + (routine_laps - 1)*long_rest;

           /* Toast.makeText(WorkoutActivity.this, "grip_laps: " + grip_laps + " time_on: " + time_on +
                    " time off: " + time_off + " total_time: " + time_total + " hang_laps: " + hang_laps + " routine_laps: " + routine_laps +
                    " rest: " + rest + " longrest: " + long_rest + " total_time: " + total_s, Toast.LENGTH_LONG).show();*/

        }
       // Toast.makeText(WorkoutActivity.this, "timeconrol0: " + time_controls[0],Toast.LENGTH_LONG).show();
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

                s++;
                total_s--;
                // String ss = "" + Math.abs(s);
                lapseTimeChrono.setText("" + Math.abs(s) );
                totalTimeChrono.setText("Time left: " + total_s);



                switch (nowDoing) {
                    case ALKULEPO:
                        if (s == -1) {nowDoing = workoutPart.WORKOUT;}

                        break;
                    case WORKOUT:
                        if( s == 0 ) {lapseTimeChrono.setText("GO");}

                        // If 59 seconds has passed, it is REST time
                        if ( s == hang_laps-1 ) {
                            nowDoing = workoutPart.LEPO;
                            hangProgressBar.setProgress(0);
                            grip_laps--;

                            if (grip_laps == 0) {nowDoing = workoutPart.PITKALEPO; }
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

                        if (s >= hang_laps) {
                          /*  Toast.makeText(WorkoutActivity.this, "grip_laps: " + grip_laps +
                                     " hang_laps: " + hang_laps + " routine_laps: " + routine_laps +
                                    " rest: " + rest + " s: " + s, Toast.LENGTH_LONG).show();*/
                            hangProgressBar.setProgress(0);
                            lapseTimeChrono.setTextColor(ColorStateList.valueOf(Color.GREEN));
                            s = -rest;
                            // gradeTextView.setText(holdsgrips[grip_laps - 1]);
                            gradeTextView.setText(workoutInfo.get(grip_laps - 1));
                             }


                        if (s == -1) {nowDoing = workoutPart.WORKOUT;}

                        break;
                    case PITKALEPO:
                        if (s >= hang_laps) {
                            /*
                            Toast.makeText(WorkoutActivity.this, "grip_laps: " + grip_laps +
                                    " hang_laps: " + hang_laps + " routine_laps: " + routine_laps +
                                    " rest: " + rest + " s: " + s, Toast.LENGTH_LONG).show();*/
                            grip_laps = time_controls[0];
                            hangProgressBar.setProgress(0);
                            lapseTimeChrono.setTextColor(ColorStateList.valueOf(Color.GREEN));

                            s = -long_rest;

                            //gradeTextView.setText(holdsgrips[grip_laps - 1]);
                            gradeTextView.setText(workoutInfo.get(grip_laps - 1));
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
