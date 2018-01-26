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
        ,MIDDLE_FINGER, INDEX_FINGER, RING_FINGER, PINKY_FINGER};
    grip_type grip_style;

    // Single holds dont have a pair with same measurements in the hangboard
    boolean single_hold;

    public HoldValue(int number) {
        single_hold = false;
        hold_number = number;
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
