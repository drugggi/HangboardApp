package com.finn.laakso.hangboardapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class EditWorkoutInfoActivity extends AppCompatActivity {

    // workoutInfoGridView helps user to see and edit single hangs. With this user can see and edit
    // completed array which stores the information how successful a hang was.
    private GridView workoutInfoGridView;

    private EditText workoutDescriptionEditText;
    private String workoutDescription;
    private ImageView hangboardImageView;

    private Button saveButton;

    private ArrayList<Hold> workoutHolds;
    private String hangboardName;
    private TimeControls timeControls;

    private WorkoutInfoAdapter workoutInfoAdapter;

    // This completed matrix is the one that user edits in this activity
    private int[] completed;
    // isNewWorkout tells if we are editing old workout from database or just completed a new one
    private boolean isNewWorkout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_edit_workout_info);
        isNewWorkout = false;

        workoutDescriptionEditText = (EditText) findViewById(R.id.workoutDescriptionEditText);
        hangboardImageView = (ImageView) findViewById(R.id.hangboardImageView);
        saveButton = (Button) findViewById(R.id.saveButton);
        workoutInfoGridView = (GridView) findViewById(R.id.workoutInfoGridView);

        // This checks whether we are editing existing workout from database or new workout from WorkoutActivity
        if (getIntent().hasExtra("com.finn.laakso.hangboardapp.NEWWORKOUT")) {
           isNewWorkout = getIntent().getExtras().getBoolean("com.finn.laakso.hangboardapp.NEWWORKOUT");
        }

        if (getIntent().hasExtra("com.finn.laakso.hangboardapp.HOLDS")) {
            workoutHolds = getIntent().getExtras().getParcelableArrayList("com.finn.laakso.hangboardapp.HOLDS");
        }

        if (getIntent().hasExtra("com.finn.laakso.hangboardapp.BOARDNAME")) {
            hangboardName = getIntent().getStringExtra("com.finn.laakso.hangboardapp.BOARDNAME");
            int imageResource = HangboardResources.getHangboardImageResource(hangboardName);
            hangboardImageView.setImageResource(imageResource);
        }

        // Description is users short description of the workout, by default empty
        if (getIntent().hasExtra("com.finn.laakso.hangboardapp.DESCRIPTION")) {
            workoutDescription = getIntent().getStringExtra("com.finn.laakso.hangboardapp.DESCRIPTION");
            workoutDescriptionEditText.setText(workoutDescription);
        }

        // This Intent brings the time controls to the workout program
        if (getIntent().hasExtra("com.finn.laakso.hangboardapp.TIMECONTROLS")) {
            int[] time_controls = getIntent().getExtras().getIntArray("com.finn.laakso.hangboardapp.TIMECONTROLS");

            timeControls = new TimeControls();
            timeControls.setTimeControls(time_controls);

            // SECURITY CHECK, WILL MAKE SURE IN FUTURE TO NEVER HAPPEN
            if (timeControls.getGripLaps()*2 != workoutHolds.size()) {
                timeControls.setGripLaps(workoutHolds.size()/2);
            }
            completed = new int[timeControls.getGripLaps() * timeControls.getRoutineLaps()];

            // We have to be careful that completed matrix will always be right size!
            for (int i = 0; i < completed.length ; i++) {
                completed[i] = 0;
            }
        }

        // Completed matrix should come from the intent
        if(getIntent().hasExtra("com.finn.laakso.hangboardapp.COMPLETEDHANGS")) {
            completed = getIntent().getExtras().getIntArray("com.finn.laakso.hangboardapp.COMPLETEDHANGS");
        }

        String saveText = "Update workout to database";
        if (isNewWorkout) {
            saveText = "Save workout to database";
        }
        saveButton.setText(saveText);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                completed = workoutInfoAdapter.getCompletedMatrix();

                if (completed.length -1 == timeControls.getGripLaps() + timeControls.getRoutineLaps()) {
                    completed = new int[timeControls.getGripLaps() * timeControls.getRoutineLaps()];

                    // We have to be careful that completed matrix will always be right size!
                    for (int i = 0; i < completed.length; i++) {
                        completed[i] = timeControls.getHangLaps();
                    }
                }

                // boolean isNewWorkout tells us if we are creating new instanse of Workoutstatistics activity
                // or returning back information
                if (isNewWorkout) {
                    if (hangboardName == null) {
                        hangboardName = "Custom";
                    }
                    if (workoutDescription == null) {
                        workoutDescription = "";
                    }

                    Intent workoutIntoDatabaseIntent = new Intent(getApplicationContext(), WorkoutHistoryActivity.class);

                    // Lets pass the necessary information to WorkoutActivity; time controls, hangboard name, and used holds with grip information
                    workoutIntoDatabaseIntent.putExtra("com.finn.laakso.hangboardapp.TIMECONTROLS", timeControls.getTimeControlsIntArray());
                    workoutIntoDatabaseIntent.putExtra("com.finn.laakso.hangboardapp.BOARDNAME", hangboardName);
                    workoutIntoDatabaseIntent.putParcelableArrayListExtra("com.finn.laakso.hangboardapp.HOLDS", workoutHolds);
                    workoutIntoDatabaseIntent.putExtra("com.finn.laakso.hangboardapp.COMPLETEDHANGS", completed);
                    workoutIntoDatabaseIntent.putExtra("com.finn.laakso.hangboardapp.DESCRIPTION", workoutDescription);

                    startActivity(workoutIntoDatabaseIntent);
                    finish();
                }
                // Returning back edited workout information
                else {
                    Intent resultCompletedHangsIntent = new Intent();
                    resultCompletedHangsIntent.putExtra("com.finn.laakso.hangboardapp.COMPLETEDHANGS", workoutInfoAdapter.getCompletedMatrix());
                    resultCompletedHangsIntent.putExtra("com.finn.laakso.hangboardapp.DESCRIPTION", workoutDescription);

                    setResult(Activity.RESULT_OK, resultCompletedHangsIntent);

                    finish();
                }

            }
        });

        workoutDescriptionEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                try {
                    workoutDescription = workoutDescriptionEditText.getText().toString();
                    if (workoutDescription.length() > 255) {
                        workoutDescription = workoutDescription.substring(0,255);
                        Toast.makeText(EditWorkoutInfoActivity.this,"Only 255 characters allowed",Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }


                return false;
            }
        });

        // workoutInfoAdapter needs a context menu for editing how many hangs was completed.
        workoutInfoAdapter = new WorkoutInfoAdapter(this,timeControls,workoutHolds, completed);
        workoutInfoGridView.setAdapter(workoutInfoAdapter);
        registerForContextMenu(workoutInfoGridView);

        // Content menu for user to select if the hang was successful or not. In repeaters user
        // can be successful from 0 to max amount of hangs, usually 6 -> x/6
        workoutInfoGridView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

                if (v.getId() == R.id.workoutInfoGridView) {

                    //AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
                    if (timeControls.getHangLaps() == 1) {
                        menu.setHeaderTitle("Did you do the Hang?");
                        menu.add(Menu.NONE, 0, 0, "No  (0/1)");
                        menu.add(Menu.NONE, 1, 1, "Yes (1/1)");
                    } else {
                        menu.setHeaderTitle("How many Hangs did you do?");

                        for (int i = 0; i <= timeControls.getHangLaps() ; i++) {
                            menu.add(Menu.NONE,i,i,(i+"/" + timeControls.getHangLaps() + "  was succesful"));
                        }

                    }

                }

            }
        });

        // Lets print the selected hang's information

        workoutInfoGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                int hold_position = position % timeControls.getGripLaps();

                String text =workoutHolds.get(2*hold_position).getHoldInfo(workoutHolds.get(2*hold_position+1));
                text = text.replaceAll("\n",", ");

                String temp = text + "\n" + "Long click to edit hangs.";

                Toast.makeText(EditWorkoutInfoActivity.this,temp,Toast.LENGTH_LONG).show();

            }
        });

    }

    // OnContextItemSelected changes the selected hang's success status to what user has selected
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        // return super.onContextItemSelected(item);
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();

        int menuItemIndex = info.position;

        int selectedItem = item.getItemId();
        int[] temp = workoutInfoAdapter.getCompletedMatrix();
        temp[menuItemIndex] = selectedItem;

        workoutInfoAdapter.setValueToCompleted(menuItemIndex,selectedItem);
        workoutInfoAdapter.notifyDataSetChanged();

        return true;
    }
}
