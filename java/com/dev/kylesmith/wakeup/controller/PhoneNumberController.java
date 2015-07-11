package com.dev.kylesmith.wakeup.controller;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Contacts;
import android.provider.ContactsContract;
import android.view.View;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import com.dev.kylesmith.wakeup.R;
import com.dev.kylesmith.wakeup.model.Constants;
import com.dev.kylesmith.wakeup.model.Converters.BitmapToByteArray;
import com.dev.kylesmith.wakeup.model.Converters.UriToBitmap;
import com.dev.kylesmith.wakeup.model.DB.DBAccessor;
import com.dev.kylesmith.wakeup.model.ServerInterface.AddUserTask;
import com.dev.kylesmith.wakeup.model.ServerInterface.AsyncResponse;
import com.google.android.gms.plus.People;

/**
 * Created by kylesmith on 1/13/15.
 */
public class PhoneNumberController extends Activity implements AsyncResponse{
    private static final int PICK_CONTACT_REQUEST = 1;
    AddUserTask addUserTask = new AddUserTask();
    FrameLayout progressBarHolder;
    EditText phone_num;
    ImageButton addNumber;
    String phoneNumber;
    private byte[] photo = null;
    Uri u;
    AlphaAnimation inAnimation, outAnimation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phonenumber);
        addNumber = (ImageButton)findViewById(R.id.add_phone_number_btn);
        inAnimation = new AlphaAnimation(0f, 1f);
        inAnimation.setDuration(200);
        outAnimation = new AlphaAnimation(1f, 0f);
        outAnimation.setDuration(200);

        u = Uri.parse(getIntent().getStringExtra(Constants.EXTRA_PHOTOURI));
        photo = BitmapToByteArray.Convert(UriToBitmap.Convert(Uri.parse(getIntent().getStringExtra(Constants.EXTRA_PHOTOURI)), this));
        addUserTask.delegate = this;
        progressBarHolder = (FrameLayout) findViewById(R.id.progressBarHolder);

    }


    private void Next(){
//        SharedPreferences settings = getSharedPreferences(Constants.PREFS_NAME, 0);
//        settings.edit().putString(Constants.PREFS_PHONE_NUM, phone_num.getText().toString());
        addUserTask.execute(photo, phoneNumber);
        addNumber.setEnabled(false);
        progressBarHolder.setAnimation(inAnimation);
        progressBarHolder.setVisibility(View.VISIBLE);
    }


    @Override
    public void processFinish(Object output) {
        int id = (int) output;
        SharedPreferences settings = getSharedPreferences(Constants.PREFS_NAME, 0);
        settings.edit().putInt(Constants.PREFS_USER_ID, id);

        progressBarHolder.setAnimation(outAnimation);
        progressBarHolder.setVisibility(View.INVISIBLE);

        new DBAccessor(this).insertUserInfo(id, phoneNumber, u);
        addNumber.setEnabled(true);
        Intent intent = new Intent(getApplicationContext(), ScheduleController.class);
        startActivity(intent);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK){
            if(requestCode == PICK_CONTACT_REQUEST){
                Uri contactUri = data.getData();
                // We only need the NUMBER column, because there will be only one row in the result
                String[] projection = {ContactsContract.CommonDataKinds.Phone.NUMBER};

                // Perform the query on the contact to get the NUMBER column
                // We don't need a selection or sort order (there's only one result for the given URI)
                // CAUTION: The query() method should be called from a separate thread to avoid blocking
                // your app's UI thread. (For simplicity of the sample, this code doesn't do that.)
                // Consider using CursorLoader to perform the query.
                Cursor cursor = getContentResolver()
                        .query(contactUri, projection, null, null, null);
                cursor.moveToFirst();

                // Retrieve the phone number from the NUMBER column
                int column = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                phoneNumber = cursor.getString(column);
                Next();
            }
        }
    }

    public void selectPhoneNumber(View view){
        Intent intent = new Intent(Intent.ACTION_PICK, Uri.parse("content://contacts"));
        intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
        startActivityForResult(intent, PICK_CONTACT_REQUEST);
    }

}
