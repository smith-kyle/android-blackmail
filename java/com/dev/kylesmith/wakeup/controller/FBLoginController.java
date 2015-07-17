package com.dev.kylesmith.wakeup.controller;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dev.kylesmith.wakeup.R;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

/**
 * Created by kylesmith on 7/16/15.
 */
public class FBLoginController extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        FacebookSdk.sdkInitialize(getActivity());
        View view = inflater.inflate(R.layout.fragment_fblogin, container, false);
        LoginButton loginButton = (LoginButton) view.findViewById(R.id.fb_login_button);
        loginButton.setPublishPermissions("publish_actions");

        loginButton.setFragment(this);
        CallbackManager callbackManager = CallbackManager.Factory.create();

        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                for(String gp : loginResult.getRecentlyGrantedPermissions())
                    Log.i("GRANTED PERMISSIONS", gp.toString());
                for(String dp : loginResult.getRecentlyDeniedPermissions())
                    Log.i("GRANTED PERMISSIONS" , dp.toString());
            }

            @Override
            public void onCancel() {
                Log.i("FACEBOOK LOGIN", "Login Canceled");
            }

            @Override
            public void onError(FacebookException exception) {
                Log.e("FACEBOOK LOGIN", exception.toString());
            }
        });

        return view;
    }
}
