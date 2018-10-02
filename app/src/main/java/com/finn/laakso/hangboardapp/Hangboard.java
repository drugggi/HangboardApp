package com.finn.laakso.hangboardapp;

import android.content.res.Resources;

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
    public Hangboard(Resources res) {
        // starter_grips = res.getStringArray(R.array.beastmaker1000);
        grades = res.getStringArray(R.array.grades);
        currentHangboard = HangboardResources.hangboardName.BM1000;

        workoutHoldList = new ArrayList<Hold>();

    }

    public ArrayList<Hold> getCurrentHoldList() {
        return workoutHoldList;
    }

    // Coordinates getters for hand image positioning
    public int getLeftFingerImage(int position) {
        return workoutHoldList.get(position*2).getGripImage(true);
    }
    public int getRightFingerImage(int position) {
        return workoutHoldList.get(position*2+1).getGripImage(false);
    }


    public int getCoordLeftX(int position) {
        int holdNumber = workoutHoldList.get(position*2).getHoldNumber();

        if (holdNumber*5 <= hold_coordinates.length) {
            return hold_coordinates[(holdNumber - 1) * 5 + 1];
        }
        return 0;
    }
    public int getCoordLeftY(int position) {
        int holdNumber = workoutHoldList.get(position*2).getHoldNumber();

        if (holdNumber*5 <= hold_coordinates.length) {
            return hold_coordinates[(holdNumber - 1) * 5 + 2];
        }
        return 0;
    }
    public int getCoordRightX(int position) {
        int holdNumber = workoutHoldList.get(position*2+1).getHoldNumber();

        if (holdNumber*5 <= hold_coordinates.length) {
            return hold_coordinates[(holdNumber - 1) * 5 + 3];
        }
        return 0;
    }
    public int getCoordRightY(int position) {
        int holdNumber = workoutHoldList.get(position*2+1).getHoldNumber();

        if (holdNumber*5 <= hold_coordinates.length) {
            return hold_coordinates[(holdNumber - 1) * 5 + 4];
        }
        return 0;
    }


/*


    public int getCoordLeftX(int position) {
        return workoutHoldList.get(position*2).getLeftCoordX();
    }
    public int getCoordLeftY(int position) {
        return workoutHoldList.get(position*2).getLeftCoordY();
    }
    public int getCoordRightX(int position) {
        return workoutHoldList.get(position*2+1).getRightCoordX();
    }
    public int getCoordRightY(int position) {
        return workoutHoldList.get(position*2+1).getRightCoordY();
    }

*/

    public String getGrade(int position) {
        return grades[position];
    }

    public String getHangboardName() {
        return HangboardResources.getHangboardStringName(currentHangboard);
    }

    // Just converts workoutHoldLists' Hold descriptions into Array of Strings
    public String[] getGrips() {
            ArrayList<String> tempList = new ArrayList<String>();

        for (int i = 0; i < workoutHoldList.size()/2; i++) {
                tempList.add((i+1) + ". " + workoutHoldList.get(2*i).getHoldInfo( workoutHoldList.get(2*i+1) ) );

        }
        return tempList.toArray(new String[tempList.size()]);
    }

    // WHAT TEHESE TWO SETGRIPS DO?!?!??!?! EXPLAIN!!!!
    public void setGrips(ArrayList<Hold> newList) {
        if (newList.size() != 0) {
            workoutHoldList = newList;

        }

    }

    // Sets holdList to match those info in String[] grips
    public String[] setGrips(int position) {
        randomizeGrips(position);
        return getGrips();

    }

    public int getCurrentHoldListSize() {
        return workoutHoldList.size();
    }

    public int getMaxHoldNumber() {
        int max = 0;
        for (int i=0; i < allHangboardHolds.length; i++) {
            if (allHangboardHolds[i].getHoldNumber() > max) {
                max = allHangboardHolds[i].getHoldNumber();
            }
        }
        return max;
    }

    public void updateHoldCoordinates() {
        int holdNumber;
        Hold.grip_type gripType;

        for (int i = 0 ; i < workoutHoldList.size() ; i++ ) {
            holdNumber = workoutHoldList.get(i).getHoldNumber();
            gripType = workoutHoldList.get(i).getGripStyle();
            workoutHoldList.set(i,createCustomHold( holdNumber , gripType ));
        }

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
    // because the hands alternates repeatedly in a single hang
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
                random_nro_alt = getHoldNumberWithValue(min_value , max_value, allHangboardHolds[random_nro].grip_style);

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


                random_nro_alt = getHoldNumberWithValue( min_value, max_value, allHangboardHolds[random_nro].grip_style);

                // Holds should not be the same, if it is lets make sure next if statement is true
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

    // getHoldNumberWithValue searches hold types with given difficulty range and wanted grip type
    // and returns first that if finds. I none is found, it increases the search range and calls itself
    private int getHoldNumberWithValue(int min_value, int max_value, Hold.grip_type wanted_hold) {

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
                    return getHoldNumberWithValue(min_value / 2, max_value * 2, wanted_hold);
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
            allHangboardHolds[hold_position/3].setGripTypeAndSingleHang(hold_values[hold_position]);
            hold_position++;
        }
        // The positions must be randomized so that GiveHoldWithValue method
        // doesn't favor one hold above the other (next)
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
        setGrips(0);
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
