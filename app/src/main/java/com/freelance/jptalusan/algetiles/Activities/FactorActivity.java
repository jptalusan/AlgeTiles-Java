package com.freelance.jptalusan.algetiles.Activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.Space;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
import android.text.style.SuperscriptSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.freelance.jptalusan.algetiles.ExtendedViews.AlgeTilesRelativeLayout;
import com.freelance.jptalusan.algetiles.ExtendedViews.AlgeTilesTextView;
import com.freelance.jptalusan.algetiles.R;
import com.freelance.jptalusan.algetiles.Utilities.AlgorithmUtilities;
import com.freelance.jptalusan.algetiles.Utilities.Constants;
import com.freelance.jptalusan.algetiles.Utilities.CustomEquationDialog;
import com.freelance.jptalusan.algetiles.Utilities.CustomEquationDialogFactor;
import com.freelance.jptalusan.algetiles.Utilities.GridValue;
import com.freelance.jptalusan.algetiles.Utilities.RectTile;
import com.freelance.jptalusan.algetiles.Utilities.TileUtilities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import me.grantland.widget.AutofitTextView;

public class FactorActivity extends AppCompatActivity implements CustomEquationDialogFactor.CustomEquationDialogFactorListener {
    private static String TAG = "AlgeTiles:Factor";
    private AutofitTextView result;
    private Boolean hasButtonBeenDroppedInCorrectzone = false;
    private String currentButtonType = "";

    private Button tutorialButton;
    private ToggleButton removeToggle;
    private ToggleButton dragToggle;
    private ToggleButton rotateToggle;
    private ToggleButton muteToggle;

    private AlgeTilesTextView tile_1;
    private AlgeTilesTextView x_tile;
    private AlgeTilesTextView x2_tile;

    private Button newQuestionButton;
    private Button refreshButton;
    private Button checkButton;

    private int numberOfVariables = 0;

    private Boolean isFirstAnswerCorrect = false;
    private Boolean isSecondAnswerCorrect = false;
    private Boolean isThirdAnswerCorrect = false;

    private AlgeTilesRelativeLayout upperLeftGrid;
    private AlgeTilesRelativeLayout upperRightGrid;
    private AlgeTilesRelativeLayout lowerLeftGrid;
    private AlgeTilesRelativeLayout lowerRightGrid;

    private GridLayout upperMiddleGrid;
    private GridLayout middleLeftGrid;
    private GridLayout middleRightGrid;
    private GridLayout lowerMiddleGrid;

    //Four outer grids
    private GridValue upperLeftGV;
    private GridValue upperRightGV;
    private GridValue lowerLeftGV;
    private GridValue lowerRightGV;

    //Four center grids
    private GridValue midUpGV;
    private GridValue midLowGV;
    private GridValue midLeftGV;
    private GridValue midRightGV;

    private ArrayList<Integer> vars = new ArrayList<>();
    private ArrayList<Integer> expandedVars = new ArrayList<>();
    private ArrayList<ViewGroup> innerGridLayoutList = new ArrayList<>();
    private ArrayList<ViewGroup> outerGridLayoutList = new ArrayList<>();
    private ArrayList<GridValue> gridValueList = new ArrayList<>();

    private MediaPlayer correct;
    private MediaPlayer incorrect;

    private EditText x_value_1;
    private EditText one_value_1;
    private EditText x_value_2;
    private EditText one_value_2;

    private ArrayList<EditText> editTextList = new ArrayList<>();

    private boolean isFirstTime = false;

    private int heightInPx = 0;
    private int widthInPx = 0;

    public ArrayList<RectTile> upperRightRectTileList = new ArrayList<>();
    public ArrayList<RectTile> upperLeftRectTileList = new ArrayList<>();
    public ArrayList<RectTile> lowerRightRectTileList = new ArrayList<>();
    public ArrayList<RectTile> lowerLeftRectTileList = new ArrayList<>();

    ArrayList<String> midUp = new ArrayList<>();
    ArrayList<String> midLeft = new ArrayList<>();
    ArrayList<String> midRight = new ArrayList<>();
    ArrayList<String> midDown = new ArrayList<>();

    private ArrayList<ArrayList<RectTile>> rectTileListList = new ArrayList<>();

    public SharedPreferences prefs;

    private Boolean oneTile_Clicked;
    private Boolean xTile_Clicked;
    private Boolean x2Tile_Clicked;

    private Button customQuestion;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_factor);
        Log.d(TAG, "Enter FACTOR");
        // Create your application here
        result = findViewById(R.id.result);

        tile_1 = findViewById(R.id.tile_1);
        x_tile = findViewById(R.id.x_tile);
        x2_tile = findViewById(R.id.x2_tile);

        tile_1.setOnClickListener(tile_click);
        x_tile.setOnClickListener(tile_click);
        x2_tile.setOnClickListener(tile_click);

        upperLeftGrid = findViewById(R.id.upperLeft);
        upperRightGrid = findViewById(R.id.upperRight);
        lowerLeftGrid = findViewById(R.id.lowerLeft);
        lowerRightGrid = findViewById(R.id.lowerRight);

        upperLeftGrid.backGroundResource = R.drawable.upperleftgridshape;
        upperRightGrid.backGroundResource = R.drawable.upperrightgridshape;
        lowerLeftGrid.backGroundResource = R.drawable.lowerleftgridshape;
        lowerRightGrid.backGroundResource = R.drawable.lowerrightgridshape;

        upperMiddleGrid = findViewById(R.id.upperMiddle);
        middleLeftGrid = findViewById(R.id.middleLeft);
        middleRightGrid = findViewById(R.id.middleRight);
        lowerMiddleGrid = findViewById(R.id.lowerMiddle);

        customQuestion = findViewById(R.id.custom);
        customQuestion.setOnClickListener(toggle_click);

        upperLeftGrid.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (!isFirstTime) {
                    heightInPx = upperLeftGrid.getHeight();
                    widthInPx = upperLeftGrid.getWidth();
                    upperLeftGrid.setMinimumHeight(0);
                    upperLeftGrid.setMinimumWidth(0);
                    setupNewQuestion();
                    refreshScreen(Constants.FACTOR, gridValueList, innerGridLayoutList, outerGridLayoutList);
                    isFirstTime = true;

                    LinearLayout.LayoutParams par_1 = (LinearLayout.LayoutParams)tile_1.getLayoutParams();
                    TileUtilities.TileFactor tF = TileUtilities.getTileFactors(tile_1.getTileType());
                    par_1.height = heightInPx / 7;
                    par_1.width = heightInPx / 7;
                    tile_1.setBackgroundResource(tF.id);
                    tile_1.setText(tF.text);
                    tile_1.setLayoutParams(par_1);
                    Log.d(TAG, tile_1.getTileType());

                    LinearLayout.LayoutParams par_x = (LinearLayout.LayoutParams)x_tile.getLayoutParams();
                    tF = TileUtilities.getTileFactors(x_tile.getTileType());
                    par_x.height = (int)(heightInPx / tF.heightFactor);
                    par_x.width = heightInPx / 7;
                    x_tile.setBackgroundResource(tF.id);
                    x_tile.setText(tF.text);
                    x_tile.setLayoutParams(par_x);
                    Log.d(TAG, x_tile.getTileType());

                    LinearLayout.LayoutParams par_x2 = (LinearLayout.LayoutParams)x2_tile.getLayoutParams();
                    tF = TileUtilities.getTileFactors(x2_tile.getTileType());
                    par_x2.height = (int)(heightInPx / tF.heightFactor);
                    par_x2.width = (int)(heightInPx / tF.widthFactor);
                    x2_tile.setBackgroundResource(tF.id);
                    x2_tile.setText(tF.text);
                    x2_tile.setLayoutParams(par_x2);
                    Log.d(TAG, x2_tile.getTileType());

                    x_value_1.setHeight(par_x2.height / 2);
                    result.setHeight(par_x2.height / 2);
                }
            }
        });

        outerGridLayoutList.add(upperLeftGrid);
        outerGridLayoutList.add(upperRightGrid);
        outerGridLayoutList.add(lowerLeftGrid);
        outerGridLayoutList.add(lowerRightGrid);

        innerGridLayoutList.add(upperMiddleGrid);
        innerGridLayoutList.add(middleLeftGrid);
        innerGridLayoutList.add(middleRightGrid);
        innerGridLayoutList.add(lowerMiddleGrid);

        //For multiply this is the initial grid available
        upperLeftGrid.setOnTouchListener(layoutTouch);
        upperRightGrid.setOnTouchListener(layoutTouch);
        lowerLeftGrid.setOnTouchListener(layoutTouch);
        lowerRightGrid.setOnTouchListener(layoutTouch);

        //Shade red the other grids
        for (int i = 0; i < innerGridLayoutList.size(); ++i)
        {
            innerGridLayoutList.get(i).setBackgroundResource(R.drawable.unavailable);
        }

        removeToggle = findViewById(R.id.remove);
        dragToggle = findViewById(R.id.drag);
        rotateToggle = findViewById(R.id.rotate);
        muteToggle = findViewById(R.id.mute);

        removeToggle.setOnClickListener(toggle_click);
        dragToggle.setOnClickListener(toggle_click);
        rotateToggle.setOnClickListener(toggle_click);
        muteToggle.setOnClickListener(toggle_click);

        tutorialButton = findViewById(R.id.tutorial);
        tutorialButton.setOnClickListener(toggle_click);

        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        muteToggle.setChecked(prefs.getBoolean(Constants.MUTE, false));

        numberOfVariables = getIntent().getIntExtra(Constants.VARIABLE_COUNT, 0);

        newQuestionButton = findViewById(R.id.new_question_button);
        refreshButton = findViewById(R.id.refresh_button);
        checkButton = findViewById(R.id.check_button);

        newQuestionButton.setOnClickListener(button_click);
        refreshButton.setOnClickListener(button_click);
        checkButton.setOnClickListener(button_click);

        upperLeftGV = new GridValue();
        upperRightGV = new GridValue();
        lowerLeftGV = new GridValue();
        lowerRightGV = new GridValue();

        midUpGV = new GridValue();
        midLowGV = new GridValue();
        midLeftGV = new GridValue();
        midRightGV = new GridValue();

        gridValueList.add(upperLeftGV);
        gridValueList.add(upperRightGV);
        gridValueList.add(lowerLeftGV);
        gridValueList.add(lowerRightGV);

        gridValueList.add(midUpGV);
        gridValueList.add(midLowGV);
        gridValueList.add(midLeftGV);
        gridValueList.add(midRightGV);

        setupNewQuestion();

        correct = MediaPlayer.create(this, R.raw.correct);
        incorrect = MediaPlayer.create(this, R.raw.wrong);

        x_value_1 = findViewById(R.id.x_value_1);
        one_value_1 = findViewById(R.id.one_value_1);

        x_value_2 = findViewById(R.id.x_value_2);
        one_value_2 = findViewById(R.id.one_value_2);

        editTextList.add(x_value_1);
        editTextList.add(one_value_1);

        editTextList.add(x_value_2);
        editTextList.add(one_value_2);

        refreshScreen(Constants.FACTOR, gridValueList, innerGridLayoutList, outerGridLayoutList);

        rectTileListList.add(upperRightRectTileList);
        rectTileListList.add(upperLeftRectTileList);
        rectTileListList.add(lowerLeftRectTileList);
        rectTileListList.add(lowerRightRectTileList);
    }

    //http://stackoverflow.com/questions/4747311/how-can-i-keep-one-button-as-pressed-after-click-on-it
    View.OnClickListener tile_click = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            AlgeTilesTextView imageViewTouch = (AlgeTilesTextView) view;
            if (imageViewTouch.isSelected()) {
                imageViewTouch.setSelected(false);
                oneTile_Clicked = false;
                xTile_Clicked = false;
                x2Tile_Clicked = false;
            } else {
                imageViewTouch.setSelected(true);
            }

            switch (imageViewTouch.getId())
            {
                case R.id.tile_1:
                    oneTile_Clicked = imageViewTouch.isSelected();
                    x_tile.setSelected(false);
                    xTile_Clicked = false;

                    x2_tile.setSelected(false);
                    x2Tile_Clicked = false;
                    break;
                case R.id.x_tile:
                    xTile_Clicked = imageViewTouch.isSelected();
                    tile_1.setSelected(false);
                    oneTile_Clicked = false;

                    x2_tile.setSelected(false);
                    x2Tile_Clicked = false;
                    break;
                case R.id.x2_tile:
                    x2Tile_Clicked = imageViewTouch.isSelected();
                    tile_1.setSelected(false);
                    oneTile_Clicked = false;

                    x_tile.setSelected(false);
                    xTile_Clicked = false;
                    break;
            }
            dragToggle.setChecked(false);
            removeToggle.setChecked(false);
        }
    };
//
    View.OnClickListener toggle_click = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            View clicked_toggle = view;
            int buttonText = clicked_toggle.getId();
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(FactorActivity.this);
            SharedPreferences.Editor editor = prefs.edit();

            switch (buttonText) {
                case R.id.remove:
                    editor.putBoolean(Constants.REMOVE, removeToggle.isChecked());
                    // editor.Commit();    // applies changes synchronously on older APIs
                    editor.apply();        // applies changes asynchronously on newer APIs

                    tile_1.setSelected(false);
                    oneTile_Clicked = false;

                    x_tile.setSelected(false);
                    xTile_Clicked = false;

                    x2_tile.setSelected(false);
                    x2Tile_Clicked = false;
                    break;
                case R.id.drag:
                    removeToggle.setChecked(removeToggle.isChecked() ? false : false);
                    if (rotateToggle.isChecked())
                    {
//                        findViewById()<LinearLayout>(R.id.notRotatedButtonLayout).Visibility = ViewStates.Visible;
                    }
                    rotateToggle.setChecked(rotateToggle.isChecked() ? false : false);
                    break;
                case R.id.rotate:
                    //Also rotate original tiles
                    if (rotateToggle.isChecked())
                    {
                        //findViewById()<LinearLayout>(R.id.notRotatedButtonLayout).Visibility = ViewStates.Gone;
                    }
                    else
                    {
                        //findViewById()<LinearLayout>(R.id.notRotatedButtonLayout).Visibility = ViewStates.Visible;
                    }
                    removeToggle.setChecked(removeToggle.isChecked() ? false : false);
                    dragToggle.setChecked(dragToggle.isChecked() ? false : false);
                    break;
                case R.id.mute:
                    editor.putBoolean(Constants.MUTE, muteToggle.isChecked());
                    // editor.Commit();    // applies changes synchronously on older APIs
                    editor.apply();        // applies changes asynchronously on newer APIs
                    break;
                case R.id.tutorial:
                    Intent intent = new Intent(FactorActivity.this, TutorialActivity.class);
                    startActivity(intent);
                    break;
                case R.id.custom:
                    FragmentManager fm = getSupportFragmentManager();
                    CustomEquationDialogFactor editNameDialogFragment = CustomEquationDialogFactor.newInstance("Some Title");
                    editNameDialogFragment.show(fm, "fragment_edit_name");
                break;
            }
        }
    };

    @Override
    public void onButtonClicked(int index) {
        if (index == -1) {
            Log.d(TAG, "clicked: wrong");
        } else {
            Log.d(TAG, "clicked: " + Constants.EQUATIONS.get(index));
            vars = AlgorithmUtilities.parseEquation(Constants.EQUATIONS.get(index));
            expandedVars = AlgorithmUtilities.expandingVars(vars);
            setupQuestionString(expandedVars);
            refreshScreen(Constants.FACTOR, gridValueList, innerGridLayoutList, outerGridLayoutList);
            //TODO: handle the new values here.
        }
    }

//
    View.OnTouchListener layoutTouch = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent event) {
            ViewGroup v = (ViewGroup)view;
            int id = v.getId();
            float x = event.getX(0);
            float y = event.getY(0);
            boolean isDroppedAtCenter = false;

//        Console.WriteLine("1: " + oneTile_Clicked + ", x: " + xTile_Clicked + " , x2: " + x2Tile_Clicked);
            if (oneTile_Clicked)
            {
                currentButtonType = Constants.ONE_TILE;
            }
            else if (xTile_Clicked)
            {
                currentButtonType = Constants.X_TILE;
            }
            else if (x2Tile_Clicked)
            {
                currentButtonType = Constants.X2_TILE;
            }
            else
            {
                //Do nothing
                currentButtonType = "";
            }

            switch (event.getAction())
            {
                case MotionEvent.ACTION_DOWN:
//                    Console.WriteLine("Inside Coords: " + x + "," + y);
                    Log.d(TAG, "Down");

                    AlgeTilesTextView algeTilesIV = new AlgeTilesTextView(getApplicationContext());
                    Boolean wasImageDropped = false;

                    //Check if x_tile is rotated before fitting or rotate before dropping automatically
                    if ((id == R.id.upperMiddle ||
                            id == R.id.middleLeft ||
                            id == R.id.middleRight ||
                            id == R.id.lowerMiddle) &&
                            currentButtonType.equals(Constants.X2_TILE))
                    {
                        //Do nothing
                    }
                    else if (isFirstAnswerCorrect &&
                            (id == R.id.upperLeft ||
                                    id == R.id.upperRight ||
                                    id == R.id.lowerLeft ||
                                    id == R.id.lowerRight))
                    {
                        //Do nothing
                    }
                    //Handle drops for second part of problem (expanded form)
                    else if (!isFirstAnswerCorrect &&
                            (id == R.id.upperLeft ||
                                    id == R.id.upperRight ||
                                    id == R.id.lowerLeft ||
                                    id == R.id.lowerRight))
                    {
                        wasImageDropped = true;
                    }
                    //Handle auto rotate for x_tile (middle)
                    //First group x
                    else if (isFirstAnswerCorrect &&
                            (id == R.id.middleLeft ||
                                    id == R.id.middleRight))
                    {
                        if (id == R.id.middleLeft)
                        {
                            algeTilesIV.setRotationY(180);
                        }

                        wasImageDropped = true;
                        isDroppedAtCenter = true;
                    }
                    //Second group x
                    else if (isFirstAnswerCorrect &&
                            (id == R.id.upperMiddle ||
                                    id == R.id.lowerMiddle))
                    {
                        if (id == R.id.upperMiddle)
                            algeTilesIV.setRotationX(180);
                        wasImageDropped = true;
                        isDroppedAtCenter = true;
                    }

                    algeTilesIV.setTileType(currentButtonType);

                    if (wasImageDropped)
                    {
                        ViewGroup container = (ViewGroup)v;
                        Log.d(TAG, currentButtonType);
                        double heightFactor = 0;
                        double widthFactor = 0;
                        TileUtilities.TileFactor tF = TileUtilities.getTileFactors(currentButtonType);
                        algeTilesIV.setBackgroundResource(tF.id);

                        if (algeTilesIV.getText().length() > 1)
                        {
                            SpannableStringBuilder cs = new SpannableStringBuilder(algeTilesIV.getText());
                            cs.setSpan(new SuperscriptSpan(), 1, 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                            cs.setSpan(new RelativeSizeSpan(0.75f), 1, 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                            algeTilesIV.setText(cs);
                        }
                        else
                        {
                            algeTilesIV.setText(tF.text);
                        }

                        heightFactor = tF.heightFactor;
                        widthFactor = tF.widthFactor;

                        if (!isDroppedAtCenter)
                        {
                            Rect r = checkIfUserDropsOnRect(id, currentButtonType, x, y, Constants.ADD);
                            if (null != r)
                            {
                                Log.d(TAG, "Dropped successfully.");
                                RelativeLayout.LayoutParams par = new RelativeLayout.LayoutParams(
                                        ViewGroup.LayoutParams.WRAP_CONTENT,
                                        ViewGroup.LayoutParams.WRAP_CONTENT);
                                par.height = r.height();
                                par.width = r.width();
                                par.topMargin = r.top;
                                par.leftMargin = r.left;
                                algeTilesIV.setLayoutParams(par);
                                algeTilesIV.setOnLongClickListener(clonedImageView_Touch);
                                container.addView(algeTilesIV);
                                checkWhichParentAndUpdate(id, currentButtonType, Constants.ADD);
                                hasButtonBeenDroppedInCorrectzone = true;
                            }
                        }
                        else
                        {
                            Log.d(TAG, "Dropped at center.");
                            GridLayout.LayoutParams gParms = new GridLayout.LayoutParams();
                            if (id == R.id.middleLeft || id == R.id.middleRight)
                            {
                                gParms.setGravity(Gravity.CENTER);
                                gParms.height = (int)(heightInPx / widthFactor);
                                gParms.width = (int)(heightInPx / heightFactor);
                            }
                            else
                            {
                                gParms.setGravity(Gravity.CENTER);
                                gParms.height = (int)(heightInPx / heightFactor);
                                gParms.width = (int)(heightInPx / widthFactor);
                            }
                            Log.d(TAG, "H: " + gParms.height + ",W:" + gParms.width);
                            algeTilesIV.setLayoutParams(gParms);
                            algeTilesIV.setOnLongClickListener(clonedImageView_Touch);
                            container.addView(algeTilesIV);
                            checkWhichParentAndUpdate(id, currentButtonType, Constants.ADD);

                            //Auto re-arrange of center tiles
                            ArrayList<AlgeTilesTextView> centerTileList = new ArrayList<>();
                            Log.d(TAG, "Container count: " + container.getChildCount());
                            for (int i = 0; i < container.getChildCount(); ++i)
                            {
                                AlgeTilesTextView a = (AlgeTilesTextView)container.getChildAt(i);
                                centerTileList.add(a);
                                Log.d(TAG, "Center count: " + i + ", " + a.getTileType());
                            }
                            container.removeAllViews();

                            Collections.sort(centerTileList, new Comparator<AlgeTilesTextView>(){
                                public int compare(AlgeTilesTextView s1, AlgeTilesTextView s2) {
                                    return s2.getTileType().compareToIgnoreCase(s1.getTileType());
                                }
                            });

//                            ArrayList<AlgeTilesTextView> sortedList = centerTileList.OrderByDescending(o => o.getTileType()).ToList();
                            for (int i = 0; i < centerTileList.size(); ++i)
                            {
                                Log.d(TAG, "Tile order:" + centerTileList.get(i).getTileType());
                                container.addView(centerTileList.get(i));
                            }
                            //End of auto re-arrange
                        }
                        resetBGColors(v);
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    resetBGColors(v);
                    break;
                default:
                    break;
            }
            return true;
        }
    };

    View.OnClickListener button_click = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            final Button button = (Button) view;
            AlertDialog.Builder builder;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                builder = new AlertDialog.Builder(FactorActivity.this, android.R.style.Theme_Material_Dialog_NoActionBar);
            } else {
                builder = new AlertDialog.Builder(FactorActivity.this);
            }
            builder.setTitle("Button pressed")
                .setMessage("Are you sure?")
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (button.getText().toString()) {
                            case Constants.NEW_Q:
                                setupNewQuestion();
                                refreshScreen(Constants.FACTOR, gridValueList, innerGridLayoutList, outerGridLayoutList);
                                break;
                            case Constants.REFR:
                                refreshScreen(Constants.FACTOR, gridValueList, innerGridLayoutList, outerGridLayoutList);
                                break;
                            case Constants.CHK:
                                checkAnswers();
                                break;
                        }
                    }
                })
                .show();
        }
    };
//
    private void refreshScreen(String ActivityType, ArrayList<GridValue> gvList, ArrayList<ViewGroup> inGLList, ArrayList<ViewGroup> outGLList)
    {
        upperRightGrid.clearRects(heightInPx, widthInPx);
        upperLeftGrid.clearRects(heightInPx, widthInPx);
        lowerRightGrid.clearRects(heightInPx, widthInPx);
        lowerLeftGrid.clearRects(heightInPx, widthInPx);

        upperRightRectTileList.clear();
        upperLeftRectTileList.clear();
        lowerRightRectTileList.clear();
        lowerLeftRectTileList.clear();

        generateInnerLayoutTileArrays();

        upperRightGrid.drawRects(upperRightRectTileList);
        upperLeftGrid.drawRects(upperLeftRectTileList);
        lowerRightGrid.drawRects(lowerRightRectTileList);
        lowerLeftGrid.drawRects(lowerLeftRectTileList);

        upperRightGrid.setBackgroundResource(R.drawable.shape_droptarget);
        upperRightGrid.resetColor();

        upperLeftGrid.setBackgroundResource(R.drawable.shape_droptarget);
        upperLeftGrid.resetColor();

        lowerRightGrid.setBackgroundResource(R.drawable.shape_droptarget);
        lowerRightGrid.resetColor();

        lowerLeftGrid.setBackgroundResource(R.drawable.shape_droptarget);
        lowerLeftGrid.resetColor();


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
            inGLList.get(i).setOnTouchListener(nullTouch);
        }

        for (int i = 0; i < outGLList.size(); ++i)
        {
            resetBGColors(outGLList);
            outGLList.get(i).setOnTouchListener(nullTouch);
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
                inGLList.get(i).setOnTouchListener(nullTouch);
            }

            for (int i = 0; i < outGLList.size(); ++i)
            {
                resetBGColors(outGLList);
                outGLList.get(i).setOnTouchListener(layoutTouch);
            }
        }
        else
        {
            for (int i = 0; i < inGLList.size(); ++i)
            {
                resetBGColors(inGLList);
                inGLList.get(i).setOnTouchListener(layoutTouch);
            }

            for (int i = 0; i < outGLList.size(); ++i)
            {
                outGLList.get(i).setBackgroundResource(R.drawable.unavailable);
                outGLList.get(i).setOnTouchListener(nullTouch);
            }
        }
    }
//
    View.OnTouchListener nullTouch = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            return false;
        }
    };

    View.OnLongClickListener nullLongClick = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View view) {
            return false;
        }
    };

//    //TODO: URGENT: must compute the right tiles in the correct quadrant in the form of (ax + b) (cx + d)
    private void checkAnswers()
    {
        //TODO: rename, for factor activity, second answer is first answer
        if (!isFirstAnswerCorrect)
        {
            GridValue[] gvArr = { upperLeftGV, upperRightGV, lowerLeftGV, lowerRightGV };

            int totalRects = 0;
            for (ArrayList<RectTile> rList : rectTileListList)
            {
                totalRects += rList.size();
            }
            Log.d(TAG, "total Rects: " + totalRects);

            int totalTiles = 0;
            for (GridValue gV : gvArr)
            {
                totalTiles += gV.getCount();
            }
            Log.d(TAG, "total tiles: " + totalTiles);

            if (AlgorithmUtilities.isSecondAnswerCorrect(expandedVars, gvArr, numberOfVariables) &&
                    (totalRects == totalTiles))
            {
                //Toast.MakeText(Application.Context, "2:correct", ToastLength.Short).Show();
                if (!muteToggle.isChecked())
                    correct.start();
                isFirstAnswerCorrect = true;

                AlertDialog.Builder builder;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder = new AlertDialog.Builder(FactorActivity.this, android.R.style.Theme_Material_Dialog_Alert);
                } else {
                    builder = new AlertDialog.Builder(FactorActivity.this);
                }
                builder.setTitle(Constants.CORRECT)
                    .setMessage(Constants.CORRECT_PLACEMENT)
                    .setPositiveButton(Constants.PROCEED_TO_FACTOR, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // continue with delete
                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // do nothing
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();

                //Loop through inner and prevent deletions by removing: clonedImageView_Touch
                for (int i = 0; i < outerGridLayoutList.size(); ++i)
                {
                    outerGridLayoutList.get(i).setBackgroundResource(R.drawable.ok);
                    outerGridLayoutList.get(i).setOnTouchListener(nullTouch);
                    for (int j = 0; j < outerGridLayoutList.get(i).getChildCount(); ++j)
                    {
                        View iv = (View)outerGridLayoutList.get(i).getChildAt(j);
                        iv.setOnLongClickListener(nullLongClick);
                        iv.setOnTouchListener(nullTouch);
                        //iv.Visibility = ViewStates.Gone;
                    }
                }

                //Shade red the other grids
                for (int i = 0; i < innerGridLayoutList.size(); ++i)
                {
                    resetBGColors(innerGridLayoutList);
                    innerGridLayoutList.get(i).setOnTouchListener(layoutTouch);
                }

                //TODO:accomodate for 2 variables (right now just for one)
                x_value_1.setEnabled(true);
                one_value_1.setEnabled(true);
                x_value_2.setEnabled(true);
                one_value_2.setEnabled(true);
            }
            else
            {
                Toast.makeText(getApplicationContext(), Constants.WRONG + " " + Constants.FACTOR, Toast.LENGTH_SHORT).show();
                incorrectPrompt(outerGridLayoutList);
            }
        }
        else if (!isSecondAnswerCorrect)
        {
            GridValue[] gvArr = { midUpGV, midLowGV, midLeftGV, midRightGV };
            if (AlgorithmUtilities.isFirstAnswerCorrect(vars, gvArr, numberOfVariables))
            {
                //TODO: First answer is second in factor
                isSecondAnswerCorrect = true;

                for (int i = 0; i < outerGridLayoutList.size(); ++i)
                    outerGridLayoutList.get(i).setBackgroundResource(R.drawable.ok);

                //Loop through inner and prevent deletions by removing: clonedImageView_Touch
                for (int i = 0; i < innerGridLayoutList.size(); ++i)
                {
                    innerGridLayoutList.get(i).setBackgroundResource(R.drawable.ok);
                    innerGridLayoutList.get(i).setOnTouchListener(nullTouch);
                    for (int j = 0; j < innerGridLayoutList.get(i).getChildCount(); ++j)
                    {
                        View iv = innerGridLayoutList.get(i).getChildAt(j);
                        iv.setOnLongClickListener(nullLongClick);
                    }
                }

                //10-28-2016: Daniel said to remove the innerGridLayout images after second correct
                for (int i = 0; i < outerGridLayoutList.size(); ++i)
                {
                    for (int j = 0; j < outerGridLayoutList.get(i).getChildCount(); ++j)
                    {
                        AlgeTilesTextView iv = (AlgeTilesTextView) outerGridLayoutList.get(i).getChildAt(j);
                        iv.setVisibility(View.INVISIBLE);
                    }
                }

                expandedVars = AlgorithmUtilities.expandingVars(vars);

                //Removing outer grid after 2nd correct (10-28-2016)
                upperRightGrid.clearRects(heightInPx, widthInPx);
                upperLeftGrid.clearRects(heightInPx, widthInPx);
                lowerRightGrid.clearRects(heightInPx, widthInPx);
                lowerLeftGrid.clearRects(heightInPx, widthInPx);

                upperRightRectTileList.clear();
                upperLeftRectTileList.clear();
                lowerRightRectTileList.clear();
                lowerLeftRectTileList.clear();

                //Toast.MakeText(Application.Context, "1:correct", ToastLength.Short).Show();
                if (!muteToggle.isChecked())
                    correct.start();

                AlertDialog.Builder builder;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder = new AlertDialog.Builder(FactorActivity.this, android.R.style.Theme_Material_Dialog_Alert);
                } else {
                    builder = new AlertDialog.Builder(FactorActivity.this);
                }
                builder.setTitle(Constants.CORRECT)
                        .setMessage(Constants.CORRECT_FACTORS)
                        .setPositiveButton(Constants.PROCEED_TO_COEFF, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // continue with delete
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
            else
            {
                for (int i = 0; i < outerGridLayoutList.size(); ++i)
                {
                    outerGridLayoutList.get(i).setOnTouchListener(nullTouch);
                }

                incorrectPrompt(innerGridLayoutList);
                Toast.makeText(getApplicationContext(), Constants.WRONG + " " + Constants.MULTIPLICATION, Toast.LENGTH_SHORT).show();
            }
        }
        else if (!isThirdAnswerCorrect)
        {
            int[] answer = new int[4];
            int temp = 0;
            answer[0] = TileUtilities.tryParseInt(x_value_1.getText().toString());
            answer[1] = TileUtilities.tryParseInt(one_value_1.getText().toString());
            answer[2] = TileUtilities.tryParseInt(x_value_2.getText().toString());
            answer[3] = TileUtilities.tryParseInt(one_value_2.getText().toString());

            Log.d(TAG, "answer:" + answer[0] + ", " + answer[1] + ", " + answer[2] + ", " + answer[3]);
            Log.d(TAG, "answer:" + vars.get(0) + ", " + vars.get(1) + ", " + vars.get(2) + ", " + vars.get(3));
            if ((Math.abs(answer[0]) +
                    Math.abs(answer[1]) +
                    Math.abs(answer[2]) +
                    Math.abs(answer[3])) == 0)
            {
                isThirdAnswerCorrect = false;
            }
            else
            {
                if ((vars.get(0) == answer[0] && vars.get(1) == answer[1]) &&
                        (vars.get(2) == answer[2] && vars.get(3) == answer[3]) ||
                        (vars.get(0) == answer[2] && vars.get(1) == answer[3]) &&
                                (vars.get(2) == answer[0] && vars.get(3) == answer[1]))
                    isThirdAnswerCorrect = true;
            }
            if (isThirdAnswerCorrect)
            {
                //Toast.MakeText(Application.Context, "3:correct/end", ToastLength.Short).Show();
                if (!muteToggle.isChecked())
                    correct.start();
                for (int i = 0; i < editTextList.size(); ++i)
                {
                    editTextList.get(i).setBackgroundResource(R.drawable.ok);
                    editTextList.get(i).setEnabled(false);
                }

                AlertDialog.Builder builder;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder = new AlertDialog.Builder(FactorActivity.this, android.R.style.Theme_Material_Dialog_Alert);
                } else {
                    builder = new AlertDialog.Builder(FactorActivity.this);
                }
                builder.setTitle(Constants.CORRECT)
                        .setMessage(Constants.CORRECT + Constants.COEFFICIENTS)
                        .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
            else
            {
                Toast.makeText(getApplicationContext(), Constants.WRONG + " " + Constants.COEFFICIENTS, Toast.LENGTH_SHORT).show();
                incorrectPrompt(editTextList, 0);
            }
        }
    }

    public void incorrectPrompt(ArrayList<EditText> gvList, int dummy)
    {
        if (!muteToggle.isChecked())
            incorrect.start();
        for (int i = 0; i < gvList.size(); ++i)
            gvList.get(i).setBackgroundResource(R.drawable.notok);
//        await Task.Delay(Constants.DELAY);
        for (int i = 0; i < gvList.size(); ++i)
            gvList.get(i).setBackgroundResource(R.drawable.shape);
    }

    public void incorrectPrompt(ArrayList<ViewGroup> gvList)
    {
        if (!muteToggle.isChecked())
            incorrect.start();
        for (int i = 0; i < gvList.size(); ++i)
            gvList.get(i).setBackgroundResource(R.drawable.notok);
//        await Task.Delay(Constants.DELAY);
        resetBGColors(gvList);
    }
//
    private void resetBGColors(ArrayList<ViewGroup> vL)
    {
        for (ViewGroup v : vL)
        {
            if (v instanceof AlgeTilesRelativeLayout)
            {
                AlgeTilesRelativeLayout temp = (AlgeTilesRelativeLayout) v;
                temp.resetColor();
            }
				else
            {
                v.setBackgroundResource(R.drawable.shape);
            }
        }
    }
//
    private void resetBGColors(ViewGroup v)
    {
        if (v instanceof AlgeTilesRelativeLayout)
        {
            AlgeTilesRelativeLayout temp = (AlgeTilesRelativeLayout) v;
            temp.resetColor();
        }
			else
        {
            v.setBackgroundResource(R.drawable.shape);
        }
    }
//
    private void setupNewQuestion()
    {
        vars = AlgorithmUtilities.RNG(Constants.FACTOR, numberOfVariables);

        String temp = "";
        for (int i : vars)
        temp += i + ",";
        Log.d(TAG, "factors (ax + b)(cx + d):" + temp);

        expandedVars = AlgorithmUtilities.expandingVars(vars);
        String temp2 = "";
        for (int i : expandedVars)
        temp2 += i + ",";
        Log.d(TAG, "expanded (ax^2 + bx + c):" + temp2);


        //(ax + b)(cx + d)
        if (Constants.ONE_VAR == numberOfVariables)
        {
            for (int i = 0; i < gridValueList.size(); ++i)
            {
                gridValueList.get(i).init();
            }
            setupQuestionString(expandedVars);
        }
    }
//
    private void setupQuestionString(ArrayList<Integer> vars)
    {
        String output = "";
        //vars = (ax^2 + bx + c)
        int ax2 = vars.get(0);
        int bx = vars.get(1);
        int c = vars.get(2);

        if (ax2 != 0) {
            output += ax2 + "x2";
        }
        if (bx != 0) {
            output += "+" + bx + "x+";
        }
        if (c != 0) {
            output += c;
        }

        output = output.replace(" ", "");
        output = output.replace("+-", "-");
        output = output.replace("+", " + ");
        output = output.replace("-", " - ");

        Log.d(TAG, "output: " + output);
        SpannableStringBuilder cs = new SpannableStringBuilder(output);
        int indexOfSuperScript = output.indexOf("x2") + 1;
        cs.setSpan(new SuperscriptSpan(), indexOfSuperScript, indexOfSuperScript + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        cs.setSpan(new RelativeSizeSpan(0.75f), indexOfSuperScript, indexOfSuperScript + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        result.setText(cs);

//        result.setText(output);
    }
//
    private Rect checkIfUserDropsOnRect(int vId, String tileType, float x, float y, int command)
    {
        Log.d(TAG, "checkIfUserDropsOnRect");
        if (R.id.upperLeft == vId)
        {
            for (RectTile r : upperLeftRectTileList)
            {
                if (r.isPointInsideRect(x, y) && r.isTileTypeSame(tileType) && !r.getTilePresence())
                {
                    if (Constants.ADD == command)
                        r.setTilePresence(true);
                    return r.getRect();
                }
                else if (r.isPointInsideRect(x, y) && r.isTileTypeSame(tileType) && r.getTilePresence())
                {
                    if (Constants.SUBTRACT == command)
                    {
                        r.setTilePresence(false);
                        return null;
                    }
                }
            }
        }

        if (R.id.upperRight == vId)
        {
            for (RectTile r : upperRightRectTileList)
            {
                if (r.isPointInsideRect(x, y) && r.isTileTypeSame(tileType) && !r.getTilePresence())
                {
                    if (Constants.ADD == command)
                        r.setTilePresence(true);
                    return r.getRect();
                }
                else if (r.isPointInsideRect(x, y) && r.isTileTypeSame(tileType) && r.getTilePresence())
                {
                    if (Constants.SUBTRACT == command)
                    {
                        r.setTilePresence(false);
                        return null;
                    }
                }
            }
        }

        if (R.id.lowerLeft == vId)
        {
            for (RectTile r : lowerLeftRectTileList)
            {
                if (r.isPointInsideRect(x, y) && r.isTileTypeSame(tileType) && !r.getTilePresence())
                {
                    if (Constants.ADD == command)
                        r.setTilePresence(true);
                    return r.getRect();
                }
                else if (r.isPointInsideRect(x, y) && r.isTileTypeSame(tileType) && r.getTilePresence())
                {
                    if (Constants.SUBTRACT == command)
                    {
                        r.setTilePresence(false);
                        return null;
                    }
                }
            }
        }

        if (R.id.lowerRight == vId)
        {
            for (RectTile r : lowerRightRectTileList)
            {
                if (r.isPointInsideRect(x, y) && r.isTileTypeSame(tileType) && !r.getTilePresence())
                {
                    if (Constants.ADD == command)
                        r.setTilePresence(true);
                    return r.getRect();
                }
                else if (r.isPointInsideRect(x, y) && r.isTileTypeSame(tileType) && r.getTilePresence())
                {
                    if (Constants.SUBTRACT == command)
                    {
                        r.setTilePresence(false);
                        return null;
                    }
                }
            }
        }

        return null;
    }
//
    private void generateInnerLayoutTileArrays()
    {
        Log.d(TAG, "generateInnerLayoutTileArrays()");

        midUp.clear();
        midRight.clear();
        midDown.clear();
        midLeft.clear();

        upperRightRectTileList.clear();
        upperLeftRectTileList.clear();
        lowerRightRectTileList.clear();
        lowerLeftRectTileList.clear();

        ArrayList<Integer> input = vars;

        int ax = input.get(0); //ax
        int b = input.get(1); //b

        int cx = input.get(2); //cx
        int d = input.get(3); //d

        Log.d(TAG, ax + "," + b + "," + cx + "," + d);

        if (ax > 0)
        {
            for (int i = 0; i < ax; ++i)
                midRight.add(Constants.X_TILE);
        }
        else if (ax < 0)
        {
            for (int i = 0; i > ax; --i)
                midLeft.add(Constants.X_TILE);
        }

        if (b > 0)
        {
            for (int i = 0; i < b; ++i)
                midRight.add(Constants.ONE_TILE);
        }
        else if (b < 0)
        {
            for (int i = 0; i > b; --i)
                midLeft.add(Constants.ONE_TILE);
        }

        if (cx > 0)
        {
            for (int i = 0; i < cx; ++i)
                midUp.add(Constants.X_TILE);
        }
        else if (cx < 0)
        {
            for (int i = 0; i > cx; --i)
                midDown.add(Constants.X_TILE);
        }

        if (d > 0)
        {
            for (int i = 0; i < d; ++i)
                midUp.add(Constants.ONE_TILE);
        }
        else if (d < 0)
        {
            for (int i = 0; i > d; --i)
                midDown.add(Constants.ONE_TILE);
        }

        int height = heightInPx;
        int width = widthInPx;

        //upmid x midRight = quadrant1
        if (midUp.size() != 0 || midRight.size() != 0)
        {
            int top = height;
            int bottom = height;
            for (int i = 0; i < midUp.size(); ++i)
            {
                int left = 0;
                int right = 0;
                boolean firstPass = true;
                for (int j = 0; j < midRight.size(); ++j)
                {
                    int[] productDimensions = TileUtilities.getDimensionsOfProduct(height, midUp.get(i), midRight.get(j));
                    if (firstPass)
                    {
                        top -= productDimensions[0];
                        bottom = top + productDimensions[0];
                        firstPass = false;
                    }
                    right += productDimensions[1];
                    left = right - productDimensions[1];

                    Rect r = new Rect(left, top, right, bottom);
                    upperRightRectTileList.add(new RectTile(r, TileUtilities.getTileTypeOfProduct(midUp.get(i), midRight.get(j))));
                }
            }
        }

        //upmid x midLeft = quadrant2
        if (midUp.size() != 0 || midLeft.size() != 0)
        {
            int top = height;
            int bottom = height;
            for (int i = 0; i < midUp.size(); ++i)
            {
                int left = width;
                int right = width;
                boolean firstPass = true;
                for (int j = 0; j < midLeft.size(); ++j)
                {
                    int[] productDimensions = TileUtilities.getDimensionsOfProduct(height, midUp.get(i), midLeft.get(j));
                    if (firstPass)
                    {
                        top -= productDimensions[0];
                        bottom = top + productDimensions[0];
                        firstPass = false;
                    }
                    left -= productDimensions[1];
                    right = left + productDimensions[1];

                    Rect r = new Rect(left, top, right, bottom);
                    upperLeftRectTileList.add(new RectTile(r, TileUtilities.getTileTypeOfProduct(midUp.get(i), midLeft.get(j))));
                }
            }
        }

        //loMid x midLeft = quadrant3
        if (midDown.size() != 0 || midLeft.size() != 0)
        {
            int top = 0;
            int bottom = 0;
            for (int i = 0; i < midDown.size(); ++i)
            {
                int left = width;
                int right = width;
                boolean firstPass = true;
                for (int j = 0; j < midLeft.size(); ++j)
                {
                    int[] productDimensions = TileUtilities.getDimensionsOfProduct(height, midDown.get(i), midLeft.get(j));
                    if (firstPass)
                    {
                        bottom += productDimensions[0];
                        top = bottom - productDimensions[0];
                        firstPass = false;
                    }
                    left -= productDimensions[1];
                    right = left + productDimensions[1];

                    Rect r = new Rect(left, top, right, bottom);
                    lowerLeftRectTileList.add(new RectTile(r, TileUtilities.getTileTypeOfProduct(midDown.get(i), midLeft.get(j))));
                }
            }
        }

        //loMid x midRight = quadrant4
        if (midDown.size() != 0 || midRight.size() != 0)
        {
            int top = 0;
            int bottom = 0;
            for (int i = 0; i < midDown.size(); ++i)
            {
                int left = 0;
                int right = 0;
                boolean firstPass = true;
                for (int j = 0; j < midRight.size(); ++j)
                {
                    int[] productDimensions = TileUtilities.getDimensionsOfProduct(height, midDown.get(i), midRight.get(j));
                    if (firstPass)
                    {
                        bottom += productDimensions[0];
                        top = bottom - productDimensions[0];
                        firstPass = false;
                    }
                    right += productDimensions[1];
                    left = right - productDimensions[1];
                    Rect r = new Rect(left, top, right, bottom);
                    lowerRightRectTileList.add(new RectTile(r, TileUtilities.getTileTypeOfProduct(midDown.get(i), midRight.get(j))));
                }
            }
        }
    }

    //http://stackoverflow.com/questions/18836432/how-to-find-the-view-of-a-button-in-its-click-eventhandler
    //TODO: When top most layer textview increases in length, the edit text gets pushed
    View.OnLongClickListener clonedImageView_Touch = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View view) {
            AlgeTilesTextView touchedImageView = (AlgeTilesTextView) view;
            ViewGroup vg = (ViewGroup)touchedImageView.getParent();
            if (removeToggle.isChecked())
            {
                Log.d(TAG, "Switch: Remove");
                checkIfUserDropsOnRect(vg.getId(),
                        touchedImageView.getTileType(),
                        touchedImageView.getLeft() + 10,
                        touchedImageView.getTop() + 10,
                        Constants.SUBTRACT);
                vg.removeView(touchedImageView);
                touchedImageView.setVisibility(View.GONE);
                Vibrator vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
                vibrator.vibrate(30);

                int id = touchedImageView.getId();

                checkWhichParentAndUpdate(vg.getId(), touchedImageView.getTileType(), Constants.SUBTRACT);
            }

            if (dragToggle.isChecked())
            {
                Log.d(TAG, "Switch: Drag");
            }

            //TODO: Not working
            if (rotateToggle.isChecked())
            {
                Log.d(TAG, "Switch: Rotate");
                touchedImageView.setRotation(touchedImageView.getRotation() - 90);
            }
            return true;
        }
    };

    private void checkWhichParentAndUpdate(int id, String tile, int process)
    {
        if (Constants.ADD == process)
        {
            if (R.id.upperLeft == id)
            {
                if (Constants.X2_TILE == tile || Constants.X2_TILE_ROT == tile)
                    ++upperLeftGV.x2TileCount;
                if (Constants.X_TILE == tile || Constants.X_TILE_ROT == tile)
                    ++upperLeftGV.xTileCount;
                if (Constants.ONE_TILE == tile || Constants.ONE_TILE_ROT == tile)
                    ++upperLeftGV.oneTileCount;
            }
            if (R.id.upperRight == id)
            {
                if (Constants.X2_TILE == tile || Constants.X2_TILE_ROT == tile)
                    ++upperRightGV.x2TileCount;
                if (Constants.X_TILE == tile || Constants.X_TILE_ROT == tile)
                    ++upperRightGV.xTileCount;
                if (Constants.ONE_TILE == tile || Constants.ONE_TILE_ROT == tile)
                    ++upperRightGV.oneTileCount;
            }
            if (R.id.lowerLeft == id)
            {
                if (Constants.X2_TILE == tile || Constants.X2_TILE_ROT == tile)
                    ++lowerLeftGV.x2TileCount;
                if (Constants.X_TILE == tile || Constants.X_TILE_ROT == tile)
                    ++lowerLeftGV.xTileCount;
                if (Constants.ONE_TILE == tile || Constants.ONE_TILE_ROT == tile)
                    ++lowerLeftGV.oneTileCount;
            }
            if (R.id.lowerRight == id)
            {
                if (Constants.X2_TILE == tile || Constants.X2_TILE_ROT == tile)
                    ++lowerRightGV.x2TileCount;
                if (Constants.X_TILE == tile || Constants.X_TILE_ROT == tile)
                    ++lowerRightGV.xTileCount;
                if (Constants.ONE_TILE == tile || Constants.ONE_TILE_ROT == tile)
                    ++lowerRightGV.oneTileCount;
            }

            //CENTER
            if (R.id.upperMiddle == id)
            {
                if (Constants.X2_TILE == tile || Constants.X2_TILE_ROT == tile)
                    ++midUpGV.x2TileCount;
                if (Constants.X_TILE == tile || Constants.X_TILE_ROT == tile)
                    ++midUpGV.xTileCount;
                if (Constants.ONE_TILE == tile || Constants.ONE_TILE_ROT == tile)
                    ++midUpGV.oneTileCount;
            }
            if (R.id.lowerMiddle == id)
            {
                if (Constants.X2_TILE == tile || Constants.X2_TILE_ROT == tile)
                    ++midLowGV.x2TileCount;
                if (Constants.X_TILE == tile || Constants.X_TILE_ROT == tile)
                    ++midLowGV.xTileCount;
                if (Constants.ONE_TILE == tile || Constants.ONE_TILE_ROT == tile)
                    ++midLowGV.oneTileCount;
            }
            if (R.id.middleLeft == id)
            {
                if (Constants.X2_TILE == tile || Constants.X2_TILE_ROT == tile)
                    ++midLeftGV.x2TileCount;
                if (Constants.X_TILE == tile || Constants.X_TILE_ROT == tile)
                    ++midLeftGV.xTileCount;
                if (Constants.ONE_TILE == tile || Constants.ONE_TILE_ROT == tile)
                    ++midLeftGV.oneTileCount;
            }
            if (R.id.middleRight == id)
            {
                if (Constants.X2_TILE == tile || Constants.X2_TILE_ROT == tile)
                    ++midRightGV.x2TileCount;
                if (Constants.X_TILE == tile || Constants.X_TILE_ROT == tile)
                    ++midRightGV.xTileCount;
                if (Constants.ONE_TILE == tile || Constants.ONE_TILE_ROT == tile)
                    ++midRightGV.oneTileCount;
            }

        }
        //REMOVE
        else if (Constants.SUBTRACT == process)
        {
            if (R.id.upperLeft == id)
            {
                if (Constants.X2_TILE == tile || Constants.X2_TILE_ROT == tile)
                    --upperLeftGV.x2TileCount;
                if (Constants.X_TILE == tile || Constants.X_TILE_ROT == tile)
                    --upperLeftGV.xTileCount;
                if (Constants.ONE_TILE == tile || Constants.ONE_TILE_ROT == tile)
                    --upperLeftGV.oneTileCount;
            }
            if (R.id.upperRight == id)
            {
                if (Constants.X2_TILE == tile || Constants.X2_TILE_ROT == tile)
                    --upperRightGV.x2TileCount;
                if (Constants.X_TILE == tile || Constants.X_TILE_ROT == tile)
                    --upperRightGV.xTileCount;
                if (Constants.ONE_TILE == tile || Constants.ONE_TILE_ROT == tile)
                    --upperRightGV.oneTileCount;
            }
            if (R.id.lowerLeft == id)
            {
                if (Constants.X2_TILE == tile || Constants.X2_TILE_ROT == tile)
                    --lowerLeftGV.x2TileCount;
                if (Constants.X_TILE == tile || Constants.X_TILE_ROT == tile)
                    --lowerLeftGV.xTileCount;
                if (Constants.ONE_TILE == tile || Constants.ONE_TILE_ROT == tile)
                    --lowerLeftGV.oneTileCount;
            }
            if (R.id.lowerRight == id)
            {
                if (Constants.X2_TILE == tile || Constants.X2_TILE_ROT == tile)
                    --lowerRightGV.x2TileCount;
                if (Constants.X_TILE == tile || Constants.X_TILE_ROT == tile)
                    --lowerRightGV.xTileCount;
                if (Constants.ONE_TILE == tile || Constants.ONE_TILE_ROT == tile)
                    --lowerRightGV.oneTileCount;
            }

            //CENTER
            if (R.id.upperMiddle == id)
            {
                if (Constants.X2_TILE == tile || Constants.X2_TILE_ROT == tile)
                    --midUpGV.x2TileCount;
                if (Constants.X_TILE == tile || Constants.X_TILE_ROT == tile)
                    --midUpGV.xTileCount;
                if (Constants.ONE_TILE == tile || Constants.ONE_TILE_ROT == tile)
                    --midUpGV.oneTileCount;
            }
            if (R.id.lowerMiddle == id)
            {
                if (Constants.X2_TILE == tile || Constants.X2_TILE_ROT == tile)
                    --midLowGV.x2TileCount;
                if (Constants.X_TILE == tile || Constants.X_TILE_ROT == tile)
                    --midLowGV.xTileCount;
                if (Constants.ONE_TILE == tile || Constants.ONE_TILE_ROT == tile)
                    --midLowGV.oneTileCount;
            }
            if (R.id.middleLeft == id)
            {
                if (Constants.X2_TILE == tile || Constants.X2_TILE_ROT == tile)
                    --midLeftGV.x2TileCount;
                if (Constants.X_TILE == tile || Constants.X_TILE_ROT == tile)
                    --midLeftGV.xTileCount;
                if (Constants.ONE_TILE == tile || Constants.ONE_TILE_ROT == tile)
                    --midLeftGV.oneTileCount;
            }
            if (R.id.middleRight == id)
            {
                if (Constants.X2_TILE == tile || Constants.X2_TILE_ROT == tile)
                    --midRightGV.x2TileCount;
                if (Constants.X_TILE == tile || Constants.X_TILE_ROT == tile)
                    --midRightGV.xTileCount;
                if (Constants.ONE_TILE == tile || Constants.ONE_TILE_ROT == tile)
                    --midRightGV.oneTileCount;
            }
        }
    }
}
