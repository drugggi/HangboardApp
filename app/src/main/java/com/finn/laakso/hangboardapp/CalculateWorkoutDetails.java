package com.finn.laakso.hangboardapp;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Laakso on 27.7.2018.
 */

public class CalculateWorkoutDetails {
    private int adjustedWorkoutTime;
    private int adjustedTUT;
    private int totalHangs;
    private int completedHangs;
    private int successfulHangRate;
    private int holdDifficultiesSum;
    private int unusedWorkoutTime;
    private float averageDifficutly;
    private float intensity;
    private float workload;
    private float difficultyPerMinute;
    private float power;



    public CalculateWorkoutDetails(TimeControls timeControls, ArrayList<Hold> workoutHolds, int[] completed) {

        adjustedWorkoutTime = 0;
        adjustedTUT = 0;
        totalHangs = timeControls.getHangLaps()*timeControls.getGripLaps()*timeControls.getRoutineLaps();
        completedHangs = 0;
        holdDifficultiesSum = 0;

        // Average difficulty for every grip lap
        int holdDifficulties[] = new int[timeControls.getGripLaps()];
        for (int i = 0 ; i < holdDifficulties.length ; i++) {
            holdDifficulties[i] = (workoutHolds.get(2*i).getHoldValue() + workoutHolds.get(2*i+1).getHoldValue() )/ 2;
        }

        // With the help of completed matrix we can calculate real time under tension, how many hangs
        // were completed and hold difficulties sum.
        for (int i = 0 ; i < completed.length ; i++) {
            adjustedTUT += completed[i] * timeControls.getTimeON();
            completedHangs += completed[i];
            holdDifficultiesSum += completed[i] * holdDifficulties[ i % timeControls.getGripLaps() ];
        }

        // We have to erase the time at the end if workout was stopped before end of workout
        // We can know and do this if the completed matrix has zeros at the end

        unusedWorkoutTime = 0;
        // from end to start
        for (int i = completed.length -1; i >= 0 && completed[i] == 0 ; i--) {

            // If completed matrix is zero matrix i.e. not a single successful hangs were done
            // we dont erase rest time
            // This is not common in actual use, but in test cases, really common situation
            if (i == 0) {
                unusedWorkoutTime += timeControls.getHangLaps()*( timeControls.getTimeON() + timeControls.getTimeOFF() );
                break;
            }

            // If whole set was zeros lets erase long rest time else only rest time
            if (i % timeControls.getGripLaps() == 0 ) {
                unusedWorkoutTime += timeControls.getLongRestTime() +
                        timeControls.getHangLaps()*( timeControls.getTimeON() + timeControls.getTimeOFF() );
            }
            else {
                unusedWorkoutTime += timeControls.getRestTime() +
                        timeControls.getHangLaps()*( timeControls.getTimeON() + timeControls.getTimeOFF() );
            }

        }


        adjustedWorkoutTime = timeControls.getTotalTime() - unusedWorkoutTime;
        successfulHangRate = 100 * completedHangs / totalHangs;

        if (adjustedWorkoutTime != 0) {
            intensity = (float) adjustedTUT / adjustedWorkoutTime;
        } else {
            intensity = -1;
        }

        if (completedHangs != 0) {
            averageDifficutly = (float) holdDifficultiesSum / completedHangs;
        } else {
            averageDifficutly = 0;
        }


        workload = averageDifficutly * adjustedTUT;


        if (adjustedWorkoutTime != 0) {
            //difficultyPerMinute = averageDifficutly / adjustedTUT;
            //difficultyPerMinute = averageDifficutly * adjustedTUT / adjustedWorkoutTime;
            //difficultyPerMinute = averageDifficutly;
            difficultyPerMinute = averageDifficutly / completedHangs;
            float test1 = averageDifficutly * 60 / adjustedTUT;
            float test2 = averageDifficutly * completedHangs * timeControls.getTimeON() / 60;
            float test3 = averageDifficutly * adjustedTUT / 60;

            Log.e("testit",": " + test1 + " , " + test2 + " , " + test3  );
            difficultyPerMinute = 0;
        } else {
            difficultyPerMinute = 0;
        }

        if (adjustedWorkoutTime != 0 ) {
            power = averageDifficutly * adjustedTUT / adjustedWorkoutTime;
        } else {
            power = 0;
        }


    }

    public int getAdjustedWorkoutTime() {
        return this.adjustedWorkoutTime;
    }
    public int getAdjustedTUT() {
        return this.adjustedTUT;
    }
    public int getTotalHangs() {
        return this.totalHangs;
    }
    public int getCompletedHangs() {
        return this.completedHangs;
    }
    public int getSuccessfulHangRate() {
        return this.successfulHangRate;
    }
    public int getDifficultiesSum() {
        return this.holdDifficultiesSum;
    }
    public int getUnusedWorkoutTime() {
        return this.unusedWorkoutTime;
    }
    public float getIntensity() {
        return this.intensity;
    }
    public float getAverageDifficutly() {
        return this.averageDifficutly;
    }
    public float getWorkload() {
        return this.workload;
    }
    public float getDifficultyPerMinute() {
        return this.difficultyPerMinute;
    }
    public float getWorkoutPower() {
        return this.power;
    }


}
