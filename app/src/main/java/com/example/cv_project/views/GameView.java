package com.example.cv_project.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.example.cv_project.base.BaseApp;
import com.example.cv_project.utils.HexInfo;
import com.example.cv_project.utils.SizeStorage;
import com.example.cv_project.utils.SizeUtils;

import javax.inject.Inject;

public class GameView extends FrameLayout {

    @Inject
    SizeStorage mSizeStorage;

    private Paint mPaintTask;
    private Paint mPaintTable;

    private GameViewTouchListener mGameViewTouchListener = new GameViewTouchListener();
    private HexView mSelectedHexView;

    public GameView(@NonNull Context context) {
        super(context);
        init();
    }

    public GameView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GameView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setWillNotDraw(false);
        BaseApp.getSizeComponent().injectGameView(this);
        initPaints();
        setListeners();
    }

    private void initPaints() {
        mPaintTask = new Paint();
        mPaintTask.setAntiAlias(true);
        mPaintTask.setColor(Color.GREEN);
        mPaintTask.setStrokeWidth(mSizeStorage.mTaskBackLineWidth);
        mPaintTask.setStyle(Paint.Style.STROKE);

        mPaintTable = new Paint();
        mPaintTable.setAntiAlias(true);
        mPaintTable.setColor(Color.RED);
        mPaintTable.setStrokeWidth(mSizeStorage.mTaskBackLineWidth);
        mPaintTable.setStyle(Paint.Style.STROKE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawPath(SizeUtils.floatArrayToPath(mSizeStorage.mTaskTops), mPaintTask);

        for (HexInfo hexInfo : mSizeStorage.mTableHexInfoHM.values()) {
            canvas.drawPath(SizeUtils.floatArrayToPath(hexInfo.mTops), mPaintTable);
        }
    }

    private void setListeners() {
        setOnTouchListener(mGameViewTouchListener);
    }

    public void addHexToTable(HexInfo hexInfo) {
        HexView hexView = new HexView(getContext());
        FrameLayout.LayoutParams hexViewLp = new FrameLayout.LayoutParams((int) mSizeStorage.mTableHexOutDia, (int) mSizeStorage.mTableHexOutDia);
        addView(hexView, hexViewLp);

        hexView.setOnTouchListener(new HexViewTouchListener());
    }

    private class GameViewTouchListener implements OnTouchListener {

        @Override
        public boolean onTouch(View view, MotionEvent event) {
            int action = event.getActionMasked();
            float fingerX = event.getX();
            float fingerY = event.getY();
            switch (action) {
                case MotionEvent.ACTION_DOWN:
                    break;
                case MotionEvent.ACTION_MOVE:
                    Log.d("MyLogs", "Finger x=" + fingerX + "; y=" + fingerY);
                    if(mSelectedHexView == null) return true;
                    FrameLayout.LayoutParams selectedHexViewLp = (FrameLayout.LayoutParams) mSelectedHexView.getLayoutParams();
                    if(selectedHexViewLp == null) return true;
                    selectedHexViewLp.setMargins((int) fingerX, (int) fingerY, 0, 0);
                    mSelectedHexView.setLayoutParams(selectedHexViewLp);
                    break;
                case MotionEvent.ACTION_CANCEL:
                    mSelectedHexView = null;
                    break;
                case MotionEvent.ACTION_UP:
                    mSelectedHexView = null;
                    break;
            }
            return true;
        }
    }

    private class HexViewTouchListener implements OnTouchListener {

        @Override
        public boolean onTouch(View view, MotionEvent event) {
            int action = event.getActionMasked();
            switch (action) {
                case MotionEvent.ACTION_DOWN:
                    mSelectedHexView = (HexView) view;
                    Log.d("MyLogs", "Hex ACTION_DOWN");
                    break;
                case MotionEvent.ACTION_CANCEL:
                case MotionEvent.ACTION_UP:
                    mSelectedHexView = null;
                    Log.d("MyLogs", "Hex ACTION_UP");
                    break;
            }
            return false;
        }
    }
}
