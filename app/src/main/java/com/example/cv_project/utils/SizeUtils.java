package com.example.cv_project.utils;

import android.content.Context;
import android.graphics.Path;

public class SizeUtils {

    public static float dpFromPx(final Context context, final float px) {
        return px / context.getResources().getDisplayMetrics().density;
    }

    public static float pxFromDp(final Context context, final float dp) {
        return dp * context.getResources().getDisplayMetrics().density;
    }

    public static Path floatArrayToPath(float[] tops) {
        Path path = new Path();
        path.moveTo(tops[0], tops[1]);
        for (int i = 0; i < tops.length; i += 2) {
            path.lineTo(tops[i], tops[i + 1]);
        }
        path.close();
        return path;
    }
}
