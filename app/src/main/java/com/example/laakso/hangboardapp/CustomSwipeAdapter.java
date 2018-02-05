package com.example.laakso.hangboardapp;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * Created by Laakso on 9.1.2018.
 */
// CustomSwipeAdapter Creates with the help of PagerAdapter swipeable view of different hangboards
public class CustomSwipeAdapter extends PagerAdapter {
    private int[] image_resources = {R.drawable.lauta1011, R.drawable.lauta2002, R.drawable.trans,
    R.drawable.tension, R.drawable.zlag, R.drawable.moonhard, R.drawable.mooneasy, R.drawable.meto};
    private Context ctx;
    private LayoutInflater layoutInflater;

    public enum hangboard {BM1000,BM2000, TRANS, TENSION, ZLAG, MOONHARD, MOONEASY, METO};

    public static hangboard getHangBoard(int position) {
        if (position == 0) { return hangboard.BM1000; }
        if (position == 1) {return hangboard.BM2000; }
        if (position == 2) {return hangboard.TRANS; }
        if (position == 3) {return hangboard.TENSION;}
        if (position == 4) {return hangboard.ZLAG; }
        if (position == 5) {return hangboard.MOONHARD; }
        if (position == 6) {return hangboard.MOONEASY; }
        if (position == 7) {return hangboard.METO; }
        return hangboard.BM1000;
    }

    public int getImageResource(int position) {
        return image_resources[position];
    }

    public CustomSwipeAdapter(Context ctx) {
        this.ctx = ctx;
    }

    @Override
    public int getCount() {
        return image_resources.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return (view==(LinearLayout)object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        layoutInflater = (LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View item_view = layoutInflater.inflate(R.layout.swipe_layout,container,false);
        ImageView imageView = (ImageView)item_view.findViewById(R.id.image_view);

       // TextView textView = (TextView)item_view.findViewById(R.id.image_count);
        imageView.setImageResource(image_resources[position]);
       // textView.setText("Image: :" + position);
        container.addView(item_view);

        return item_view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout)object);
    }


}
