package com.freelance.jptalusan.algetiles.Utilities;

/**
 * Created by jptalusan on 10/1/17.
 */

public class GridValue {
    //Add type (x, x2, 1)
    //Add group?
    public int x2TileCount = 0;
    public int y2TileCount = 0;
    public int xyTileCount = 0;
    public int xTileCount = 0;
    public int yTileCount = 0;
    public int oneTileCount = 0;

    public GridValue(int x2Count, int y2Count, int xyCount,
                     int xCount, int yCount, int oneCount)
    {
        x2TileCount = x2Count;
        y2TileCount = y2Count;
        xyTileCount = xyCount;
        xTileCount = xCount;
        yTileCount = yCount;
        oneTileCount = oneCount;
    }

    public GridValue()
    {
        x2TileCount = 0;
        y2TileCount = 0;
        xyTileCount = 0;
        xTileCount = 0;
        yTileCount = 0;
        oneTileCount = 0;
    }

    public void init()
    {
        x2TileCount = 0;
        y2TileCount = 0;
        xyTileCount = 0;
        xTileCount = 0;
        yTileCount = 0;
        oneTileCount = 0;
    }

    public static GridValue subtract(GridValue g1, GridValue g2)
    {
        return new GridValue(g1.x2TileCount - g2.x2TileCount, g1.y2TileCount - g2.y2TileCount, g1.xyTileCount - g2.xyTileCount,
                g1.xTileCount - g2.xTileCount, g1.yTileCount - g2.yTileCount, g1.oneTileCount - g2.oneTileCount);
    }

    public static GridValue add(GridValue g1, GridValue g2)
    {
        return new GridValue(g1.x2TileCount + g2.x2TileCount, g1.y2TileCount + g2.y2TileCount, g1.xyTileCount + g2.xyTileCount,
                g1.xTileCount + g2.xTileCount, g1.yTileCount + g2.yTileCount, g1.oneTileCount + g2.oneTileCount);
    }

    @Override
    public String toString()
    {
        return (String.format("x2:{0}, y2:{1}, xy:{2}, x:{3}, y:{4}, one:{5}", x2TileCount, y2TileCount, xyTileCount, xTileCount, yTileCount, oneTileCount));
    }

    public int getCount()
    {
        return x2TileCount + y2TileCount + xyTileCount + xTileCount + yTileCount + oneTileCount;
    }
}
