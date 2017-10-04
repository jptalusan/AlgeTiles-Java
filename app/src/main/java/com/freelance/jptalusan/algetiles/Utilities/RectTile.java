package com.freelance.jptalusan.algetiles.Utilities;

import android.graphics.Rect;

/**
 * Created by jptalusan on 10/1/17.
 */

public class RectTile {
    private Rect r;
    private String _tileType;
    private int height;
    private int width;
    private boolean alreadyHasTile = false;

    public RectTile(Rect r, String tileType)
    {
        this.r = r;
        height = r.top - r.bottom;
        width = r.right - r.left;
        this._tileType = tileType;
    }

    public String getTileType()
    {
        return _tileType;
    }

    public int getHeight()
    {
        return height;
    }

    public int getWidth()
    {
        return width;
    }

    public Rect getRect()
    {
        return r;
    }

    public void setTilePresence(boolean b)
    {
        alreadyHasTile = b;
    }

    public boolean getTilePresence()
    {
        return alreadyHasTile;
    }

    public boolean isPointInsideRect(float x, float y)
    {
        //Console.WriteLine("isPointInsideRect: x: " + x + ",y: " + y);
        if (y > r.top && y < r.bottom && x > r.left && x < r.right)
            return true;
        return false;
    }

    public boolean isTileTypeSame(String tileType)
    {
        //Console.WriteLine("isTileTypeSame: " + tileType + " /curr: " + _tileType);
        if (this._tileType.equals(tileType) || tileType.equals(this._tileType))
            return true;
        return false;
    }
}
