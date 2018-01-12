package com.example.laakso.hangboardapp;

/**
 * Created by Laakso on 12.1.2018.
 */

public class TimeControls {

    private int grip_laps=6;
    private int hang_laps=6;
    private int hang_laps_seconds=60;
    private int routine_laps=3;
    private int time_on = 7;
    private int time_off= 3;
    // private int time_total = time_on + time_off;
    private int rest = 150;
    private int long_rest = 600;


    public void TimeControls() {
        grip_laps = 6;
        hang_laps = 6;
        routine_laps = 3;
        time_on = 7;
        time_off = 3;
        /// time_total = time_on + time_off;
        rest = 150;
        long_rest = 600;
        hang_laps_seconds = hang_laps * (time_on + time_off);

    }



    public void setGripLaps(int grip_laps) {
        this.grip_laps = grip_laps;
    }
    public int getGripLaps() {
        return grip_laps;
    }

    public void setHangLaps(int hang_laps) {
        this.hang_laps = hang_laps;
    }
    public int getHangLaps() {
        return hang_laps;
    }

    public void setTimeON(int time_on) {
        this.time_on = time_on;
    }
    public int getTimeON() {
        return time_on;
    }

    public void setTimeOFF(int time_off) {
        this.time_off = time_off;
    }
    public int getTimeOFF() {
        return time_off;
    }

    public void setRoutineLaps(int routine_laps) {
        this.routine_laps = routine_laps;
    }
    public int getRoutineLaps() {
        return  routine_laps;
    }

    public void setRestTime(int rest) {
        this.rest = rest;
    }
    public int getRestTime() {
        return rest;
    }

    public void setLongRestTime(int long_rest) {
        this.long_rest = long_rest;
    }
    public int getLongRestTime() {
        return long_rest;
    }

    public int getTimeONandOFF() {
        return time_on + time_off;
    }

    public int getHangLapsSeconds() {
        return hang_laps*(time_on + time_off);
    }

   // time_controls = new int[] {6, 6, 7 ,3 , 3, 150, 600};

    public int getTotalTime() {
        // 0 represents workout starts in time
        return 0 + (hang_laps_seconds*grip_laps+(grip_laps - 1)*rest) * routine_laps  + (routine_laps - 1)*long_rest;
    }

    public void setTimeControls(int[] time_controls) {
        if( time_controls.length != 7 ) {return; }

        grip_laps = time_controls[0];
        hang_laps = time_controls[1];
        time_on = time_controls[2];
        time_off = time_controls[3];
        // time_total = time_on + time_off;
        routine_laps = time_controls[4];
        rest = time_controls[5];
        long_rest = time_controls[6];
    }

    public int[] getTimeControlsIntArray() {
        int[] timecontrols = {grip_laps, hang_laps,  time_on, time_off, routine_laps , rest, long_rest};
        return timecontrols;
    }


}
