package com.svalero.supermarketscan.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;

public class ImageUtils {
    public static byte[] fromImageViewToByteArray(ImageView imageView) {
        imageView.buildDrawingCache();
        Bitmap bitmap = imageView.getDrawingCache();
        return bitmapToByteArray(bitmap);
    }

    public static Bitmap getBitmapFromByte(byte[] bytes) {
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    public static String encodeImageToBase64(byte[] imageBytes) {
        return Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }

    public static Bitmap getBitmapFromBase64(String base64String) {
        byte[] decodedString = Base64.decode(base64String, Base64.DEFAULT);
        Bitmap decodedByte = getBitmapFromByte(decodedString);
        return decodedByte;
    }

    public static byte[] decodeImageFromBase64(String base64String) {
        return Base64.decode(base64String, Base64.DEFAULT);
    }

    public static byte[] bitmapToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }
}
