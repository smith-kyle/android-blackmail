package com.dev.kylesmith.wakeup.controller;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.dev.kylesmith.wakeup.R;
import com.dev.kylesmith.wakeup.model.Constants;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by kylesmith on 7/8/15.
 */
public class IntroController extends Activity {
    @Bind(R.id.signupInstructionsLi1) LinearLayout mInstruction1;
    @Bind(R.id.signupInstructionsLi2) LinearLayout mInstruction2;
    @Bind(R.id.signupInstructionsLi3) LinearLayout mInstruction3;
    @Bind(R.id.buttons_container) LinearLayout mButtonsContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);

        setLayout();
        ButterKnife.bind(this);

        startIntroAnimation();
    }

    private void startIntroAnimation(){
        int fadeInDuration = 500;
        int startDelay = 300;

        ValueAnimator fadeIn1 = ObjectAnimator.ofFloat(mInstruction1, "alpha", 0f, 1f);
        fadeIn1.setDuration(fadeInDuration);

        ValueAnimator fadeIn2 = ObjectAnimator.ofFloat(mInstruction2, "alpha", 0f, 1f);
        fadeIn2.setDuration(fadeInDuration);
        fadeIn2.setStartDelay(startDelay);

        ValueAnimator fadeIn3 = ObjectAnimator.ofFloat(mInstruction3, "alpha", 0f, 1f);
        fadeIn3.setDuration(fadeInDuration);
        fadeIn3.setStartDelay(startDelay);

        ValueAnimator fadeIn4 = ObjectAnimator.ofFloat(mButtonsContainer, "alpha", 0f, 1f);
        fadeIn4.setDuration(fadeInDuration);
        fadeIn4.setStartDelay(startDelay);

        AnimatorSet sequentialFadeIn = new AnimatorSet();
        sequentialFadeIn.play(fadeIn1).before(fadeIn2);
        sequentialFadeIn.play(fadeIn2).before(fadeIn3);
        sequentialFadeIn.play(fadeIn3).before(fadeIn4);

        sequentialFadeIn.setStartDelay(1000);

        sequentialFadeIn.start();
    }


    private void setLayout(){
        if (false) {
            Intent intent = new Intent(this, ScheduleController.class);
            startActivity(intent);
        }
        else{
            setContentView(R.layout.activity_signup);
        }
    }


    private boolean isLoggedIn(){
        SharedPreferences settings = getSharedPreferences(Constants.PREFS_NAME, 0);
        return settings.getBoolean(Constants.PREFS_FIRST_TIME, true);
    }
}
