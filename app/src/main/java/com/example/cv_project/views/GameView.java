package com.example.cv_project.views;

import android.animation.ValueAnimator;
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
import android.widget.FrameLayout;

import com.example.cv_project.base.BaseApp;
import com.example.cv_project.utils.gamedata.HexPosition;
import com.example.cv_project.utils.gamedata.HexInfo;
import com.example.cv_project.utils.SizeStorage;
import com.example.cv_project.utils.SizeUtils;
import com.example.cv_project.utils.gamedata.HexTableInfo;
import com.example.cv_project.utils.gamedata.LineInfo;
import com.example.cv_project.utils.gamedata.MissionInstance;

import java.util.HashMap;

import javax.inject.Inject;

public class GameView extends FrameLayout {

    @Inject
    SizeStorage mSizeStorage;

    private Paint mPaintTask;
    private Paint mPaintTable;

    private GameViewTouchListener mGameViewTouchListener = new GameViewTouchListener();
    private HexView mSelectedHexView;
    private HexView mSelectedHexViewCopy;

    private MissionInstance mCurrentMissionInstance = null;
    private HashMap<HexPosition, HexView> mHexViews = new HashMap<>();

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

    private void setListeners() {
        setOnTouchListener(mGameViewTouchListener);
    }

    public void loadMission(MissionInstance missionInstance) {
        mCurrentMissionInstance = missionInstance;
        for (HexPosition hexPosition : mCurrentMissionInstance.getMissionHexMap().keySet()) {
            HexInfo hexInfo = mCurrentMissionInstance.getMissionHexMap().get(hexPosition);
            addHexToTable(hexPosition, hexInfo);
        }
    }

    public void addHexToTable(HexPosition startPosition, HexInfo hexInfo) {
        HexView hexView = new HexView(getContext());
        hexView.mHexInfo = hexInfo;
        mHexViews.put(startPosition, hexView);
        //Set Size
        FrameLayout.LayoutParams hexViewLp = new FrameLayout.LayoutParams((int) mSizeStorage.mTableHexOutDia, (int) mSizeStorage.mTableHexOutDia);
        //Add on Table
        addView(hexView, hexViewLp);
        //Set Start Position
        hexView.setTranslationX(mSizeStorage.getHexTranslationXByPosition(startPosition));
        hexView.setTranslationY(mSizeStorage.getHexTranslationYByPosition(startPosition));
        //Set Touch Listener
        hexView.setOnTouchListener(new HexViewTouchListener());
    }

    public void releaseHex() {
        if (mSelectedHexView == null) return;
        HexPosition nearestHexPosition = mSizeStorage.getNearestHexPosition(mSelectedHexView.getTranslationX() + mSizeStorage.mTableHexOutRad, mSelectedHexView.getTranslationY() + mSizeStorage.mTableHexOutRad);
        if (nearestHexPosition != null) {
            if (mCurrentMissionInstance.getMissionHexMap().containsKey(nearestHexPosition)) {
                releaseHexAtLastPosition();
            } else {
                releaseHexAtNewPosition(nearestHexPosition);
            }
        } else {
            releaseHexAtLastPosition();
        }
        logCurrentMissionInstance();
    }

    private void releaseHexAtNewPosition(HexPosition newHexPosition) {
        HexPosition startHexPosition = mSelectedHexView.mHexInfo.mStartHexPosition;
        HexPosition lastHexPosition = mSelectedHexView.mHexInfo.mLastHexPosition;
        HexInfo hexInfo = mSelectedHexView.mHexInfo;

        mCurrentMissionInstance.getMissionHexMap().remove(lastHexPosition);
        mSelectedHexView = null;

        final HexView hexView = mHexViews.get(startHexPosition);
        hexView.mHexInfo.mLastHexPosition = newHexPosition;
        mCurrentMissionInstance.getMissionHexMap().put(newHexPosition, hexInfo);

        float currentTranslationX = hexView.getTranslationX();
        float currentTranslationY = hexView.getTranslationY();

        float newTranslationX = mSizeStorage.getHexTranslationXByPosition(newHexPosition);
        float newTranslationY = mSizeStorage.getHexTranslationYByPosition(newHexPosition);

        ValueAnimator animatorTranslationX = ValueAnimator.ofFloat(currentTranslationX, newTranslationX);
        animatorTranslationX.setDuration(150);
        animatorTranslationX.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                hexView.setTranslationX(value);
            }
        });

        ValueAnimator animatorTranslationY = ValueAnimator.ofFloat(currentTranslationY, newTranslationY);
        animatorTranslationY.setDuration(150);
        animatorTranslationY.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                hexView.setTranslationY(value);

            }
        });
        animatorTranslationX.start();
        animatorTranslationY.start();
    }

    private void releaseHexAtLastPosition() {
        HexView hexView = mHexViews.get(mSelectedHexView.mHexInfo.mStartHexPosition);
        mSelectedHexView = null;
        HexPosition lastHexPosition = hexView.mHexInfo.mLastHexPosition;
        hexView.setTranslationX(mSizeStorage.getHexTranslationXByPosition(lastHexPosition));
        hexView.setTranslationY(mSizeStorage.getHexTranslationYByPosition(lastHexPosition));
    }

    private void drawLines(){

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
                    if (mSelectedHexView == null) return true;
                    mSelectedHexView.setTranslationX((int) fingerX - mSizeStorage.mTableHexOutRad);
                    mSelectedHexView.setTranslationY((int) fingerY - mSizeStorage.mTableHexOutRad);
                    break;
                case MotionEvent.ACTION_CANCEL:
                    mSelectedHexView = null;
                    break;
                case MotionEvent.ACTION_UP:
                    releaseHex();
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
                    break;
                case MotionEvent.ACTION_CANCEL:
                    break;
                case MotionEvent.ACTION_UP:
                    mSelectedHexView = null;
                    break;
            }
            return false;
        }
    }

    private void logCurrentMissionInstance() {
        Log.d("MyLogs", "CurrentMissionInstance");
        for (HexPosition hexPosition : mCurrentMissionInstance.getMissionHexMap().keySet()) {
            Log.d("MyLogs", hexPosition.first + "," + hexPosition.second + " -> " + mCurrentMissionInstance.getMissionHexMap().get(hexPosition));
        }
        for(LineInfo lineInfo : mCurrentMissionInstance.getMissionLines()){
            Log.d("MyLogs", "Line -> " + lineInfo.first + " - " + lineInfo.second);
        }
    }
}
