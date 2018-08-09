package com.finn.laakso.hangboardapp;

/**
 * Created by Laakso on 12.1.2018.
 */

// Class TimeControls tries to hide a lot of time control parameters that are confusing to follow
public class TimeControls {
    // In future I will refactor TimeControls class to keep track with current lap
    // private int current_lap=0;

    private int grip_laps=6;
    private int hang_laps=6;
    private int hang_laps_seconds=60;
    private int time_on = 7;
    private int time_off= 3;
    private int routine_laps=3;
    // private int time_total = time_on + time_off;
    private int rest = 150;
    private int long_rest = 360;
    private boolean isRepeaters = true;


    public TimeControls() {
        this.grip_laps = 6;
        this.hang_laps = 6;

        this.time_on = 7;
        this.time_off = 3;
        this.routine_laps = 3;
        /// time_total = time_on + time_off;
        this.rest = 150;
        this.long_rest = 360;
        this.hang_laps_seconds = hang_laps * (time_on + time_off);

    }
    public boolean isRepeaters() {
        return isRepeaters;
    }
    public void setToRepeaters(boolean setTo) {
        isRepeaters = setTo;
    }

    // Not working very well setToRepeaters is good enough
    public void changeTimeToSingleHangs() {
        isRepeaters = false;
    }

    // getGripMatrix method generates a readable String which represents the hang program and
    // includes grips, rest and long rest
    // Refactor using StingBuilder in the future for good practice
    public String getGripMatrix(boolean showTimeInfo) {
        String matrix;

        // if showTimeInfo is true lets print grips and rests in seconds
        if (showTimeInfo) {
            String grip_time= "[ "+ hang_laps_seconds+"s ]";
            String grips = "[ "+ hang_laps_seconds+"s ]";

            // Lets generate a single set string
            for (int i=2; i<=grip_laps; i++) {
                grips = grips + " " + rest + "s ";
                grips = grips + "[ "+ hang_laps_seconds+"s ]";
            }
            // no paste that single set string as many times as there are sets
            matrix = "1. SET:  " + grips;
            for (int i=2; i<=routine_laps; i++) {
                matrix = matrix + "  LONG REST( " + long_rest + "s )\n";
                matrix = matrix + i + ". SET:  " + grips;
            }
            matrix = matrix + "  workout ends\n";
            matrix = matrix + "Time under tension: " + getTimeUnderTension() + "s ";
            matrix = matrix + " Workout time: " + getTotalTime() + "s ";

        }
        // if showTimeInfo is false lets print grip 1, rest, grip 2, rest etc...
        else {
        String grips ="[grip 1]";
        for (int i=2; i <= grip_laps; i++) {
            grips = grips + " rest ";
            grips = grips + "[grip " + i + "]";
        }

        matrix = "1. SET:  " + grips;
        for (int i=2; i <= routine_laps; i++) {
            matrix = matrix + "  LONG REST( " + long_rest + "s )\n";
            matrix = matrix + i + ". SET:  " + grips;
        }
        matrix = matrix + "  workout ends";
        }
        return matrix;
    }

    // Time under thesion is the time that user actually hangs on those grips
    public int getTimeUnderTension() {
        return routine_laps*grip_laps*hang_laps*time_on;
    }

    public void changeTimeToRepeaters() {
        isRepeaters = true;
        this.grip_laps = 6;
        this.hang_laps = 6;
        this.time_on = 7;
        this.time_off = 3;
        this.routine_laps = 3;
        this.rest = 150;
        this.long_rest = 360;
        this.hang_laps_seconds = hang_laps * (time_on + time_off);
    }

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

    // PITÄÄ VARMAAN MUUTTAA MYÖS HANGLAPS SECONDSIT JOS MUUTTAA TÄTÄ
    public void setHangLaps(int hang_laps) {

        if (hang_laps > 0 && hang_laps <= 20) {
            this.hang_laps = hang_laps;
        }
        else {
            this.hang_laps = 6;
        }
        hang_laps_seconds = hang_laps * (time_on + time_off);

        if (this.hang_laps == 1 ) {
            isRepeaters = false;
        }
        else {
            isRepeaters = true;
        }
    }
    public int getHangLaps() {
        return hang_laps;
    }

    public void setTimeON(int time_on) {
        if ( time_on > 0 && time_off <= 60 ) {
            this.time_on = time_on;
        }
        else {
            time_on = 7;
        }
        hang_laps_seconds = hang_laps * (time_on + time_off);
    }
    public int getTimeON() {
        return time_on;
    }

    public void setTimeOFF(int time_off) {
        if ( time_off >= 0 && time_off <= 200 ) {
            this.time_off = time_off;
        }
        else {
            this.time_off = 3;
        }
        hang_laps_seconds = hang_laps * (time_on + time_off);
    }
    public int getTimeOFF() {
        return time_off;
    }

    public void setRoutineLaps(int routine_laps) {
        if (routine_laps > 0 && routine_laps <= 50) {
            this.routine_laps = routine_laps;
        }
        else {
            this.routine_laps = 3;
        }

    }
    public int getRoutineLaps() {
        return  routine_laps;
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


    // Pre made workoutprograms, hopefully in future these will be replaced by community standards
    public void setProgramBasedOnTime(int workout_time) {
        if (isRepeaters) {
            if (workout_time < 25) {
                setTimeControls(new int[]{5, 5, 7, 3, 2, 70, 180}); // 20min program
            } else if (workout_time < 40) {
                setTimeControls(new int[]{6, 5, 7, 3, 2, 120, 240}); // 35min program
            } else if (workout_time < 55) {
                setTimeControls(new int[]{5, 6, 7, 3, 3, 140, 300}); // 50min program
            } else if (workout_time < 70) {
                setTimeControls(new int[]{6, 6, 7, 3, 3, 150, 360}); // default program
            } else {
                setTimeControls(new int[]{15, 6, 7, 3, 1, 150, 150}); // TEST grade program
            }
        }
        else {
            if (workout_time < 25) {
                setTimeControls(new int[]{12, 1, 10, 0, 2, 40, 90}); // 20min program
            } else if (workout_time < 40) {
                setTimeControls(new int[]{19, 1, 10, 0, 2, 45, 120}); // 35min program
            } else if (workout_time < 55) {
                setTimeControls(new int[]{16, 1, 10, 0, 3, 50, 120}); // 50min program
            } else if (workout_time < 70) {
                setTimeControls(new int[]{21, 1, 10, 0, 3, 50, 150}); // default program
            } else {
                setTimeControls(new int[]{15, 1, 10, 0, 1, 50, 50}); // TEST grade program
            }

        }

    }

    public void setPremadeTimeControls(int progressBarPosition) {
        if (isRepeaters) {
            if (progressBarPosition == 0) {
                setTimeControls(new int[] {6, 6, 7, 3, 1, 50, 1}); // 10min OK
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
        return "Grip laps: " + grip_laps + " Hang laps: " + hang_laps + " Time on/off: " + time_on + "/"
                + time_off + " Sets: " + routine_laps + " rest/long rest: " + rest + "/" + long_rest;
    }

    public int getTotalTime() {
        // 0 represents workout starts in time
        // total_s = workout_starts_in + (hang_laps*grip_laps+(grip_laps - 1)*rest) * routine_laps  + (routine_laps - 1)*long_rest;
        return 0 + (hang_laps_seconds*grip_laps+(grip_laps - 1)*rest) * routine_laps  + (routine_laps - 1)*long_rest;
    }

    public void setTimeControls(int[] time_controls) {
        if( time_controls.length != 7 ) {return; }

        setGripLaps(time_controls[0]);
        setHangLaps(time_controls[1]);
        setTimeON(time_controls[2]);
        setTimeOFF(time_controls[3]);
        setRoutineLaps(time_controls[4]);
        setRestTime(time_controls[5]);
        setLongRestTime(time_controls[6]);
        /*
        hang_laps = time_controls[1];
        time_on = time_controls[2];
        time_off = time_controls[3];
        // time_total = time_on + time_off;
        routine_laps = time_controls[4];
        rest = time_controls[5];
        long_rest = time_controls[6];
        hang_laps_seconds = hang_laps*(time_on + time_off);*/
    }

    public int[] getTimeControlsIntArray() {
        int[] timecontrols = {grip_laps, hang_laps,  time_on, time_off, routine_laps , rest, long_rest};
        return timecontrols;
    }


}
