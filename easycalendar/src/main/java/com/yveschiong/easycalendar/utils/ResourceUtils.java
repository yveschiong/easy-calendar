package com.yveschiong.easycalendar.utils;

import android.content.Context;
import android.support.annotation.DimenRes;
import android.util.TypedValue;

public class ResourceUtils {

    public static float getFloatDimensionPixelSize(Context context, @DimenRes int id) {
        TypedValue value = new TypedValue();
        context.getResources().getValue(id, value, true);
        return value.getFloat();
    }
}
