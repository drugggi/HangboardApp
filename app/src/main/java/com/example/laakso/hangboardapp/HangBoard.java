package com.example.laakso.hangboardapp;

import android.content.res.Resources;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**
 * Created by Laakso on 16.11.2017.
 */

public class HangBoard {

    private String[] grades;
    CustomSwipeAdapter.hangboard current_board;

    // All possible grip types in a hangboard
    private Hold[] all_hold_values;

    // valueList represents the holds that are in a current workout shown in holdsListView
    // These will be sent to WorkoutActivity. Even values has the left hand information and odd values  right hand information
    ArrayList<Hold> valueList;

    // Grips constructor takes resources so that it can read all the information needed constructing
    // workout and hangs and grips
    public HangBoard(Resources res) {
        // starter_grips = res.getStringArray(R.array.beastmaker1000);
        grades = res.getStringArray(R.array.grades);
        current_board = CustomSwipeAdapter.hangboard.BM1000;

        valueList = new ArrayList<Hold>();

    }

    public ArrayList<Hold> getCurrentHoldList() {
        return valueList;
    }

    // Coordinates getters for hand image positioning
    public int getLeftFingerImage(int position) {
        return valueList.get(position*2).getGripImage(true);
    }
    public int getRightFingerImage(int position) {
        return valueList.get(position*2+1).getGripImage(false);
    }

    public int getCoordLeftX(int position) {
        return valueList.get(position*2).getLeftCoordX();

    }
    public int getCoordLeftY(int position) {
        return valueList.get(position*2).getLeftCoordY();

    }
    public int getCoordRightX(int position) {
        return valueList.get(position*2+1).getRightCoordX();

    }
    public int getCoordRightY(int position) {
        return valueList.get(position*2+1).getRightCoordY();

    }


    public String getGrade(int position) {
        return grades[position];
    }

    // Just converts valueLists' Hold descriptions into Array of Strings
    public String[] getGrips() {
        ArrayList<String> testList = new ArrayList<String>();

        for (int i = 0; i < valueList.size()/2; i++) {
                testList.add((i+1) + ". " + valueList.get(2*i).getHoldInfo( valueList.get(2*i+1) ) );

        }
        String[] holds = testList.toArray(new String[testList.size()]);
        return holds;
    }


    // Sets holdList to match those info in String[] grips
    public String[] setGrips(int position) {
        randomizeGrips(position);
        return getGrips();

    }

    public int getCurrentHoldListSize() {
        return valueList.size();
    }

    public void sortHoldByDifficulty() {
        Arrays.sort(all_hold_values);

        valueList.clear();

        int i = 0;

        while (i < all_hold_values.length ) {
            // Skip the holds that are just one of in hangboard
            if (all_hold_values[i].isSingleHold()) {
                i++;
                continue;
            }

            valueList.add(all_hold_values[i]);
            valueList.add(all_hold_values[i]);

            i++;
        }

    }

    public String[] getGrades() {
        return grades;
    }

    // Sets the valueList to given amount and randomizes those holds
    public void setGripAmount(int amount, int grade_position) {
        valueList.clear();

        while (amount > 0) {
            valueList.add(new Hold(1));
            valueList.add(new Hold(1));
            --amount;
        }

        // Lets randomize the holds in all_hold_values
        Hold temp;
        int index;
        Random random = new Random();
        for (int i = all_hold_values.length - 1; i > 0; i--)
        {
            index = random.nextInt(i + 1);
            temp = all_hold_values[index];
            all_hold_values[index] = all_hold_values[i];
            all_hold_values[i] = temp;
        }

        randomizeGrips(grade_position);

    }

    // Method randomizeGrips randomizes holds and grips that are used in a workout
    public void randomizeGrips(int grade_position) {
        int poista_tama = valueList.size()/2;
        if (poista_tama == 0) {poista_tama = 6; }

        valueList.clear();
        // Random generator that is only used if we are using the same hold or alternating between holds
        Random rn = new Random();
        boolean isAlternate = rn.nextBoolean();

        // grips[position] = "";

        // these ints will be randomized and those represents holds in all_hold_values array
        int random_nro;
        int random_nro_alt;

        int min_value=getMinValue(grades[grade_position]);
        int max_value=getMaxValue(grades[grade_position]);

        int value = 0;
        int i=0;

        // There is holdList.size() different grips in a given workout so lets randomize all of them
        while (i < poista_tama ) {

            if (isAlternate) {

                // Lets search for a holds that max hardness is half the remaining points for a give grade
                random_nro = getHoldNumberWithValue(min_value, max_value );
                // And then search for a hold that could be slightly harder than the first one
                random_nro_alt = getHoldNumberWithValue(min_value , max_value*2, all_hold_values[random_nro].grip_style);

                // Holds should not be the same, if it is lets just find one hold ie. jump to else statement
                if (random_nro == random_nro_alt) { isAlternate = false; continue; }
                valueList.add(all_hold_values[random_nro]);
                valueList.add(all_hold_values[random_nro_alt]);
                /*holdList.set(i, i + ". HOLD: " + all_hold_values[random_nro].getHoldNumber() + "/" + all_hold_values[random_nro_alt].getHoldNumber() + "\nGRIP: " +
                        all_hold_values[random_nro].getHoldText() + " alternate\n Difficulty: "+
                                (all_hold_values[random_nro].getHoldValue() + all_hold_values[random_nro_alt].getHoldValue() )/2);*/

            }

            else {
                // Lets search for a hold that max hardness is half the remaining points for a give grade
                random_nro = getHoldNumberWithValue(min_value, max_value);

                value = value + all_hold_values[random_nro].getHoldValue();
                valueList.add(all_hold_values[random_nro]);
                valueList.add(all_hold_values[random_nro]);
                /*
                holdList.set(i, i + ". HOLD: " + all_hold_values[random_nro].getHoldNumber() + "\nGRIP: " + all_hold_values[random_nro].getHoldText() +
                        "\nDifficulty: " + all_hold_values[random_nro].getHoldValue() );*/

            }
            isAlternate = rn.nextBoolean();
            ++i;
        }

        return;
    }

    // RandomizeGrip method randomizes selected grip instead of all the grips
    public void randomizeGrip(int grade_position, int hold_nro) {

        // Random generator that is only used if we are using the same hold or alternating between holds
        Random rn = new Random();
        boolean isAlternate = rn.nextBoolean();

        // these ints will be randomized and those represents holds in all_hold_values array
        int random_nro;
        int random_nro_alt;

        // Min and max values of grades which the hold search is based on
        int min_value=getMinValue(grades[grade_position]);
        int max_value=getMaxValue(grades[grade_position]);

        int value = 0;

        if (isAlternate) {

                // Lets search for a holds that max hardness is half the remaining points for a give grade
                random_nro = getHoldNumberWithValue(min_value/2, max_value );
                // And then search for a hold that could be slightly harder than the first one
                random_nro_alt = getHoldNumberWithValue( min_value, max_value*2, all_hold_values[random_nro].grip_style);

                // Holds should not be the same, if it is lets make sure next if statement is true
                if (random_nro == random_nro_alt) { isAlternate = false; }

            valueList.set(hold_nro*2, all_hold_values[random_nro]);
            valueList.set(hold_nro*2 + 1,all_hold_values[random_nro_alt]);
/*
                holdList.set(hold_nro, hold_nro + ". HOLD: " + all_hold_values[random_nro].getHoldNumber() + "/"
                        + all_hold_values[random_nro_alt].getHoldNumber() + "\nGRIP: " +
                        all_hold_values[random_nro].getHoldText() + " alternate\n Difficulty: "+
                        (all_hold_values[random_nro].getHoldValue() + all_hold_values[random_nro_alt].getHoldValue() )/2);*/

            }

        if ( !isAlternate ) {
                // Lets search for a hold that max hardness is half the remaining points for a give grade
                random_nro = getHoldNumberWithValue(min_value, max_value);

                value = value + all_hold_values[random_nro].getHoldValue();

            valueList.set(hold_nro*2, all_hold_values[random_nro]);
            valueList.set(hold_nro*2 + 1,all_hold_values[random_nro]);



            }
    }

    // getHoldNumberWithValue searches hold types with given difficulty range and wanted grip type
    // and returns first that if finds. I none is found, it increases the search range and calls itself
    private int getHoldNumberWithValue(int min_value, int max_value, Hold.grip_type wanted_hold) {

        Random rng = new Random();
        int search_point = rng.nextInt(all_hold_values.length);
        int tuplakierros = 0;

        while ( all_hold_values[search_point].getHoldValue() < min_value ||
                all_hold_values[search_point].getHoldValue() > max_value ||
                all_hold_values[search_point].grip_style != wanted_hold) {

            ++search_point;
            ++tuplakierros;

            if (search_point == all_hold_values.length) { search_point = 0; }
            if (tuplakierros > all_hold_values.length) {
                if (min_value < 1 && max_value > 1000) {
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
        int search_point = rng.nextInt(all_hold_values.length);
        int tuplakierros = 0;

        // Search as long as hold is between wanted grade values and not a single hold
        // Starting point is random and will set to 0 when all_hold_value array ends
        while ( all_hold_values[search_point].getHoldValue() < min_value ||
                all_hold_values[search_point].getHoldValue() > max_value ||
                all_hold_values[search_point].isSingleHold() ) {

            ++search_point;
            ++tuplakierros;
            if (search_point == all_hold_values.length) { search_point = 0; }
            if (tuplakierros > all_hold_values.length) {
                if (min_value < 1 && max_value > 1000) {
                    return 0;
                } else {
                    return getHoldNumberWithValue(min_value / 2, max_value * 2);
                }
            }
        }
        return search_point;
    }


    // InitializeHolds method collects from resources all the possible grip, holds and difficulty
    // in a give hangboard that can be applied in a hangboard workout
    public void InitializeHolds(Resources res, CustomSwipeAdapter.hangboard new_board) {
        current_board = new_board;

        int hold_pos = 0;
        if (current_board == CustomSwipeAdapter.hangboard.BM1000) {
            int[] arvot = res.getIntArray(R.array.grip_values_bm1000);

            all_hold_values = new Hold[arvot.length/3];

            while (hold_pos/3 < all_hold_values.length) {
                all_hold_values[hold_pos/3] = new Hold(arvot[hold_pos]);
                hold_pos++;
                all_hold_values[hold_pos/3].setHoldCoordinates(res.getIntArray(R.array.bm1000_coordinates));
                all_hold_values[hold_pos/3].setHoldValue(arvot[hold_pos]);
                hold_pos++;
                all_hold_values[hold_pos/3].setGripTypeAndSingleHang(arvot[hold_pos]);
                hold_pos++;
            }
        }

        else if (current_board == CustomSwipeAdapter.hangboard.BM2000 ) {
            int[] arvot = res.getIntArray(R.array.grip_values_bm2000);

            all_hold_values = new Hold[arvot.length/3];

            while (hold_pos/3 < all_hold_values.length) {
                all_hold_values[hold_pos/3] = new Hold(arvot[hold_pos]);
                hold_pos++;
                all_hold_values[hold_pos/3].setHoldCoordinates(res.getIntArray(R.array.bm2000_coordinates));
                all_hold_values[hold_pos/3].setHoldValue(arvot[hold_pos]);
                hold_pos++;
                all_hold_values[hold_pos/3].setGripTypeAndSingleHang(arvot[hold_pos]);
                hold_pos++;
            }
        }

        else if (current_board == CustomSwipeAdapter.hangboard.TRANS ) {
            int[] arvot = res.getIntArray(R.array.grip_values_trans);

            all_hold_values = new Hold[arvot.length/3];

            while (hold_pos/3 < all_hold_values.length) {
                all_hold_values[hold_pos/3] = new Hold(arvot[hold_pos]);
                hold_pos++;
                all_hold_values[hold_pos/3].setHoldCoordinates(res.getIntArray(R.array.trans_coordinates));
                all_hold_values[hold_pos/3].setHoldValue(arvot[hold_pos]);
                hold_pos++;
                all_hold_values[hold_pos/3].setGripTypeAndSingleHang(arvot[hold_pos]);
                hold_pos++;
            }
        }

        else if (current_board == CustomSwipeAdapter.hangboard.TENSION ) {
            int[] arvot = res.getIntArray(R.array.grip_values_tension);

            all_hold_values = new Hold[arvot.length/3];

            while (hold_pos/3 < all_hold_values.length) {
                all_hold_values[hold_pos/3] = new Hold(arvot[hold_pos]);
                hold_pos++;
                all_hold_values[hold_pos/3].setHoldCoordinates(res.getIntArray(R.array.tension_coordinates));
                all_hold_values[hold_pos/3].setHoldValue(arvot[hold_pos]);
                hold_pos++;
                all_hold_values[hold_pos/3].setGripTypeAndSingleHang(arvot[hold_pos]);
                hold_pos++;
            }
        }

        else if (current_board == CustomSwipeAdapter.hangboard.ZLAG ) {
            int[] arvot = res.getIntArray(R.array.grip_values_zlag);

            all_hold_values = new Hold[arvot.length/3];

            while (hold_pos/3 < all_hold_values.length) {
                all_hold_values[hold_pos/3] = new Hold(arvot[hold_pos]);
                hold_pos++;
                all_hold_values[hold_pos/3].setHoldCoordinates(res.getIntArray(R.array.zlag_coordinates));
                all_hold_values[hold_pos/3].setHoldValue(arvot[hold_pos]);
                hold_pos++;
                all_hold_values[hold_pos/3].setGripTypeAndSingleHang(arvot[hold_pos]);
                hold_pos++;
            }
        }

        else if (current_board == CustomSwipeAdapter.hangboard.MOONHARD ) {
            int[] arvot = res.getIntArray(R.array.grip_values_moonhard);

            all_hold_values = new Hold[arvot.length/3];

            while (hold_pos/3 < all_hold_values.length) {
                all_hold_values[hold_pos/3] = new Hold(arvot[hold_pos]);
                hold_pos++;
                all_hold_values[hold_pos/3].setHoldCoordinates(res.getIntArray(R.array.moonhard_coordinates));
                all_hold_values[hold_pos/3].setHoldValue(arvot[hold_pos]);
                hold_pos++;
                all_hold_values[hold_pos/3].setGripTypeAndSingleHang(arvot[hold_pos]);
                hold_pos++;
            }
        }

        else if (current_board == CustomSwipeAdapter.hangboard.MOONEASY ) {
            int[] arvot = res.getIntArray(R.array.grip_values_mooneasy);

            all_hold_values = new Hold[arvot.length/3];

            while (hold_pos/3 < all_hold_values.length) {
                all_hold_values[hold_pos/3] = new Hold(arvot[hold_pos]);
                hold_pos++;
                all_hold_values[hold_pos/3].setHoldCoordinates(res.getIntArray(R.array.mooneasy_coordinates));
                all_hold_values[hold_pos/3].setHoldValue(arvot[hold_pos]);
                hold_pos++;
                all_hold_values[hold_pos/3].setGripTypeAndSingleHang(arvot[hold_pos]);
                hold_pos++;
            }
        }
        else if (current_board == CustomSwipeAdapter.hangboard.METO ) {
            int[] arvot = res.getIntArray(R.array.grip_values_meto);

            all_hold_values = new Hold[arvot.length/3];

            while (hold_pos/3 < all_hold_values.length) {
                all_hold_values[hold_pos/3] = new Hold(arvot[hold_pos]);
                hold_pos++;
                all_hold_values[hold_pos/3].setHoldCoordinates(res.getIntArray(R.array.meto_coordinates));
                all_hold_values[hold_pos/3].setHoldValue(arvot[hold_pos]);
                hold_pos++;
                all_hold_values[hold_pos/3].setGripTypeAndSingleHang(arvot[hold_pos]);
                hold_pos++;
            }
        }

        else if (current_board == CustomSwipeAdapter.hangboard.ROCKPRODIGY ) {
            int[] arvot = res.getIntArray(R.array.grip_values_rockprodigy);

            all_hold_values = new Hold[arvot.length/3];

            while (hold_pos/3 < all_hold_values.length) {
                all_hold_values[hold_pos/3] = new Hold(arvot[hold_pos]);
                hold_pos++;
                all_hold_values[hold_pos/3].setHoldCoordinates(res.getIntArray(R.array.rockprodigy_coordinates));
                all_hold_values[hold_pos/3].setHoldValue(arvot[hold_pos]);
                hold_pos++;
                all_hold_values[hold_pos/3].setGripTypeAndSingleHang(arvot[hold_pos]);
                hold_pos++;
            }
        }
        else if (current_board == CustomSwipeAdapter.hangboard.PROBLEMSOLVER ) {
            int[] arvot = res.getIntArray(R.array.grip_values_problemsolver);

            all_hold_values = new Hold[arvot.length/3];

            while (hold_pos/3 < all_hold_values.length) {
                all_hold_values[hold_pos/3] = new Hold(arvot[hold_pos]);
                hold_pos++;
                all_hold_values[hold_pos/3].setHoldCoordinates(res.getIntArray(R.array.problemsolver_coordinates));
                all_hold_values[hold_pos/3].setHoldValue(arvot[hold_pos]);
                hold_pos++;
                all_hold_values[hold_pos/3].setGripTypeAndSingleHang(arvot[hold_pos]);
                hold_pos++;
            }
        }

        else if (current_board == CustomSwipeAdapter.hangboard.METO_CONTACT ) {
            int[] arvot = res.getIntArray(R.array.grip_values_meto_contact);

            all_hold_values = new Hold[arvot.length/3];

            while (hold_pos/3 < all_hold_values.length) {
                all_hold_values[hold_pos/3] = new Hold(arvot[hold_pos]);
                hold_pos++;
                all_hold_values[hold_pos/3].setHoldCoordinates(res.getIntArray(R.array.meto_contact_coordinates));
                all_hold_values[hold_pos/3].setHoldValue(arvot[hold_pos]);
                hold_pos++;
                all_hold_values[hold_pos/3].setGripTypeAndSingleHang(arvot[hold_pos]);
                hold_pos++;
            }
        }

        else if (current_board == CustomSwipeAdapter.hangboard.METO_WOOD ) {
            int[] arvot = res.getIntArray(R.array.grip_values_meto_wood);

            all_hold_values = new Hold[arvot.length/3];

            while (hold_pos/3 < all_hold_values.length) {
                all_hold_values[hold_pos/3] = new Hold(arvot[hold_pos]);
                hold_pos++;
                all_hold_values[hold_pos/3].setHoldCoordinates(res.getIntArray(R.array.meto_wood_coordinates));
                all_hold_values[hold_pos/3].setHoldValue(arvot[hold_pos]);
                hold_pos++;
                all_hold_values[hold_pos/3].setGripTypeAndSingleHang(arvot[hold_pos]);
                hold_pos++;
            }
        }

        // The positions must be randomized so that GiveHoldWithValue method
        // doesn't favor one hold above the other (next)
        Hold temp;
        int index;
        Random random = new Random();
        for (int i = all_hold_values.length - 1; i > 0; i--)
        {
            index = random.nextInt(i + 1);
            temp = all_hold_values[index];
            all_hold_values[index] = all_hold_values[i];
            all_hold_values[i] = temp;
        }
        setGrips(0);

    }

    // Arbitary grade values, what hold_values to search in a give grade
    // For example Grade 6c consist of hold that are betweent 7 and 18 in difficulty
    private static int getMinValue(String grade) {
        if (grade.equals("5a")) {return 1;}
        else if (grade.equals("5b")) {return 2;}
        else if (grade.equals("5c")) {return 3;}
        else if (grade.equals("6a")) {return 4;}
        else if (grade.equals("6b")) {return 5;}
        else if (grade.equals("6c")) {return 7;}
        else if (grade.equals("7a")) {return 9;}
        else if (grade.equals("7b")) {return 14;}
        else if (grade.equals("7c")) {return 18;}
        else if (grade.equals("8a")) {return 29;}
        else if (grade.equals("8b")) {return 49;}
        return 1;
    }
    private static int getMaxValue(String grade) {
        if (grade.equals("5a")) {return 3;}
        else if (grade.equals("5b")) {return 5;}
        else if (grade.equals("5c")) {return 7;}
        else if (grade.equals("6a")) {return 10;}
        else if (grade.equals("6b")) {return 15;}
        else if (grade.equals("6c")) {return 18;}
        else if (grade.equals("7a")) {return 25;}
        else if (grade.equals("7b")) {return 35;}
        else if (grade.equals("7c")) {return 120;}
        else if (grade.equals("8a")) {return 200;}
        else if (grade.equals("8b")) {return 500;}
        return 1;
    }

}
