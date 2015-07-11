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
import com.dev.kylesmith.wakeup.model.Converters.UriToBitmap;
import com.dev.kylesmith.wakeup.model.DB.DBAccessor;

import java.util.HashMap;

/**
 * Created by kylesmith on 1/24/15.
 */
public class SettingsController extends Activity{
    DBAccessor dbAccessor = new DBAccessor(this);
    HashMap<String, Object> userInfo = new HashMap<>();
    TextView phoneNumTextView;
    ImageView photoImageView;
    ImageButton backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        userInfo = dbAccessor.getUserInfo();
        phoneNumTextView = (TextView)findViewById(R.id.settings_phone_num);
        phoneNumTextView.setText(userInfo.get(Constants.MAPKEY_PHONE_NUM).toString());
        photoImageView = (ImageView)findViewById(R.id.settings_photo);
        photoImageView.setImageBitmap(UriToBitmap.Convert(Uri.parse(userInfo.get(Constants.MAPKEY_PHOTO_URI).toString()), this));
        backButton = (ImageButton)findViewById(R.id.back_btn);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
    }
}
