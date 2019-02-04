package com.example.cv_project.views;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.example.cv_project.base.BaseApp;
import com.example.cv_project.utils.SizeStorage;
import com.example.cv_project.utils.gamedata.LineInfo;
import com.example.cv_project.utils.gamedata.MissionInstance;

import java.util.HashMap;

import javax.inject.Inject;

public class GameLinesView extends FrameLayout {

    @Inject
    SizeStorage mSizeStorage;

    private MissionInstance mCurrentMissionInstance;
    private HashMap<LineInfo, GameLineView> mGameLineViewList = new HashMap<>();

    public GameLinesView(@NonNull Context context) {
        super(context);
        init();
    }

    public GameLinesView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GameLinesView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        BaseApp.getSizeComponent().injectGameLinesView(this);
    }

    public void loadMission(MissionInstance mission) {
        mCurrentMissionInstance = mission;
        for (LineInfo lineInfo : mCurrentMissionInstance.getMissionLines()) {
            addLineView(lineInfo);
        }
    }

    private void addLineView(LineInfo lineInfo) {
        GameLineView gameLineView = new GameLineView(getContext());
        mGameLineViewList.put(lineInfo, gameLineView);

        FrameLayout.LayoutParams gameLineViewLp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        addView(gameLineView, gameLineViewLp);

        float fromX = mSizeStorage.getHexCenterXByPosition(lineInfo.first);
        float fromY = mSizeStorage.getHexCenterYByPosition(lineInfo.first);
        float toX = mSizeStorage.getHexCenterXByPosition(lineInfo.second);
        float toY = mSizeStorage.getHexCenterYByPosition(lineInfo.second);

        gameLineView.drawLine(fromX, fromY, toX, toY);
    }

    public void redrawLine(LineInfo lineInfo, float fromX, float fromY, float toX, float toY) {
        GameLineView gameLineView = mGameLineViewList.get(lineInfo);
        if (gameLineView != null) gameLineView.drawLine(fromX, fromY, toX, toY);
    }
}
