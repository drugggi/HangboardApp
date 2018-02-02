package com.example.laakso.hangboardapp;

/**
 * Created by Laakso on 12.1.2018.
 */

// Class TimeControls tries to hide a lot of time control parameters that are kinda confusing to follow
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
    private boolean isRepeaters = true;


    public void TimeControls() {
        this.grip_laps = 6;
        this.hang_laps = 6;
        this.routine_laps = 3;
        this.time_on = 7;
        this.time_off = 3;
        /// time_total = time_on + time_off;
        this.rest = 150;
        this.long_rest = 600;
        this.hang_laps_seconds = hang_laps * (time_on + time_off);

    }

    // Not working very well
    public void changeTimeToSingleHangs() {
        isRepeaters = false;
        int total_time = getTotalTime();
        grip_laps = total_time / 60;
        hang_laps = 1;
        hang_laps_seconds = 10;
        routine_laps = 1;
        time_on = 10;
        time_off = 1;
        rest = 50;

    }

    public void changeTimeToRepeaters() {
        isRepeaters = true;
        this.grip_laps = 6;
        this.hang_laps = 6;
        this.routine_laps = 3;
        this.time_on = 7;
        this.time_off = 3;
        this.rest = 150;
        this.long_rest = 600;
        this.hang_laps_seconds = hang_laps * (time_on + time_off);
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

    public void setProgramBasedOnTime(int workout_time) {
        if (workout_time < 25) {
            setTimeControls(new int[] {5, 5, 7 ,3 , 2, 70, 180}); // 20min program
        }
        else if (workout_time < 40) {
            setTimeControls(new int[] {6, 5, 7 ,3 , 2, 120, 240}); // 35min program
        }
        else if (workout_time < 55) {
            setTimeControls(new int[] {5, 6, 7 ,3 , 3, 140, 300}); // 50min program
        }
        else if (workout_time < 70) {
            setTimeControls(new int[] {6, 6, 7 ,3 , 3, 150, 360}); // default program
        }
        else {
            setTimeControls(new int[] {7, 6, 7 ,3 , 3, 160, 420}); // 80min program
        }
    }


    public int getTotalTime() {
        // 0 represents workout starts in time
        // total_s = workout_starts_in + (hang_laps*grip_laps+(grip_laps - 1)*rest) * routine_laps  + (routine_laps - 1)*long_rest;
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
        hang_laps_seconds = hang_laps*(time_on + time_off);
    }

    public int[] getTimeControlsIntArray() {
        int[] timecontrols = {grip_laps, hang_laps,  time_on, time_off, routine_laps , rest, long_rest};
        return timecontrols;
    }


}