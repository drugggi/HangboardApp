package com.finn.laakso.hangboardapp;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class GradeListAdapter extends BaseAdapter {

    private final Context mContext;
    private int selectGrade;
    static private int[] grade_images = new int[] {R.drawable.custom_grade_logo, R.drawable.fivea, R.drawable.fiveb,
    R.drawable.fivec, R.drawable.sixa, R.drawable.sixb, R.drawable.sixc, R.drawable.sevena,
    R.drawable.sevenb, R.drawable.sevenc, R.drawable.eighta, R.drawable.eightb};

    static class GradeViewHolder {
        ImageView gradeImageView;
        int position;
    }

    public GradeListAdapter(Context context) {
        selectGrade = 0;
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return grade_images.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {

        GradeViewHolder viewHolder;

        if (convertView == null) {

            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            convertView = inflater.inflate(R.layout.gradeimageview,null, false);
            viewHolder = new GradeViewHolder();

            viewHolder.gradeImageView = (ImageView) convertView.findViewById(R.id.gradeImageView);
            viewHolder.position = position;

            convertView.setTag(viewHolder);

        }
        else {
            viewHolder = (GradeViewHolder) convertView.getTag();
        }
        viewHolder.gradeImageView.setImageResource(grade_images[position]);
        return convertView;
    }
}
