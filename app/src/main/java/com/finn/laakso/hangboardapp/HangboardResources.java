package com.finn.laakso.hangboardapp;

public final class  HangboardResources {
    private static int[] image_resources = {R.drawable.lauta1011, R.drawable.lauta2002, R.drawable.trans,
            R.drawable.tension, R.drawable.zlag, R.drawable.moonhard, R.drawable.mooneasy, R.drawable.meto,
            R.drawable.rockprodigy, R.drawable.problemsolver, R.drawable.meto_contact, R.drawable.meto_wood,
            R.drawable.drcc};


    private static int[] coordinate_resources = {R.array.bm1000_coordinates, R.array.bm2000_coordinates,
    R.array.trans_coordinates, R.array.tension_coordinates, R.array.zlag_coordinates, R.array.moonhard_coordinates,
            R.array.mooneasy_coordinates, R.array.meto_coordinates, R.array.rockprodigy_coordinates,
            R.array.problemsolver_coordinates, R.array.meto_contact_coordinates, R.array.meto_wood_coordinates,
            R.array.drcc_coordinates};

    private static int[] hold_resources = {R.array.grip_values_bm1000, R.array.grip_values_bm2000,
            R.array.grip_values_trans, R.array.grip_values_tension, R.array.grip_values_zlag, R.array.grip_values_moonhard,
            R.array.grip_values_mooneasy, R.array.grip_values_meto, R.array.grip_values_rockprodigy,
            R.array.grip_values_problemsolver, R.array.grip_values_meto_contact, R.array.grip_values_meto_wood,
            R.array.grip_values_drcc};

    private static String[] hangboardStrings = {"BM 1000", "BM 2000", "Transgression","Tension",
            "Zlagboard","Moonboard hard","Moonboard easy","Metolius","Rock Prodigy","Meto. Contact",
            "Meto. Wood","DRCC"};


    // All supported Hangboards
            public enum hangboardName {BM1000, BM2000, TRANS, TENSION, ZLAG, MOONHARD, MOONEASY, METO,
        ROCKPRODIGY, PROBLEMSOLVER, METO_CONTACT, METO_WOOD, DRCC}

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

                return hangboardName.values()[position];
/*
        if (position == 0) { return hangboardName.BM1000; }
        else if (position == 1) {return hangboardName.BM2000; }
        else if (position == 2) {return hangboardName.TRANS; }
        else if (position == 3) {return hangboardName.TENSION;}
        else if (position == 4) {return hangboardName.ZLAG; }
        else if (position == 5) {return hangboardName.MOONHARD; }
        else if (position == 6) {return hangboardName.MOONEASY; }
        else if (position == 7) {return hangboardName.METO; }
        else if (position == 8) {return hangboardName.ROCKPRODIGY; }
        else if (position == 9) {return hangboardName.PROBLEMSOLVER; }
        else if (position == 10) {return hangboardName.METO_CONTACT; }
        else if (position == 11) {return hangboardName.METO_WOOD; }
        else if (position == 12) {return hangboardName.DRCC; }
        else {return hangboardName.BM1000; }
*/
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
/*
        if (HB.equals("BM 1000") ) {return 0; }
        else if (HB.equals("BM 2000") ) {return 1; }
        else if (HB.equals("Transgression") ) {return 2; }
        else if (HB.equals("Tension") ) {return 3; }
        else if (HB.equals("Zlagboard") ) {return 4; }
        else if (HB.equals("Moonboard hard") ) {return 5; }
        else if (HB.equals("Moonboard easy") ) {return 6; }
        else if (HB.equals("Metolius") ) {return 7; }
        else if (HB.equals("Rock Prodigy") ) {return 8; }
        else if (HB.equals("Problemsolver") ) {return 9; }
        else if (HB.equals("Meto. Contact") ) {return 10; }
        else if (HB.equals("Meto. Wood") ) {return 11; }
        else if (HB.equals("DRCC") ) {return 12; }
        else {return 0; }
        */
    }

    // Converts hangboardName enum into describing name. Should be somewhere else
    public static String getHangboardStringName(hangboardName HB) {

                int position = HB.ordinal();
                return hangboardStrings[position];
/*
        switch (HB) {
            case BM1000:
                return "BM 1000";
            case BM2000:
                return "BM 2000";
            case TRANS:
                return "Transgression";
            case TENSION:
                return "Tension";
            case ZLAG:
                return "Zlagboard";
            case MOONHARD:
                return "Moonboard hard";
            case MOONEASY:
                return "Moonboard easy";
            case METO:
                return "Metolius";
            case ROCKPRODIGY:
                return "Rock Prodigy";
            case PROBLEMSOLVER:
                return "Problemsolver";
            case METO_CONTACT:
                return "Meto. Contact";
            case METO_WOOD:
                return "Meto. Wood";
            case DRCC:
                return "DRCC";
            default:
                return "Custom";
        }*/
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

 /*
        hangboardName tempHB;
        String tempName;
        for (int position = 0 ; position < image_resources.length; position++) {
            tempHB = getHangboardName(position);
            tempName = getHangboardStringName(tempHB);
            if (tempName.equals(hangboardName)){
                return getHangboardImageResource(position);
            }

        }
        return R.drawable.lauta1011;
        }
        */


    // Converts hangboard image resource into describing name. This should be somewhere else.
    public static String getHangboardStringName(int HBresource) {

        for (int position = 0; position < image_resources.length; position++) {
            if (image_resources[position] == HBresource) {
                return hangboardStrings[position];
            }
        }
        return "Custom";
/*
        switch (HBresource) {
            case R.drawable.lauta1011:
                return "BM 1000";
            case R.drawable.lauta2002:
                return "BM 2000";
            case R.drawable.trans:
                return "Transgression";
            case R.drawable.tension:
                return "Tension";
            case R.drawable.zlag:
                return "Zlagboard";
            case R.drawable.moonhard:
                return "Moonboard hard";
            case R.drawable.mooneasy:
                return "Moonboard easy";
            case R.drawable.meto:
                return "Metolius";
            case R.drawable.rockprodigy:
                return "Rock Prodigy";
            case R.drawable.problemsolver:
                return "Problemsolver";
            case R.drawable.meto_contact:
                return "Meto. Contact";
            case R.drawable.meto_wood:
                return "Meto. Wood";
            case R.drawable.drcc:
                return "DRCC";
            default:
                return "Custom";
        }
    */
    }
}
