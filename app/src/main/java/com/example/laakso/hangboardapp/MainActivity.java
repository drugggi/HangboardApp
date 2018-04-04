package com.example.laakso.hangboardapp;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
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

public class MainActivity extends AppCompatActivity {

    ListView gradesListView;
    ListView holdsListView;
    ArrayAdapter<String> holdsAdapter;

    // Maybe get rid of these in the future
    int grade_descr_position;
    int hang_descr_position;

    Button startWorkout;
    Button randomizeBtn;
    Button timeControlBtn;

    CheckBox repeatersBox;
    TextView durationTextView;
    SeekBar durationSeekBar;

    ImageView leftFingerImage;
    ImageView rightFingerImage;
    //ImageView fingerImage;

    ViewPager viewPager;
    CustomSwipeAdapter adapter;

    HangBoard everyBoard;
    TimeControls timeControls;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        // HandImages that show the type of grip used in a hold, usually what fingers are used
        leftFingerImage = (ImageView) findViewById(R.id.leftFingerImageView);
        rightFingerImage = (ImageView) findViewById(R.id.rightFingerImageView);

        /*
        fingerImage = (ImageView) findViewById(R.id.templateFingerImageView);
        fingerImage.setImageResource(R.drawable.finger_template);
        fingerImage.setVisibility(View.INVISIBLE); // TESTING PURPOSES
*/

        // HangBoard class holds all the information about grades and holds and grips
        final Resources res = getResources();
        everyBoard = new HangBoard(res);
        everyBoard.initializeHolds(res, CustomSwipeAdapter.hangboard.BM1000);
        everyBoard.setGripAmount(6,0);

        //Default hangboard program (65min)
        timeControls = new TimeControls();
        timeControls.setTimeControls(new int[] {6, 6, 7 ,3 , 3, 150, 360});

        // Lets use ArrayAdapter to list all the grades in to grades ListView
        gradesListView = (ListView) findViewById(R.id.gradeListView);

        final ArrayAdapter<String> gradeAdapter = new ArrayAdapter<String>(this, R.layout.gradetextview, everyBoard.getGrades());
        gradesListView.setAdapter(gradeAdapter);

        // HOLDSADAPTER IS ALLREADY USING R.LAYOUT.MYTESTVIEW. CONSIDER MAKING IT MUCH MORE SWEETER FOR EXAMPLE HOLD AND GRIPS LEFT AND DIFFICULTY RIGHT
        holdsListView = (ListView) findViewById(R.id.holdsListView);
        holdsAdapter = new  ArrayAdapter<String>(this, R.layout.mytextview, everyBoard.setGrips(0));
        holdsListView.setAdapter(holdsAdapter);
        registerForContextMenu(holdsListView);


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
                durationSeekBar.setVisibility(View.VISIBLE);
                repeatersBox.setVisibility(View.VISIBLE);

                // Lets change the HangBoard for every swipe
                // everyBoard.NewBoard(res,CustomSwipeAdapter.getHangBoard(position));
                // Every HangBoard has different unique holds
                everyBoard.initializeHolds(res, CustomSwipeAdapter.getHangBoard(position));
                holdsAdapter = new  ArrayAdapter<String>(MainActivity.this ,
                        R.layout.mytextview , everyBoard.setGrips(grade_descr_position));
                holdsListView.setAdapter(holdsAdapter);
                durationSeekBar.setProgress(3);

                randomizeBtn.setText("New "+ everyBoard.getGrade(grade_descr_position)+ " Workout");
                hang_descr_position = 0;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        // Every time a grade is selected from the grade list, HangBoard generates holds and grips
        // to the program based on grade difficulty
        gradesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // This is useless now that gradeslistview visibility is set to invisible
                if (durationSeekBar.getProgress() == 4) {
                    Toast.makeText(MainActivity.this, "There is no grades in \"progression TEST\" program", Toast.LENGTH_LONG).show();
                    return;
                }

                Drawable selectColor = gradesListView.getSelector();
                selectColor.setAlpha(90+position*15);
                //Toast.makeText(MainActivity.this, position + " There's your alpha: "+ selectColor.getAlpha(), Toast.LENGTH_LONG).show();
                gradesListView.setSelector(selectColor);

                rightFingerImage.setVisibility(View.INVISIBLE);
                leftFingerImage.setVisibility(View.INVISIBLE);

                grade_descr_position = gradesListView.getPositionForView(view);
/*
                holdsAdapter = new  ArrayAdapter<String>(MainActivity.this ,
                        R.layout.mytextview , everyBoard.setGrips(grade_descr_position));
                holdsListView.setAdapter(holdsAdapter); */

                randomizeBtn.setText("New "+ everyBoard.getGrade(grade_descr_position)+ " Workout");
                hang_descr_position = 0;



                // THIS IS ONLY FOR TESTING HAND IMAGES POSITION PURPOSES
                /*float x;
                if (position % 2 != 0) {
                x = fingerImage.getX() + position * 3; }
                else { x = fingerImage.getX() - position * 3; }
                fingerImage.setX(x+5);*/

            }
        });

        // MenuListener for user to create custom holds to the holdsList
        holdsListView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

               // Toast.makeText(MainActivity.this, " CONTEXT MNEU", Toast.LENGTH_SHORT).show();
                if (v.getId()==R.id.holdsListView) {
                    AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
                    menu.setHeaderTitle("Select to change hold or grip");

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
                hang_descr_position = position+1;
                randomizeBtn.setText(hang_descr_position + ". New "+ everyBoard.getGrade(grade_descr_position) + " Hang" );

                // holdsListView.setCacheColorHint(android.R.color.holo_blue_dark);

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
               /* float y;
                if (position % 2 != 0) {
                y = fingerImage.getY() + position*3; }
                else {y = fingerImage.getY() - position*3; }
                fingerImage.setY(y+5);*/

            }
        });


        // Attempts to launch an activity within our own app
        startWorkout = (Button) findViewById(R.id.startWorkoutBtn);
        startWorkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent workoutIntent = new Intent(getApplicationContext(), WorkoutActivity.class);

                // DELETE THESE TWO
                //workoutIntent.putStringArrayListExtra("com.example.laakso.hangboardapp.HANGLIST", everyBoard.GetGripList() );
                //workoutIntent.putExtra("com.example.laakso.hangboardapp.COORDINATES", everyBoard.getCoordinates());
                // ArrayList<Hold> currentHoldList = everyBoard.getCurrentHoldList();
                // Lets pass the necessary information to WorkoutActivity; time controls, hangboard image, and used holds with grip information
                workoutIntent.putExtra("com.example.laakso.hangboardapp.TIMECONTROLS",timeControls.getTimeControlsIntArray() );
                workoutIntent.putExtra("com.example.laakso.hangboardapp.BOARDIMAGE",adapter.getImageResource(viewPager.getCurrentItem()));
                workoutIntent.putParcelableArrayListExtra("com.example.laakso.hangboardapp.HOLDS", everyBoard.getCurrentHoldList());

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

                    if (repeatersBox.isChecked() == false) {
                        everyBoard.setHoldsForSingleHangs();
                    }
                }
                else {
                    everyBoard.randomizeGrip(grade_descr_position,hang_descr_position-1);

                    ImageView imageView = (ImageView) findViewById(R.id.image_view);
                    int position = hang_descr_position - 1;
                    Float multiplyer_w = imageView.getWidth() / 350F;
                    Float multiplyer_h = imageView.getHeight() / 150F;

                    leftFingerImage.setImageResource(everyBoard.getLeftFingerImage(position));
                    leftFingerImage.setX(everyBoard.getCoordLeftX(position)* multiplyer_w);
                    leftFingerImage.setY(everyBoard.getCoordLeftY(position)* multiplyer_h);

                    rightFingerImage.setImageResource(everyBoard.getRightFingerImage(position));
                    rightFingerImage.setX(everyBoard.getCoordRightX(position)*multiplyer_w);
                    rightFingerImage.setY(everyBoard.getCoordRightY(position)*multiplyer_h);
                }

                ArrayAdapter<String> holdsAdapter = new  ArrayAdapter<String>(MainActivity.this ,
                        R.layout.mytextview , everyBoard.getGrips());
                holdsListView.setAdapter(holdsAdapter);


            }
        });

        // timeControlBtn lets the user control the time controls that are running in the workout
        timeControlBtn = (Button) findViewById(R.id.timeControlBtn);
        timeControlBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Toast.makeText(MainActivity.this,"X: "+ fingerImage.getWidth()+ " Y: " + fingerImage.getHeight(),Toast.LENGTH_LONG ).show();
                //Toast.makeText(MainActivity.this,"X/1.5: "+ fingerImage.getX()/1.5+ " Y/1.5: " + fingerImage.getY()/1.5 ,Toast.LENGTH_LONG ).show();
            // Toast.makeText(MainActivity.this, " Make new activity with time controls etc", Toast.LENGTH_SHORT).show()

                Intent settingsIntent = new Intent(getApplicationContext(),SettingsActivity.class);

                settingsIntent.putExtra("com.example.laakso.hangboardapp.TIMECONTROLS", timeControls.getTimeControlsIntArray() );

                setResult(Activity.RESULT_OK,settingsIntent);
               startActivityForResult(settingsIntent,1);


            }
        });

        durationTextView = (TextView) findViewById(R.id.durationTextView);
        durationSeekBar = (SeekBar) findViewById(R.id.durationSeekBar);

        repeatersBox = (CheckBox) findViewById(R.id.repeatersCheckBox);
        repeatersBox.setChecked(true);

        // There are two main types of hang programs called repeaters or single hangs
        repeatersBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    timeControls.changeTimeToRepeaters();

                } else {
                    timeControls.changeTimeToSingleHangs();

                }
                timeControls.setProgramBasedOnTime(20 + durationSeekBar.getProgress() * 15);

                // If the progressBar is "TEST progress" we must sort the holds
                if (durationSeekBar.getProgress() == 4) {
                    everyBoard.sortHoldByDifficulty();
                    timeControls.setGripLaps((everyBoard.getCurrentHoldListSize()/2));
                }
                else {
                    everyBoard.setGripAmount(timeControls.getGripLaps(),grade_descr_position);
                }

                if (!isChecked) {
                    everyBoard.setHoldsForSingleHangs();
                }

                holdsAdapter = new  ArrayAdapter<String>(MainActivity.this ,
                        R.layout.mytextview , everyBoard.getGrips());
                holdsListView.setAdapter(holdsAdapter);

            }
        });


        durationSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                // In "TEST progression" case we must sort the holds by their difficulty
                if (progress == 4) {
                    gradesListView.setVisibility(View.INVISIBLE);

                    durationTextView.setText("progression TEST");
                    timeControls.setProgramBasedOnTime(20 + progress * 15);

                    everyBoard.sortHoldByDifficulty();
                    timeControls.setGripLaps(everyBoard.getCurrentHoldListSize()/2 );
                }
                else {
                    gradesListView.setVisibility(View.VISIBLE);

                    durationTextView.setText("Duration: " + (20 + progress * 15) + "min");
                    timeControls.setProgramBasedOnTime(20 + progress * 15);

                    everyBoard.setGripAmount(timeControls.getGripLaps(),grade_descr_position);

                    if (repeatersBox.isChecked() == false) {
                        everyBoard.setHoldsForSingleHangs();
                    }

                }

                holdsAdapter = new  ArrayAdapter<String>(MainActivity.this ,
                        R.layout.mytextview , everyBoard.getGrips());
                holdsListView.setAdapter(holdsAdapter);
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
        // return super.onContextItemSelected(item);
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();

        int menuItemIndex = info.position;

        // with item.getItemId() we can extract the data which hand hold or grip type was selected
        // not very elegant way at all
        everyBoard.addCustomHold(item.getItemId(),info.position);

        holdsAdapter = new  ArrayAdapter<String>(MainActivity.this ,
                R.layout.mytextview , everyBoard.getGrips());
        holdsListView.setAdapter(holdsAdapter);


        ImageView imageView = (ImageView) findViewById(R.id.image_view);

        Float multiplyer_w = imageView.getWidth() / 350F;
        Float multiplyer_h = imageView.getHeight() / 150F;

        leftFingerImage.setImageResource(everyBoard.getLeftFingerImage(menuItemIndex));
        leftFingerImage.setX(everyBoard.getCoordLeftX(menuItemIndex)* multiplyer_w);
        leftFingerImage.setY(everyBoard.getCoordLeftY(menuItemIndex)* multiplyer_h);

        rightFingerImage.setImageResource(everyBoard.getRightFingerImage(menuItemIndex));
        rightFingerImage.setX(everyBoard.getCoordRightX(menuItemIndex)*multiplyer_w);
        rightFingerImage.setY(everyBoard.getCoordRightY(menuItemIndex)*multiplyer_h);

        /*
        int max = everyBoard.getMaxHoldNumber();

        int menuItemIndex = item.getItemId();

        if (menuItemIndex < 2*max) {
            int holdnumber = (menuItemIndex+1)/2;
            Hold customHold = new Hold(holdnumber);
        }
        else {

        }*/
//        String[] menuItems = getResources().getStringArray(R.array.menu);
      //  String menuItemName = menuItems[menuItemIndex];



        // Toast.makeText(MainActivity.this," menuitemindex eli mitä valikosta valittiin: " + menuItemIndex + " info pos eli mikä indexi holdlistinfossa 0-5: " +info.position, Toast.LENGTH_SHORT).show();


       //onCreateContextMenu(MainActivity.this,this,);
        // String listItemName = Countries[info.position];

        //TextView text = (TextView)findViewById(R.id.footer);
        //text.setText(String.format("Selected %s for item %s", menuItemName, listItemName));
        return true;

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Toast.makeText(MainActivity.this, "There is no grades in \"TEST progression\" program", Toast.LENGTH_LONG).show();
        if (resultCode == Activity.RESULT_OK) {
            //int i = data.getIntExtra("com.example.laakso.hangboardapp.SETTINGS",0);
            int[] i = data.getIntArrayExtra("com.example.laakso.hangboardapp.SETTINGS");

            // If Grip laps amount has been changed we have to randomize new grips, otherwise lets
            // keep the old grips that user has maybe liked
            if (i[0] != timeControls.getGripLaps() ) {
                timeControls.setTimeControls(i);
                everyBoard.setGripAmount(timeControls.getGripLaps(),grade_descr_position);
                holdsAdapter = new  ArrayAdapter<String>(MainActivity.this ,
                        R.layout.mytextview , everyBoard.getGrips());
                holdsListView.setAdapter(holdsAdapter);
                hang_descr_position = 0;
            }
            else {
                timeControls.setTimeControls(i);
            }

            Toast.makeText(MainActivity.this, "Settings saved, pre made time controls disabled ", Toast.LENGTH_LONG).show();
            repeatersBox.setVisibility(View.INVISIBLE);
            durationSeekBar.setVisibility(View.INVISIBLE);
            durationTextView.setText("Duration: " + timeControls.getTotalTime()/60 + "min");

        }
        else {
            Toast.makeText(MainActivity.this, "Settings not saved, pre made time controls enabled", Toast.LENGTH_LONG).show();
            repeatersBox.setVisibility(View.VISIBLE);
            durationSeekBar.setVisibility(View.VISIBLE);
            durationTextView.setText("Duration: "+ timeControls.getTotalTime()/60 + "min");
        }


    }
}
