package com.dev.kylesmith.wakeup.controller;

import android.app.Activity;
import android.content.Intent;
import android.media.tv.TvInputService;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import com.dev.kylesmith.wakeup.R;
import com.dev.kylesmith.wakeup.model.ServerInterface.ApiConstants;
import com.dev.kylesmith.wakeup.model.ServerInterface.RestClient;
import com.dev.kylesmith.wakeup.model.ServerInterface.SubscriptionResponse;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by kylesmith on 7/12/15.
 */
public class SignupController extends FragmentActivity {
    private LoginButton FBLoginBtn;
    private CallbackManager callbackManager;
    private RestClient restClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_signup);
        restClient = new RestClient();
        callbackManager = CallbackManager.Factory.create();
        LoginButton loginButton = (LoginButton) findViewById(R.id.fb_login_button);
        loginButton.setPublishPermissions("publish_actions");
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                if(!loginResult.getRecentlyDeniedPermissions().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "We need those permissions though", Toast.LENGTH_SHORT).show();
                }
                else {
                    restClient.get().addUser(ApiConstants.PASSWORD,
                            ApiConstants.SHARE_TYPE_FB,
                            "first name",
                            "last name",
                            "email",
                            loginResult.getAccessToken().toString(),
                            new SubscriptionCallback());
                }
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
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private class SubscriptionCallback implements Callback<SubscriptionResponse> {
        @Override
        public void success(SubscriptionResponse subscriptionResponse, Response response) {
            int i = 0;
        }

        @Override
        public void failure(RetrofitError error) {
            int i = 12;
        }
    }
}
