package com.dev.kylesmith.wakeup.controller;

import android.app.Activity;
import android.os.Bundle;

import com.dev.kylesmith.wakeup.R;
import com.facebook.FacebookSdk;

/**
 * Created by kylesmith on 7/12/15.
 */
public class SignupController extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_signup);
    }
}
