package com.example.cv_project.utils;

import android.util.Log;

import com.example.cv_project.utils.gamedata.HexInfo;
import com.example.cv_project.utils.gamedata.HexPosition;
import com.example.cv_project.utils.gamedata.LineInfo;
import com.example.cv_project.utils.gamedata.MissionInstance;
import com.example.cv_project.utils.gamedata.OnGameViewEventListener;
import com.example.cv_project.views.GameBackView;
import com.example.cv_project.views.GameLinesView;
import com.example.cv_project.views.GameTouchView;

import java.util.ArrayList;
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
        ArrayList<LineInfo> mCurrentMiniLines = new ArrayList<>();
        for (LineInfo line : mission.getMissionLineList()) {
            HexPosition from = line.first;
            HexPosition to = line.second;
            HexPosition currentFrom = null;
            HexPosition currentTo = null;
            for (HexInfo hex : mCurrentHexesMap.values()) {
                if (hex.mStartHexPosition.equals(from)) {
                    currentFrom = hex.mLastHexPosition;
                }
                if (hex.mStartHexPosition.equals(to)) {
                    currentTo = hex.mLastHexPosition;
                }
            }
            LineInfo lineInfo = mission.getLineInfoName(currentFrom, currentTo);
            mCurrentLinesSet.add(lineInfo);
        }
        mCurrentMiniLines = separateLines(new ArrayList<>(mCurrentLinesSet));

        Log.d("myLogs", "mCurrentLinesSet()");
        for (LineInfo lineInfo : mCurrentMiniLines) {
            Log.d("myLogs", lineInfo.toString());
        }
    }

    public ArrayList<LineInfo> separateLines(ArrayList<LineInfo> lines) {
        ArrayList<LineInfo> result = new ArrayList<>();
        for (LineInfo line : lines) {

        }
        return result;
    }

    private ArrayList<LineInfo> separateLine(LineInfo line) {
        ArrayList<LineInfo> result = new ArrayList<>();
        return result;
    }

    private ArrayList<LineInfo> separateVerticalLine(LineInfo line) {
        ArrayList<LineInfo> result = new ArrayList<>();
        return result;
    }

    private ArrayList<LineInfo> separateHorizontalLine(LineInfo line) {
        ArrayList<LineInfo> result = new ArrayList<>();
        float fromX = Math.min(line.first.first, line.second.first);
        float toX = Math.max(line.first.first, line.second.first);
        float Y = line.first.second;
        for (float i = fromX; i <= toX; i += 0.5) {

        }
        return result;
    }
}
