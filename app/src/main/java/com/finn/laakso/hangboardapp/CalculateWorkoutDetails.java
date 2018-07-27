package com.finn.laakso.hangboardapp;

import java.util.ArrayList;

/**
 * Created by Laakso on 27.7.2018.
 */

public class CalculateWorkoutDetails {
    private int adjustedWorkoutTime;
    private int adjustedTUT;
    private int completedSum;
    private int holdDifficultiesSum;



    public CalculateWorkoutDetails(TimeControls timeControls, ArrayList<Hold> workoutHolds, int[] completed) {

        this.adjustedWorkoutTime = 0;
        this.adjustedTUT = 0;
        this.completedSum = 0;
        this.holdDifficultiesSum = 0;

        int holdDifficulties[] = new int[timeControls.getGripLaps()];
        for (int i = 0 ; i < holdDifficulties.length ; i++) {
            holdDifficulties[i] = (workoutHolds.get(2*i).getHoldValue() + workoutHolds.get(2*i+1).getHoldValue() )/ 2;
        }

        for (int i = 0 ; i < completed.length ; i++) {
            adjustedTUT += completed[i] * timeControls.getTimeON();
            completedSum += completed[i];
            holdDifficultiesSum += completed[i] * holdDifficulties[ i % timeControls.getGripLaps() ];
        }

    }

    public int getAdjustedWorkoutTime() {
        return this.adjustedWorkoutTime;
    }
    public int getAdjustedTUT() {
        return this.adjustedTUT;
    }
    public int getCompletedSum() {
        return this.completedSum;
    }
    public int getDifficultiesSum() {
        return this.holdDifficultiesSum;
    }


}
