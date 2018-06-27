package com.finn.laakso.hangboardapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Laakso on 5.6.2018.
 */

public class WorkoutHistoryAdapter extends BaseAdapter {

    LayoutInflater mInflator;

    MyDBHandler db;



    public WorkoutHistoryAdapter(Context c) {


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
        return position;
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        position = position + 1;

        View v = mInflator.inflate(R.layout.workout_history_listview,null);
        TextView boardTextView = (TextView) v.findViewById(R.id.dateTextView);
        TextView holdsTextView = (TextView) v.findViewById(R.id.hangboardTextView);
        TextView workoutTextView = (TextView) v.findViewById(R.id.difficultyTextView);

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");

        Date resultdate = new Date(db.lookUpDate(position));

        String board= db.lookUpHangboard(position) + "\n" + sdf.format(resultdate);
        String hold=" Workout time: " + db.lookUpTimeControls(position).getTotalTime() + "s";
        String grade="TUT: " + db.lookUpTimeControls(position).getTimeUnderTension()+ "s";

        boardTextView.setText(board);
        holdsTextView.setText(hold);
        workoutTextView.setText(grade);
        return v;
    }
}