package com.finn.laakso.hangboardapp;

// HangboardResources manages Hangboard images, image resources, hold values and coordinates
// name conversion between enums and strings etc.

import android.util.Log;

public final class  HangboardResources {
    private static int[] image_resources = {R.drawable.lauta1011, R.drawable.lauta2002, R.drawable.trans,
            R.drawable.tension, R.drawable.zlag, R.drawable.moonhard, R.drawable.mooneasy, R.drawable.meto,
            R.drawable.rockprodigy, R.drawable.problemsolver, R.drawable.meto_contact, R.drawable.meto_wood,
            R.drawable.drcc,R.drawable.solution, R.drawable.edge, R.drawable.soillboost, R.drawable.ultimate,
            R.drawable.grill, R.drawable.grillto};


    private static int[] coordinate_resources = {R.array.bm1000_coordinates, R.array.bm2000_coordinates,
    R.array.trans_coordinates, R.array.tension_coordinates, R.array.zlag_coordinates, R.array.moonhard_coordinates,
            R.array.mooneasy_coordinates, R.array.meto_coordinates, R.array.rockprodigy_coordinates,
            R.array.problemsolver_coordinates, R.array.meto_contact_coordinates, R.array.meto_wood_coordinates,
            R.array.drcc_coordinates, R.array.solution_coordinates, R.array.edge_coordinates, R.array.soillboost_coordinates,
            R.array.ultimate_coordinates, R.array.grill_coordinates, R.array.grillto_coordinates};

    private static int[] hold_resources = {R.array.grip_values_bm1000, R.array.grip_values_bm2000,
            R.array.grip_values_trans, R.array.grip_values_tension, R.array.grip_values_zlag, R.array.grip_values_moonhard,
            R.array.grip_values_mooneasy, R.array.grip_values_meto, R.array.grip_values_rockprodigy,
            R.array.grip_values_problemsolver, R.array.grip_values_meto_contact, R.array.grip_values_meto_wood,
            R.array.grip_values_drcc, R.array.grip_values_solution, R.array.grip_values_edge, R.array.grip_values_soillboost,
            R.array.grip_values_ultimate, R.array.grip_values_grill, R.array.grip_values_grillto};

    private static String[] hangboardStrings = {"BM 1000", "BM 2000", "Transgression","Tension",
            "Zlagboard","Moonboard hard","Moonboard easy","Metolius","Rock Prodigy","problemsolver","Meto. Contact",
            "Meto. Wood","DRCC","Solution","Edge", "So iLL Boost","Ultimate", "Grill", "Grillto"};


    // All supported Hangboards
            public enum hangboardName {BM1000, BM2000, TRANS, TENSION, ZLAG, MOONHARD, MOONEASY, METO,
        ROCKPRODIGY, PROBLEMSOLVER, METO_CONTACT, METO_WOOD, DRCC, SOLUTION, EDGE, SOILLBOOST, ULTIMATE,
            GRILL, GRILLTO}

            public static void TESTallHangboardResourcesMethods() {

                hangboardName enumName;
                String stringName;
                int imageResource;
                int holdResources;
                int coordResources;

                for (int i = -2 ; i < getHangboardCount() + 1 ; i++) {
                    enumName = getHangboardName(i);
                    imageResource = getHangboardImageResource(i);
                    stringName = getHangboardStringName(enumName);
                    holdResources = getHoldValueResources(enumName);
                    coordResources = getHoldValueResources(enumName);

                    if (imageResource != getHangboardImageResource(stringName)) {
                        Log.e("ERROR","IMAGERESOURCE DIFFERENT");
                    }
                    Log.d("TESThangboardresources","i: " + i + " enum: " + enumName.toString() + " string: " + stringName
                            + " imgRes: " + imageResource + " holdRes: " + holdResources + " coordRes: " + coordResources);
                }

            }

     private HangboardResources() {



        }

        public static int getHoldValueResources(hangboardName hangboard) {
                int position = hangboard.ordinal();
              //  Log.d("HoldValueResources","HANGBOARD: " +hangboard.toString() + "  pos: " + position);
                if (position >= 0 && position < hold_resources.length) {
                    return hold_resources[position];
                }
            Log.e("ERR","ERROR getholdvalueresources");
                return hold_resources[0];
        }

        public static int getHoldCoordinates(hangboardName hangboard) {
                int position = hangboard.ordinal();
              //  Log.d("HoldCoordinates","HANGBOARD: " +hangboard.toString() + "  pos: " + position);
                if(position >= 0 && position < coordinate_resources.length) {
                    return coordinate_resources[position];
                }
            Log.e("ERR","ERROR getholdcoordinates");
                return coordinate_resources[0];
        }

    public static hangboardName forInt(int id) {
        return hangboardName.values()[id-1];

    }

        public static int getHangboardCount() {
              //  Log.d("ALL SIZES","  " + image_resources.length + "   " + hangboardStrings.length + "   "
               //         + hangboardName.values().length +  "  " + hold_resources.length  + "   " + coordinate_resources.length );
                return image_resources.length;
        }

    // Converts PagerAdapter position into hangboard enum.
    public static hangboardName getHangboardName(int position) {


                if (position >= 0 && position < hangboardName.values().length ) {
                   // Log.d("getHnagboardName","HANGBOARD: " +hangboardName.values()[position].toString() +  "   pos: " + position);
                    return hangboardName.values()[position];
                }
                Log.e("ERR","ERROR getHangboardName");
                return hangboardName.BM1000;
    }

    public static int getHangboardImageResource(int position) {

                if (position >= 0 && position < image_resources.length) {
                    return image_resources[position];
                }
                Log.e("ERR","ERROR gethangboardimageresources");
                return R.drawable.lauta1011;
    }

    public static int getHangboardPosition(String HB) {

        for (int position = 0 ; position < hangboardStrings.length ; position++) {
            if (hangboardStrings[position].equals(HB )) {
                return position;
            }

        }
        return 0;
    }

    // Converts hangboardName enum into describing name. Should be somewhere else
    public static String getHangboardStringName(hangboardName HB) {

                int position = HB.ordinal();
                if (position >= 0 && position < hangboardStrings.length ) {
                  //  Log.d("hangboardstringname","HANGBOARD: " + HB.toString() + "   stringname: " + hangboardStrings[position] + "  pos: " + position);
                    return hangboardStrings[position];
                }
               // Log.e("ERR","ERROR gethangboardstringname");
                return hangboardStrings[0];

    }


    // Returns the hangboard picture resource int. Used to get picture when name is known and name
    // is stored into database.
    public static int getHangboardImageResource(String hangboardName) {

        for (int position = 0; position < hangboardStrings.length; position++) {
            if (hangboardStrings[position].equals(hangboardName)) {
                return image_resources[position];
            }
        }
        return image_resources[0];
    }

    // Converts hangboard image resource into describing name. This should be somewhere else.
    public static String getHangboardStringName(int HBresource) {

        for (int position = 0; position < image_resources.length; position++) {
            if (image_resources[position] == HBresource) {
                return hangboardStrings[position];
            }
        }
        return "Custom";

    }
}
