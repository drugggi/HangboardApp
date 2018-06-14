package com.finn.laakso.hangboardapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Laakso on 5.6.2018.
 */

public class WorkoutHistoryAdapter extends BaseAdapter {

    LayoutInflater mInflator;
    ArrayList<TimeControls> timeControls;
    ArrayList<String> dates;
    ArrayList<ArrayList<Hold>> arrayListOfHolds;

    MyDBHandler db;


    public WorkoutHistoryAdapter(Context c,ArrayList<String> dates, ArrayList<TimeControls> tc,
                                 ArrayList<ArrayList<Hold>> arrayOfHolds) {

        this.arrayListOfHolds = arrayOfHolds;
        this.dates = dates;
        this.timeControls = tc;

        this.db = new MyDBHandler(c,null,null,1);

        mInflator = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {

        return db.lookUpWorkoutCount();
        //return timeControls.size();
    }

    @Override
    public Object getItem(int position) {
        return dates.get(position);
    }

    public TimeControls getSelectedTimeControls(int position) {
        return timeControls.get(position);
    }

    public String getSelectedHoldInfo(int position) {

        String all_holdText = "SIZE: " + arrayListOfHolds.get(position).size() + "\n ";
        for (int i = 0; i < arrayListOfHolds.get(position).size() ; i++) {

            all_holdText = all_holdText + arrayListOfHolds.get(position).get(i).getHoldText() + " Hold NRO: " + arrayListOfHolds.get(position).get(i).getHoldNumber() + " DIFF: " +  arrayListOfHolds.get(position).get(i).getHoldValue() + "\n";
        }
        return all_holdText;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = mInflator.inflate(R.layout.workout_history_listview,null);
        TextView boardTextView = (TextView) v.findViewById(R.id.dateTextView);
        TextView holdsTextView = (TextView) v.findViewById(R.id.hangboardTextView);
        TextView workoutTextView = (TextView) v.findViewById(R.id.difficultyTextView);

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
        Date resultdate = new Date(db.lookUpDate(position));

        String board="SQL BOARDS\n" + sdf.format(resultdate);
        String hold=" Grip Laps: " + db.lookUpTimeControls(position).getGripLaps();
        String grade="TUT: " + db.lookUpTimeControls(position).getTimeUnderTension();

        boardTextView.setText(board);
        holdsTextView.setText(hold);
        workoutTextView.setText(grade);
        return v;
    }
}