package com.finn.laakso.hangboardapp;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
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
        position = position + 1;

        View v = mInflator.inflate(R.layout.workout_history_listview,null);
        TextView boardTextView = (TextView) v.findViewById(R.id.dateTextView);
        TextView holdsTextView = (TextView) v.findViewById(R.id.hangboardTextView);
        TextView workoutTextView = (TextView) v.findViewById(R.id.difficultyTextView);



        /*
        int h = db.lookUpCompletedHangs(position).length;

        Log.e("lenght","l " + h);

        int[] jes = db.lookUpCompletedHangs(position);

        String just = "";

        for (int i = 0; i < jes.length ; i++) {
            just = just + jes[i];
            Log.e("copmleted","i: "+ i + "  arvo: " + jes[i]);
        }*/

        //SQLiteDatabase database = db.getReadableDatabase();
        Cursor data = db.getListContents();

        if (data.getCount() == 0) {
            Log.e("cursor: ","database empty count: " + data.getCount());
        }/*
        else {
            while (data.moveToNext() ){
                Log.e("getString",data.getString(2));
            }
        }*/


        if (data.moveToPosition(position - 1)) {
            Log.e("getString","id: " + data.getString(0) + " " + data.getString(2));
        }
        else {
            Log.e("ei onnannut"," to pos: " + position);
        }

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");

        Date resultdate = new Date(data.getLong(1));

        String board= data.getString(2) + "\n" + sdf.format(resultdate);
        String hold=" Workout time: " + db.lookUpTimeControls(position).getTotalTime() + "s";
        String grade="TUT: " + db.lookUpTimeControls(position).getTimeUnderTension()+ "s";

        boardTextView.setText(board);
        holdsTextView.setText(hold);
        workoutTextView.setText(grade);
        return v;
    }
}