package com.finn.laakso.hangboardapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Laakso on 5.6.2018.
 */

public class WorkoutHistoryAdapter extends BaseAdapter {

    LayoutInflater mInflator;
    ArrayList<TimeControls> timeControls;
    ArrayList<String> dates;
    ArrayList<ArrayList<Hold>> arrayListOfHolds;


    public WorkoutHistoryAdapter(Context c,ArrayList<String> dates, ArrayList<TimeControls> tc,
                                 ArrayList<ArrayList<Hold>> arrayOfHolds) {

        this.arrayListOfHolds = arrayOfHolds;
        this.dates = dates;
        this.timeControls = tc;

        mInflator = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        return timeControls.size();
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

        String board="RANDOM BOARDS\n" + dates.get(position);
        String hold=" Grip Laps: " + timeControls.get(position).getGripLaps();
        String grade="Hold array size" + arrayListOfHolds.get(position).size();

        boardTextView.setText(board);
        holdsTextView.setText(hold);
        workoutTextView.setText(grade);
        return v;
    }
}