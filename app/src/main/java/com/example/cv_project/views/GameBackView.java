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
import com.example.cv_project.utils.gamedata.HexPosition;
import com.example.cv_project.utils.gamedata.HexGlobalInfo;
import com.example.cv_project.utils.gamedata.LineInfo;
import com.example.cv_project.utils.gamedata.MissionInstance;

import java.util.ArrayList;

import javax.inject.Inject;

public class GameBackView extends FrameLayout {

    @Inject
    SizeStorage mSizeStorage;

    private MissionInstance mCurrentMissionInstance;
    private Paint mPaintTask;
    private Paint mPaintTable;
    private ArrayList<Float> mTargetLinesTops = new ArrayList<>();

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
        mPaintTask.setColor(Color.BLACK);
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

        for (HexGlobalInfo hexGlobalInfo : mSizeStorage.mTableHexInfoLHM.values()) {
            canvas.drawPath(SizeUtils.floatArrayToPath(hexGlobalInfo.mTops), mPaintTable);
        }

        for (int i = 0; i < mTargetLinesTops.size(); i += 4) {
            float fromX = mTargetLinesTops.get(i);
            float fromY = mTargetLinesTops.get(i + 1);
            float toX = mTargetLinesTops.get(i + 2);
            float toY = mTargetLinesTops.get(i + 3);
            canvas.drawLine(fromX, fromY, toX, toY, mPaintTask);
        }
    }

    public void loadMission(MissionInstance mission) {
        mCurrentMissionInstance = mission;
        for(LineInfo lineInfo : mCurrentMissionInstance.getMissionTargetLineList()){
            HexPosition lineFrom = lineInfo.first;
            HexPosition lineTo = lineInfo.second;

            HexGlobalInfo infoFrom = mSizeStorage.mTaskHexInfoLHM.get(lineFrom);
            HexGlobalInfo infoTo =  mSizeStorage.mTaskHexInfoLHM.get(lineTo);

            float fromX = infoFrom.mCenterX;
            float fromY = infoFrom.mCenterY;
            float toX = infoTo.mCenterX;
            float toY = infoTo.mCenterY;

            mTargetLinesTops.add(fromX);
            mTargetLinesTops.add(fromY);
            mTargetLinesTops.add(toX);
            mTargetLinesTops.add(toY);
        }
        invalidate();
    }
}
