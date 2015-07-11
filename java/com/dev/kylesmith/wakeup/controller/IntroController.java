package com.dev.kylesmith.wakeup.controller;

import android.animation.Animator;
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

/**
 * Created by kylesmith on 7/8/15.
 */
public class IntroController extends Activity {
    private static final String TAG = "Intro";
    private static final int SELECT_PICTURE = 1;
    private ImageView photo;
    private Uri outputFileUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        super.onCreate(savedInstanceState);

        SharedPreferences settings = getSharedPreferences(Constants.PREFS_NAME, 0);
        if (settings.getBoolean(Constants.PREFS_FIRST_TIME, true)) {
            setContentView(R.layout.activity_signup);
            settings.edit().putBoolean(Constants.PREFS_FIRST_TIME, false).commit();
        }
        else{
            Intent intent = new Intent(this, ScheduleController.class);
            startActivity(intent);
        }

        LinearLayout mAddAClass = (LinearLayout) findViewById(R.id.addAClassli);
        ValueAnimator fadeIn1 = ObjectAnimator.ofFloat(mAddAClass, "alpha", 0f, 1f);
        fadeIn1.setDuration(1000);
        fadeIn1.start();
    }
}
