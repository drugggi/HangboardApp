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

    LayoutInflater mInflator;

    // Database Handler to help put items correctly on a view
    MyDBHandler db;

    boolean showHidden;
    Cursor dbCursor;


    public WorkoutHistoryAdapter(Context c, MyDBHandler dbHandler, boolean showHidden) {
        this.db = dbHandler;
        this.showHidden = showHidden;
        mInflator = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        dbCursor = dbHandler.getSortedContents();

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
        TextView boardTextView = (TextView) v.findViewById(R.id.dateTextView);
        TextView holdsTextView = (TextView) v.findViewById(R.id.hangboardTextView);
        TextView workoutTextView = (TextView) v.findViewById(R.id.difficultyTextView);

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");

        Date resultdate = new Date(db.lookUpDate(position));

        String board = db.lookUpHangboard(position) + "\n" + sdf.format(resultdate);

        //Log.e("non hidden"," " + db.lookUpWorkoutCount());
        // Log.e("All"," " + db.lookUpUnHiddenWorkoutCount());

        if (showHidden) {
            dbCursor = db.getHiddenContents();
        }
        else {
            dbCursor = db.getNonHiddenContents();
        }

        if (dbCursor.move(position)) {
            board = dbCursor.getString(2) + " isHID: "+ dbCursor.getInt(14);
        }
/*
        String pooltest = "NO hidden";

        if (db.lookUpIsHidden(position)) {
            pooltest = "YES hidden";
            boardTextView.setText("");
            holdsTextView.setText("");
            workoutTextView.setText("");
            return v; }*/

        String hold = " Workout time: " + db.lookUpTimeControls(position).getTotalTime() + "s";
        String grade = "TUT: " + db.lookUpTimeControls(position).getTimeUnderTension() + "s";

        if (position == 2) boardTextView.setTextColor(Color.BLUE);

        boardTextView.setText(board);
        holdsTextView.setText(hold);
        workoutTextView.setText(grade);

        /*
        Cursor curseMe;

        curseMe = db.getListContents();

        if (curseMe.move(position) ) {
            Log.e("test " , "pos  " + position);

            resultdate = new Date(curseMe.getLong(1));
            String board = curseMe.getString(2) + "\n" + sdf.format(resultdate);



            //String board = db.lookUpHangboard(position) + "\n" + sdf.format(resultdate);
            String hold = " Workout time: " + db.lookUpTimeControls(position).getTotalTime() + "s";
            String grade = "TUT: " + db.lookUpTimeControls(position).getTimeUnderTension() + "s";

            boardTextView.setText(board);
            holdsTextView.setText(hold);
            workoutTextView.setText(grade);
        }

        else {
            String board = db.lookUpHangboard(position) + "\n" + sdf.format(resultdate);
            String hold = " Workout time: " + db.lookUpTimeControls(position).getTotalTime() + "s";
            String grade = "TUT: " + db.lookUpTimeControls(position).getTimeUnderTension() + "s";

            boardTextView.setText(board);
            holdsTextView.setText(hold);
            workoutTextView.setText(grade);

        }*/
        return v;
    }
}