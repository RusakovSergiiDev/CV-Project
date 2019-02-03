package com.example.cv_project.utils.dagger;

import android.content.Context;

import com.example.cv_project.utils.SizeStorage;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class SizeModule {

    private Context mContext;

    public SizeModule(Context mContext) {
        this.mContext = mContext;
    }

    @Singleton
    @Provides
    SizeStorage getSizeStorageInstance() {
        return new SizeStorage(mContext);
    }
}
