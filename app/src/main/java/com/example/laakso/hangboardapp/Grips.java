package com.example.laakso.hangboardapp;

import android.content.res.Resources;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

/**
 * Created by Laakso on 16.11.2017.
 */

public class Grips {

    private String[] grades;

    // All possible grip types in a hangboard
    private HoldValue[] all_hold_values;

    // starter_grips holds the information of all starting holds and grips of every grade
    private String[] starter_grips;

    // holdList represents the changeable holds that user will modify and are give to workoutactivity
    ArrayList<String> holdList;

    // Grips constructor takes resources so that it can read all the information needed constructing
    // workout and hangs and grips
    public Grips(Resources res) {
        starter_grips = res.getStringArray(R.array.beastmaker1000);
        grades = res.getStringArray(R.array.grades);

        holdList = new ArrayList<String>();

    }
    public void NewBoard(Resources res,CustomSwipeAdapter.hangboards new_board , int position ) {
        if (position == 0) {
            starter_grips = res.getStringArray(R.array.beastmaker1000);
        }

        if (position == 1) {
            starter_grips = res.getStringArray(R.array.beastmaker2000);
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


    public String[] getGrades() {

        return grades;
    }

    // THIS RANDOMIZER ACTUALLY WORKS QUITE DECENTLY
    // Method randomizeGrips randomizes holds and grips that are used in a workout
    public void randomizeGrips(int position) {

        // Random generator that is only used if we are using the same hold or alternating between holds
        Random rn = new Random();
        boolean isAlternate = rn.nextBoolean();

        // grips[position] = "";

        // these ints will be randomized and those represents holds in all_hold_values array
        int random_nro;
        int random_nro_alt;
        // String kaijutus="";

        int min_value=1;
        int max_value=3;

        // Each hold has value which represent how hard it is to hang. The harder the grade
        // the bigger values are needed so that holds are harder enough for the grade
        if (grades[position].equals("5a")) {min_value =1; max_value = 3; }
        else if (grades[position].equals("5b")) {min_value =2; max_value = 5; }
        else if (grades[position].equals("5c")) {min_value =3; max_value = 7; }
        else if (grades[position].equals("6a")) {min_value =4; max_value = 10; }
        else if (grades[position].equals("6b")) {min_value =5; max_value = 15; }
        else if (grades[position].equals("6c")) {min_value =7; max_value = 18; }
        else if (grades[position].equals("7a")) {min_value =9; max_value = 25; }
        else if (grades[position].equals("7b")) {min_value =14; max_value = 35; }
        else if (grades[position].equals("7b+")) {min_value =16; max_value = 48; }
        else if (grades[position].equals("7c")) {min_value =18; max_value = 120; }
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

/*
                // first is the hold
                grips[position] = grips[position] + "hold: " + all_hold_values[random_nro].GetHoldNumber()
                        + "/" + all_hold_values[random_nro_alt].GetHoldNumber() + " grip: ";

                value = value + (all_hold_values[random_nro].GetHoldValue() + all_hold_values[random_nro_alt].GetHoldValue() )/2;

                // then the grip
                grips[position] = grips[position] + all_hold_values[random_nro].GetHoldText() + " Alternate. H: "+
                        (all_hold_values[random_nro].GetHoldValue() + all_hold_values[random_nro_alt].GetHoldValue() )/2 + "\n";
*/

                holdList.set(i, "HOLD: " + all_hold_values[random_nro].GetHoldNumber() + "/" + all_hold_values[random_nro_alt].GetHoldNumber() + "\nGRIP: " +
                        all_hold_values[random_nro].GetHoldText() + " alternate\n Difficulty: "+
                                (all_hold_values[random_nro].GetHoldValue() + all_hold_values[random_nro_alt].GetHoldValue() )/2);



            }

            else {
                // Lets search for a hold that max hardness is half the remaining points for a give grade
                random_nro = getHoldNumberWithValue(min_value, max_value);

                value = value + all_hold_values[random_nro].GetHoldValue();
/*
                // first is the hold
                grips[position] = grips[position] + "hold: " + all_hold_values[random_nro].GetHoldNumber() + " grip: ";

                // then the grip
                grips[position] = grips[position] + all_hold_values[random_nro].GetHoldText() +
                        " H: " + all_hold_values[random_nro].GetHoldValue() + "\n";
*/
                holdList.set(i, "HOLD: " + all_hold_values[random_nro].GetHoldNumber() + "\nGRIP: " + all_hold_values[random_nro].GetHoldText() +
                        "\nDifficulty: " + all_hold_values[random_nro].GetHoldValue() );

            }
            isAlternate = rn.nextBoolean();
            ++i;
        }

        //kaijutus = all_hold_values[j].GetHoldNumber() + " " + all_hold_values[j].GetHoldText()
        //        + " value: " + all_hold_values[j].GetHoldValue() +  " random nro: "+ random_nro;
        return;
    }

    public void randomizeGrip(int position, int hold_nro) {

        // Random generator that is only used if we are using the same hold or alternating between holds
        Random rn = new Random();
        boolean isAlternate = rn.nextBoolean();


        // these ints will be randomized and those represents holds in all_hold_values array
        int random_nro;
        int random_nro_alt;

        int min_value=1;
        int max_value=3;

        // Each hold has value which represent how hard it is to hang. The harder the grade
        // the bigger values are needed so that holds are harder enough for the grade
        if (grades[position].equals("5a")) {min_value =1; max_value = 3; }
        else if (grades[position].equals("5b")) {min_value =2; max_value = 5; }
        else if (grades[position].equals("5c")) {min_value =3; max_value = 7; }
        else if (grades[position].equals("6a")) {min_value =4; max_value = 10; }
        else if (grades[position].equals("6b")) {min_value =5; max_value = 15; }
        else if (grades[position].equals("6c")) {min_value =7; max_value = 18; }
        else if (grades[position].equals("7a")) {min_value =9; max_value = 25; }
        else if (grades[position].equals("7b")) {min_value =14; max_value = 35; }
        else if (grades[position].equals("7b+")) {min_value =16; max_value = 48; }
        else if (grades[position].equals("7c")) {min_value =18; max_value = 120; }
        int value = 0;

        if (isAlternate) {

                // Lets search for a holds that max hardness is half the remaining points for a give grade
                random_nro = getHoldNumberWithValue(min_value, max_value );
                // And then search for a hold that could be slightly harder than the first one
                random_nro_alt = getHoldNumberWithValue(min_value , max_value*2, all_hold_values[random_nro].grip_style);

                // Holds should not be the same, if it is lets make sure next if statement is true
                if (random_nro == random_nro_alt) { isAlternate = false; }


                holdList.set(hold_nro, "HOLD: " + all_hold_values[random_nro].GetHoldNumber() + "/" + all_hold_values[random_nro_alt].GetHoldNumber() + "\nGRIP: " +
                        all_hold_values[random_nro].GetHoldText() + " alternate\n Difficulty: "+
                        (all_hold_values[random_nro].GetHoldValue() + all_hold_values[random_nro_alt].GetHoldValue() )/2);

            }

        if ( !isAlternate ) {
                // Lets search for a hold that max hardness is half the remaining points for a give grade
                random_nro = getHoldNumberWithValue(min_value, max_value);

                value = value + all_hold_values[random_nro].GetHoldValue();

                holdList.set(hold_nro, "HOLD: " + all_hold_values[random_nro].GetHoldNumber() + "\nGRIP: " + all_hold_values[random_nro].GetHoldText() +
                        "\nDifficulty: " + all_hold_values[random_nro].GetHoldValue() );

            }

        return;
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
            if (tuplakierros > 36) {return 0;}
        }
        return search_point;
    }

    private int getHoldNumberWithValue(int min_value, int max_value) {
        Random rng = new Random();
        int search_point = rng.nextInt(all_hold_values.length);
        int tuplakierros = 0;

        while ( all_hold_values[search_point].GetHoldValue() < min_value ||
                all_hold_values[search_point].GetHoldValue() > max_value ||
                all_hold_values[search_point].GetHoldNumber() == 9) {

            ++search_point;
            ++tuplakierros;
            if (search_point == all_hold_values.length) { search_point = 0; }
            if (tuplakierros > 36) {return 0;}
        }
        return search_point;
    }

    public void InitializeHolds(Resources res) {

        int position = 0;
        int[] arvot = res.getIntArray(R.array.grip_values);
        all_hold_values = new HoldValue[arvot.length];


        while ( position < arvot.length ) {

            all_hold_values[position] = new HoldValue((position+3) /3 );
            all_hold_values[position].SetHoldValue(arvot[position]);
            if ( position%3 == 0 ) {all_hold_values[position].SetGripType(HoldValue.grip_type.FOUR_FINGER);}
            if ( position%3 == 1 ) {all_hold_values[position].SetGripType(HoldValue.grip_type.THREE_FRONT);}
            if ( position%3 == 2 ) {all_hold_values[position].SetGripType(HoldValue.grip_type.THREE_BACK);}

            if (position == 18 || position == 30) {all_hold_values[position].SetGripType(HoldValue.grip_type.TWO_FRONT);}
            if (position == 19 || position == 31) {all_hold_values[position].SetGripType(HoldValue.grip_type.TWO_MIDDLE);}
            if (position == 20 || position == 32) {all_hold_values[position].SetGripType(HoldValue.grip_type.TWO_BACK);}

            ++position;
        }

    }

}
