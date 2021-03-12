package com.yveschiong.easycalendar.utils;

import android.content.Context;
import android.util.TypedValue;

import androidx.annotation.DimenRes;

public class ResourceUtils {
    /**
     * Obtains a float from
     * @param context
     * @param id
     * @return
     */
    public static float getFloatDimension(Context context, @DimenRes int id) {
        TypedValue value = new TypedValue();
        context.getResources().getValue(id, value, true);
        return value.getFloat();
    }
}
