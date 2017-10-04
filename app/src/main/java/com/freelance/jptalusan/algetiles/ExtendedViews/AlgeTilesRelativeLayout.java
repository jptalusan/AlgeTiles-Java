package com.freelance.jptalusan.algetiles.ExtendedViews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.freelance.jptalusan.algetiles.R;
import com.freelance.jptalusan.algetiles.Utilities.RectTile;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jptalusan on 10/1/17.
 */

public class AlgeTilesRelativeLayout extends RelativeLayout {
    private Paint paint;
    private boolean bDrawRects = false;
    private boolean update = false;
    private boolean bClearRects = false;
    private int height = 0;
    private int width = 0;
    private ArrayList<Rect> rList = new ArrayList<>();
    public int backGroundResource = R.drawable.shape;
    private DashPathEffect dashPathEffect = new DashPathEffect(new float[] { 10, 20 }, 0);

    public AlgeTilesRelativeLayout(Context context)
    {
        super(context);
        paint = new Paint();
    }

    public AlgeTilesRelativeLayout(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        paint = new Paint();
    }

    public AlgeTilesRelativeLayout(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        paint = new Paint();
    }

    public void drawRects(List<RectTile> rectTiles)
    {
        for (RectTile rectTile : rectTiles) {
            rList.add(rectTile.getRect());
        }

        bDrawRects = true;
        bClearRects = false;
    }

    public void clearRects(int height, int width)
    {
        bDrawRects = false;
        bClearRects = true;
        this.height = height;
        this.width = width;
        rList.clear();
    }

    public void resetColor()
    {
        setBackgroundResource(backGroundResource);
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        if (bDrawRects)
        {
            paint.setColor(Color.BLACK);
            paint.setStyle(Paint.Style.STROKE);
            paint.setPathEffect(dashPathEffect);
            for(Rect r : rList){
                canvas.drawRect(r, paint);
            }
        }

        if (bClearRects)
        {
            paint.setColor(Color.WHITE);
            paint.setStyle(Paint.Style.STROKE);
            canvas.drawRect(new Rect(0, 0, width, height), paint);
        }
    }
}
