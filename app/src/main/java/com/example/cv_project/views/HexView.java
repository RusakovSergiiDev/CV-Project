package com.example.cv_project.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.example.cv_project.utils.SizeUtils;

public class HexView extends View {

    private Paint mPaint;
    float[] mTops = new float[12];

    public HexView(Context context) {
        super(context);
        init();
    }

    public HexView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public HexView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float centerX = (float) getWidth() / 2;
        float centerY = (float) getHeight() / 2;
        float outRad = (float) getHeight() / 2;
        float innRad = (float) (Math.sqrt(3) / 2 * outRad);

        mTops[0] = centerX;
        mTops[1] = centerY - outRad;
        mTops[2] = centerX + innRad;
        mTops[3] = centerY - innRad / 2;
        mTops[4] = centerX + innRad;
        mTops[5] = centerY + innRad / 2;
        mTops[6] = centerX;
        mTops[7] = centerY + outRad;
        mTops[8] = centerX - innRad;
        mTops[9] = centerY + innRad / 2;
        mTops[10] = centerX - innRad;
        mTops[11] = centerY - innRad / 2;

        canvas.drawPath(SizeUtils.floatArrayToPath(mTops), mPaint);
    }
}
