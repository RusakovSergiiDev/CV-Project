package com.example.cv_project;

import android.os.Bundle;

import com.example.cv_project.base.BaseActivity;
import com.example.cv_project.base.BaseApp;
import com.example.cv_project.utils.GameUIManager;
import com.example.cv_project.utils.SizeStorage;
import com.example.cv_project.utils.gamedata.MissionInstance;
import com.example.cv_project.views.GameBackView;
import com.example.cv_project.views.GameLinesView;
import com.example.cv_project.views.GameTouchView;

import javax.inject.Inject;

public class MainActivity extends BaseActivity {

    @Inject
    SizeStorage mSizeStorage;

    private GameBackView mGameBackView;
    private GameLinesView mGameLinesView;
    private GameTouchView mGameTouchView;

    private GameUIManager mGameUIManager = new GameUIManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BaseApp.getSizeComponent().injectMainActivity(this);
        mGameBackView = findViewById(R.id.game_back_view);
        mGameLinesView = findViewById(R.id.game_lines_view);
        mGameTouchView = findViewById(R.id.game_view);
        mGameUIManager.registerViews(mGameBackView, mGameLinesView, mGameTouchView);

        MissionInstance missionInstance = new MissionInstance();
        missionInstance.initMission(null);
        mGameUIManager.loadMission(missionInstance);
    }
}
