package com.example.cv_project.utils;

import com.example.cv_project.utils.gamedata.HexInfo;
import com.example.cv_project.utils.gamedata.HexPosition;
import com.example.cv_project.utils.gamedata.LineInfo;
import com.example.cv_project.utils.gamedata.MissionInstance;
import com.example.cv_project.utils.gamedata.OnGameViewEventListener;
import com.example.cv_project.views.GameBackView;
import com.example.cv_project.views.GameLinesView;
import com.example.cv_project.views.GameTouchView;

import java.util.HashMap;
import java.util.HashSet;

public class GameProcessManager implements OnGameViewEventListener {

    private GameBackView mGameBackView;
    private GameLinesView mGameLinesView;
    private GameTouchView mGameTouchView;

    public void registerViews(GameBackView gameBackView, GameLinesView gameLinesView, GameTouchView gameTouchView) {
        mGameBackView = gameBackView;
        mGameLinesView = gameLinesView;
        mGameTouchView = gameTouchView;
        mGameTouchView.setListener(this);
    }

    public void loadMission(MissionInstance mission) {
        if (mGameBackView != null) mGameBackView.loadMission(mission);
        if (mGameLinesView != null) mGameLinesView.loadMission(mission);
        if (mGameTouchView != null) mGameTouchView.loadMission(mission);
    }

    @Override
    public void onRedrawLines(LineInfo lineInfo, float fromX, float fromY, float toX, float toY) {
        if (mGameLinesView != null) mGameLinesView.redrawLine(lineInfo, fromX, fromY, toX, toY);
    }

    @Override
    public void onStepFinished(MissionInstance mission) {
        HashMap<HexPosition, HexInfo> mCurrentHexesMap = mission.getMissionHexMap();
        HashSet<LineInfo> mCurrentLinesSet = new HashSet<>();
        for (HexPosition hexPosition : mCurrentHexesMap.keySet()) {
            HexInfo hexInfo = mCurrentHexesMap.get(hexPosition);
            for (HexPosition hexPositionTo : hexInfo.mLines) {
                LineInfo lineInfo = mission.getLineInfoName(hexPosition, hexPositionTo);
                mCurrentLinesSet.add(lineInfo);
            }
        }

        for(LineInfo lineInfo : mCurrentLinesSet){
            HexPosition hexIndexFrom = lineInfo.first;
            HexPosition hexIndexTo = lineInfo.second;
        }
    }
}
