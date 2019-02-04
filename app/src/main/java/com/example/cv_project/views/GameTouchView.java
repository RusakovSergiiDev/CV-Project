package com.example.cv_project.views;

import android.animation.ValueAnimator;
import android.content.Context;
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
import com.example.cv_project.utils.gamedata.LineInfo;
import com.example.cv_project.utils.gamedata.MissionInstance;
import com.example.cv_project.utils.gamedata.OnGameTouchViewListener;

import java.util.ArrayList;
import java.util.HashMap;

import javax.inject.Inject;

public class GameTouchView extends FrameLayout {

    @Inject
    SizeStorage mSizeStorage;

    private MissionInstance mCurrentMissionInstance = null;
    private GameViewTouchListener mGameViewTouchListener = new GameViewTouchListener();

    private HexView mSelectedHexView;
    private HashMap<HexPosition, HexView> mHexViews = new HashMap<>();

    private OnGameTouchViewListener mOnGameTouchViewListener;

    public GameTouchView(@NonNull Context context) {
        super(context);
        init();
    }

    public GameTouchView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GameTouchView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        BaseApp.getSizeComponent().injectGameView(this);
        setOnTouchListener(mGameViewTouchListener);
    }

    public void setListener(OnGameTouchViewListener listener) {
        mOnGameTouchViewListener = listener;
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
        float selectedHexCenterX = mSelectedHexView.getTranslationX() + mSizeStorage.mTableHexOutRad;
        float selectedHexCenterY = mSelectedHexView.getTranslationY() + mSizeStorage.mTableHexOutRad;
        HexPosition nearestHexPosition = mSizeStorage.getNearestHexPosition(selectedHexCenterX, selectedHexCenterY);
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
        final HexPosition startHexPosition = mSelectedHexView.mHexInfo.mStartHexPosition;
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
                redrawLines(startHexPosition);
            }
        });

        ValueAnimator animatorTranslationY = ValueAnimator.ofFloat(currentTranslationY, newTranslationY);
        animatorTranslationY.setDuration(150);
        animatorTranslationY.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                hexView.setTranslationY(value);
                redrawLines(startHexPosition);
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

        redrawLines(hexView.mHexInfo.mStartHexPosition);
    }

    private void redrawLines(HexPosition hexPosition) {
        HexView hexViewFrom = mHexViews.get(hexPosition);
        float currentCenterX = hexViewFrom.getTranslationX() + mSizeStorage.mTableHexOutRad;
        float currentCenterY = hexViewFrom.getTranslationY() + mSizeStorage.mTableHexOutRad;

        ArrayList<HexPosition> lines = hexViewFrom.mHexInfo.mLines;
        for (HexPosition hexPositionTo : lines) {
            HexView hexViewTo = mHexViews.get(hexPositionTo);
            float toX = mSizeStorage.getHexCenterXByPosition(hexViewTo.mHexInfo.mLastHexPosition);
            float toY = mSizeStorage.getHexCenterYByPosition(hexViewTo.mHexInfo.mLastHexPosition);
            for (LineInfo lineInfo : mCurrentMissionInstance.getMissionLines()) {
                if ((lineInfo.first.equals(hexPosition) && lineInfo.second.equals(hexPositionTo)) ||
                        (lineInfo.second.equals(hexPosition) && lineInfo.first.equals(hexPositionTo))) {
                    mOnGameTouchViewListener.onRedrawLines(lineInfo, currentCenterX, currentCenterY, toX, toY);
                }
            }
        }

        for (LineInfo lineInfo : mCurrentMissionInstance.getMissionLines()) {
            if (lineInfo.first.equals(hexPosition)) {
                HexInfo hexInfoSecond = mHexViews.get(lineInfo.second).mHexInfo;
                HexPosition hexPositionSecond = hexInfoSecond.mLastHexPosition;
                float fromX = currentCenterX;
                float fromY = currentCenterY;
                float toX = mSizeStorage.getHexCenterXByPosition(hexPositionSecond);
                float toY = mSizeStorage.getHexCenterYByPosition(hexPositionSecond);
                if (mOnGameTouchViewListener != null)
                    mOnGameTouchViewListener.onRedrawLines(lineInfo, fromX, fromY, toX, toY);
            } else if (lineInfo.second.equals(hexPosition)) {
                HexInfo hexInfoFirst = mHexViews.get(lineInfo.first).mHexInfo;
                HexPosition hexPositionFirst = hexInfoFirst.mLastHexPosition;
                float fromX = mSizeStorage.getHexCenterXByPosition(hexPositionFirst);
                float fromY = mSizeStorage.getHexCenterYByPosition(hexPositionFirst);
                float toX = currentCenterX;
                float toY = currentCenterY;
                if (mOnGameTouchViewListener != null)
                    mOnGameTouchViewListener.onRedrawLines(lineInfo, fromX, fromY, toX, toY);
            }
        }
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
                    redrawLines(mSelectedHexView.mHexInfo.mStartHexPosition);
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
    }
}
