package com.freelance.jptalusan.algetiles.Utilities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.text.SpannableStringBuilder;
import android.text.style.RelativeSizeSpan;
import android.text.style.SuperscriptSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.freelance.jptalusan.algetiles.Activities.FactorActivity;
import com.freelance.jptalusan.algetiles.Activities.TutorialActivity;
import com.freelance.jptalusan.algetiles.ExtendedViews.AlgeTilesActivity;
import com.freelance.jptalusan.algetiles.ExtendedViews.AlgeTilesTextView;
import com.freelance.jptalusan.algetiles.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by jptalusan on 10/5/17.
 */

public class Listeners {
    private static String TAG = "AlgeTiles:Listeners";
    AlgeTilesActivity a;
    public Listeners(AlgeTilesActivity a)
    {
        this.a = a;
    }

    //TODO: When top most layer textview increases in length, the edit text gets pushed
    public View.OnLongClickListener clonedImageView_Touch = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            AlgeTilesTextView touchedImageView = (AlgeTilesTextView) v;
            ViewGroup vg = (ViewGroup)touchedImageView.getParent();
            if (a.removeToggle.isChecked())
            {
                Log.d(TAG, "Switch: Remove");
                TileUtilities.checkIfUserDropsOnRect(vg.getId(),
                        touchedImageView.getTileType(),
                        touchedImageView.getLeft() + 10,
                        touchedImageView.getTop() + 10,
                        Constants.SUBTRACT,
                        a.rectTileListList);
                vg.removeView(touchedImageView);
                touchedImageView.setVisibility(View.GONE);
                Vibrator vibrator = (Vibrator)a.getSystemService(Context.VIBRATOR_SERVICE);
                vibrator.vibrate(30);

                int id = touchedImageView.getId();

                TileUtilities.checkWhichParentAndUpdate(vg.getId(), touchedImageView.getTileType(), Constants.SUBTRACT, a.gridValueList);
            }

            if (a.dragToggle.isChecked())
            {
                Log.d(TAG, "Switch: Drag");
            }

            //TODO: Not working
            if (a.rotateToggle.isChecked())
            {
                Log.d(TAG, "Switch: Rotate");
                touchedImageView.setRotation(touchedImageView.getRotation() - 90);
            }

            return true;
        }
    };

    public View.OnTouchListener Layout_Touch = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent e) {
            ViewGroup vg = (ViewGroup) v;
            float x = e.getX(0);
            float y = e.getY(0);
            boolean isDroppedAtCenter = false;

            if (a.oneTile_Clicked)
            {
                a.currentButtonType = Constants.ONE_TILE;
            }
            else if (a.xTile_Clicked)
            {
                a.currentButtonType = Constants.X_TILE;
            }
            else if (a.x2Tile_Clicked)
            {
                a.currentButtonType = Constants.X2_TILE;
            }
            else if (a.xyTile_Clicked)
            {
                a.currentButtonType = Constants.XY_TILE;
            }
            else if (a.yTile_Clicked)
            {
                a.currentButtonType = Constants.Y_TILE;
            }
            else if (a.y2Tile_Clicked)
            {
                a.currentButtonType = Constants.Y2_TILE;
            }
            else
            {
                //Do nothing
                a.currentButtonType = "";
            }

            switch (e.getAction())
            {
                case MotionEvent.ACTION_DOWN:
                    Log.d(TAG, "Dropped: " + a.currentButtonType);

                    AlgeTilesTextView algeTilesIV = new AlgeTilesTextView(a);
                    Boolean wasImageDropped = false;

                    if (a.activityType.equals(Constants.MULTIPLY))
                    {
                        if (!a.isFirstAnswerCorrect &&
                                (a.currentButtonType.equals(Constants.X_TILE) ||
                                        a.currentButtonType.equals(Constants.Y_TILE) ||
                                        a.currentButtonType.equals(Constants.ONE_TILE)))
                        {
                            if (v.getId() == R.id.middleLeft)
                                algeTilesIV.setRotationY(180);
                            if (v.getId() == R.id.upperMiddle)
                                algeTilesIV.setRotationX(180);

                            wasImageDropped = true;
                            isDroppedAtCenter = true;
                        }
                        else if (a.isFirstAnswerCorrect)
                        {
                            wasImageDropped = true;
                        }
                    }
                    else
                    {
                        if (a.isFirstAnswerCorrect &&
                                (a.currentButtonType.equals(Constants.X_TILE) ||
                                        a.currentButtonType.equals(Constants.Y_TILE) ||
                                        a.currentButtonType.equals(Constants.ONE_TILE)))
                        {
                            if (v.getId() == R.id.middleLeft)
                                algeTilesIV.setRotationY(180);
                            if (v.getId() == R.id.upperMiddle)
                                algeTilesIV.setRotationX(180);
                            wasImageDropped = true;
                            isDroppedAtCenter = true;
                        }
                        else if (!a.isFirstAnswerCorrect)
                        {
                            wasImageDropped = true;
                        }
                    }

                    algeTilesIV.setTileType(a.currentButtonType);

                    if (wasImageDropped)
                    {
                        ViewGroup container = (ViewGroup) v;
                        Log.d(TAG, a.currentButtonType);
                        double heightFactor = 0;
                        double widthFactor = 0;
                        TileUtilities.TileFactor tF = TileUtilities.getTileFactors(a.currentButtonType);
                        algeTilesIV.setBackgroundResource(tF.id);
                        algeTilesIV.setText(tF.text);

                        heightFactor = tF.heightFactor;
                        widthFactor = tF.widthFactor;

                        if (!isDroppedAtCenter)
                        {
                            Rect r = TileUtilities.checkIfUserDropsOnRect(v.getId(), a.currentButtonType, x, y, Constants.ADD, a.rectTileListList);
                            if (null != r)
                            {
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
                                TileUtilities.checkWhichParentAndUpdate(v.getId(), a.currentButtonType, Constants.ADD, a.gridValueList);
                                a.hasButtonBeenDroppedInCorrectzone = true;
                            }
                        }
                        else
                        {
                            GridLayout.LayoutParams gParms = new GridLayout.LayoutParams();
                            if (v.getId() == R.id.middleLeft || v.getId() == R.id.middleRight) {
                                gParms.setGravity(Gravity.CENTER);
                                gParms.height = (int)(a.heightInPx / widthFactor);
                                gParms.width = (int)(a.heightInPx / heightFactor);
                            } else {
                                gParms.setGravity(Gravity.CENTER);
                                gParms.height = (int)(a.heightInPx / heightFactor);
                                gParms.width = (int)(a.heightInPx / widthFactor);
                            }

                            algeTilesIV.setLayoutParams(gParms);
                            algeTilesIV.setOnLongClickListener(clonedImageView_Touch);
                            container.addView(algeTilesIV);
                            TileUtilities.checkWhichParentAndUpdate(v.getId(), a.currentButtonType, Constants.ADD, a.gridValueList);

                            //Auto re-arrange of center tiles
                            ArrayList<AlgeTilesTextView> centerTileList = new ArrayList<>();
                            for (int i = 0; i < container.getChildCount(); ++i)
                            {
                                AlgeTilesTextView a = (AlgeTilesTextView)container.getChildAt(i);
                                centerTileList.add(a);
                            }
                            container.removeAllViews();

                            Collections.sort(centerTileList, new Comparator<AlgeTilesTextView>(){
                                public int compare(AlgeTilesTextView s1, AlgeTilesTextView s2) {
                                    return s2.getTileType().compareToIgnoreCase(s1.getTileType());
                                }
                            });

//                            List<AlgeTilesTextView> sortedList = centerTileList.OrderByDescending(o => o.getTileType()).ToList();
                            for (int i = 0; i < centerTileList.size(); ++i) {
                                container.addView(centerTileList.get(i));
                            }
                            //End of auto re-arrange
                        }
                        a.resetBGColors(vg);
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    a.resetBGColors(vg);
                    break;
                default:
                    break;
            }
            return true;
        }
    };

    public View.OnClickListener toggle_click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int buttonText = v.getId();
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(a);
            SharedPreferences.Editor editor = prefs.edit();

            switch (buttonText)
            {
                case R.id.remove:
                    editor.putBoolean(Constants.REMOVE, a.removeToggle.isChecked());
                    // editor.Commit();    // applies changes synchronously on older APIs
                    editor.apply();        // applies changes asynchronously on newer APIs

                    a.dragToggle.setChecked(a.dragToggle.isChecked() ? false : false);
                    a.rotateToggle.setChecked(a.rotateToggle.isChecked() ? false : false);

                    a.tile_1.setSelected(false);
                    a.oneTile_Clicked = false;

                    a.x_tile.setSelected(false);
                    a.xTile_Clicked = false;

                    a.x2_tile.setSelected(false);
                    a.x2Tile_Clicked = false;

                    a.xy_tile.setSelected(false);
                    a.xyTile_Clicked = false;

                    a.y_tile.setSelected(false);
                    a.yTile_Clicked = false;

                    a.y2_tile.setSelected(false);
                    a.y2Tile_Clicked = false;

                    break;
                case R.id.drag:
                    a.removeToggle.setChecked(a.removeToggle.isChecked() ? false : false);
                    if (a.rotateToggle.isChecked())
                    {
//                        a.findViewById(R.id.notRotatedButtonLayout).Visibility = ViewStates.Visible;
                    }
                    a.rotateToggle.setChecked(a.rotateToggle.isChecked() ? false : false);
                    break;
                case R.id.rotate:
                    //Also rotate original tiles
                    if (a.rotateToggle.isChecked())
                    {
//                        a.FindViewById<LinearLayout>(R.id.notRotatedButtonLayout).Visibility = ViewStates.Gone;
                    }
                    else
                    {
//                        a.FindViewById<LinearLayout>(R.id.notRotatedButtonLayout).Visibility = ViewStates.Visible;
                    }
                    a.removeToggle.setChecked(a.removeToggle.isChecked() ? false : false);
                    a.dragToggle.setChecked(a.dragToggle.isChecked() ? false : false);
                    break;
                case R.id.mute:
                    editor.putBoolean(Constants.MUTE, a.muteToggle.isChecked());
                    // editor.Commit();    // applies changes synchronously on older APIs
                    editor.apply();        // applies changes asynchronously on newer APIs
                    break;
                case R.id.tutorial:
                    //https://developer.android.com/training/animation/screen-slide.html
                    //https://www.bignerdranch.com/blog/viewpager-without-fragments/
                    //https://developer.xamarin.com/samples/monodroid/ActionBarViewPager/
                    //https://components.xamarin.com/gettingstarted/xamandroidsupportdesign
                    //http://stackoverflow.com/questions/7693633/android-image-dialog-popup
                    Intent intent = new Intent(a, TutorialActivity.class);
                    a.startActivity(intent);
                    break;
            }
        }
    };

    public View.OnClickListener tile_click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            AlgeTilesTextView imageViewTouch = (AlgeTilesTextView) v;
            if (imageViewTouch.isSelected()) {
                imageViewTouch.setSelected(false);
                a.oneTile_Clicked = false;
                a.xTile_Clicked = false;
                a.x2Tile_Clicked = false;
                a.xyTile_Clicked = false;
                a.yTile_Clicked = false;
                a.y2Tile_Clicked = false;
            }
            else
            {
                imageViewTouch.setSelected(true);
            }

            switch (imageViewTouch.getId())
            {
                case R.id.tile_1:
                    a.oneTile_Clicked = imageViewTouch.isSelected();

                    a.x_tile.setSelected(false);
                    a.xTile_Clicked = false;

                    a.x2_tile.setSelected(false);
                    a.x2Tile_Clicked = false;

                    a.xy_tile.setSelected(false);
                    a.xyTile_Clicked = false;

                    a.y_tile.setSelected(false);
                    a.yTile_Clicked = false;

                    a.y2_tile.setSelected(false);
                    a.y2Tile_Clicked = false;
                    break;
                case R.id.x_tile:
                    a.xTile_Clicked = imageViewTouch.isSelected();

                    a.tile_1.setSelected(false);
                    a.oneTile_Clicked = false;

                    a.x2_tile.setSelected(false);
                    a.x2Tile_Clicked = false;

                    a.xy_tile.setSelected(false);
                    a.xyTile_Clicked = false;

                    a.y_tile.setSelected(false);
                    a.yTile_Clicked = false;

                    a.y2_tile.setSelected(false);
                    a.y2Tile_Clicked = false;
                    break;
                case R.id.x2_tile:
                    a.x2Tile_Clicked = imageViewTouch.isSelected();

                    a.tile_1.setSelected(false);
                    a.oneTile_Clicked = false;

                    a.x_tile.setSelected(false);
                    a.xTile_Clicked = false;

                    a.xy_tile.setSelected(false);
                    a.xyTile_Clicked = false;

                    a.y_tile.setSelected(false);
                    a.yTile_Clicked = false;

                    a.y2_tile.setSelected(false);
                    a.y2Tile_Clicked = false;
                    break;
                case R.id.xy_tile:
                    a.xyTile_Clicked = imageViewTouch.isSelected();

                    a.x_tile.setSelected(false);
                    a.xTile_Clicked = false;

                    a.x2_tile.setSelected(false);
                    a.x2Tile_Clicked = false;

                    a.tile_1.setSelected(false);
                    a.oneTile_Clicked = false;

                    a.y_tile.setSelected(false);
                    a.yTile_Clicked = false;

                    a.y2_tile.setSelected(false);
                    a.y2Tile_Clicked = false;
                    break;
                case R.id.y_tile:
                    a.yTile_Clicked = imageViewTouch.isSelected();

                    a.tile_1.setSelected(false);
                    a.oneTile_Clicked = false;

                    a.x2_tile.setSelected(false);
                    a.x2Tile_Clicked = false;

                    a.xy_tile.setSelected(false);
                    a.xyTile_Clicked = false;

                    a.x_tile.setSelected(false);
                    a.xTile_Clicked = false;

                    a.y2_tile.setSelected(false);
                    a.y2Tile_Clicked = false;
                    break;
                case R.id.y2_tile:
                    a.y2Tile_Clicked = imageViewTouch.isSelected();

                    a.tile_1.setSelected(false);
                    a.oneTile_Clicked = false;

                    a.x_tile.setSelected(false);
                    a.xTile_Clicked = false;

                    a.xy_tile.setSelected(false);
                    a.xyTile_Clicked = false;

                    a.y_tile.setSelected(false);
                    a.yTile_Clicked = false;

                    a.x2_tile.setSelected(false);
                    a.x2Tile_Clicked = false;
                    break;
            }
            a.dragToggle.setChecked(false);
            a.removeToggle.setChecked(false);
        }
    };

    public View.OnTouchListener nullTouch = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            return false;
        }
    };

    public View.OnLongClickListener nullLongClick = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View view) {
            return false;
        }
    };

}
