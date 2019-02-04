package com.example.cv_project.utils.gamedata;

public interface OnGameViewEventListener {

    void onRedrawLines(LineInfo lineInfo, float fromX, float fromY, float toX, float toY);

    void onStepFinished(MissionInstance mission);
}
