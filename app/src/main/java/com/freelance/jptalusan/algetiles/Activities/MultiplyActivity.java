package com.freelance.jptalusan.algetiles.Activities;

import android.app.Dialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.SpannableStringBuilder;
import android.text.style.RelativeSizeSpan;
import android.text.style.SuperscriptSpan;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.freelance.jptalusan.algetiles.ExtendedViews.AlgeTilesActivity;
import com.freelance.jptalusan.algetiles.ExtendedViews.AlgeTilesTextView;
import com.freelance.jptalusan.algetiles.R;
import com.freelance.jptalusan.algetiles.Utilities.AlgorithmUtilities;
import com.freelance.jptalusan.algetiles.Utilities.Constants;
import com.freelance.jptalusan.algetiles.Utilities.GridValue;
import com.freelance.jptalusan.algetiles.Utilities.Listeners;
import com.freelance.jptalusan.algetiles.Utilities.TileUtilities;

import java.io.Console;
import java.util.ArrayList;

/**
 * Created by jptalusan on 10/5/17.
 */

public class MultiplyActivity extends AlgeTilesActivity {

    private static String TAG = "AlgeTiles:Multiply";
    public Button customQuestion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        numberOfVariables = getIntent().getIntExtra(Constants.VARIABLE_COUNT, 0);

        setContentView(R.layout.activity_multiply);
        activityType = Constants.MULTIPLY;
        listeners = new Listeners(this);
        // Create your application here
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
        x2TV.setText("x2 + ");

        customQuestion = findViewById(R.id.custom);
        customQuestion.setOnClickListener(CustomQuestion_Click);

        upperLeftGrid.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (!isFirstTime) {
                    heightInPx = upperLeftGrid.getHeight();
                    widthInPx = upperLeftGrid.getWidth();
                    upperLeftGrid.setMinimumHeight(0);
                    upperLeftGrid.setMinimumWidth(0);
                    isFirstTime = true;

                    LinearLayout.LayoutParams par_1 = (LinearLayout.LayoutParams) tile_1.getLayoutParams();
                    TileUtilities.TileFactor tF = TileUtilities.getTileFactors(tile_1.getTileType());
                    par_1.height = heightInPx / 7;
                    par_1.width = heightInPx / 7;
                    //tile_1.setDimensions(par_1.Height, par_1.Width);
                    tile_1.setBackgroundResource(tF.id);
                    tile_1.setText(tF.text);
                    tile_1.setLayoutParams(par_1);
                    Log.d(TAG, tile_1.getTileType());

                    LinearLayout.LayoutParams par_x = (LinearLayout.LayoutParams) x_tile.getLayoutParams();
                    tF = TileUtilities.getTileFactors(x_tile.getTileType());
                    par_x.height = (int) (heightInPx / tF.heightFactor);
                    par_x.width = heightInPx / 7;
                    //x_tile.setDimensions(par_x.Height, par_x.Width);
                    x_tile.setBackgroundResource(tF.id);
                    x_tile.setText(tF.text);
                    x_tile.setLayoutParams(par_x);
                    Log.d(TAG, x_tile.getTileType());

                    LinearLayout.LayoutParams par_x2 = (LinearLayout.LayoutParams) x2_tile.getLayoutParams();
                    tF = TileUtilities.getTileFactors(x2_tile.getTileType());
                    par_x2.height = (int) (heightInPx / tF.heightFactor);
                    par_x2.width = (int) (heightInPx / tF.widthFactor);
                    //x2_tile.setDimensions(par_x2.Height, par_x2.Width);
                    x2_tile.setBackgroundResource(tF.id);
                    x2_tile.setText(tF.text);
                    x2_tile.setLayoutParams(par_x2);
                    Log.d(TAG, x2_tile.getTileType());

                    x2ET.setHeight(par_x2.height);
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
        //Together form one Part of the formula
        upperMiddleGrid.setOnTouchListener(listeners.Layout_Touch);
        lowerMiddleGrid.setOnTouchListener(listeners.Layout_Touch);

        //Together form one Part of the formula
        middleLeftGrid.setOnTouchListener(listeners.Layout_Touch);
        middleRightGrid.setOnTouchListener(listeners.Layout_Touch);

        //Shade red the other grids
        for (int i = 0; i < outerGridLayoutList.size(); ++i) {
            outerGridLayoutList.get(i).setBackgroundResource(R.drawable.unavailable);
        }

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

        prefs = PreferenceManager.getDefaultSharedPreferences(this);
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

        setupNewQuestion(numberOfVariables);

        correct = MediaPlayer.create(this, R.raw.correct);
        incorrect = MediaPlayer.create(this, R.raw.wrong);

        x2ET = findViewById(R.id.x2_value);
        xET = findViewById(R.id.x_value);
        oneET = findViewById(R.id.one_value);

        editTextList.add(x2ET);
        editTextList.add(xET);
        editTextList.add(oneET);

        refreshScreen(Constants.MULTIPLY, gridValueList, innerGridLayoutList, outerGridLayoutList);

        rectTileListList.add(upperRightRectTileList);
        rectTileListList.add(upperLeftRectTileList);
        rectTileListList.add(lowerLeftRectTileList);
        rectTileListList.add(lowerRightRectTileList);

        settingsDialog = new Dialog(this);
//        settingsDialog.Window.RequestFeature(WindowFeatures.NoTitle);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_multiply;
    }

    @Override
    protected void setupQuestionString(ArrayList<Integer> vars) {
        String output = "(";
        //vars = (ax + b)(cx + d)
        int ax = vars.get(0);
        int b = vars.get(1);
        int cx = vars.get(2);
        int d = vars.get(3);

        if (ax != 0)
            output += ax + "x+";
        if (b != 0)
            output += b;
        else
            output = output.substring(0, output.length() - 1);

        output += ")(";

        if (cx != 0)
            output += cx + "x+";
        if (d != 0)
            output += d;
        else
            output = output.substring(0, output.length() - 1);

        output += ")";
        output = output.replace(" ", "");
        output = output.replace("+-", "-");
        output = output.replace("+", " + ");
        output = output.replace("-", " - ");
        result.setText(output);
    }

    private View.OnClickListener CustomQuestion_Click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
//            var dialog = CustomEquationDialog.NewInstance();
//            dialog.Dismissed += (s, events) =>
//            {
//                if (events.vars[0].HasValue)
//                    Console.WriteLine("Done with dialog: " + events.vars[0] + "," + events.vars[1] + "," + events.vars[2] + "," + events.vars[3]);
//
//                //Replace vars with event.vars and rerun the setupQuestionString(vars) after checking if this is valid, if not, show toast
//                if (AlgorithmUtilities.isSuppliedMultiplyEquationValid(events.vars, Constants.ONE_VAR))
//                {
//                    vars.Clear();
//                    for(int i = 0; i < events.vars.Length; ++i)
//                    {
//                        vars.Add(events.vars[i].GetValueOrDefault());
//                    }
//                    setupQuestionString(vars);
//                    refreshScreen(Constants.MULTIPLY, gridValueList, innerGridLayoutList, outerGridLayoutList);
//                }
//                else
//                {
//                    Toast.MakeText(this, "Invalid, please enter values again.", ToastLength.Short).Show();
//                }
//            };
//            dialog.Show(FragmentManager, "dialog");
        }
    };

    private View.OnClickListener button_click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
//            var button = sender as Button;
//            AlertDialog.Builder builder = new AlertDialog.Builder(this);
//            AlertDialog alertDialog = builder.Create();
//            alertDialog.SetMessage("Are you sure?");
//
//            alertDialog.SetButton("Yes", (s, ev) =>
//            {
//                switch (button.Text)
//                {
//                    case Constants.NEW_Q:
//                        setupNewQuestion(numberOfVariables);
//                        refreshScreen(Constants.MULTIPLY, gridValueList, innerGridLayoutList, outerGridLayoutList);
//                        break;
//                    case Constants.REFR:
//                        refreshScreen(Constants.MULTIPLY, gridValueList, innerGridLayoutList, outerGridLayoutList);
//                        break;
//                    case Constants.CHK:
//                        checkAnswers(this);
//                        break;
//                }
//            });
//
//            alertDialog.SetButton2("No", (s, ev) =>
//            {
//
//            });
//
//            alertDialog.Show();
        }
    };
}
