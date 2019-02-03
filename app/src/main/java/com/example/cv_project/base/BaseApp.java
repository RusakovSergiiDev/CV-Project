package com.example.cv_project.base;

import android.app.Application;

import com.example.cv_project.utils.dagger.DaggerSizeComponent;
import com.example.cv_project.utils.dagger.SizeComponent;
import com.example.cv_project.utils.dagger.SizeModule;

public class BaseApp extends Application {

    private static SizeComponent mSizeComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        mSizeComponent = DaggerSizeComponent.builder().sizeModule(new SizeModule(getApplicationContext())).build();
    }

    public static SizeComponent getSizeComponent() {
        return mSizeComponent;
    }
}
