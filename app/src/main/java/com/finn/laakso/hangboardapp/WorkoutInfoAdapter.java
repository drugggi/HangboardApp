package com.finn.laakso.hangboardapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Laakso on 21.6.2018.
 */

public class WorkoutInfoAdapter extends BaseAdapter {

    LayoutInflater mInflator;
    private final Context mContext;

    private TimeControls timeControls;
    ArrayList<Hold> workoutHolds;

    public WorkoutInfoAdapter(Context context, TimeControls timeControls, ArrayList<Hold> holds ) {
        this.timeControls = timeControls;
        this.workoutHolds = holds;

        this.mContext = context;
        this.mInflator = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
        //ImageView handImageView = (ImageView) v.findViewById(R.id.testImageView);

        hangPosTextView.setText(timeControls.getRoutineLaps() + ". set (1/6)");
        completedTextView.setText("6/6");

        return v;
    }
}
