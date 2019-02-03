package com.example.cv_project.utils;

public class HexInfo {

    public float mCenterX = 0;
    public float mCenterY = 0;
    public float mOutRad = 0;
    public float mOutDia = 0;
    public float mInnRad = 0;
    public float mInnDia = 0;
    public float[] mTops = new float[12];

    public HexInfo() {
    }

    public HexInfo build() {
        mInnRad = (float) (Math.sqrt(3) / 2 * mOutRad);
        mInnDia = mInnRad * 2;
        mTops[0] = mCenterX;
        mTops[1] = mCenterY - mOutRad;
        mTops[2] = mCenterX + mInnRad;
        mTops[3] = mCenterY - mInnRad / 2;
        mTops[4] = mCenterX + mInnRad;
        mTops[5] = mCenterY + mInnRad / 2;
        mTops[6] = mCenterX;
        mTops[7] = mCenterY + mOutRad;
        mTops[8] = mCenterX - mInnRad;
        mTops[9] = mCenterY + mInnRad / 2;
        mTops[10] = mCenterX - mInnRad;
        mTops[11] = mCenterY - mInnRad / 2;
        return this;
    }
}
