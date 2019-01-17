package com.finn.laakso.hangboardapp;

import android.app.Activity;
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

    private static final int COLOR_DARKERGRAY = Color.argb(200,96,96,96);
    private static final int COLOR_GRAY = Color.argb(66,169,169,169);
    private static final int COLOR_HIDDENCOLOR = Color.argb(255,102,24,51);
    private static final int COLOR_BLACK = Color.BLACK;

    //private LayoutInflater mInflator;
    private Context mContext;

    // Database Handler to help put items correctly on a view
    private WorkoutDBHandler dbHandler;

    private boolean[] selectedWorkouts;
    private static int selectedAmount;

    private boolean showHidden;
    // private Cursor dbCursor;

    static class HistoryListViewHolder {
        TextView dateTextView;
        TextView workoutTimeTextView;
        TextView workoutDescriptionTextView;
        TextView workoutNumberTextView;
        int position;
    }

    public void workoutClicked(int position) {
        if (selectedWorkouts[position] ) {
            selectedWorkouts[position] = false;
            selectedAmount--;
        } else {
            selectedWorkouts[position] = true;
            selectedAmount++;
        }

        // Log.d("Selected amount" ,  ": " + selectedAmount );
/*        for (int i = 0 ; i < selectedWorkouts.length ; i++) {
            Log.d("t or F" , i + ": " + selectedWorkouts[i] );
        }*/
    }

    public int getSelectedWorkoutsAmount() {
        return selectedAmount;
    }

    public void clearSelectedWorkouts() {
        selectedWorkouts = new boolean[getCount()];
        selectedAmount = 0;
    }

    public WorkoutHistoryAdapter(Context c, WorkoutDBHandler dbHandler, boolean showHidden) {
        this.dbHandler = dbHandler;
        this.showHidden = showHidden;
        //this.mInflator = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mContext = c;

        selectedWorkouts = new boolean[getCount()];
        selectedAmount = 0;
        // Log.d("boolean","count: " + selectedWorkouts.length);
    }

    public boolean getShowHiddenStatus() {
        return this.showHidden;
    }

    public boolean[] getSelectedWorkouts() {
        return selectedWorkouts;
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

        HistoryListViewHolder viewHolder;

        if (convertView == null) {

            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();

            convertView = inflater.inflate(R.layout.workout_history_listview,parent,false);
            viewHolder = new HistoryListViewHolder();
            viewHolder.dateTextView = (TextView) convertView.findViewById(R.id.dateTextView);
            viewHolder.workoutTimeTextView = (TextView) convertView.findViewById(R.id.workoutTimeTextView);
            viewHolder.workoutDescriptionTextView = (TextView) convertView.findViewById(R.id.workoutDescriptionTextView);
            viewHolder.workoutNumberTextView = (TextView) convertView.findViewById(R.id.workoutNumberTextView);

            viewHolder.position = position;

            convertView.setTag(viewHolder);
        }
        else {

            viewHolder = (HistoryListViewHolder) convertView.getTag();

        }

        // Database starts from 1 not 0
        position++;

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


        if (selectedWorkouts[position-1] ) {

            convertView.setBackgroundColor(COLOR_DARKERGRAY);
        } else {
            convertView.setBackgroundColor(COLOR_GRAY);
        }


        if (showHidden) {

            if (dbHandler.lookUpIsHidden(position, showHidden) ) {

                viewHolder.dateTextView.setTextColor(COLOR_HIDDENCOLOR);
                viewHolder.workoutTimeTextView.setTextColor(COLOR_HIDDENCOLOR);
                viewHolder.workoutDescriptionTextView.setTextColor(COLOR_HIDDENCOLOR);
                viewHolder.workoutNumberTextView.setTextColor(COLOR_HIDDENCOLOR);

                // convertView.setBackgroundColor(hiddenColor);
            }

            else {

                viewHolder.dateTextView.setTextColor(COLOR_BLACK);
                viewHolder.workoutTimeTextView.setTextColor(COLOR_BLACK);
                viewHolder.workoutDescriptionTextView.setTextColor(COLOR_BLACK);
                viewHolder.workoutNumberTextView.setTextColor(COLOR_BLACK);
                //convertView.setBackgroundColor(normalColor);
            }

        }
        else {
            viewHolder.dateTextView.setTextColor(COLOR_BLACK);
            viewHolder.workoutTimeTextView.setTextColor(COLOR_BLACK);
            viewHolder.workoutDescriptionTextView.setTextColor(COLOR_BLACK);
            viewHolder.workoutNumberTextView.setTextColor(COLOR_BLACK);
            //convertView.setBackgroundColor(normalColor);

        }



        viewHolder.workoutNumberTextView.setText(workoutNumber);
        viewHolder.workoutDescriptionTextView.setText(workoutDescrption);
        viewHolder.workoutTimeTextView.setText(workoutTime);
        viewHolder.dateTextView.setText(workoutDate);

        return convertView;
  /*

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


        return v;*/
    }
}