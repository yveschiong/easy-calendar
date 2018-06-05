package com.yveschiong.easycalendar.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.yveschiong.easycalendar.R;
import com.yveschiong.easycalendar.models.CalendarRange;
import com.yveschiong.easycalendar.utils.CalendarUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MonthView extends View {

    private Paint backgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private TextPaint yearTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
    private TextPaint monthTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
    private TextPaint weekdayTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
    private TextPaint dayTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);

    private Rect yearTextBounds = new Rect();
    private Rect monthTextBounds = new Rect();

    private int monthYearPadding;
    private int headerPadding;

    private CalendarRange monthRange = CalendarUtils.createCalendarMonthRange();

    public MonthView(Context context) {
        super(context);
        init(context, null);
    }

    public MonthView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public MonthView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MonthView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        handleAttributes(context, attrs);
    }

    private void handleAttributes(Context context, AttributeSet attrs) {
        // Set default dimensions
        backgroundPaint.setColor(ContextCompat.getColor(context, R.color.monthViewDefaultBackgroundColor));

        yearTextPaint.setColor(ContextCompat.getColor(context, R.color.monthViewDefaultYearTextColor));
        yearTextPaint.setTextSize(context.getResources().getDimensionPixelSize(R.dimen.monthViewDefaultYearTextSize));

        monthTextPaint.setColor(ContextCompat.getColor(context, R.color.monthViewDefaultMonthTextColor));
        monthTextPaint.setTextSize(context.getResources().getDimensionPixelSize(R.dimen.monthViewDefaultMonthTextSize));

        weekdayTextPaint.setColor(ContextCompat.getColor(context, R.color.monthViewDefaultWeekdayTextColor));
        weekdayTextPaint.setTextSize(context.getResources().getDimensionPixelSize(R.dimen.monthViewDefaultWeekdayTextSize));

        dayTextPaint.setColor(ContextCompat.getColor(context, R.color.monthViewDefaultDayTextColor));
        dayTextPaint.setTextSize(context.getResources().getDimensionPixelSize(R.dimen.monthViewDefaultDayTextSize));

        monthYearPadding = context.getResources().getDimensionPixelSize(R.dimen.monthViewDefaultMonthYearPadding);
        headerPadding = context.getResources().getDimensionPixelSize(R.dimen.monthViewDefaultHeaderPadding);

        if (attrs == null) {
            return;
        }

        // Set styled attributes
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.MonthView);
        try {
            backgroundPaint.setColor(typedArray.getColor(R.styleable.MonthView_backgroundColor, backgroundPaint.getColor()));

            yearTextPaint.setColor(typedArray.getColor(R.styleable.MonthView_yearTextColor, yearTextPaint.getColor()));
            yearTextPaint.setTextSize(typedArray.getDimension(R.styleable.MonthView_yearTextSize, yearTextPaint.getTextSize()));

            monthTextPaint.setColor(typedArray.getColor(R.styleable.MonthView_monthTextColor, monthTextPaint.getColor()));
            monthTextPaint.setTextSize(typedArray.getDimension(R.styleable.MonthView_monthTextSize, monthTextPaint.getTextSize()));

            weekdayTextPaint.setColor(typedArray.getColor(R.styleable.MonthView_weekdayTextColor, weekdayTextPaint.getColor()));
            weekdayTextPaint.setTextSize(typedArray.getDimension(R.styleable.MonthView_weekdayTextSize, weekdayTextPaint.getTextSize()));

            dayTextPaint.setColor(typedArray.getColor(R.styleable.MonthView_dayTextColor, dayTextPaint.getColor()));
            dayTextPaint.setTextSize(typedArray.getDimension(R.styleable.MonthView_dayTextSize, dayTextPaint.getTextSize()));

            monthYearPadding = typedArray.getDimensionPixelSize(R.styleable.MonthView_monthYearPadding, monthYearPadding);
            headerPadding = typedArray.getDimensionPixelSize(R.styleable.MonthView_headerPadding, headerPadding);
        } finally {
            typedArray.recycle();
        }
    }

    // region getters and setters
    @ColorInt
    public int getBackgroundColor() {
        return backgroundPaint.getColor();
    }

    public void setBackgroundPaint(@ColorInt int color) {
        backgroundPaint.setColor(color);
        invalidate();
    }

    @ColorInt
    public int getYearTextColor() {
        return yearTextPaint.getColor();
    }

    public void setYearTextColor(@ColorInt int color) {
        yearTextPaint.setColor(color);
        invalidate();
    }

    public float getYearTextSize() {
        return yearTextPaint.getTextSize();
    }

    public void setYearTextSize(float yearTextSize) {
        yearTextPaint.setTextSize(yearTextSize);
        refresh();
    }

    @ColorInt
    public int getMonthTextColor() {
        return monthTextPaint.getColor();
    }

    public void setMonthTextColor(@ColorInt int color) {
        monthTextPaint.setColor(color);
        invalidate();
    }

    public float getMonthTextSize() {
        return monthTextPaint.getTextSize();
    }

    public void setMonthTextSize(float monthTextSize) {
        monthTextPaint.setTextSize(monthTextSize);
        refresh();
    }

    @ColorInt
    public int getWeekdayTextColor() {
        return weekdayTextPaint.getColor();
    }

    public void setWeekdayTextColor(@ColorInt int color) {
        weekdayTextPaint.setColor(color);
        invalidate();
    }

    public float getWeekdayTextSize() {
        return weekdayTextPaint.getTextSize();
    }

    public void setWeekdayTextSize(float weekdayTextSize) {
        weekdayTextPaint.setTextSize(weekdayTextSize);
        refresh();
    }

    @ColorInt
    public int getDayTextColor() {
        return dayTextPaint.getColor();
    }

    public void setDayTextColor(@ColorInt int color) {
        dayTextPaint.setColor(color);
        invalidate();
    }

    public float getDayTextSize() {
        return dayTextPaint.getTextSize();
    }

    public void setDayTextSize(float dayTextSize) {
        dayTextPaint.setTextSize(dayTextSize);
        refresh();
    }

    public int getMonthYearPadding() {
        return monthYearPadding;
    }

    public void setMonthYearPadding(int monthYearPadding) {
        this.monthYearPadding = monthYearPadding;
        refresh();
    }

    public int getHeaderPadding() {
        return headerPadding;
    }

    public void setHeaderPadding(int headerPadding) {
        this.headerPadding = headerPadding;
        refresh();
    }

    public CalendarRange getMonth() {
        return monthRange;
    }

    public void setMonth(Calendar day) {
        monthRange = CalendarUtils.createCalendarMonthRange(day);
        refresh();
    }

    public void setMonth(Date day) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(day);
        setMonth(calendar);
    }
    // endregion

    // region helper methods
    private void refresh() {
        requestLayout();
        invalidate();
    }
    // endregion

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        String yearText = String.valueOf(monthRange.getStart().get(Calendar.YEAR));
        String monthText = monthRange.getStart().getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());

        monthTextPaint.getTextBounds(monthText, 0, monthText.length(), monthTextBounds);

        // We use this to properly place the year underneath the month due to tails in text (like July)
        int bottom = monthTextBounds.bottom;

        // This sets the bounds ready to draw onto the canvas with just the left and top values
        monthTextBounds.offsetTo(
                (int) ((width - monthTextBounds.width()) * 0.5f - monthTextBounds.left),
                headerPadding + monthTextBounds.height() - bottom
        );

        yearTextPaint.getTextBounds(yearText, 0, yearText.length(), yearTextBounds);

        // This sets the bounds ready to draw onto the canvas with just the left and top values
        yearTextBounds.offsetTo(
                (int) ((width - yearTextBounds.width()) * 0.5f - yearTextBounds.left),
                monthTextBounds.top + bottom + yearTextBounds.height() - yearTextBounds.bottom + monthYearPadding
        );

        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // Draw the background color
        canvas.drawColor(backgroundPaint.getColor());

        // Draw the month text
        canvas.drawText(monthRange.getStart().getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault()), monthTextBounds.left, monthTextBounds.top, monthTextPaint);

        // Draw the year text
        canvas.drawText(String.valueOf(monthRange.getStart().get(Calendar.YEAR)), yearTextBounds.left, yearTextBounds.top, yearTextPaint);
    }
}
