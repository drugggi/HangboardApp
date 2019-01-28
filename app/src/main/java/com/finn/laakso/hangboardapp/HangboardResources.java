package com.finn.laakso.hangboardapp;

// HangboardResources manages Hangboard images, image resources, hold values and coordinates
// name conversion between enums and strings etc.

public final class  HangboardResources {
/*
    // Some base values for holds based on millimeter depth from 6mm - 40mm and jug
    private static final int JUG = 1, MM40 = 2, MM35 = 3, MM32 = 4,MM30 = 5, MM28 = 6, MM25= 7,
            MM23= 8, MM21 = 9, MM20 = 10, MM18= 12,MM16 = 16, MM14 = 16, MM12 = 18, MM11 = 20,
            MM10= 24, MM9=35, MM8 = 50, MM7= 60, MM6=100;

    // Sloper difficulties based on angle degrees
    private static final int S20d = 3, S35d = 23, S45d = 100;

        // finger representation four finger, front three, back three, front two, middle two, back two, index
        // middle, ring, index and little finger
    private static final int FF = 10, F3=20, B3 = 30, F2= 40, M2 = 50, B2 = 60, IN = 70, MI = 80, RI = 90, LI = 100;
    private static final int FFS = 11, F3S=21, B3S = 31, F2S= 41, M2S =51, B2S = 61, INS = 71,
            MIS = 81, RIS = 91, LIS = 101;

    private static final int[] gripValues = {
            1,,FF, 1,,F3, 1,,B3, 1,,F2, 1,,M2, 1,,B2,
            2,,FF, 2,,F3, 2,,B3, 2,,F2, 2,,M2, 2,,B2,
            3,,FF, 3,,F3, 3,,B3, 3,,F2, 3,,M2, 3,,B2,
            4,,FF, 4,,F3, 4,,B3, 4,,F2, 4,,M2, 4,,B2,
            5,,FF, 5,,F3, 5,,B3, 5,,F2, 5,,M2, 5,,B2,
            6,,FF, 6,,F3, 6,,B3, 6,,F2, 6,,M2, 6,,B2,
            7,,FF, 7,,F3, 7,,B3, 7,,F2, 7,,M2, 7,,B2,
            8,,FF, 8,,F3, 8,,B3, 8,,F2, 8,,M2, 8,,B2,
            9,,FF, 9,,F3, 9,,B3, 9,,F2, 9,,M2, 9,,B2,
            10,,FF, 10,,F3, 10,,B3, 10,,F2, 10,,M2, 10,,B2,
            11,,FF, 11,,F3, 11,,B3, 11,,F2, 11,,M2, 11,,B2,
            12,,FF, 12,,F3, 12,,B3, 12,,F2, 12,,M2, 12,,B2,
            13,,FF, 13,,F3, 13,,B3, 13,,F2, 13,,M2, 13,,B2,

    };


private static final int[] gripValuesGrillto = {
        1,1,FF, 1,2,F3, 1,2,B3, 1,21,F2, 1,15,M2, 1,38,B2, 1,85,IN, 1,75,MI, 1,135,RI, 1,190,LI,
        2,1,FF, 2,2,F3, 2,3,B3, 2,17,F2, 2,13,M2, 2,35,B2,
        3,1,FFS, 3,2,F3S, 3,2,B3S,
        4,15,FFS, 4,23,F3S, 4,25,B3S,
        5,10,FF, 5,17,F3, 5,19,B3,
        6,8,FF, 6,16,F3, 6,18,B3, 6,27,F2, 6,24,M2, 6,45,B2,
        7,20,FF, 7,33,F3, 7,37,B3,
         8,23,F2, 8,18,M2, 8,37,B2,
         9,18,F3, 9,20,B3,
         10,31,F2, 10,27,M2, 10,55,B2,
        11,95,INS, 11,85,MIS, 11,165,RIS, 11,220,LIS
};

private static final int[] gripValuesGrill = {
        1,3,FF, 1,7,F3, 1,9,B3,
          2,85,IN, 2,75,MI, 2,135,RI, 2,190,LI,
        3,1,FF, 3,2,F3, 3,2,B3, 3,17,F2, 3,13,M2, 3,29,B2,
        4,2,FF, 4,4,F3, 4,6,B3, 4,23,F2, 4,17,M2, 4,35,B2,
        5,10,FF, 5,18,F3, 5,21,B3,
        6,20,FF, 6,29,F3, 6,33,B3,
        7,8,FF, 7,21,F3, 7,24,B3,
        8,15,FF, 8,35,F3, 8,45,B3,
        // 9,,PINCH not implemented yet
         10,25,F2, 10,21,M2, 10,45,B2,
         11,45,F2, 11,35,M2, 11,75,B2,
         12,37,F2, 12,32,M2, 12,65,B2,
        13,95,INS, 13,85,MIS, 13,165,RIS, 13,220,LIS,
};



private static final int[] gripValuesUltimate = {
        1,1,FF, 1,2,F3, 1,3,B3,
        2,2,FF, 2,3,F3, 2,4,B3,
        3,9,FF, 3,21,F3, 3,25,B3,
        4,5,FF, 4,11,F3, 4,13,B3, 4,40,F2, 4,25,M2, 4,60,B2,
        5,2,FFS, 5,3,F3S, 5,4,B3S, 5,21,F2S, 5,17,M2S, 5,35,B2S,
        6,10,FF, 6,17,F3, 6,20,B3, 6,80,F2, 6,50,M2, 6,90,B2,
        7,3,FFS, 7,5,F3S, 7,7,B3S, 7,26,F2S, 7,20,M2S, 7,45,B2S,
        8,19,FF, 8,43,F3, 8,46,B3,
        9,4,FFS, 9,10,F3S, 9,11,B3S, 9,35,F2S, 9,23,M2S, 9,55,B2S,
        10,50,FF, 10,95,F3, 10,105,B3,
        11,18,FFS, 11,40,F3S, 11,44,B3S,
        12,100,FF, 12,195,F3, 12,250,B3,
         13,19,F2, 13,14,M2, 13,29,B2,
         14,23,F2, 14,18,M2, 14,37,B2,
         15,35,F2, 15,25,M2, 15,55,B2,
         16,95,IN, 16,75,MI, 16,135,RI, 16,190,LI
};

private static final int[] gripValuesSoillboost = {
        1,1,FF, 1,2,F3, 1,3,B3,
        2,1,FF, 2,2,F3, 2,3,B3,
        3,5,FF, 3,13,F3, 3,16,B3,
         4,5,F3, 4,6,B3, 4,23,F2, 4,18,M2, 4,35,B2,
        5,90,IN, 5,65,MI, 5,135,RI, 5,180,LI,
        6,3,FF, 6,6,F3, 6,7,B3,
         7,9,F3, 7,11,B3, 7,32,F2, 7,25,M2, 7,45,B2,
         8,110,IN, 8,85,MI, 8,155,RI, 8,210,LI,
        9,5,FF, 9,10,F3, 9,12,B3,
            10,35,F2S, 10,29,M2S, 10,50,B2S,
        11,10,FF, 11,15,F3, 11,18,B3,
         12,45,F2, 12,34,M2, 12,65,B2,
         13,130,IN, 13,95,MI, 13,170,RI, 13,230,LI,
        14,12,FFS, 14,17,F3S, 14,19,B3S,
        15,15,FF, 15,25,F3, 15,29,B3,
        16,14,FF, 16,23,F3, 16,27,B3,
         17,55,F2, 17,45,M2, 17,78,B2
};
private static final int[] gripValuesSolution = {
        1,1,FF, 1,2,F3, 1,3,B3,
        2,25,FF, 2,35,F3, 2,39,B3,
        3,55,FF, 3,75,F3, 3,85,B3,
            4,39,F2, 4,30,M2, 4,55,B2,
        5,150,IN, 5,80,MI, 5,200,RI, 5,250,LI,
        6,15,FF, 6,28,F3, 6,32,B3,
            7,38,F2, 7,49,M2, 7,70,B2,
            8,23,F2S, 8,18,M2S, 8,37,B2S,
        9,6,FF, 9,8,F3, 9,9,B3,
            10,21,F2, 10,16,M2, 10,33,B2,
        11,10,FF, 11,15,F3, 11,18,B3,
        12,65,FF, 12,85,F3, 12,95,B3,
            13,110,IN, 13,65,MI, 13,130,RI, 13,175,LI,
        14,20,FF, 14,27,F3, 14,30,B3,
        15,8,FFS, 15,13,F3S, 15,16,B3S

};

  private static final int[] gripValuesEdge = {
          1,30,FF, 1,45,F3, 1,51,B3,
          2,18,10, 2,28,20, 2,32,30,
          3,8,FF, 3,18,F3, 3,23,B3,
          4,2,FF, 4,5,F3, 4,7,B3, 4,23,F2, 4,19,M2, 4,29,B2,
          5,8,FF, 5,11,F3, 5,13,B3, 5,35,F2, 5,28,M2, 5,69,B2,
          6,10,FF, 6,13,F3, 6,15,B3,
          7,11,FF, 7,14,F3, 7,16,B3,
          8,13,FF, 8,16,F3, 8,18,B3,
          9,15,FF, 9,17,F3, 9,20,B3,
          10,19,FF, 10,24,F3, 10,27,B3,
          11,22,FF, 11,28,F3, 11,30,B3,
          12,35,FF, 12,49,F3, 12,59,B3
  };



    private static final int[] bm1000coordinates = {
             1 , 12 , 15 , 279 , 15 ,
         2 , 67 , 32 , 214 , 32 ,
         3 , 119 , 27 , 165 , 27 ,
         4 , 5 , 52 , 279 , 52 ,
         5 , 115 , 52 , 165 , 52 ,
         6 , 3 , 73 , 283 , 73 ,
         7 , 47 , 73 , 233 , 73 ,
         8 , 88 , 73 , 195 , 73 ,
         9 , 142 , 73 , 141 , 73 ,
         10 , 31 , 102 , 251 , 102 ,
         11 , 80 , 102 , 203 , 102 ,
         12 , 119 , 102 , 162 , 102 ,
};
*/
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

    public static String[] getHangboardNames() {
        String[] hangboardNames = new String[hangboardStrings.length];

        System.arraycopy(hangboardStrings,0,hangboardNames,0,hangboardStrings.length);
/*
        for (int i = 0 ; i < hangboardNames.length ; i++) {
            hangboardNames[i] = hangboardStrings[i];
        }

        */
        return hangboardNames;
    }

    // All supported Hangboards
    public enum hangboardName {BM1000, BM2000, TRANS, TENSION, ZLAG, MOONHARD, MOONEASY, METO,
        ROCKPRODIGY, PROBLEMSOLVER, METO_CONTACT, METO_WOOD, DRCC, SOLUTION, EDGE, SOILLBOOST, ULTIMATE,
            GRILL, GRILLTO}
/*

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
*/

     private HangboardResources() {



        }

        public static int getHoldValueResources(hangboardName hangboard) {
                int position = hangboard.ordinal();
              //  Log.d("HoldValueResources","HANGBOARD: " +hangboard.toString() + "  pos: " + position);
                if (position >= 0 && position < hold_resources.length) {
                    return hold_resources[position];
                }
           // Log.e("ERR","ERROR getholdvalueresources");
                return hold_resources[0];
        }

        public static int getHoldCoordinates(hangboardName hangboard) {
                int position = hangboard.ordinal();
              //  Log.d("HoldCoordinates","HANGBOARD: " +hangboard.toString() + "  pos: " + position);
                if(position >= 0 && position < coordinate_resources.length) {
                    return coordinate_resources[position];
                }
           // Log.e("ERR","ERROR getholdcoordinates");
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
                //Log.e("ERR","ERROR getHangboardName");
                return hangboardName.BM1000;
    }

    public static int getHangboardImageResource(int position) {

                if (position >= 0 && position < image_resources.length) {
                    return image_resources[position];
                }
               // Log.e("ERR","ERROR gethangboardimageresources");
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
