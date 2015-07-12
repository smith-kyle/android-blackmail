package com.dev.kylesmith.wakeup.controller;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import com.dev.kylesmith.wakeup.R;
import com.dev.kylesmith.wakeup.model.Constants;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class IntroController_old extends Activity {
    private static final String TAG = "Intro";
    private static final int SELECT_PICTURE = 1;
    private ImageView photo;
    private Uri outputFileUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        super.onCreate(savedInstanceState);

        setImgUri();

        SharedPreferences settings = getSharedPreferences(Constants.PREFS_NAME, 0);
        if (settings.getBoolean(Constants.PREFS_IS_LOGGED_IN, true)) {
            setContentView(R.layout.activity_intro_old);
            settings.edit().putBoolean(Constants.PREFS_IS_LOGGED_IN, false).commit();
        }
        else{
            Intent intent = new Intent(this, ScheduleController.class);
            startActivity(intent);
        }

        photo = (ImageView) findViewById(R.id.photoImageView);
    }









    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == SELECT_PICTURE) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                final boolean isCamera;
                if(data == null) isCamera = true;
                else{
                    final String action = data.getAction();
                    if(action == null) isCamera = false;
                    else isCamera = action.equals(MediaStore.ACTION_IMAGE_CAPTURE);
                }
                Uri selectedImageUri;
                if(isCamera) selectedImageUri = outputFileUri;
                else selectedImageUri = data == null? null : data.getData();
                Intent intent = new Intent(this, PhoneNumberController.class);
                if(selectedImageUri != null)
                    intent.putExtra(Constants.EXTRA_PHOTOURI, selectedImageUri.toString());
                startActivity(intent);
            }
        }
    }







    public void takeOrSelectPhoto(View view){
        final List<Intent> cameraIntents = new ArrayList<Intent>();
        final Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        final PackageManager packageManager = getPackageManager();
        final List<ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent, 0);
        for(ResolveInfo res : listCam){
            final String packageName = res.activityInfo.packageName;
            final Intent intent = new Intent(captureIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(packageName);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            cameraIntents.add(intent);
        }

        Intent pickIntent = new Intent();
        pickIntent.setType("image/*");
        pickIntent.setAction(Intent.ACTION_GET_CONTENT);

        String pickTitle = "Select or take a new photo"; // Or get from strings.xml
        Intent chooserIntent = Intent.createChooser(pickIntent, pickTitle);
        chooserIntent.putExtra
                (
                        Intent.EXTRA_INITIAL_INTENTS,
                        cameraIntents.toArray(new Parcelable[]{}));

        startActivityForResult(chooserIntent, SELECT_PICTURE);
    }







    private void setImgUri(){
        final File root = new File(Environment.getExternalStorageDirectory() + File.separator + "Blackmail" + File.separator);
        root.mkdirs();
        final String fname = "EmbarrassingPhoto"; // might have to add an extension on that
        final File sdImageMainDirectory = new File(root, fname);
        outputFileUri = Uri.fromFile(sdImageMainDirectory);
    }
}
