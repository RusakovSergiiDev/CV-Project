package com.example.cv_project.utils.dagger;

import com.example.cv_project.MainActivity;
import com.example.cv_project.views.GameView;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = SizeModule.class)
public interface SizeComponent {

    void injectMainActivity(MainActivity mainActivity);

    void injectGameView(GameView gameView);
}
