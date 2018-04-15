package com.yveschiong.easycalendar.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import com.yveschiong.easycalendar.R;
import com.yveschiong.easycalendar.models.Event;
import com.yveschiong.easycalendar.utils.CalendarUtils;
import com.yveschiong.easycalendar.utils.ResourceUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class DayView extends View {

    private static final int HOURS = (int) TimeUnit.DAYS.toHours(1);
    private static final int MINUTES = (int) TimeUnit.HOURS.toMinutes(1);

    private SimpleDateFormat dateFormat = new SimpleDateFormat("h aa", Locale.getDefault());

    private Paint backgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint dividerLinesPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint timeBlockPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint timeBlockTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint eventPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private int rowHeight;

    private int dividerLinesPadding;
    private int dividerLinesStrokeWidth;

    private int timeBlockWidth;
    private int defaultTimeBlockWidth;
    private float timeBlockScale;
    private float defaultTimeBlockScale;
    private Rect timeBlockTextBounds = new Rect();

    private RectF[] rowBounds = new RectF[HOURS];

    private Calendar day = CalendarUtils.createCalendar();

    private List<Event> events = new ArrayList<>();
    private Map<Event, RectF> eventsBounds = new HashMap<>();
    private int eventsPadding;

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

        // Initialize with empty values
        for (int i = 0; i < rowBounds.length; i++) {
            rowBounds[i] = new RectF();
        }
    }

    private void handleAttributes(Context context, AttributeSet attrs) {
        // Set default dimensions
        backgroundPaint.setColor(ContextCompat.getColor(context, R.color.defaultBackgroundColor));
        dividerLinesPaint.setColor(ContextCompat.getColor(context, R.color.defaultDividerLinesColor));
        timeBlockPaint.setColor(ContextCompat.getColor(context, R.color.defaultTimeBlockColor));
        timeBlockTextPaint.setColor(ContextCompat.getColor(context, R.color.defaultTimeBlockTextColor));
        timeBlockTextPaint.setTextSize(context.getResources().getDimensionPixelSize(R.dimen.defaultTimeBlockTextSize));
        eventPaint.setColor(ContextCompat.getColor(context, R.color.defaultEventColor));

        rowHeight = context.getResources().getDimensionPixelSize(R.dimen.defaultRowHeight);

        dividerLinesPadding = context.getResources().getDimensionPixelSize(R.dimen.defaultDividerLinesPadding);
        dividerLinesStrokeWidth = context.getResources().getDimensionPixelSize(R.dimen.defaultDividerLinesStrokeWidth);

        defaultTimeBlockWidth = context.getResources().getDimensionPixelSize(R.dimen.defaultTimeBlockWidth);
        timeBlockWidth = defaultTimeBlockWidth;
        defaultTimeBlockScale = ResourceUtils.getFloatDimension(context, R.dimen.defaultTimeBlockScale);
        timeBlockScale = defaultTimeBlockScale;

        eventsPadding = context.getResources().getDimensionPixelSize(R.dimen.defaultEventsPadding);

        if (attrs == null) {
            return;
        }

        // Set styled attributes
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.DayView);
        try {
            backgroundPaint.setColor(typedArray.getColor(R.styleable.DayView_backgroundColor, backgroundPaint.getColor()));
            dividerLinesPaint.setColor(typedArray.getColor(R.styleable.DayView_dividerLinesColor, dividerLinesPaint.getColor()));
            timeBlockPaint.setColor(typedArray.getColor(R.styleable.DayView_timeBlockColor, timeBlockPaint.getColor()));
            timeBlockTextPaint.setColor(typedArray.getColor(R.styleable.DayView_timeBlockTextColor, timeBlockTextPaint.getColor()));
            timeBlockTextPaint.setTextSize(typedArray.getDimension(R.styleable.DayView_timeBlockTextSize, timeBlockTextPaint.getTextSize()));
            eventPaint.setColor(typedArray.getColor(R.styleable.DayView_eventsColor, eventPaint.getColor()));

            rowHeight = typedArray.getDimensionPixelSize(R.styleable.DayView_rowHeight, rowHeight);

            dividerLinesPadding = typedArray.getDimensionPixelSize(R.styleable.DayView_dividerLinesPadding, dividerLinesPadding);
            dividerLinesStrokeWidth = typedArray.getDimensionPixelSize(R.styleable.DayView_dividerLinesStrokeWidth, dividerLinesStrokeWidth);

            timeBlockWidth = typedArray.getDimensionPixelSize(R.styleable.DayView_timeBlockWidth, timeBlockWidth);
            timeBlockScale = typedArray.getFloat(R.styleable.DayView_timeBlockScale, timeBlockScale);

            eventsPadding = typedArray.getDimensionPixelSize(R.styleable.DayView_eventsPadding, eventsPadding);
        } finally {
            typedArray.recycle();
        }
    }

    public SimpleDateFormat getDateFormat() {
        return dateFormat;
    }

    public void setDateFormat(SimpleDateFormat dateFormat) {
        this.dateFormat = dateFormat;
    }

    @ColorInt
    public int getBackgroundColor() {
        return backgroundPaint.getColor();
    }

    public void setBackgroundPaint(@ColorInt int color) {
        backgroundPaint.setColor(color);
        invalidate();
    }

    @ColorInt
    public int getDividerLinesColor() {
        return dividerLinesPaint.getColor();
    }

    public void setDividerLinesColor(@ColorInt int color) {
        dividerLinesPaint.setColor(color);
        invalidate();
    }

    @ColorInt
    public int getTimeBlockColor() {
        return timeBlockPaint.getColor();
    }

    public void setTimeBlockColor(@ColorInt int color) {
        timeBlockPaint.setColor(color);
        invalidate();
    }

    @ColorInt
    public int getTimeBlockTextColor() {
        return timeBlockTextPaint.getColor();
    }

    public void setTimeBlockTextColor(@ColorInt int color) {
        timeBlockTextPaint.setColor(color);
        invalidate();
    }

    public float getTimeBlockTextSize() {
        return timeBlockTextPaint.getTextSize();
    }

    public void setTimeBlockTextSize(float timeBlockTextSize) {
        timeBlockTextPaint.setTextSize(timeBlockTextSize);
        requestLayout();
    }

    @ColorInt
    public int getEventColor() {
        return eventPaint.getColor();
    }

    public void setEventColor(@ColorInt int color) {
        eventPaint.setColor(color);
        invalidate();
    }

    public int getRowHeight() {
        return rowHeight;
    }

    public void setRowHeight(int rowHeight) {
        this.rowHeight = rowHeight;
        requestLayout();
    }

    public int getDividerLinesPadding() {
        return dividerLinesPadding;
    }

    public void setDividerLinesPadding(int dividerLinesPadding) {
        this.dividerLinesPadding = dividerLinesPadding;
        requestLayout();
    }

    public int getDividerLinesStrokeWidth() {
        return dividerLinesStrokeWidth;
    }

    public void setDividerLinesStrokeWidth(int dividerLinesStrokeWidth) {
        this.dividerLinesStrokeWidth = dividerLinesStrokeWidth;
        requestLayout();
    }

    public int getTimeBlockWidth() {
        return timeBlockWidth;
    }

    public void setTimeBlockWidth(int timeBlockWidth) {
        this.timeBlockWidth = timeBlockWidth;

        // Reset the scaling since this is a manual definition of the width
        timeBlockScale = defaultTimeBlockScale;

        requestLayout();
    }

    public float getTimeBlockScale() {
        return timeBlockScale;
    }

    public void setTimeBlockScale(float timeBlockScale) {
        this.timeBlockScale = timeBlockScale;
        requestLayout();
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
        refreshEventBounds();
        requestLayout();
    }

    public void addEvent(Event event) {
        events.add(event);
        updateEventBounds(event);
        requestLayout();
    }

    public void removeEvent(Event event) {
        events.remove(event);
        eventsBounds.remove(event);
        requestLayout();
    }

    public void clearEvents() {
        events.clear();
        eventsBounds.clear();
        requestLayout();
    }

    public int getEventsPadding() {
        return eventsPadding;
    }

    public void setEventsPadding(int eventsPadding) {
        this.eventsPadding = eventsPadding;
        requestLayout();
    }

    private void refreshEventBounds() {
        eventsBounds.clear();

        if (events == null || events.isEmpty()) {
            // Do not init event bounds for empty event list
            return;
        }

        for (Event event : events) {
            updateEventBounds(event);
        }
    }

    private void updateEventBounds(Event event) {
        if (event.getStartCalendar() == null || event.getEndCalendar() == null) {
            // Do not update event bounds for an invalid event
            return;
        }

        // Set an empty object so we know to update it in the measure step
        eventsBounds.put(event, new RectF());
    }

    private float getCalendarRowY(Calendar calendar) {
        return (calendar.get(Calendar.HOUR_OF_DAY) + ((float) calendar.get(Calendar.MINUTE) / MINUTES)) * rowHeight;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);

        // Set up the bounds for each row
        for (int i = 0; i < rowBounds.length; i++) {
            rowBounds[i].left = 0;
            rowBounds[i].top = i * rowHeight;
            rowBounds[i].right = width;
            rowBounds[i].bottom = (i + 1) * rowHeight;
        }

        // Time block scaling overrides manual width definitions
        if (timeBlockWidth == defaultTimeBlockWidth || timeBlockScale != defaultTimeBlockScale) {
            timeBlockWidth = (int) (timeBlockScale * width);
        }

        for (Map.Entry<Event, RectF> entry : eventsBounds.entrySet()) {
            entry.getValue().set(
                    timeBlockWidth + eventsPadding,
                    getCalendarRowY(entry.getKey().getStartCalendar()),
                    width - eventsPadding,
                    getCalendarRowY(entry.getKey().getEndCalendar())
            );
        }

        // Set height as row height multiplied by 24 hours to represent this view's height
        setMeasuredDimension(width, rowHeight * HOURS);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // Draw the background color
        canvas.drawColor(backgroundPaint.getColor());

        // Draw the side time block background
        canvas.drawRect(0, 0, timeBlockWidth, getMeasuredHeight(), timeBlockPaint);

        for (int i = 1; i < rowBounds.length; i++) {
            // Draw the dividers
            canvas.drawRect(
                    rowBounds[i].left + dividerLinesPadding + timeBlockWidth,
                    rowBounds[i].top,
                    rowBounds[i].right - dividerLinesPadding,
                    rowBounds[i].top + dividerLinesStrokeWidth,
                    dividerLinesPaint
            );

            day.set(Calendar.HOUR_OF_DAY, i);
            String hourText = dateFormat.format(day.getTime());
            timeBlockTextPaint.getTextBounds(hourText, 0, hourText.length(), timeBlockTextBounds);

            // We need to take into consideration the text bounds and the left and bottom displacement,
            // refer to https://stackoverflow.com/questions/11120392/android-center-text-on-canvas
            // This is why we subtract the text bounds left and bottom and should only be considered when drawing text
            // The text is also drawn from the baseline for the y-coordinate so we also need to consider that
            canvas.drawText(
                    hourText,
                    rowBounds[i].left + (timeBlockWidth - timeBlockTextBounds.width()) * 0.5f - timeBlockTextBounds.left,
                    rowBounds[i].top + (dividerLinesStrokeWidth + timeBlockTextBounds.height()) * 0.5f - timeBlockTextBounds.bottom,
                    timeBlockTextPaint
            );
        }

        for (RectF bounds : eventsBounds.values()) {
            canvas.drawRect(bounds, eventPaint);
        }
    }
}
