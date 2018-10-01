package com.finn.laakso.hangboardapp;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Laakso on 20.11.2017.
 * Hold class represents a hangboard hold that one hand can grip. It has hangboards hold number,
 * hold value that is how difficult it is to hang there, it has grip type and coordinates to hangboard
 * so that it can be visually showed to user
 */

// Hold contains the information that a single hang can hold
public class Hold implements Comparable<Hold>, Parcelable {
    // Hold number corresponds with the number in hangboard picture
    private int hold_number;

    // hold value tries to measure the difficulty to hang in each hold number with the different grip types
    private int hold_value;

    // hold coordinates are based on hold number, left or right hand and currently used hangboard.
/*
    private int lefthand_coord_x;
    private int lefthand_coord_y;
    private int righthand_coord_x;
    private int righthand_coord_y;
*/

    // grip type describes the fingers used in hanging in a hold
    public enum grip_type {FOUR_FINGER, THREE_FRONT, THREE_BACK, TWO_FRONT, TWO_MIDDLE, TWO_BACK
        , INDEX_FINGER,MIDDLE_FINGER, RING_FINGER, LITTLE_FINGER}
    grip_type grip_style;
    public static grip_type forInt(int id) {
        return grip_type.values()[id-1];
    }

    // Single holds dont have a pair with same measurements in the hangboard
    private boolean single_hold;

    public boolean isEqual(Hold compareHold) {
        return (this.hold_number == compareHold.getHoldNumber() && this.grip_style == compareHold.getGripStyle());
    }

    public Hold(int number) {
        single_hold = false;
        hold_number = number;
    }
    // Holds are sent as extra to WorkoutIntent so Parcel them
    private Hold(Parcel in) {
        hold_number = in.readInt();
        hold_value = in.readInt();
 /*       lefthand_coord_x = in.readInt();
        lefthand_coord_y = in.readInt();
        righthand_coord_x = in.readInt();
        righthand_coord_y = in.readInt();
 */
        boolean[] booleanArr = new boolean[1];
        in.readBooleanArray(booleanArr);
        single_hold = booleanArr[0];
        grip_style = grip_type.values()[in.readInt()];
        // single_hold = in.readBooleanArray();

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(hold_number);
        dest.writeInt(hold_value);
        /*dest.writeInt(lefthand_coord_x);
        dest.writeInt(lefthand_coord_y);
        dest.writeInt(righthand_coord_x);
        dest.writeInt(righthand_coord_y);
        */
        dest.writeBooleanArray(new boolean[] {single_hold});
        dest.writeInt(grip_style.ordinal());

    }

    public static final Parcelable.Creator<Hold> CREATOR = new Parcelable.Creator<Hold>() {
        public Hold createFromParcel(Parcel in) {
            return new Hold(in);
        }

        public Hold[] newArray(int size) {
            return new Hold[size];
        }
    };

    // Hold Class implements comparable so that holds can be sorted by their value ie. how
    // difficult it is to hang in a give hold with given grip_type
    public int compareTo(Hold compareHold) {
            return this.hold_value - compareHold.hold_value;
    }
/*

    public int getLeftCoordX() {
        return lefthand_coord_x;
    }
    public int getLeftCoordY() {
        return lefthand_coord_y;
    }
    public int getRightCoordX() {
        return righthand_coord_x;
    }
    public int getRightCoordY() {return righthand_coord_y;}

    public void setHoldCoordinates(int[] coordinates) {

        int coordinateRow = hold_number - 1;


        // Security check, it is possible that user manually changes hangboard so that there is no
        // hold number/ hold coordinates in that hangboard
        if (coordinateRow*5 + 4 < coordinates.length) {
            lefthand_coord_x = coordinates[coordinateRow * 5 + 1];
            lefthand_coord_y = coordinates[coordinateRow * 5 + 2];
            righthand_coord_x = coordinates[coordinateRow * 5 + 3];
            righthand_coord_y = coordinates[coordinateRow * 5 + 4];
        }
        else {
            lefthand_coord_x = 0;
            lefthand_coord_y = 0;
            righthand_coord_x = 0;
            righthand_coord_y = 0;
        }


    }
*/

    // Get hold info Takes two holds left(this) and right hand hold and makes String information
    // out of those: Grip type is alternate and hold value is average
    public String getHoldInfo(Hold rightHandHold) {
        String hold_value_text;

        if (hold_number == rightHandHold.getHoldNumber()) {
            if ( hold_value == 0) { hold_value_text = "Custom"; }
            else { hold_value_text = "" +hold_value; }

            return "HOLD: " + hold_number + "\nGRIP: " + getHoldText() + "\nDifficulty: " + hold_value_text;
        }

        else {
            if ( hold_value == 0 || rightHandHold.getHoldValue() == 0) { hold_value_text = "Custom"; }
            else { hold_value_text = "" + (hold_value + rightHandHold.getHoldValue())/2; }

            return "HOLD: " + hold_number + "/" + rightHandHold.getHoldNumber() + "\nGRIP: " + getHoldText()
                    + " Alternate\nDifficulty: " + hold_value_text;
        }
    }

    // Returns the image that corresponds the grip_type
    public int getGripImage(boolean left_hand) {

      if (left_hand) {
          if (grip_style == grip_type.FOUR_FINGER) {return R.drawable.fourfingerleft;}
          else if (grip_style == grip_type.THREE_FRONT) {return R.drawable.threefrontleft;}
          else if (grip_style == grip_type.THREE_BACK) {return R.drawable.threebackleft;}
          else if (grip_style == grip_type.TWO_FRONT) {return R.drawable.twofrontleft;}
          else if (grip_style == grip_type.TWO_MIDDLE) {return R.drawable.twomiddleleft;}
          else if (grip_style == grip_type.TWO_BACK) {return R.drawable.twobackleft;}
          else if (grip_style == grip_type.INDEX_FINGER) {return R.drawable.indexleft;}
          else if (grip_style == grip_type.MIDDLE_FINGER) {return R.drawable.middleleft;}
          else if (grip_style == grip_type.RING_FINGER) {return R.drawable.ringleft;}
          else if (grip_style == grip_type.LITTLE_FINGER) {return R.drawable.pinkyleft;}

      }
        else {
          if (grip_style == grip_type.FOUR_FINGER) {return R.drawable.fourfingerright;}
          else if (grip_style == grip_type.THREE_FRONT) {return R.drawable.threefrontright;}
          else if (grip_style == grip_type.THREE_BACK) {return R.drawable.threebackright;}
          else if (grip_style == grip_type.TWO_FRONT) {return R.drawable.twofrontright;}
          else if (grip_style == grip_type.TWO_MIDDLE) {return R.drawable.twomiddleright;}
          else if (grip_style == grip_type.TWO_BACK) {return R.drawable.twobackright;}
          else if (grip_style == grip_type.INDEX_FINGER) {return R.drawable.indexright;}
          else if (grip_style == grip_type.MIDDLE_FINGER) {return R.drawable.middleright;}
          else if (grip_style == grip_type.RING_FINGER) {return R.drawable.ringright;}
          else if (grip_style == grip_type.LITTLE_FINGER) {return R.drawable.pinkyright;}

      }
      return R.drawable.fourfingerright;
    }

    // XY, X is the grip type and Y is 1 if it is single hold and 0 if not
    public void setGripTypeAndSingleHang(int i_hold_both_info) {
        if (i_hold_both_info % 10 == 1) {single_hold = true; }

        i_hold_both_info = i_hold_both_info / 10;

        if (i_hold_both_info == 1) {grip_style = grip_type.FOUR_FINGER; }
        else if ( i_hold_both_info == 2) {grip_style = grip_type.THREE_FRONT; }
        else if ( i_hold_both_info == 3) {grip_style = grip_type.THREE_BACK; }

        else if ( i_hold_both_info == 4) {grip_style = grip_type.TWO_FRONT; }
        else if ( i_hold_both_info == 5) {grip_style = grip_type.TWO_MIDDLE; }
        else if ( i_hold_both_info == 6) {grip_style = grip_type.TWO_BACK; }

        else if ( i_hold_both_info == 7) {grip_style = grip_type.INDEX_FINGER; }
        else if ( i_hold_both_info == 8) {grip_style = grip_type.MIDDLE_FINGER; }
        else if ( i_hold_both_info == 9) {grip_style = grip_type.RING_FINGER; }
        else if ( i_hold_both_info == 10) {grip_style = grip_type.LITTLE_FINGER; }

        else  {grip_style = grip_type.FOUR_FINGER; }

    }
    public int getHoldNumber() {
        return hold_number;
    }

    public void SetGripType(grip_type fingers) {
        grip_style = fingers;
    }


    public void setHoldValue(int value) {
        hold_value = value;
    }


    public int getHoldValue() {
        return hold_value;
    }

    public boolean isSingleHold() {
        return single_hold;
    }

    public void setGripType(int grip_type) {

        grip_style = forInt(grip_type);
    }
    public void setGripStyle(grip_type newgrip) {
        grip_style = newgrip;
    }

    public grip_type getGripStyle() {
        return grip_style;
    }
    public int getGripStyleInt() {return grip_style.ordinal()+1;}


    // Returns the text that represents the grip type
    private String getHoldText() {
        if (grip_style == grip_type.FOUR_FINGER) { return "four fingers";}
        if (grip_style == grip_type.THREE_FRONT) { return "three front";}
        if (grip_style == grip_type.THREE_BACK) { return "three back";}
        if (grip_style == grip_type.TWO_FRONT) { return "two front";}
        if (grip_style == grip_type.TWO_MIDDLE) { return "two middle";}
        if (grip_style == grip_type.TWO_BACK) { return "two back";}
        if (grip_style == grip_type.MIDDLE_FINGER) { return "middle finger";}
        if (grip_style == grip_type.INDEX_FINGER) { return "index finger";}
        if (grip_style == grip_type.RING_FINGER) { return "ring finger";}
        if (grip_style == grip_type.LITTLE_FINGER) { return "little finger";}
        return "";
    }

}
