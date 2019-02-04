package com.example.cv_project.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.example.cv_project.base.BaseApp;
import com.example.cv_project.utils.SizeStorage;
import com.example.cv_project.utils.SizeUtils;
import com.example.cv_project.utils.gamedata.HexTableInfo;

import javax.inject.Inject;

public class GameBackView extends FrameLayout {

    @Inject
    SizeStorage mSizeStorage;

    private Paint mPaintTask;
    private Paint mPaintTable;

    public GameBackView(@NonNull Context context) {
        super(context);
        init();
    }

    public GameBackView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GameBackView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setWillNotDraw(false);
        BaseApp.getSizeComponent().injectGameBackView(this);
        initPaints();
    }

    private void initPaints() {
        mPaintTask = new Paint();
        mPaintTask.setAntiAlias(true);
        mPaintTask.setColor(Color.GREEN);
        mPaintTask.setStrokeWidth(mSizeStorage.mTaskBackLineWidth);
        mPaintTask.setStyle(Paint.Style.STROKE);

        mPaintTable = new Paint();
        mPaintTable.setAntiAlias(true);
        mPaintTable.setColor(Color.BLACK);
        mPaintTable.setStrokeWidth(mSizeStorage.mTaskBackLineWidth);
        mPaintTable.setStyle(Paint.Style.STROKE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawPath(SizeUtils.floatArrayToPath(mSizeStorage.mTaskTops), mPaintTask);

        for (HexTableInfo hexTableInfo : mSizeStorage.mTableHexInfoHM.values()) {
            canvas.drawPath(SizeUtils.floatArrayToPath(hexTableInfo.mTops), mPaintTable);
        }
    }
}
