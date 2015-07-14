package com.dev.kylesmith.wakeup.controller;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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
public class SignupController extends Activity {
    @Bind(R.id.fb_login_button) LoginButton FBLoginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_signup);

        ButterKnife.bind(this);
        CallbackManager callbackManager = FacebookCallbackManager.Factory.create();

        FBLoginBtn.setPublishPermissions(new String[]{"publish_actions"});

        FBLoginBtn.registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        for(String gp : loginResult.getRecentlyGrantedPermissions())
                            Log.i("GRANTED PERMISSIONS" , gp.toString());
                        for(String dp : loginResult.getRecentlyDeniedPermissions())
                            Log.i("GRANTED PERMISSIONS" , dp.toString());

                    }

                    @Override
                    public void onCancel() {
                        Log.i("FACEBOOK LOGIN", "Login Canceled");
                    }

                    @Override
                    public void onError(FacebookException e) {
                        Log.e("FACEBOOK LOGIN", e.toString());
                    }
                });
    }

    private class FacebookCallbackManager implements CallbackManager{
        @Override
        public boolean onActivityResult(int i, int i2, Intent intent) {
            Log.i("FACEBOOK LOGIN" , "WE GOT SOMETHING!");
            return false;
        }
    }
}
