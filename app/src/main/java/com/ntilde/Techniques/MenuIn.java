package com.ntilde.Techniques;


import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.view.animation.OvershootInterpolator;

import com.daimajia.androidanimations.library.BaseViewAnimator;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

/**
 * Created by Developer001 on 21/03/2015.
 */
public class MenuIn extends BaseViewAnimator{

    private int delay=0;
    private int pivotX=0;
    private int pivotY=0;
    private int windowWidth=0;
    private int rotation=0;
    private int leftMargin=0;
    private int duration=1000;

    @Override
    public void prepare(View target){
        target.setPivotX(pivotX);
        target.setPivotY(pivotY);
        double vueltas=(windowWidth-leftMargin)/(Math.PI*pivotX*2);
        rotation=(int)(360f*vueltas);
        duration=(int)(vueltas*1000);
        target.setAlpha(0);
        View info=((ViewGroup)target.getParent()).getChildAt(1);
        info.setAlpha(0);

        Interpolator interpolator=new OvershootInterpolator(1f);

        ObjectAnimator oa1=ObjectAnimator.ofFloat(target, "rotation", rotation, 0);
        ObjectAnimator oa2=ObjectAnimator.ofFloat(target, "alpha", 0, 1);
        ObjectAnimator oa3=ObjectAnimator.ofFloat(target, "translationX",windowWidth,leftMargin);

        android.animation.AnimatorSet aaa=new android.animation.AnimatorSet();
        aaa.setStartDelay(delay);
        aaa.setDuration(duration);
        aaa.setInterpolator(interpolator);
        aaa.playTogether(oa1, oa2, oa3);
        aaa.addListener(new MenuInListener(info));
        aaa.start();
    }

    public void setPivot(int px, int py){
        pivotX=px;
        pivotY=py;
    }

    public void setDelay(int d){
        delay=d;
    }

    public void setWindowWidth(int ww){
        windowWidth=ww;
    }

    public void setLeftMargin(int lm){
        leftMargin=lm;
    }

    private class MenuInListener implements Animator.AnimatorListener{

        private View informacion;

        public MenuInListener(View info){
            informacion=info;
        }

        @Override
        public void onAnimationStart(Animator animation){}

        @Override
        public void onAnimationEnd(Animator animation){
            YoYo.with(Techniques.FadeIn).playOn(informacion);
        }

        @Override
        public void onAnimationCancel(Animator animation){}

        @Override
        public void onAnimationRepeat(Animator animation){}
    }
}
