package com.finn.laakso.hangboardapp;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
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

    private ListView gradesListView;
    private ListView holdsListView;
    // private ArrayAdapter<String> gradeAdapter;
    private HangListAdapter hangsAdapter;
    private GradeListAdapter gradeAdapter;

    // Maybe get rid of these in the future
    private int grade_descr_position;
    // private int hang_descr_position;
    private int hangboard_descr_position;

    private Button startWorkoutButton;
    private Button newWorkoutButton;
    private Button settingsButton;

    private CheckBox repeatersBox;
    private TextView durationTextView;
    private SeekBar durationSeekBar;

    private ImageView leftFingerImage;
    private ImageView rightFingerImage;
    // private ImageView fingerTESTImage;

    private ViewPager viewPager;
    private HangboardSwipeAdapter swipeAdapter;

    private Hangboard everyBoard;
    private TimeControls timeControls;

    android.support.v7.app.ActionBar actionBar;

    private static final long ANIMATION_DURATION = 500;
    private static final int REQUEST_TIME_CONTROLS = 0;
    private static final int REQUEST_COPY_WORKOUT = 1;
    private static final int REQUEST_COPY_BENCHMARK = 2;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("mainactivity_grade_desc_pos",grade_descr_position);
        outState.putParcelableArrayList("mainactivity_hangboardholds", everyBoard.getCurrentWorkoutHoldList());
        outState.putInt("mainactivity_durationseekbarprogression", durationSeekBar.getProgress());
        outState.putInt("mainactivity_durationseekbarvisibility", durationSeekBar.getVisibility());
        outState.putInt("mainactivity_repeatersboxvisibility",repeatersBox.getVisibility());
        outState.putIntArray("mainactivity_timecontrolsintarray", timeControls.getTimeControlsIntArray());
        outState.putInt("mainactivity_hangboardposition",hangboard_descr_position);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId() ) {
            /*case R.id.action_settings:
                Toast.makeText(this,"about and grip types sort",Toast.LENGTH_SHORT).show();

                everyBoard.clearWorkoutHoldList();
              // everyBoard.addEveryGripTypeCompinationToWorkoutList(Hold.grip_type.FOUR_FINGER);

                everyBoard.addEveryGripTypeCompinationToWorkoutList(Hold.grip_type.THREE_FRONT);
                everyBoard.addEveryGripTypeCompinationToWorkoutList(Hold.grip_type.THREE_BACK);
               // everyBoard.addEveryGripTypeCompinationToWorkoutList(Hold.grip_type.TWO_FRONT);
               // everyBoard.addEveryGripTypeCompinationToWorkoutList(Hold.grip_type.TWO_MIDDLE);
               // everyBoard.addEveryGripTypeCompinationToWorkoutList(Hold.grip_type.TWO_BACK);


                //everyBoard.addEveryGripTypeCompinationToWorkoutList(Hold.grip_type.INDEX_FINGER);
               // everyBoard.addEveryGripTypeCompinationToWorkoutList(Hold.grip_type.MIDDLE_FINGER);
                everyBoard.setDifficultyLimits(1,25);

                everyBoard.sortWorkoutHoldList();
                hangsAdapter.notifyDataSetChanged();
                break;*/
                // This is for testing WorkoutHistoryActivity, with these we can simulate completed workouts
                // by simply marking current workout as done without going through start workout
/*                Intent workoutHistoryTestIntent = new Intent(getApplicationContext(), WorkoutHistoryActivity.class);

                // Lets pass the necessary information to WorkoutActivity; time controls, hangboard image, and used holds with grip information
                workoutHistoryTestIntent.putExtra("com.finn.laakso.hangboardapp.TIMECONTROLS",timeControls.getTimeControlsIntArray() );
                 workoutHistoryTestIntent.putExtra("com.finn.laakso.hangboardapp.BOARDNAME",everyBoard.getHangboardName() );
               // workoutHistoryTestIntent.putExtra("com.finn.laakso.hangboardapp.BOARDIMAGE",HangboardResources.getHangboardImageResource(viewPager.getCurrentItem()));
                workoutHistoryTestIntent.putParcelableArrayListExtra("com.finn.laakso.hangboardapp.HOLDS", everyBoard.getCurrentWorkoutHoldList());
                workoutHistoryTestIntent.putExtra("com.finn.laakso.hangboardapp.DESCRIPTION","selityselitys");

                startActivity(workoutHistoryTestIntent);*//*

                break;
                */
            case R.id.action_benchmark:

                Intent benchmarkIntent = new Intent(getApplicationContext(), BenchmarkActivity.class);
                benchmarkIntent.putExtra("com.finn.laakso.hangboardapp.HANGBOARDNAME",everyBoard.getHangboardName() );
                benchmarkIntent.putExtra("com.finn.laakso.hangboardapp.TIMECONTROLS",timeControls.getTimeControlsIntArray() );
                benchmarkIntent.putParcelableArrayListExtra("com.finn.laakso.hangboardapp.HOLDS",
                        everyBoard.getCurrentWorkoutHoldList());
                startActivityForResult(benchmarkIntent, REQUEST_COPY_BENCHMARK);

                break;
            case R.id.action_logbook:

                Intent workoutHistoryIntent = new Intent(getApplicationContext(), WorkoutHistoryActivity.class);
                startActivityForResult(workoutHistoryIntent, REQUEST_COPY_WORKOUT);

                break;
            case R.id.action_settings:

                Intent settingsIntent = new Intent(getApplicationContext(),SettingsActivity.class);
                startActivity(settingsIntent);

                break;
            case R.id.action_filter:
                Intent filterIntent = new Intent(getApplicationContext(), FilterActivity.class);
                filterIntent.putExtra("com.finn.laakso.hangboardapp.BOARDIMAGE",
                        HangboardResources.getHangboardImageResource(viewPager.getCurrentItem()));

                startActivity(filterIntent);
                break;
            default:

        }
        return true;
    }

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Setting the ActionBar icon and text. Must be a better way to do this

        try {
            android.support.v7.app.ActionBar actionBar = getSupportActionBar();
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setTitle(Html.fromHtml("<font color='#3E0E1F'>Grips & Grades</font>"));
            actionBar.setLogo(R.drawable.gripgrading48x);
            actionBar.setDisplayUseLogoEnabled(true);

        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        setContentView(R.layout.activity_main);
        leftFingerImage = (ImageView) findViewById(R.id.leftFingerImageView);
        rightFingerImage = (ImageView) findViewById(R.id.rightFingerImageView);
/*

        fingerTESTImage = (ImageView) findViewById(R.id.templateFingerImageView);
        fingerTESTImage.setVisibility(View.VISIBLE);
        fingerTESTImage.setImageResource(R.drawable.finger_template);

*/
        grade_descr_position = 1;
        newWorkoutButton = (Button) findViewById(R.id.randomizeBtn);
        if (savedInstanceState != null) {
            grade_descr_position = savedInstanceState.getInt("mainactivity_grade_desc_pos");
            String selectedGrade = "New " + HangboardResources.grades[grade_descr_position]+ "\nWorkout";
            newWorkoutButton.setText(selectedGrade);
            hangboard_descr_position = savedInstanceState.getInt("mainactivity_hangboardposition");
        }

        // Hangboard class holds all the information about grades and holds and grips
        everyBoard = new Hangboard(HangboardResources.getHangboardName(hangboard_descr_position));

        timeControls = new TimeControls();
        if (savedInstanceState != null) {
            timeControls.setTimeControls(savedInstanceState.getIntArray("mainactivity_timecontrolsintarray"));
        }
        else {
            //Default hangboard program (65min)
            timeControls.setTimeControls(new int[] {6, 6, 7 ,3 , 3, 150, 360});
        }

        // Lets use ArrayAdapter to list all the grades in to grades ListView
        gradesListView = (ListView) findViewById(R.id.gradeListView);
        gradeAdapter = new GradeListAdapter(this);
        gradesListView.setAdapter(gradeAdapter);

        // HOLDSADAPTER IS ALLREADY USING R.LAYOUT.MYTESTVIEW. CONSIDER MAKING IT MUCH MORE SWEETER FOR EXAMPLE HOLD AND GRIPS LEFT AND DIFFICULTY RIGHT
        holdsListView = (ListView) findViewById(R.id.holdsListView);

        if (savedInstanceState != null) {
            ArrayList<Hold> holds = savedInstanceState.getParcelableArrayList("mainactivity_hangboardholds");
            everyBoard.setNewWorkoutHolds(holds);
        }
        else {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
            everyBoard.randomizeNewWorkoutHolds(grade_descr_position,timeControls);
            // everyBoard.newCustomWorkoutHolds(prefs);
        }
        // holdsAdapter = new  ArrayAdapter<String>(this, R.layout.mytextview, everyBoard.getGrips());

        hangsAdapter = new HangListAdapter(this, everyBoard.getCurrentWorkoutHoldList() );
        holdsListView.setAdapter(hangsAdapter);
        registerForContextMenu(holdsListView);

        durationTextView = (TextView) findViewById(R.id.durationTextView);
        final String durationText = "Duration: " + timeControls.getTotalTime()/60 + "min";
        durationTextView.setText(durationText);

        durationSeekBar = (SeekBar) findViewById(R.id.durationSeekBar);

        repeatersBox = (CheckBox) findViewById(R.id.repeatersCheckBox);
        if (savedInstanceState != null) {
            durationSeekBar.setProgress(savedInstanceState.getInt("mainactivity_durationseekbarprogression"));
            durationSeekBar.setVisibility(savedInstanceState.getInt("mainactivity_durationseekbarvisibility"));
            repeatersBox.setVisibility(savedInstanceState.getInt("mainactivity_repeatersboxvisibility"));
        }
        else {
            repeatersBox.setChecked(true);
        }

        // Lets use HangboardSwipeAdapter to show different hangboards in a swipeable fashion
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        swipeAdapter = new HangboardSwipeAdapter(this);
        viewPager.setAdapter(swipeAdapter);

        // ViewPager for showing and swiping different HangBoards.
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

              }

            @Override
            public void onPageSelected(int position) {
                // If the new pages is scrolled (not just phone orientation changes) lets initialize
                // new board and holds
                if (hangboard_descr_position != position) {
                    hangboard_descr_position = position;
/*
                    rightFingerImage.setVisibility(View.INVISIBLE);
                    leftFingerImage.setVisibility(View.INVISIBLE);
                    */
                    durationSeekBar.setVisibility(View.VISIBLE);
                    repeatersBox.setVisibility(View.VISIBLE);

                    Hold.grip_type lastGripType = everyBoard.getLeftHandGripType(hangsAdapter.getSelectedHangNumber() - 1 );

                    everyBoard.initializeHolds(HangboardResources.getHangboardName(hangboard_descr_position));

                    if (grade_descr_position == 0) {
                        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                        everyBoard.newCustomWorkoutHolds(prefs);
                    } else {
                        everyBoard.randomizeNewWorkoutHolds(grade_descr_position, timeControls);
                    }
                    String randomizeText = "New " + HangboardResources.grades[grade_descr_position] + "\nWorkout";
                    newWorkoutButton.setText(randomizeText);

                    if (hangsAdapter.getSelectedHangNumber() != 0) {

                        animateHandImagesToPosition(lastGripType,hangsAdapter.getSelectedHangNumber() - 1);
                    }
                    // This causes huge lag in image transition, could not figure out better way update hanglist
                    // Figured out it was because I was not using ViewHolder in Adapters
                    hangsAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }

        });

        // Every time a grade is selected from the grade list, Hangboard generates holds and grips
        // to the program based on grade difficulty
        gradesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // The harder the grade the darker the color
                Drawable selectColor = gradesListView.getSelector();
                selectColor.setAlpha(90+position*15);
                gradesListView.setSelector(selectColor);

                animateFingerImagesToInvisible();

                rightFingerImage.setVisibility(View.INVISIBLE);
                leftFingerImage.setVisibility(View.INVISIBLE);

                grade_descr_position = gradesListView.getPositionForView(view);

                String randomizeText = "New "+ HangboardResources.grades[grade_descr_position]+ "\nWorkout";
                newWorkoutButton.setText(randomizeText);
                hangsAdapter.setSelectedHangNumber(0);
                hangsAdapter.notifyDataSetChanged();
/*
                // THIS IS ONLY FOR TESTING HAND IMAGES POSITION PURPOSES
                float x;
                if (position % 2 != 0) {
                    x = fingerTESTImage.getX() + position * 3; }
                else { x = fingerTESTImage.getX() - position * 3; }
                fingerTESTImage.setX(x+5);
                Log.e("FINGER COORD","X:" + fingerTESTImage.getX()/1.5 + "   Y:" + fingerTESTImage.getY()/1.5 );*/
            }
        });

        // MenuListener for user to create custom holds to the holdsList
        holdsListView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

                if (v.getId()==R.id.holdsListView) {
                    AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
                    menu.setHeaderTitle("Select to change hold or grip");

                    // We have to know how many holds there are in current hangboard
                    int max = everyBoard.getMaxHoldNumber();
                    int i = 0;
                    int j = 1;

                    // Lets add every hold that hangboard has to the menu
                    while (i < 2*max) {
                        menu.add(Menu.NONE, i, i, "HOLD: " + j + " for left hand");
                        i++;
                        menu.add(Menu.NONE, i, i, "HOLD: " + j + " for right hand");
                        j++;
                        i++;
                    }

                    // Lets add different grip types to the menu
                    String[] menuItems = getResources().getStringArray(R.array.grip_types);
                    j=0;
                    while ( j < menuItems.length) {
                        menu.add(Menu.NONE,i,i,"GRIP: " + menuItems[j]);
                        i++;
                        j++;
                    }


                }

            }


        });



        // Every time a hold is pressed on the holdsList, update to randomize button only
        // to randomize that hold. And everyBoard to show that Hold's picture
        holdsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                int lastPosition = hangsAdapter.getSelectedHangNumber() - 1;

                hangsAdapter.setSelectedHangNumber(position+1);

                String randomizeText = "New " + HangboardResources.grades[grade_descr_position] + "\nHang to " + (position +1 ) + ".";
                newWorkoutButton.setText(randomizeText);

                Hold.grip_type lastGripType = everyBoard.getLeftHandGripType(lastPosition);
                animateHandImagesToPosition(lastGripType, position);


                hangsAdapter.notifyDataSetChanged();

/*


                // THIS IS ONLY FOR TESTING HAND IMAGES POSITION
                // REMEMBER RIGHT DEVICE NEXUS S AP/ 27, AND TO DIVIDE X AND Y BY 1.5
                // you can compare coord values to values that device puts and figure out relation, its 1.5 on nexus s api 27
                // REMEMBER ALSO PHONE ORIENTATION. FINGERIMAGE MUST BE DECLARET IN BOTH LANDSCAPE AND PORTRAIT MODE
               float y;
                if (position % 2 != 0) {
                y = fingerTESTImage.getY() + position*3; }
                else {y = fingerTESTImage.getY() - position*3; }
                fingerTESTImage.setY(y+5);

                //rightFingerImage.setVisibility(View.INVISIBLE);
                 //leftFingerImage.setVisibility(View.INVISIBLE);

                // Log.d("FINGER COORD","X:" + fingerTESTImage.getX() + "   Y:" + fingerTESTImage.getY() );
                Log.e("FINGER COORD","X:" + fingerTESTImage.getX()/1.5 + "   Y:" + fingerTESTImage.getY()/1.5 );

*/

            }
        });

        // Attempts to launch an activity within our own app
        startWorkoutButton = (Button) findViewById(R.id.startWorkoutBtn);
        startWorkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent workoutIntent = new Intent(getApplicationContext(), WorkoutActivity.class);

                // Lets pass the necessary information to WorkoutActivity; time controls, hangboard image, and used holds with grip information
                workoutIntent.putExtra("com.finn.laakso.hangboardapp.TIMECONTROLS",timeControls.getTimeControlsIntArray() );
                workoutIntent.putExtra("com.finn.laakso.hangboardapp.BOARDIMAGE",
                        HangboardResources.getHangboardImageResource(viewPager.getCurrentItem()));
                workoutIntent.putParcelableArrayListExtra("com.finn.laakso.hangboardapp.HOLDS",
                        everyBoard.getCurrentWorkoutHoldList());

                startActivity(workoutIntent);

            }
        });

        // RnewWorkoutButton listener that randomizes hold or holds that user wants
        newWorkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if ( hangsAdapter.getSelectedHangNumber() == 0 ) {
                    // different method for creating workout holds if user has custom grade selected
                    if (grade_descr_position == 0) {
                        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                        everyBoard.newCustomWorkoutHolds(prefs);
                    }
                    else {
                        everyBoard.randomizeNewWorkoutHolds(grade_descr_position, timeControls);
                    }

                    //testAndCollectDataRandomizeNewWorkoutHolds(grade_descr_position,timeControls);
                    if (!repeatersBox.isChecked()) {
                        everyBoard.setHoldsForSingleHangs();
                    }
                }
                // randomize a single grip
                else {
                    int hangPosition = hangsAdapter.getSelectedHangNumber() - 1;

                    Hold.grip_type oldGripType = everyBoard.getLeftHandGripType(hangPosition);

                    if (grade_descr_position == 0) {
                        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                        everyBoard.newCustomWorkoutHold(prefs, hangPosition);
                    }
                    else {
                        everyBoard.randomizeNewWorkoutHold(grade_descr_position, hangPosition,timeControls);
                    }
                    animateHandImagesToPosition(oldGripType,hangPosition);
/*
                    ImageView imageView = (ImageView) findViewById(R.id.image_view);
                    Float multiplyer_w = imageView.getWidth() / 350F;
                    Float multiplyer_h = imageView.getHeight() / 150F;

                    leftFingerImage.setImageResource(everyBoard.getLeftFingerImage(hangPosition));
                    leftFingerImage.setX(everyBoard.getCoordLefthandX(hangPosition)* multiplyer_w);
                    leftFingerImage.setY(everyBoard.getCoordLefthandY(hangPosition)* multiplyer_h);

                    rightFingerImage.setImageResource(everyBoard.getRightFingerImage(hangPosition));
                    rightFingerImage.setX(everyBoard.getCoordRighthandX(hangPosition)*multiplyer_w);
                    rightFingerImage.setY(everyBoard.getCoordRighthandY(hangPosition)*multiplyer_h);*/
                }

                hangsAdapter.notifyDataSetChanged();


            }
        });

        // timeControlBtn starts settings activity where user can control mainly time control settings
        settingsButton = (Button) findViewById(R.id.timeControlBtn);
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent settingsIntent = new Intent(getApplicationContext(), TimeControlsActivity.class);
                settingsIntent.putExtra("com.finn.laakso.hangboardapp.TIMECONTROLS", timeControls.getTimeControlsIntArray() );

                startActivityForResult(settingsIntent, REQUEST_TIME_CONTROLS);

            }
        });

        // There are two main types of hang programs called repeaters or single hangs
        repeatersBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    timeControls.setHangLaps(2);

                } else {
                    timeControls.setHangLaps(1);

                }
                timeControls.setPremadeTimeControls(durationSeekBar.getProgress() );

                // If the progressBar is "TEST progress" we must sort the holds
                if (durationSeekBar.getProgress() == 7) {
                    everyBoard.sortHoldByDifficulty();
                    timeControls.setGripLaps((everyBoard.getCurrentHoldListSize()/2));
                }
                else {
                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                    everyBoard.setGripAmount(timeControls,grade_descr_position,prefs);
                }

                if (!isChecked) {
                    everyBoard.setHoldsForSingleHangs();
                }

                String durationText = "Duration: " + timeControls.getTotalTime()/60 + "min";

                durationTextView.setText(durationText);
                hangsAdapter.setSelectedHangNumber(0);
                String randomizeText = "New " + HangboardResources.grades[grade_descr_position] + "\nWorkout";
                newWorkoutButton.setText(randomizeText);
                hangsAdapter.notifyDataSetChanged();

                animateFingerImagesToInvisible();

                // rightFingerImage.setVisibility(View.INVISIBLE);
                // leftFingerImage.setVisibility(View.INVISIBLE);

            }
        });

        durationSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                String durationText;
                hangsAdapter.setSelectedHangNumber(0);
                // In "TEST progression" case we must sort the holds by their difficulty
                if (progress == 7) {
                    gradesListView.setVisibility(View.INVISIBLE);

                    durationText = "progression TEST";
                    timeControls.setPremadeTimeControls(progress);
                    //timeControls.setProgramBasedOnTime(20 + progress * 15);

                    everyBoard.sortHoldByDifficulty();
                    timeControls.setGripLaps(everyBoard.getCurrentHoldListSize()/2 );
                }
                else {
                    gradesListView.setVisibility(View.VISIBLE);

                    timeControls.setPremadeTimeControls(progress);
                    //timeControls.setProgramBasedOnTime(20 + progress * 15);
                    durationText = "Duration: " + timeControls.getTotalTime()/60 + "min";

                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                    everyBoard.setGripAmount(timeControls,grade_descr_position,prefs);


                    if (!repeatersBox.isChecked() ) {
                        everyBoard.setHoldsForSingleHangs();
                    }

                }

                String randomizeText = "New " + HangboardResources.grades[grade_descr_position] + "\nWorkout";
                newWorkoutButton.setText(randomizeText);

                durationTextView.setText(durationText);

                animateFingerImagesToInvisible();

                // rightFingerImage.setVisibility(View.INVISIBLE);
                // leftFingerImage.setVisibility(View.INVISIBLE);

                hangsAdapter.notifyDataSetChanged();

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {


            }
        });


    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();

        int menuItemIndex = info.position;
        int itemId = item.getItemId();

        // with item.getItemId() we can extract the data which hand hold or grip type was selected
        // not very elegant way at all
        Hold.grip_type fromGripType = everyBoard.getLeftHandGripType(hangsAdapter.getSelectedHangNumber() -1 );
        everyBoard.addCustomHold(itemId,menuItemIndex);

        // Animate fingers only if hang was changed
        hangsAdapter.setSelectedHangNumber(menuItemIndex +1);
        animateHandImagesToPosition(fromGripType, menuItemIndex);

        hangsAdapter.notifyDataSetChanged();

        return true;
    }

    // Lets get the time controls settings back to main activity.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
            if (requestCode == REQUEST_TIME_CONTROLS) {
                if (resultCode == Activity.RESULT_OK ) {
                    int[] i = data.getIntArrayExtra("com.finn.laakso.hangboardapp.SETTINGS");

                    // If Grip laps amount has been changed we have to randomize new grips, otherwise lets
                    // keep the old grips that user has maybe liked
                    if (i[0] != timeControls.getGripLaps()) {
                        timeControls.setTimeControls(i);
                        everyBoard.setGripAmount(timeControls, grade_descr_position,prefs);

                        hangsAdapter.notifyDataSetChanged();

                        hangsAdapter.setSelectedHangNumber(0);
                    } else {
                        timeControls.setTimeControls(i);
                    }

                    //Disable the slider and check box, so that those are accidentally changed
                    //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
                     if (prefs.getBoolean("helpSwitch",true) ) {
                         Toast.makeText(MainActivity.this, "Settings applied, pre made time controls disabled ", Toast.LENGTH_SHORT).show();
                     }
                    repeatersBox.setVisibility(View.INVISIBLE);
                    durationSeekBar.setVisibility(View.INVISIBLE);
                }
                else {
                    if (prefs.getBoolean("helpSwitch",true) ) {
                        Toast.makeText(MainActivity.this, "Settings not applied, pre made time controls enabled", Toast.LENGTH_SHORT).show();
                    }
                    repeatersBox.setVisibility(View.VISIBLE);
                    durationSeekBar.setVisibility(View.VISIBLE);
                }

            } // Enabling them when settings are not saved, in future must be made more intuitive.


            // If user has copied workout from workout history database
            if (requestCode == REQUEST_COPY_WORKOUT) {
                if (resultCode == Activity.RESULT_OK) {
                    if (prefs.getBoolean("helpSwitch",true) ) {
                        Toast.makeText(MainActivity.this, "Workout copied", Toast.LENGTH_SHORT).show();
                    }
                    int[] timeSettings = data.getIntArrayExtra("com.finn.laakso.hangboardapp.SETTINGS");
                    ArrayList<Hold> newHolds = data.getParcelableArrayListExtra("com.finn.laakso.hangboardapp.HOLDS");
                    String hbname = data.getStringExtra("com.finn.laakso.hangboardapp.BOARDNAME");

                    if ( !HangboardResources.isThereHangboard(hbname) ) {
                        Toast.makeText(MainActivity.this, "Could not find hangboard named: "+hbname, Toast.LENGTH_LONG).show();
                    }

                    TimeControls temp = new TimeControls();
                    temp.setTimeControls(timeSettings);

                    int hangboardPosition = HangboardResources.getHangboardPosition(hbname);
                    HangboardResources.hangboardName newHangboard = HangboardResources.getHangboardName(hangboardPosition);

                    hangboard_descr_position = hangboardPosition;
                    viewPager.setCurrentItem(hangboardPosition);
                    timeControls.setTimeControls(timeSettings);
                    everyBoard.initializeHolds(newHangboard);
                    everyBoard.setNewWorkoutHolds(newHolds);

                    hangsAdapter = new HangListAdapter(this, everyBoard.getCurrentWorkoutHoldList());
                    holdsListView.setAdapter(hangsAdapter);

                    repeatersBox.setVisibility(View.INVISIBLE);
                    durationSeekBar.setVisibility(View.INVISIBLE);
                }
                else {
                    repeatersBox.setVisibility(View.VISIBLE);
                    durationSeekBar.setVisibility(View.VISIBLE);
                }
            }


        // If user has copied workout from workout history database
        if (requestCode == REQUEST_COPY_BENCHMARK) {
            if (resultCode == Activity.RESULT_OK) {
                if (prefs.getBoolean("helpSwitch",true) ) {
                    Toast.makeText(MainActivity.this, "Pre made workout copied", Toast.LENGTH_SHORT).show();
                }
                Hold.grip_type oldGripType = everyBoard.getLeftHandGripType(hangsAdapter.getSelectedHangNumber() );

                int[] timeSettings = data.getIntArrayExtra("com.finn.laakso.hangboardapp.SETTINGS");
                ArrayList<Hold> newHolds = data.getParcelableArrayListExtra("com.finn.laakso.hangboardapp.HOLDS");
                String hbname = data.getStringExtra("com.finn.laakso.hangboardapp.BOARDNAME");

                TimeControls temp = new TimeControls();
                temp.setTimeControls(timeSettings);

                int hangboardPosition = HangboardResources.getHangboardPosition(hbname);
                HangboardResources.hangboardName newHangboard = HangboardResources.getHangboardName(hangboardPosition);

                hangboard_descr_position = hangboardPosition;

                viewPager.setCurrentItem(hangboardPosition);
                timeControls.setTimeControls(timeSettings);
                everyBoard.initializeHolds(newHangboard);
                everyBoard.setNewWorkoutHolds(newHolds);

                hangsAdapter = new HangListAdapter(this, everyBoard.getCurrentWorkoutHoldList());
                hangsAdapter.setSelectedHangNumber(1);

                holdsListView.setAdapter(hangsAdapter);
                //hangsAdapter.setSelectedHangNumber(1);

//                animateHandImagesToPosition(oldGripType,hangsAdapter.getSelectedHangNumber() );
                animateHandImagesToPosition(oldGripType,0 );

                newWorkoutButton.setText("New " + HangboardResources.grades[grade_descr_position] + "\nHang to "+
                        hangsAdapter.getSelectedHangNumber()+".");
                repeatersBox.setVisibility(View.INVISIBLE);
                durationSeekBar.setVisibility(View.INVISIBLE);
            }
            else {
                repeatersBox.setVisibility(View.VISIBLE);
                durationSeekBar.setVisibility(View.VISIBLE);
            }
        }

            String durationText = "Duration: " + timeControls.getTotalTime() / 60 + "min";
            durationTextView.setText(durationText);

    }

    private void animateHandImagesToPosition(Hold.grip_type fromGripType, int newPosition) {

        ImageView imageView = (ImageView) findViewById(R.id.image_view);

        // For some reason if phone orientation was changed when on child activity, it causes
        // imageView to be null. Not understanding this fully yet as of 20.2.2019
        if (imageView == null) {
            return;
        }
        if (leftFingerImage.getVisibility() == View.INVISIBLE || rightFingerImage.getVisibility() == View.INVISIBLE) {

            Animation leftFingerFadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in500ms);
            Animation rightFingerFadeIn = AnimationUtils.loadAnimation(this,R.anim.fade_in500ms);
            leftFingerFadeIn.reset();
            rightFingerFadeIn.reset();
            ImageView leftAnim = (ImageView) findViewById(R.id.leftFingerImageView);
            ImageView rightAnim = (ImageView) findViewById(R.id.rightFingerImageView);
            leftAnim.clearAnimation();
            rightAnim.clearAnimation();
            leftAnim.startAnimation(leftFingerFadeIn);
            rightAnim.startAnimation(rightFingerFadeIn);

            leftFingerImage.setVisibility(View.VISIBLE);
            rightFingerImage.setVisibility(View.VISIBLE);

            leftFingerImage.setY(leftFingerImage.getY() + 50);
            rightFingerImage.setY(rightFingerImage.getY() + 50);

        }

        leftFingerImage.setImageResource(everyBoard.getLeftFingerImage(newPosition));
        rightFingerImage.setImageResource(everyBoard.getRightFingerImage(newPosition));

        // Hopefully this multiplier works in every android device

        Float    multiplier_width = imageView.getWidth() / 350F;
        Float    multiplier_height = imageView.getHeight() / 150F;

        Float newLeftHandCoordX = everyBoard.getCoordLefthandX(newPosition) * multiplier_width;
        Float newLeftHandCoordY = everyBoard.getCoordLefthandY(newPosition) * multiplier_height;
        Float newRightHandCoordX = everyBoard.getCoordRighthandX(newPosition) * multiplier_width;
        Float newRightHandCoordY = everyBoard.getCoordRighthandY(newPosition) * multiplier_height;

        ObjectAnimator leftHandAnimatorX = ObjectAnimator.ofFloat(leftFingerImage,"x",newLeftHandCoordX);
        ObjectAnimator leftHandAnimatorY = ObjectAnimator.ofFloat(leftFingerImage,"y",newLeftHandCoordY);
        ObjectAnimator rightHandAnimatorX = ObjectAnimator.ofFloat(rightFingerImage,"x",newRightHandCoordX);
        ObjectAnimator rightHandAnimatorY = ObjectAnimator.ofFloat(rightFingerImage,"y",newRightHandCoordY);

        leftHandAnimatorX.setDuration(ANIMATION_DURATION);
        leftHandAnimatorY.setDuration(ANIMATION_DURATION);
        rightHandAnimatorX.setDuration(ANIMATION_DURATION);
        rightHandAnimatorY.setDuration(ANIMATION_DURATION);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(leftHandAnimatorX);
        animatorSet.playTogether(leftHandAnimatorY);
        animatorSet.playTogether(rightHandAnimatorX);
        animatorSet.playTogether(rightHandAnimatorY);

        animatorSet.start();

        if ( newPosition < 0 || (newPosition+1)*2 > everyBoard.getCurrentHoldListSize() ) {return; }

        Hold.grip_type newGripType = everyBoard.getLeftHandGripType(newPosition);

        int animationResourcesLeftHand = FingerAnimationBuilder.getHandTransitionStart(fromGripType,newGripType,true);
        int animationResourecesRightHand = FingerAnimationBuilder.getHandTransitionStart(fromGripType,newGripType,false);

        if ( animationResourcesLeftHand == 0 ) {  return;}
        if ( animationResourecesRightHand == 0 ) { return; }

        rightFingerImage.setImageResource(animationResourecesRightHand);
        leftFingerImage.setImageResource(animationResourcesLeftHand );

         AnimationDrawable rightHandAnimation = (AnimationDrawable) rightFingerImage.getDrawable();
         AnimationDrawable leftHandAnimation = (AnimationDrawable) leftFingerImage.getDrawable();

        rightHandAnimation.start();
        leftHandAnimation.start();



    }

    private void animateFingerImagesToInvisible() {

        if (leftFingerImage.getVisibility() == View.VISIBLE ||rightFingerImage.getVisibility() == View.VISIBLE) {

            Animation leftFingerFadeIn = AnimationUtils.loadAnimation(this, R.anim.hide_fingerimages);
            Animation rightFingerFadeIn = AnimationUtils.loadAnimation(this,R.anim.hide_fingerimages);
            leftFingerFadeIn.reset();
            rightFingerFadeIn.reset();
            ImageView leftAnim = (ImageView) findViewById(R.id.leftFingerImageView);
            ImageView rightAnim = (ImageView) findViewById(R.id.rightFingerImageView);
            leftAnim.clearAnimation();
            rightAnim.clearAnimation();
            leftAnim.startAnimation(leftFingerFadeIn);
            rightAnim.startAnimation(rightFingerFadeIn);

            leftFingerImage.setVisibility(View.INVISIBLE);
            rightFingerImage.setVisibility(View.INVISIBLE);

        }

    }

    // This is for testing randomizeWorkout button
    /*
   private void testAndCollectDataRandomizeNewWorkoutHolds(int grade_descr_position,TimeControls timeControls) {

        int datapoints = 100;

        ArrayList<Hold> tempHoldList ;
        ArrayList<Integer> holdDifficulties = new ArrayList<>();
        TreeMap<Hold.grip_type,Integer> holdTypeMap = new TreeMap<Hold.grip_type, Integer>();
        TreeMap<Integer,Integer> holdNumberMap = new TreeMap<Integer, Integer>();

        int totalHoldAmount = timeControls.getGripLaps()*2* datapoints;

        for (int i = 0 ; i < datapoints ; i++) {
            everyBoard.randomizeNewWorkoutHolds(grade_descr_position, timeControls);

            tempHoldList = everyBoard.getCurrentWorkoutHoldList();

            for (int j = 0 ; j < tempHoldList.size() ; j++) {
                holdDifficulties.add(tempHoldList.get(j).getHoldValue() );

                if (holdTypeMap.containsKey( tempHoldList.get(j).getGripStyle() ) ) {
                    holdTypeMap.put(tempHoldList.get(j).getGripStyle(), holdTypeMap.get(tempHoldList.get(j).getGripStyle() ) +1 );
                }
                else {
                    holdTypeMap.put(tempHoldList.get(j).getGripStyle(),1);
                }

                if (holdNumberMap.containsKey( tempHoldList.get(j).getHoldNumber() ) ) {
                    holdNumberMap.put(tempHoldList.get(j).getHoldNumber(), holdNumberMap.get(tempHoldList.get(j).getHoldNumber() ) +1 );
                }
                else {
                    holdNumberMap.put(tempHoldList.get(j).getHoldNumber(),1);
                }

            }

        }

        for(Map.Entry<Hold.grip_type,Integer> entry: holdTypeMap.entrySet()) {
            Hold.grip_type key = entry.getKey();
            int value = entry.getValue();

            if ( value == 0) {continue;} // custom hold

           // Log.d("Grip types",key + ": " + value + "  avg: " +value*100/totalHoldAmount);

        }

        for(Map.Entry<Integer,Integer> entry: holdNumberMap.entrySet()) {
            int key = entry.getKey();
            int value = entry.getValue();

            if ( value == 0) {continue;} // custom hold

           // Log.d("Hold Numbers","hold:" +key + " : " + value+ "  avg: " +value*100/totalHoldAmount);

        }
        int total = 0;
        for (int i = 0 ; i < holdDifficulties.size() ; i++ ) {
            total += holdDifficulties.get(i);
        }
        // Log.d("hold difficulties","avg: " + (float)total/totalHoldAmount );

    }

*/

/*
    private void testMethod() {
        Random rng = new Random();

        timeControls = WorkoutHistoryActivity.getTotallyRandomTimeControls();

        int gradePosition = rng.nextInt(11);

        everyBoard.setGripAmount(timeControls.getGripLaps(), gradePosition);

        hangsAdapter.notifyDataSetChanged();

    }
    */
}
