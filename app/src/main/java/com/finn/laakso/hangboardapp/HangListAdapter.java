package com.finn.laakso.hangboardapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class HangListAdapter extends BaseAdapter {

    private LayoutInflater mInflator;
    private final Context mContext;


    private ArrayList<Hold> workoutHolds;


    public HangListAdapter(Context context,  ArrayList<Hold> holds) {
        this.workoutHolds = holds;

        this.mContext = context;
        this.mInflator = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        return workoutHolds.size()/2;
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

        View v = mInflator.inflate(R.layout.hanglist,null);
        TextView hangPositionTextView = (TextView) v.findViewById(R.id.hangPositionTextView);
        TextView hangInfoTextView = (TextView) v.findViewById(R.id.hangInfoTextView);

        Hold leftHandHold = workoutHolds.get(2*position);
        Hold rightHandHold = workoutHolds.get(2*position + 1);

        String test = leftHandHold.getHoldInfo(rightHandHold);
        String positionText = (position+1) + ".";

        hangInfoTextView.setText(test);
        hangPositionTextView.setText(positionText);

        return v;
    }
}
