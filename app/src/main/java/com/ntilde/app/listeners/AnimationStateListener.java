package com.ntilde.app.listeners;

import android.support.annotation.Nullable;

import com.github.jorgecastillo.FillableLoader;
import com.github.jorgecastillo.listener.OnStateChangeListener;
import com.ntilde.app.views.SplashView;

/**
 * Created by emanuel on 20/01/16.
 */
public class AnimationStateListener implements OnStateChangeListener {

    private SplashView activityView;
    private FillableLoader nextView;
    private int reactState;

    public AnimationStateListener(SplashView activityView, @Nullable  FillableLoader nextView, int reactState){
        this.nextView = nextView;
        this.reactState = reactState;
        this.activityView = activityView;
    }

    @Override
    public void onStateChange(int state) {
        if(reactState != state) return;

        if(nextView != null){
            nextView.reset();
            nextView.start();

        }else{
            activityView.animationFinished();
        }

    }
}
