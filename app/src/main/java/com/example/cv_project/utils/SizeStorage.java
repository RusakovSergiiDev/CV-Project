package com.example.cv_project.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Pair;
import android.view.Display;
import android.view.WindowManager;

import com.example.cv_project.R;
import com.example.cv_project.utils.gamedata.HexInfo;
import com.example.cv_project.utils.gamedata.HexPosition;
import com.example.cv_project.utils.gamedata.HexTableInfo;

import java.util.HashMap;

public class SizeStorage {

    public static int UNKNOWN_MAT_POSITION = 100;

    private Context mContext;

    //Screen
    public float mScreenWidth = 0;
    public float mScreenHeight = 0;
    //Header
    public float mHeaderTop = 0;
    public float mHeaderBottom = 0;
    public float mHeaderHeight = 0;
    //Task
    public float mTaskCenterX = 0;
    public float mTaskCenterY = 0;
    public float mTaskTop = 0;
    public float mTaskBottom = 0;
    public float mTaskWidth = 0;
    public float mTaskHeight = 0;
    public float mTaskBackHexRad = 0;
    public float mTaskBackLineWidth = 0;
    public float[] mTaskTops = new float[12];
    //Table
    public float mTableCenterX = 0;
    public float mTableCenterY = 0;
    public float mTableTop = 0;
    public float mTableBottom = 0;
    public float mTableWidth = 0;
    public float mTableHeight = 0;
    public float mTableHorizontalRelation = 0.9f;
    public float mTableHexOutDia = 0;
    public float mTableHexOutRad = 0;
    public float mTableHexVerticalInterval = 0;
    public float mTableBackLineWidth = 0;
    public HashMap<HexPosition, HexTableInfo> mTableHexInfoHM = new HashMap<>();
    //Line
    public float mLineWidth = 0;

    //Helper
    public int mMargin = 0;

    public SizeStorage(Context context) {
        this.mContext = context;
        mMargin = (int) mContext.getResources().getDimension(R.dimen.margin);
        getSizes();
    }

    private void getSizes() {
        setScreenSizes(mContext);
        setHeaderSizes();
        setTaskSizes();
        setTableSizes();
    }

    private void setScreenSizes(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        mScreenWidth = metrics.widthPixels;
        mScreenHeight = metrics.heightPixels;
    }

    private void setHeaderSizes() {
        mHeaderTop = mMargin;
        mHeaderHeight = (int) mContext.getResources().getDimension(R.dimen.header_btn_height);
        mHeaderBottom = mMargin + mHeaderHeight;
    }

    private void setTaskSizes() {
        mTaskTop = mHeaderBottom + mMargin;
        mTaskWidth = mScreenWidth;
        mTaskHeight = mScreenHeight / 4;
        mTaskBottom = mTaskTop + mTaskHeight;
        mTaskCenterX = mScreenWidth / 2;
        mTaskCenterY = mTaskTop + mTaskHeight / 2;
        mTaskBackHexRad = (float) ((mTaskHeight / 2) / Math.sin(Math.PI / 3));
        mTaskBackLineWidth = mContext.getResources().getDimension(R.dimen.task_back_line_width);
        mTaskTops[0] = mTaskCenterX - mTaskBackHexRad;
        mTaskTops[1] = mTaskCenterY;
        mTaskTops[2] = mTaskCenterX - mTaskBackHexRad / 2;
        mTaskTops[3] = mTaskCenterY - mTaskHeight / 2;
        mTaskTops[4] = mTaskCenterX + mTaskBackHexRad / 2;
        mTaskTops[5] = mTaskCenterY - mTaskHeight / 2;
        mTaskTops[6] = mTaskCenterX + mTaskBackHexRad;
        mTaskTops[7] = mTaskCenterY;
        mTaskTops[8] = mTaskCenterX + mTaskBackHexRad / 2;
        mTaskTops[9] = mTaskCenterY + mTaskHeight / 2;
        mTaskTops[10] = mTaskCenterX - mTaskBackHexRad / 2;
        mTaskTops[11] = mTaskCenterY + mTaskHeight / 2;
    }

    private void setTableSizes() {
        mTableTop = mTaskBottom + mMargin * 2;
        mTableBottom = mScreenHeight - mMargin * 2;
        mTableWidth = mScreenWidth;
        mTableHeight = mTableBottom - mTableTop;
        mTableCenterX = mScreenWidth / 2;
        mTableCenterY = mTableTop + mTableHeight / 2;
        mTableBackLineWidth = mContext.getResources().getDimension(R.dimen.task_back_line_width);

        int tableWorkWidth = (int) (mTableWidth * mTableHorizontalRelation);
        mTableHexOutRad = (float) tableWorkWidth / 10;
        mTableHexOutDia = mTableHexOutRad * 2;
        mTableHexVerticalInterval = (float) (Math.sqrt(3) / 2 * mTableHexOutDia);

        mTableHexInfoHM.put(new HexPosition(-1f, 2f), new HexTableInfo());
        mTableHexInfoHM.put(new HexPosition(0f, 2f), new HexTableInfo());
        mTableHexInfoHM.put(new HexPosition(1f, 2f), new HexTableInfo());

        mTableHexInfoHM.put(new HexPosition(-1.5f, 1f), new HexTableInfo());
        mTableHexInfoHM.put(new HexPosition(-0.5f, 1f), new HexTableInfo());
        mTableHexInfoHM.put(new HexPosition(0.5f, 1f), new HexTableInfo());
        mTableHexInfoHM.put(new HexPosition(1.5f, 1f), new HexTableInfo());

        mTableHexInfoHM.put(new HexPosition(-2f, 0f), new HexTableInfo());
        mTableHexInfoHM.put(new HexPosition(-1f, 0f), new HexTableInfo());
        mTableHexInfoHM.put(new HexPosition(0f, 0f), new HexTableInfo());
        mTableHexInfoHM.put(new HexPosition(1f, 0f), new HexTableInfo());
        mTableHexInfoHM.put(new HexPosition(2f, 0f), new HexTableInfo());

        mTableHexInfoHM.put(new HexPosition(-1.5f, -1f), new HexTableInfo());
        mTableHexInfoHM.put(new HexPosition(-0.5f, -1f), new HexTableInfo());
        mTableHexInfoHM.put(new HexPosition(0.5f, -1f), new HexTableInfo());
        mTableHexInfoHM.put(new HexPosition(1.5f, -1f), new HexTableInfo());

        mTableHexInfoHM.put(new HexPosition(-1f, -2f), new HexTableInfo());
        mTableHexInfoHM.put(new HexPosition(0f, -2f), new HexTableInfo());
        mTableHexInfoHM.put(new HexPosition(1f, -2f), new HexTableInfo());

        for (HexPosition hexTablePosition : mTableHexInfoHM.keySet()) {
            HexTableInfo hexTableInfo = mTableHexInfoHM.get(hexTablePosition);
            hexTableInfo.mOutDia = mTableHexOutDia;
            hexTableInfo.mOutRad = mTableHexOutRad;
            hexTableInfo.mCenterX = getHexCenterXByPosition(hexTablePosition);
            hexTableInfo.mCenterY = getHexCenterYByPosition(hexTablePosition);
            hexTableInfo.build();
        }

        mLineWidth = mTableHexOutRad / 2;
    }

    public float getHexCenterXByPosition(HexPosition position) {
        float x = position.first;
        return mTableCenterX + x * mTableHexOutDia;
    }

    public float getHexTranslationXByPosition(HexPosition position) {
        return getHexCenterXByPosition(position) - mTableHexOutRad;
    }

    public float getHexCenterYByPosition(HexPosition position) {
        float y = position.second;
        return mTableCenterY - y * mTableHexVerticalInterval;
    }

    public float getHexTranslationYByPosition(HexPosition position) {
        return getHexCenterYByPosition(position) - mTableHexOutRad;
    }

    public HexPosition getNearestHexPosition(float x, float y) {
        float minDelta = mTableHexOutDia * 2;
        HexPosition nearestHexPosition = null;
        for (HexPosition hexTablePosition : mTableHexInfoHM.keySet()) {
            HexTableInfo hexTableInfo = mTableHexInfoHM.get(hexTablePosition);
            float matPositionX = hexTableInfo.mCenterX;
            float matPositionY = hexTableInfo.mCenterY;
            float deltaX = Math.abs(Math.max(matPositionX, x) - Math.min(matPositionX, x));
            float deltaY = Math.abs(Math.max(matPositionY, y) - Math.min(matPositionY, y));
            float delta = (float) Math.sqrt(deltaX * deltaX + deltaY * deltaY);
            if (delta < minDelta) {
                minDelta = delta;
                nearestHexPosition = hexTablePosition;
            }
        }
        if (minDelta >= mTableHexOutRad) {
            nearestHexPosition = null;
        }
        return nearestHexPosition;
    }
}
