package com.finn.laakso.hangboardapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class WorkoutActivity extends AppCompatActivity {

    // This hack provides the information which Context Menu was created. The two are
    // progressbar long click listener and lapseTimeChrono long click listener. We have to
    // be able to tell which one was pressed.
    private boolean progressBarContextMenu;

    // Chronometer totalTimeChrono;
    private Chronometer lapseTimeChrono;

    private ProgressBar hangProgressBar;
    private ProgressBar restProgressBar;

    private PinchZoomImageView pinchZoomBoardImage;

    private int boardimageResource;
    private ImageView boardimage;
    private ImageView leftHandImage;
    private ImageView rightHandImage;
    private enum workoutPart {INITIALREST, WORKOUT, REST, LONGREST};

    private Button pauseBtn;
    private Button workoutProgressButton;

    private TimeControls timeControls;
    private int current_lap;
    private int current_set;
    private workoutPart nowDoing = workoutPart.INITIALREST;

    // workout starts in 30 seconds
    private int s;

    // total_s is the total workout_time and will count down to zero
    private int total_s;

    // Change the name, this keeps track on grips used at the time
    private ArrayList<Hold> workoutHolds;
    private TextView gradeTextView;
    private TextView infoTextView;

    private int[] completedHangs;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("workoutactivity_hangboardholds", workoutHolds);
        outState.putIntArray("workoutactivity_timecontrolsintarray", timeControls.getTimeControlsIntArray());
        outState.putIntArray("workoutactivity_completedhangs", completedHangs);
        outState.putInt("workoutactivity_workoutseconds",s);
        outState.putInt("workoutactivity_currentlap", current_lap);
        outState.putInt("workoutactivity_currentset", current_set);
        outState.putInt("workoutactivity_totalworkouttime",total_s);
        outState.putSerializable("workoutactivity_workoutpart",nowDoing);
        outState.putString("workoutactivity_gripinfo", gradeTextView.getText().toString());

    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_workout);

        // Sound files that will be played for every second that user hangs, and at the end finish sound
        final MediaPlayer playSound = MediaPlayer.create(this,R.raw.tick);
        final MediaPlayer playFinishSound = MediaPlayer.create(this,R.raw.finish_tick);
        setVolumeControlStream(AudioManager.STREAM_MUSIC);

        s = -30;
        total_s = 0;

        // Major inconsistency during the program current_lap starts from 0 mainly because it is used to pick
        // correct hold information from workoutHold array.
        current_lap = 0;
        current_set = 1;

        boardimage = (ImageView) findViewById(R.id.boardImageView);
        boardimage.setVisibility(View.INVISIBLE);
        pinchZoomBoardImage = (PinchZoomImageView) findViewById(R.id.pinchZoomImageView);

        hangProgressBar = (ProgressBar) findViewById(R.id.hangProgressBar);
        hangProgressBar.setProgressTintList(ColorStateList.valueOf(Color.RED));
        restProgressBar = (ProgressBar) findViewById(R.id.restProgressBar);
        restProgressBar.setProgressTintList(ColorStateList.valueOf(Color.GREEN));

        pauseBtn = (Button) findViewById(R.id.pauseBtn);
        pauseBtn.setText("pause");
        workoutProgressButton = (Button) findViewById(R.id.workoutProgressButton);

        gradeTextView = (TextView) findViewById(R.id.gradTextView);
        infoTextView = (TextView) findViewById(R.id.infoTextView);

        leftHandImage = (ImageView) findViewById(R.id.leftHandImageView);
        rightHandImage = (ImageView) findViewById(R.id.rightHandImageView);

        lapseTimeChrono = (Chronometer) findViewById(R.id.lapseTimeChrono);
        lapseTimeChrono.setTextColor(ColorStateList.valueOf(Color.GREEN));

        // Holds that will be used in this workout program
        if (getIntent().hasExtra("com.finn.laakso.hangboardapp.HOLDS")) {
            try {
                workoutHolds = getIntent().getExtras().getParcelableArrayList("com.finn.laakso.hangboardapp.HOLDS");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // Hangboard image that user has selected
        if (getIntent().hasExtra("com.finn.laakso.hangboardapp.BOARDIMAGE")) {
            boardimageResource = getIntent().getIntExtra("com.finn.laakso.hangboardapp.BOARDIMAGE", 0);
            boardimage.setImageResource(boardimageResource);
            pinchZoomBoardImage.setImageBitmap(BitmapFactory.decodeResource(getResources(),boardimageResource));
            pinchZoomBoardImage.setVisibility(View.VISIBLE);
        }

        // This Intent brings the time controls to the workout program
        if (getIntent().hasExtra("com.finn.laakso.hangboardapp.TIMECONTROLS")) {
            int[] time_controls = getIntent().getExtras().getIntArray("com.finn.laakso.hangboardapp.TIMECONTROLS");

            timeControls = new TimeControls();
            timeControls.setTimeControls(time_controls);

            // SECURITY CHECK, WILL MAKE SURE IN FUTURE TO NEVER HAPPEN
            if (timeControls.getGripLaps()*2 != workoutHolds.size()) {
                Toast.makeText(WorkoutActivity.this,timeControls.getGripLaps() + " ERROR!! Griplaps and workoutHolds sizes doesn't match " + workoutHolds.size(), Toast.LENGTH_LONG).show();
                timeControls.setGripLaps(workoutHolds.size()/2);
            }
            total_s = -s + timeControls.getTotalTime();
            completedHangs = new int[timeControls.getGripLaps()*timeControls.getRoutineLaps()];

        }

        // If phone is in portrait position Board image can take the whole width of the phone
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT ) {
            pinchZoomBoardImage.setScale(1f);
            pinchZoomBoardImage.setTranslateY(300f);

        }

        // If phone orientation has changed, lets bring the state back from savedInstanceState
        if (savedInstanceState != null) {
            workoutHolds = savedInstanceState.getParcelableArrayList("workoutactivity_hangboardholds");
            timeControls.setTimeControls(savedInstanceState.getIntArray("workoutactivity_timecontrolsintarray"));
            nowDoing = (workoutPart) savedInstanceState.get("workoutactivity_workoutpart");
            s = savedInstanceState.getInt("workoutactivity_workoutseconds");
            total_s = savedInstanceState.getInt("workoutactivity_totalworkouttime");
            current_lap = savedInstanceState.getInt("workoutactivity_currentlap");
            current_set = savedInstanceState.getInt("workoutactivity_currentset");
            gradeTextView.setText(savedInstanceState.getString("workoutactivity_gripinfo"));
            completedHangs = savedInstanceState.getIntArray("workoutactivity_completedhangs");

            lapseTimeChrono.stop();
            pauseBtn.setText("resume");
            Toast.makeText(this,"Paused",Toast.LENGTH_SHORT).show();

            workoutProgressButton.setVisibility(View.VISIBLE);

        }
        else {
            for (int i = 0; i < completedHangs.length ; i++) {
                completedHangs[i] = 0;
            }
            workoutProgressButton.setVisibility(View.INVISIBLE);

            lapseTimeChrono.start();
        }
/*
        hangProgressBar.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(WorkoutActivity.this,"jes",Toast.LENGTH_SHORT).show();
                return false;
            }
        });*/

        hangProgressBar.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                if (v.getId() == R.id.hangProgressBar) {
                    progressBarContextMenu = true;

                    if (current_lap == 0 && current_set == 1) {
                        Toast.makeText(WorkoutActivity.this, "Only the last hang can be edited, no hangs completed yet.", Toast.LENGTH_LONG).show();
                    }
                    else {
                        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
                        if (timeControls.getHangLaps() == 1) {
                            String title = "Did you do hang: (" + (current_lap + 1) + "/" + timeControls.getHangLaps() + ")";
                            menu.setHeaderTitle(title);
                            menu.add(Menu.NONE, 0, 0, "No  (0/1)");
                            menu.add(Menu.NONE, 1, 1, "Yes (1/1)");
                        } else {
                            String title = "Edit hang: (" + (current_lap + 1) + "/" + timeControls.getHangLaps() + ")";
                            menu.setHeaderTitle(title);

                            for (int i = 0; i <= timeControls.getHangLaps(); i++) {
                                menu.add(Menu.NONE, i, i, (i + "/" + timeControls.getHangLaps() + "  was succesful"));
                            }

                        }
                    }
                }
            }
        });

        // Set the text becouse if the phone orientation has changed it would display "00:00" instead
        String timeText = "" + Math.abs(s);
        lapseTimeChrono.setText(timeText );

        // Context Menu Listener so user can change the timer text size
        lapseTimeChrono.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

                if (v.getId()==R.id.lapseTimeChrono) {
                    progressBarContextMenu = false;

                    menu.setHeaderTitle("Select new size");

                    // Options to change the size of lapseTimeChrono
                    menu.add(Menu.NONE,1,1,"GROW: 1.5x");
                    menu.add(Menu.NONE,2,2,"GROW: 1.25x");
                    menu.add(Menu.NONE,3,3,"GROW: 1.1x");
                    menu.add(Menu.NONE,4,4,"SHRINK: 0.9x");
                    menu.add(Menu.NONE,5,5,"SHRINK: 0.75x");
                    menu.add(Menu.NONE,6,6,"SHRINK: 0.5x");

                }

            }
        });

        // Lets stop or start chronometer on user input
        // i = -1;
        pauseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if ( pauseBtn.getText().equals("pause") ) {
                    lapseTimeChrono.stop();
                    pauseBtn.setText("resume");
                    workoutProgressButton.setVisibility(View.VISIBLE);
                }

                else {
                    pauseBtn.setText("pause");
                    lapseTimeChrono.start();
                    workoutProgressButton.setVisibility(View.INVISIBLE);
                }

            }
        });

        workoutProgressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent workoutProgress = new Intent(getApplicationContext(), EditWorkoutInfoActivity.class);

                // Lets pass the necessary information to WorkoutActivity; time controls, hangboard image, and used holds with grip information
                workoutProgress.putExtra("com.finn.laakso.hangboardapp.TIMECONTROLS",timeControls.getTimeControlsIntArray() );

                String hangboardName = HangboardSwipeAdapter.getHangboardName(boardimageResource);
                workoutProgress.putExtra("com.finn.laakso.hangboardapp.BOARDNAME",hangboardName);
                workoutProgress.putExtra("com.finn.laakso.hangboardapp.BOARDIMAGE",boardimageResource);
                workoutProgress.putParcelableArrayListExtra("com.finn.laakso.hangboardapp.HOLDS", workoutHolds);
                workoutProgress.putExtra("com.finn.laakso.hangboardapp.COMPLETEDHANGS",completedHangs);

                // is the workout new or are we editing old workout.
                boolean isNewWorkout = true;
                workoutProgress.putExtra("com.finn.laakso.hangboardapp.NEWWORKOUT", isNewWorkout);

                startActivity(workoutProgress);
                // setResult(Activity.RESULT_OK,editWorkout);
                //startActivityForResult(editWorkout, REQUEST_HANGS_COMPLETED);
            }
        });

        // Progress our program for every tick that lapseTimeChrono produces

        lapseTimeChrono.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                s++;
                total_s--;

                StringBuilder timeTextBuilder = new StringBuilder();
                timeTextBuilder.append(Math.abs(s));
                lapseTimeChrono.setText(timeTextBuilder.toString() );

                timeTextBuilder = new StringBuilder();
                timeTextBuilder.append(total_s/60).append("min left\n").append(current_set)
                        .append(". set (").append(current_lap+1).append("/")
                        .append(timeControls.getGripLaps() ).append(") ");
                infoTextView.setText(timeTextBuilder.toString() );

                updateGripDisplay();

                switch (nowDoing) {
                    case INITIALREST:

                        // At 25s mark lets show the next hang instructions
                        if ( s == -25) {
                            leftHandImage.setImageResource(R.drawable.threebackleft);
                            rightHandImage.setImageResource(R.drawable.twomiddleright);

                            updateGripDisplay();
                        }
                        if (s == -1) {nowDoing = workoutPart.WORKOUT;}

                        break;
                    case WORKOUT:
                        if( s == 0 ) {
                            lapseTimeChrono.setText("GO");
                            restProgressBar.setProgress(0);
                        }

                        // If seconds in a hang lap (59s) has passed, it is REST time
                        if ( s == timeControls.getHangLapsSeconds() ) {
                            nowDoing = workoutPart.REST;
                            updateCompletedHangs();

                             if (timeControls.getTimeOFF() == 0 ) { playFinishSound.start(); }
                            hangProgressBar.setProgress(0);
                            restProgressBar.setProgress(0);
                            current_lap++;
                            s--;

                            if (current_lap == timeControls.getGripLaps()) {
                                nowDoing = workoutPart.LONGREST;
                                //updateCompletedHangs();
                            }
                            break;
                        }

                        //If the first digit is less than seven its hanging time and lets indicate
                        // that putting progressbar and ChronoTimer on color RED
                        if ( s%timeControls.getTimeONandOFF()  < timeControls.getTimeON() ) {
                            restProgressBar.setProgress(0);
                            playSound.start();
                            //hangProgressBar.setProgress(( (s%timeControls.getTimeONandOFF())*100) / timeControls.getTimeONandOFF());
                            hangProgressBar.setProgressTintList(ColorStateList.valueOf(Color.RED));
                           // hangProgressBar.setProgress(( ((s%timeControls.getTimeONandOFF()) %timeControls.getTimeON())*100) / timeControls.getTimeON());
                            hangProgressBar.setProgress( (s%timeControls.getTimeONandOFF()) * 100 / timeControls.getTimeON());
                            lapseTimeChrono.setTextColor(ColorStateList.valueOf(Color.RED));
                        }

                        // If the first digit is 7 it is rest time for three seconds,
                        else {
                            if (s%timeControls.getTimeONandOFF() == timeControls.getTimeON() ) {
                                playFinishSound.start();
                                Hold tempHold = workoutHolds.get(current_lap*2);
                                workoutHolds.set(current_lap*2, workoutHolds.get(current_lap*2+1));
                                workoutHolds.set(current_lap*2+1,tempHold);
                                updateGripDisplay();
                            }
                             hangProgressBar.setProgressTintList(ColorStateList.valueOf(Color.GREEN));
                            hangProgressBar.setProgress(100);

                            restProgressBar.setProgress( ((s%timeControls.getTimeONandOFF())-timeControls.getTimeON())*100 / timeControls.getTimeOFF() );
                            lapseTimeChrono.setTextColor(ColorStateList.valueOf(Color.GREEN));
                        }

                        break;
                    case REST:

                        // This if statement will be called only once because s is positive and will be negative thereafter
                        if (s >= timeControls.getHangLapsSeconds()) {
                            hangProgressBar.setProgress(0);
                            restProgressBar.setProgress(0);
                            lapseTimeChrono.setTextColor(ColorStateList.valueOf(Color.GREEN));
                            s = -timeControls.getRestTime();
                            updateGripDisplay();

                             }
                        restProgressBar.setProgress((s+timeControls.getRestTime())*100 / timeControls.getRestTime() );

                        if (s == -1) {nowDoing = workoutPart.WORKOUT;}

                        break;
                    case LONGREST:
                        // This if statmenet will be called once becouse s is positive and will be negative
                        if (s >= timeControls.getHangLapsSeconds()) {
                            current_set++;
                            current_lap = 0;
                            hangProgressBar.setProgress(0);
                            restProgressBar.setProgress(0);
                            lapseTimeChrono.setTextColor(ColorStateList.valueOf(Color.GREEN));
                            updateGripDisplay();
                            s = -timeControls.getLongRestTime();

                        }
                        restProgressBar.setProgress((s+timeControls.getLongRestTime())*100 / timeControls.getLongRestTime() );

                        if (timeControls.getRoutineLaps() == current_set - 1) {
                            lapseTimeChrono.stop();
                            workoutProgressButton.setVisibility(View.VISIBLE);

                        }

                        if (s == -1) {nowDoing = workoutPart.WORKOUT;
                            // timeControls.setRoutineLaps(timeControls.getRoutineLaps() - 1);
                        }

                        break;

                }



            }
        });

    }

    // Lets change the lapseTimeChrono size base on what user chose.
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int clicked_position = item.getItemId();

        if (progressBarContextMenu) {

            int index = (current_set - 1 )* timeControls.getGripLaps() + current_lap - 1;

            if (index >= 0 && index < completedHangs.length ) {
                Log.e("called","conContextItemSelected called set: " + current_set + " lap: " + current_lap);
                if (clicked_position >= 0 && clicked_position <= timeControls.getHangLaps() ) {
                    completedHangs[index] = clicked_position;
                }
            }
            else {
                Log.e("Out of bounds","conContextItemSelected index out of bounds set: " + current_set + " lap: " + current_lap);
            }
        }
        else {
            DisplayMetrics metrics;
            metrics = getApplicationContext().getResources().getDisplayMetrics();
            float chronoTextSize = lapseTimeChrono.getTextSize() / metrics.density;

            if (clicked_position == 1) {
                lapseTimeChrono.setTextSize(chronoTextSize * 1.5f);
            } else if (clicked_position == 2) {
                lapseTimeChrono.setTextSize(chronoTextSize * 1.25f);
            } else if (clicked_position == 3) {
                lapseTimeChrono.setTextSize(chronoTextSize * 1.1f);
            } else if (clicked_position == 4) {
                lapseTimeChrono.setTextSize(chronoTextSize * 0.9f);
            } else if (clicked_position == 5) {
                lapseTimeChrono.setTextSize(chronoTextSize * 0.75f);
            } else {
                lapseTimeChrono.setTextSize(chronoTextSize * 0.5f);
            }

            // Lets not allow user to make ridiculously small or big text size.
            if (lapseTimeChrono.getTextSize() > 1500f || lapseTimeChrono.getTextSize() < 100f) {
                Toast.makeText(WorkoutActivity.this, "Inappropriate text size", Toast.LENGTH_SHORT).show();
                lapseTimeChrono.setTextSize(155f);
            }
        }
        return true;

    }

    private void updateCompletedHangs() {

        // remember that current_lap starts from 0 and current_set starts from 1
        int index = (current_set - 1 )* timeControls.getGripLaps() + current_lap;

        if (index >= 0 && index < completedHangs.length ) {
            Log.e("called","updateCompletedHangs called set: " + current_set + " lap: " + current_lap);
            completedHangs[index] = timeControls.getHangLaps();
        }
        else {
            Log.e("Out of bounds","updateCompletedHangs index out of bounds set: " + current_set + " lap: " + current_lap);
        }

    }

    // updateGripDisplay updates the board image and finger images for every chrono tick
    private void updateGripDisplay() {

        Float scaleFactor = pinchZoomBoardImage.getScaleFactor();

        // 0.5912f comes from scaling nexus 5 width to 1050, different phones different values
        float scaleto1050 = 1050f/pinchZoomBoardImage.getWidth();

        // offsetX is very bad way to re calculate coordinates in workoutactivity from mainactivity coordinates
        Float offsetX = -60+100*scaleFactor;
        offsetX = offsetX/scaleto1050;

        scaleFactor = scaleFactor  / scaleto1050;
        Float multiplier_w = 3f*scaleFactor;
        Float multiplier_h = 3f*scaleFactor;

        Float imagewidth = pinchZoomBoardImage.getImageWidth();
        Float imageheight = pinchZoomBoardImage.getImageHeight();

        Float offsetY = (0.4286f*imagewidth - imageheight)/2;

        // OffsetY and offsetX are just arbitary values to correct hold coordinates that are stored badly
        // Hopefully in future better way is achieved somehow
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT ) {
            offsetX = offsetX - 20f;

        }

    // MAKE SURE THIS TEST IS USELESS IN THE FUTURE! this method is called when the current lap is not reduced
       // Log.e("CURRENT LAP" , "size and current_lap: "+ workoutHolds.size() + " / " + current_lap);
        if ( current_lap*2 >= workoutHolds.size()-1) {current_lap = 0; }

        leftHandImage.setImageResource(workoutHolds.get(current_lap*2 ).getGripImage(true));
        rightHandImage.setImageResource(workoutHolds.get(current_lap*2 + 1).getGripImage(false));

        // MAYBE TRY TO PUT OFFSETX BEFOR MULTIPLIER LIKE THIS: (workoutHolds.get(current_lap*2 ).getLeftCoordX()+offsetX) * MULTIPLIER_W

        leftHandImage.setX(pinchZoomBoardImage.getImageX() + workoutHolds.get(current_lap*2 ).getLeftCoordX()*multiplier_w+offsetX);
        leftHandImage.setY(pinchZoomBoardImage.getImageY()+ workoutHolds.get(current_lap*2).getLeftCoordY()*multiplier_h-offsetY);
        rightHandImage.setX(pinchZoomBoardImage.getImageX() + workoutHolds.get(current_lap*2 + 1).getRightCoordX()*multiplier_w+offsetX);
        rightHandImage.setY(pinchZoomBoardImage.getImageY() + workoutHolds.get(current_lap*2 + 1).getRightCoordY()*multiplier_h-offsetY);


        // Lets get the correct description to next hold and grip
        String texti =workoutHolds.get(2*current_lap).getHoldInfo(workoutHolds.get(2*current_lap+1));
        texti = texti.replaceAll("\n",", ");
        gradeTextView.setText(texti);

    }


}
