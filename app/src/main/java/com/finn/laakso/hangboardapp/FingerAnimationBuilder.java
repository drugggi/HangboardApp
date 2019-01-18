package com.finn.laakso.hangboardapp;

// FingerAnimationBuilder stores xml and png files need for finger animations. And picks the right

public final class FingerAnimationBuilder {

    private static int[] leftHand_4_to_x = {0,R.drawable.animation_left_4_to_3f,R.drawable.animation_left_4_to_3b,R.drawable.animation_left_4_to_2f,R.drawable.animation_left_4_to_2m,R.drawable.animation_left_4_to_2b};

    private static int[] leftHand_3f_to_x = {R.drawable.animation_left_3f_to_4,0,R.drawable.animation_left_3f_to_3b,R.drawable.animation_left_3f_to_2f,R.drawable.animation_left_3f_to_2m,R.drawable.animation_left_3f_to_2b};

    private static int[] leftHand_3b_to_x = {R.drawable.animation_left_3b_to_4,R.drawable.animation_left_3b_to_3f,0,R.drawable.animation_left_3b_to_2f,R.drawable.animation_left_3b_to_2m,R.drawable.animation_left_3b_to_2b};

    private static int[] leftHand_2f_to_x = {R.drawable.animation_left_2f_to_4,R.drawable.animation_left_2f_to_3f,R.drawable.animation_left_2f_to_3b,0,R.drawable.animation_left_2f_to_2m,R.drawable.animation_left_2f_to_2b};

    private static int[] leftHand_2m_to_x = {R.drawable.animation_left_2m_to_4,R.drawable.animation_left_2m_to_3f,R.drawable.animation_left_2m_to_3b,R.drawable.animation_left_2m_to_2f,0,R.drawable.animation_left_2m_to_2b};

    private static int[] leftHand_2b_to_x = {R.drawable.animation_left_2b_to_4,R.drawable.animation_left_2b_to_3f,R.drawable.animation_left_2b_to_3b,R.drawable.animation_left_2b_to_2f,R.drawable.animation_left_2b_to_2m,0};



    private static int[] rightHand_4_to_x = {0,R.drawable.animation_right_4_to_3f,R.drawable.animation_right_4_to_3b,R.drawable.animation_right_4_to_2f,R.drawable.animation_right_4_to_2m,R.drawable.animation_right_4_to_2b};

    private static int[] rightHand_3f_to_x = {R.drawable.animation_right_3f_to_4,0,R.drawable.animation_right_3f_to_3b,R.drawable.animation_right_3f_to_2f,R.drawable.animation_right_3f_to_2m,R.drawable.animation_right_3f_to_2b};

    private static int[] rightHand_3b_to_x = {R.drawable.animation_right_3b_to_4,R.drawable.animation_right_3b_to_3f,0,R.drawable.animation_right_3b_to_2f,R.drawable.animation_right_3b_to_2m,R.drawable.animation_right_3b_to_2b};

    private static int[] rightHand_2f_to_x = {R.drawable.animation_right_2f_to_4,R.drawable.animation_right_2f_to_3f,R.drawable.animation_right_2f_to_3b,0,R.drawable.animation_right_2f_to_2m,R.drawable.animation_right_2f_to_2b};

    private static int[] rightHand_2m_to_x = {R.drawable.animation_right_2m_to_4,R.drawable.animation_right_2m_to_3f,R.drawable.animation_right_2m_to_3b,R.drawable.animation_right_2m_to_2f,0,R.drawable.animation_right_2m_to_2b};

    private static int[] rightHand_2b_to_x = {R.drawable.animation_right_2b_to_4,R.drawable.animation_right_2b_to_3f,R.drawable.animation_right_2b_to_3b,R.drawable.animation_right_2b_to_2f,R.drawable.animation_right_2b_to_2m,0};




    private static int[][] leftHandAnimations = {leftHand_4_to_x,leftHand_3f_to_x,leftHand_3b_to_x,
            leftHand_2f_to_x,leftHand_2m_to_x,leftHand_2b_to_x };

    private static int[][] rightHandAnimations = {rightHand_4_to_x,rightHand_3f_to_x,rightHand_3b_to_x,
            rightHand_2f_to_x,rightHand_2m_to_x,rightHand_2b_to_x };


    private FingerAnimationBuilder() {

    }


    // getHandTransitionStart returns the right animation when start and end grip type is known
    public static int getHandTransitionStart(Hold.grip_type fromThisGrip, Hold.grip_type toThisGrip, boolean leftHand) {

        int x = fromThisGrip.ordinal();
        int y = toThisGrip.ordinal();

        if (x >= leftHandAnimations.length) {return 0; }
        if (y >= leftHandAnimations.length) {return 0; }

        if (leftHand) {
            return leftHandAnimations[x][y];
        }
        else   {
            return rightHandAnimations[x][y];
        }
    }
}
