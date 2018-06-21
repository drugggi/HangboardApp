package com.finn.laakso.hangboardapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class EditWorkoutInfo extends AppCompatActivity {

    static final String[] numbers = new String[] {
            "A", "B", "C", "D", "E",
            "F", "G", "H", "I", "J",
            "K", "L", "M", "N", "O",
            "P", "Q", "R", "S", "T",
            "U", "V", "W", "X", "Y", "Z"};

    GridView workoutInfoGridView;
    TextView hangInfoTextView;

    ArrayList<Hold> workoutHolds;
    String hangboardName;
    TimeControls timeControls;

    ImageView hangboard;
    WorkoutInfoAdapter workoutInfoAdapter;

    Button saveButton;

    int[] completed;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_workout_info);


        saveButton = (Button) findViewById(R.id.saveButton);

        hangInfoTextView = (TextView) findViewById(R.id.hangInfoTextView);

        if (getIntent().hasExtra("com.finn.laakso.hangboardapp.HOLDS")) {
            workoutHolds = getIntent().getExtras().getParcelableArrayList("com.finn.laakso.hangboardapp.HOLDS");
        }

        // Hangboard image that user has selected
        if (getIntent().hasExtra("com.finn.laakso.hangboardapp.BOARDNAME")) {
            hangboardName = getIntent().getStringExtra("com.finn.laakso.hangboardapp.BOARDNAME");
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
        }

        completed = new int[timeControls.getGripLaps() * timeControls.getRoutineLaps()];

        for (int i = 0; i < completed.length ; i++) {
            completed[i] = 0;
        }

        if(getIntent().hasExtra("com.finn.laakso.hangboardapp.COMPLETEDHANGS")) {
            completed = getIntent().getExtras().getIntArray("com.finn.laakso.hangboardapp.COMPLETEDHANGS");
        }

        workoutInfoGridView = (GridView) findViewById(R.id.workoutInfoGridView);


        //ArrayAdapter<String> workoutInfoAdapter = new ArrayAdapter<String>(this,
          //              android.R.layout.simple_list_item_1, numbers);



        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent statsIntent = new Intent(getApplicationContext(), WorkoutStatistics.class);

                // Lets pass the necessary information to WorkoutActivity; time controls, hangboard image, and used holds with grip information
                statsIntent.putExtra("com.finn.laakso.hangboardapp.TIMECONTROLS",timeControls.getTimeControlsIntArray() );
                statsIntent.putExtra("com.finn.laakso.hangboardapp.BOARDNAME",hangboardName );
                statsIntent.putParcelableArrayListExtra("com.finn.laakso.hangboardapp.HOLDS",workoutHolds);
                statsIntent.putExtra("com.finn.laakso.hangboardapp.COMPLETEDHANGS",completed);

                startActivity(statsIntent);
            }
        });

        workoutInfoAdapter = new WorkoutInfoAdapter(this,timeControls,workoutHolds, completed);
        workoutInfoGridView.setAdapter(workoutInfoAdapter);
        registerForContextMenu(workoutInfoGridView);

        workoutInfoGridView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

                if (v.getId() == R.id.workoutInfoGridView) {
                    //Toast.makeText(EditWorkoutInfo.this, "Context Menu Created ", Toast.LENGTH_SHORT).show();

                    AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
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



        workoutInfoGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               // Toast.makeText(EditWorkoutInfo.this,"pos: " + position,Toast.LENGTH_SHORT).show();

                int hold_position = position % timeControls.getGripLaps();

                String text =workoutHolds.get(hold_position).getHoldInfo(workoutHolds.get(hold_position+1));
                text = text.replaceAll("\n",", ");
                hangInfoTextView.setText(text);

//                hangInfoTextView.setText(workoutHolds.get(hold_position).getHoldInfo(workoutHolds.get(hold_position+1)));

            }
        });


    }

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
