package com.example.androidfinal.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Helper class for working with images.
 */
public class ImageHelper {

    private static final String TAG = ImageHelper.class.getSimpleName();

    public static Bitmap uriToBitmap(Uri imageUri, Context context) {
        try {
            final InputStream imageStream = context.getContentResolver().openInputStream(imageUri);
            return BitmapFactory.decodeStream(imageStream);
        } catch (FileNotFoundException err) {
            Log.e(TAG, "Image not found:" + imageUri, err);
            return null;
        }
    }

    public static String encodeImage(Uri imageUri, Context context) {
        try {
            final InputStream imageStream = context.getContentResolver().openInputStream(imageUri);
            return encodeImage(BitmapFactory.decodeStream(imageStream));
        } catch (FileNotFoundException err) {
            Log.e(TAG, "Image not found:" + imageUri, err);
            return null;
        }
    }

    public static String encodeImage(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        String encImage = Base64.encodeToString(b, Base64.DEFAULT);
        return encImage;
    }

    public static Bitmap decodeImage(String encodedImage) {
        byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }

    public static String getImageName() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime()) + ".jpg";
    }

}
