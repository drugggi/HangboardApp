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
    ArrayList<ArrayList<Hold>> ArrayListOfHolds;


    public WorkoutHistoryAdapter(Context c,ArrayList<String> dates, ArrayList<TimeControls> tc) {

        this.dates = dates;
        timeControls = tc;

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

        String board="HANGBOARD\n" + dates.get(position);
        String hold=" time: " + timeControls.get(position).getTotalTime();
        String grade="DIFFICULTY" + timeControls.get(position).getLongRestTime();

        boardTextView.setText(board);
        holdsTextView.setText(hold);
        workoutTextView.setText(grade);
        return v;
    }
}