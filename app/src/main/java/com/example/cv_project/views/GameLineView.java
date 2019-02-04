package com.example.cv_project.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.example.cv_project.base.BaseApp;
import com.example.cv_project.utils.SizeStorage;

import javax.inject.Inject;

public class GameLineView extends View {

    @Inject
    SizeStorage mSizeStorage;

    float mFromX;
    float mFromY;
    float mToX;
    float mToY;

    private Paint mPaint;

    public GameLineView(Context context) {
        super(context);
        init();
    }

    public GameLineView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GameLineView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        BaseApp.getSizeComponent().injectGameLineView(this);
        setWillNotDraw(false);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.BLACK);
        mPaint.setStrokeWidth(mSizeStorage.mLineWidth);
    }

    public void drawLine(float fromX, float fromY, float toX, float toY) {
        mFromX = fromX;
        mFromY = fromY;
        mToX = toX;
        mToY = toY;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawLine(mFromX, mFromY, mToX, mToY, mPaint);
    }
}
