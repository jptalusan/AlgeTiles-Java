package com.freelance.jptalusan.algetiles.ExtendedViews;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.TextView;

import com.freelance.jptalusan.algetiles.R;

/**
 * Created by jptalusan on 10/1/17.
 */

public class AlgeTilesTextView extends AppCompatTextView {
    private String tileType;
    private int height;
    private int width;

    public AlgeTilesTextView(Context context)
    {
        super(context);
        this.setTextAlignment(TEXT_ALIGNMENT_CENTER);
        this.setGravity(Gravity.CENTER);
        this.setTextColor(Color.BLACK);
        //this.SetTextSize(ComplexUnitType.Px, height);
    }
    public AlgeTilesTextView(Context context, AttributeSet attrs)
    {
        super(context,attrs);
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.AlgeTilesTextView, 0, 0);

        this.tileType = a.getString(R.styleable.AlgeTilesTextView_tileType);
        this.setTextAlignment(TEXT_ALIGNMENT_CENTER);
        this.setGravity(Gravity.CENTER);
        this.setTextColor(Color.BLACK);
        //this.SetTextSize(ComplexUnitType.Px, height);
        a.recycle();
    }

    public AlgeTilesTextView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
    }

    public void setTileType(String tileType)
    {
        this.tileType = tileType;
    }

    public String getTileType()
    {
        return tileType;
    }

    public void setDimensions(int height, int width)
    {
        this.height = height;
        this.width = width;
    }
}
