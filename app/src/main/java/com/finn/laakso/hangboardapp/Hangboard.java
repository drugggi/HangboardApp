package com.finn.laakso.hangboardapp;

import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**
 * Created by Laakso on 16.11.2017.
 * Hangboard represents the hangboard that user is currently using. In a hangboard there are holds
 * that have different coodinates, difficulty levels and possible grip types. WorkoutHoldList consist
 * of these holds and are picked by user either manually or pseudo randomly based on grades.
 */

public class Hangboard {

    private String[] grades;
    private HangboardResources.hangboardName currentHangboard;
    private int[] hold_coordinates;

    // All possible grip types in a hangboard
    private Hold[] allHangboardHolds;

    // workoutHoldList represents the holds that are in a current workout shown in holdsListView
    // These will be sent to WorkoutActivity. Even values has the left hand information and odd values  right hand information
    private ArrayList<Hold> workoutHoldList;

    // Grips constructor takes resources so that it can read all the information needed constructing
    // workout and hangs and grips
    public Hangboard(HangboardResources.hangboardName HB) {

        grades = HangboardResources.grades;
        workoutHoldList = new ArrayList<Hold>();

        initializeHolds(HB);
    }

    // Hold class knows the image resources for hand images
    public int getLeftFingerImage(int position) {
        if (position < 0 || position >= workoutHoldList.size() / 2) {
            return workoutHoldList.get(0).getGripImage(true);
        }
        return workoutHoldList.get(position*2).getGripImage(true);
    }
    public int getRightFingerImage(int position) {
        if (position < 0 || position >= workoutHoldList.size() / 2) {
            return workoutHoldList.get(0).getGripImage(false);
        }
        return workoutHoldList.get(position*2+1).getGripImage(false);
    }


    // Coordinates getters for hand image positioning, it is possible to request hold number that
    // does not exist in hold_coordinates
    public int getCoordLefthandX(int position) {
        if (position < 0 || position >= workoutHoldList.size() / 2 ) {
            return 0;
        }
        int holdNumber = workoutHoldList.get(position*2).getHoldNumber();

        if (holdNumber*5 <= hold_coordinates.length) {
            return hold_coordinates[(holdNumber - 1) * 5 + 1];
        }
        return 0;
    }
    public int getCoordLefthandY(int position) {
        if (position < 0 || position >= workoutHoldList.size() / 2 ) {
            return 0;
        }
        int holdNumber = workoutHoldList.get(position*2).getHoldNumber();

        if (holdNumber*5 <= hold_coordinates.length) {
            return hold_coordinates[(holdNumber - 1) * 5 + 2];
        }
        return 0;
    }
    public int getCoordRighthandX(int position) {
        if (position < 0 || position >= workoutHoldList.size() / 2 ) {
            return 0;
        }
        int holdNumber = workoutHoldList.get(position*2+1).getHoldNumber();

        if (holdNumber*5 <= hold_coordinates.length) {
            return hold_coordinates[(holdNumber - 1) * 5 + 3];
        }
        return 0;
    }
    public int getCoordRighthandY(int position) {
        if (position < 0 || position >= workoutHoldList.size() / 2 ) {
            return 0;
        }
        int holdNumber = workoutHoldList.get(position*2+1).getHoldNumber();

        if (holdNumber*5 <= hold_coordinates.length) {
            return hold_coordinates[(holdNumber - 1) * 5 + 4];
        }
        return 0;
    }



    public String getGrade(int position) {
        return grades[position];
    }

    public String getHangboardName() {
        return HangboardResources.getHangboardStringName(currentHangboard);
    }


    public ArrayList<Hold> getCurrentWorkoutHoldList() {
        return workoutHoldList;
    }


    public int getCurrentHoldListSize() {
        return workoutHoldList.size();
    }


    public void setNewWorkoutHolds(ArrayList<Hold> newList) {

        if (newList.size() != 0) {
            workoutHoldList = newList;

        }

    }


// For every hold there must be four corresponding coordinates so
    public int getMaxHoldNumber() {
        return hold_coordinates.length / 5;

    }


    // Creates a custom hold and tries to search if the holds is in stored list, if not
    // especially the hold value is impossible to set, and is put to custom = 0
    private Hold createCustomHold(int holdnumber, Hold.grip_type grip_style) {
        Hold customHold = new Hold(holdnumber);
        customHold.setGripStyle(grip_style);

        for (int i = 0; i < allHangboardHolds.length; i++) {
            if (allHangboardHolds[i].isEqual(customHold)) {
                return  allHangboardHolds[i];
            }
        }
        // customHold.setHoldCoordinates(hold_coordinates);
        return customHold;

    }

    // addCustomHold method manipulates Hold information at selected position. If info is more than twice
    // the size of maximun hold number at a given hangboard, then the user selected different grip type.
    // We also have to do dirty copying
    public void addCustomHold(int info, int position) {
        int max = getMaxHoldNumber();
        Hold customHold;

        // User selected a different hold number for hand
        if (info < 2*max) {
            int holdnumber = (info+2)/2;

            customHold = createCustomHold(holdnumber,workoutHoldList.get(position*2).getGripStyle());

            //Left hand
            if (info % 2 == 0) {
                workoutHoldList.set(position*2, customHold);
            }
            // right hand
            else {
                workoutHoldList.set(position*2+1, customHold);
            }

        }
        // User selected different grip type, lets change the grip type
        else {
            info = info - 2*max+1;
            Hold.grip_type newgriptype = Hold.forInt(info);

            // Lets change it for both left (even value) and right (odd value) hand
            customHold = createCustomHold(workoutHoldList.get(position*2).getHoldNumber(), newgriptype);
            workoutHoldList.set(position*2,customHold);

            customHold = createCustomHold(workoutHoldList.get(position*2+1).getHoldNumber(), newgriptype);
            workoutHoldList.set(position*2+1,customHold);
        }

    }

    // This is useful when user wants to go from easiest hold to hardest progressively.
    public void sortHoldByDifficulty() {
        Arrays.sort(allHangboardHolds);

        workoutHoldList.clear();

        int i = 0;

        while (i < allHangboardHolds.length ) {
            // Skip the holds that are just one of in hangboard
            if (allHangboardHolds[i].isSingleHold()) {
                i++;
                continue;
            }

            workoutHoldList.add(allHangboardHolds[i]);
            workoutHoldList.add(allHangboardHolds[i]);

            i++;
        }

    }

    // Sets the workoutHoldList to given amount and randomizes those holds
    public void setGripAmount(TimeControls timeControls, int grade_position, SharedPreferences prefs) {

        int amount = timeControls.getGripLaps();
        // No need to change if the size is the same than wanthed size (amount)
        if (amount*2 < workoutHoldList.size() ) {
            while (amount*2 < workoutHoldList.size() ) {
                workoutHoldList.remove(workoutHoldList.size()-1 );
                workoutHoldList.remove(workoutHoldList.size()-1 );
            }

        }
        else if (amount*2 > workoutHoldList.size() ) {
            while (amount*2 > workoutHoldList.size() ){
                workoutHoldList.add(new Hold(1));
                workoutHoldList.add(new Hold(1));
                if (grade_position != 0 ) {
                    randomizeNewWorkoutHold(grade_position, workoutHoldList.size() / 2 - 1, timeControls);
                } else {
                    newCustomWorkoutHold(prefs,workoutHoldList.size() / 2 -1);
                }
                // randomizeGrip(grade_position, workoutHoldList.size()/2-1 );

            }

        }

        // Lets randomize the holds in allHangboardHolds
        randomizeHoldList();

    }

    // RandomizeHoldList method is necessary to guarantee that holds are random for every
    // button press. Otherwise it would pick some holds more frequently
    private void randomizeHoldList() {
        Hold temp;
        int index;
        Random random = new Random();
        // why not guarantee index = 0 with i >= 0, does not matter tho
        for (int i = allHangboardHolds.length - 1; i > 0; i--)
        {
            index = random.nextInt(i + 1);
            temp = allHangboardHolds[index];
            allHangboardHolds[index] = allHangboardHolds[i];
            allHangboardHolds[i] = temp;
        }
    }

    // Left and right hand should have same grip type, thats why only lefthand method is usually used
    public Hold.grip_type getLeftHandGripType(int position) {

        if (position < 0 || position*2 +1  >= workoutHoldList.size() )
        {return Hold.grip_type.MIDDLE_FINGER; }
        return workoutHoldList.get(position*2).getGripStyle();
    }
    public Hold.grip_type getRightHandGripType(int position) {
        if (position < 0 || position*2 +1  >= workoutHoldList.size() )
        {return Hold.grip_type.MIDDLE_FINGER; }
        return workoutHoldList.get(position*2+1).getGripStyle();
    }

    // setHoldsForSingleHangs method makes sure that if one hang contains different holds for left
    // and right hand, then the next hang will be the opposite. In Repeaters this is not necessary
    // because the hands alternates repeatedly as time goes on.
    public void setHoldsForSingleHangs() {
        int i = 0;
        while ( i < workoutHoldList.size() - 2 ) {
            if (workoutHoldList.get(i).getHoldNumber() != workoutHoldList.get(i+1).getHoldNumber() ) {
                workoutHoldList.set(i+2, workoutHoldList.get(i+1));
                workoutHoldList.set(i+3, workoutHoldList.get(i));
                i = i + 4;
            }
            else {
                i = i + 2; }
        }
    }

    // RandomizeGrip method randomizes selected grip instead of all the grips
/*
    public void randomizeGrip(int grade_position, int hold_nro) {

        if (hold_nro < 0 || hold_nro >= workoutHoldList.size() ) {
            return;
        }
        // Random generator that is only used if we are using the same hold or alternating between holds
        Random rn = new Random();
        boolean isAlternate = rn.nextBoolean();

        // these ints will be randomized and those represents holds in allHangboardHolds array
        int random_nro;
        int random_nro_alt;
        int temp_hold_value;

        // Min and max values of grades which the hold search is based on
        int min_value=getMinValue(grades[grade_position]);
        int max_value=getMaxValue(grades[grade_position]);

        if (isAlternate) {

            random_nro = getHoldNumberWithValue(min_value/2, (max_value*3)/2 );
            temp_hold_value = allHangboardHolds[random_nro].getHoldValue();

            min_value = 2*min_value-temp_hold_value;
            if (min_value < 1 ) { min_value = 1; }
            max_value = 2*max_value-temp_hold_value;
            if (max_value < 2 ) { max_value = 2; }


                random_nro_alt = getHoldNumberWithGripType( min_value, max_value, allHangboardHolds[random_nro].grip_style);

                // Holds should not be the same, if it is lets make sure next if statement is true
            // and replace the hold
                if (random_nro == random_nro_alt) { isAlternate = false; }

            workoutHoldList.set(hold_nro*2, allHangboardHolds[random_nro]);
            workoutHoldList.set(hold_nro*2 + 1,allHangboardHolds[random_nro_alt]);

            }

        if ( !isAlternate ) {
            // Lets search for a hold that max hardness is half the remaining points for a give grade
            random_nro = getHoldNumberWithValue(min_value, max_value);

            workoutHoldList.set(hold_nro*2, allHangboardHolds[random_nro]);
            workoutHoldList.set(hold_nro*2 + 1,allHangboardHolds[random_nro]);

            }
            randomizeHoldList();
    }

*/
    // We use this method when we are randomizing grips, we actually don't want 100% random grips,
    // but pseudo random so that each grip type withing a grade is represented at least once.
 /*    private ArrayList<Hold.grip_type> getGripTypesWithinGrade(int min_value, int max_value) {
        ArrayList<Hold.grip_type> differentGripTypes = new ArrayList<>();

        int holdValue;
        for (int i = 0 ; i < allHangboardHolds.length ; i++ ) {
            // Lets ignore one finger grip types
            if (allHangboardHolds[i].getGripStyleInt() >= 7) {
                continue;
            }

            holdValue =  allHangboardHolds[i].getHoldValue();

            if (holdValue >= min_value && holdValue <= max_value ) {

                Hold.grip_type newGripType = allHangboardHolds[i].getGripStyle();

                boolean isUnique = true;
                for (int j = 0 ; j < differentGripTypes.size() ; j++) {
                    if (differentGripTypes.get(j) == newGripType) {
                        isUnique = false;
                    }
                }
                if (isUnique) {
                    differentGripTypes.add(allHangboardHolds[i].getGripStyle());
                }
            }

        }

        return differentGripTypes;
    }
*/
    // getScaledHoldValue scales the base grade values depending how hard timeControls are
 /*   private static int getScaledHoldValue(int value,TimeControls timeControls) {
        int TUT = timeControls.getTimeUnderTension();
        int WT = timeControls.getTotalTime();
        float intensity = (float) TUT / WT;

        // 300 and 0.25 are just some base values found with trial and error
        int workload = value * 500;
        float power = value * 0.20f;

        float newValueBasedOnWorkload = (float) workload / TUT;
        float newValueBasedOnPower = power / intensity;

        int newValue = (int) ((newValueBasedOnWorkload + newValueBasedOnPower) / 2);

        if (newValue - value > 50) {newValue = value*value*value; }

        if (newValue < 1) {return 1; }
        else if (newValue > 500) {return 500; }
        else {return newValue; }

    }
*/
    public void newCustomWorkoutHold(SharedPreferences prefs, int position) {
        int minDiff = prefs.getInt("minDifficultyFilter",FilterActivity.DEFAULT_MIN_DIFFICULTY);
        int maxDiff = prefs.getInt("maxDifficultyFilter", FilterActivity.DEFAULT_MAX_DIFFICULTY);
        int altFactor = prefs.getInt("alternateFactorFilter", FilterActivity.DEFAULT_ALTERNATE_FACTOR);
        Boolean[] gripTypes = new Boolean[FilterActivity.DEFAULT_GRIPTYPES_ALLOWED.length];
        for (int i = 0 ; i < gripTypes.length ; i++) {
            gripTypes[i] = prefs.getBoolean("gripType_"+i+"_Filter",FilterActivity.DEFAULT_GRIPTYPES_ALLOWED[i]);
        }
        ArrayList<Hold> holdsInRange = getHoldsInRange(minDiff,maxDiff,gripTypes);
        ArrayList<Hold> altHoldsInRange = getAlternateHoldsInRange(minDiff,maxDiff,altFactor,gripTypes);
        if (holdsInRange.size() == 0 && altHoldsInRange.size() == 0) {return; }
        else if (holdsInRange.size() == 0) {holdsInRange = altHoldsInRange; }
        else if (altHoldsInRange.size() == 0) {altHoldsInRange = holdsInRange; }

        Random rng = new Random();
        int random_nro;

        if (rng.nextBoolean() ) {
            // small trickery, random_nro must be even ,
            random_nro = rng.nextInt(holdsInRange.size()/2 )*2;
            workoutHoldList.set(position*2, holdsInRange.get(random_nro) );
            workoutHoldList.set(position*2 + 1,holdsInRange.get(random_nro+1) );
        }
        else {
            random_nro = rng.nextInt(altHoldsInRange.size()/2 )*2;
            workoutHoldList.set(position*2, altHoldsInRange.get(random_nro) );
            workoutHoldList.set(position*2 +1, altHoldsInRange.get(random_nro+1) );
        }
    }
    public void newCustomWorkoutHolds(SharedPreferences prefs) {
        int minDiff = prefs.getInt("minDifficultyFilter",FilterActivity.DEFAULT_MIN_DIFFICULTY);
        int maxDiff = prefs.getInt("maxDifficultyFilter", FilterActivity.DEFAULT_MAX_DIFFICULTY);
        int altFactor = prefs.getInt("alternateFactorFilter", FilterActivity.DEFAULT_ALTERNATE_FACTOR);
        Boolean[] gripTypes = new Boolean[FilterActivity.DEFAULT_GRIPTYPES_ALLOWED.length];
        for (int i = 0 ; i < gripTypes.length ; i++) {
            gripTypes[i] = prefs.getBoolean("gripType_"+i+"_Filter",FilterActivity.DEFAULT_GRIPTYPES_ALLOWED[i]);
        }
        boolean useEveryGripType = prefs.getBoolean("fillGripTypesFilter",FilterActivity.DEFAULT_USE_EVERY_GRIP);
        boolean sorting = prefs.getBoolean("sortWorkoutHoldsFilter", FilterActivity.DEFAULT_SORT_HOLDS);
        int sortOrder = prefs.getInt("sortOrderFilter",FilterActivity.DEFAULT_SORT_ORDER);
        int sortMethod = prefs.getInt("sortMethodFilter", FilterActivity.DEFAULT_SORT_METHOD);

        ArrayList<Hold> holdsInRange = getHoldsInRange(minDiff,maxDiff,gripTypes);
        ArrayList<Hold> altHoldsInRange = getAlternateHoldsInRange(minDiff,maxDiff,altFactor,gripTypes);
        if (holdsInRange.size() == 0 && altHoldsInRange.size() == 0) {return; }
        else if (holdsInRange.size() == 0) {holdsInRange = altHoldsInRange; }
        else if (altHoldsInRange.size() == 0) {altHoldsInRange = holdsInRange; }

        Random rng = new Random();
        int totalHolds = workoutHoldList.size() /2;
        if (totalHolds == 0) {totalHolds = 6; }
        workoutHoldList.clear();

        // make sure that every grip type is in workoutholds if userEveryGripType is set
        if (useEveryGripType) {
            for (int i = 0; i < holdsInRange.size(); i++) {
                boolean alreadyIn = false;
                for (int j = 0; j < workoutHoldList.size(); j++) {
                    if (holdsInRange.get(i).getGripStyle() == workoutHoldList.get(j).getGripStyle()) {
                        alreadyIn = true;
                        break;
                    }
                }
                if (alreadyIn == false) {
                    workoutHoldList.add(holdsInRange.get(i));
                    workoutHoldList.add(holdsInRange.get(i + 1));
                }
            }
            for (int i = 0; i < altHoldsInRange.size(); i++) {
                boolean alreadyIn = false;
                for (int j = 0; j < workoutHoldList.size(); j++) {
                    if (altHoldsInRange.get(i).getGripStyle() == workoutHoldList.get(j).getGripStyle()) {
                        alreadyIn = true;
                        break;
                    }
                }
                if (alreadyIn == false) {
                    workoutHoldList.add(altHoldsInRange.get(i));
                    workoutHoldList.add(altHoldsInRange.get(i + 1));
                }
            }
            while (workoutHoldList.size() / 2 > totalHolds) {
                workoutHoldList.remove(workoutHoldList.size()-1);
                workoutHoldList.remove(workoutHoldList.size()-1);
            }
        }

        // fill workoutholdlist with random holds at random place that meets the filter requirements
        for (int i = workoutHoldList.size()/2 ; i < totalHolds ; i++) {
            int rngPlace = 0;
            if (workoutHoldList.size() != 0) {
                rngPlace = rng.nextInt(workoutHoldList.size() / 2) * 2;
            }
            if (rng.nextBoolean() ) {
                int nro = rng.nextInt(holdsInRange.size()/2) * 2;
                workoutHoldList.add(rngPlace, holdsInRange.get(nro));
                workoutHoldList.add(rngPlace, holdsInRange.get(nro+1));
            }
            else {
               int nro = rng.nextInt(altHoldsInRange.size() / 2) * 2;
               workoutHoldList.add(rngPlace, altHoldsInRange.get(nro) );
               workoutHoldList.add(rngPlace, altHoldsInRange.get(nro+1));
            }
        }
        if (sorting) {
            sortWorkoutHoldList(sortOrder, sortMethod);
        }
        randomizeHoldList();
    }

    public void sortWorkoutHoldList(int sortOrder, int sortMethod) {

        // nothing to sort if only one hang in the list (two holds)
        if (workoutHoldList.size() < 4) {return; }
        ArrayList<Hold> sortedWorkoutHolds = new ArrayList<>();
        sortedWorkoutHolds.add(workoutHoldList.get(0) );
        sortedWorkoutHolds.add(workoutHoldList.get(1) );

       for (int i = 2 ; i < workoutHoldList.size() ; i = i+2) {
           for (int j = 0 ; j < sortedWorkoutHolds.size() ; j= j+2) {
               int value1 = 0;
               int value2 = 0;
               if (sortMethod == FilterActivity.DIFFICULTY) {
                   value1 = workoutHoldList.get(i).getHoldValue() + workoutHoldList.get(i + 1).getHoldValue();
                   value2 = sortedWorkoutHolds.get(j).getHoldValue() + sortedWorkoutHolds.get(j + 1).getHoldValue();
               }
               else if (sortMethod == FilterActivity.GRIPTYPE) {
                   value1 = workoutHoldList.get(i).getGripStyleInt();
                   value2 = sortedWorkoutHolds.get(j).getGripStyleInt();
               }
               else if (sortMethod == FilterActivity.HOLDNUMBER) {
                   value1 = 100*workoutHoldList.get(i).getHoldNumber()+workoutHoldList.get(i+1).getHoldNumber();
                   value2 = 100*sortedWorkoutHolds.get(j).getHoldNumber()+sortedWorkoutHolds.get(j+1).getHoldNumber();
               }

               if (sortOrder == FilterActivity.ASCENDING) {
                   if (value1 <= value2) {
                       sortedWorkoutHolds.add(j, workoutHoldList.get(i + 1));
                       sortedWorkoutHolds.add(j, workoutHoldList.get(i));
                       break;
                   }
               }
               if (sortOrder == FilterActivity.DESCENDING) {
                   if (value1 >= value2) {
                       sortedWorkoutHolds.add(j, workoutHoldList.get(i + 1 ));
                       sortedWorkoutHolds.add(j, workoutHoldList.get(i) );
                       break;
                   }
               }
               if (j+2 >= sortedWorkoutHolds.size() ) {
                   sortedWorkoutHolds.add(workoutHoldList.get(i+1));
                   sortedWorkoutHolds.add(workoutHoldList.get(i));
                   break;
               }

           }

       }
        workoutHoldList.clear();
       workoutHoldList.addAll(sortedWorkoutHolds);
    }

    // Same as randomizeNewWorkoutHolds but just for one selected hang (hangPosition)
    public void randomizeNewWorkoutHold(int grade_positino,int hangPosition, final TimeControls timeControls) {
        Random rng = new Random();
        int minDiff = HangboardResources.getMinDifficulty(grade_positino,timeControls);
        int maxDiff = HangboardResources.getMaxDifficulty(grade_positino,timeControls);
        Boolean[] gripTypes = new Boolean[] {true, true, true, true, true, true, true, true, true,true};
        int altFactor = rng.nextInt(4);

        ArrayList<Hold> holdsInRange = getHoldsInRange(minDiff,maxDiff,gripTypes);
        ArrayList<Hold> altHoldsInRange = getAlternateHoldsInRange(minDiff,maxDiff,altFactor,gripTypes);
        int forceBreak = 0;
        while (holdsInRange.size() == 0 && altHoldsInRange.size() == 0) {
            if (forceBreak > 5) {return; }

            minDiff = minDiff/2;
            maxDiff = maxDiff*2;
            holdsInRange = getHoldsInRange(minDiff,maxDiff,gripTypes);
            altHoldsInRange = getAlternateHoldsInRange(minDiff,maxDiff,altFactor,gripTypes);
            forceBreak++;
            }
        if (holdsInRange.size() == 0) {holdsInRange = altHoldsInRange; }
        else if (altHoldsInRange.size() == 0) {altHoldsInRange = holdsInRange; }

        int random_nro;
        if (rng.nextBoolean() ) {

            random_nro = rng.nextInt(holdsInRange.size()/2 );
            workoutHoldList.set(hangPosition*2, holdsInRange.get(random_nro*2) );
            workoutHoldList.set(hangPosition*2 + 1,holdsInRange.get(random_nro*2+1) );
        }
        else {
            random_nro = rng.nextInt(altHoldsInRange.size()/2 );
            workoutHoldList.set(hangPosition*2, altHoldsInRange.get(random_nro*2) );
            workoutHoldList.set(hangPosition*2 +1, altHoldsInRange.get(random_nro*2+1) );
        }

    }
    // randomizeNewWorkoutHolds randomizes whole workout hold list base on selected grade
    // and what workout length and time under tensions are, if they are relatively small hold
    // difficulties will be a little higher
    public void randomizeNewWorkoutHolds(int grade_position, final TimeControls timeControls) {

        Random rng = new Random();
        float tut = (float)timeControls.getTimeUnderTension()/(float)timeControls.getTotalTime();
        int minDiff = HangboardResources.getMinDifficulty(grade_position,timeControls);
        int maxDiff = HangboardResources.getMaxDifficulty(grade_position,timeControls);
        Boolean[] gripTypes = new Boolean[] {true, true, true, true, true, true, true, true, true,true};
        int altFactor = rng.nextInt(4);

        ArrayList<Hold> holdsInRange = getHoldsInRange(minDiff,maxDiff,gripTypes);
        ArrayList<Hold> altHoldsInRange = getAlternateHoldsInRange(minDiff,maxDiff,altFactor,gripTypes);
        int forceBreak = 0;
        while (holdsInRange.size() == 0 && altHoldsInRange.size() == 0) {
            if (forceBreak > 5) {return; }

            minDiff = minDiff/2;
            maxDiff = maxDiff*2;
            holdsInRange = getHoldsInRange(minDiff,maxDiff,gripTypes);
            altHoldsInRange = getAlternateHoldsInRange(minDiff,maxDiff,altFactor,gripTypes);
            forceBreak++;
        }
        if (holdsInRange.size() == 0) {holdsInRange = altHoldsInRange; }
        else if (altHoldsInRange.size() == 0) {altHoldsInRange = holdsInRange; }

        int totalHolds = workoutHoldList.size() / 2;
        if (totalHolds == 0) {totalHolds = 6; }
        workoutHoldList.clear();

        ArrayList<Hold> fourfinger = new ArrayList<>();

        ArrayList<Hold> threefront = new ArrayList<>();
        ArrayList<Hold> threeback = new ArrayList<>();
        ArrayList<Hold> othergrips = new ArrayList<>();
        for (int i = 0 ; i < holdsInRange.size() ; i = i+2 ) {
            if (holdsInRange.get(i).getGripStyle() == Hold.grip_type.FOUR_FINGER) {
                fourfinger.add(holdsInRange.get(i) );
                fourfinger.add(holdsInRange.get(i+1) );
            }
            else if (holdsInRange.get(i).getGripStyle() == Hold.grip_type.THREE_FRONT) {
                threefront.add(holdsInRange.get(i) );
                threefront.add(holdsInRange.get(i+1) );
            }
            else if (holdsInRange.get(i).getGripStyle() == Hold.grip_type.THREE_BACK) {
                threeback.add(holdsInRange.get(i) );
                threeback.add(holdsInRange.get(i+1) );
            }
            else {
                othergrips.add(holdsInRange.get(i) );
                othergrips.add(holdsInRange.get(i+1) );
            }
        }
        for (int i = 0 ; i < altHoldsInRange.size() ; i = i+2 ) {
            if (altHoldsInRange.get(i).getGripStyle() == Hold.grip_type.FOUR_FINGER) {
                fourfinger.add(altHoldsInRange.get(i) );
                fourfinger.add(altHoldsInRange.get(i+1) );
            }
            else if (altHoldsInRange.get(i).getGripStyle() == Hold.grip_type.THREE_FRONT) {
                threefront.add(altHoldsInRange.get(i) );
                threefront.add(altHoldsInRange.get(i+1) );
            }
            else if (altHoldsInRange.get(i).getGripStyle() == Hold.grip_type.THREE_BACK) {
                threeback.add(altHoldsInRange.get(i) );
                threeback.add(altHoldsInRange.get(i+1) );
            }
            else {
                othergrips.add(altHoldsInRange.get(i) );
                othergrips.add(altHoldsInRange.get(i+1) );
            }
        }

        for (int i = 0; i < totalHolds ; i++) {
            int holdType = rng.nextInt(4);
            int holdPlace = 0;
            switch(holdType) {
                case (0):
                    if (fourfinger.size() != 0) {
                        holdPlace = rng.nextInt(fourfinger.size()/2)*2;
                        workoutHoldList.add(fourfinger.get(holdPlace) );
                        workoutHoldList.add(fourfinger.get(holdPlace+1) );
                        break;
                    }
                case (1):
                    if (threefront.size() != 0) {
                        holdPlace = rng.nextInt(threefront.size()/2)*2;
                        workoutHoldList.add(threefront.get(holdPlace) );
                        workoutHoldList.add(threefront.get(holdPlace+1) );
                        break;
                    }
                case (2):
                    if (threeback.size() != 0) {
                        holdPlace = rng.nextInt(threeback.size()/2)*2;
                        workoutHoldList.add(threeback.get(holdPlace) );
                        workoutHoldList.add(threeback.get(holdPlace+1) );
                        break;
                    }
                case (3):
                    if (othergrips.size() != 0) {
                        holdPlace = rng.nextInt(othergrips.size()/2)*2;
                        workoutHoldList.add(othergrips.get(holdPlace) );
                        workoutHoldList.add(othergrips.get(holdPlace+1) );
                        break;
                    }
                default:
                    if (holdsInRange.size() != 0) {
                        holdPlace = rng.nextInt(holdsInRange.size() / 2) *2;
                        workoutHoldList.add(holdsInRange.get(holdPlace) );
                        workoutHoldList.add(holdsInRange.get(holdPlace+1) );
                    }
                    else if (altHoldsInRange.size() != 0) {
                        holdPlace = rng.nextInt(altHoldsInRange.size() / 2) *2;
                        workoutHoldList.add(altHoldsInRange.get(holdPlace) );
                        workoutHoldList.add(altHoldsInRange.get(holdPlace+1) );
                    }
                    else {
                        workoutHoldList.add(allHangboardHolds[0]);
                        workoutHoldList.add(allHangboardHolds[0]);
                    }
            }
        }
        randomizeHoldList();

    }

/*

    public void randomizenewworkoutholds2(int grade_position,final timecontrols timecontrols) {
        int holdsamount = workoutholdlist.size()/2;
        if (holdsamount == 0) {holdsamount = 6; }

        workoutholdlist.clear();
        // random generator that is only used if we are using the same hold or alternating between holds
        random rng = new random();
        boolean isalternate = rng.nextboolean();

        // these ints will be randomized and those represents holds in allhangboardholds array
        int random_nro;
        int random_nro_alt;
        int temp_hold_value;

        int min_value=getscaledholdvalue(getminvalue(grades[grade_position]),timecontrols);
        int max_value=getscaledholdvalue(getmaxvalue(grades[grade_position]),timecontrols);

        int i=0;

        // lets see how many different grip types we find within given grade range
        arraylist<hold.grip_type> wantedgriptypes = getgriptypeswithingrade(min_value, max_value);

        hold.grip_type randomgriptype;
        // lets prefer four finger grip by setting one extra at random place
        if (wantedgriptypes.size() != 0) {
            random_nro = rng.nextint(wantedgriptypes.size());
            randomgriptype = wantedgriptypes.get(random_nro);
            wantedgriptypes.set(random_nro, hold.grip_type.four_finger);
            wantedgriptypes.add(randomgriptype);
        }
        else {
            wantedgriptypes.add(hold.grip_type.four_finger);
        }

        int initialsize = wantedgriptypes.size();

        while (wantedgriptypes.size() < holdsamount) {
            random_nro = rng.nextint(initialsize );
            randomgriptype = wantedgriptypes.get(random_nro);

            wantedgriptypes.add(randomgriptype);
        }

        while (i < holdsamount ) {
            randomgriptype = wantedgriptypes.get(i);

            if (isalternate) {

                // lets search for a holds that max hardness is half the remaining points for a give grade
                random_nro = getholdnumberwithgriptype(min_value/2, (max_value*3)/2,randomgriptype );

                temp_hold_value = allhangboardholds[random_nro].getholdvalue();

                int adjustedminvalue = 2*min_value-temp_hold_value;
                if (adjustedminvalue < 1 ) { adjustedminvalue = 1; }
                int adjustedmaxvalue = 2*max_value-temp_hold_value;
                if (adjustedmaxvalue < 2 ) { adjustedmaxvalue = 2; }

                // and then search for a hold that could be slightly harder than the first one
                random_nro_alt = getholdnumberwithgriptype(adjustedminvalue , adjustedmaxvalue, allhangboardholds[random_nro].grip_style);

                // holds should not be the same, if it is lets just find one hold ie. jump to else statement
                if (random_nro == random_nro_alt) {
                    isalternate = false;
                    continue; }

            }

            else {
                // lets search for a hold that max hardness is half the remaining points for a give grade

                    random_nro = getholdnumberwithgriptype(min_value, max_value, randomgriptype);
                    // it's possible to get single hold for getholdnumberwithgriptype method
                    if (allhangboardholds[random_nro].issinglehold() ) {
                        random_nro = getholdnumberwithvalue(min_value, max_value);
                    }
                    if (rng.nextint(100) < 25) {

                            random_nro = getholdnumberwithvalue(min_value,max_value);
                        }


                // same hold for both hands ie. not alteranating hold
                random_nro_alt = random_nro;
                }
            workoutholdlist.add(allhangboardholds[random_nro]);
            workoutholdlist.add(allhangboardholds[random_nro_alt]);

            isalternate = rng.nextboolean();
            ++i;
        }

        randomizeholdlist();
    }

*/
    // getHoldNumberWithGripType searches hold types with given difficulty range and wanted grip type
    // and returns first that it finds. If none is found, it increases the search range and calls itself
/*

    private int getHoldNumberWithGripType(int min_value, int max_value, Hold.grip_type wanted_hold) {

        Random rng = new Random();
        int search_point = rng.nextInt(allHangboardHolds.length);
        int tuplakierros = 0;

        while ( allHangboardHolds[search_point].getHoldValue() < min_value ||
                allHangboardHolds[search_point].getHoldValue() > max_value ||
                allHangboardHolds[search_point].grip_style != wanted_hold) {

            ++search_point;
            ++tuplakierros;

            if (search_point == allHangboardHolds.length) { search_point = 0; }
            if (tuplakierros > allHangboardHolds.length) {
                // Max value should never be negative anymore, this was a temporary bug fix which allowed new bug
                if (min_value < 1 && max_value > 1000 || max_value <= 0) {

                    return 0;
                } else {
                    return getHoldNumberWithGripType(min_value / 2, max_value * 2, wanted_hold);
                }
            }
        }
        return search_point;
    }

    private int getHoldNumberWithValue(int min_value, int max_value) {

        Random rng = new Random();
        int search_point = rng.nextInt(allHangboardHolds.length);
        int tuplakierros = 0;

        // Search as long as hold is between wanted grade values and not a single hold
        // Starting point is random and will set to 0 when all_hold_value array ends
        while ( allHangboardHolds[search_point].getHoldValue() < min_value ||
                allHangboardHolds[search_point].getHoldValue() > max_value ||
                allHangboardHolds[search_point].isSingleHold() ) {

            ++search_point;
            ++tuplakierros;
            if (search_point == allHangboardHolds.length) { search_point = 0; }

            // for some reason max_value went negative in some test cases, those were probably just
            // illegally created holds but nonetheless, now it wont crash the method anymore
            if (tuplakierros > allHangboardHolds.length) {
                if (min_value < 1 && max_value > 1000 || max_value <= 0) {
                    return 0;
                } else {
                    return getHoldNumberWithValue(min_value / 2, max_value * 2);
                }
            }
        }

        return search_point;
    }
*/
    // initializeHolds method collects from resources all the possible grip types, hold numbers,
    // coordinates and difficulties that a Hangboard can have. Those will be stored in allHangboardHolds
    // and they are randomized so that when a hold is picked it will be random.
    public void initializeHolds(HangboardResources.hangboardName new_board) {

        currentHangboard = new_board;
        int[] hold_values;

       // int holdValueResources = HangboardResources.getHoldValueResources(currentHangboard);
        hold_values = HangboardResources.getGripValues(currentHangboard);

       // int holdCoordinateResources = HangboardResources.getHoldCoordinates(currentHangboard);
        hold_coordinates = HangboardResources.getHoldCoordinates(currentHangboard);

        // Lets put all the possible holds that hangboard can have into allHangboardHolds
        int hold_position = 0;
        allHangboardHolds = new Hold[hold_values.length/3];

        while (hold_position/3 < allHangboardHolds.length) {
            allHangboardHolds[hold_position/3] = new Hold(hold_values[hold_position]);
            hold_position++;
            allHangboardHolds[hold_position/3].setHoldValue(hold_values[hold_position]);
            hold_position++;
            allHangboardHolds[hold_position/3].setGripTypeAndSingleHold(hold_values[hold_position]);
            hold_position++;
        }
        // The positions must be randomized so that GiveHoldWithValue method
        // doesn't favor one hold above the other (next)

        randomizeHoldList();
    }

    // creates a Hold list with same holds within custom grade range, not including holds that are
    // filtered away
    public ArrayList<Hold> getHoldsInRange(int minDifficulty,int maxDifficulty, Boolean[] gripFilter) {

        ArrayList<Hold> holdsInRange = new ArrayList<>();

        for (int i = 0 ; i < allHangboardHolds.length ; i++) {
            if (gripFilter[allHangboardHolds[i].getGripStyleInt()-1] == false) {
                continue;
            }
            if (allHangboardHolds[i].getHoldValue() >= minDifficulty &&
            allHangboardHolds[i].getHoldValue() <= maxDifficulty &&
            allHangboardHolds[i].isSingleHold() == false) {
                holdsInRange.add(allHangboardHolds[i]);
                holdsInRange.add(allHangboardHolds[i]);
            }
        }
        return holdsInRange;
    }

    // creates a Hold list with alternating holds withing custom grade range, not including holds
    // that are filtered away
    public ArrayList<Hold> getAlternateHoldsInRange(int minDifficulty,int maxDifficulty, int alteringFactor, Boolean[] gripFilter) {
        ArrayList<Hold> holdsInRange = new ArrayList<>();
        int minAlter = minDifficulty*2 / (alteringFactor+2);
        int maxAlter = maxDifficulty*( 2 + alteringFactor) / 2;

        for (int i = 0 ; i < allHangboardHolds.length ; i++) {
            if (gripFilter[allHangboardHolds[i].getGripStyleInt()-1] == false) {
                continue;
            }
            if (allHangboardHolds[i].getHoldValue() >= minAlter &&
            allHangboardHolds[i].getHoldValue() <= maxAlter) {

                for (int j = i + 1 ; j < allHangboardHolds.length ; j++) {
                    if (allHangboardHolds[i].getGripStyle() != allHangboardHolds[j].getGripStyle()) {
                        continue;
                    }
                    int holdValue = (allHangboardHolds[i].getHoldValue() +
                            allHangboardHolds[j].getHoldValue() )/2;
                    if (holdValue >= minDifficulty && holdValue <= maxDifficulty) {
                        holdsInRange.add(allHangboardHolds[i] );
                        holdsInRange.add(allHangboardHolds[j] );
                    }

                }
            }
        }
        return holdsInRange;
    }

    // Arbitrary grade values, what hold_values to search in a give grade
    // For example grade 6c consist of holds that are between 7 and 18 in difficulty
/*

    private static int getMinValue(String grade) {
        if (grade.equals("5A")) {return 1;} // 1 - 2		1
        else if (grade.equals("5B")) {return 2;} // 2 - 5	3
        else if (grade.equals("5C")) {return 3;} // 3 - 7	5
        else if (grade.equals("6A")) {return 4;} // 4 - 10	8
        else if (grade.equals("6B")) {return 5;} // 5 - 15	10
        else if (grade.equals("6C")) {return 7;} // 7 - 18	14
        else if (grade.equals("7A")) {return 9;} // 9 - 25	18
        else if (grade.equals("7B")) {return 14;}// 14 - 35	25
        else if (grade.equals("7C")) {return 18;}// 18 - 120	40
        else if (grade.equals("8A")) {return 29;}// 29 - 200	80
        else if (grade.equals("8B")) {return 49;}// 49 - 500	100
        else {return 1; }
    }
    private static int getMaxValue(String grade) {
        if (grade.equals("5A")) {return 2;}
        else if (grade.equals("5B")) {return 5;}
        else if (grade.equals("5C")) {return 7;}
        else if (grade.equals("6A")) {return 10;}
        else if (grade.equals("6B")) {return 15;}
        else if (grade.equals("6C")) {return 18;}
        else if (grade.equals("7A")) {return 25;}
        else if (grade.equals("7B")) {return 35;}
        else if (grade.equals("7C")) {return 120;}
        else if (grade.equals("8A")) {return 200;}
        else if (grade.equals("8B")) {return 500;}
        else {return 1; }
    }

*/
/*
    public void clearWorkoutHoldList() {
        workoutHoldList.clear();
    }
*/

/*
    public void setDifficultyLimits(int lowerBound, int upperBound) {

        Hold[] tempHoldList = new Hold[workoutHoldList.size() ];

        for (int i = 0 ; i < workoutHoldList.size() ; i++ ) {
            tempHoldList[i] = workoutHoldList.get(i);
        }

        clearWorkoutHoldList();

        for (int i = 0 ; i < tempHoldList.length ; i= i +2) {

            // Trying to exclude hold compinations where one hold is too easy (JUG)
            if (tempHoldList[i].getHoldValue() <= tempHoldList[i+1].getHoldValue() / 3 ||
            tempHoldList[i+1].getHoldValue() <= tempHoldList[i].getHoldValue() / 3 ) {

                continue;
            }

            int holdValue = (tempHoldList[i].getHoldValue() + tempHoldList[i+1].getHoldValue() ) / 2;


            if (holdValue >= lowerBound && holdValue <= upperBound) {
                workoutHoldList.add(tempHoldList[i]);
                workoutHoldList.add(tempHoldList[i+1]);
            }
        }


    }

    public void addEveryGripTypeCompinationToWorkoutList(Hold.grip_type wantedGripType) {
        Arrays.sort(allHangboardHolds);

        //ArrayList<Hold> gripTypeWorkoutList = new ArrayList<>();
        for (int i = 0 ; i < allHangboardHolds.length ; i++) {

            if (allHangboardHolds[i].grip_style == wantedGripType) {
                for (int j = 0 ; j < allHangboardHolds.length ; j++) {

                    if (allHangboardHolds[i].isSingleHold() && allHangboardHolds[j].isSingleHold() &&
                            allHangboardHolds[i].getHoldNumber() == allHangboardHolds[j].getHoldNumber()
                    ) {
                        continue;
                    }

                    if (allHangboardHolds[j].grip_style == wantedGripType) {
                        workoutHoldList.add(allHangboardHolds[i]);
                        workoutHoldList.add(allHangboardHolds[j]);
                    }

                }

            }

        }
       // workoutHoldList = gripTypeWorkoutList;



    }

 */
/*

    public void TESTprintHoldList() {

        for (int i = 0 ; i  < workoutHoldList.size() ; i=i+2) {

            String holdInfo = workoutHoldList.get(i).getHoldInfo(workoutHoldList.get(i+1)).replace("\n"," ");
            Log.v("MMit",holdInfo);
        }
    }
*/

}
