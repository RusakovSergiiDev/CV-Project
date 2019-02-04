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
import com.example.cv_project.utils.gamedata.OnGameViewEventListener;

import java.util.ArrayList;
import java.util.HashMap;

import javax.inject.Inject;

public class GameTouchView extends FrameLayout {

    @Inject
    SizeStorage mSizeStorage;

    private MissionInstance mCurrentMissionInstance = null;

    private HexView mSelectedHexView;
    private ValueAnimator mAnimatorTranslationX;
    private ValueAnimator mAnimatorTranslationY;
    private HashMap<HexPosition, HexView> mHexViews = new HashMap<>();

    private OnGameViewTouchListener mOnGameViewTouchListener = new OnGameViewTouchListener();
    private OnGameViewEventListener mOnGameViewEventListener;

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
        setOnTouchListener(mOnGameViewTouchListener);
    }

    public void setListener(OnGameViewEventListener listener) {
        mOnGameViewEventListener = listener;
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

        mAnimatorTranslationX = ValueAnimator.ofFloat(currentTranslationX, newTranslationX);
        mAnimatorTranslationX.setDuration(150);
        mAnimatorTranslationX.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                hexView.setTranslationX(value);
                redrawLines(startHexPosition);
                if (value == 150) {
                    mAnimatorTranslationY.cancel();
                    mAnimatorTranslationY = null;
                    stepFinished();
                }
                stepFinished();
            }
        });

        mAnimatorTranslationY = ValueAnimator.ofFloat(currentTranslationY, newTranslationY);
        mAnimatorTranslationY.setDuration(150);
        mAnimatorTranslationY.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                hexView.setTranslationY(value);
                redrawLines(startHexPosition);
                if (value == 150) {
                    mAnimatorTranslationX.cancel();
                    mAnimatorTranslationX = null;
                    stepFinished();
                }
            }
        });
        mAnimatorTranslationX.start();
        mAnimatorTranslationY.start();
    }

    private void releaseHexAtLastPosition() {
        HexView hexView = mHexViews.get(mSelectedHexView.mHexInfo.mStartHexPosition);
        mSelectedHexView = null;
        HexPosition lastHexPosition = hexView.mHexInfo.mLastHexPosition;
        hexView.setTranslationX(mSizeStorage.getHexTranslationXByPosition(lastHexPosition));
        hexView.setTranslationY(mSizeStorage.getHexTranslationYByPosition(lastHexPosition));

        redrawLines(hexView.mHexInfo.mStartHexPosition);
        stepFinished();
    }

    private void redrawLines(HexPosition hexPosition) {
        HexView hexViewFrom = mHexViews.get(hexPosition);
        float currentCenterX = hexViewFrom.getTranslationX() + mSizeStorage.mTableHexOutRad;
        float currentCenterY = hexViewFrom.getTranslationY() + mSizeStorage.mTableHexOutRad;

        ArrayList<HexPosition> lines = hexViewFrom.mHexInfo.mLines;
        for (HexPosition hexPositionTo : lines) {
            LineInfo lineInfo = mCurrentMissionInstance.getLineInfoName(hexPosition, hexPositionTo);
            HexView hexViewTo = mHexViews.get(hexPositionTo);
            float toX = mSizeStorage.getHexCenterXByPosition(hexViewTo.mHexInfo.mLastHexPosition);
            float toY = mSizeStorage.getHexCenterYByPosition(hexViewTo.mHexInfo.mLastHexPosition);
            mOnGameViewEventListener.onRedrawLines(lineInfo, currentCenterX, currentCenterY, toX, toY);
        }
    }

    private void stepFinished() {
        mOnGameViewEventListener.onStepFinished(mCurrentMissionInstance);
    }

    private class OnGameViewTouchListener implements OnTouchListener {

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
