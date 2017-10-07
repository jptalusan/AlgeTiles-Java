package com.freelance.jptalusan.algetiles.Utilities;

import android.graphics.Rect;
import android.util.Log;
import android.widget.RelativeLayout;

import com.freelance.jptalusan.algetiles.ExtendedViews.AlgeTilesTextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by jptalusan on 10/1/17.
 */

public class AlgorithmUtilities {
    private static String TAG = "AlgorithmUtilities";
    public static ArrayList<Integer> RNG(String activityType, int numberOfVariables)
    {
        int[] multipyOneVarChoices = { -3, -1, 0, 1, 3 };
        int[] multipyTwoVarChoices = { -2, -1, 0, 1, 2 };
        int a = 0;
        int b = 0;
        int c = 0;
        int d = 0;
        int e = 0;
        int f = 0;

        long seed = System.currentTimeMillis();
        Random rnd = new Random((int)seed);

        ArrayList<Integer> vars = new ArrayList<>();

        Log.d(TAG, activityType + "," + numberOfVariables);

        if (Constants.MULTIPLY == activityType)
        {
            //(ax + b)(cx + d)
            if (Constants.ONE_VAR == numberOfVariables)
            {
                while (a == 0 && b == 0)
                {
                    //a = multipyOneVarChoices[rnd.Next(multipyOneVarChoices.Length)];
                    a = PickRandom(rnd, multipyOneVarChoices);
                    b = a >= 0 ? PickRandom(rnd, -9, 9 - (3 * a)) : PickRandom(rnd, -9 - (3 * a), 9);
                }
                while (c == 0 && d == 0)
                {
                    c = PickRandom(rnd, multipyOneVarChoices);
                    d = c >= 0 ? PickRandom(rnd, -9, 9 - (3 * c)) : PickRandom(rnd, -9 - (3 * c), 9);
                }

                vars.add(a);
                vars.add(b);
                vars.add(c);
                vars.add(d);
            }
            //(ax + by + c)(dx + ey + f)
            else if (Constants.TWO_VAR == numberOfVariables)
            {

                a = PickRandom(rnd, multipyTwoVarChoices);
                b = a > 0 ? PickRandom(rnd, -2, 2 - a) : PickRandom(rnd, -2 - (2 * a), 2);
                //e
                if (a >= 0 && b >= 0)
                    c = PickRandom(rnd, -8, 8 - (3 * a) - (3 * b));
                if (a >= 0 && b < 0)
                    c = PickRandom(rnd, -8 - (3 * b), 8 - (3 * a));
                if (a < 0 && b >= 0)
                    c = PickRandom(rnd, -8 - (3 * a), 8 - (3 * b));
                if (a < 0 && b < 0)
                    c = PickRandom(rnd, -8 - (3 * a) - (3 * b), 8);

                d = PickRandom(rnd, multipyTwoVarChoices);
                e = d > 0 ? PickRandom(rnd, -2, 2 - d) : PickRandom(rnd, -2 - (2 * d), 2);
                //f
                if (d >= 0 && e >= 0)
                    f = PickRandom(rnd, -8, 8 - (3 * d) - (3 * e));
                if (d >= 0 && e < 0)
                    f = PickRandom(rnd, -8 - (3 * e), 8 - (3 * d));
                if (d < 0 && e >= 0)
                    f = PickRandom(rnd, -8 - (3 * d), 8 - (3 * e));
                if (d < 0 && e < 0)
                    f = PickRandom(rnd, -8 - (3 * d) - (3 * e), 8);

                //TODO: add more values for e and f generation
                vars.add(a); //ax
                vars.add(b); //by
                vars.add(c); //c

                vars.add(d); //ax
                vars.add(e); //by
                vars.add(f); //c
            }
        } else if (Constants.FACTOR == activityType)
        {
            Log.d(TAG, "RNG: factor");
            //(ax + b)(cx + d)
            if (Constants.ONE_VAR == numberOfVariables)
            {
                while (a == 0 && b == 0)
                {
                    a = PickRandom(rnd, -3, 3);
                    b = a >= 0 ? PickRandom(rnd, -9, 9 - (3 * a)) : PickRandom(rnd, -9 - (3 * a), 9);
                }

                while (c == 0 && d == 0)
                {
                    c = PickRandom(rnd, -3, 3);
                    d = c >= 0 ? PickRandom(rnd, -9, 9 - (3 * c)) : PickRandom(rnd, -9 - (3 * c), 9);
                }

                int gcdOfFirst = GCD(a, b);
                int gcdOfSecond = GCD(c, d);

                a /= gcdOfFirst;
                b /= gcdOfFirst;
                c /= gcdOfSecond;
                d /= gcdOfSecond;

                vars.add(a);
                vars.add(b);
                vars.add(c);
                vars.add(d);
            }
        }

        //TODO: Fix for 2 variables
        if (areConstantsOnlyOneWwithValues(activityType, vars, numberOfVariables)) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException err) {
                err.printStackTrace();
            }
            return RNG(activityType, numberOfVariables);
        }

        return vars;
    }

    private static boolean areConstantsOnlyOneWwithValues(String activityType, ArrayList<Integer> vars, int numberOfVariables)
    {
        Log.d(TAG, "vars: " + vars.toString());
        if (numberOfVariables == 2)
        {
            int ax = vars.get(0);
            int by = vars.get(1);
            int dx = vars.get(3);
            int ey = vars.get(4);

            int total1 = ax + by;
            int total2 = dx + ey;
            if (total1 == 0 || total2 == 0)
                return true;
            return false;
        }
        else if (Constants.MULTIPLY.equals(activityType))
        {
            int ax = vars.get(0);
            int b = vars.get(1);
            int cx = vars.get(2);
            int d = vars.get(3);
            if (ax == 0 || cx == 0)
                return true;
        }
        else if (Constants.FACTOR.equals(activityType))
        {
            int ax = vars.get(0);
            int b = vars.get(1) ;
            int cx = vars.get(2);
            int d = vars.get(3);

            if (ax == 0 || b == 0 || cx == 0 || d == 0)
                return true;
        }
        return false;
    }

    public static int PickRandom(Random rnd, int[]Selection)
    {
        return Selection[rnd.nextInt(Selection.length)];
    }

    public static int PickRandom(Random rnd, int a, int b)
    {
        Log.d(TAG, "bound (b-a): " + b + "-" + a);
        if (a == b) {
            return 0;
        }
        return rnd.nextInt(b - a) + a;
    }

    public static boolean isFirstAnswerCorrect(ArrayList<Integer> vars, GridValue[] gvArr, int numberOfVariables)
    {
        Log.d(TAG, "isFirstAnswerCorrect");
        for (int i : vars)
        {
            Log.d(TAG, i + "");
        }

        Log.d(TAG, "Mult: GvArr: midUpGV, midLowGV, midLeftGV, midRightGV");
        for (int i = 0; i < gvArr.length; ++i)
        {
            Log.d(TAG, "GvArr:" + gvArr[i].toString());
        }

        if (Constants.ONE_VAR == numberOfVariables)
        {
            //For 1 variable
            int a = 0;
            int b = 0;
            int c = 0;
            int d = 0;

            //Should expand/change when checking for outer grids
            GridValue midUp = gvArr[0];
            GridValue midLo = gvArr[1];
            GridValue midLeft = gvArr[2];
            GridValue midRight = gvArr[3];

            a = vars.get(0);
            b = vars.get(1);
            c = vars.get(2);
            d = vars.get(3);

            if ((a == GridValue.subtract(midUp, midLo).xTileCount && b == GridValue.subtract(midUp, midLo).oneTileCount) &&
                    (c == GridValue.subtract(midRight, midLeft).xTileCount && d == GridValue.subtract(midRight, midLeft).oneTileCount))
            {
                return true;
            }
            else if ((a == GridValue.subtract(midRight, midLeft).xTileCount && b == GridValue.subtract(midRight, midLeft).oneTileCount) &&
                    (c == GridValue.subtract(midUp,  midLo).xTileCount && d == GridValue.subtract(midUp, midLo).oneTileCount))
            {
                return true;
            }
            else
            {
                return false;
            }
        }
        else
        {
            int a = 0;
            int b = 0;
            int c = 0;
            int d = 0;
            int e = 0;
            int f = 0;

            //Should expand/change when checking for outer grids
            GridValue midUp = gvArr[0];
            GridValue midLo = gvArr[1];
            GridValue midLeft = gvArr[2];
            GridValue midRight = gvArr[3];

            a = vars.get(0); //x
            b = vars.get(1); //y
            c = vars.get(2); //one

            d = vars.get(3); //x
            e = vars.get(4); //y
            f = vars.get(5); //one

            if ((a == GridValue.subtract(midUp, midLo).xTileCount && b == GridValue.subtract(midUp, midLo).yTileCount && c == GridValue.subtract(midUp, midLo).oneTileCount) &&
                    (d == GridValue.subtract(midRight, midLeft).xTileCount && e == GridValue.subtract(midRight, midLeft).yTileCount && f == GridValue.subtract(midRight, midLeft).oneTileCount))
            {
                return true;
            }
            else if ((a == GridValue.subtract(midRight, midLeft).xTileCount && b == GridValue.subtract(midRight, midLeft).yTileCount && c == GridValue.subtract(midRight, midLeft).oneTileCount) &&
                    (d == GridValue.subtract(midUp, midLo).xTileCount && e == GridValue.subtract(midUp, midLo).yTileCount && f == GridValue.subtract(midUp, midLo).oneTileCount))
            {
                return true;
            }
            else
            {
                return false;
            }
        }
    }

    public static boolean isSecondAnswerCorrect(ArrayList<Integer> vars, GridValue[] gvArr, int numberOfVariables)
    {
        Log.d(TAG, "isSecondAnswerCorrect");
        for (int i : vars)
        {
            Log.d(TAG, i + "");
        }
        Log.d(TAG, "Mult: GvArr:  upperLeftGV, upperRightGV, lowerLeftGV, lowerRightGV ");
        for (int i = 0; i < gvArr.length; ++i)
            Log.d(TAG, "GvArr:" + gvArr[i].toString());
        if (Constants.ONE_VAR == numberOfVariables)
        {
            //For 1 variable
            Log.d(TAG, "One var");
            int a = 0;
            int b = 0;
            int c = 0;

            //Should expand/change when checking for outer grids
            GridValue upLeft = gvArr[0];
            GridValue upRight = gvArr[1];
            GridValue downLeft = gvArr[2];
            GridValue downRight = gvArr[3];

            a = vars.get(0);
            b = vars.get(1);
            c = vars.get(2);

            if (a == GridValue.subtract(GridValue.add(upRight, downLeft), GridValue.add(upLeft, downRight)).x2TileCount &&
                    b == GridValue.subtract(GridValue.add(upRight, downLeft), GridValue.add(upLeft, downRight)).xTileCount &&
                    c == GridValue.subtract(GridValue.add(upRight, downLeft), GridValue.add(upLeft, downRight)).oneTileCount)
                return true;
            else
            {
                return false;
            }
        } else
        {
            Log.d(TAG, "Two var");
            int a = 0;
            int b = 0;
            int c = 0;
            int d = 0;
            int e = 0;
            int f = 0;

            //Should expand/change when checking for outer grids
            GridValue upLeft = gvArr[0];
            GridValue upRight = gvArr[1];
            GridValue downLeft = gvArr[2];
            GridValue downRight = gvArr[3];

            a = vars.get(0); //x2
            b = vars.get(1); //y2
            c = vars.get(2); //xy
            d = vars.get(3); //x
            e = vars.get(4); //y
            f = vars.get(5); //one

            if (a == GridValue.subtract(GridValue.add(upRight, downLeft), GridValue.add(upLeft, downRight)).x2TileCount &&
                    b == GridValue.subtract(GridValue.add(upRight, downLeft), GridValue.add(upLeft, downRight)).y2TileCount &&
                    c == GridValue.subtract(GridValue.add(upRight, downLeft), GridValue.add(upLeft, downRight)).xyTileCount &&
                    d == GridValue.subtract(GridValue.add(upRight, downLeft), GridValue.add(upLeft, downRight)).xTileCount &&
                    e == GridValue.subtract(GridValue.add(upRight, downLeft), GridValue.add(upLeft, downRight)).yTileCount &&
                    f == GridValue.subtract(GridValue.add(upRight, downLeft), GridValue.add(upLeft, downRight)).oneTileCount)
                return true;
            else
            {
                return false;
            }
        }
    }

    public static ArrayList<Integer> expandingVars(ArrayList<Integer> vars)
    {
        ArrayList<Integer> output = new ArrayList<>();
        //For 1 variable
        int a = 0;
        int b = 0;
        int c = 0;
        int d = 0;
        //For 2 variables
        int e = 0;
        int f = 0;
        if (vars.size() <= 4)
        {
            a = vars.get(0);
            b = vars.get(1);
            c = vars.get(2);
            d = vars.get(3);

            output.add(a * c);
            output.add((a * d) + (b * c));
            output.add(b * d);
        } else //two variables
        {
            a = vars.get(0); //ax
            b = vars.get(1); //by
            c = vars.get(2); //c

            d = vars.get(3); //dx
            e = vars.get(4); //ey
            f = vars.get(5); //f

            output.add(a * d); //x2
            output.add(b * e); //y2
            output.add((a * e) + (b * d)); //xy
            output.add((a * f) + (c * d)); //x
            output.add((b * f) + (c * e)); //y
            output.add(c * f); //one
        }
        if (vars.size() <= 4)
            Log.d(TAG, "x2, x, 1");
        else
            Log.d(TAG, "x2, y2, xy, x, y, 1");
        for (int i : output)
        Log.d(TAG, "Expanded: " + i);
        return output;
    }

    private static String removeSpacesFromString(String input)
    {
        return input.replaceAll("\\s", "");
    }

    public static boolean isThirdAnswerCorrect(ArrayList<Integer> vars, String answer)
    {
        answer = removeSpacesFromString(answer);

        return false;
    }

    public static Rect getRectOfView(AlgeTilesTextView alIV)
    {
        RelativeLayout.LayoutParams rPrms = (RelativeLayout.LayoutParams)alIV.getLayoutParams();
        return new Rect(rPrms.leftMargin, rPrms.topMargin, rPrms.leftMargin + rPrms.width, rPrms.topMargin + rPrms.height);
    }

    public static int GCD(int p, int q)
    {
        if (q == 0)
        {
            return p;
        }

        int r = p % q;

        return GCD(q, r);
    }

    /*Check:
     * if it will fit maximum size of grid
     * if it is all integers
    */
    public static boolean isSuppliedMultiplyEquationValid(int[] vars, int numberOfVariables)
    {
        Log.d(TAG, "isSupplied: " + vars.toString() + ", " + numberOfVariables);
        if (Constants.ONE_VAR == numberOfVariables)
        {
            int ax = vars[0];
            int b = vars[1];

            int cx = vars[2];
            int d = vars[3];

//            Console.WriteLine("isSuppliedValid: " + ax + "," + b + "," + cx + "," + d);

            boolean firstEq = false;
            boolean secondEq = false;

            if (ax > 0 && b > 0)
            {
                if ((Math.abs(ax) * 3 + Math.abs(b)) > 9)
                {
//                    Console.WriteLine("Here?" + Math.abs(ax) * 3);
                    firstEq = false;
                }
                else
                {
                    firstEq = true;
                }
            }
            else
            {
                if ((Math.abs(ax) * 3) > 9 ||
                        Math.abs(b) > 9)
                {
                    firstEq = false;
                }
                else
                {
                    firstEq = true;
                }
            }

            if (cx > 0 && d > 0)
            {
                if ((Math.abs(cx) * 3 + Math.abs(d)) > 9)
                {
                    secondEq = false;
                }
                else
                {
                    secondEq = true;
                }
            }
            else
            {
                if ((Math.abs(cx) * 3) > 9 ||
                        Math.abs(d) > 9)
                {
                    secondEq = false;
                }
                else
                {
                    secondEq = true;
                }
            }

//            Console.WriteLine("End: " + firstEq + "," + secondEq);
            return firstEq && secondEq;
        }
        else if (Constants.TWO_VAR == numberOfVariables)
        {
            int ax = vars[0];
            int by = vars[1];
            int c = vars[2];

            int dx = vars[3];
            int ey = vars[4];
            int f = vars[5];

            boolean firstEq = false;
            boolean secondEq = false;

            Log.d(TAG, "isSuppliedValid: (" + ax + "," + by + "," + c + ")(" + dx + "," + ey + "," + f);
//            Console.WriteLine("isSuppliedValid: " + ax + "," + by + "," + c + ")(" + dx + "," + ey + "," + f);

            if (c > 0)
            {
                if (ax > 0)
                {
                    if (by > 0)
                    {
                        if (ax * 3 + by * 3 + c > 9)
                        {
                            firstEq = false;
                        }
                        else
                        {
                            firstEq = true;
                        }
                    }
                    else if (by < 0)
                    {
                        if (ax * 3 + c > 9 ||
                                by * 3 < -9)
                        {
                            firstEq = false;
                        }
                        else
                        {
                            firstEq = true;
                        }
                    }
                }
                else if (ax < 0)
                {
                    if (by > 0)
                    {
                        if (by * 3 + c > 9 ||
                                ax * 3 < -9)
                        {
                            firstEq = false;
                        }
                        else
                        {
                            firstEq = true;
                        }
                    }
                    else if (by < 0)
                    {
                        if (ax * 3 < -9 ||
                                by * 3 < -9 ||
                                c > 9)
                        {
                            firstEq = false;
                        }
                        else
                        {
                            firstEq = true;
                        }
                    }
                }
            }
            else if (c < 0)
            {
                if (ax > 0)
                {
                    if (by > 0)
                    {
                        if (ax * 3 + by * 3 > 9 ||
                                c < -9)
                        {
                            firstEq = false;
                        }
                        else
                        {
                            firstEq = true;
                        }
                    }
                    else if (by < 0)
                    {
                        if (by * 3 + c < -9 ||
                                ax * 3 > 9)
                        {
                            firstEq = false;
                        }
                        else
                        {
                            firstEq = true;
                        }
                    }
                }
                else if (ax < 0)
                {
                    if (by > 0)
                    {
                        if (ax * 3 + c < -9 ||
                                by * 3 > 9)
                        {
                            firstEq = false;
                        }
                        else
                        {
                            firstEq = true;
                        }
                    }
                    else if (by < 0)
                    {
                        if (ax * 3 + by * 3 + c > 9)
                        {
                            firstEq = false;
                        }
                        else
                        {
                            firstEq = true;
                        }
                    }
                }
            }

            if (f > 0)
            {
                if (dx > 0)
                {
                    if (ey > 0)
                    {
                        if (dx * 3 + ey * 3 + f > 9)
                        {
                            secondEq = false;
                        }
                        else
                        {
                            secondEq = true;
                        }
                    }
                    else if (ey < 0)
                    {
                        if (dx * 3 + f > 9 ||
                                ey * 3 < -9)
                        {
                            secondEq = false;
                        }
                        else
                        {
                            secondEq = true;
                        }
                    }
                }
                else if (dx < 0)
                {
                    if (ey > 0)
                    {
                        if (ey * 3 + f > 9 ||
                                dx * 3 < -9)
                        {
                            secondEq = false;
                        }
                        else
                        {
                            secondEq = true;
                        }
                    }
                    else if (ey < 0)
                    {
                        if (dx * 3 < -9 ||
                                ey * 3 < -9 ||
                                f > 9)
                        {
                            secondEq = false;
                        }
                        else
                        {
                            secondEq = true;
                        }
                    }
                }
            }
            else if (f < 0)
            {
                if (dx > 0)
                {
                    if (ey > 0)
                    {
                        if (dx * 3 + ey * 3 > 9 ||
                                f < -9)
                        {
                            secondEq = false;
                        }
                        else
                        {
                            secondEq = true;
                        }
                    }
                    else if (ey < 0)
                    {
                        if (ey * 3 + f < -9 ||
                                dx * 3 > 9)
                        {
                            secondEq = false;
                        }
                        else
                        {
                            secondEq = true;
                        }
                    }
                }
                else if (dx < 0)
                {
                    if (ey > 0)
                    {
                        if (dx * 3 + f < -9 ||
                                ey * 3 > 9)
                        {
                            secondEq = false;
                        }
                        else
                        {
                            secondEq = true;
                        }
                    }
                    else if (ey < 0)
                    {
                        if (dx * 3 + ey * 3 + f > 9)
                        {
                            secondEq = false;
                        }
                        else
                        {
                            secondEq = true;
                        }
                    }
                }
            }

//            Console.WriteLine("End: " + firstEq + "," + secondEq);
            return firstEq && secondEq;
        } else
        {
            return false;
        }
    }

    /* Pick from pre-supplied list of equations
     * Use the show equation method in the activies to display this
     */
    public static ArrayList<Integer> parseEquation(String equationCSV)
    {
        ArrayList<Integer> output = new ArrayList<>();
        String[] vals = equationCSV.split(",");

        int cnt = 0;
        for(String s : vals)
        {
            output.add(Integer.parseInt(s));
            cnt++;
        }
        return output;
    }
}
