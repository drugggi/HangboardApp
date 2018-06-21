package com.finn.laakso.hangboardapp;

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
import java.util.Random;

/**
 * Created by Laakso on 21.6.2018.
 */

public class WorkoutInfoAdapter extends BaseAdapter {

    LayoutInflater mInflator;
    private final Context mContext;

    private TimeControls timeControls;
    ArrayList<Hold> workoutHolds;
    int[] hangsCompleted;

    public WorkoutInfoAdapter(Context context, TimeControls timeControls, ArrayList<Hold> holds , int[] completed) {
        this.timeControls = timeControls;
        this.workoutHolds = holds;

        this.mContext = context;
        this.mInflator = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.hangsCompleted = completed;

        for(int i = 0 ; i < hangsCompleted.length ; i++) {
            hangsCompleted[i] = timeControls.getHangLaps();
        }

        //setCompletedRandomly();
    }


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

        View v = mInflator.inflate(R.layout.single_hang_info,null);

        TextView hangPosTextView = (TextView) v.findViewById(R.id.hangPositionTextView);
        TextView completedTextView = (TextView) v.findViewById(R.id.completedTextView);
        ImageView leftHandImageView = (ImageView) v.findViewById(R.id.leftHandImageView);
        ImageView rightHandImageView = (ImageView) v.findViewById(R.id.rightHandImageView);


        int set = position/ timeControls.getGripLaps() + 1;
        int hang = position % timeControls.getGripLaps() + 1 ;

        if (position == 5) {
            hangPosTextView.setTextColor(Color.BLUE);
        }

        int hold_position = hang - 1;

        leftHandImageView.setImageResource(workoutHolds.get(2*hold_position).getGripImage(true));
        rightHandImageView.setImageResource(workoutHolds.get(2*hold_position + 1).getGripImage(false));

        hangPosTextView.setText(set + ". set (" + hang + "/" + timeControls.getGripLaps()+")");

        completedTextView.setText(hangsCompleted[position] + "/" + timeControls.getHangLaps());

        if (hangsCompleted[position] == 0) {
            completedTextView.setTextColor(Color.RED);
        }
        else {
            /*
            Drawable selectColor = gradesListView.getSelector();
            selectColor.setAlpha(90+position*15);
            gradesListView.setSelector(selectColor);*/

            int alpha = 100 + (155 * hangsCompleted[position] ) / timeControls.getHangLaps();
            //int green = 100 + (100 * hangsCompleted[position] ) / timeControls.getHangLaps();

            completedTextView.setTextColor(Color.argb(alpha,0,175,0));

            if ( hangsCompleted[position] == timeControls.getHangLaps() ) {
                completedTextView.setTypeface(Typeface.DEFAULT_BOLD);
            }
            }

        return v;
    }

    public void setCompletedRandomly() {
        Random rng = new Random();
        for (int i = 0 ; i < hangsCompleted.length; i++) {
            hangsCompleted[i] = rng.nextInt(timeControls.getHangLaps()+1);
        }


    }
}
