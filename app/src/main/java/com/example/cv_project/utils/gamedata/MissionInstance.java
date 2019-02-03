package com.example.cv_project.utils.gamedata;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class MissionInstance {

    private HashMap<HexPosition, HexInfo> mMissionHexMap = new HashMap<>();

    public void initMission(ArrayList<HexInfo> hexInfoList) {
//        for (HexInfo hexInfo : hexInfoList) {
////            mMissionHexMap.put(new HexPosition(0f, 0f), hexInfo);
////        }

        HexInfo hexInfo0p0p = new HexInfo();
        hexInfo0p0p.mStartHexPosition = new HexPosition(0f, 0f);
        hexInfo0p0p.mLastHexPosition = new HexPosition(0f, 0f);
        hexInfo0p0p.mLines.add(new HexPosition(1f, 0f));
        hexInfo0p0p.mLines.add(new HexPosition(-2f, 0f));

        HexInfo hexInfo1p0p = new HexInfo();
        hexInfo1p0p.mStartHexPosition = new HexPosition(1f, 0f);
        hexInfo1p0p.mLastHexPosition = new HexPosition(1f, 0f);
        hexInfo1p0p.mLines.add(new HexPosition(0f, 0f));

        HexInfo hexInfo2m0p = new HexInfo();
        hexInfo2m0p.mStartHexPosition = new HexPosition(-2f, 0f);
        hexInfo2m0p.mLastHexPosition = new HexPosition(-2f, 0f);
        hexInfo2m0p.mLines.add(new HexPosition(0f, 0f));

        HexInfo hexInfo05p1p = new HexInfo();
        hexInfo05p1p.mStartHexPosition = new HexPosition(0.5f, 1f);
        hexInfo05p1p.mLastHexPosition = new HexPosition(0.5f, 1f);

        mMissionHexMap.put(hexInfo0p0p.mStartHexPosition, hexInfo0p0p);
        mMissionHexMap.put(hexInfo1p0p.mStartHexPosition, hexInfo1p0p);
        mMissionHexMap.put(hexInfo2m0p.mStartHexPosition, hexInfo2m0p);
        mMissionHexMap.put(hexInfo05p1p.mStartHexPosition, hexInfo05p1p);
    }

    public HashMap<HexPosition, HexInfo> getMissionHexMap() {
        return mMissionHexMap;
    }

    public HashSet<LineInfo> getMissionLines() {
        HashSet<LineInfo> lines = new HashSet<>();
        for (HexInfo hexInfo : mMissionHexMap.values()) {
            for (HexPosition line : hexInfo.mLines) {
                HexPosition lineFrom = hexInfo.mStartHexPosition;
                HexPosition lineTo = line;
                lines.add(new LineInfo(lineFrom, lineTo));
            }
        }
        return lines;
    }
}
