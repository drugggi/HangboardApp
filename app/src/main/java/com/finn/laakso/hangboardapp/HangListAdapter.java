package com.finn.laakso.hangboardapp;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class HangListAdapter extends BaseAdapter {

    //private LayoutInflater mInflator;
    private final Context mContext;

    // selected hang number, start from 1, 0 means nothing is selected
    private int selectedHangNumber;

    // WorkoutHolds seen in a list
    private ArrayList<Hold> workoutHolds;

    static class HangListViewHolder {
        TextView hangPositionTextView;
        TextView hangInfoTextView;
        int position;
    }


    public HangListAdapter(Context context,  ArrayList<Hold> holds) {
        this.selectedHangNumber = 0;
        this.workoutHolds = holds;

        this.mContext = context;
        // this.mInflator = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    public void setSelectedHangNumber(int position) {
        this.selectedHangNumber = position;
    }
    public int getSelectedHangNumber() {
        return this.selectedHangNumber;
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

        HangListViewHolder viewHolder;

        if (convertView == null) {

            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();

            convertView = inflater.inflate(R.layout.hanglist,parent,false);
           viewHolder = new HangListViewHolder();
           viewHolder.hangInfoTextView = (TextView) convertView.findViewById(R.id.hangInfoTextView);
           viewHolder.hangPositionTextView = (TextView) convertView.findViewById(R.id.hangPositionTextView);

           viewHolder.position = position;

            convertView.setTag(viewHolder);
        }
        else {

            viewHolder = (HangListViewHolder) convertView.getTag();

        }



        if (selectedHangNumber == position +1 ) {
            convertView.setBackgroundColor(Color.GRAY);
        } else {
            convertView.setBackgroundColor(0xd3d3d3);
        }


        Hold leftHandHold = workoutHolds.get(2*position);
        Hold rightHandHold = workoutHolds.get(2*position + 1);

        String test = leftHandHold.getHoldInfo(rightHandHold);
        String positionText = (position+1) + ".";


        viewHolder.hangInfoTextView.setText(test);
        viewHolder.hangPositionTextView.setText(positionText);

        return convertView;
    }
}
