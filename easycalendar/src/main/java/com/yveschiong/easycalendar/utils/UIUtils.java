package com.yveschiong.easycalendar.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.support.annotation.DrawableRes;
import android.support.graphics.drawable.VectorDrawableCompat;

public class UIUtils {
    public static Bitmap getBitmapFromVectorFitDimensions(Context context, @DrawableRes int resId, float width, float height) {
        VectorDrawableCompat vectorDrawableCompat = VectorDrawableCompat.create(context.getResources(), resId, context.getTheme());

        String resourceName = context.getResources().getResourceEntryName(resId);
        if (vectorDrawableCompat == null) {
            throw new IllegalArgumentException("Vector resource for " + resourceName + " cannot be created");
        }

        Bitmap bitmap = Bitmap.createBitmap((int) width, (int) height, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        vectorDrawableCompat.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        vectorDrawableCompat.draw(canvas);
        return bitmap;
    }
}
