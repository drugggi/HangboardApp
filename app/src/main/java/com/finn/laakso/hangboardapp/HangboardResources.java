package com.finn.laakso.hangboardapp;

// HangboardResources manages Hangboard images, image resources, hold values and coordinates
// name conversion between enums and strings etc.

public final class  HangboardResources {
    private static int[] image_resources = {R.drawable.lauta1011, R.drawable.lauta2002, R.drawable.trans,
            R.drawable.tension, R.drawable.zlag, R.drawable.moonhard, R.drawable.mooneasy, R.drawable.meto,
            R.drawable.rockprodigy, R.drawable.problemsolver, R.drawable.meto_contact, R.drawable.meto_wood,
            R.drawable.drcc,R.drawable.concept};


    private static int[] coordinate_resources = {R.array.bm1000_coordinates, R.array.bm2000_coordinates,
    R.array.trans_coordinates, R.array.tension_coordinates, R.array.zlag_coordinates, R.array.moonhard_coordinates,
            R.array.mooneasy_coordinates, R.array.meto_coordinates, R.array.rockprodigy_coordinates,
            R.array.problemsolver_coordinates, R.array.meto_contact_coordinates, R.array.meto_wood_coordinates,
            R.array.drcc_coordinates, R.array.concept_coordinates};

    private static int[] hold_resources = {R.array.grip_values_bm1000, R.array.grip_values_bm2000,
            R.array.grip_values_trans, R.array.grip_values_tension, R.array.grip_values_zlag, R.array.grip_values_moonhard,
            R.array.grip_values_mooneasy, R.array.grip_values_meto, R.array.grip_values_rockprodigy,
            R.array.grip_values_problemsolver, R.array.grip_values_meto_contact, R.array.grip_values_meto_wood,
            R.array.grip_values_drcc, R.array.grip_values_concept};

    private static String[] hangboardStrings = {"BM 1000", "BM 2000", "Transgression","Tension",
            "Zlagboard","Moonboard hard","Moonboard easy","Metolius","Rock Prodigy","problemsolver","Meto. Contact",
            "Meto. Wood","DRCC","Concept"};


    // All supported Hangboards
            public enum hangboardName {BM1000, BM2000, TRANS, TENSION, ZLAG, MOONHARD, MOONEASY, METO,
        ROCKPRODIGY, PROBLEMSOLVER, METO_CONTACT, METO_WOOD, DRCC, CONCEPT}

        private HangboardResources() {

        }

        public static int getHoldValueResources(hangboardName hangboard) {
                int position = hangboard.ordinal();
                return hold_resources[position];
        }

        public static int getHoldCoordinates(hangboardName hangboard) {
                int position = hangboard.ordinal();
                return coordinate_resources[position];
        }

    public static hangboardName forInt(int id) {
        return hangboardName.values()[id-1];

    }

        public static int getHangboardCount() {
                return image_resources.length;
        }

    // Converts PagerAdapter position into hangboard enum.
    public static hangboardName getHangboardName(int position) {
                if (position >= 0 && position < hangboardName.values().length ) {
                    return hangboardName.values()[position];
                }
                return hangboardName.BM1000;
    }

    public static int getHangboardImageResource(int position) {

                if (position < image_resources.length) {
                    return image_resources[position];
                }
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
                return hangboardStrings[position];

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
