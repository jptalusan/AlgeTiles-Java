package com.freelance.jptalusan.algetiles.ExtendedViews;

import android.app.Application;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.freelance.jptalusan.algetiles.R;
import com.freelance.jptalusan.algetiles.Utilities.AlgorithmUtilities;
import com.freelance.jptalusan.algetiles.Utilities.Constants;
import com.freelance.jptalusan.algetiles.Utilities.GridValue;
import com.freelance.jptalusan.algetiles.Utilities.Listeners;
import com.freelance.jptalusan.algetiles.Utilities.RectTile;
import com.freelance.jptalusan.algetiles.Utilities.TileUtilities;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jptalusan on 10/5/17.
 */

public abstract class AlgeTilesActivity extends AppCompatActivity {
    private static String TAG = "AlgeTiles:AlgeTilesActivity";
    public String activityType = "";

    public TextView result;
    public Boolean hasButtonBeenDroppedInCorrectzone = false;
    public String currentButtonType = "";
    public ViewGroup currentOwner;

    public Button tutorialButton;

    public ToggleButton removeToggle;
    public ToggleButton dragToggle;
    public ToggleButton rotateToggle;
    public ToggleButton muteToggle;

    public AlgeTilesTextView tile_1;
    public AlgeTilesTextView x_tile;
    public AlgeTilesTextView y_tile;
    public AlgeTilesTextView x2_tile;
    public AlgeTilesTextView y2_tile;
    public AlgeTilesTextView xy_tile;

    public Button newQuestionButton;
    public Button refreshButton;
    public Button checkButton;

    public int numberOfVariables = 0;

    public Boolean isFirstAnswerCorrect = false;
    public Boolean isSecondAnswerCorrect = false;
    public Boolean isThirdAnswerCorrect = false;

    public AlgeTilesRelativeLayout upperLeftGrid;
    public AlgeTilesRelativeLayout upperRightGrid;
    public AlgeTilesRelativeLayout lowerLeftGrid;
    public AlgeTilesRelativeLayout lowerRightGrid;

    public GridLayout upperMiddleGrid;
    public GridLayout middleLeftGrid;
    public GridLayout middleRightGrid;
    public GridLayout lowerMiddleGrid;

    //Four outer grids
    public GridValue upperLeftGV;
    public GridValue upperRightGV;
    public GridValue lowerLeftGV;
    public GridValue lowerRightGV;

    //Four center grids
    public GridValue midUpGV;
    public GridValue midLowGV;
    public GridValue midLeftGV;
    public GridValue midRightGV;

    public ArrayList<Integer> vars = new ArrayList<>();
    public ArrayList<Integer> expandedVars = new ArrayList<>();
    public ArrayList<ViewGroup> innerGridLayoutList = new ArrayList<>();
    public ArrayList<ViewGroup> outerGridLayoutList = new ArrayList<>();
    public ArrayList<GridValue> gridValueList = new ArrayList<>();

    public MediaPlayer correct;
    public MediaPlayer incorrect;

    public EditText x2ET;
    public EditText y2ET;
    public EditText xyET;
    public EditText xET;
    public EditText yET;
    public EditText oneET;

    public ArrayList<EditText> editTextList = new ArrayList<>();

    public ScrollView sv;

    public boolean isFirstTime = false;

    public int heightInPx = 0;
    public int widthInPx = 0;

    public ArrayList<RectTile> upperRightRectTileList = new ArrayList<>();
    public ArrayList<RectTile> upperLeftRectTileList = new ArrayList<>();
    public ArrayList<RectTile> lowerRightRectTileList = new ArrayList<>();
    public ArrayList<RectTile> lowerLeftRectTileList = new ArrayList<>();

    public ArrayList<ArrayList<RectTile>> rectTileListList = new ArrayList<>();

    public Listeners listeners;
    public SharedPreferences prefs;

    public Dialog settingsDialog;

    public TextView x2TV;
    public TextView y2TV;

    public Boolean oneTile_Clicked;
    public Boolean xTile_Clicked;
    public Boolean x2Tile_Clicked;
    public Boolean xyTile_Clicked;
    public Boolean yTile_Clicked;
    public Boolean y2Tile_Clicked;

    public int layoutResource = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResource());
    }

    protected abstract int getLayoutResource();
    protected abstract void setupQuestionString(ArrayList<Integer> vars);

    public void resetBGColors(ArrayList<ViewGroup> vL)
    {
        for (ViewGroup v : vL)
        {
            if (v instanceof AlgeTilesRelativeLayout)
            {
                AlgeTilesRelativeLayout temp = (AlgeTilesRelativeLayout)v;
                temp.resetColor();
            }
 				else
            {
                v.setBackgroundResource(R.drawable.shape);
            }
        }
    }

    public void resetBGColors(ViewGroup v)
    {
        if (v instanceof AlgeTilesRelativeLayout)
        {
            AlgeTilesRelativeLayout temp = (AlgeTilesRelativeLayout)v;
            temp.resetColor();
        }
 			else
        {
            v.setBackgroundResource(R.drawable.shape);
        }
    }

    public void incorrectPrompt(List<EditText> gvList, int dummy)
    {
        if (!muteToggle.isChecked())
            incorrect.start();
        for (int i = 0; i < gvList.size(); ++i)
            gvList.get(i).setBackgroundResource(R.drawable.notok);
//        await Task.Delay(Constants.DELAY);
        for (int i = 0; i < gvList.size(); ++i)
        {
            gvList.get(i).setBackgroundResource(R.drawable.shape);
        }
    }

    public void incorrectPrompt(ArrayList<ViewGroup> gvList)
    {
        if (!muteToggle.isChecked())
            incorrect.start();
        for (int i = 0; i < gvList.size(); ++i)
            gvList.get(i).setBackgroundResource(R.drawable.notok);
//        await Task.Delay(Constants.DELAY);
        for (int i = 0; i < gvList.size(); ++i)
        {
            resetBGColors(gvList);
        }
    }

    public void checkAnswers()
    {
        if (!isFirstAnswerCorrect)
        {
            GridValue[] gvArr = { midUpGV, midLowGV, midLeftGV, midRightGV };
            if (AlgorithmUtilities.isFirstAnswerCorrect(vars, gvArr, numberOfVariables))
            {
                isFirstAnswerCorrect = true;

                //Change color of draggable areas to signify "Done/Correct"
                upperLeftGrid.setOnTouchListener(listeners.Layout_Touch);
                upperRightGrid.setOnTouchListener(listeners.Layout_Touch);
                lowerLeftGrid.setOnTouchListener(listeners.Layout_Touch);
                lowerRightGrid.setOnTouchListener(listeners.Layout_Touch);

                //Shade red the other grids
                for (int i = 0; i < outerGridLayoutList.size(); ++i)
                {
                    resetBGColors(outerGridLayoutList);
                }

                for (int i = 0; i < innerGridLayoutList.size(); ++i)
                {
                    innerGridLayoutList.get(i).setBackgroundResource(R.drawable.ok);
                    innerGridLayoutList.get(i).setOnTouchListener(listeners.nullTouch);
                    for (int j = 0; j < innerGridLayoutList.get(i).getChildCount(); ++j)
                    {
                        AlgeTilesTextView iv = (AlgeTilesTextView) innerGridLayoutList.get(i).getChildAt(j);
                        iv.setOnLongClickListener(listeners.nullLongClick);
                    }
                }

                expandedVars = AlgorithmUtilities.expandingVars(vars);
                for (int i : expandedVars)
                Log.d(TAG, "Expanded : activity: " + i);

                if (Constants.ONE_VAR == numberOfVariables)
                {
                    x2ET.setEnabled(true);
                    xET.setEnabled(true);
                    oneET.setEnabled(true);
                }
                else
                {
                    x2ET.setEnabled(true);
                    y2ET.setEnabled(true);
                    xyET.setEnabled(true);
                    xET.setEnabled(true);
                    yET.setEnabled(true);
                    oneET.setEnabled(true);
                }

                if (!muteToggle.isChecked())
                    correct.start();

                TileUtilities.generateInnerLayoutTileArrays(heightInPx, widthInPx, innerGridLayoutList, rectTileListList);
                upperRightGrid.drawRects(upperRightRectTileList);
                upperLeftGrid.drawRects(upperLeftRectTileList);
                lowerRightGrid.drawRects(lowerRightRectTileList);
                lowerLeftGrid.drawRects(lowerLeftRectTileList);

//                new AlertDialog.Builder(this)
//                        .SetPositiveButton(Constants.PROCEED_TO_MULTIP, (sender, args) =>
//                {
//                    // User pressed yes
//                })
//						.SetMessage(Constants.CORRECT_PLACEMENT)
//                    .SetTitle(Constants.CORRECT)
//                    .Show();
            }
            else
            {
                for (int i = 0; i < outerGridLayoutList.size(); ++i)
                {
                    outerGridLayoutList.get(i).setOnTouchListener(listeners.nullTouch);
                }

                incorrectPrompt(innerGridLayoutList);

                Toast.makeText(getApplicationContext(), Constants.WRONG + Constants.MULTIPLICATION, Toast.LENGTH_SHORT).show();
            }
        }
        else if (!isSecondAnswerCorrect)
        {
            Log.d(TAG, "isSecondAnswerCorrect branch");
            GridValue[] gvArr = { upperLeftGV, upperRightGV, lowerLeftGV, lowerRightGV };

            for (int i = 0; i < gvArr.length; ++i)
                Log.d(TAG, gvArr[i].toString());
            if (AlgorithmUtilities.isSecondAnswerCorrect(expandedVars, gvArr, numberOfVariables))
            {
                //Cancelling out
                int posX = 0;
                ViewGroup posXVG = null;
                if (upperRightGV.xTileCount != 0)
                {
                    posX = upperRightGV.xTileCount;
                    posXVG = upperRightGrid;
                }
                else
                {
                    if (lowerLeftGV.xTileCount != 0)
                    {
                        posX = lowerLeftGV.xTileCount;
                        posXVG = lowerLeftGrid;
                    }
                }

                int posY = 0;
                ViewGroup posYVG = null;
                if (upperRightGV.yTileCount != 0)
                {
                    posY = upperRightGV.yTileCount;
                    posYVG = upperRightGrid;
                }
                else
                {
                    if (lowerLeftGV.yTileCount != 0)
                    {
                        posY = lowerLeftGV.yTileCount;
                        posYVG = lowerLeftGrid;
                    }
                }

                int posXY = 0;
                ViewGroup posXYVG = null;
                if (upperRightGV.xyTileCount != 0)
                {
                    posXY = upperRightGV.xyTileCount;
                    posXYVG = upperRightGrid;
                }
                else
                {
                    if (lowerLeftGV.xyTileCount != 0)
                    {
                        posXY = lowerLeftGV.xyTileCount;
                        posXYVG = lowerLeftGrid;
                    }
                }

                int negX = 0;
                ViewGroup negXVG = null;
                if (upperLeftGV.xTileCount != 0)
                {
                    negX = upperLeftGV.xTileCount;
                    negXVG = upperLeftGrid;
                }
                else
                {
                    if (lowerRightGV.xTileCount != 0)
                    {
                        negX = lowerRightGV.xTileCount;
                        negXVG = lowerRightGrid;
                    }
                }

                int negY = 0;
                ViewGroup negYVG = null;
                if (upperLeftGV.yTileCount != 0)
                {
                    negY = upperLeftGV.yTileCount;
                    negYVG = upperLeftGrid;
                }
                else
                {
                    if (lowerRightGV.yTileCount != 0)
                    {
                        negY = lowerRightGV.yTileCount;
                        negYVG = lowerRightGrid;
                    }
                }

                int negXY = 0;
                ViewGroup negXYVG = null;
                if (upperLeftGV.xyTileCount != 0)
                {
                    negXY = upperLeftGV.xyTileCount;
                    negXYVG = upperLeftGrid;
                }
                else
                {
                    if (lowerRightGV.xyTileCount != 0)
                    {
                        negXY = lowerRightGV.xyTileCount;
                        negXYVG = lowerRightGrid;
                    }
                }

                int xToRemove = posX > negX ? negX : posX;
                List<AlgeTilesTextView> tobeRemovedX = new ArrayList<>();
                List<AlgeTilesTextView> negTobeRemovedX = new ArrayList<>();
                if (posX != 0 && negX != 0)
                {
                    Log.d(TAG, "Cancelling out: " + posX + ", " + negX);
                    Log.d(TAG, "To remove: " + xToRemove);
                    for (int j = 0; j < posXVG.getChildCount(); ++j)
                    {
                        AlgeTilesTextView alIV = (AlgeTilesTextView) posXVG.getChildAt(j);
                        if (alIV.getTileType().equals(Constants.X_TILE) ||
                                alIV.getTileType().equals(Constants.X_TILE_ROT))
                        {
                            tobeRemovedX.add(alIV);
                        }
                    }

                    for (int j = 0; j < negXVG.getChildCount(); ++j)
                    {
                        AlgeTilesTextView negalIV = (AlgeTilesTextView) negXVG.getChildAt(j);
                        if (negalIV.getTileType().equals(Constants.X_TILE) ||
                                negalIV.getTileType().equals(Constants.X_TILE_ROT))
                        {
                            negTobeRemovedX.add(negalIV);
                        }
                    }
                }

                int yToRemove = posY > negY ? negY : posY;
                List<AlgeTilesTextView> tobeRemovedY = new ArrayList<>();
                List<AlgeTilesTextView> negTobeRemovedY = new ArrayList<>();
                if (posY != 0 && negY != 0)
                {
                    Log.d(TAG, "Cancelling out: " + posY + ", " + negY);
                    Log.d(TAG, "To remove: " + yToRemove);
                    for (int j = 0; j < posYVG.getChildCount(); ++j)
                    {
                        AlgeTilesTextView alIV = (AlgeTilesTextView) posYVG.getChildAt(j);
                        if (alIV.getTileType().equals(Constants.Y_TILE) ||
                                alIV.getTileType().equals(Constants.Y_TILE_ROT))
                        {
                            tobeRemovedY.add(alIV);
                        }
                    }

                    for (int j = 0; j < negYVG.getChildCount(); ++j)
                    {
                        AlgeTilesTextView negalIV = (AlgeTilesTextView) negYVG.getChildAt(j);
                        if (negalIV.getTileType().equals(Constants.Y_TILE) ||
                                negalIV.getTileType().equals(Constants.Y_TILE_ROT))
                        {
                            negTobeRemovedY.add(negalIV);
                        }
                    }
                }

                int xyToRemove = posXY > negXY ? negXY : posXY;
                List<AlgeTilesTextView> tobeRemovedXY = new ArrayList<>();
                List<AlgeTilesTextView> negTobeRemovedXY = new ArrayList<>();
                if (posXY != 0 && negXY != 0)
                {
                    Log.d(TAG, "Cancelling out: " + posXY + ", " + negXY);
                    Log.d(TAG, "To remove: " + xyToRemove);

                    for (int j = 0; j < posXYVG.getChildCount(); ++j)
                    {
                        AlgeTilesTextView alIV = (AlgeTilesTextView) posXYVG.getChildAt(j);
                        if (alIV.getTileType().equals(Constants.XY_TILE) ||
                                alIV.getTileType().equals(Constants.XY_TILE_ROT))
                        {
                            tobeRemovedXY.add(alIV);
                        }
                    }


                    for (int j = 0; j < negXYVG.getChildCount(); ++j)
                    {
                        AlgeTilesTextView negalIV = (AlgeTilesTextView) negXYVG.getChildAt(j);
                        if (negalIV.getTileType().equals(Constants.XY_TILE) ||
                                negalIV.getTileType().equals(Constants.XY_TILE_ROT))
                        {
                            negTobeRemovedXY.add(negalIV);
                        }
                    }
                }

                //added changing color of tiles to be cancelled and 2 second delay before cancelling out
                for (int j = 0; j < xToRemove; ++j)
                {
                    tobeRemovedX.get(j).setBackgroundResource(R.drawable.cancelling);
                    negTobeRemovedX.get(j).setBackgroundResource(R.drawable.cancelling);
                }

                for (int j = 0; j < yToRemove; ++j)
                {
                    tobeRemovedY.get(j).setBackgroundResource(R.drawable.cancelling);
                    negTobeRemovedY.get(j).setBackgroundResource(R.drawable.cancelling);
                }


                for (int j = 0; j < xyToRemove; ++j)
                {
                    tobeRemovedXY.get(j).setBackgroundResource(R.drawable.cancelling);
                    negTobeRemovedXY.get(j).setBackgroundResource(R.drawable.cancelling);
                }

//                if (xToRemove + yToRemove + xyToRemove > 0)
//                    await Task.Delay(Constants.CANCELOUT_DELAY);

                for (int j = 0; j < xToRemove; ++j)
                {
                    posXVG.removeView(tobeRemovedX.get(j));
                    negXVG.removeView(negTobeRemovedX.get(j));
                }

                for (int j = 0; j < yToRemove; ++j)
                {
                    posYVG.removeView(tobeRemovedY.get(j));
                    negYVG.removeView(negTobeRemovedY.get(j));
                }


                for (int j = 0; j < xyToRemove; ++j)
                {
                    posXYVG.removeView(tobeRemovedXY.get(j));
                    negXYVG.removeView(negTobeRemovedXY.get(j));
                }
                //End Cancelling out

                if (!muteToggle.isChecked())
                    correct.start();

                //Loop through inner and prevent deletions by removing: listeners.clonedImageView_Touch
                for (int i = 0; i < outerGridLayoutList.size(); ++i)
                {
                    outerGridLayoutList.get(i).setBackgroundResource(R.drawable.ok);
                    outerGridLayoutList.get(i).setOnTouchListener(listeners.nullTouch);
                    for (int j = 0; j < outerGridLayoutList.get(i).getChildCount(); ++j)
                    {
                        AlgeTilesTextView iv = (AlgeTilesTextView) outerGridLayoutList.get(i).getChildAt(j);
                        iv.setOnLongClickListener(listeners.nullLongClick);
                    }
                }

                //Removing outer grid after 2nd correct (10-28-2016)
                upperRightGrid.clearRects(heightInPx, widthInPx);
                upperLeftGrid.clearRects(heightInPx, widthInPx);
                lowerRightGrid.clearRects(heightInPx, widthInPx);
                lowerLeftGrid.clearRects(heightInPx, widthInPx);

                upperRightRectTileList.clear();
                upperLeftRectTileList.clear();
                lowerRightRectTileList.clear();
                lowerLeftRectTileList.clear();

                isSecondAnswerCorrect = true;
//                new AlertDialog.Builder(this)
//                        .SetPositiveButton(Constants.PROCEED + Constants.COEFFICIENTS, (sender, args) =>
//                {
//                    // User pressed yes
//                })
//						.SetMessage(Constants.CORRECT_MULTIP)
//                    .SetTitle(Constants.CORRECT)
//                    .Show();

                for (int i = 0; i < innerGridLayoutList.size(); ++i)
                {
                    for (int j = 0; j < innerGridLayoutList.get(i).getChildCount(); ++j)
                    {
                        View v = innerGridLayoutList.get(i).getChildAt(j);
                        innerGridLayoutList.get(i).removeAllViews();
                    }
                }
            }
            else
            {

                Toast.makeText(getApplicationContext(), Constants.WRONG + " " + Constants.FACTOR, Toast.LENGTH_SHORT).show();
                incorrectPrompt(outerGridLayoutList);
            }
        }
        else if (!isThirdAnswerCorrect)
        {
            //TODO: Accomodate for two variables
            if (Constants.ONE_VAR == numberOfVariables)
            {
                int[] answer = new int[3];
                int temp = 0;
                answer[0] = TileUtilities.tryParseInt(x2ET.getText().toString());
                answer[1] = TileUtilities.tryParseInt(xET.getText().toString());
                answer[2] = TileUtilities.tryParseInt(oneET.getText().toString());

                if ((Math.abs(answer[0]) +
                        Math.abs(answer[1]) +
                        Math.abs(answer[2])) == 0)
                {
                    isThirdAnswerCorrect = false;
                }
                else
                {
                    if (expandedVars.get(0) == answer[0] &&
                            expandedVars.get(1) == answer[1] &&
                            expandedVars.get(2) == answer[2])
                        isThirdAnswerCorrect = true;
                }
            }
            else
            {
                int[] answer = new int[6];
                int temp = 0;
                answer[0] = TileUtilities.tryParseInt(x2ET.getText().toString());
                answer[1] = TileUtilities.tryParseInt(y2ET.getText().toString());
                answer[2] = TileUtilities.tryParseInt(xyET.getText().toString());
                answer[3] = TileUtilities.tryParseInt(xET.getText().toString());
                answer[4] = TileUtilities.tryParseInt(yET.getText().toString());
                answer[5] = TileUtilities.tryParseInt(oneET.getText().toString());

                for (int i : answer)
                Log.d(TAG, "answer:" + i);

                for (int i : expandedVars)
                Log.d(TAG, "expandedVars:" + i);

                if ((Math.abs(answer[0]) +
                        Math.abs(answer[1]) +
                        Math.abs(answer[2]) +
                        Math.abs(answer[3]) +
                        Math.abs(answer[4]) +
                        Math.abs(answer[5])) == 0)
                {
                    isThirdAnswerCorrect = false;
                }
                else
                {
                    if (expandedVars.get(0) == answer[0] &&
                            expandedVars.get(1) == answer[1] &&
                            expandedVars.get(2) == answer[2] &&
                            expandedVars.get(3) == answer[3] &&
                            expandedVars.get(4) == answer[4] &&
                            expandedVars.get(5) == answer[5])
                        isThirdAnswerCorrect = true;
                }
            }


            if (isThirdAnswerCorrect)
            {
                for (int i = 0; i < editTextList.size(); ++i)
                {
                    editTextList.get(i).setBackgroundResource(R.drawable.ok);
                    editTextList.get(i).setEnabled(false);
                }
                if (!muteToggle.isChecked())
                    correct.start();


//                new AlertDialog.Builder(this)
//                        //.SetPositiveButton("New Question", (sender, args) =>
//                        //{
//                        //	setupNewQuestion(numberOfVariables);
//                        //	refreshScreen(Constants.FACTOR, gridValueList, innerGridLayoutList, outerGridLayoutList);
//                        //})
//                        .SetNegativeButton("OK", (sender, args) =>
//                {
//
//                })
//						.SetMessage(Constants.CORRECT + Constants.COEFFICIENTS)
//                    .SetTitle(Constants.CORRECT)
//                    .Show();
            }
            else
            {
                Toast.makeText(getApplicationContext(), Constants.WRONG + " " + Constants.COEFFICIENTS, Toast.LENGTH_SHORT).show();
                incorrectPrompt(editTextList, 0);
            }
        }
    }

    protected void setupNewQuestion()
    {
        isFirstAnswerCorrect = false;
        Log.d(TAG, "Number of vars: " + numberOfVariables);
        vars = AlgorithmUtilities.RNG(Constants.MULTIPLY, numberOfVariables);

        //(ax + b)(cx + d)
        //if (Constants.ONE_VAR == numberOfVariables)
        //{
        for (int i = 0; i < gridValueList.size(); ++i)
        {
            gridValueList.get(i).init();
        }

        setupQuestionString(vars);
        //}

        for (int i : vars)
        {
            Log.d(TAG, i + "");
        }
    }

    protected void refreshScreen(String ActivityType, ArrayList<GridValue> gvList, ArrayList<ViewGroup> inGLList, ArrayList<ViewGroup> outGLList)
    {
        for (List<RectTile> rList : rectTileListList)
        {
            rList.clear();
        }

        for (int i = 0; i < editTextList.size(); ++i)
        {
            editTextList.get(i).setBackgroundResource(R.drawable.shape);
            editTextList.get(i).setEnabled(false);
            editTextList.get(i).setText("");
        }

        isFirstAnswerCorrect = false;
        isSecondAnswerCorrect = false;
        isThirdAnswerCorrect = false;

        for (int i = 0; i < inGLList.size(); ++i)
        {
            resetBGColors(inGLList);
            inGLList.get(i).setOnTouchListener(listeners.nullTouch);
        }

        for (int i = 0; i < outGLList.size(); ++i)
        {
            resetBGColors(outGLList);
            outGLList.get(i).setOnTouchListener(listeners.nullTouch);
            AlgeTilesRelativeLayout a = (AlgeTilesRelativeLayout)outGLList.get(i);
            a.clearRects(heightInPx, widthInPx);
        }

        for (int i = 0; i < gvList.size(); ++i)
        {
            gvList.get(i).init();
        }

        for (int i = 0; i < inGLList.size(); ++i)
        {
            for (int j = 0; j < inGLList.get(i).getChildCount(); ++j)
            {
                View v = inGLList.get(i).getChildAt(j);
                inGLList.get(i).removeAllViews();
            }
        }

        for (int i = 0; i < outGLList.size(); ++i)
        {
            for (int j = 0; j < outGLList.get(i).getChildCount(); ++j)
            {
                View v = outGLList.get(i).getChildAt(j);
                outGLList.get(i).removeAllViews();
            }
        }

        if (Constants.FACTOR == ActivityType)
        {
            for (int i = 0; i < inGLList.size(); ++i)
            {
                inGLList.get(i).setBackgroundResource(R.drawable.unavailable);
                inGLList.get(i).setOnTouchListener(listeners.nullTouch);
            }

            for (int i = 0; i < outGLList.size(); ++i)
            {
                resetBGColors(outGLList);
                outGLList.get(i).setOnTouchListener(listeners.Layout_Touch);
            }
        }
        else
        {
            for (int i = 0; i < inGLList.size(); ++i)
            {
                resetBGColors(inGLList);
                inGLList.get(i).setOnTouchListener(listeners.Layout_Touch);
            }

            for (int i = 0; i < outGLList.size(); ++i)
            {
                outGLList.get(i).setBackgroundResource(R.drawable.unavailable);
                outGLList.get(i).setOnTouchListener(listeners.nullTouch);
            }
        }
    }
}
