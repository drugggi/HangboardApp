package com.finn.laakso.hangboardapp;

import android.content.res.Resources;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**
 * Created by Laakso on 16.11.2017.
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
    public Hangboard(Resources res, HangboardResources.hangboardName HB) {

        grades = res.getStringArray(R.array.grades);

        workoutHoldList = new ArrayList<Hold>();

        initializeHolds(res,HB);

    }

    // Hold class knows the image resources for hand images
    public int getLeftFingerImage(int position) {
        return workoutHoldList.get(position*2).getGripImage(true);
    }
    public int getRightFingerImage(int position) {
        return workoutHoldList.get(position*2+1).getGripImage(false);
    }


    // Coordinates getters for hand image positioning, it is possible to request hold number that
    // does not exist in hold_coordinates
    public int getCoordLefthandX(int position) {
        int holdNumber = workoutHoldList.get(position*2).getHoldNumber();

        if (holdNumber*5 <= hold_coordinates.length) {
            return hold_coordinates[(holdNumber - 1) * 5 + 1];
        }
        return 0;
    }
    public int getCoordLefthandY(int position) {
        int holdNumber = workoutHoldList.get(position*2).getHoldNumber();

        if (holdNumber*5 <= hold_coordinates.length) {
            return hold_coordinates[(holdNumber - 1) * 5 + 2];
        }
        return 0;
    }
    public int getCoordRighthandX(int position) {
        int holdNumber = workoutHoldList.get(position*2+1).getHoldNumber();

        if (holdNumber*5 <= hold_coordinates.length) {
            return hold_coordinates[(holdNumber - 1) * 5 + 3];
        }
        return 0;
    }
    public int getCoordRighthandY(int position) {
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

    // Sets holdList to match those info in String[] grips
/*
    public void setNewWorkoutHolds(int position) {
        randomizeGrips(position);
        // return getGrips();

    }
*/


// For every hold there must be four corresponding coordinates so
    public int getMaxHoldNumber() {
        return hold_coordinates.length / 5;
/*
        int max = 0;
        for (int i=0; i < allHangboardHolds.length; i++) {
            if (allHangboardHolds[i].getHoldNumber() > max) {
                max = allHangboardHolds[i].getHoldNumber();
            }
        }

        Log.e("maxhold Number","max / testMax " + max + "/" + test_max);
        return max;
        */
    }

/*
    // Probably deprecated becouse coordinates responsibility was moved from Hold to Hangboard/HangboardResources
    public void updateHoldCoordinates() {
        int holdNumber;
        Hold.grip_type gripType;

        for (int i = 0 ; i < workoutHoldList.size() ; i++ ) {
            holdNumber = workoutHoldList.get(i).getHoldNumber();
            gripType = workoutHoldList.get(i).getGripStyle();
            workoutHoldList.set(i,createCustomHold( holdNumber , gripType ));
        }

    }
*/

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
        // User selected different grip type, lets chan
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

    public String[] getGrades() {
        return grades;
    }

    // Sets the workoutHoldList to given amount and randomizes those holds
    public void setGripAmount(int amount, int grade_position) {

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
                randomizeGrip(grade_position, workoutHoldList.size()/2-1 );

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
        for (int i = allHangboardHolds.length - 1; i > 0; i--)
        {
            index = random.nextInt(i + 1);
            temp = allHangboardHolds[index];
            allHangboardHolds[index] = allHangboardHolds[i];
            allHangboardHolds[i] = temp;
        }
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

    // Method randomizeGrips randomizes holds and grips that are used in a workout
    public void randomizeGrips(int grade_position) {


        int holdsAmount = workoutHoldList.size()/2;
        if (holdsAmount == 0) {holdsAmount = 6; }


        workoutHoldList.clear();
        // Random generator that is only used if we are using the same hold or alternating between holds
        Random rn = new Random();
        boolean isAlternate = rn.nextBoolean();


        // these ints will be randomized and those represents holds in allHangboardHolds array
        int random_nro;
        int random_nro_alt;
        int temp_hold_value;

        int min_value=getMinValue(grades[grade_position]);
        int max_value=getMaxValue(grades[grade_position]);
        int value = 0;
        int i=0;

        while (i < holdsAmount ) {

            if (isAlternate) {

                // Lets search for a holds that max hardness is half the remaining points for a give grade
                random_nro = getHoldNumberWithValue(min_value/2, (max_value*3)/2 );

                temp_hold_value = allHangboardHolds[random_nro].getHoldValue();

                min_value = 2*min_value-temp_hold_value;
                if (min_value < 1 ) { min_value = 1; }
                max_value = 2*max_value-temp_hold_value;
                if (max_value < 2 ) { max_value = 2; }

                // And then search for a hold that could be slightly harder than the first one
                random_nro_alt = getHoldNumberWithGripType(min_value , max_value, allHangboardHolds[random_nro].grip_style);

                min_value=getMinValue(grades[grade_position]);
                max_value=getMaxValue(grades[grade_position]);


                // Holds should not be the same, if it is lets just find one hold ie. jump to else statement
                if (random_nro == random_nro_alt) {
                    isAlternate = false;
                    continue; }
                workoutHoldList.add(allHangboardHolds[random_nro]);
                workoutHoldList.add(allHangboardHolds[random_nro_alt]);

            }

            else {
                // Lets search for a hold that max hardness is half the remaining points for a give grade
                random_nro = getHoldNumberWithValue(min_value, max_value);

                value = value + allHangboardHolds[random_nro].getHoldValue();
                workoutHoldList.add(allHangboardHolds[random_nro]);
                workoutHoldList.add(allHangboardHolds[random_nro]);

            }
            isAlternate = rn.nextBoolean();
            ++i;
        }

        randomizeHoldList();
    }

    // RandomizeGrip method randomizes selected grip instead of all the grips
    public void randomizeGrip(int grade_position, int hold_nro) {


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

    private ArrayList<Hold.grip_type> getGripTypesWithingGrade(int min_value, int max_value) {
        ArrayList<Hold.grip_type> differentGripTypes = new ArrayList<>();

        int holdValue;
        for (int i = 0 ; i < allHangboardHolds.length ; i++ ) {
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
/*
        for (int i = 0 ; i < differentGripTypes.size() ; i++) {
            Log.d("GripTypes","different: " + differentGripTypes.get(i) );
        }*/
        return differentGripTypes;
    }

    private static int getScaledHoldValue(int value,TimeControls timeControls) {
        int TUT = timeControls.getTimeUnderTension();
        int WT = timeControls.getTotalTime();
        float intensity = (float) TUT / WT;

        // 300 and 0.25 are just some base values
        int workload = value * 500;
        float power = value * 0.20f;

        float newValueBasedOnWorkload = (float) workload / TUT;
        float newValueBasedOnPower = power / intensity;

        // Log.d("Values", "value: " + value + "   valueWorkload/valuePower: " + newValueBasedOnWorkload + "/"+newValueBasedOnPower);

        int newValue = (int) ((newValueBasedOnWorkload + newValueBasedOnPower) / 2);

        if (newValue < 1) {return 1; }
        else if (newValue > 500) {return 500; }
        else {return newValue; }

    }
    public void randomizeNewWorkoutHolds(int grade_position, TimeControls timeControls) {
        int holdsAmount = workoutHoldList.size()/2;
        if (holdsAmount == 0) {holdsAmount = 6; }

        workoutHoldList.clear();
        // Random generator that is only used if we are using the same hold or alternating between holds
        Random rng = new Random();
        boolean isAlternate = rng.nextBoolean();

        // these ints will be randomized and those represents holds in allHangboardHolds array
        int random_nro;
        int random_nro_alt;
        int temp_hold_value;
/*

        TimeControls tempControls = new TimeControls();

            for (int j = 0 ; j< 7 ; j++) {
                tempControls.setPremadeTimeControls(j);
                Log.d("TC",tempControls.getTimeControlsAsString() );
                for (int value = 1 ; value < 50 ; value+= 5) {
                    Log.d("VALUE", "Old/New  " + value + "/" + getScaledHoldValue(value, tempControls));
                }
            }
            tempControls.setHangLaps(1);
        for (int j = 0 ; j< 7 ; j++) {
            tempControls.setPremadeTimeControls(j);
            Log.d("TC",tempControls.getTimeControlsAsString() );
            for (int value = 1 ; value < 50 ; value+= 5) {
                Log.d("VALUE", "Old/New  " + value + "/" + getScaledHoldValue(value, tempControls));
            }
        }

*/


        int min_value=getScaledHoldValue(getMinValue(grades[grade_position]),timeControls);
        int max_value=getScaledHoldValue(getMaxValue(grades[grade_position]),timeControls);

       // Log.d("MinValue" , "Old/New  " + getMinValue(grades[grade_position] ) + "/" + min_value );
       // Log.d("MaxValue","Old/New   " +  getMaxValue(grades[grade_position]) + "/" + max_value );

        int i=0;

        // Lets see how many different grip types we find within given grade range

        ArrayList<Hold.grip_type> wantedGripTypes = getGripTypesWithingGrade(min_value, max_value);

        Hold.grip_type randomGripType;
        // Lets prefer four finger grip by setting one extra at random place
        if (wantedGripTypes.size() != 0) {
            random_nro = rng.nextInt(wantedGripTypes.size());
            randomGripType = wantedGripTypes.get(random_nro);
            wantedGripTypes.set(random_nro, Hold.grip_type.FOUR_FINGER);
            wantedGripTypes.add(randomGripType);
        }
        else {
            Log.e("ERR","WantedGrypTypes size was 0!!!");
            wantedGripTypes.add(Hold.grip_type.FOUR_FINGER);
        }
        int initialSize = wantedGripTypes.size();

        while (wantedGripTypes.size() < holdsAmount) {
            random_nro = rng.nextInt(initialSize );
            randomGripType = wantedGripTypes.get(random_nro);

            wantedGripTypes.add(randomGripType);
        }
/*

        for (int j = 0 ; j < wantedGripTypes.size() ; j++ ) {
            Log.d("GripTypes",j + " wanted griptypes: " + wantedGripTypes.get(j) );

            if (wantedGripTypes.size() < holdsAmount) {
                Log.e("ERR", "HOLDSAMOUNT > WANTEDGRIPTYPES SIZE");
            }
        }

*/


        while (i < holdsAmount ) {

            randomGripType = wantedGripTypes.get(i);
    /*
            Hold.grip_type nextGripType;
            if (i < wantedGripTypes.size() ) {
                nextGripType = wantedGripTypes.get(i);
            } else {
                nextGripType = null;
            }
*/
            if (isAlternate) {

                // Lets search for a holds that max hardness is half the remaining points for a give grade
                random_nro = getHoldNumberWithGripType(min_value/2, (max_value*3)/2,randomGripType );

                temp_hold_value = allHangboardHolds[random_nro].getHoldValue();

                int adjustedMinValue = 2*min_value-temp_hold_value;
                if (adjustedMinValue < 1 ) { adjustedMinValue = 1; }
                int adjustedMaxValue = 2*max_value-temp_hold_value;
                if (adjustedMaxValue < 2 ) { adjustedMaxValue = 2; }

                // And then search for a hold that could be slightly harder than the first one
                random_nro_alt = getHoldNumberWithGripType(adjustedMinValue , adjustedMaxValue, allHangboardHolds[random_nro].grip_style);
/*

                min_value=getMinValue(grades[grade_position]);
                max_value=getMaxValue(grades[grade_position]);
*/

                // Holds should not be the same, if it is lets just find one hold ie. jump to else statement
                if (random_nro == random_nro_alt) {
                  //  Log.e("Single hold Alternative","täytyy ettiä uus paremmall tekniikalla: " + i);
                    isAlternate = false;
                    continue; }

            }

            else {
                // Lets search for a hold that max hardness is half the remaining points for a give grade

                    random_nro = getHoldNumberWithGripType(min_value, max_value, randomGripType);
                    // it's possible to get single hold for getholdnumberwithgriptype method
                    if (allHangboardHolds[random_nro].isSingleHold() ) {
                       // Log.e("Single hold löyty", "täytyy ettiä uus paremmall tekniikalla: " + i +": " +allHangboardHolds[random_nro].getGripStyle());
                        random_nro = getHoldNumberWithValue(min_value, max_value);
                    }
                        // 25% percent change we dont set the preferrec griptype
                    if (rng.nextInt(100) < 25) {

                            random_nro = getHoldNumberWithValue(min_value,max_value);
                           // Log.e("25%","Mahkulla laitettiin ote: " + i + ": griptype" +allHangboardHolds[random_nro].getGripStyle() );
                        }


                // Same hold for both hands ie. not alteranating hold
                random_nro_alt = random_nro;
                }
            workoutHoldList.add(allHangboardHolds[random_nro]);
            workoutHoldList.add(allHangboardHolds[random_nro_alt]);

            isAlternate = rng.nextBoolean();
            ++i;
        }

        randomizeHoldList();
    }

    // getHoldNumberWithGripType searches hold types with given difficulty range and wanted grip type
    // and returns first that if finds. I none is found, it increases the search range and calls itself
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
    // initializeHolds method collects from resources all the possible grip types, hold numbers,
    // coordinates and difficulties that a Hangboard can have. Those will be stored in allHangboardHolds
    // and they are randomized so that when a hold is picked it will be random.
    public void initializeHolds(Resources res, HangboardResources.hangboardName new_board) {
 /*       currentHangboard = new_board;

        int[] hold_values = res.getIntArray(R.array.grip_values_bm1000);
        
        switch (new_board) {
            case BM1000:
                hold_values = res.getIntArray(R.array.grip_values_bm1000);
                hold_coordinates = res.getIntArray(R.array.bm1000_coordinates);
                break;
            case BM2000:
                hold_values = res.getIntArray(R.array.grip_values_bm2000);
                hold_coordinates = res.getIntArray(R.array.bm2000_coordinates);
                break;
            case TRANS:
                hold_values = res.getIntArray(R.array.grip_values_trans);
                hold_coordinates = res.getIntArray(R.array.trans_coordinates);
                break;
            case TENSION:
                hold_values = res.getIntArray(R.array.grip_values_tension);
                hold_coordinates = res.getIntArray(R.array.tension_coordinates);
                break;
            case ZLAG:
                hold_values = res.getIntArray(R.array.grip_values_zlag);
                hold_coordinates = res.getIntArray(R.array.zlag_coordinates);
                break;
            case MOONHARD:
                hold_values = res.getIntArray(R.array.grip_values_moonhard);
                hold_coordinates = res.getIntArray(R.array.moonhard_coordinates);
                break;
            case MOONEASY:
                hold_values = res.getIntArray(R.array.grip_values_mooneasy);
                hold_coordinates = res.getIntArray(R.array.mooneasy_coordinates);
                break;
            case METO:
                hold_values = res.getIntArray(R.array.grip_values_meto);
                hold_coordinates = res.getIntArray(R.array.meto_coordinates);
                break;
            case ROCKPRODIGY:
                hold_values = res.getIntArray(R.array.grip_values_rockprodigy);
                hold_coordinates = res.getIntArray(R.array.rockprodigy_coordinates);
                break;
            case PROBLEMSOLVER:
                hold_values = res.getIntArray(R.array.grip_values_problemsolver);
                hold_coordinates = res.getIntArray(R.array.problemsolver_coordinates);
                break;
            case METO_CONTACT:
                hold_values = res.getIntArray(R.array.grip_values_meto_contact);
                hold_coordinates = res.getIntArray(R.array.meto_contact_coordinates);
                break;
            case METO_WOOD:
                hold_values = res.getIntArray(R.array.grip_values_meto_wood);
                hold_coordinates = res.getIntArray(R.array.meto_wood_coordinates);
                break;
            case DRCC:
                hold_values = res.getIntArray(R.array.grip_values_drcc);
                hold_coordinates = res.getIntArray(R.array.drcc_coordinates);
                break;
        }
*/
        currentHangboard = new_board;
        int[] hold_values;

        int holdValueResources = HangboardResources.getHoldValueResources(currentHangboard);
        hold_values = res.getIntArray(holdValueResources);

        int holdCoordinateResources = HangboardResources.getHoldCoordinates(currentHangboard);
        hold_coordinates = res.getIntArray(holdCoordinateResources);


        // Lets put all the possible holds that hangboard can have into allHangboardHolds
        int hold_position = 0;
        allHangboardHolds = new Hold[hold_values.length/3];

        while (hold_position/3 < allHangboardHolds.length) {
            allHangboardHolds[hold_position/3] = new Hold(hold_values[hold_position]);
            hold_position++;
           // allHangboardHolds[hold_position/3].setHoldCoordinates(hold_coordinates);
            allHangboardHolds[hold_position/3].setHoldValue(hold_values[hold_position]);
            hold_position++;
            allHangboardHolds[hold_position/3].setGripTypeAndSingleHold(hold_values[hold_position]);
            hold_position++;
        }
        // The positions must be randomized so that GiveHoldWithValue method
        // doesn't favor one hold above the other (next)

        // When new board is set, lets make new hold list with grade 0 -> 5A
        randomizeGrips(0);
        randomizeHoldList();
/*
        Hold temp;
        int index;
        Random random = new Random();
        for (int i = allHangboardHolds.length - 1; i > 0; i--)
        {
            index = random.nextInt(i + 1);
            temp = allHangboardHolds[index];
            allHangboardHolds[index] = allHangboardHolds[i];
            allHangboardHolds[i] = temp;
        }*/
        // setNewWorkoutHolds(0);
    }


    // Arbitrary grade values, what hold_values to search in a give grade
    // For example grade 6c consist of holds that are betweent 7 and 18 in difficulty
    private static int getMinValue(String grade) {
        if (grade.equals("5A")) {return 1;}
        else if (grade.equals("5B")) {return 2;}
        else if (grade.equals("5C")) {return 3;}
        else if (grade.equals("6A")) {return 4;}
        else if (grade.equals("6B")) {return 5;}
        else if (grade.equals("6C")) {return 7;}
        else if (grade.equals("7A")) {return 9;}
        else if (grade.equals("7B")) {return 14;}
        else if (grade.equals("7C")) {return 18;}
        else if (grade.equals("8A")) {return 29;}
        else if (grade.equals("8B")) {return 49;}
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

}
