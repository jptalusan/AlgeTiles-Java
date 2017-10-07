package com.freelance.jptalusan.algetiles.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.text.SpannableStringBuilder;
import android.text.style.RelativeSizeSpan;
import android.text.style.SuperscriptSpan;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.freelance.jptalusan.algetiles.ExtendedViews.AlgeTilesActivity;
import com.freelance.jptalusan.algetiles.ExtendedViews.AlgeTilesTextView;
import com.freelance.jptalusan.algetiles.R;
import com.freelance.jptalusan.algetiles.Utilities.AlgorithmUtilities;
import com.freelance.jptalusan.algetiles.Utilities.Constants;
import com.freelance.jptalusan.algetiles.Utilities.CustomEquationDialog;
import com.freelance.jptalusan.algetiles.Utilities.CustomEquationDialogTwoVar;
import com.freelance.jptalusan.algetiles.Utilities.GridValue;
import com.freelance.jptalusan.algetiles.Utilities.Listeners;
import com.freelance.jptalusan.algetiles.Utilities.TileUtilities;

import java.util.ArrayList;

/**
 * Created by jptalusan on 10/7/17.
 */

public class MultiplyTwoVarActivity extends AlgeTilesActivity implements CustomEquationDialogTwoVar.CustomEquationDialogTwoVarListener {
    private static String TAG = "AlgeTiles:MultiplyTwoVarActivity";
    public Button customQuestion;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        numberOfVariables = getIntent().getIntExtra(Constants.VARIABLE_COUNT, 0);

        setContentView(R.layout.activity_multiply_twovar);

        // Create your application here
        activityType = Constants.MULTIPLY;
        listeners = new Listeners(this);

        result = findViewById(R.id.result);

        tile_1 = findViewById(R.id.tile_1);
        x_tile = findViewById(R.id.x_tile);
        y_tile = findViewById(R.id.y_tile);
        xy_tile = findViewById(R.id.xy_tile);
        x2_tile = findViewById(R.id.x2_tile);
        y2_tile = findViewById(R.id.y2_tile);

        tile_1.setOnClickListener(listeners.tile_click);
        x_tile.setOnClickListener(listeners.tile_click);
        y_tile.setOnClickListener(listeners.tile_click);
        x2_tile.setOnClickListener(listeners.tile_click);
        y2_tile.setOnClickListener(listeners.tile_click);
        xy_tile.setOnClickListener(listeners.tile_click);

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

        x2TV = findViewById(R.id.x2TV);
        y2TV = findViewById(R.id.y2TV);
//        x2TV.setText("x\xB2 + ");
//        y2TV.setText("y\xB2 + ");

        customQuestion = findViewById(R.id.custom);
        customQuestion.setOnClickListener(CustomQuestion_Click);

        upperLeftGrid.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (!isFirstTime)
                {
                    heightInPx = upperLeftGrid.getHeight();
                    widthInPx = upperLeftGrid.getWidth();
                    upperLeftGrid.setMinimumHeight(0);
                    upperLeftGrid.setMinimumWidth(0);
                    isFirstTime = true;
    
                    LinearLayout.LayoutParams par_1 = (LinearLayout.LayoutParams)tile_1.getLayoutParams();
                    TileUtilities.TileFactor tF = TileUtilities.getTileFactors(tile_1.getTileType());
                    par_1.height = (heightInPx / 7);
                    par_1.width = (heightInPx / 7);
                    tile_1.setBackgroundResource(tF.id);
                    tile_1.setText(tF.text);
                    tile_1.setLayoutParams(par_1);
    
                    LinearLayout.LayoutParams par_x = (LinearLayout.LayoutParams)x_tile.getLayoutParams();
                    tF = TileUtilities.getTileFactors(x_tile.getTileType());
                    par_x.height = (int)(heightInPx / tF.heightFactor);
                    par_x.width = (heightInPx / 7);
                    x_tile.setBackgroundResource(tF.id);
                    x_tile.setText(tF.text);
                    x_tile.setLayoutParams(par_x);
    
                    LinearLayout.LayoutParams par_y = (LinearLayout.LayoutParams)y_tile.getLayoutParams();
                    tF = TileUtilities.getTileFactors(y_tile.getTileType());
                    par_y.height = (int)(heightInPx / tF.heightFactor);
                    par_y.width = (heightInPx / 7);
                    y_tile.setBackgroundResource(tF.id);
                    y_tile.setText(tF.text);
                    y_tile.setLayoutParams(par_y);
    
                    LinearLayout.LayoutParams par_x2 = (LinearLayout.LayoutParams)x2_tile.getLayoutParams();
                    tF = TileUtilities.getTileFactors(x2_tile.getTileType());
                    par_x2.height = (int)(heightInPx / tF.heightFactor);
                    par_x2.width = (int)(heightInPx / tF.widthFactor);
                    x2_tile.setBackgroundResource(tF.id);
                    x2_tile.setText(tF.text);
                    x2_tile.setLayoutParams(par_x2);
    
                    LinearLayout.LayoutParams par_y2 = (LinearLayout.LayoutParams)y2_tile.getLayoutParams();
                    tF = TileUtilities.getTileFactors(y2_tile.getTileType());
                    par_y2.height = (int)(heightInPx / tF.heightFactor);
                    par_y2.width = (int)(heightInPx / tF.widthFactor);
                    y2_tile.setBackgroundResource(tF.id);

                    y2_tile.setText(tF.text);
                    y2_tile.setLayoutParams(par_y2);
    
                    LinearLayout.LayoutParams par_xy = (LinearLayout.LayoutParams)xy_tile.getLayoutParams();
                    tF = TileUtilities.getTileFactors(xy_tile.getTileType());
                    par_xy.height = (int)(heightInPx / tF.heightFactor);
                    par_xy.width = (int)(heightInPx / tF.widthFactor);
                    xy_tile.setBackgroundResource(tF.id);
                    xy_tile.setText(tF.text);

                    xy_tile.setLayoutParams(par_xy);
    
                    x2ET.setHeight(par_x2.height);
                    xET.setHeight(par_x2.height);
                    result.setHeight(par_x2.height / 2);
            }
        }});

        outerGridLayoutList.add(upperLeftGrid);
        outerGridLayoutList.add(upperRightGrid);
        outerGridLayoutList.add(lowerLeftGrid);
        outerGridLayoutList.add(lowerRightGrid);

        innerGridLayoutList.add(upperMiddleGrid);
        innerGridLayoutList.add(middleLeftGrid);
        innerGridLayoutList.add(middleRightGrid);
        innerGridLayoutList.add(lowerMiddleGrid);

        //For multiply this is the initial grid available
        //Together form one Part of the formula
        upperMiddleGrid.setOnTouchListener(listeners.Layout_Touch);
        lowerMiddleGrid.setOnTouchListener(listeners.Layout_Touch);

        //Together form one Part of the formula
        middleLeftGrid.setOnTouchListener(listeners.Layout_Touch);
        middleRightGrid.setOnTouchListener(listeners.Layout_Touch);

        //Shade red the other grids
        for (int i = 0; i < outerGridLayoutList.size(); ++i)
        outerGridLayoutList.get(i).setBackgroundResource(R.drawable.unavailable);

        removeToggle = findViewById(R.id.remove);
        dragToggle = findViewById(R.id.drag);
        rotateToggle = findViewById(R.id.rotate);
        muteToggle = findViewById(R.id.mute);

        removeToggle.setOnClickListener(listeners.toggle_click);
        dragToggle.setOnClickListener(listeners.toggle_click);
        rotateToggle.setOnClickListener(listeners.toggle_click);
        muteToggle.setOnClickListener(listeners.toggle_click);

        tutorialButton = findViewById(R.id.tutorial);
        tutorialButton.setOnClickListener(listeners.toggle_click);

        prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        muteToggle.setChecked(prefs.getBoolean(Constants.MUTE, false));

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

        x2ET = findViewById(R.id.x2_value);
        y2ET = findViewById(R.id.y2_value);
        xyET = findViewById(R.id.xy_value);
        xET = findViewById(R.id.x_value);
        yET = findViewById(R.id.y_value);
        oneET = findViewById(R.id.one_value);

        editTextList.add(x2ET);
        editTextList.add(y2ET);
        editTextList.add(xyET);
        editTextList.add(xET);
        editTextList.add(yET);
        editTextList.add(oneET);

        sv = findViewById(R.id.sv);

        refreshScreen(Constants.MULTIPLY, gridValueList, innerGridLayoutList, outerGridLayoutList);

        rectTileListList.add(upperRightRectTileList);
        rectTileListList.add(upperLeftRectTileList);
        rectTileListList.add(lowerLeftRectTileList);
        rectTileListList.add(lowerRightRectTileList);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_multiply;
    }

    @Override
    protected void setupQuestionString(ArrayList<Integer> vars) {

        String output = "";
        output += "(";
        //vars = (ax + by + c)(dx + ey + f)
        int ax = vars.get(0);
        int by = vars.get(1);
        int c = vars.get(2);

        int dx = vars.get(3);
        int ey = vars.get(4);
        int f = vars.get(5);

        if (ax != 0)
            output += ax + "x+";
        if (by != 0)
            output += by + "y+";
        if (c != 0)
            output += c;
        else
            output = output.substring(0, output.length() - 1);

        output += ")(";

        if (dx != 0)
            output += dx + "x+";
        if (ey != 0)
            output += ey + "y+";
        if (f != 0)
            output += f;
        else
            output = output.substring(0, output.length() - 1);

        output += ")";
        output = output.replace(" ", "");
        output = output.replace("+-", "-");
        output = output.replace("+", " + ");
        output = output.replace("-", " - ");
        result.setText(output);
    }

    @Override
    public void onButtonClicked(int[] questions) {
        Log.d(TAG, "Clicked..");
        if (questions.length != 6) {
            Log.d(TAG, "clicked: wrong");
        } else {
            Log.d(TAG, "clicked: " + questions[0] + ", " + questions[1] + ", " + questions[2] + ", " + questions[3] + ", " + questions[4] + ", " + questions[5]);
            if (AlgorithmUtilities.isSuppliedMultiplyEquationValid(questions, Constants.TWO_VAR))
            {
                vars.clear();
                for (int i : questions)
                {
                    vars.add(i);
                }
                setupQuestionString(vars);
                refreshScreen(Constants.MULTIPLY, gridValueList, innerGridLayoutList, outerGridLayoutList);
            }
            else
            {
                Toast.makeText(this, "Invalid, please enter values again.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private View.OnClickListener CustomQuestion_Click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            FragmentManager fm = getSupportFragmentManager();
            CustomEquationDialogTwoVar editNameDialogFragment = CustomEquationDialogTwoVar.newInstance("Some Title");
            editNameDialogFragment.show(fm, "fragment_edit_name");
        }
    };

    private View.OnClickListener button_click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final Button button = (Button) v;
            AlertDialog.Builder builder;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                builder = new AlertDialog.Builder(MultiplyTwoVarActivity.this, android.R.style.Theme_Material_Dialog_NoActionBar);
            } else {
                builder = new AlertDialog.Builder(MultiplyTwoVarActivity.this);
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
                                    refreshScreen(Constants.MULTIPLY, gridValueList, innerGridLayoutList, outerGridLayoutList);
                                    break;
                                case Constants.REFR:
                                    refreshScreen(Constants.MULTIPLY, gridValueList, innerGridLayoutList, outerGridLayoutList);
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
}
