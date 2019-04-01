package com.example.b00682737.maryportholidaypark.design;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class PageNavigator extends View {
    private static final int SPACING = 15;
    private static final int RADIUS = 15;
    private int mSize = 4;
    private int mPosition = 0;
    private static final Paint mOnPaint = new Paint(Paint.ANTI_ALIAS_FLAG);;
    private static final Paint mOffPaint = new Paint(Paint.ANTI_ALIAS_FLAG);;

    public PageNavigator(Context context) {
        super(context);
        mOnPaint.setColor(0xff2020fd);
        mOffPaint.setColor(0x302020fd);
    }

    public PageNavigator(Context c, int size) {
        this(c);
        mSize = size;
    }

    public PageNavigator(Context context, AttributeSet attrs) {
        super(context, attrs);
        mOnPaint.setColor(0xff2020fd);
        mOffPaint.setColor(0x302020fd);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        for (int i = 0; i < mSize; ++i) {
            if (i == mPosition) {
                canvas.drawCircle(i * (2 * RADIUS + SPACING) + RADIUS, RADIUS, RADIUS, mOnPaint);
            } else {
                canvas.drawCircle(i * (2 * RADIUS + SPACING) + RADIUS, RADIUS, RADIUS, mOffPaint);
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(mSize * (2 * RADIUS + SPACING) - SPACING, 2 * RADIUS);
    }


    public void setPosition(int id) {
        mPosition = id;
    }

    public int getPosition() {
        return mPosition;
    }

    public void setSize(int size) {
        mSize = size;
    }

    public void setPaints(int onColor, int offColor) {
        mOnPaint.setColor(onColor);
        mOffPaint.setColor(offColor);
    }

    public void setBlack() {
        setPaints(0xE6000000, 0x66000000);
    }

}

