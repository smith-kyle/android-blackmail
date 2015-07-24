package com.dev.kylesmith.wakeup.controller;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.dev.kylesmith.wakeup.R;
import com.dev.kylesmith.wakeup.model.Constants;
import com.dev.kylesmith.wakeup.model.DB.DBAccessor;
import com.dev.kylesmith.wakeup.util.Convert;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by kylesmith on 1/24/15.
 */
public class SettingsController extends Activity{
    DBAccessor dbAccessor = new DBAccessor(this);
    HashMap<String, Object> userInfo = new HashMap<>();
    @Bind(R.id.settings_phone_num) TextView phoneNumTextView;
    @Bind(R.id.settings_photo) ImageView photoImageView;
    @Bind(R.id.back_btn) ImageButton backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        userInfo = dbAccessor.getUserInfo();
        phoneNumTextView.setText(userInfo.get(Constants.MAPKEY_PHONE_NUM).toString());
        photoImageView.setImageBitmap(Convert.uriToBitmap(Uri.parse(userInfo.get(Constants.MAPKEY_PHOTO_URI).toString()), this));
    }

    @OnClick(R.id.back_btn)
    public void submit(){
        onBackPressed();
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
    }
}
