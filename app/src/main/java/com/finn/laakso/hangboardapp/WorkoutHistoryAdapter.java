package com.finn.laakso.hangboardapp;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * Created by Laakso on 5.6.2018.
 * WorkoutHistoryAdapter manages the workout list on WorkoutHistoryActivity. It collects the shown
 * items information from database.
 */

public class WorkoutHistoryAdapter extends BaseAdapter {

    private LayoutInflater mInflator;

    // Database Handler to help put items correctly on a view
    private MyDBHandler dbHandler;

    private boolean showHidden;
    // private Cursor dbCursor;


    public WorkoutHistoryAdapter(Context c, MyDBHandler dbHandler, boolean showHidden) {
        this.dbHandler = dbHandler;
        this.showHidden = showHidden;
        mInflator = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    public boolean getShowHiddenStatus() {
        return this.showHidden;
    }

    @Override
    public int getCount() {

        if (showHidden) {
            return dbHandler.lookUpWorkoutCount(showHidden);
        }
        else {
            return dbHandler.lookUpWorkoutCount(showHidden);
        }
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
        TextView dateTextView = (TextView) v.findViewById(R.id.dateTextView);
        TextView workoutTimeTextView = (TextView) v.findViewById(R.id.workoutTimeTextView);
        TextView workoutDescriptionTextView = (TextView) v.findViewById(R.id.workoutDescriptionTextView);
        TextView workoutNumberTextView = (TextView) v.findViewById(R.id.workoutNumberTextView);

        String workoutDescrption = dbHandler.lookUpWorkoutDescription(position,showHidden);;

        DateFormat dateFormat = SimpleDateFormat.getDateInstance();

        long workoutTimeMillis = dbHandler.lookUpDate(position,showHidden);
        String hangboard = dbHandler.lookUpHangboard(position,showHidden);
        String workoutDate = hangboard + "\n" + dateFormat.format(workoutTimeMillis);


        String workoutNumber = "" + (this.getCount() - position + 1);

        String workoutTime;
        TimeControls timeControls = dbHandler.lookUpTimeControls(position,showHidden);
        if (timeControls.getHangLaps() == 1) {
                workoutTime = "Single hangs\n";
            }
        else {
                workoutTime = "Repeaters\n";
            }

        workoutTime += "Time: " + timeControls.getTotalTime()/60 + "min\n" +
                    "TUT: " +  timeControls.getTimeUnderTension()/60+"min";


        if (dbHandler.lookUpIsHidden(position, showHidden) ) {

            int hiddenColor = Color.argb(255,102,24,51);

            dateTextView.setTextColor(hiddenColor);
            workoutTimeTextView.setTextColor(hiddenColor);
            workoutDescriptionTextView.setTextColor(hiddenColor);
            workoutNumberTextView.setTextColor(hiddenColor);
        }

        dateTextView.setText(workoutDate);
        workoutTimeTextView.setText(workoutTime);
        workoutDescriptionTextView.setText(workoutDescrption);
        workoutNumberTextView.setText(workoutNumber);


        return v;
    }
}