package com.finn.laakso.hangboardapp;

// HangboardResources manages Hangboard images, image resources, hold values and coordinates
// name conversion between enums and strings etc.

import android.util.Log;

public final class  HangboardResources {
    // Some base values for holds based on millimeter depth from 6mm - 40mm and jug
    private static final int JUG = 1,JUG3F = 2, JUG3B= 2,
            MM40 = 2 ,MM40F3 = 6 , MM40B3 = 8, MM40F2 = 22, MM40M2 = 18, MM40B2 = 50,
            MM35 = 3, MM35F3 = 8 , MM35B3 = 10, MM35F2 = 32, MM35M2 = 24, MM35B2 = 60,
            MM32 = 4, MM32F3 = 10, MM32B3 = 12, Mm32F2 = 39, MM32M2 = 29, MM32B2 = 69,
            MM30 = 5, MM30F3 = 12, MM30B3 = 14, MM30F2 = 45, MM30M2 = 32, MM30B2 = 75,
            MM28 = 6, MM28F3 = 13, MM28B3 = 15, MM28F2 = 50, MM28M2 = 35, MM28B2 = 80,
            MM25= 7, MM25F3 = 14, MM25B3 = 16, MM25F2 = 58, MM25M2 = 41, MM25B2 = 86,
            MM23 = 8, MM23F3 = 16, MM23B3 = 18,
            MM22 = 9, MM22F3 = 18, MM22B3 = 20, MM22F2 = 65, MM22M2 = 55, MM22B2 = 95,
            MM21 = 9, MM21F3 = 20, MM21B3 = 22,
            MM20 = 10, MM20F3 = 22, MM20B3 = 24,MM20F2 = 75, MM20M2 = 65, MM20B2 = 115,
            MM19 = 11, MM19F3 = 23, MM19B3 = 25,
            MM18 = 12, MM18F3 = 25, MM18B3 = 28,
            MM17 = 14, MM17F3 = 28, MM17B3 = 32,
            MM16 = 16, MM16F3 = 32, MM16B3 = 36,
            MM15 = 20, MM15F3 = 37, MM15B3 = 42, //BM1000 (4)
            MM14 = 22, MM14F3 = 43, MM14B3 = 48,
            MM13 = 24, MM13F3 = 50, MM13B3 = 58,
            MM12 = 26, MM12F3 = 58, MM12B3 = 67,
            MM11 = 28, MM11F3 = 67, MM11B3 = 78,
            MM10 = 30, MM10F3 = 77, MM10B3 = 85,
            MM9 = 40, MM9F3 = 95, MM9B3 = 120,
            MM8 = 50, MM8F3 = 150, MM8B3 = 225,
            MM7 = 70, MM7F3 = 275, MM7B3 = 350,
            MM6 =100, MM6F3 = 450, MM6B3 = 500;

    // underscore before the hold depth description means that it is a pocket, which instead means
    // that it is easier hold than flat holds with same depths. This is mainly because friction and
    // surface area increases on fingers
    private static final int
    _MM40F3 = 4 , _MM40B3 = 5, _MM40F2 = 22, _MM40M2 = 14, _MM40B2 = 40,
     _MM35F3 = 6 , _MM35B3 = 8, _MM35F2 = 24, _MM35M2 = 19, _MM35B2 = 48,
     _MM32F3 = 8, _MM32B3 = 10, _Mm32F2 = 33, _MM32M2 = 24, _MM32B2 = 57,
     _MM30F3 = 9, _MM30B3 = 11, _MM30F2 = 38, _MM30M2 = 27, _MM30B2 = 68,
     _MM28F3 = 10, _MM28B3 = 12, _MM28F2 = 45, _MM28M2 = 31, _MM28B2 = 75,
     _MM25F3 = 11, _MM25B3 = 14, _MM25F2 = 52, _MM25M2 = 35, _MM25B2 = 80,
    _MM23F3 = 13, _MM23B3 = 16,
     _MM22F3 = 16, _MM22B3 = 18, _MM22F2 = 57, _MM22M2 = 45, _MM22B2 = 90,
    _MM21F3 = 18, _MM21B3 = 20,
     _MM20F3 = 20, _MM20B3 = 21, _MM20F2 = 69, _MM20M2 = 55, _MM20B2 = 105,
     _MM19F3 = 21, _MM19B3 = 23,
     _MM18F3 = 23, _MM18B3 = 25,
     _MM17F3 = 26, _MM17B3 = 28,
     _MM16F3 = 28, _MM16B3 = 32,
            _MM15F3 = 32, _MM15B3 = 35,
     _MM14F3 = 36, _MM14B3 = 40,
     _MM13F3 = 40, _MM13B3 = 50,
     _MM12F3 = 50, _MM12B3 = 60,
     _MM11F3 = 60, _MM11B3 = 70,
     _MM10F3 = 70, _MM10B3 = 80;

    /*    MM40 = 2, MM35 = 3, MM32 = 4,MM30 = 5, MM28 = 6, MM25= 7,
            MM23= 8, MM21 = 9, MM20 = 10, MM18= 12,MM16 = 14,MM15=16, MM14 = 17, MM12 = 18, MM11 = 20,
            MM10= 24, MM9=35, MM8 = 50, MM7= 60, MM6=100;
    */
    // Two finger pockets
    private static final int TWO45mm = 12,TWO35mm = 15, TWO30mm = 21, TWO25mm = 27, TWO20mm = 33;


    // Sloper difficulties based on angle degrees
    private static final int S20d = 3, S35d = 23, S45d = 100;

        // finger representation four finger, front three, back three, front two, middle two, back two, index
        // middle, ring, index and little finger
    private static final int FF = 10, F3=20, B3 = 30, F2= 40, M2 = 50, B2 = 60, IN = 70, MI = 80, RI = 90, LI = 100;
    private static final int FFS = 11, F3S=21, B3S = 31, F2S= 41, M2S =51, B2S = 61, INS = 71,
            MIS = 81, RIS = 91, LIS = 101;

private static final int[] gripValuesBM1000 = {
         1 , JUG , FF , 1 , JUG3F , F3 , 1 , 2 , B3 ,
         2 , 23 , FF , 2 , 70 , F3 , 2 , 75 , B3 ,
         3 , 3 , FF , 3 , 12 , F3 , 3 , 8 , B3 ,
         4 , MM15 , FF , 4 , MM15F3 , F3 ,4 , MM15B3 , B3 ,
         5 , 11 , F3 , 5 , 13 , B3 , 5 , 35 , F2 , 5 , 25 , M2 ,
         6 , MM40 , FF , 6 , MM40F3 , F3 , 6 , MM40B3 , B3 , 6 , MM40F2 , F2 , 6 , MM40M2 , M2 , 6 ,MM40B2 ,B2 ,
         7 , 17 , F2 , 7 , 12 , M2 , 7 , 40 , B2 , 7 , 100 ,IN , 7 ,80 ,MI ,
         8 ,4 , F3 , 8 , 5 , B3 ,
         9 , 2 , FFS , 9 , 4 , F3S , 9 , 7 , B3S ,
         10 , MM20 , FF , 10 , MM20F3 , F3 , 10 , MM20B3 , B3 , 10 , 69 , F2 , 10 , 40 , M2 ,
         11 ,55 ,F2 , 11 , 33 , M2 , 11 , 100 , B2 ,
         12 , 17 , F3 , 12 , 19 , B3 ,
};

    private static final int[] gripValuesBM2000 = {
            1, 100, 10,
            2, 23, 10, 2, 70, 20, 2, 75, 30,
         3, 3, 10, 3, 12, 20, 3, 8, 30,
         4, 4, 21, 4, 5, 31,
         5, 17, 21, 5, 19, 31,
         6, 3, 10, 6, 9, 20, 6, 11, 30,
         7, 139, 70, 7, 69, 80, 7, 150, 90, 7, 195, 100,
         8, 19, F2, 8, 15, M2, 8, 40, B2, 8, 155, IN, 8 , 87, MI, 8 , 200, RI,8 , 250, RI, 8 , 350, LI,
         9, 25, 40, 9, 21, 50, 9, 69, 60,
            10, 2, 11, 10, 4, 21, 10, 5, 31,
         11, 14, 10, 11, 22, 20, 11, 25, 30,
         12, 170, 70, 12, 125, 80, 12, 350, 90, 12, 450, 100,
            13, 60, 40, 13, 35, 50, 13, 110, 60,
            14, 250, 40, 14, 170, 50, 14, 500, 60,
         15, 8, 11, 15,18,21, 15,20,31,

    };

    private static final int[] gripValuesTrans = {
            1, 1, FF, 1, 2, F3, 1, 2, B3, 1, 27, F2, 1, 23, M2, 1, 45, B2, 1, 70, 70, 1, 60, 80, 1, 150, 90,
            2, MM18-6, FF, 2, MM18F3-10, F3, 2, MM18-10, B3, 2, 55, F2, 2, 39, M2, 2, 75, B2, 2, 150, 70, 2, 100, 80,
            3, MM14-12, FF, 3, MM14F3-20, F3, 3, MM14B3-22, B3, 3, 95, F2, 3, 75, M2, 3, 125, B2,
            4, MM12-10, FF, 4, MM12F3-30, F3, 4, MM12B3-30, B3, 4, 90, F2, 4, 70, M2, 4, 150, B2,
            5, MM10-10, FF, 5, MM10F3-30, F3, 5, MM10B3-35, B3, 5, 160, F2, 5, 100, M2, 5, 300, B2,
            6, MM9-7, FF, 6, MM9F3-30, F3, 6, MM9B3-35, B3, 6, 250, F2, 6, 150, M2, 6, 500, B2,
            7, MM8-5, FF, 7, MM8F3-30, F3, 7, MM8B3-35, B3, 7, 400, F2,
            8, MM7-5, FF, 8, MM7F3-30, F3, 8, MM7B3-35, B3,
            9, MM6-5, FF, 9, MM6F3-30, F3, 9, MM6B3-35, B3,


        };

    private static final int[] gripValuesTension = {
            1, JUG, FF, 1, JUG3F, F3, 1, JUG3B, B3, 1, 17, F2, 1, 14, M2, 1, 25, B2,
            2, MM35, FF, 2, MM35F3, F3, 2, MM35B3, B3, 2, MM35F2, F2, 2, MM35M2, M2, 2, MM35B2, B2,
            3, JUG, FFS, 3, JUG3F+1, F3S, 3, JUG3B+2, B3S, // JUG
            4, MM30, FF, 4, MM30F3, F3, 4, MM30B3, B3, 4, MM30F2, F2, 4 , MM30M2, M2, 4, MM30B2 , B2,
            5, MM25, FF, 5, MM25F3, F3, 5, MM25B3, B3, 5, MM25F2, F2, 5 , MM25M2, M2, 5, MM25B2, B2,
            6, MM40, FFS, 6, MM40F3-1, F3S, 6, MM40B3-1, B3S, 6, 21, F2S, 6, 17, M2S, 6, 35, B2S,  // 50mm hold
            7, MM20, FF, 7, MM20F3, F3, 7, MM20B3, B3,
            8, MM15, FF, 8, MM15F3, F3, 8, MM15B3, B3,
            9, MM22, FFS, 9, MM22F3, F3S, 9, MM22B3, B3S,

    };

    private static final int[] gripValuesTensionPro = {
            1, JUG, FF, 1, JUG3F, F3, 1, JUG3B, B3,
            2, MM40, FF, 2, MM40F3, F3, 2, MM40B3, B3,

            3, MM8, FF, 3, MM8F3, F3, 3, MM8B3, B3, // incut 7mm crimp -> maybe 8mm values
            4, MM35, FF, 4, MM35F3, F3, 4, MM35B3, B3, 4, MM35F2, F2, 4 , MM35M2, M2, 4, MM35B2 , B2,

            5, MM20, FF, 5, MM20F3, F3, 5, MM20B3, B3, 5, MM20F2, F2, 5 , MM20M2, M2, 5, MM20B2, B2,
            6, 139, 70, 6, 69, 80, 6, 150, 90, 6, 195, 100, // Same as BM2000 monos
            7, 39, F2, 7, 28, M2, 7, 85, B2,

            8, MM30, FFS, 8, MM30F3, F3S, 8, MM30B3, B3S, 8, MM30F2, F2S, 8, MM30M2, M2S, 8, MM30B2, B2S,

            9, MM15, FF, 9, MM15F3, F3, 9, MM15B3, B3,
            10, MM10, FF, 10, MM10F3, F3, 10, MM10B3, B3,

            11, MM22, FFS, 11, MM22F3, F3S, 11, MM22B3, B3S,

    };

    private static final int[] gripValuesZlag = {
            1, JUG, 10, 1, 2, 20, 1, 2, 30, 1, 16, 40, 1, 14, 50, 1, 35, 60,
            2, 13, 10, 2, 30, 20, 2, 35, 30,
            3, 3, 11, 3, 8, 21, 3, 9, 31,
            4, MM30, 10, 4, MM30F3, 20, 4, MM30B3, 30,
            5, 23, 20, 5, 26, 30,
            6, MM30-1, 11, 6, 10, 21, 6, 11, 31,
            7, MM20, 10, 7, MM20F3+1, 20, 7, MM20B3+1, 30,
            8, 350, 70, 8, 300, 80, 8, 450, 90,
            9, 39, 40, 9, TWO30mm+15, 50, 9, 65, 60,
            11, MM20, 11, 11, MM20F3, 21, 11, MM20B3, 31,
            12, MM10, FF, 12, MM10F3, F3, 12, MM10B3,B3,
            13, 150, 70, 13, 80, 80, 13, 300, 90,
            14, 150, 40, 14, 100, 50, 14, 300, 60,
            15, MM15+2, FFS, 15, MM15F3, F3S, 15, MM15B3, B3S,
    };

    private static final int[] gripValuesMoonhard = {
            1, 5, 10, 1, 15, 20, 1, 17, 30, 1, 80, 40, 1, 68, 50, 1, 134, 60,
            2, 45, 10,
            3, 9, 10, 3, 20, 20, 3, 23, 30,
            4, 12, 10, 4, 25, 20, 4, 29, 30, 4, 123, 40, 4, 113, 50,
            5, 70, 10,5, 200, 20, 5, 250, 30,
            6, 19, 10, 6, 75, 20, 6, 80, 30,
            7, 15, 10, 7, 40, 20, 7, 45, 30, 7, 113, 40, 7, 98, 50,
            8, 70, 41, 8, 55, 51, 8, 150, 61,

    };

    private static final int[] gripValuesMooneasy = {
            1, 1, 10, 1, 2, 20, 1, 3, 30, 1, 30, 40, 1, 25, 50, 1, 46, 60,
            2, 8, 10, 2, 15, 20, 2, 17, 30,
            3, 3, 10, 3, 12, 20, 3, 14, 30,
            4, 2, 10, 4, 3, 20, 4, 4, 30, 4, 19, 40, 4, 14, 50, 4, 42, 60,
            5, 10, 10, 5, 19, 20, 5, 21, 30,
            6, 3, 10, 6, 4, 20, 6, 5, 30, 6, 21, 40, 6, 15, 50, 6, 48, 60,
            7, 17, 40, 7, 12, 50, 7, 37, 60,

    };

    private static final int[] gripValuesMeto = {
            1, 1, 10, 1, 2, 20, 1, 2, 30, 1, 19, 40, 1, 23, 50, 1, 35, 60,
            2, 5, 10,
            3, 4, 10,
            4, 13, 20, 4, 15, 30,
            5, 10, 10, 5, 16, 20, 5, 18, 30,
            6, 15, 10, 6, 19, 20, 6, 21, 30,
            7, 2, 10, 7, 5, 20, 7, 6, 30,
            8, 19, 20, 8, 21, 30,
            9, 10, 20, 9, 12, 30,
            10, 20, 20, 10, 23, 30,
            11, 17, 10, 11, 23, 20, 11, 26, 30,
            12, 25, 40, 12, 22, 50, 12, 39, 60,
            13, 79, 40, 13, 65, 50, 13, 111, 60,
            14, 1, 11, 14, 2, 21, 14, 2, 31,
            15, 3, 21, 15, 4, 31,
            16, 11, 21, 16, 13, 31,
            17, 14, 21, 17, 16, 31,
            18, 35, 41, 18, 27, 51, 18, 85, 61,


        };

    private static final int[] gripValuesRockprodigy = {
            2, 4, 10, 2, 13, 20, 2, 17, 30,
            3, 1, 10, 3, 2, 20, 3, 2, 30,
            4, 14, 10, 4, 22, 20, 4, 25, 30,
            5, 5, 10, 5, 11, 20, 5, 13, 30,
            6, 23, 10, 6, 63, 20, 6, 70, 30,
            7, 11, 10, 7, 16, 20, 7, 19, 30,
            8, 27, 10, 8, 69, 20, 8, 89, 30,
            9, 2, 10, 9, 5, 20, 9, 7, 30,
            10, 16, 20, 10, 19, 30, 10, 27, 40, 10, 18, 50, 10, 39, 60,
            11, 37, 40, 11, 29, 50, 11, 55, 60,
            12, 37, 40, 12, 29, 50, 12, 55, 60,
            13, 77, 40, 13, 69, 50, 13, 105, 60,

    };

    private static final int[] gripValuesProblemsolver = {

 1, 1, 10, 1, 2, 20, 1, 2, 30, 1, 19, 40, 1, 14, 50, 1, 29, 60,
            2, 10, 10, 2, 17, 20, 2, 19, 30, 2, 39, 40, 2, 27, 50, 2, 67, 60,
            3, 15, 10, 3, 22, 20, 3, 26, 30,
            4, 29, 10, 4, 49, 20, 4, 75, 30,
            5, 11, 10, 5, 18, 20, 5, 20, 30,
            6, 14, 11, 6, 21, 21, 6, 25, 31,
            7, 24, 10, 7, 35, 20, 7, 40, 30,
            8, 35, 10, 8, 79, 20, 8, 99, 30,
            9, 13, 10, 9, 20, 20, 9, 23, 30,
            10, 18, 11, 10, 25, 21, 10, 30, 31,

    };

    private static final int[] gripValuesMetocontact = {
            1, 1, 10, 1, 2, 20, 1, 2, 30,
            2, 5, 10,
            3, 7, 10,
            4, 15, 10, 4, 22, 20, 4, 26, 30,
            5, 5, 10, 5, 10, 20, 5, 12, 30,
            6, 19, 40, 6, 16, 50, 6, 29, 60,
            7, 3, 10, 7, 8, 20, 7, 10, 30,
            8, 17, 20, 8, 19, 30,
            9, 4, 10, 9, 9, 20, 9, 11, 30,
            10, 31, 40, 10, 24, 50, 10, 45, 60,
            11, 7, 10, 11, 13, 20, 11, 15, 30,
            12, 10, 10, 12, 15, 20, 12, 18, 30,
            13, 14, 20,
            13, 16, 30,
            14, 51, 40, 14, 34, 50, 14, 75, 60,
            16, 18, 10, 16, 25, 20, 16, 30, 30,
            17, 19, 20, 17, 22, 30,
            18, 71, 40, 18, 49, 50, 18, 105, 60,
            19, 9, 10, 19, 14, 20, 19, 17, 30,

    };

    private static final int[] gripValuesMetowood = {
 1, 1, 10, 1, 2, 20, 1, 2, 30, 1, 24, 40, 1, 16, 50, 1, 34, 60,
 2, 3, 10, 2, 18, 20, 2, 24, 30,
 3, 13, 20, 3, 15, 30,
 4, 39, 40, 4, 21, 50, 4, 59, 60,
 5, 11, 20, 5, 13, 30,
 6, 8, 20, 6, 10, 30,
 7, 10, 10, 7, 15, 20, 7, 18, 30,
 8, 19, 20, 8, 22, 30,
 9, 15, 10, 9, 21, 20, 9, 23, 30,
 10, 20, 10, 10, 25, 20, 10, 29, 30,

    };

    private static final int[] gripValuesMetoWoodDeluxe = {
            1, JUG, FF, 1, JUG3F, F3, 1, JUG3B, B3,
            2, 15, FF, 2,45 , F3, 2,55 , B3,
            3,10, FF, 3,33 , F3, 3,43 , B3,

            4, MM30, FF, 4, MM30F3, F3, 4, MM30B3, B3,
            5, MM32F3-1, F3, 5, MM32B3-1, B3, // three finger pocktes thus -1 reduction

            6, MM35F2, F2, 6, MM35M2 , M2, 6, MM35B2, B2, // probably same dimension as hold 7
            7, MM32, FFS, 7, MM32F3, F3S, 7, MM32B3, B3S, // same dimension as hold 4 but wanted to make this easier

            8, MM13, FF, 8, MM13F3, F3, 8, MM13B3, B3,
            9, MM19F3-5, F3, 9, MM19B3-5, B3, // three finger pocket thus -5 reduction

            10, MM20F2-10, F2, 10, MM20M2 -10, M2, 10, MM20B2-10, B2,
            11, MM19, FFS, 11, MM19F3, F3S, 11, MM19B3, B3S,

            12, MM10, FF, 12, MM10F3, F3, 12, MM10B3, B3, // I guessed 10mm depth
            13, MM15F3-5 , F3, 13, MM15B3-5, B3,

            14, 85, F2, 6, 75, M2, 6, 115, B2,
            15, MM15, FFS, 15, MM15F3, F3S, 15, MM15B3, B3S, // I guessed 15mm depth


    };

    private static final int[] gripValuesDrcc = {
 1, 1, 10, 1, 2, 20, 1, 2, 30, 1, 25, 40, 1, 15, 50, 1, 49, 60,
 2, 2, 10, 2, 3, 20, 2, 4, 30,
 3, 3, 10, 3, 11, 20, 3, 13, 30,
 4, 2, 10, 4, 3, 20, 4, 3, 30, 4, 29, 40, 4, 19, 50, 4, 69, 60,
 5, 8, 10, 5, 13, 20, 5, 15, 30, 5, 45, 40, 5, 25, 50, 5, 79, 60,
 6, 21, 10, 6, 45, 20, 6, 55, 30,
            // 7
 8, 13, 10, 8, 19, 20, 8, 23, 30,
            9, 20, 10, 9, 50, 20, 9, 60, 30,
 10, 18, 10, 10, 45, 20, 10, 55, 30,

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
            9,MM28,FF, 9,MM28F3,F3, 9,MM28B3,B3,
            10,21,F2, 10,16,M2, 10,33,B2,
            11,MM18,FF, 11,MM18F3,F3, 11,MM18B3,B3,
            12,MM7,FF, 12,MM7F3,F3, 12,MM7B3,B3,
            13,110,IN, 13,65,MI, 13,130,RI, 13,175,LI,
            14,MM10,FF, 14,MM10F3,F3, 14,MM10B3,B3,
            15,MM22,FFS, 15,MM22F3,F3S, 15,MM22B3,B3S

    };

    private static final int[] gripValuesEdge = {
            1,30,FF, 1,45,F3, 1,51,B3,
            2,18,FF, 2,28,F3, 2,32,B3,
            3,8,FF, 3,18,F3, 3,23,B3,
            4,MM35,FF, 4,MM35F3,F3, 4,MM35B3,B3, 4,MM35F2,F2, 4,MM35M2,M2, 4,MM35B2,B2,
            5,MM25,FF, 5,MM25F3,F3, 5,MM25B3,B3, 5,MM25F2,F2, 5,MM25M2,M2, 5,MM25B2,B2,
            6,MM20,FF, 6,MM20F3,F3, 6,MM20B3,B3, 6,MM20F2,F2, 6,MM20M2,M2, 6,MM20B2,B2,
            7,MM18,FF, 7,MM18F3,F3, 7,MM18B3,B3,
            8,MM16,FF, 8,MM16F3,F3, 8,MM16B3,B3,
            9,MM14,FF, 9,MM14F3,F3, 9,MM14B3,B3,
            10,MM12,FF, 10,MM12F3,F3, 10,MM12B3,B3,
            11,MM10,FF, 11,MM10F3,F3, 11,MM10B3,B3,
            12,MM8,FF, 12,MM8F3,F3, 12,MM8B3,B3
    };

    private static final int[] gripValuesMatrix = {
            1,12,FF, 1,33,F3, 1,39,B3,
            2,MM40,FF, 2,MM40F3+2,F3, 2,MM40B3+3,B3, // flat edge

            3,MM10,FF, 3,MM10F3,F3, 3,MM10B3,B3,
            4,MM15,FF, 4,MM15F3,F3, 4,MM15B3,B3,
            5,MM35,FFS, 5,MM35F3,F3S, 5,MM35B3,B3S,

            6,MM40,FF, 6,MM40F3,F3, 6,MM40B3,B3,
            7,19,F2, 7,13,M2, 7,40,B2,
            8,MM28F3-1,F3, 8,MM28B3-1,B3,
            9,MM25,FFS, 9,MM25,F3S, 9,MM25B3,B3S,

            10,MM18,FF, 10,MM18F3,F3, 10,MM18B3,B3,
            11,40,F2, 11,25,M2, 11,75,B2,
            12,105,IN, 12,95,MI, 12,185,RI, 12,235,LI,
            13,55,F2, 13,33,M2, 13,100,B2,
            14,MM22,FFS, 14,MM22F3,F3S, 14,MM22B3,B3S,
    };

    private static final int[] gripValuesCore = {
            1,3,FF, 1,7,F3, 1,9,B3,
            2,3,FF, 2,7,F3, 2,9,B3,
            3,3,FFS, 3,7,F3S, 3,9,B3S,

            4,3,FF, 4,7,F3, 4,9,B3,
            5,3,FF, 5,7,F3, 5,9,B3,
            6,3,FFS, 6,7,F3S, 6,9,B3S,

            7,3,FF, 7,7,F3, 7,9,B3,
            8,3,FF, 8,7,F3, 8,9,B3,
            9,3,FFS, 9,7,F3S, 9,9,B3S,

            10,3,FF, 10,7,F3, 10,9,B3,
            11,3,FF, 11,7,F3, 11,9,B3,
            12,3,FFS, 12,7,F3S, 12,9,B3S,

            13,3,FF, 13,7,F3, 13,9,B3,
            14,27,F2, 14,24,M2, 14,45,B2,
            15,7,F3, 15,9,B3,
            16,3,FFS, 16,7,F3S, 16,9,B3S,
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
        1,JUG,FF, 1,JUG3F+1,F3, 1,JUG3B+2,B3,
        2,2,FF, 2,4,F3, 2,6,B3,
        3,9,FF, 3,21,F3, 3,25,B3,
        4,MM20-5,FF, 4,MM20F3-11,F3, 4,MM20B3-11,B3, 4,40,F2, 4,25,M2, 4,60,B2, // Easier than wood 20mm
        5,MM40,FFS, 5,MM40F3-1,F3S, 5,MM40B3-2,B3S, 5,MM40F2,F2S, 5,MM40M2,M2S, 5,MM40B2,B2S,
        6,MM14-12,FF, 6,MM14F3-20,F3, 6,MM14B3-22,B3, 6,80,F2, 6,50,M2, 6,90,B2,
        7,MM30-1,FFS, 7,MM30F3-5,F3S, 7,MM30B3-5,B3S, 7,26,F2S, 7,20,M2S, 7,45,B2S,
        8,MM10-10,FF, 8,MM10F3-30,F3, 8,MM10B3-35,B3,
        9,MM20-6,FFS, 9,MM20F3-12,F3S, 9,MM20B3-12,B3S, 9,35,F2S, 9,23,M2S, 9,55,B2S,
        10,MM8-5,FF, 10,MM8F3-30,F3, 10,MM8B3-35,B3,
        11,MM10-11,FFS, 11,MM10F3-33,F3S, 11,MM10B3-40,B3S,
        12,MM6-5,FF, 12,MM6F3-30,F3, 12,MM6B3-35,B3,
         13,19,F2, 13,14,M2, 13,29,B2,
         14,23,F2, 14,18,M2, 14,37,B2,
         15,35,F2, 15,25,M2, 15,55,B2,
         16,95,IN, 16,75,MI, 16,135,RI, 16,190,LI
};

private static final int[] gripValuesSoillboost = {
        1,1,FF, 1,2,F3, 1,3,B3,
        2,1,FF, 2,2,F3, 2,2,B3,
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



    private static final int[] bm1000Coordinates = {
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

    private static final int[] bm2000Coordinates = {
             1 , 0 , 47 , 285 , 47 ,
         2 , 59 , 39 , 224 , 39 ,
         3 , 115 , 29 , 170 , 29 ,
         4 , 115 , 50 , 115 , 50 ,
         5 , 163 , 50 , 163 , 50 ,
         6 , -4 , 71 , 282 , 71 ,
         7 , 31 , 71 , 249 , 71 ,
         8 , 59 , 71 , 222 , 71 ,
            9 , 94 , 71 , 187 , 71 ,
         10 , 137 , 71 , 138 , 71 ,
         11 , -4 , 102 , 282 , 102 ,
         12 , 31 , 102 , 249 , 102 ,
         13 , 59 , 102 , 222 , 102 ,
         14 , 94 , 102 , 187 , 102 ,
         15 , 137 , 102 , 138 , 102 ,


};

    private static final int[] transCoordinates = {
 1 , 87 , 9 , 240 , 9 ,
 2 , 77 , 22 , 240 , 22 ,
 3 , 67 , 33 , 240 , 33 ,
 4 , 57 , 48 , 240 , 48 ,
 5 , 47 , 57 , 240 , 57 ,
 6 , 37 , 68 , 240 , 68 ,
 7 , 20 , 80 , 240 , 80 ,
 8 , 15 , 90 , 240 , 90 ,
 9 , 10 , 100 , 240 , 100 ,

        };

    private static final int[] tensionCoordinates = {
 1 , 8 , 17 , 277 , 17 ,
 2 , 73 , 34 , 208 , 34 ,
 3 , 139 , 18 , 139 , 18 ,
 4 , 8 , 68 , 208 , 68 ,
 5 , 73 , 68 , 277 , 68 ,
 6 , 139 , 68 , 139 , 68 ,
 7 , 8 , 102 , 208 , 102 ,
 8 , 73 , 102 , 277 , 102 ,
 9 , 139 , 102 , 139 , 102 ,

        };

    private static final int[] tensionProCoordinates = {
            1 , 8 , 7,273 , 7,
            2 , 72, 8,205, 8,
            3 , 8, 31, 273, 31,
            4 , 72, 30,205, 30,
            5 , 8, 65,205 , 65,
            6 , 51 , 65,251 , 65,
            7 , 85, 65, 285, 65,
            8 , 137, 65, 137, 65,
            9 , 8, 104, 204 , 104,
            10, 72, 104, 272, 104,
            11, 137, 105, 137,105
    };

    private static final int[] zlagCoordinates = {
 1 , 4 , 24 , 278 , 24 ,
 2 , 67 , 32 , 218 , 32 ,
 3 , 143 , 32 , 143 , 32 ,
            4 , 4 , 53 , 278 , 53 ,
 5 , 67 , 53 , 218 , 53 ,
 6 , 143 , 53 , 143 , 53 ,
 7 , 4 , 76 , 278 , 76 ,
 8 , 48 , 76 , 233 , 76 ,
            9 , 85 , 76 , 199 , 76 ,
            10 , 143 , 76 , 143 , 76 ,
 11 , 143 , 76 , 143 , 76 ,
 12 , 4 , 93 , 278 , 93 ,
 13 , 48 , 93 , 233 , 93 ,
 14 , 85 , 93 , 199 , 93 ,
 15 , 143 , 93 , 143 , 93 ,

    };

    private static final int[] moonhardCoordinates = {
 1 , 9 , 34 , 275 , 34 ,
 2 , 73 , 35 , 210 , 35 ,
 3 , 121 , 34 , 172 , 34 ,
 4 , 9 , 59 , 275 , 59 ,
 5 , 73 , 58 , 210 , 58 ,
 6 , 9 , 89 , 275 , 89 ,
 7 , 73 , 80 , 210 , 80 ,
 8 , 140 , 89 , 140 , 89 ,
    };

    private static final int[] mooneasyCoordinates = {

 1 , 11 , 29 , 270 , 29 ,
 2 , 70 , 40 , 206 , 40 ,
 3 , 120 , 28 , 165 , 28 ,
 4 , 11 , 58 , 270 , 58 ,
 5 , 11 , 79 , 270 , 79 ,
 6 , 70 , 74 , 206 , 74 ,
 7 , 118 , 86 , 160 , 86 ,

        };

    private static final int[] metoCoordinates = {
             1 , -11 , 30 , 294 , 30 ,
         2 , 38 , 30 , 240 , 30 ,
         3 , 94 , 15 , 184 ,15 ,
         4 , -10 , 60 , 294 , 60 ,
         5 , 7 , 75 , 273 , 75 ,
         6 , 51 , 67 , 227 , 67 ,
         7 , 89 , 60 , 188 , 60 ,
         8 , 20 , 93 , 264 , 93 ,
         9 , 66 , 86 , 223 , 86 ,
         10 , 100 , 81 ,184 , 81 ,
         11 , 41 , 110 , 238 , 110 ,
         12 , 80 , 107 , 199 , 107 ,
         13 , 110 , 107 , 175 , 107 ,
         14 , 142 , 3 , 142 , 3 ,
         15 , 142 , 37 , 142 , 37 ,
         16 , 142 , 61 , 142 , 61 ,
         17 , 142 , 85 , 142 , 85 ,
         18 , 142 , 108 , 142 , 108 ,

    };

    private static final int[] rockprodigyCoordinates = {
 1 , -8 , 13 , 284 , 13 ,
 2 , 52 , 3 , 226 , 3 ,
 3 , 109 , 0 , 170 , 0 ,
 4 , 57 , 42 , 222 , 42 ,
 5 , 107 , 38 , 172 , 38 ,
 6 , 57 , 61 , 222 , 61 ,
 7 , 107 , 56 , 172 , 56 ,
 8 , 57 , 79 , 225 , 79 ,
 9 , 109 , 75 , 170 , 75 ,
 10 , 55 , 101 , 224 , 101 ,
 11 , 98 , 101 , 182 , 101 ,
 12 , 65 , 123 , 217 , 123 ,
 13 , 98 , 123 , 182 , 123 ,

    };

    private static final int[] problemsolverCoordinates = {
 1 , 36 , 1 , 239 , 1 ,
 2 , 36 , 45 , 239 , 45 ,
 3 , 13 , 79 , 267 , 79 ,
 4 , 53 , 79 , 224 , 79 ,
 5 , 101 , 79 , 176 , 79 ,
 6 , 141 , 79 , 141 , 79 ,
 7 , 14 , 111 , 267 , 111 ,
 8 , 53 , 111 , 224 , 111 ,
 9 , 101 , 111 , 176 , 111 ,
 10 , 141 , 111 , 141 , 111 ,

    };

    private static final int[] metocontactCoordinates = {
 1 , 53 , 1 , 231 , 1 ,
 2 , 81 , 2 , 198 , 2 ,
 3 , 114 , 2 , 167 , 2 ,
            4 , 117 , 15 , 164 , 15 ,
 5 , 51 , 44 , 232 , 44 ,
 6 , 81 , 44 , 199 , 44 ,
 7 , 116 , 48 , 165 , 48 ,
 8 , 22 , 66 , 264 , 66 ,
 9 , 51 , 66 , 230 , 66 ,
 10 , 81 , 66 ,200 , 66 ,
 11 , 116 , 76 , 166 , 76 ,
 12 , 17 , 87 , 264 , 87 ,
 13 , 51 , 86 , 230 , 86 ,
 14 , 81 , 85 , 201 , 85 ,
 15 , -19 , 102 , 299 , 102 ,
 16 , 17 , 106 , 264 , 106 ,
 17 , 51 , 105 , 230 , 105 ,
 18 , 81 , 104 , 202 , 104 ,
 19 , 116 , 103 , 167 , 103 ,

    };

    private static final int[] metowoodCoordinates = {
 1 , 15 , 10 , 275 , 10 ,
 2 , 91 , 18 , 191 , 18 ,
            3 , 58 , 49 , 223 , 49 ,
 4 , 91 , 49 , 191 , 49 ,
 5 , -8 , 74 , 290 , 74 ,
 6 , 37 , 74 , 247 , 74 ,
 7 , 82 , 74 , 201 , 74 ,
 8 , -8 , 100 , 290 , 100 ,
 9 , 45 , 100 , 238 , 100 ,
 10 , 97 , 100 , 186 , 100 ,


        };

    private static final int[] metoWoodDeluxeCoordinates = {
            1 , -3 , 5  , 283 , 5 ,
            2 , 53 , 9 , 227 , 9 ,
            3 , 111 , 9 , 169 , 9 ,
            4 , 4 , 47 , 271 , 47 ,
            5 , 53 , 47 , 224 , 47 ,
            6 , 93 , 47 , 189 , 47 ,
            7 , 140 , 47 , 140 , 47 ,
            8 , 12 , 80 , 268 , 80 ,
            9 , 61 , 80 , 219 , 80 ,
            10 , 95 , 80 , 183 , 80 ,
            11 , 140 , 80 , 140 , 80 ,
            12 , 16 , 113 , 259 , 113 ,
            13 , 63 , 113 , 215 , 113 ,
            14 , 97 , 113 , 181 , 113 ,
            15 , 139 , 113 , 139 , 113 ,

    };

    private static final int[] drccCoordinates = {
 1 , 20 , 13 , 257 , 13 ,
 2 , 70 , 8 , 206 , 8 ,
 3 , 117 , 13 , 159 , 13 ,
 4 , 22 , 48 , 255 , 48 ,
 5 , 72 , 37 , 204 , 37 ,
 6 , 118 , 45 , 158 , 45 ,
 7 , -18 , 79 , 291 , 79 ,
 8 , 26 , 79 , 251 , 79 ,
 9 , 72 , 66 , 204 , 66 ,
 10 , 112 , 80 , 164 , 80 ,

    };

    private static final int[] solutionCoordinates = {
 1 , 21 , 0 , 269 , 0 ,
 2 , 71 , 3 , 222 , 3 ,
 3 , 125 , 7 , 170 , 7 ,
 4 , 17 , 37 , 185 , 39 ,
 5 , 39 , 37 , 260 , 39 ,
 6 , 76 , 35 , 226 , 36 ,
 7 , 119 , 38 , 283 , 39 ,
 8 , 152 , 35 , 152 , 35 ,
 9 , 16 , 72 , 196 , 71 ,
 10 , 60 , 72 , 239 , 71 ,
 11 , 106 , 72 , 281 , 71 ,
 12 , 25 , 109 , 202 , 107 ,
 13 , 62 , 109 , 239 , 107 ,
 14 , 100 , 109 , 272 , 106 ,
 15 , 153 , 109 , 153 , 109 ,

    };

    private static final int[] edgeCoordinates = {
 1 , 0 , 11 , 176 , 11 ,
            2 , 51 , 11 , 227 , 11 ,
 3 , 101 , 11 , 281 , 11 ,
 4 , 0 , 44 , 176 , 44 ,
 5 , 51 , 44 , 227 , 44 ,
 6 , 101 , 44 , 281 , 44 ,
 7 , 0 , 76 , 176 , 76 ,
 8 , 51 , 76 , 227 , 76 ,
 9 , 101 , 76 , 281 , 76 ,
 10 , 0 , 109 , 176 , 109 ,
 11 , 51 , 109 , 227 , 109 ,
 12 , 101 , 109 , 281 , 109 ,

    };
    private static final int[] matrixCoordinates = {
            1 , 9 , 19 , 271 , 19 ,
            2 , 89 , 10 , 200 , 10 ,
            3 , 1 , 49 , 285 , 49 ,
            4 , 76 , 50 , 213 , 50 ,
            5 , 147 , 50 , 147 , 50 ,
            6 , 7 , 73 , 278 , 73 ,
            7 , 45 , 73 , 242 , 73 ,
            8 , 79 , 73 , 208 , 73 ,
            9 , 147 , 75 , 147 , 75 ,
            10 , 7 , 99 , 277 , 97 ,
            11 , 42 , 99 , 242 , 97 ,
            12 , 66 , 99 , 221 , 97 ,
            13 , 89 , 99 , 200 , 97 ,
            14 , 147 , 99 , 147 , 97 ,

    };

    private static final int[] coreCoordinates = {
            1 , 9 , 1 , 273 , 1 ,
            2 , 81 , 17 , 201 , 17 ,
            3 , 142 , 4 , 142 , 4 ,
            4 , -12 ,33  , 293 , 33 ,
            5 , 60 , 33 , 225 , 33 ,
            6 , 142 , 33 , 142 , 33 ,
            7 , 31 , 61 , 257 , 61 ,
            8 , 71 , 63 , 217 , 63 ,
            9 , 143 , 55 , 143 , 55 ,
            10 , -8 , 79 , 295 , 79 ,
            11 , 34 , 87 , 252 , 87 ,
            12 , 143 , 77 , 143 , 77 ,
            13 , 3 , 106 , 283 , 106 ,
            14 , 50 , 115 , 233 , 115 ,
            15 , 83 , 100 , 203 , 100 ,
            16 , 143 , 96 , 143 , 96 ,


    };
    private static final int[] soillboostCoordinates = {
 1 , 10 , 0 , 273 , 0 ,
 2 , 56 , 0 , 227 , 0 ,
 3 , 110 , 0 , 175 , 0 ,
 4 , 13 , 31 , 273 , 31 ,
 5 , 57 , 31 , 226 , 31 ,
 6 , 108 , 31 ,176 , 31 ,
 7 , 13 , 60 , 269 , 60 ,
 8 , 49 , 60 , 235 , 60 ,
 9 , 91 , 60 , 194 , 60 ,
 10 , 139 , 60 , 139 , 60 ,
 11 , 11 , 89 , 273 , 89 ,
 12 , 61 , 89 , 226 , 89 ,
 13 , 98 , 89 , 188 , 89 ,
 14 , 145 , 89 , 145 , 89 ,
 15 , 5 , 116 , 280 , 116 ,
 16 , 58 , 117 , 222 , 117 ,
 17 , 111 , 118 , 173 , 118 ,

    };

    private static final int[] ultimateCoordinates = {
 1 , 0 , 8 , 281 , 8 ,
 2 , 55 , 0 , 228 , 0 ,
 3 , 101 , 14 , 180 , 14 ,
 4 , 39 , 41 , 250 , 41 ,
 5 , 141 , 45 , 141 , 45 ,
 6 , 40 , 59 , 249 , 59 ,
 7 , 141 , 68 , 141 , 68 ,
 8 , 50 , 73 , 239 , 73 ,
 9 , 141 , 91 , 141 , 91 ,
 10 , 64 , 85 , 223 , 85 ,
 11 , 141 , 114 , 141 , 114 ,
 12 , 68 , 96 , 219 , 96 ,
 13 , 7 , 114 , 273 , 114 ,
 14 , 39 , 114 , 242 , 114 ,
 15 , 69 , 114 , 212 , 114 ,
 16 , 94 , 114 , 187 , 114
    };

    private static final int[] grillCoordinates = {
 1 , 15 , 12 , 273 , 12 ,
 2 , 48 , 20 , 231 , 20 ,
 3 , 95 , 8 , 180 , 8 ,
 4 , 79 , 45 , 204 , 45 ,
 5 , 121 , 45 , 160 , 45 ,
 6 , -8 , 70 , 295 , 70 ,
 7 , 53 , 69 , 230 , 69 ,
 8 , 116 , 69 , 165 , 69 ,
 9 , 23 , 93 , 259 , 93 ,
 10 , 61 , 97 , 221 , 97 ,
 11 , 90 , 95 , 193 , 95 ,
 12 , 118 , 95 , 165 , 95 ,
 13 , 141 , 96 , 141 , 96 ,

    };
    
    private static final int[] grilltoCoordinates = {
 1 , 3 , 7 , 288 , 7 ,
 2 , 57 , 9 , 225 , 9 ,
 3 , 123 , 13 , 154 , 13 ,
 4 , 140 , 36 , 140 , 36 ,
            5 , -7 , 59 , 289 , 59 ,
 6 , 50 , 60 , 233 , 60 ,
 7 , 112 , 59 , 168 , 59 ,
 8 , 27 , 91 , 256 , 91 ,
 9 , 71 , 91 , 209 , 91 ,
 10 , 113 , 91 , 167 , 91 ,
 11 , 141 , 91 , 141 , 91 ,
    };

    private static int[][] hangboardsGripValues = { gripValuesBM1000, gripValuesBM2000, gripValuesTrans,
            gripValuesTension,gripValuesTensionPro, gripValuesZlag, gripValuesMoonhard, gripValuesMooneasy, gripValuesMeto,
            gripValuesRockprodigy, gripValuesProblemsolver, gripValuesMetocontact, gripValuesMetowood, gripValuesMetoWoodDeluxe,
            gripValuesDrcc, gripValuesSolution, gripValuesEdge, gripValuesMatrix ,gripValuesCore,
            gripValuesSoillboost, gripValuesUltimate, gripValuesGrill, gripValuesGrillto,
            
    };
    
    private static int[][] hangboardsHoldCoordinates = {bm1000Coordinates,bm2000Coordinates,transCoordinates,
            tensionCoordinates, tensionProCoordinates, zlagCoordinates, moonhardCoordinates, mooneasyCoordinates, metoCoordinates,
            rockprodigyCoordinates, problemsolverCoordinates,
            metocontactCoordinates, metowoodCoordinates, metoWoodDeluxeCoordinates, drccCoordinates,
            solutionCoordinates, edgeCoordinates, matrixCoordinates, coreCoordinates,
            soillboostCoordinates, ultimateCoordinates, grillCoordinates, grilltoCoordinates
    };

    private static int[] benchmark_resources = {R.array.bm1000_benchmarks, R.array.bm2000_benchmarks,
            R.array.trans_benchmarks, R.array.tension_benchmarks,R.array.tension_pro_benchmarks, R.array.zlag_benchmarks,
            R.array.moonhard_benchmarks, R.array.mooneasy_benchmarks, R.array.meto_benchmarks,
            R.array.rockprodigy_benchmarks, R.array.problemsolver_benchmarks,
            R.array.meto_contact_benchmarks, R.array.meto_wood_benchmarks, R.array.meto_wood_deluxe_benchmarks, R.array.drcc_benchmarks,
            R.array.solution_benchmarks, R.array.edge_benchmarks, R.array.matrix_benchmarks, R.array.core_benchmarks, R.array.soillboost_benchmarks,
            R.array.ultimate_benchmarks, R.array.grill_benchmarks, R.array.grillto_benchmarks
    };

    private static int[] image_resources = {R.drawable.lauta1011, R.drawable.lauta2002, R.drawable.trans,
            R.drawable.tension, R.drawable.tension_pro, R.drawable.zlag, R.drawable.moonhard, R.drawable.mooneasy, R.drawable.meto,
            R.drawable.rockprodigy, R.drawable.problemsolver, R.drawable.meto_contact, R.drawable.meto_wood,
            R.drawable.meto_wood_deluxe,
            R.drawable.drcc,R.drawable.solution, R.drawable.edge, R.drawable.matrix,
            R.drawable.core, R.drawable.soillboost, R.drawable.ultimate,
            R.drawable.grill, R.drawable.grillto};


    private static String[] hangboardStrings = {"BM 1000", "BM 2000", "Transgression","Tension","Tension Pro",
            "Zlagboard","Moonboard hard","Moonboard easy","Metolius","Rock Prodigy","problemsolver","Meto. Contact",
            "Meto. Wood", "Meto. Wood Deluxe","DRCC","Solution","Edge","Matrix","Core",
            "So iLL Boost","Ultimate", "Grill", "Grillto"};

    public static int getHoldDifficulty(Hold hold, String hbName) {
        int position = getHangboardPosition(hbName);

        int[] holdResources = hangboardsGripValues[position];

        for (int i = 0 ; i < holdResources.length ; i++) {
            Hold tempHold = new Hold(holdResources[i] );
            i++;
            tempHold.setHoldValue(holdResources[i]);
            i++;
            tempHold.setGripTypeAndSingleHold(holdResources[i] );
            //Log.d("TempHold",tempHold.getHoldNumber() + " " + tempHold.getGripStyleInt() );
            //Log.d("hold",hold.getHoldNumber() + " " + hold.getGripStyleInt() );

            if (tempHold.isEqual(hold)) {
                // Log.d("hold found",tempHold.getHoldNumber()+ " " + tempHold.getGripStyle() + " " + tempHold.getHoldValue());
                return  tempHold.getHoldValue();
            }
        }
        return 0;


    }

    public static int getBenchmarkResources(int position) {
        if (position >= 0 && position < benchmark_resources.length) {
            return benchmark_resources[position];
        }
        return R.array.noBenchmarks;
    }

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
/*

        public static int getHoldValueResources(hangboardName hangboard) {
                int position = hangboard.ordinal();
              //  Log.d("HoldValueResources","HANGBOARD: " +hangboard.toString() + "  pos: " + position);
                if (position >= 0 && position < hold_resources.length) {
                    return hold_resources[position];
                }
           // Log.e("ERR","ERROR getholdvalueresources");
                return hold_resources[0];
        }
*/

        public static int[] getHoldCoordinates(hangboardName hangboard) {
/*

            int holdCoordinateResources = HangboardResources.getHoldCoordinates(hangboard);
            int[] testHoldCoordinates = res.getIntArray(holdCoordinateResources);
            if (hangboardsHoldCoordinates.length != coordinate_resources.length ) {
                Log.e("SIZE","resource arrays not the same size");
            }
            TESTisArraysTheSame(hangboardsHoldCoordinates[hangboard.ordinal()], testHoldCoordinates);
*/

            int position = hangboard.ordinal();
            if ( position >= 0 && position < hangboardsHoldCoordinates.length) {
                return hangboardsHoldCoordinates[position];
            }
            return hangboardsHoldCoordinates[0];
        }

        public static int[] getGripValues(hangboardName hangboard) {
  /*          int gripValueResources = HangboardResources.getHoldValueResources(hangboard);
            int[] testGripValues = res.getIntArray(gripValueResources);
            if (hangboardsGripValues.length != hold_resources.length ) {
                Log.e("SIZE","grip resource arrays not the same size");
            }
            TESTisArraysTheSame(hangboardsGripValues[hangboard.ordinal()], testGripValues);
*/
            int position = hangboard.ordinal();
            if ( position >= 0 && position < hangboardsGripValues.length) {
                return hangboardsGripValues[position];
            }

            return hangboardsGripValues[0];
        }

        private static void TESTisArraysTheSame(int[] array1, int[] array2) {
         if (array1.length != array2.length ) {
             Log.e("SIZE","Arrays are not even the same size");
            return;
         }

         boolean isSame = true;
         for (int i = 0 ; i < array1.length ; i++ ) {
             if (array1[i] != array2[i] ) {
                 Log.e("VALUE DIFF","pos: " + i + "   array1: " + array1[i] + "  array2: " + array2[i]);
                isSame = false;
             }
         }

         if (isSame) {
             Log.d("OK","arrays the same");
         }

        }
/*

        public static int getHoldCoordinates(hangboardName hangboard) {
                int position = hangboard.ordinal();
              //  Log.d("HoldCoordinates","HANGBOARD: " +hangboard.toString() + "  pos: " + position);
                if(position >= 0 && position < coordinate_resources.length) {
                    return coordinate_resources[position];
                }
           // Log.e("ERR","ERROR getholdcoordinates");
                return coordinate_resources[0];
        }
*/

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
