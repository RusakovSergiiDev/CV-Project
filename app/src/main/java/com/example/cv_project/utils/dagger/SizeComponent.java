package com.example.cv_project.utils.dagger;

import com.example.cv_project.MainActivity;
import com.example.cv_project.views.GameBackView;
import com.example.cv_project.views.GameLineView;
import com.example.cv_project.views.GameLinesView;
import com.example.cv_project.views.GameTouchView;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = SizeModule.class)
public interface SizeComponent {

    void injectMainActivity(MainActivity mainActivity);

    void injectGameView(GameTouchView gameTouchView);

    void injectGameBackView(GameBackView gameBackView);

    void injectGameLinesView(GameLinesView gameLinesView);

    void injectGameLineView(GameLineView gameLineView);
}
