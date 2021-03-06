package com.finn.laakso.hangboardapp;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Laakso on 21.6.2018.
 *
 * WorkoutInfoAdapter manages the cells of different hangs in a workout. Every cell is basically
 * a Hold, that has hold numbers, hold values, grip types in it. In addition it has completed information
 * that tells if the hang was successful or not, user edits this information to match how the workout went.
 * The cell has also visual images of the used grip type
 */


public class WorkoutInfoAdapter extends BaseAdapter {

    private LayoutInflater mInflator;
    private final Context mContext;

    private TimeControls timeControls;
    private ArrayList<Hold> workoutHolds;
    private int[] hangsCompleted;

    static class WorkoutInfoViewHolder {

        TextView hangPosTextView;
        TextView completedTextView;
        ImageView leftHandImageView ;
        ImageView rightHandImageView;
        int position;
    }


    public WorkoutInfoAdapter(Context context, TimeControls timeControls, ArrayList<Hold> holds , int[] completed) {
        this.timeControls = timeControls;
        this.workoutHolds = holds;

        this.mContext = context;
        this.mInflator = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.hangsCompleted = completed;
    }

    // Getter to completed matrix that is meant to be edited
    public int[] getCompletedMatrix() {
        return hangsCompleted;
    }

    public void setValueToCompleted(int position, int value) {

        // Cannot be out of bounds and larger than how many hangs user was supposed to do
        if (position < hangsCompleted.length  && value <= timeControls.getHangLaps()) {
            hangsCompleted[position] = value;
        }
    }

    // this is the same as hangsCompleted.length
    @Override
    public int getCount() {
        return timeControls.getGripLaps()*timeControls.getRoutineLaps();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        WorkoutInfoViewHolder viewHolder;

        if (convertView == null) {

            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();

            convertView = inflater.inflate(R.layout.single_hang_info,parent,false);
            viewHolder = new WorkoutInfoViewHolder();

            viewHolder.hangPosTextView = (TextView) convertView.findViewById(R.id.hangPositionTextView);
            viewHolder.completedTextView = (TextView) convertView.findViewById(R.id.completedTextView);
            viewHolder.leftHandImageView = (ImageView) convertView.findViewById(R.id.leftHandImageView);
            viewHolder.rightHandImageView = (ImageView) convertView.findViewById(R.id.rightHandImageView);

            viewHolder.position = position;

            convertView.setTag(viewHolder);
        }
        else {

            viewHolder = (WorkoutInfoViewHolder) convertView.getTag();

        }

        int set = position/ timeControls.getGripLaps() + 1;
        int hang = position % timeControls.getGripLaps() + 1 ;

        int hold_position = hang - 1;

        // Lets set the correct grip images to every cell
        viewHolder.leftHandImageView.setImageResource(workoutHolds.get(2*hold_position).getGripImage(true));
        viewHolder.rightHandImageView.setImageResource(workoutHolds.get(2*hold_position + 1).getGripImage(false));

        viewHolder.hangPosTextView.setText(set + ". set (" + hang + "/" + timeControls.getGripLaps()+")");

        // Bold every other set, so user can more easily distinguish them
        if (set % 2 == 0) {
            viewHolder.hangPosTextView.setTextColor(Color.argb(255,0,0,0));
        }
        else {
            viewHolder.hangPosTextView.setTextColor(Color.argb(155,0,0,0));
        }

        viewHolder.completedTextView.setText(hangsCompleted[position] + "/" + timeControls.getHangLaps());

        // If the hang was no completed at all, show it as red
        if (hangsCompleted[position] == 0) {
            viewHolder.completedTextView.setTextColor(Color.RED);
        }
        // other hangs are shown in different shades of green
        else {

            int alpha = 100 + (155 * hangsCompleted[position] ) / timeControls.getHangLaps();

            viewHolder.completedTextView.setTextColor(Color.argb(alpha,0,175,0));

            if ( hangsCompleted[position] == timeControls.getHangLaps() ) {
                viewHolder.completedTextView.setTypeface(Typeface.DEFAULT_BOLD);
                }
            }

        return convertView;
    }
/*
    public void setCompletedRandomly() {
        Random rng = new Random();
        for (int i = 0 ; i < hangsCompleted.length; i++) {
            hangsCompleted[i] = rng.nextInt(timeControls.getHangLaps()+1);
        }


    }
    */
}
