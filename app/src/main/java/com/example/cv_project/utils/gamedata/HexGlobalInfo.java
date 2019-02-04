package com.example.cv_project.utils.gamedata;

public class HexGlobalInfo extends HexInfo{

    public float mCenterX = 0;
    public float mCenterY = 0;
    public float mBorderLeft = 0;
    public float mBorderTop = 0;
    public float mBorderRight = 0;
    public float mBorderBottom = 0;
    public float[] mTops = new float[12];

    public HexInfo build() {
        mInnRad = (float) (Math.sqrt(3) / 2 * mOutRad);
        mInnDia = mInnRad * 2;

        mBorderLeft = mCenterX - mOutRad;
        mBorderTop = mCenterY - mOutRad;
        mBorderRight = mCenterX + mOutRad;
        mBorderBottom = mCenterY + mOutRad;

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
