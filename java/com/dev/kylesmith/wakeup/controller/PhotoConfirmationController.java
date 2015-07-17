package com.dev.kylesmith.wakeup.controller;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.dev.kylesmith.wakeup.R;
import com.dev.kylesmith.wakeup.model.Constants;
import com.dev.kylesmith.wakeup.model.Converters.BitmapToByteArray;
import com.dev.kylesmith.wakeup.model.Converters.UriToBitmap;

import butterknife.Bind;

/**
 * Created by kylesmith on 1/12/15.
 */
public class PhotoConfirmationController extends Activity {
    @Bind(R.id.mailIcon) ImageView photo;
    byte[] img;
    BitmapToByteArray bitmapToByteArray = new BitmapToByteArray();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_displayphoto);

        Uri u = Uri.parse(getIntent().getStringExtra(Constants.EXTRA_PHOTOURI));
        photo.setImageBitmap(UriToBitmap.Convert(u, this));

        animatePhoto();
    }



    private void animatePhoto(){
        Animation a = AnimationUtils.loadAnimation(this, R.anim.zoom);
        a.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                startNextActivity();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        photo.startAnimation(a);
    }


    private void startNextActivity(){
        Intent intent = new Intent(getApplicationContext(), PhoneNumberController.class);
        this.startActivity(intent);
    }

}
