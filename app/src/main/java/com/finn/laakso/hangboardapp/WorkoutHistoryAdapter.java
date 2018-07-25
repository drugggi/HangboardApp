package com.finn.laakso.hangboardapp;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
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

    private LayoutInflater mInflator;

    // Database Handler to help put items correctly on a view
    private MyDBHandler db;

    private boolean showHidden;
    private Cursor dbCursor;


    public WorkoutHistoryAdapter(Context c, MyDBHandler dbHandler, boolean showHidden) {
        this.db = dbHandler;
        this.showHidden = showHidden;
        mInflator = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        dbCursor = dbHandler.getSortedContents();

    }

    public boolean getShowHiddenStatus() {
        return this.showHidden;
    }

    @Override
    public int getCount() {

        if (showHidden) {
            return db.lookUpWorkoutCount();
        }
        else {
            return db.lookUpUnHiddenWorkoutCount();
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

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");

        Date resultdate;
       // Date resultdate = new Date(db.lookUpDate(position,true));

        //String board = db.lookUpHangboard(position) + "\n" + sdf.format(resultdate);
        //String hold = " Workout time: " + db.lookUpTimeControls(position).getTotalTime() + "s";
        //String grade = "TUT: " + db.lookUpTimeControls(position).getTimeUnderTension() + "s";
        //Log.e("non hidden"," " + db.lookUpWorkoutCount());
        // Log.e("All"," " + db.lookUpUnHiddenWorkoutCount());

        String workoutDate = "fail workout date";
        String workoutTime = "fail workout time";
        String workoutDescrption = "Fail desc:";

        TimeControls timeControls = new TimeControls();

     //   Log.e("WO adapter","show hidden value: " + showHidden);

        if (showHidden) {
            dbCursor = db.getHiddenContents();
        }
        else {
            dbCursor = db.getNonHiddenContents();
        }

        if (dbCursor.move(position)) {
            resultdate = new Date(dbCursor.getLong(1));
            workoutDate = dbCursor.getString(2) + "\n" + sdf.format(resultdate);

                timeControls.setGripLaps(dbCursor.getInt(6));
                timeControls.setHangLaps(dbCursor.getInt(7));
                timeControls.setTimeON(dbCursor.getInt(8));
                timeControls.setTimeOFF(dbCursor.getInt(9));
                timeControls.setRoutineLaps(dbCursor.getInt(10));
                timeControls.setRestTime(dbCursor.getInt(11));
                timeControls.setLongRestTime(dbCursor.getInt(12));

            if (timeControls.getHangLaps() == 1) {
                workoutTime = "Single hangs\n";
            }
            else {
                workoutTime = "Repeaters\n";
            }

            workoutTime += "Time: " + timeControls.getTotalTime()/60 + "min\n" +
                    "TUT: " +  timeControls.getTimeUnderTension()/60+"min";



            workoutDescrption =  dbCursor.getString(15);
        }
/*
        String pooltest = "NO hidden";

        if (db.lookUpIsHidden(position)) {
            pooltest = "YES hidden";
            boardTextView.setText("");
            holdsTextView.setText("");
            workoutTextView.setText("");
            return v; }*/


        if (dbCursor.getInt(14) == 1) {

            int hiddenColor = Color.argb(255,102,24,51);

            dateTextView.setTextColor(hiddenColor);
            workoutTimeTextView.setTextColor(hiddenColor);
            workoutDescriptionTextView.setTextColor(hiddenColor);
        }

        dateTextView.setText(workoutDate);
        workoutTimeTextView.setText(workoutTime);
        workoutDescriptionTextView.setText(workoutDescrption);


        return v;
    }
}