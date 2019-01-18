package com.finn.laakso.hangboardapp;

/**
 * Created by Laakso on 12.1.2018.
 * Class TimeControls tries to hide a lot of time control parameters that are confusing to follow
 */

public class TimeControls {

    private int grip_laps;
    private int hang_laps;
    private int hang_laps_seconds;
    private int time_on ;
    private int time_off;
    private int sets;
    private int rest;
    private int long_rest ;
    private boolean isRepeaters = true;


    public TimeControls() {
        this.grip_laps = 6;
        this.hang_laps = 6;

        this.time_on = 7;
        this.time_off = 3;
        this.sets = 3;
        this.rest = 150;
        this.long_rest = 360;
        this.hang_laps_seconds = hang_laps * (time_on + time_off);

    }
    public boolean isRepeaters() {
        return isRepeaters;
    }

    // getGripMatrix method generates a readable String which represents the hang program and
    // includes grips, rest and long rest
    // Refactor using StingBuilder in the future for good practice
    public String getGripMatrix(boolean showTimeInfo) {
        String matrix;

        // if showTimeInfo is true lets print grips and rests in seconds
        if (showTimeInfo) {
            // String grip_time= "[ "+ hang_laps_seconds+"s ]";
            String grips = "[ "+ hang_laps_seconds+"s ]";

            // Lets generate a single set string
            for (int i=2; i<=grip_laps; i++) {
                grips = grips + " " + rest + "s ";
                grips = grips + "[ "+ hang_laps_seconds+"s ]";
            }
            // no paste that single set string as many times as there are sets
            matrix = "1. SET:  " + grips;
            for (int i=2; i<=sets; i++) {
                matrix = matrix + "  LONG REST( " + long_rest + "s )\n";
                matrix = matrix + i + ". SET:  " + grips;
            }
            matrix = matrix + "  workout ends\n";
            matrix = matrix + "Time under tension: " + getTimeUnderTension() + "s/" + getTimeUnderTension()/60+"min ";
            matrix = matrix + " Workout time: " + getTotalTime() + "s/" + getTotalTime()/60 +"min ";

        }
        // if showTimeInfo is false lets print grip 1, rest, grip 2, rest etc...
        else {
        String grips ="[grip 1]";
        for (int i=2; i <= grip_laps; i++) {
            grips = grips + " rest ";
            grips = grips + "[grip " + i + "]";
        }

        matrix = "1. SET:  " + grips;
        for (int i=2; i <= sets; i++) {
            matrix = matrix + "  LONG REST( " + long_rest + "s )\n";
            matrix = matrix + i + ". SET:  " + grips;
        }
        matrix = matrix + "  workout ends";
        }
        return matrix;
    }

    // Time under thesion is the time that user actually hangs on those grips
    public int getTimeUnderTension() {
        return sets*grip_laps*hang_laps*time_on;
    }
/*

    public void changeTimeToRepeaters() {
        isRepeaters = true;
        this.grip_laps = 6;
        this.hang_laps = 6;
        this.time_on = 7;
        this.time_off = 3;
        this.sets = 3;
        this.rest = 150;
        this.long_rest = 360;
        this.hang_laps_seconds = hang_laps * (time_on + time_off);
    }
*/

    public void setGripLaps(int grip_laps) {
        if (grip_laps > 0 && grip_laps <= 100) {
            this.grip_laps = grip_laps;
        }
        else {
            this.grip_laps = 6;
        }
    }
    public int getGripLaps() {
        return grip_laps;
    }

    public void setHangLaps(int hang_laps) {

        if (hang_laps > 0 && hang_laps <= 20) {
            this.hang_laps = hang_laps;
        }
        else {
            this.hang_laps = 6;
        }

        if (this.hang_laps == 1 ) {
        // There is no OFF time in single hangs
            this.time_off = 0;
            isRepeaters = false;
        }
        else {
            isRepeaters = true;
        }
        hang_laps_seconds = this.hang_laps * (time_on + time_off);

    }
    public int getHangLaps() {
        return hang_laps;
    }

    public void setTimeON(int time_on) {
        if ( time_on > 0 && time_off <= 60 ) {
            this.time_on = time_on;
        }
        else {
            this.time_on = 7;
        }
        hang_laps_seconds = hang_laps * (this.time_on + time_off);
    }
    public int getTimeON() {
        return time_on;
    }

    public void setTimeOFF(int time_off) {
        if (this.hang_laps == 1) {
            this.time_off = 0;
            return;
        }
        if ( time_off >= 0 && time_off <= 200 ) {
            this.time_off = time_off;
        }
        else {
            this.time_off = 3;
        }
        hang_laps_seconds = hang_laps * (time_on + this.time_off);
    }
    public int getTimeOFF() {
        return time_off;
    }

    public void setRoutineLaps(int sets) {
        if (sets > 0 && sets <= 50) {
            this.sets = sets;
        }
        else {
            this.sets = 3;
        }

    }
    public int getRoutineLaps() {
        return  sets;
    }

    public void setRestTime(int rest) {
        if (rest > 0 && rest <= 500) {
            this.rest = rest;
        }
        else {
            this.rest = 150;
        }
    }
    public int getRestTime() {
        return rest;
    }

    public void setLongRestTime(int long_rest) {
        if (long_rest > 0 && long_rest <= 1000) {
            this.long_rest = long_rest;
        }
        else {
            this.long_rest = 360;
        }
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


    // Premade time controls that are found decent via trial and error
    public void setPremadeTimeControls(int progressBarPosition) {
        if (isRepeaters) {
            if (progressBarPosition == 0) {
                setTimeControls(new int[] {6, 6, 7, 3, 1, 60, 1}); // 10min OK
            } else if (progressBarPosition == 1) {
                setTimeControls(new int[] {4, 5, 7, 3, 2, 60, 140}); // 15min OK
            } else if (progressBarPosition == 2) {
                setTimeControls(new int[] {5, 5, 7, 3, 2, 70, 180}); // 20min OK
            } else if (progressBarPosition == 3) {
                setTimeControls(new int[]{5, 6, 7, 3, 2, 100, 300});  // 30min OK
            } else if (progressBarPosition == 4) {
                setTimeControls(new int[] {4, 6, 7, 3, 3, 120, 270}); // 40min OK
            } else if (progressBarPosition == 5) {
                setTimeControls(new int[]{5, 6, 7, 3, 3, 140, 300}); // 50min OK
            } else if (progressBarPosition == 6) {
                setTimeControls(new int[]{6, 6, 7, 3, 3, 150, 360}); // 65min OK
            } else {
                setTimeControls(new int[]{15, 6, 7, 3, 1, 150, 150}); // TEST grade program
            }

        }
        else {
            if (progressBarPosition == 0) {
                setTimeControls(new int[]{16, 1, 10, 0, 1, 30, 1}); // 10min OK
            } else if (progressBarPosition == 1) {
                setTimeControls(new int[]{10, 1, 10, 0, 2, 35, 70}); // 15min OK
            } else if (progressBarPosition == 2) {
                setTimeControls(new int[]{12, 1, 10, 0, 2, 40, 90}); // 20min OK
            } else if (progressBarPosition == 3) {
                setTimeControls(new int[]{17, 1, 10, 0, 2, 45, 120}); // 30min OK
            } else if (progressBarPosition == 4) {
                setTimeControls(new int[]{14, 1, 10, 0, 3, 45, 120}); // 40min OK
            } else if (progressBarPosition == 5) {
                setTimeControls(new int[]{16, 1, 10, 0, 3, 50, 120}); // 50min OK
            } else if (progressBarPosition == 6) {
                setTimeControls(new int[]{21, 1, 10, 0, 3, 50, 150}); // 65min OK
            } else {
                setTimeControls(new int[]{15, 1, 10, 0, 1, 50, 50}); // TEST grade program
            }

        }
    }

    public String getTimeControlsAsString() {
        return "Grips: " + grip_laps + " Hangs: " + hang_laps + " Time on/off: " + time_on + "/"
                + time_off + " Sets: " + sets + " rest/long rest: " + rest + "/" + long_rest;
    }

    public String getTimeControlsAsJSONGString() {
        return grip_laps + "," + hang_laps + "," + time_on + "," + time_off + "," + sets
                + "," + rest + "," + long_rest;
    }

    public void setTimeControlsFromString(String JSONString) {

        String[] parcedTimeControls = JSONString.split(",");

        int[] timeControlValues = new int[parcedTimeControls.length];

        for (int i = 0; i < parcedTimeControls.length; i++) {
            timeControlValues[i] = Integer.parseInt(parcedTimeControls[i]);
        }

        setTimeControls(timeControlValues );

    }

    public int getTotalTime() {
        return (hang_laps_seconds*grip_laps+(grip_laps - 1)*rest) * sets  + (sets - 1)*long_rest;
    }

    public void setTimeControls(int[] time_controls) {
        if( time_controls.length != 7 ) {
            setGripLaps(6);
            setHangLaps(6);
            setTimeON(7);
            setTimeOFF(3);
            setRoutineLaps(3);
            setRestTime(150);
            setLongRestTime(360);
            return;
        }

        setGripLaps(time_controls[0]);
        setHangLaps(time_controls[1]);
        setTimeON(time_controls[2]);
        setTimeOFF(time_controls[3]);
        setRoutineLaps(time_controls[4]);
        setRestTime(time_controls[5]);
        setLongRestTime(time_controls[6]);

    }

    public int[] getTimeControlsIntArray() {
        return new int[] {grip_laps, hang_laps,  time_on, time_off, sets , rest, long_rest};
    }


}
