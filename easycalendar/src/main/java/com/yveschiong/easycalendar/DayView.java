package com.yveschiong.easycalendar;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import java.util.concurrent.TimeUnit;

public class DayView extends View {

    private int minWidth;
    private int minHeight;
    private int rowHeight;

    private Paint backgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    public DayView(Context context) {
        super(context);
        init(context, null);
    }

    public DayView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public DayView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public DayView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        handleAttributes(context, attrs);
    }

    private void handleAttributes(Context context, AttributeSet attrs) {
        // Set default dimensions
        minWidth = context.getResources().getDimensionPixelSize(R.dimen.defaultMinWidth);
        minHeight = context.getResources().getDimensionPixelSize(R.dimen.defaultMinHeight);
        rowHeight = context.getResources().getDimensionPixelSize(R.dimen.defaultRowHeight);
        backgroundPaint.setColor(ContextCompat.getColor(context, R.color.defaultBackgroundColor));

        if (attrs == null) {
            return;
        }

        // Set styled attributes
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.DayView);
        try {
            minWidth = typedArray.getDimensionPixelSize(R.styleable.DayView_minWidth, minWidth);
            minHeight = typedArray.getDimensionPixelSize(R.styleable.DayView_minHeight, minHeight);
            rowHeight = typedArray.getDimensionPixelSize(R.styleable.DayView_rowHeight, rowHeight);
            backgroundPaint.setColor(typedArray.getDimensionPixelSize(R.styleable.DayView_backgroundColor, backgroundPaint.getColor()));
        } finally {
            typedArray.recycle();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        // Get size requested and size mode
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);

        int width;

        // Only determine width
        switch (widthMode) {
            case MeasureSpec.EXACTLY:
                width = widthSize;
                break;
            case MeasureSpec.AT_MOST:
                width = Math.max(minWidth, widthSize);
                break;
            case MeasureSpec.UNSPECIFIED:
            default:
                width = minWidth;
                break;
        }

        // Set height as row height multiplied by 24 hours to represent this view's height while maintaining a minimum height
        setMeasuredDimension(width, Math.max((int) (rowHeight * TimeUnit.DAYS.toHours(1)), minHeight));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(backgroundPaint.getColor());
    }
}
