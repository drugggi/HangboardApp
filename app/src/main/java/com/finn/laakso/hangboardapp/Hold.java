package com.finn.laakso.hangboardapp;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

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

    // grip type describes the fingers used in hanging in a hold
    public enum grip_type {FOUR_FINGER, THREE_FRONT, THREE_BACK, TWO_FRONT, TWO_MIDDLE, TWO_BACK
        , INDEX_FINGER,MIDDLE_FINGER, RING_FINGER, LITTLE_FINGER}
    grip_type grip_style;
    public static grip_type forInt(int id)
    {
        if (id > 0 && id <= grip_type.values().length ) {
            return grip_type.values()[id - 1];
        }
        return grip_type.FOUR_FINGER;
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

        boolean[] booleanArr = new boolean[1];
        in.readBooleanArray(booleanArr);
        single_hold = booleanArr[0];
        grip_style = grip_type.values()[in.readInt()];

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(hold_number);
        dest.writeInt(hold_value);
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
          if (grip_style == grip_type.FOUR_FINGER) {return R.drawable.animation_left_4_to_3b_01;}
          else if (grip_style == grip_type.THREE_FRONT) {return R.drawable.animation_left_4_to_3f_10;}
          else if (grip_style == grip_type.THREE_BACK) {return R.drawable.animation_left_4_to_3b_10;}
          else if (grip_style == grip_type.TWO_FRONT) {return R.drawable.animation_left_4_to_2f_10;}
          else if (grip_style == grip_type.TWO_MIDDLE) {return R.drawable.animation_left_4_to_2m_10;}
          else if (grip_style == grip_type.TWO_BACK) {return R.drawable.animation_left_4_to_2b_10;}
          else if (grip_style == grip_type.INDEX_FINGER) {return R.drawable.indexleft;}
          else if (grip_style == grip_type.MIDDLE_FINGER) {return R.drawable.middleleft;}
          else if (grip_style == grip_type.RING_FINGER) {return R.drawable.ringleft;}
          else if (grip_style == grip_type.LITTLE_FINGER) {return R.drawable.pinkyleft;}

      }
        else {
          if (grip_style == grip_type.FOUR_FINGER) {return R.drawable.animation_right_4_to_3b_01;}
          else if (grip_style == grip_type.THREE_FRONT) {return R.drawable.animation_right_4_to_3f_10;}
          else if (grip_style == grip_type.THREE_BACK) {return R.drawable.animation_right_4_to_3b_10;}
          else if (grip_style == grip_type.TWO_FRONT) {return R.drawable.animation_right_4_to_2f_10;}
          else if (grip_style == grip_type.TWO_MIDDLE) {return R.drawable.animation_right_4_to_2m_10;}
          else if (grip_style == grip_type.TWO_BACK) {return R.drawable.animation_right_4_to_2b_10;}
          else if (grip_style == grip_type.INDEX_FINGER) {return R.drawable.indexright;}
          else if (grip_style == grip_type.MIDDLE_FINGER) {return R.drawable.middleright;}
          else if (grip_style == grip_type.RING_FINGER) {return R.drawable.ringright;}
          else if (grip_style == grip_type.LITTLE_FINGER) {return R.drawable.pinkyright;}

      }
      return R.drawable.fourfingerright;
    }

    // XY, X is the grip type and Y is 1 if it is single hold and 0 if not
    public void setGripTypeAndSingleHold(int i_hold_both_info) {
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
