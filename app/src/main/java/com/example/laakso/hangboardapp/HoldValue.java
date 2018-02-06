package com.example.laakso.hangboardapp;

/**
 * Created by Laakso on 20.11.2017.
 */

// HoldValue contains the information that a single hang can hold
public class HoldValue {
    // Hold number corresponds with the number in hangboard picture
    private int hold_number;

    // hold value tries to measure the difficulty to hang in each hold number with the different grip types
    private int hold_value;

    // grip type describes the fingers used in hanging in a hold
    public enum grip_type {FOUR_FINGER, THREE_FRONT, THREE_BACK, TWO_FRONT, TWO_MIDDLE, TWO_BACK
        , INDEX_FINGER,MIDDLE_FINGER, RING_FINGER, PINKY_FINGER};
    grip_type grip_style;

    private int[] finger_images = {R.drawable.fourfingerleft, R.drawable.fourfingerright, R.drawable.threefrontleft
    , R.drawable.fourfingerright, R.drawable.threebackleft, R.drawable.threebackright, R.drawable.twofrontleft
            ,R.drawable.twofrontright, R.drawable.twomiddleleft , R.drawable.twomiddleright, R.drawable.twobackleft,
            R.drawable.twobackright, R.drawable.indexleft, R.drawable.indexright, R.drawable.middleleft,
    R.drawable.middleright, R.drawable.ringleft, R.drawable.ringright, R.drawable. pinkyleft, R.drawable.pinkyright};

    private int finger_image;

    // Single holds dont have a pair with same measurements in the hangboard
    boolean single_hold;

    public HoldValue(int number) {
        single_hold = false;
        hold_number = number;
    }

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
          else if (grip_style == grip_type.PINKY_FINGER) {return R.drawable.pinkyleft;}

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
          else if (grip_style == grip_type.PINKY_FINGER) {return R.drawable.pinkyright;}

      }
      return R.drawable.fourfingerright;
    }

    public void SetGripTypeAndSingleHang(int i_hold_both_info) {
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
        else if ( i_hold_both_info == 10) {grip_style = grip_type.PINKY_FINGER; }

        else  {grip_style = grip_type.FOUR_FINGER; }

    }
    public int GetHoldNumber() {
        return hold_number;
    }

    public void SetGripType(grip_type fingers) {
        grip_style = fingers;
    }


    public int getHoldNumber() {

        return hold_number;
    }


    public void SetHoldValue(int value) {
        hold_value = value;
    }


    public int GetHoldValue() {
        return hold_value;
    }


    public void setAsSingleHold(boolean isit) {
        single_hold = isit;
    }


    public boolean isSingleHold() {
        return single_hold;
    }


    // Returns the text that represents the grip type
    public String GetHoldText() {
        if (grip_style == grip_type.FOUR_FINGER) { return "four fingers";}
        if (grip_style == grip_type.THREE_FRONT) { return "three front";}
        if (grip_style == grip_type.THREE_BACK) { return "three back";}
        if (grip_style == grip_type.TWO_FRONT) { return "two front";}
        if (grip_style == grip_type.TWO_MIDDLE) { return "two middle";}
        if (grip_style == grip_type.TWO_BACK) { return "two back";}
        if (grip_style == grip_type.MIDDLE_FINGER) { return "middle finger";}
        if (grip_style == grip_type.INDEX_FINGER) { return "index finger";}
        if (grip_style == grip_type.RING_FINGER) { return "ring finger";}
        if (grip_style == grip_type.PINKY_FINGER) { return "pinky finger";}
        return "";
    }

}
