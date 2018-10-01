package com.finn.laakso.hangboardapp;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * Created by Laakso on 9.1.2018.
 * HangboardSwipeAdapter Creates with the help of PagerAdapter swipeable view of different hangboards.
 * It also converts them between different representation (enum, int, string)
 */

public class HangboardSwipeAdapter extends PagerAdapter {
    private static int[] image_resources = {R.drawable.lauta1011, R.drawable.lauta2002, R.drawable.trans,
    R.drawable.tension, R.drawable.zlag, R.drawable.moonhard, R.drawable.mooneasy, R.drawable.meto,
    R.drawable.rockprodigy, R.drawable.problemsolver, R.drawable.meto_contact, R.drawable.meto_wood,
    R.drawable.drcc};
    private Context ctx;
    private LayoutInflater layoutInflater;
    // int[] coordinates;

    // All supported Hangboards
    public enum hangboard {BM1000, BM2000, TRANS, TENSION, ZLAG, MOONHARD, MOONEASY, METO,
        ROCKPRODIGY, PROBLEMSOLVER, METO_CONTACT, METO_WOOD, DRCC}

    // Converts PagerAdapter position into hangboard enum.
    public static hangboard getHangBoard(int position) {
        if (position == 0) { return hangboard.BM1000; }
        else if (position == 1) {return hangboard.BM2000; }
        else if (position == 2) {return hangboard.TRANS; }
        else if (position == 3) {return hangboard.TENSION;}
        else if (position == 4) {return hangboard.ZLAG; }
        else if (position == 5) {return hangboard.MOONHARD; }
        else if (position == 6) {return hangboard.MOONEASY; }
        else if (position == 7) {return hangboard.METO; }
        else if (position == 8) {return hangboard.ROCKPRODIGY; }
        else if (position == 9) {return hangboard.PROBLEMSOLVER; }
        else if (position == 10) {return hangboard.METO_CONTACT; }
        else if (position == 11) {return hangboard.METO_WOOD; }
        else if (position == 12) {return hangboard.DRCC; }
        else {return hangboard.BM1000; }
    }

    public static int getHangboardPosition(String HB) {

        if (HB.equals("BM 1000") ) {return 0; }
        else if (HB.equals("BM 2000") ) {return 1; }
        else if (HB.equals("Transgression") ) {return 2; }
        else if (HB.equals("Tension") ) {return 3; }
        else if (HB.equals("Zlagboard") ) {return 4; }
        else if (HB.equals("Moonboard hard") ) {return 5; }
        else if (HB.equals("Moonboard easy") ) {return 6; }
        else if (HB.equals("Metolius") ) {return 7; }
        else if (HB.equals("Rock Prodigy") ) {return 8; }
        else if (HB.equals("Problemsolver") ) {return 9; }
        else if (HB.equals("Meto. Contact") ) {return 10; }
        else if (HB.equals("Meto. Wood") ) {return 11; }
        else if (HB.equals("DRCC") ) {return 12; }
        else {return 0; }
    }

    // Converts hangboard enum into describing name. Should be somewhere else
    public static String getHangboardName(hangboard HB) {

        switch (HB) {
            case BM1000:
                return "BM 1000";
            case BM2000:
                return "BM 2000";
            case TRANS:
                return "Transgression";
            case TENSION:
                return "Tension";
            case ZLAG:
                return "Zlagboard";
            case MOONHARD:
                return "Moonboard hard";
            case MOONEASY:
                return "Moonboard easy";
            case METO:
                return "Metolius";
            case ROCKPRODIGY:
                return "Rock Prodigy";
            case PROBLEMSOLVER:
                return "Problemsolver";
            case METO_CONTACT:
                return "Meto. Contact";
            case METO_WOOD:
                return "Meto. Wood";
            case DRCC:
                return "DRCC";
            default:
                return "Custom";
        }
    }

    // Returns the hangboard picture resource int. Used to get picture when name is known and name
    // is stored into database.
    public static int getHangboardResource(String hangboardName) {

        hangboard tempHB;
        String tempName;
       for (int position = 0 ; position < image_resources.length; position++) {
           tempHB = getHangBoard(position);
           tempName = HangboardSwipeAdapter.getHangboardName(tempHB);
           if (tempName.equals(hangboardName)){
               return HangboardSwipeAdapter.getImageResource(position);
           }

       }
        return R.drawable.lauta1011;
    }

    // Converts hangboard image resource into describing name. This should be somewhere else.
    public static String getHangboardName(int HBresource) {

        switch (HBresource) {
            case R.drawable.lauta1011:
                return "BM 1000";
            case R.drawable.lauta2002:
                return "BM 2000";
            case R.drawable.trans:
                return "Transgression";
            case R.drawable.tension:
                return "Tension";
            case R.drawable.zlag:
                return "Zlagboard";
            case R.drawable.moonhard:
                return "Moonboard hard";
            case R.drawable.mooneasy:
                return "Moonboard easy";
            case R.drawable.meto:
                return "Metolius";
            case R.drawable.rockprodigy:
                return "Rock Prodigy";
            case R.drawable.problemsolver:
                return "Problemsolver";
            case R.drawable.meto_contact:
                return "Meto. Contact";
            case R.drawable.meto_wood:
                return "Meto. Wood";
            case R.drawable.drcc:
                return "DRCC";
            default:
                return "Custom";
        }
    }


    public static int getImageResource(int position) {
        return image_resources[position];
    }

    public HangboardSwipeAdapter(Context ctx) {
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

        imageView.setImageResource(image_resources[position]);

        container.addView(item_view);

        return item_view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout)object);
    }


}
