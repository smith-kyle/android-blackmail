package com.dev.kylesmith.wakeup.controller;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.dev.kylesmith.wakeup.R;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by kylesmith on 7/12/15.
 */
public class SignupController extends FragmentActivity {
    private LoginButton FBLoginBtn;
    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_signup);
        callbackManager = CallbackManager.Factory.create();
        LoginButton loginButton = (LoginButton) findViewById(R.id.fb_login_button);
        loginButton.setPublishPermissions("publish_actions");
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                LoginResult lr = loginResult;
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException e) {
                Log.e("FACEBOOK ERROR", e.toString());
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
