package com.dev.kylesmith.wakeup.controller;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.dev.kylesmith.wakeup.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by kylesmith on 1/24/15.
 */
public class AboutController extends Activity {
    @Bind(R.id.back_btn) ImageButton backButton;
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ButterKnife.bind(this);

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
