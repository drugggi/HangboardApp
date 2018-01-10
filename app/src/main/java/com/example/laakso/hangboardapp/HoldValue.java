package com.example.laakso.hangboardapp;

/**
 * Created by Laakso on 20.11.2017.
 */

// HoldValue contains the information that a single hang can hold
public class HoldValue {
    private int hold_number;
    private int hold_value;
    public enum grip_type {FOUR_FINGER, THREE_FRONT, THREE_BACK, TWO_FRONT, TWO_MIDDLE, TWO_BACK
        ,MIDDLE_FINGER, INDEX_FINGER, RING_FINGER, PINKY_FINGER};
    grip_type grip_style;

    public HoldValue(int number) {

        hold_number = number;
    }
    public int GetHoldNumber() {
        return hold_number;
    }

    public void SetGripType(grip_type fingers) {
        grip_style = fingers;
    }

    public void SetHoldNumber(int number) {
        hold_number = number;
    }

    public void SetHoldValue(int value) {
        hold_value = value;
    }

    public int GetHoldValue() {
        return hold_value;
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
