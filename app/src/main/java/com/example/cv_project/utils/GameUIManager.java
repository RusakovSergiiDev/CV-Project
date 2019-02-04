package com.example.cv_project.utils;

import com.example.cv_project.utils.gamedata.LineInfo;
import com.example.cv_project.utils.gamedata.MissionInstance;
import com.example.cv_project.utils.gamedata.OnGameTouchViewListener;
import com.example.cv_project.views.GameBackView;
import com.example.cv_project.views.GameLinesView;
import com.example.cv_project.views.GameTouchView;

public class GameUIManager implements OnGameTouchViewListener {

    private GameBackView mGameBackView;
    private GameLinesView mGameLinesView;
    private GameTouchView mGameTouchView;

    public void registerViews(GameBackView gameBackView, GameLinesView gameLinesView, GameTouchView gameTouchView){
        mGameBackView = gameBackView;
        mGameLinesView = gameLinesView;
        mGameTouchView = gameTouchView;
        mGameTouchView.setListener(this);
    }

    public void loadMission(MissionInstance mission){
        if(mGameBackView!= null) mGameBackView.loadMission(mission);
        if(mGameLinesView!= null) mGameLinesView.loadMission(mission);
        if(mGameTouchView!= null) mGameTouchView.loadMission(mission);
    }

    @Override
    public void onRedrawLines(LineInfo lineInfo, float fromX, float fromY, float toX, float toY) {
        if(mGameLinesView!= null) mGameLinesView.redrawLine(lineInfo, fromX, fromY, toX, toY);
    }
}
