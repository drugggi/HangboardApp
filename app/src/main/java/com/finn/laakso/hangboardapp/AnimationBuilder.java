package com.finn.laakso.hangboardapp;

public final class AnimationBuilder {

    private static int[] leftHand_4_to_x = {
            0,R.drawable.animation_left_4_to_3f,R.drawable.animation_left_4_to_3b,R.drawable.animation_left_4_to_2f,R.drawable.animation_left_4_to_2m,0};

    private static int[] leftHand_3f_to_x = {
            R.drawable.animation_left_3f_to_4,0,R.drawable.animation_left_3f_to_3b,R.drawable.animation_left_3f_to_2f,R.drawable.animation_left_3f_to_2m,0};

    private static int[] leftHand_3b_to_x = {
            R.drawable.animation_left_3b_to_4,R.drawable.animation_left_3b_to_3f,0,0,R.drawable.animation_left_3b_to_2m,0};

    private static int[] leftHand_2f_to_x = {R.drawable.animation_left_2f_to_4,R.drawable.animation_left_2f_to_3f,0,0,0,0};

    private static int[] leftHand_2m_to_x = {R.drawable.animation_left_2m_to_4,R.drawable.animation_left_2m_to_3f,R.drawable.animation_left_2m_to_3b,0,0,0};

    private static int[] leftHand_2b_to_x = {0,0,0,0,0,0};

    private static int[][] leftHandAnimations = {leftHand_4_to_x,leftHand_3f_to_x,leftHand_3b_to_x,
    leftHand_2f_to_x,leftHand_2m_to_x,leftHand_2b_to_x };




    private static int[] rightHand_4_to_x = {
            0,R.drawable.animation_right_4_to_3f,R.drawable.animation_right_4_to_3b,R.drawable.animation_right_4_to_2f,R.drawable.animation_right_4_to_2m,0};

    private static int[] rightHand_3f_to_x = {
            R.drawable.animation_right_3f_to_4,0,R.drawable.animation_right_3f_to_3b,R.drawable.animation_right_3f_to_2f,R.drawable.animation_right_3f_to_2m,0};

    private static int[] rightHand_3b_to_x = {
            R.drawable.animation_right_3b_to_4,R.drawable.animation_right_3b_to_3f,0,0,R.drawable.animation_right_3b_to_2m,0};

    private static int[] rightHand_2f_to_x = {R.drawable.animation_right_2f_to_4,R.drawable.animation_right_2f_to_3f,0,0,0,0};

    private static int[] rightHand_2m_to_x = {R.drawable.animation_right_2m_to_4,R.drawable.animation_right_2m_to_3f,R.drawable.animation_right_2m_to_3b,0,0,0};

    private static int[] rightHand_2b_to_x = {0,0,0,0,0,0};

    private static int[][] rightHandAnimations = {rightHand_4_to_x,rightHand_3f_to_x,rightHand_3b_to_x,
            rightHand_2f_to_x,rightHand_2m_to_x,rightHand_2b_to_x };



    private AnimationBuilder() {

    }


public static int getHandTransitionStart(Hold.grip_type fromThisGrip, Hold.grip_type toThisGrip, boolean leftHand) {

    int x = fromThisGrip.ordinal();
    int y = toThisGrip.ordinal();

  //  Log.d("From Grip","" + fromThisGrip + "   valueof: x: " +  fromThisGrip.ordinal() );
  //  Log.d("To Grip","" + toThisGrip+ "   valueof: y: " + toThisGrip.ordinal() );


    if (x >= leftHandAnimations.length) {return 0; }
    if (y >= leftHandAnimations.length) {return 0; }

    if (leftHand) {
    //    Log.d("leftanimationvalue",":  " + leftHandAnimations[x][y]);
        return leftHandAnimations[x][y];
    }
    else   {

    //    Log.d("rightanimationvalue",":  " + rightHandAnimations[x][y]);
        return rightHandAnimations[x][y];
    }
}
 /*   public static int getHandTransitionStartOldVersion(Hold.grip_type fromThisGrip, Hold.grip_type toThisGrip, boolean leftHand) {


        Log.d("From Grip","" + fromThisGrip + "   valueof: " +  fromThisGrip.ordinal() );
        Log.d("To Grip","" + toThisGrip+ "   valueof: " + toThisGrip.ordinal() );



        if (leftHand) {

            switch (fromThisGrip) {
                case FOUR_FINGER:

                    if (toThisGrip == Hold.grip_type.THREE_FRONT) {
                        return R.drawable.animation_left_4_to_3f;
                    } else if (toThisGrip == Hold.grip_type.THREE_BACK) {
                        return R.drawable.animation_left_4_to_3b;
                    } else {


                        return 0;
                    }
                case THREE_FRONT:
                    if (toThisGrip == Hold.grip_type.FOUR_FINGER) {
                        return R.drawable.animation_left_3f_to_4;
                    } else if (toThisGrip == Hold.grip_type.THREE_BACK) {
                        return R.drawable.animation_left_3f_to_3b;
                    } else {
                        return 0;
                    }
                case THREE_BACK:
                    if (toThisGrip == Hold.grip_type.FOUR_FINGER) {
                        return R.drawable.animation_left_3b_to_4;
                    } else if (toThisGrip == Hold.grip_type.THREE_FRONT) {
                        return R.drawable.animation_left_3b_to_3f;
                    } else {
                        return 0;
                    }
                default:
                    return 0;
            }


        } else { // rightHand
            switch (fromThisGrip) {
                case FOUR_FINGER:

                    if (toThisGrip == Hold.grip_type.THREE_FRONT) {
                        return R.drawable.animation_right_4_to_3f;
                    } else if (toThisGrip == Hold.grip_type.THREE_BACK) {
                        return R.drawable.animation_right_4_to_3b;
                    } else {
                        return 0;
                    }
                case THREE_FRONT:
                    if (toThisGrip == Hold.grip_type.FOUR_FINGER) {
                        return R.drawable.animation_right_3f_to_4;
                    } else if (toThisGrip == Hold.grip_type.THREE_BACK) {
                        return R.drawable.animation_right_3f_to_3b;
                    } else {
                        return 0;
                    }
                case THREE_BACK:
                    if (toThisGrip == Hold.grip_type.FOUR_FINGER) {
                        return R.drawable.animation_right_3b_to_4;
                    } else if (toThisGrip == Hold.grip_type.THREE_FRONT) {
                        return R.drawable.animation_right_3b_to_3f;
                    } else {
                        return 0;
                    }
                default:
                    return 0;
            }
        }
    }
*/
}
