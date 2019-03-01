package com.finn.laakso.hangboardapp;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

// BenchmarkHangboardAdapter manages the hangboard list in BenchmarkActivity
// Little animations are shown here and there
public class BenchmarkHangboardAdapter extends BaseAdapter {

    private final Context mContext;
    private String[] hangboardNames;
    private int selectedHangboard;

    static class HangboardViewHolder {
        TextView hangboardNameTextView;
        ImageView hangboardImageView;
        int position;

    }


    public BenchmarkHangboardAdapter(Context context) {
        selectedHangboard = 0;
        hangboardNames = HangboardResources.getHangboardNames();

        this.mContext = context;
    }


    public String getHangboardName(int position) {
        if (position >= 0 && position < hangboardNames.length ) {
            return hangboardNames[position];
        } else {
            return "Error: hangboard name";
        }
    }

    public void setSelectedHangboard(int position) {
        selectedHangboard = position;
    }

    @Override
    public int getCount() {
        return hangboardNames.length;
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

        HangboardViewHolder viewHolder;

        if (convertView == null) {

            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();

            convertView = inflater.inflate(R.layout.hangboard_listview,parent,false);
            viewHolder = new HangboardViewHolder();

            viewHolder.hangboardImageView = (ImageView) convertView.findViewById(R.id.hangboardImageView);
            viewHolder.hangboardNameTextView = (TextView) convertView.findViewById(R.id.hangboardNameTextView);
            viewHolder.position = position;

            convertView.setTag(viewHolder);
        }
        else {

            viewHolder = (HangboardViewHolder) convertView.getTag();

        }

        viewHolder.hangboardNameTextView.setText(hangboardNames[position] );
        // When selected hangboard appears slowly in the background
        if (selectedHangboard == position) {
            viewHolder.hangboardImageView.setImageResource(HangboardResources.getHangboardImageResource(position));
            Animation animation = AnimationUtils.loadAnimation(mContext,R.anim.fade_in1000ms);
            viewHolder.hangboardImageView.startAnimation(animation);
        }
        else {
            viewHolder.hangboardImageView.setImageResource(0);
        }

        return convertView;
    }
}
