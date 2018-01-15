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
import android.widget.Toast;

import java.util.ArrayList;

public class WorkoutActivity extends AppCompatActivity {

    Chronometer totalTimeChrono;
    Chronometer lapseTimeChrono;
    ProgressBar hangProgressBar;
    // ImageView kuva;
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
    TextView gradeTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        final MediaPlayer playSound = MediaPlayer.create(this,R.raw.tick);
        final MediaPlayer playFinishSound = MediaPlayer.create(this,R.raw.finish_tick);

        // kuva = (ImageView) findViewById(R.id.imageView);
        // kuva.setImageResource(R.drawable.lauta1011);

        hangProgressBar = (ProgressBar) findViewById(R.id.hangProgressBar);
        pauseBtn = (Button) findViewById(R.id.pauseBtn);
        pauseBtn.setText("pause");
        gradeTextView = (TextView) findViewById(R.id.gradTextView);

        // If Intent has extra information, lets get it HANGLIST should contain the hang and crip information
        if (getIntent().hasExtra("com.example.laakso.hangboardapp.HANGLIST")) {
           workoutInfo = new ArrayList<String>();
           workoutInfo = getIntent().getStringArrayListExtra("com.example.laakso.hangboardapp.HANGLIST");

            int i = 0;
            while (i < workoutInfo.size() ) {
                 workoutInfo.set(i, workoutInfo.get(i).replace("\n", " ") );
                 ++i;
            }

            i = 0;
            while (i < workoutInfo.size() ) {
                Toast.makeText(WorkoutActivity.this,workoutInfo.get(i),Toast.LENGTH_LONG).show();
                i++;
            }

            gradeTextView.setText(workoutInfo.get(0));
        }

        // This Intent brings the time controls to the workout program
        if (getIntent().hasExtra("com.example.laakso.hangboardapp.TIMECONTROLS")) {
            int[] time_controls = getIntent().getExtras().getIntArray("com.example.laakso.hangboardapp.TIMECONTROLS");
            // total_s = time_total*time_controls[0] + 15*time_controls[1] + 2*time_controls[2] -s ;

            timeControls = new TimeControls();
            timeControls.setTimeControls(time_controls);

            // grip_laps = time_controls[0];
            // SECURITY CHECK, WILL MAKE SURE IN FUTURE TO NEVER HAPPEN
            if (timeControls.getGripLaps() > workoutInfo.size() ) { timeControls.setGripLaps(workoutInfo.size() ); }
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
        pauseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Check if chronometer is active lets stop it and store pause time
                if ( pauseBtn.getText().equals("pause") ) {
                    lapseTimeChrono.stop();
                    pauseBtn.setText("start");

                }
                // Chrono meter has been stopped, lets set the basetime when it was stopped
                else {
                    pauseBtn.setText("pause");
                    lapseTimeChrono.start();
                }

            }
        });



        current_lap = 1;
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

                        // This if statmenet will be called once becouse s is positive and will be negative
                        if (s >= timeControls.getHangLapsSeconds()) {
                            hangProgressBar.setProgress(0);
                            lapseTimeChrono.setTextColor(ColorStateList.valueOf(Color.GREEN));
                            s = -timeControls.getRestTime();
                            gradeTextView.setText(workoutInfo.get(current_lap - 1));
                             }


                        if (s == -1) {nowDoing = workoutPart.WORKOUT;}

                        break;
                    case PITKALEPO:
                        // This if statmenet will be called once becouse s is positive and will be negative
                        if (s >= timeControls.getHangLapsSeconds()) {
                            current_lap = 1;
                            hangProgressBar.setProgress(0);
                            lapseTimeChrono.setTextColor(ColorStateList.valueOf(Color.GREEN));

                            s = -timeControls.getLongRestTime();
                            gradeTextView.setText(workoutInfo.get(current_lap - 1));
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

}
