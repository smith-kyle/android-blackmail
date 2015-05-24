package com.dev.kylesmith.wakeup.model.Converters;

import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;

/**
 * Created by kylesmith on 1/16/15.
 */
public class BitmapToByteArray {
    public byte[] Convert(Bitmap bitmap){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }
}
