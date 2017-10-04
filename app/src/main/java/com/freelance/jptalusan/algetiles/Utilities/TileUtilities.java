package com.freelance.jptalusan.algetiles.Utilities;

import android.graphics.Rect;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.RelativeSizeSpan;
import android.text.style.SuperscriptSpan;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.TextView;

import com.freelance.jptalusan.algetiles.ExtendedViews.AlgeTilesTextView;
import com.freelance.jptalusan.algetiles.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jptalusan on 10/1/17.
 */

public class TileUtilities {
    private static String TAG = "AlgeTiles:TileUtilities";

    public static String getTileTypeOfProduct(String tile1, String tile2)
    {
        String output = "";
        if (tile1.equals(Constants.X_TILE) &&
                tile2.equals(Constants.X_TILE))
            output = Constants.X2_TILE;

        if (tile1.equals(Constants.Y_TILE) &&
                tile2.equals(Constants.Y_TILE))
            output = Constants.Y2_TILE;

        if ((tile1.equals(Constants.X_TILE) &&
                tile2.equals(Constants.Y_TILE)) ||
                (tile1.equals(Constants.Y_TILE) &&
                        tile2.equals(Constants.X_TILE)))
            output = Constants.XY_TILE;

        if ((tile1.equals(Constants.X_TILE) &&
                tile2.equals(Constants.ONE_TILE)) ||
                (tile1.equals(Constants.ONE_TILE) &&
                        tile2.equals(Constants.X_TILE)))
            output = Constants.X_TILE;

        if ((tile1.equals(Constants.Y_TILE) &&
                tile2.equals(Constants.ONE_TILE)) ||
                (tile1.equals(Constants.ONE_TILE) &&
                        tile2.equals(Constants.Y_TILE)))
            output = Constants.Y_TILE;

        if (tile1.equals(Constants.ONE_TILE) &&
                tile2.equals(Constants.ONE_TILE))
            output = Constants.ONE_TILE;

        return output;
    }

    public static class TileFactor
    {
        public int id = 0;
        public SpannableStringBuilder text = null;
        public double heightFactor = 0;
        public double widthFactor = 0;

        public TileFactor()
        {

        }

        public TileFactor(int id, SpannableStringBuilder text, double heightFactor, double widthFactor)
        {
            this.id = id;
            this.text = text;
            this.heightFactor = heightFactor;
            this.widthFactor = widthFactor;
        }
    }

    public static TileFactor getTileFactors(String tileType)
    {
        TileFactor tF = new TileFactor();

        switch (tileType)
        {
            case Constants.ONE_TILE:
                tF.id = R.drawable.one_selector;

                tF.text = new SpannableStringBuilder("1");
                tF.heightFactor = Constants.ONE_SIDE;
                tF.widthFactor = Constants.ONE_SIDE;
                break;
            case Constants.X_TILE:
                tF.id = R.drawable.x_selector;

                tF.text = new SpannableStringBuilder("x");
                tF.heightFactor = Constants.X_LONG_SIDE;
                tF.widthFactor = Constants.ONE_SIDE;
                break;
            case Constants.X2_TILE:
                tF.id = R.drawable.x_selector;

                SpannableStringBuilder cs = new SpannableStringBuilder("x2");
                cs.setSpan(new SuperscriptSpan(), 1, 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                cs.setSpan(new RelativeSizeSpan(0.75f), 1, 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                tF.text = cs;
                tF.heightFactor = Constants.X_LONG_SIDE;
                tF.widthFactor = Constants.X_LONG_SIDE;
                break;
            case Constants.Y_TILE:
                tF.id = R.drawable.y_selector;
                tF.text = new SpannableStringBuilder("y");
                tF.heightFactor = Constants.Y_LONG_SIDE;
                tF.widthFactor = Constants.ONE_SIDE;
                break;
            case Constants.Y2_TILE:
                tF.id = R.drawable.y_selector;

                SpannableStringBuilder csy = new SpannableStringBuilder("y2");
                csy.setSpan(new SuperscriptSpan(), 1, 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                csy.setSpan(new RelativeSizeSpan(0.75f), 1, 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                tF.text = csy;
                tF.heightFactor = Constants.Y_LONG_SIDE;
                tF.widthFactor = Constants.Y_LONG_SIDE;
                break;
            case Constants.XY_TILE:
                tF.id = R.drawable.xy_selector;
                tF.text = new SpannableStringBuilder("xy");
                tF.heightFactor = Constants.Y_LONG_SIDE;
                tF.widthFactor = Constants.X_LONG_SIDE;
                break;
        }
        return tF;
    }

    public static int[] getDimensionsOfProduct(int height, String verticalTile, String horizontalTile)
    {
        int[] output = { 0, 0 };

        output[0] = (int) (height / getTileFactors(verticalTile).heightFactor);
        output[1] = (int) (height / getTileFactors(horizontalTile).heightFactor);

        return output;
    }

    public static void checkWhichParentAndUpdate(int id, String tile, int process, List<GridValue> gridValueList)
    {
        if (Constants.ADD == process)
        {
            //OUTER
            if (R.id.upperLeft == id)
            {
                if (Constants.X2_TILE == tile || Constants.X2_TILE_ROT == tile)
                    ++gridValueList.get(0).x2TileCount;
                if (Constants.Y2_TILE == tile || Constants.Y2_TILE_ROT == tile)
                    ++gridValueList.get(0).y2TileCount;
                if (Constants.XY_TILE == tile || Constants.XY_TILE_ROT == tile)
                    ++gridValueList.get(0).xyTileCount;
                if (Constants.X_TILE == tile || Constants.X_TILE_ROT == tile)
                    ++gridValueList.get(0).xTileCount;
                if (Constants.Y_TILE == tile || Constants.Y_TILE_ROT == tile)
                    ++gridValueList.get(0).yTileCount;
                if (Constants.ONE_TILE == tile || Constants.ONE_TILE_ROT == tile)
                    ++gridValueList.get(0).oneTileCount;
            }
            if (R.id.upperRight == id)
            {
                if (Constants.X2_TILE == tile || Constants.X2_TILE_ROT == tile)
                    ++gridValueList.get(1).x2TileCount;
                if (Constants.Y2_TILE == tile || Constants.Y2_TILE_ROT == tile)
                    ++gridValueList.get(1).y2TileCount;
                if (Constants.XY_TILE == tile || Constants.XY_TILE_ROT == tile)
                    ++gridValueList.get(1).xyTileCount;
                if (Constants.X_TILE == tile || Constants.X_TILE_ROT == tile)
                    ++gridValueList.get(1).xTileCount;
                if (Constants.Y_TILE == tile || Constants.Y_TILE_ROT == tile)
                    ++gridValueList.get(1).yTileCount;
                if (Constants.ONE_TILE == tile || Constants.ONE_TILE_ROT == tile)
                    ++gridValueList.get(1).oneTileCount;
            }
            if (R.id.lowerLeft == id)
            {
                if (Constants.X2_TILE == tile || Constants.X2_TILE_ROT == tile)
                    ++gridValueList.get(2).x2TileCount;
                if (Constants.Y2_TILE == tile || Constants.Y2_TILE_ROT == tile)
                    ++gridValueList.get(2).y2TileCount;
                if (Constants.XY_TILE == tile || Constants.XY_TILE_ROT == tile)
                    ++gridValueList.get(2).xyTileCount;
                if (Constants.X_TILE == tile || Constants.X_TILE_ROT == tile)
                    ++gridValueList.get(2).xTileCount;
                if (Constants.Y_TILE == tile || Constants.Y_TILE_ROT == tile)
                    ++gridValueList.get(2).yTileCount;
                if (Constants.ONE_TILE == tile || Constants.ONE_TILE_ROT == tile)
                    ++gridValueList.get(2).oneTileCount;
            }
            if (R.id.lowerRight == id)
            {
                if (Constants.X2_TILE == tile || Constants.X2_TILE_ROT == tile)
                    ++gridValueList.get(3).x2TileCount;
                if (Constants.Y2_TILE == tile || Constants.Y2_TILE_ROT == tile)
                    ++gridValueList.get(3).y2TileCount;
                if (Constants.XY_TILE == tile || Constants.XY_TILE_ROT == tile)
                    ++gridValueList.get(3).xyTileCount;
                if (Constants.X_TILE == tile || Constants.X_TILE_ROT == tile)
                    ++gridValueList.get(3).xTileCount;
                if (Constants.Y_TILE == tile || Constants.Y_TILE_ROT == tile)
                    ++gridValueList.get(3).yTileCount;
                if (Constants.ONE_TILE == tile || Constants.ONE_TILE_ROT == tile)
                    ++gridValueList.get(3).oneTileCount;
            }

            //CENTER
            if (R.id.upperMiddle == id)
            {
                if (Constants.X2_TILE == tile || Constants.X2_TILE_ROT == tile)
                    ++gridValueList.get(4).x2TileCount;
                if (Constants.Y2_TILE == tile || Constants.Y2_TILE_ROT == tile)
                    ++gridValueList.get(4).y2TileCount;
                if (Constants.XY_TILE == tile || Constants.XY_TILE_ROT == tile)
                    ++gridValueList.get(4).xyTileCount;
                if (Constants.X_TILE == tile || Constants.X_TILE_ROT == tile)
                    ++gridValueList.get(4).xTileCount;
                if (Constants.Y_TILE == tile || Constants.Y_TILE_ROT == tile)
                    ++gridValueList.get(4).yTileCount;
                if (Constants.ONE_TILE == tile || Constants.ONE_TILE_ROT == tile)
                    ++gridValueList.get(4).oneTileCount;
            }
            if (R.id.lowerMiddle == id)
            {
                if (Constants.X2_TILE == tile || Constants.X2_TILE_ROT == tile)
                    ++gridValueList.get(5).x2TileCount;
                if (Constants.Y2_TILE == tile || Constants.Y2_TILE_ROT == tile)
                    ++gridValueList.get(5).y2TileCount;
                if (Constants.XY_TILE == tile || Constants.XY_TILE_ROT == tile)
                    ++gridValueList.get(5).xyTileCount;
                if (Constants.X_TILE == tile || Constants.X_TILE_ROT == tile)
                    ++gridValueList.get(5).xTileCount;
                if (Constants.Y_TILE == tile || Constants.Y_TILE_ROT == tile)
                    ++gridValueList.get(5).yTileCount;
                if (Constants.ONE_TILE == tile || Constants.ONE_TILE_ROT == tile)
                    ++gridValueList.get(5).oneTileCount;
            }
            if (R.id.middleLeft == id)
            {
                if (Constants.X2_TILE == tile || Constants.X2_TILE_ROT == tile)
                    ++gridValueList.get(6).x2TileCount;
                if (Constants.Y2_TILE == tile || Constants.Y2_TILE_ROT == tile)
                    ++gridValueList.get(6).y2TileCount;
                if (Constants.XY_TILE == tile || Constants.XY_TILE_ROT == tile)
                    ++gridValueList.get(6).xyTileCount;
                if (Constants.X_TILE == tile || Constants.X_TILE_ROT == tile)
                    ++gridValueList.get(6).xTileCount;
                if (Constants.Y_TILE == tile || Constants.Y_TILE_ROT == tile)
                    ++gridValueList.get(6).yTileCount;
                if (Constants.ONE_TILE == tile || Constants.ONE_TILE_ROT == tile)
                    ++gridValueList.get(6).oneTileCount;
            }
            if (R.id.middleRight == id)
            {
                if (Constants.X2_TILE == tile || Constants.X2_TILE_ROT == tile)
                    ++gridValueList.get(7).x2TileCount;
                if (Constants.Y2_TILE == tile || Constants.Y2_TILE_ROT == tile)
                    ++gridValueList.get(7).y2TileCount;
                if (Constants.XY_TILE == tile || Constants.XY_TILE_ROT == tile)
                    ++gridValueList.get(7).xyTileCount;
                if (Constants.X_TILE == tile || Constants.X_TILE_ROT == tile)
                    ++gridValueList.get(7).xTileCount;
                if (Constants.Y_TILE == tile || Constants.Y_TILE_ROT == tile)
                    ++gridValueList.get(7).yTileCount;
                if (Constants.ONE_TILE == tile || Constants.ONE_TILE_ROT == tile)
                    ++gridValueList.get(7).oneTileCount;
            }
        }
        //REMOVE
        else if (Constants.SUBTRACT == process)
        {
            //OUTER
            if (R.id.upperLeft == id)
            {
                if (Constants.X2_TILE == tile || Constants.X2_TILE_ROT == tile)
                    --gridValueList.get(0).x2TileCount;
                if (Constants.Y2_TILE == tile || Constants.Y2_TILE_ROT == tile)
                    --gridValueList.get(0).y2TileCount;
                if (Constants.XY_TILE == tile || Constants.XY_TILE_ROT == tile)
                    --gridValueList.get(0).xyTileCount;
                if (Constants.X_TILE == tile || Constants.X_TILE_ROT == tile)
                    --gridValueList.get(0).xTileCount;
                if (Constants.Y_TILE == tile || Constants.Y_TILE_ROT == tile)
                    --gridValueList.get(0).yTileCount;
                if (Constants.ONE_TILE == tile || Constants.ONE_TILE_ROT == tile)
                    --gridValueList.get(0).oneTileCount;
            }
            if (R.id.upperRight == id)
            {
                if (Constants.X2_TILE == tile || Constants.X2_TILE_ROT == tile)
                    --gridValueList.get(1).x2TileCount;
                if (Constants.Y2_TILE == tile || Constants.Y2_TILE_ROT == tile)
                    --gridValueList.get(1).y2TileCount;
                if (Constants.XY_TILE == tile || Constants.XY_TILE_ROT == tile)
                    --gridValueList.get(1).xyTileCount;
                if (Constants.X_TILE == tile || Constants.X_TILE_ROT == tile)
                    --gridValueList.get(1).xTileCount;
                if (Constants.Y_TILE == tile || Constants.Y_TILE_ROT == tile)
                    --gridValueList.get(1).yTileCount;
                if (Constants.ONE_TILE == tile || Constants.ONE_TILE_ROT == tile)
                    --gridValueList.get(1).oneTileCount;
            }
            if (R.id.lowerLeft == id)
            {
                if (Constants.X2_TILE == tile || Constants.X2_TILE_ROT == tile)
                    --gridValueList.get(2).x2TileCount;
                if (Constants.Y2_TILE == tile || Constants.Y2_TILE_ROT == tile)
                    --gridValueList.get(2).y2TileCount;
                if (Constants.XY_TILE == tile || Constants.XY_TILE_ROT == tile)
                    --gridValueList.get(2).xyTileCount;
                if (Constants.X_TILE == tile || Constants.X_TILE_ROT == tile)
                    --gridValueList.get(2).xTileCount;
                if (Constants.Y_TILE == tile || Constants.Y_TILE_ROT == tile)
                    --gridValueList.get(2).yTileCount;
                if (Constants.ONE_TILE == tile || Constants.ONE_TILE_ROT == tile)
                    --gridValueList.get(2).oneTileCount;
            }
            if (R.id.lowerRight == id)
            {
                if (Constants.X2_TILE == tile || Constants.X2_TILE_ROT == tile)
                    --gridValueList.get(3).x2TileCount;
                if (Constants.Y2_TILE == tile || Constants.Y2_TILE_ROT == tile)
                    --gridValueList.get(3).y2TileCount;
                if (Constants.XY_TILE == tile || Constants.XY_TILE_ROT == tile)
                    --gridValueList.get(3).xyTileCount;
                if (Constants.X_TILE == tile || Constants.X_TILE_ROT == tile)
                    --gridValueList.get(3).xTileCount;
                if (Constants.Y_TILE == tile || Constants.Y_TILE_ROT == tile)
                    --gridValueList.get(3).yTileCount;
                if (Constants.ONE_TILE == tile || Constants.ONE_TILE_ROT == tile)
                    --gridValueList.get(3).oneTileCount;
            }

            //CENTER
            if (R.id.upperMiddle == id)
            {
                if (Constants.X2_TILE == tile || Constants.X2_TILE_ROT == tile)
                    --gridValueList.get(4).x2TileCount;
                if (Constants.Y2_TILE == tile || Constants.Y2_TILE_ROT == tile)
                    --gridValueList.get(4).y2TileCount;
                if (Constants.XY_TILE == tile || Constants.XY_TILE_ROT == tile)
                    --gridValueList.get(4).xyTileCount;
                if (Constants.X_TILE == tile || Constants.X_TILE_ROT == tile)
                    --gridValueList.get(4).xTileCount;
                if (Constants.Y_TILE == tile || Constants.Y_TILE_ROT == tile)
                    --gridValueList.get(4).yTileCount;
                if (Constants.ONE_TILE == tile || Constants.ONE_TILE_ROT == tile)
                    --gridValueList.get(4).oneTileCount;
            }
            if (R.id.lowerMiddle == id)
            {
                if (Constants.X2_TILE == tile || Constants.X2_TILE_ROT == tile)
                    --gridValueList.get(5).x2TileCount;
                if (Constants.Y2_TILE == tile || Constants.Y2_TILE_ROT == tile)
                    --gridValueList.get(5).y2TileCount;
                if (Constants.XY_TILE == tile || Constants.XY_TILE_ROT == tile)
                    --gridValueList.get(5).xyTileCount;
                if (Constants.X_TILE == tile || Constants.X_TILE_ROT == tile)
                    --gridValueList.get(5).xTileCount;
                if (Constants.Y_TILE == tile || Constants.Y_TILE_ROT == tile)
                    --gridValueList.get(5).yTileCount;
                if (Constants.ONE_TILE == tile || Constants.ONE_TILE_ROT == tile)
                    --gridValueList.get(5).oneTileCount;
            }
            if (R.id.middleLeft == id)
            {
                if (Constants.X2_TILE == tile || Constants.X2_TILE_ROT == tile)
                    --gridValueList.get(6).x2TileCount;
                if (Constants.Y2_TILE == tile || Constants.Y2_TILE_ROT == tile)
                    --gridValueList.get(6).y2TileCount;
                if (Constants.XY_TILE == tile || Constants.XY_TILE_ROT == tile)
                    --gridValueList.get(6).xyTileCount;
                if (Constants.X_TILE == tile || Constants.X_TILE_ROT == tile)
                    --gridValueList.get(6).xTileCount;
                if (Constants.Y_TILE == tile || Constants.Y_TILE_ROT == tile)
                    --gridValueList.get(6).yTileCount;
                if (Constants.ONE_TILE == tile || Constants.ONE_TILE_ROT == tile)
                    --gridValueList.get(6).oneTileCount;
            }
            if (R.id.middleRight == id)
            {
                if (Constants.X2_TILE == tile || Constants.X2_TILE_ROT == tile)
                    --gridValueList.get(7).x2TileCount;
                if (Constants.Y2_TILE == tile || Constants.Y2_TILE_ROT == tile)
                    --gridValueList.get(7).y2TileCount;
                if (Constants.XY_TILE == tile || Constants.XY_TILE_ROT == tile)
                    --gridValueList.get(7).xyTileCount;
                if (Constants.X_TILE == tile || Constants.X_TILE_ROT == tile)
                    --gridValueList.get(7).xTileCount;
                if (Constants.Y_TILE == tile || Constants.Y_TILE_ROT == tile)
                    --gridValueList.get(7).yTileCount;
                if (Constants.ONE_TILE == tile || Constants.ONE_TILE_ROT == tile)
                    --gridValueList.get(7).oneTileCount;
            }
        }
    }

    public static Rect checkIfUserDropsOnRect(int vId, String tileType,
                                              float x, float y, int command,
                                              List<List<RectTile>> rectTileListList)
    {
        if (R.id.upperRight == vId)
        {
            for (RectTile r : rectTileListList.get(0))
            {
                Log.d(TAG, "tiletype input: " + tileType + ", rect: " + r.getTileType());
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

        if (R.id.upperLeft == vId)
        {
            for (RectTile r : rectTileListList.get(1))
            {
                Log.d(TAG, "tiletype input: " + tileType + ", rect: " + r.getTileType());
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
            for (RectTile r : rectTileListList.get(2))
            {
                Log.d(TAG, "tiletype input: " + tileType + ", rect: " + r.getTileType());
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
            for (RectTile r : rectTileListList.get(3))
            {
                Log.d(TAG, "tiletype input: " + tileType + ", rect: " + r.getTileType());
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

    public static void generateInnerLayoutTileArrays(int heightInPx, int widthInPx,
                                                     List<ViewGroup> innerGridLayoutList,
                                                     List<List<RectTile>> rectTileListList)
    {
        Log.d(TAG, "generateInnerLayoutTileArrays");
        ArrayList<String> midUp = new ArrayList<>();
        ArrayList<String> midLeft = new ArrayList<>();
        ArrayList<String> midRight = new ArrayList<>();
        ArrayList<String> midDown = new ArrayList<>();

        ArrayList<ArrayList<String>> output = new ArrayList<>();
        output.add(midUp);
        output.add(midLeft);
        output.add(midRight);
        output.add(midDown);

        //midup, midleft, midright, middown
        for (int i = 0; i < innerGridLayoutList.size(); ++i)
        {
            Log.d(TAG, i + "");
            GridLayout gl = (GridLayout)innerGridLayoutList.get(i);
            for (int j = 0; j < gl.getChildCount(); ++j)
            {
                AlgeTilesTextView al = (AlgeTilesTextView) gl.getChildAt(j);
                Log.d(TAG, al.getTileType());
                output.get(i).add(al.getTileType());
            }
        }

        int height = heightInPx;
        int width = widthInPx;

        //upmid x midRight = quadrant1
        if (midUp.size() != 0 || midRight.size() != 0)
        {
            Log.d(TAG, "in upmid x midright");
            int top = height; //height of relative layout
            int bottom = height; //height of relative layout
            for (int i = 0; i < midUp.size(); ++i)
            {
                int left = 0;
                int right = 0;
                boolean firstPass = true;
                for (int j = 0; j < midRight.size(); ++j)
                {
                    int[] productDimensions = getDimensionsOfProduct(height, midUp.get(i), midRight.get(j));
                    if (firstPass)
                    {
                        top -= productDimensions[0];
                        bottom = top + productDimensions[0];
                        firstPass = false;
                    }
                    right += productDimensions[1];
                    left = right - productDimensions[1];

                    Rect r = new Rect(left, top, right, bottom);
                    rectTileListList.get(0).add(new RectTile(r, getTileTypeOfProduct(midUp.get(i), midRight.get(j))));
                }
            }
        }

        //upmid x midLeft = quadrant2
        if (midUp.size() != 0 || midLeft.size() != 0)
        {
            Log.d(TAG, "in upmid x midLeft");
            int top = height;
            int bottom = height;
            for (int i = 0; i < midUp.size(); ++i)
            {
                int left = width;
                int right = width;
                boolean firstPass = true;
                for (int j = 0; j < midLeft.size(); ++j)
                {
                    int[] productDimensions = getDimensionsOfProduct(height, midUp.get(i), midLeft.get(j));
                    if (firstPass)
                    {
                        top -= productDimensions[0];
                        bottom = top + productDimensions[0];
                        firstPass = false;
                    }
                    left -= productDimensions[1];
                    right = left + productDimensions[1];

                    Rect r = new Rect(left, top, right, bottom);
                    rectTileListList.get(1).add(new RectTile(r, getTileTypeOfProduct(midUp.get(i), midLeft.get(j))));
                }
            }
        }

        //loMid x midLeft = quadrant3
        if (midDown.size() != 0 || midLeft.size() != 0)
        {
            Log.d(TAG, "in loMid x midLeft");
            int top = 0;
            int bottom = 0;
            for (int i = 0; i < midDown.size(); ++i)
            {
                int left = width;
                int right = width;
                boolean firstPass = true;
                for (int j = 0; j < midLeft.size(); ++j)
                {
                    int[] productDimensions = getDimensionsOfProduct(height, midDown.get(i), midLeft.get(j));
                    if (firstPass)
                    {
                        bottom += productDimensions[0];
                        top = bottom - productDimensions[0];
                        firstPass = false;
                    }
                    left -= productDimensions[1];
                    right = left + productDimensions[1];

                    Rect r = new Rect(left, top, right, bottom);
                    rectTileListList.get(2).add(new RectTile(r, getTileTypeOfProduct(midDown.get(i), midLeft.get(j))));
                }
            }
        }

        //loMid x midRight = quadrant4
        if (midDown.size() != 0 || midRight.size() != 0)
        {
            Log.d(TAG, "in loMid x midRight");
            int top = 0;
            int bottom = 0;
            for (int i = 0; i < midDown.size(); ++i)
            {
                int left = 0;
                int right = 0;
                boolean firstPass = true;
                for (int j = 0; j < midRight.size(); ++j)
                {
                    int[] productDimensions = getDimensionsOfProduct(height, midDown.get(i), midRight.get(j));
                    if (firstPass)
                    {
                        bottom += productDimensions[0];
                        top = bottom - productDimensions[0];
                        firstPass = false;
                    }
                    right += productDimensions[1];
                    left = right - productDimensions[1];
                    Rect r = new Rect(left, top, right, bottom);
                    rectTileListList.get(3).add(new RectTile(r, getTileTypeOfProduct(midDown.get(i), midRight.get(j))));
                }
            }
        }
    }
}
