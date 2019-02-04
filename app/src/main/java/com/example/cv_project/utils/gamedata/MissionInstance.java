package com.example.cv_project.utils.gamedata;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class MissionInstance {

    private HashMap<HexPosition, HexInfo> mMissionHexMap = new HashMap<>();
    private ArrayList<LineInfo> mMissionLineList = new ArrayList<>();

    public void initMission(ArrayList<HexInfo> hexInfoList) {
        HexInfo hexInfo0 = new HexInfo();
        hexInfo0.mStartHexPosition = new HexPosition(-1f, 2f);
        hexInfo0.mLastHexPosition = new HexPosition(-1f, 2f);
        hexInfo0.mLines.add(new HexPosition(0f, -2f));

        HexInfo hexInfo1 = new HexInfo();
        hexInfo1.mStartHexPosition = new HexPosition(1f, 2f);
        hexInfo1.mLastHexPosition = new HexPosition(1f, 2f);
        hexInfo0.mLines.add(new HexPosition(0f, -2f));

        HexInfo hexInfo2 = new HexInfo();
        hexInfo2.mStartHexPosition = new HexPosition(0f, -2f);
        hexInfo2.mLastHexPosition = new HexPosition(0f, -2f);
        hexInfo2.mLines.add(new HexPosition(-1f, 2f));
        hexInfo2.mLines.add(new HexPosition(1f, 2f));

        mMissionHexMap.put(hexInfo0.mStartHexPosition, hexInfo0);
        mMissionHexMap.put(hexInfo1.mStartHexPosition, hexInfo1);
        mMissionHexMap.put(hexInfo2.mStartHexPosition, hexInfo2);

        mMissionLineList = new ArrayList<>(getMissionLines());
    }

    public HashMap<HexPosition, HexInfo> getMissionHexMap() {
        return mMissionHexMap;
    }

    public HashSet<LineInfo> getMissionLines() {
        HashSet<LineInfo> lines = new HashSet<>();
        for (HexInfo hexInfo : mMissionHexMap.values()) {
            HexPosition lineFrom = hexInfo.mStartHexPosition;
            for (HexPosition lineTo : hexInfo.mLines) {
                LineInfo lineInfo = getLineInfoName(lineFrom, lineTo);
                lines.add(lineInfo);
            }
        }
        return lines;
    }

    public LineInfo getLineInfoName(HexPosition hexPositionFrom, HexPosition hexPositionTo) {
        float lineFromX = hexPositionFrom.first;
        float lineFromY = hexPositionFrom.second;
        float lineToX = hexPositionTo.first;
        float lineToY = hexPositionTo.second;

        if (lineFromY > lineToY) {
            return new LineInfo(hexPositionFrom, hexPositionTo);
        } else if (lineFromY == lineToY) {
            if (lineFromX < lineToX) {
                return new LineInfo(hexPositionFrom, hexPositionTo);
            } else {
                return new LineInfo(hexPositionTo, hexPositionFrom);
            }
        } else {
            return new LineInfo(hexPositionTo, hexPositionFrom);
        }
    }
}
