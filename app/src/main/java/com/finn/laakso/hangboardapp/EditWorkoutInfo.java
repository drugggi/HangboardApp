package com.finn.laakso.hangboardapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_workout_info);

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


        workoutInfoGridView = (GridView) findViewById(R.id.workoutInfoGridView);


        //ArrayAdapter<String> workoutInfoAdapter = new ArrayAdapter<String>(this,
          //              android.R.layout.simple_list_item_1, numbers);

        int[] completed = new int[timeControls.getGripLaps() * timeControls.getRoutineLaps()];

        for (int i = 0; i < completed.length ; i++) {
            completed[i] = 0;
        }

        WorkoutInfoAdapter workoutInfoAdapter = new WorkoutInfoAdapter(this,timeControls,workoutHolds, completed);

        workoutInfoGridView.setAdapter(workoutInfoAdapter);

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
}
