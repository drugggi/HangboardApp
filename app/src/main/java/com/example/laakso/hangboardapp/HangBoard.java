package com.example.laakso.hangboardapp;

import android.content.res.Resources;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

/**
 * Created by Laakso on 16.11.2017.
 */

public class HangBoard {

    private String[] grades;
    CustomSwipeAdapter.hangboard current_board;

    // All possible grip types in a hangboard
    private HoldValue[] all_hold_values;

    // starter_grips holds the information of all starting holds and grips of every grade
    private String[] starter_grips;

    // holdList represents the changeable holds that user will modify and are give to workoutactivity
    ArrayList<String> holdList;
    ArrayList<HoldValue> valueList;

    // Hold coordinates stores the information where hand pictures will be placed on a hangboard image
    private int[] hold_coordinates;

    // Grips constructor takes resources so that it can read all the information needed constructing
    // workout and hangs and grips
    public HangBoard(Resources res) {
        starter_grips = res.getStringArray(R.array.beastmaker1000);
        grades = res.getStringArray(R.array.grades);
        current_board = CustomSwipeAdapter.hangboard.BM1000;
        hold_coordinates = res.getIntArray(R.array.bm1000_coordinates);

        holdList = new ArrayList<String>();
        valueList = new ArrayList<HoldValue>();

    }

    public int getLeftFingerImage(int position) {
        return valueList.get(position*2).getGripImage(true);
    }
    public int getRightFingerImage(int position) {
        return valueList.get(position*2+1).getGripImage(false);
    }

    // Valuelist keeps track what holds and grips is currently used in a program
    // even values has the left hand information and odd values  right hand information
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

    public int[] getCoordinates() {
        return hold_coordinates;
    }

    public void NewBoard(Resources res,CustomSwipeAdapter.hangboard new_board) {
        if (CustomSwipeAdapter.hangboard.BM1000 == new_board) {
            current_board = CustomSwipeAdapter.hangboard.BM1000;
            starter_grips = res.getStringArray(R.array.beastmaker1000);
            hold_coordinates = res.getIntArray(R.array.bm1000_coordinates);
        }

        else if (CustomSwipeAdapter.hangboard.BM2000 == new_board) {
            current_board = CustomSwipeAdapter.hangboard.BM2000;
            starter_grips = res.getStringArray(R.array.beastmaker2000);
            hold_coordinates = res.getIntArray(R.array.bm2000_coordinates);
        }

        else if (CustomSwipeAdapter.hangboard.TRANS == new_board) {
            current_board = CustomSwipeAdapter.hangboard.TRANS;
            starter_grips = res.getStringArray(R.array.no_start_program);
            hold_coordinates = res.getIntArray(R.array.trans_coordinates);
        }

        else if (CustomSwipeAdapter.hangboard.ZLAG == new_board) {
            current_board = CustomSwipeAdapter.hangboard.ZLAG;
            starter_grips = res.getStringArray(R.array.no_start_program);
            hold_coordinates = res.getIntArray(R.array.zlag_coordinates);
        }

        else if (CustomSwipeAdapter.hangboard.MOONHARD == new_board) {
            current_board = CustomSwipeAdapter.hangboard.MOONHARD;
            starter_grips = res.getStringArray(R.array.no_start_program);
            hold_coordinates = res.getIntArray(R.array.moonhard_coordinates);
        }

        else if (CustomSwipeAdapter.hangboard.TENSION == new_board) {
            current_board = CustomSwipeAdapter.hangboard.TENSION;
            starter_grips = res.getStringArray(R.array.no_start_program);
            hold_coordinates = res.getIntArray(R.array.tension_coordinates);
        }
        setGrips(0);

    }


    // Gives long String with holds and grips, position == grade
    public String getGrip(int position) {

        return starter_grips[position];
    }

    public ArrayList<String> GetGripList() {
        return holdList;
    }



    public String getGrade(int position) {
        return grades[position];
    }

    // Just converts List of string into Array of Strings
    public String[] getGrips() {
        String[] holds = holdList.toArray(new String[holdList.size()]);
        return holds;

    }


    // Sets holdList to match those info in String[] grips
    public String[] setGrips(int position) {

        holdList.clear();
         String[] holds;

            if (position >= starter_grips.length) {
                int i = 0;
                while (i < 6) {
                    holdList.add("No Example program available.\n Click Randomize ALL");
                    ++i;
                }
                return holdList.toArray(new String[holdList.size()]);
            }

            Scanner in = new Scanner(starter_grips[position]);

            // Lets put hang instruction to String table that will be presented as hangboard program goes on

            while (in.hasNextLine()) {
                holdList.add(in.nextLine());
                holdList.set(holdList.size() - 1, holdList.get(holdList.size() - 1).replace("grip", "\ngrip"));

            }
            holds = holdList.toArray(new String[holdList.size()]);

        return holds;
    }

    public int getHoldListSize() {
        return all_hold_values.length;
    }

    public void sortHoldByDifficulty() {

        Arrays.sort(all_hold_values);

        holdList.clear();
        valueList.clear();

        int i = 0;

        while (i < all_hold_values.length ) {
            if ( all_hold_values[i].isSingleHold() ) {i++; continue; }

            valueList.add(all_hold_values[i]);
            valueList.add(all_hold_values[i]);

            holdList.add(i + ". HOLD: " + all_hold_values[i].GetHoldNumber() +
                    "\nGRIP: " + all_hold_values[i].GetHoldText() +
                    "\nDifficulty: " + all_hold_values[i].GetHoldValue());
            i++;
        }
    }

    public String[] getGrades() {
        return grades;
    }


    public void setGripAmount(int amount, int grade_position) {
        holdList.clear();
        while (amount > 0) {
            holdList.add("No Example program available.\n Click Randomize ALL");
            --amount;
        }
        randomizeGrips(grade_position);

    }

    public void setGripsFromStart() {
        int i = 0;
        valueList.clear();

        while (i < holdList.size()) {
            valueList.add(all_hold_values[i]);
            i++;

            holdList.set(i, i + ". HOLD: " + all_hold_values[i].GetHoldNumber() +
                "\nGRIP: " + all_hold_values[i].GetHoldText() +
                "\nDifficulty: " + all_hold_values[i].GetHoldValue());
        }
    }

    // THIS RANDOMIZER ACTUALLY WORKS QUITE DECENTLY
    // Method randomizeGrips randomizes holds and grips that are used in a workout
    public void randomizeGrips(int grade_position) {
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
        while (i < holdList.size() ) {

            if (isAlternate) {

                // Lets search for a holds that max hardness is half the remaining points for a give grade
                random_nro = getHoldNumberWithValue(min_value, max_value );
                // And then search for a hold that could be slightly harder than the first one
                random_nro_alt = getHoldNumberWithValue(min_value , max_value*2, all_hold_values[random_nro].grip_style);

                // Holds should not be the same, if it is lets just find one hold ie. jump to else statement
                if (random_nro == random_nro_alt) { isAlternate = false; continue; }
                valueList.add(all_hold_values[random_nro]);
                valueList.add(all_hold_values[random_nro_alt]);
                holdList.set(i, i + ". HOLD: " + all_hold_values[random_nro].GetHoldNumber() + "/" + all_hold_values[random_nro_alt].GetHoldNumber() + "\nGRIP: " +
                        all_hold_values[random_nro].GetHoldText() + " alternate\n Difficulty: "+
                                (all_hold_values[random_nro].GetHoldValue() + all_hold_values[random_nro_alt].GetHoldValue() )/2);

            }

            else {
                // Lets search for a hold that max hardness is half the remaining points for a give grade
                random_nro = getHoldNumberWithValue(min_value, max_value);

                value = value + all_hold_values[random_nro].GetHoldValue();
                valueList.add(all_hold_values[random_nro]);
                valueList.add(all_hold_values[random_nro]);
                holdList.set(i, i + ". HOLD: " + all_hold_values[random_nro].GetHoldNumber() + "\nGRIP: " + all_hold_values[random_nro].GetHoldText() +
                        "\nDifficulty: " + all_hold_values[random_nro].GetHoldValue() );

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

                holdList.set(hold_nro, hold_nro + ". HOLD: " + all_hold_values[random_nro].GetHoldNumber() + "/"
                        + all_hold_values[random_nro_alt].GetHoldNumber() + "\nGRIP: " +
                        all_hold_values[random_nro].GetHoldText() + " alternate\n Difficulty: "+
                        (all_hold_values[random_nro].GetHoldValue() + all_hold_values[random_nro_alt].GetHoldValue() )/2);

            }

        if ( !isAlternate ) {
                // Lets search for a hold that max hardness is half the remaining points for a give grade
                random_nro = getHoldNumberWithValue(min_value, max_value);

                value = value + all_hold_values[random_nro].GetHoldValue();

            valueList.set(hold_nro*2, all_hold_values[random_nro]);
            valueList.set(hold_nro*2 + 1,all_hold_values[random_nro]);

                holdList.set(hold_nro, hold_nro +  ". HOLD: " + all_hold_values[random_nro].GetHoldNumber() +
                        "\nGRIP: " + all_hold_values[random_nro].GetHoldText() +
                        "\nDifficulty: " + all_hold_values[random_nro].GetHoldValue() );

            }
    }


    // MUUTA TUPLAKIERROS HOLDVALUE.SIZE() JOTTA OTETYYPPEJÄ KÄYDÄÄN LÄPI SEN VERRAN MITÄ NIITÄ ON
    // TALLENNETTU. EIKÄ VAKIO 36 MÄÄRÄ
    private int getHoldNumberWithValue(int min_value, int max_value, HoldValue.grip_type wanted_hold) {

        Random rng = new Random();
        int search_point = rng.nextInt(all_hold_values.length);
        int tuplakierros = 0;

        while ( all_hold_values[search_point].GetHoldValue() < min_value ||
                all_hold_values[search_point].GetHoldValue() > max_value ||
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
        while ( all_hold_values[search_point].GetHoldValue() < min_value ||
                all_hold_values[search_point].GetHoldValue() > max_value ||
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
    public void InitializeHolds(Resources res) {

        int hold_pos = 0;
        if (current_board == CustomSwipeAdapter.hangboard.BM1000) {
            int[] arvot = res.getIntArray(R.array.grip_values_bm1000);

            all_hold_values = new HoldValue[arvot.length/3];

            while (hold_pos/3 < all_hold_values.length) {
                all_hold_values[hold_pos/3] = new HoldValue(arvot[hold_pos]);
                hold_pos++;
                all_hold_values[hold_pos/3].setHoldCoordinates(res.getIntArray(R.array.bm1000_coordinates));
                all_hold_values[hold_pos/3].SetHoldValue(arvot[hold_pos]);
                hold_pos++;
                all_hold_values[hold_pos/3].SetGripTypeAndSingleHang(arvot[hold_pos]);
                hold_pos++;
            }
        }

        else if (current_board == CustomSwipeAdapter.hangboard.BM2000 ) {
            int[] arvot = res.getIntArray(R.array.grip_values_bm2000);

            all_hold_values = new HoldValue[arvot.length/3];

            while (hold_pos/3 < all_hold_values.length) {
                all_hold_values[hold_pos/3] = new HoldValue(arvot[hold_pos]);
                hold_pos++;
                all_hold_values[hold_pos/3].setHoldCoordinates(res.getIntArray(R.array.bm2000_coordinates));
                all_hold_values[hold_pos/3].SetHoldValue(arvot[hold_pos]);
                hold_pos++;
                all_hold_values[hold_pos/3].SetGripTypeAndSingleHang(arvot[hold_pos]);
                hold_pos++;
            }
        }

        else if (current_board == CustomSwipeAdapter.hangboard.TRANS ) {
            int[] arvot = res.getIntArray(R.array.grip_values_trans);

            all_hold_values = new HoldValue[arvot.length/3];

            while (hold_pos/3 < all_hold_values.length) {
                all_hold_values[hold_pos/3] = new HoldValue(arvot[hold_pos]);
                hold_pos++;
                all_hold_values[hold_pos/3].setHoldCoordinates(res.getIntArray(R.array.trans_coordinates));
                all_hold_values[hold_pos/3].SetHoldValue(arvot[hold_pos]);
                hold_pos++;
                all_hold_values[hold_pos/3].SetGripTypeAndSingleHang(arvot[hold_pos]);
                hold_pos++;
            }
        }

        else if (current_board == CustomSwipeAdapter.hangboard.TENSION ) {
            int[] arvot = res.getIntArray(R.array.grip_values_tension);

            all_hold_values = new HoldValue[arvot.length/3];

            while (hold_pos/3 < all_hold_values.length) {
                all_hold_values[hold_pos/3] = new HoldValue(arvot[hold_pos]);
                hold_pos++;
                all_hold_values[hold_pos/3].setHoldCoordinates(res.getIntArray(R.array.tension_coordinates));
                all_hold_values[hold_pos/3].SetHoldValue(arvot[hold_pos]);
                hold_pos++;
                all_hold_values[hold_pos/3].SetGripTypeAndSingleHang(arvot[hold_pos]);
                hold_pos++;
            }
        }

        else if (current_board == CustomSwipeAdapter.hangboard.ZLAG ) {
            int[] arvot = res.getIntArray(R.array.grip_values_zlag);

            all_hold_values = new HoldValue[arvot.length/3];

            while (hold_pos/3 < all_hold_values.length) {
                all_hold_values[hold_pos/3] = new HoldValue(arvot[hold_pos]);
                hold_pos++;
                all_hold_values[hold_pos/3].setHoldCoordinates(res.getIntArray(R.array.zlag_coordinates));
                all_hold_values[hold_pos/3].SetHoldValue(arvot[hold_pos]);
                hold_pos++;
                all_hold_values[hold_pos/3].SetGripTypeAndSingleHang(arvot[hold_pos]);
                hold_pos++;
            }
        }

        else if (current_board == CustomSwipeAdapter.hangboard.MOONHARD ) {
            int[] arvot = res.getIntArray(R.array.grip_values_moonhard);

            all_hold_values = new HoldValue[arvot.length/3];

            while (hold_pos/3 < all_hold_values.length) {
                all_hold_values[hold_pos/3] = new HoldValue(arvot[hold_pos]);
                hold_pos++;
                all_hold_values[hold_pos/3].setHoldCoordinates(res.getIntArray(R.array.moonhard_coordinates));
                all_hold_values[hold_pos/3].SetHoldValue(arvot[hold_pos]);
                hold_pos++;
                all_hold_values[hold_pos/3].SetGripTypeAndSingleHang(arvot[hold_pos]);
                hold_pos++;
            }
        }

        // The positions must sadly be randomized so that GiveHoldWithValue method
        // doesn't favor one hold above the other (next)
        HoldValue temp;
        int index;
        Random random = new Random();
        for (int i = all_hold_values.length - 1; i > 0; i--)
        {
            index = random.nextInt(i + 1);
            temp = all_hold_values[index];
            all_hold_values[index] = all_hold_values[i];
            all_hold_values[i] = temp;
        }

    }

    /*
    // InitializeHolds method collects from resources all the possible grip, holds and difficulty
    // in a give hangboard that can be applied in a hangboard workout
    public void InitializeHolds2(Resources res) {
        int position = 0;

        if (current_board == CustomSwipeAdapter.hangboard.BM1000) {

            int[] arvot = res.getIntArray(R.array.grip_values_bm1000);
            all_hold_values = new HoldValue[arvot.length];


            while (position < arvot.length) {

                // This sets the hold number to match the picture hold number
                // each hold number consist of three different hanging possibility
                all_hold_values[position] = new HoldValue((position + 3) / 3);
                all_hold_values[position].SetHoldValue(arvot[position]);

                if (all_hold_values[position].getHoldNumber() == 9) {
                    all_hold_values[position].setAsSingleHold(true);
                }

                // These holds are slopers and crimps
                if (position % 3 == 0) {
                    all_hold_values[position].SetGripType(HoldValue.grip_type.FOUR_FINGER);
                }
                if (position % 3 == 1) {
                    all_hold_values[position].SetGripType(HoldValue.grip_type.THREE_FRONT);
                }
                if (position % 3 == 2) {
                    all_hold_values[position].SetGripType(HoldValue.grip_type.THREE_BACK);
                }
                // these hold are two finger pockets (7 and 11)
                if (position == 18 || position == 30) {
                    all_hold_values[position].SetGripType(HoldValue.grip_type.TWO_FRONT);
                }
                if (position == 19 || position == 31) {
                    all_hold_values[position].SetGripType(HoldValue.grip_type.TWO_MIDDLE);
                }
                if (position == 20 || position == 32) {
                    all_hold_values[position].SetGripType(HoldValue.grip_type.TWO_BACK);
                }
                ++position;
            }
        }

        if (current_board == CustomSwipeAdapter.hangboard.BM2000) {

            int[] arvot = res.getIntArray(R.array.grip_values_bm2000);
            all_hold_values = new HoldValue[arvot.length];


            while (position < arvot.length) {

                all_hold_values[position] = new HoldValue((position + 3) / 3);
                all_hold_values[position].SetHoldValue(arvot[position]);

                if (all_hold_values[position].getHoldNumber() == 4 || all_hold_values[position].getHoldNumber() == 5 ||
                        all_hold_values[position].getHoldNumber() == 10 || all_hold_values[position].getHoldNumber() == 15) {
                    all_hold_values[position].setAsSingleHold(true);
                }

                if (position % 3 == 0) {
                    all_hold_values[position].SetGripType(HoldValue.grip_type.FOUR_FINGER);
                }
                if (position % 3 == 1) {
                    all_hold_values[position].SetGripType(HoldValue.grip_type.THREE_FRONT);
                }
                if (position % 3 == 2) {
                    all_hold_values[position].SetGripType(HoldValue.grip_type.THREE_BACK);
                }
                // these holds are monos (7 and 12)
                if (position == 18 || position == 33) {
                    all_hold_values[position].SetGripType(HoldValue.grip_type.INDEX_FINGER);
                }
                if (position == 19 || position == 34) {
                    all_hold_values[position].SetGripType(HoldValue.grip_type.MIDDLE_FINGER);
                }
                if (position == 20 || position == 35) {
                    all_hold_values[position].SetGripType(HoldValue.grip_type.RING_FINGER);
                }

                // these holds are two finger pockets (8, 9, 13, 14)
                if (position == 21 || position == 24 || position == 36 || position == 39) {
                    all_hold_values[position].SetGripType(HoldValue.grip_type.TWO_FRONT);
                }
                if (position == 22 || position == 25 || position == 37 || position == 40) {
                    all_hold_values[position].SetGripType(HoldValue.grip_type.TWO_MIDDLE);
                }
                if (position == 23 || position == 26|| position == 38 || position == 41) {
                    all_hold_values[position].SetGripType(HoldValue.grip_type.TWO_BACK);
                }

                ++position;
            }
        }

        if (current_board == CustomSwipeAdapter.hangboard.TRANS) {

            int[] arvot = res.getIntArray(R.array.grip_values_trans);
            all_hold_values = new HoldValue[arvot.length];


            while (position < arvot.length) {

                all_hold_values[position] = new HoldValue((position + 3) / 3);
                all_hold_values[position].SetHoldValue(arvot[position]);
                if (position % 3 == 0) {
                    all_hold_values[position].SetGripType(HoldValue.grip_type.FOUR_FINGER);
                }
                if (position % 3 == 1) {
                    all_hold_values[position].SetGripType(HoldValue.grip_type.THREE_FRONT);
                }
                if (position % 3 == 2) {
                    all_hold_values[position].SetGripType(HoldValue.grip_type.THREE_BACK);
                }

                if (position % 3 == 0 && position > 23) {
                    all_hold_values[position].SetGripType(HoldValue.grip_type.TWO_FRONT);
                }
                if (position % 3 == 1 && position > 23) {
                    all_hold_values[position].SetGripType(HoldValue.grip_type.TWO_MIDDLE);
                }
                if (position %3 == 2 && position > 23) {
                    all_hold_values[position].SetGripType(HoldValue.grip_type.TWO_BACK);
                }

                ++position;
            }
        }

    }

    */

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
