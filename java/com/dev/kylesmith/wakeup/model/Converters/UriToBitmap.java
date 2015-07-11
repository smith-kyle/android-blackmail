package com.dev.kylesmith.wakeup.model.Converters;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;

/**
 * Created by kylesmith on 1/16/15.
 */
public class UriToBitmap {
    public static Bitmap Convert(Uri u, Context context){
        Bitmap bitmap, scaled = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), u);
            int nh = (int) (bitmap.getHeight() * (512.0 / bitmap.getWidth()));
            scaled = Bitmap.createScaledBitmap(bitmap, 512, nh, true);
        }catch (Exception e){
            e.printStackTrace();
        }
        return scaled;
    }
}
