package com.example.cv_project;

import android.os.Bundle;

import com.example.cv_project.base.BaseActivity;
import com.example.cv_project.base.BaseApp;
import com.example.cv_project.utils.SizeStorage;
import com.example.cv_project.utils.gamedata.MissionInstance;
import com.example.cv_project.views.GameView;

import javax.inject.Inject;

public class MainActivity extends BaseActivity {

    @Inject
    SizeStorage mSizeStorage;

    private GameView mGameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BaseApp.getSizeComponent().injectMainActivity(this);
        mGameView = findViewById(R.id.game_view);

        MissionInstance missionInstance = new MissionInstance();
        missionInstance.initMission(null);
        mGameView.loadMission(missionInstance);
    }
}
