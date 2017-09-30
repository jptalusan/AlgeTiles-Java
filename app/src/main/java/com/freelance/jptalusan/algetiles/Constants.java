package com.freelance.jptalusan.algetiles;

import java.util.ArrayList;

public class Constants {
    public static String FACTOR = "Factor";
    public static String MULTIPLY = "Multiply";
    public static int ONE_VAR = 1;
    public static int TWO_VAR = 2;
    public static String NEW_Q = "new Q";
    public static String CHK = "chk";
    public static String REFR = "refr";
    public static String MUTE = "mute";
    public static String FIRST_TIME = "firsttime";

    public static String DRAG = "drag";
    public static String REMOVE = "remove";
    public static String ROTATE = "rotate";

    public static String VARIABLE_COUNT = "variable_count";
    public static String VIDEO_ID = "video_id";

    public static String ONE_TILE = "tile_1";
    public static String X_TILE = "x_tile";
    public static String Y_TILE = "y_tile";
    public static String X2_TILE = "x2_tile";
    public static String Y2_TILE = "y2_tile";
    public static String XY_TILE = "xy_tile";

    public static String ONE_TILE_ROT = "tile_1_rot";
    public static String X_TILE_ROT = "x_tile_rot";
    public static String Y_TILE_ROT = "y_tile_rot";
    public static String X2_TILE_ROT = "x2_tile_rot";
    public static String Y2_TILE_ROT = "y2_tile_rot";
    public static String XY_TILE_ROT = "xy_tile_rot";

    public static String BUTTON_TYPE = "BUTTON_TYPE";
    public static String CLONED_BUTTON = "CLONE_BUTTON";
    public static String ORIGINAL_BUTTON = "ORIGINAL_BUTTON";
    //TODO:static  Add for y, xy, x2 and rotated versions (probably just for y and xy)

    public static int SUBTRACT = 0;
    public static int ADD = 1;
    public static int DELAY = 1500;
    public static int CANCELOUT_DELAY = 1500;

    public static double SNAP_GRID_INTERVAL = 9.0;

    public static double X_LONG_SIDE = 3.0;
    public static double ONE_SIDE = 9.0;
    public static double Y_LONG_SIDE = 2.6;

    public static String PROCEED = "Proceed to";
    public static String CORRECT = "Correct";
    public static String WRONG = "Wrong";
    public static String MULTIPLICATION= " multiplication";
    public static String COEFFICIENTS = " coefficients";

    public static String CORRECT_PLACEMENT = "Correct Placement of Tiles";
    public static String PROCEED_TO_FACTOR = "Proceed to Factoring";
    public static String PROCEED_TO_MULTIP = "Proceed to Multiplication";
    public static String CORRECT_FACTORS = "Correct Factors";
    public static String CORRECT_MULTIP = "Correct Multiplication";
    public static String PROCEED_TO_COEFF = "Proceed to coefficients";


    public static String EQUATION_001 = "1,1,1,-6";
    public static String EQUATION_002 = "2,-1,1,4";
    public static String EQUATION_003 = "2,0,1,3";
    public static String EQUATION_004 = "1,-3,1,3";
    public static String EQUATION_005 = "1,2,1,2";
    public static String EQUATION_006 = "2,-1,2,1";
    public static String EQUATION_007 = "1,0,1,-4";
    public static String EQUATION_008 = "1,-2,1,3";
    public static String EQUATION_009 = "-2,5,-3,7";
    public static String EQUATION_010 = "-2,5,-1,1";
    public static String EQUATION_011 = "1,4,3,-2";
    public static String EQUATION_012 = "2,3,1,-4";

    //Equations
    public static ArrayList<String> EQUATIONS = new ArrayList<String>()
    {{
                add(EQUATION_001);
                add(EQUATION_002);
                add(EQUATION_003);
                add(EQUATION_004);
                add(EQUATION_005);
                add(EQUATION_006);
                add(EQUATION_007);
                add(EQUATION_008);
                add(EQUATION_009);
                add(EQUATION_010);
                add(EQUATION_011);
                add(EQUATION_012);
    }};
}
